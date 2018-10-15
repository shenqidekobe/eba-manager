/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.FreightConfigDao;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.FreightConfig;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.service.FreightConfigService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("freightConfigServiceImpl")
public class FreightConfigServiceImpl extends BaseServiceImpl<FreightConfig, Long> implements FreightConfigService {

	@Resource(name = "freightConfigDaoImpl")
	private FreightConfigDao freightConfigDao;

	@Transactional(readOnly = true)
	public boolean exists(ShippingMethod shippingMethod, Area area) {
		return freightConfigDao.exists(shippingMethod, area);
	}

	@Transactional(readOnly = true)
	public boolean unique(ShippingMethod shippingMethod, Area previousArea, Area currentArea) {
		if (previousArea != null && previousArea.equals(currentArea)) {
			return true;
		}
		return !freightConfigDao.exists(shippingMethod, currentArea);
	}

	@Transactional(readOnly = true)
	public Page<FreightConfig> findPage(ShippingMethod shippingMethod, Pageable pageable) {
		return freightConfigDao.findPage(shippingMethod, pageable);
	}

}