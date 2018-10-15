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
import com.microBusiness.manage.dao.NewMessageDao;
import com.microBusiness.manage.entity.NewMessage;

import org.springframework.stereotype.Repository;

@Repository("newMessageDaoImpl")
public class NewMessageDaoImpl extends BaseDaoImpl<NewMessage, Long> implements NewMessageDao {

	@Override
	public Page<NewMessage> query(NewMessage newMessage, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewMessage> criteriaQuery = criteriaBuilder.createQuery(NewMessage.class);
		Root<NewMessage> root = criteriaQuery.from(NewMessage.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (newMessage.getTitle() != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("title"), "%"+newMessage.getTitle()+"%"));
		}
		if (newMessage.getId() != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("id"), newMessage.getId()));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}