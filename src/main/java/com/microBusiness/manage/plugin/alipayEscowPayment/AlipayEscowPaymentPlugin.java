/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.plugin.alipayEscowPayment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.PaymentLog;
import com.microBusiness.manage.entity.PluginConfig;
import com.microBusiness.manage.plugin.PaymentPlugin;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("alipayEscowPaymentPlugin")
public class AlipayEscowPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "支付宝(担保交易)";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "utlz";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.dreamforyou";
	}

	@Override
	public String getInstallUrl() {
		return "alipay_escow_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "alipay_escow_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "alipay_escow_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://mapi.alipay.com/gateway.do";
	}

	@Override
	public PaymentPlugin.RequestMethod getRequestMethod() {
		return PaymentPlugin.RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		PluginConfig pluginConfig = getPluginConfig();
		PaymentLog paymentLog = getPaymentLog(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("service", "create_partner_trade_by_buyer");
		parameterMap.put("partner", pluginConfig.getAttribute("partner"));
		parameterMap.put("_input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl(PaymentPlugin.NotifyMethod.sync));
		parameterMap.put("notify_url", getNotifyUrl(PaymentPlugin.NotifyMethod.async));
		parameterMap.put("out_trade_no", sn);
		parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
		parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
		parameterMap.put("payment_type", "1");
		parameterMap.put("logistics_type", "EXPRESS");
		parameterMap.put("logistics_fee", "0");
		parameterMap.put("logistics_payment", "SELLER_PAY");
		parameterMap.put("price", paymentLog.getAmount().setScale(2).toString());
		parameterMap.put("quantity", "1");
		parameterMap.put("seller_id", pluginConfig.getAttribute("partner"));
		parameterMap.put("total_fee", paymentLog.getAmount().setScale(2).toString());
		parameterMap.put("show_url", setting.getSiteUrl());
		parameterMap.put("paymethod", "directPay");
		parameterMap.put("extend_param", "isv^1860648a1");
		parameterMap.put("exter_invoke_ip", request.getLocalAddr());
		parameterMap.put("extra_common_param", "utlz");
		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		PaymentLog paymentLog = getPaymentLog(request.getParameter("out_trade_no"));
		if (paymentLog != null && generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && pluginConfig.getAttribute("partner").equals(request.getParameter("seller_id"))
				&& ("WAIT_SELLER_SEND_GOODS".equals(request.getParameter("trade_status")) || "TRADE_FINISHED".equals(request.getParameter("trade_status"))) && paymentLog.getAmount().compareTo(new BigDecimal(request.getParameter("total_fee"))) == 0) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("service", "notify_verify");
			parameterMap.put("partner", pluginConfig.getAttribute("partner"));
			parameterMap.put("notify_id", request.getParameter("notify_id"));
			if ("true".equals(WebUtils.post("https://mapi.alipay.com/gateway.do", parameterMap))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("out_trade_no");
	}

	@Override
	public String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		if (PaymentPlugin.NotifyMethod.async.equals(notifyMethod)) {
			return "success";
		}
		return null;
	}

	private String generateSign(Map<String, ?> parameterMap) {
		PluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, pluginConfig.getAttribute("key"), "&", true, "sign_type", "sign"));
	}

}