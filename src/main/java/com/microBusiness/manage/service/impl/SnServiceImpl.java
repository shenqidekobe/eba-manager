/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.SnService;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Sn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("snServiceImpl")
public class SnServiceImpl implements SnService {

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Transactional
	public String generate(Sn.Type type) {
		return snDao.generate(type);
	}

}