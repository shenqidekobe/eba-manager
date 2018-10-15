/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import com.microBusiness.manage.dao.PaymentLogDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.PaymentLog;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.PaymentLogService;

import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.PaymentLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("paymentLogServiceImpl")
public class PaymentLogServiceImpl extends BaseServiceImpl<PaymentLog, Long> implements PaymentLogService {

	@Resource(name = "paymentLogDaoImpl")
	private PaymentLogDao paymentLogDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Transactional(readOnly = true)
	public PaymentLog findBySn(String sn) {
		return paymentLogDao.findBySn(sn);
	}

	public void handle(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);

		if (!LockModeType.PESSIMISTIC_WRITE.equals(paymentLogDao.getLockMode(paymentLog))) {
			paymentLogDao.refresh(paymentLog, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(paymentLog.getType());

		if (!PaymentLog.Status.wait.equals(paymentLog.getStatus())) {
			return;
		}

		switch (paymentLog.getType()) {
		case recharge:
			Member member = paymentLog.getMember();
			if (member != null) {
				memberService.addBalance(member, paymentLog.getEffectiveAmount(), DepositLog.Type.recharge, null, null);
			}
			break;
		case payment:
			Order order = paymentLog.getOrder();
			if (order != null) {
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.online);
				payment.setPaymentMethod(paymentLog.getPaymentPluginName());
				payment.setFee(paymentLog.getFee());
				payment.setAmount(paymentLog.getAmount());
				payment.setOrder(order);
				orderService.payment(order, payment, null);
			}
			break;
		}
		paymentLog.setStatus(PaymentLog.Status.success);
	}

	@Override
	@Transactional
	public PaymentLog save(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);

		paymentLog.setSn(snDao.generate(Sn.Type.paymentLog));

		return super.save(paymentLog);
	}

}