/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.RoleDao;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Role.Switchs;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

	@Override
	public List<Role> findAll(Switchs switchs,Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
		Root<Role> root = criteriaQuery.from(Role.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (switchs != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("switchs"), switchs));
		}
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<Role> findPage(Pageable pageable, Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
		Root<Role> root = criteriaQuery.from(Role.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		criteriaQuery.where(restrictions);
		return findPage(criteriaQuery, pageable);
	}

}