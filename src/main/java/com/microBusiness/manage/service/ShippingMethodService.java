/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;

import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShippingMethod;

public interface ShippingMethodService extends BaseService<ShippingMethod, Long> {

	BigDecimal calculateFreight(ShippingMethod shippingMethod, Area area, Integer weight);

	BigDecimal calculateFreight(ShippingMethod shippingMethod, Receiver receiver, Integer weight);

}