/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Supplier;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

	public boolean usernameExists(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		String jpql = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("username", username).getSingleResult();
		return count > 0;
	}

	public Admin findByUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		try {
			String jpql = "select admin from Admin admin where lower(admin.username) = lower(:username)";
			return entityManager.createQuery(jpql, Admin.class).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Page<Admin> findPage(String searchValue,Pageable pageable, Supplier currentSupplier, Long adminId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Admin> criteriaQuery = criteriaBuilder.createQuery(Admin.class);
		Root<Admin> root = criteriaQuery.from(Admin.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (currentSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), currentSupplier));
		}
		if (adminId != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("id"), adminId));
		}
		if (StringUtils.isNotBlank(searchValue)) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.<String>get("username") , "%"+searchValue+"%"),criteriaBuilder.like(root.<String>get("name") , "%"+searchValue+"%"),criteriaBuilder.like(root.<String>get("bindPhoneNum") , "%"+searchValue+"%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Admin findBybindPhoneNum(String tel) {
		if (StringUtils.isEmpty(tel)) {
			return null;
		}
		try {
			String jpql = "select admins from Admin admins where admins.bindPhoneNum = :tel";
			return entityManager.createQuery(jpql , Admin.class).setParameter("tel", tel).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Admin> getListByDepartment(Department department,Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Admin> criteriaQuery = criteriaBuilder.createQuery(Admin.class);
		Root<Admin> root = criteriaQuery.from(Admin.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (department != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("department"), department));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Admin find(Supplier supplier, boolean isSystem) {
		try {
			String jpql = "select * from xx_admin admin where admin.supplier = :supplier and admin.is_system = :isSystem";
			Query query = entityManager.createNativeQuery(jpql, Admin.class).setParameter("supplier", supplier)
			.setParameter("isSystem", isSystem);
			Admin admin = (Admin) query.getSingleResult();
			return admin;
		} catch (Exception e) {
			return null;
		}
		
		
	}

}