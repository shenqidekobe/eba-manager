/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.dao.NavigationDao;
import com.microBusiness.manage.entity.Navigation;
import com.microBusiness.manage.service.NavigationService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("navigationServiceImpl")
public class NavigationServiceImpl extends BaseServiceImpl<Navigation, Long> implements NavigationService {

	@Resource(name = "navigationDaoImpl")
	private NavigationDao navigationDao;

	@Transactional(readOnly = true)
	public List<Navigation> findList(Navigation.Position position) {
		return navigationDao.findList(position);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "navigation", condition = "#useCache")
	public List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return navigationDao.findList(null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public Navigation save(Navigation navigation) {
		return super.save(navigation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public Navigation update(Navigation navigation) {
		return super.update(navigation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public Navigation update(Navigation navigation, String... ignoreProperties) {
		return super.update(navigation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(Navigation navigation) {
		super.delete(navigation);
	}

}