/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.dao.impl.SmsDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.MessageConfigService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.util.SmsClient;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.service.MessageConfigService;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.emay.sdk.client.api.Client;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("smsServiceImpl")
public class SmsServiceImpl extends BaseServiceImpl<Sms , Long> implements SmsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SmsDao smsDao ;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource(name = "messageConfigServiceImpl")
	private MessageConfigService messageConfigService;

	private void addSendTask(final String[] mobiles, final String content, final Date sendTime) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(mobiles, content, sendTime);
			}
		});
	}

	private void send(String[] mobiles, String content, Date sendTime) {
		Assert.notEmpty(mobiles);
		Assert.hasText(content);

		Setting setting = SystemUtils.getSetting();
		String smsSn = setting.getSmsSn();
		String smsKey = setting.getSmsKey();
		if (StringUtils.isEmpty(smsSn) || StringUtils.isEmpty(smsKey)) {
			return;
		}
		try {
			Client client = new Client(smsSn, smsKey);
			if (sendTime != null) {
				client.sendScheduledSMS(mobiles, content, DateFormatUtils.format(sendTime, "yyyyMMddhhmmss"));
			} else {
				client.sendSMS(mobiles, content, 5);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String[] mobiles, String content, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles);
		Assert.hasText(content);

		if (async) {
			addSendTask(mobiles, content, sendTime);
		} else {
			send(mobiles, content, sendTime);
		}
	}

	public void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles);
		Assert.hasText(templatePath);

		try {
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate(templatePath);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			send(mobiles, content, sendTime, async);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String mobile, String content) {
		Assert.hasText(mobile);
		Assert.hasText(content);

		send(new String[] { mobile }, content, null, true);
	}

	public void send(String mobile, String templatePath, Map<String, Object> model) {
		Assert.hasText(mobile);
		Assert.hasText(templatePath);

		send(new String[] { mobile }, templatePath, model, null, true);
	}

	public void sendRegisterMemberSms(Member member) {
		if (member == null || StringUtils.isEmpty(member.getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.registerMember);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("member", member);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerMemberSms");
		send(member.getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendCreateOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.createOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("createOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendUpdateOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.updateOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("updateOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendCancelOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.cancelOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("cancelOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReviewOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.reviewOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("reviewOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendPaymentOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.paymentOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("paymentOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendRefundsOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.refundsOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("refundsOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendShippingOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.shippingOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("shippingOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReturnsOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.returnsOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("returnsOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReceiveOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.receiveOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("receiveOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendCompleteOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.completeOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("completeOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendFailOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(MessageConfig.Type.failOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failOrderSms");
		send(order.getMember().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public long getBalance() {
		Setting setting = SystemUtils.getSetting();
		String smsSn = setting.getSmsSn();
		String smsKey = setting.getSmsKey();
		if (StringUtils.isEmpty(smsSn) || StringUtils.isEmpty(smsKey)) {
			return -1L;
		}
		try {
			Client client = new Client(smsSn, smsKey);
			double result = client.getBalance();
			if (result >= 0) {
				return (long) (result * 10);
			}
		} catch (Exception e) {
		}
		return -1L;
	}

	@Override
	public boolean sendSms(String mobile, String content) {
		try {

			SmsClient client=new SmsClient();
			String result_mt = client.mdsmssend(mobile, URLEncoder.encode(content , "utf-8"), "", "", "", "");
			logger.info("sms/sendSms:phone="+mobile+"&content"+content+"&result_mt"+result_mt);
			return true;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false ;
		}
	}

	@Override
	public Sms findSms(Sms sms) {
		return smsDao.findSms(sms);
	}

	@Override
	public Long countSms(Sms sms , boolean isToday) {
		return smsDao.countSms(sms , isToday);
	}

	@Override
	public boolean isExpired(Sms sms, Long expireTime) {
		return false;
	}

	@Override
	public boolean isOver(Long totalCount, Long maxCount) {
		return false;
	}

	@Override
	public boolean sendContent(String mobile, String content) {
		try {
			SmsClient client=new SmsClient();
			String result_mt = client.mdsmssend(mobile, URLEncoder.encode(content , "utf-8"), "", "", "", "");
			logger.info("sms/sendContent:phone="+mobile+"&content"+content+"&result_mt"+result_mt);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false ;
		}
	}
}