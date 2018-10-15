/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.Payment;

public interface PaymentDao extends BaseDao<Payment, Long> {

	Payment findBySn(String sn);
	
	boolean isExistTransactionId(String transactionId);

}