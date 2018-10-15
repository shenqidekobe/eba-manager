/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Returns;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.ReturnsService;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Returns;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.ReturnsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("returnsServiceImpl")
public class ReturnsServiceImpl extends BaseServiceImpl<Returns, Long> implements ReturnsService {

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Override
	@Transactional
	public Returns save(Returns returns) {
		Assert.notNull(returns);

		returns.setSn(snDao.generate(Sn.Type.returns));

		return super.save(returns);
	}

}