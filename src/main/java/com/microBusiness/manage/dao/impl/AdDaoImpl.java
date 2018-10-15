/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.dao.AdDao;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.entity.Ad.Type;
import org.springframework.stereotype.Repository;

@Repository("adDaoImpl")
public class AdDaoImpl extends BaseDaoImpl<Ad, Long> implements AdDao {

	@Override
	public List<Ad> query(Long id) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Ad> criteriaQuery = criteriaBuilder.createQuery(Ad.class);
		Root<Ad> root = criteriaQuery.from(Ad.class);
		criteriaQuery.select(root);
		Predicate restrictions=criteriaBuilder.disjunction();

		restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), Type.image));
		restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("adPosition"), id));

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

}