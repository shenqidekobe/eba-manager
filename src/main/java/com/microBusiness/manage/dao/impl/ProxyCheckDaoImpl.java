/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ProxyCheckDao;
import com.microBusiness.manage.entity.ProxyCheck;

@Repository("proxyCheckDaoImpl")
public class ProxyCheckDaoImpl extends BaseDaoImpl<ProxyCheck, Long> implements ProxyCheckDao {

	@Override
	public Page<ProxyCheck> findPage(Pageable pageable, String dateType, Date startDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProxyCheck> criteriaQuery = criteriaBuilder.createQuery(ProxyCheck.class);
		Root<ProxyCheck> root = criteriaQuery.from(ProxyCheck.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (StringUtils.isNotEmpty(dateType)) {
			if (startDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get(dateType), startDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get(dateType), endDate));
			}
			
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}