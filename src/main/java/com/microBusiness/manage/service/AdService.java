/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Ad;

public interface AdService extends BaseService<Ad, Long> {

	/**
	 * 查询广告
	 */
	public List<Ad> query(Long id);
}