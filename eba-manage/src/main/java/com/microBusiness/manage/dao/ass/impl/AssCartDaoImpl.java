/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.dao.ass.AssCartDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCart;

import org.springframework.stereotype.Repository;

@Repository("assCartDaoImpl")
public class AssCartDaoImpl extends BaseDaoImpl<AssCart, Long> implements AssCartDao {

	public List<AssCart> findList(Boolean hasExpired, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCart> criteriaQuery = criteriaBuilder.createQuery(AssCart.class);
		Root<AssCart> root = criteriaQuery.from(AssCart.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThan(root.<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, null, null);
	}

}