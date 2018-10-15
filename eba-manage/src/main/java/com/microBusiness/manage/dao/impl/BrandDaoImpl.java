/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.dao.BrandDao;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.ProductCategory;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.dao.BrandDao;
import com.microBusiness.manage.entity.ProductCategory;
import org.springframework.stereotype.Repository;

@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long> implements BrandDao {

	public List<Brand> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Brand> criteriaQuery = criteriaBuilder.createQuery(Brand.class);
		Root<Brand> root = criteriaQuery.from(Brand.class);
		criteriaQuery.select(root);
		if (productCategory != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.join("productCategories"), productCategory));
		}
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

}