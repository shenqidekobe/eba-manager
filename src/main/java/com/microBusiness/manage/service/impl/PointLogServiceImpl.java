/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.PointLogDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.service.PointLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pointLogServiceImpl")
public class PointLogServiceImpl extends BaseServiceImpl<PointLog, Long> implements PointLogService {

	@Resource(name = "pointLogDaoImpl")
	private PointLogDao pointLogDao;

	@Transactional(readOnly = true)
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		return pointLogDao.findPage(member, pageable);
	}

}