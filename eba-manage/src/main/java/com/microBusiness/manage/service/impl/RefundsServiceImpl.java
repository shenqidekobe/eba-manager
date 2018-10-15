/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.service.RefundsService;

import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Sn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("refundsServiceImpl")
public class RefundsServiceImpl extends BaseServiceImpl<Refunds, Long> implements RefundsService {

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Override
	@Transactional
	public Refunds save(Refunds refunds) {
		Assert.notNull(refunds);

		refunds.setSn(snDao.generate(Sn.Type.refunds));

		return super.save(refunds);
	}

}