/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.PaymentLog;
import com.microBusiness.manage.entity.PaymentLog;

public interface PaymentLogDao extends BaseDao<PaymentLog, Long> {

	PaymentLog findBySn(String sn);

}