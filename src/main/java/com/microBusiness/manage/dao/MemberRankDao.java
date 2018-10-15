/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.math.BigDecimal;

import com.microBusiness.manage.entity.MemberRank;

public interface MemberRankDao extends BaseDao<MemberRank, Long> {

	boolean nameExists(String name);

	boolean amountExists(BigDecimal amount);

	MemberRank findDefault();

	MemberRank findByAmount(BigDecimal amount);

	void setDefault(MemberRank memberRank);

}