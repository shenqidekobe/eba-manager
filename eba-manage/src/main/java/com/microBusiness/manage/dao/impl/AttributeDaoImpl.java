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
import com.microBusiness.manage.dao.AttributeDao;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ProductCategory;

import com.microBusiness.manage.entity.ProductCategory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("attributeDaoImpl")
public class AttributeDaoImpl extends BaseDaoImpl<Attribute, Long> implements AttributeDao {

	public Integer findUnusedPropertyIndex(ProductCategory productCategory) {
		Assert.notNull(productCategory);

		for (int i = 0; i < Goods.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			String jpql = "select count(*) from Attribute attribute where attribute.productCategory = :productCategory and attribute.propertyIndex = :propertyIndex";
			Long count = entityManager.createQuery(jpql, Long.class).setParameter("productCategory", productCategory).setParameter("propertyIndex", i).getSingleResult();
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	public List<Attribute> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Attribute> criteriaQuery = criteriaBuilder.createQuery(Attribute.class);
		Root<Attribute> root = criteriaQuery.from(Attribute.class);
		criteriaQuery.select(root);
		if (productCategory != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("productCategory"), productCategory));
		}
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

}