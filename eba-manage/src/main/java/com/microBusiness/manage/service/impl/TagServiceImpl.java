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
import com.microBusiness.manage.dao.TagDao;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.TagService;

import com.microBusiness.manage.dao.TagDao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("tagServiceImpl")
public class TagServiceImpl extends BaseServiceImpl<Tag, Long> implements TagService {

	@Resource(name = "tagDaoImpl")
	private TagDao tagDao;

	@Transactional(readOnly = true)
	public List<Tag> findList(Tag.Type type) {
		return tagDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "tag", condition = "#useCache")
	public List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tagDao.findList(null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public Tag save(Tag tag) {
		return super.save(tag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public Tag update(Tag tag) {
		return super.update(tag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public Tag update(Tag tag, String... ignoreProperties) {
		return super.update(tag, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public void delete(Tag tag) {
		super.delete(tag);
	}

}