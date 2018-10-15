/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.dao.BrandDao;
import com.microBusiness.manage.dao.ProductCategoryDao;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.BrandService;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.BrandService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("brandServiceImpl")
public class BrandServiceImpl extends BaseServiceImpl<Brand, Long> implements BrandService {

	@Resource(name = "brandDaoImpl")
	private BrandDao brandDao;
	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;

	@Transactional(readOnly = true)
	public List<Brand> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		return brandDao.findList(productCategory, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "brand", condition = "#useCache")
	public List<Brand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return brandDao.findList(productCategory, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand save(Brand brand) {
		return super.save(brand);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand update(Brand brand) {
		return super.update(brand);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand update(Brand brand, String... ignoreProperties) {
		return super.update(brand, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Brand brand) {
		super.delete(brand);
	}

}