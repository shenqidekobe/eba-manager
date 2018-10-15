/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.service.MemberRankService;

import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.service.MemberRankService;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("memberRankServiceImpl")
public class MemberRankServiceImpl extends BaseServiceImpl<MemberRank, Long> implements MemberRankService {

	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return memberRankDao.nameExists(name);
	}

	@Transactional(readOnly = true)
	public boolean nameUnique(String previousName, String currentName) {
		return StringUtils.equalsIgnoreCase(previousName, currentName) || !memberRankDao.nameExists(currentName);
	}

	@Transactional(readOnly = true)
	public boolean amountExists(BigDecimal amount) {
		return memberRankDao.amountExists(amount);
	}

	@Transactional(readOnly = true)
	public boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount) {
		return (previousAmount != null && previousAmount.compareTo(currentAmount) == 0) || !memberRankDao.amountExists(currentAmount);
	}

	@Transactional(readOnly = true)
	public MemberRank findDefault() {
		return memberRankDao.findDefault();
	}

	@Transactional(readOnly = true)
	public MemberRank findByAmount(BigDecimal amount) {
		return memberRankDao.findByAmount(amount);
	}

	@Override
	@Transactional
	public MemberRank save(MemberRank memberRank) {
		Assert.notNull(memberRank);

		if (BooleanUtils.isTrue(memberRank.getIsDefault())) {
			memberRankDao.setDefault(memberRank);
		}
		return super.save(memberRank);
	}

	@Override
	@Transactional
	public MemberRank update(MemberRank memberRank) {
		Assert.notNull(memberRank);

		if (BooleanUtils.isTrue(memberRank.getIsDefault())) {
			memberRankDao.setDefault(memberRank);
		}
		return super.update(memberRank);
	}

}