/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NewMessageCompamyDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessageCompamy;

import org.springframework.stereotype.Repository;

@Repository("newMessageCompamyDaoImpl")
public class NewMessageCompamyDaoImpl extends BaseDaoImpl<NewMessageCompamy, Long> implements NewMessageCompamyDao {

	@Override
	public Page<NewMessageCompamy> query(Admin admin, Boolean readBoolean, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewMessageCompamy> criteriaQuery = criteriaBuilder.createQuery(NewMessageCompamy.class);
		Root<NewMessageCompamy> root = criteriaQuery.from(NewMessageCompamy.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("receiver"), admin));
		if (readBoolean!=null) {
			if (readBoolean) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("receiverRead"), false));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}