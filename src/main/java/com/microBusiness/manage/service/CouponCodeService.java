/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;

public interface CouponCodeService extends BaseService<CouponCode, Long> {

	boolean codeExists(String code);

	CouponCode findByCode(String code);

	CouponCode generate(Coupon coupon, Member member);

	List<CouponCode> generate(Coupon coupon, Member member, Integer count);

	CouponCode exchange(Coupon coupon, Member member, Admin operator);

	Page<CouponCode> findPage(Member member, Pageable pageable);

	Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

}