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
import com.microBusiness.manage.dao.DepositLogDao;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;

import com.microBusiness.manage.entity.Member;
import org.springframework.stereotype.Repository;

@Repository("depositLogDaoImpl")
public class DepositLogDaoImpl extends BaseDaoImpl<DepositLog, Long> implements DepositLogDao {

	public Page<DepositLog> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return Page.emptyPage(pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DepositLog> criteriaQuery = criteriaBuilder.createQuery(DepositLog.class);
		Root<DepositLog> root = criteriaQuery.from(DepositLog.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findPage(criteriaQuery, pageable);
	}

}