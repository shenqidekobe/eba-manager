/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;

public interface CouponDao extends BaseDao<Coupon, Long> {

	Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

}