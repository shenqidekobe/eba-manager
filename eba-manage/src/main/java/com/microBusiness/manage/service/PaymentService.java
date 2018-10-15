/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.Payment;

public interface PaymentService extends BaseService<Payment, Long> {

	Payment findBySn(String sn);
	
	boolean isExistTransactionId(String transactionId);

}