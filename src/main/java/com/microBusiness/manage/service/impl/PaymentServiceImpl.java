/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.PaymentDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.PaymentService;

import com.microBusiness.manage.dao.PaymentDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.Sn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}

	@Override
	@Transactional
	public Payment save(Payment payment) {
		Assert.notNull(payment);

		payment.setSn(snDao.generate(Sn.Type.payment));

		return super.save(payment);
	}
	
	
	@Override
	public boolean isExistTransactionId(String transactionId) {
		return paymentDao.isExistTransactionId(transactionId);
	}

}