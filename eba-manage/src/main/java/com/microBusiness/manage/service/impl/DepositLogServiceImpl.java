/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.DepositLogDao;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.DepositLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("depositLogServiceImpl")
public class DepositLogServiceImpl extends BaseServiceImpl<DepositLog, Long> implements DepositLogService {

	@Resource(name = "depositLogDaoImpl")
	private DepositLogDao depositLogDao;

	@Transactional(readOnly = true)
	public Page<DepositLog> findPage(Member member, Pageable pageable) {
		return depositLogDao.findPage(member, pageable);
	}

}