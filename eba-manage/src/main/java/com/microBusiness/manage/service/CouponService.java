/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.Coupon;

public interface CouponService extends BaseService<Coupon, Long> {

	boolean isValidPriceExpression(String priceExpression);

	Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

}