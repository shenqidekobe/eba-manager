/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.GoodsSyncDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsSyncLog;
import com.microBusiness.manage.service.BaseService;

public interface AssGoodsSyncLogService extends BaseService<AssGoodsSyncLog, Long> {
	
	AssGoodsSyncLog add(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember);

	Page<AssGoodsSyncLog> query(String startDate, String endDate, String searchName, Pageable pageable, AssChildMember assChildMember);
	
	List<GoodsSyncDto> queryAssGoodsSyncLogByDate(String ts, Date startDate, Date endDate, AssChildMember assChildMember);
	
	Integer queryTotalByDate(Date startDate, Date endDate, AssChildMember assChildMember);
}