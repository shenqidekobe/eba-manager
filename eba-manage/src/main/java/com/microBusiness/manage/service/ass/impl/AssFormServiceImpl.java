/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;

import com.microBusiness.manage.dao.ass.AssFormDao;
import com.microBusiness.manage.entity.ass.AssForm;
import com.microBusiness.manage.service.ass.AssFormService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("assFormServiceImpl")
public class AssFormServiceImpl extends BaseServiceImpl<AssForm, Long> implements AssFormService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private AssFormDao assFormDao ;
	@Override
	public void clearExpired() {
		int deleteNum=assFormDao.clearExpired();
		logger.info("删除"+deleteNum+"条过期formId!");
	}
	
}