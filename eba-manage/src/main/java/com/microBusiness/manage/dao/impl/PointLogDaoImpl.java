/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.PointLogDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.PointLogDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;
import org.springframework.stereotype.Repository;

@Repository("pointLogDaoImpl")
public class PointLogDaoImpl extends BaseDaoImpl<PointLog, Long> implements PointLogDao {

	public Page<PointLog> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return Page.emptyPage(pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PointLog> criteriaQuery = criteriaBuilder.createQuery(PointLog.class);
		Root<PointLog> root = criteriaQuery.from(PointLog.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findPage(criteriaQuery, pageable);
	}

}