/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AdDao;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.service.AdService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adServiceImpl")
public class AdServiceImpl extends BaseServiceImpl<Ad, Long> implements AdService {

	@Resource(name = "adDaoImpl")
	private AdDao AdDao;
	
	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad save(Ad ad) {
		return super.save(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad) {
		return super.update(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad, String... ignoreProperties) {
		return super.update(ad, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Ad ad) {
		super.delete(ad);
	}

	@Override
	public List<Ad> query(Long id) {
		return AdDao.query(id);
	}

}