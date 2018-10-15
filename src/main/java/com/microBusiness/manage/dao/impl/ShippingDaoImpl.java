/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ShippingDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.Shipping;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("shippingDaoImpl")
public class ShippingDaoImpl extends BaseDaoImpl<Shipping, Long> implements ShippingDao {

	public Shipping findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String jpql = "select shipping from Shipping shipping where lower(shipping.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Shipping.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Page<Shipping> getShippingByOrder(Order order, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping> criteriaQuery = criteriaBuilder.createQuery(Shipping.class);
		Root<Shipping> root = criteriaQuery.from(Shipping.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (order != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order"), order));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}