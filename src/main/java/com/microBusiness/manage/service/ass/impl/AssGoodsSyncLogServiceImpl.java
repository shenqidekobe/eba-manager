/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodsSyncLogDao;
import com.microBusiness.manage.dto.GoodsSyncDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsSyncLog;
import com.microBusiness.manage.service.ass.AssGoodsSyncLogService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

import org.springframework.stereotype.Service;

@Service("assGoodsSyncLogServiceImpl")
public class AssGoodsSyncLogServiceImpl extends BaseServiceImpl<AssGoodsSyncLog, Long> implements AssGoodsSyncLogService {

	@Resource
	private AssGoodsSyncLogDao assGoodsSyncLogDao;
	
	@Override
	public AssGoodsSyncLog add(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember) {
		AssGoodsSyncLog assGoodsSyncLog = new AssGoodsSyncLog();
		assGoodsSyncLog.setAssChildMember(assCustomerRelation.getSource().getAssChildMember());
		assGoodsSyncLog.setAssCustomerRelation(assCustomerRelation);
		assGoodsSyncLog.setBeAssChildMember(assChildMember);
		assGoodsSyncLogDao.persist(assGoodsSyncLog);
		return assGoodsSyncLog;
	}

	@Override
	public Page<AssGoodsSyncLog> query(String startDate, String endDate, String searchName, Pageable pageable, AssChildMember assChildMember){
		return assGoodsSyncLogDao.query(startDate, endDate, searchName, pageable, assChildMember);
	}

	@Override
	public List<GoodsSyncDto> queryAssGoodsSyncLogByDate(String ts, Date startDate, Date endDate, AssChildMember assChildMember) {
		List<GoodsSyncDto> list = assGoodsSyncLogDao.queryAssGoodsSyncLogByDate(startDate, endDate, assChildMember);
		
		Map<String, GoodsSyncDto> map = new HashMap<>();
		for (GoodsSyncDto goodsSyncDto : list) {
			map.put(goodsSyncDto.getReportDate(), goodsSyncDto);
		}

		List<GoodsSyncDto> syncLogList = new ArrayList<>();
		// 两个时间差 天数
		int days =DateUtils.daysBetween(startDate, endDate);
		for (int i = 0; i <= days; i++) {
			Date date = DateUtils.plusDays(startDate, i);
			String key = DateUtils.formatDateToString(date, DateformatEnum.yyyyMMdd2);
			GoodsSyncDto log = map.get(key);
//			if(ts.equalsIgnoreCase("thisWeek") || ts.equalsIgnoreCase("lastWeek")) {
//				key = DateUtils.toWeekDay(String.valueOf(i+1));
//			}
			if (log == null) {
				log = new GoodsSyncDto();
				log.setSyncNumber(0);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			log.setReportDate(sdf.format(date));
			syncLogList.add(log);
		}

		return syncLogList;
	}

	@Override
	public Integer queryTotalByDate(Date startDate, Date endDate, AssChildMember assChildMember) {
		return assGoodsSyncLogDao.queryTotalByDate(startDate, endDate, assChildMember);
	}

}