/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.PaymentDao;
import com.microBusiness.manage.entity.Payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements PaymentDao {

	public Payment findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Payment.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public boolean isExistTransactionId(String transactionId) {
		String jpql = "select payment from Payment payment where payment.transactionId = :transactionId";
		try {
			Payment payment = entityManager.createQuery(jpql, Payment.class)
						.setParameter("transactionId", transactionId).getSingleResult();
			return (payment != null) ? true : false;
		} catch (NoResultException e) {
			return false;
		}
	}


}