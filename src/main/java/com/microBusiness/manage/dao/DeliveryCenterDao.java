/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.DeliveryCenter;

public interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long> {

	DeliveryCenter findDefault();

	void setDefault(DeliveryCenter deliveryCenter);

}