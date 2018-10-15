/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.CouponCodeDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.service.CouponCodeService;
import com.microBusiness.manage.service.MemberService;

import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Member;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("couponCodeServiceImpl")
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode, Long> implements CouponCodeService {

	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Transactional(readOnly = true)
	public boolean codeExists(String code) {
		return couponCodeDao.codeExists(code);
	}

	@Transactional(readOnly = true)
	public CouponCode findByCode(String code) {
		return couponCodeDao.findByCode(code);
	}

	public CouponCode generate(Coupon coupon, Member member) {
		Assert.notNull(coupon);

		CouponCode couponCode = new CouponCode();
		couponCode.setCode(coupon.getPrefix() + DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)).toUpperCase());
		couponCode.setIsUsed(false);
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		return super.save(couponCode);
	}

	public List<CouponCode> generate(Coupon coupon, Member member, Integer count) {
		Assert.notNull(coupon);
		Assert.notNull(count);

		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = generate(coupon, member);
			couponCodes.add(couponCode);
			if (i % 50 == 0) {
				couponCodeDao.flush();
				couponCodeDao.clear();
			}
		}
		return couponCodes;
	}

	public CouponCode exchange(Coupon coupon, Member member, Admin operator) {
		Assert.notNull(coupon);
		Assert.notNull(coupon.getPoint());
		Assert.state(coupon.getIsEnabled() && coupon.getIsExchange() && !coupon.hasExpired());
		Assert.notNull(member);
		Assert.notNull(member.getPoint());
		Assert.state(member.getPoint().longValue() >= coupon.getPoint());

		if (coupon.getPoint() > 0) {
			memberService.addPoint(member, -coupon.getPoint(), PointLog.Type.exchange, operator, null);
		}

		return generate(coupon, member);
	}

	@Transactional(readOnly = true)
	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		return couponCodeDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		return couponCodeDao.count(coupon, member, hasBegun, hasExpired, isUsed);
	}

}