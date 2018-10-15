/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.DeliveryCenter;

public interface DeliveryCenterService extends BaseService<DeliveryCenter, Long> {

	DeliveryCenter findDefault();

}