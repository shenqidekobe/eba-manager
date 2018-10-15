/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;

public interface CouponCodeDao extends BaseDao<CouponCode, Long> {

	boolean codeExists(String code);

	CouponCode findByCode(String code);

	Page<CouponCode> findPage(Member member, Pageable pageable);

	Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

}