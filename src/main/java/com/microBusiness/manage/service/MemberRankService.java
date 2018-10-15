/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;

import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.entity.MemberRank;

public interface MemberRankService extends BaseService<MemberRank, Long> {

	boolean nameExists(String name);

	boolean nameUnique(String previousName, String currentName);

	boolean amountExists(BigDecimal amount);

	boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount);

	MemberRank findDefault();

	MemberRank findByAmount(BigDecimal amount);

}