package com.microBusiness.manage.service;

import java.math.BigDecimal;

import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShippingMethod;

public interface ShippingMethodService extends BaseService<ShippingMethod, Long> {

	BigDecimal calculateFreight(ShippingMethod shippingMethod, Area area, Integer weight);

	BigDecimal calculateFreight(ShippingMethod shippingMethod, Receiver receiver, Integer weight);

}