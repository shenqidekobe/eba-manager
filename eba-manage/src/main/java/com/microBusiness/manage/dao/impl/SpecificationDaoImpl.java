/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SpecificationDao;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.dao.SpecificationDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;

import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository("specificationDaoImpl")
public class SpecificationDaoImpl extends BaseDaoImpl<Specification, Long> implements SpecificationDao {

    @Override
    public Page<Specification> findPage(Pageable pageable, Supplier supplier) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Specification> criteriaQuery = criteriaBuilder.createQuery(Specification.class);
        Root<Specification> root = criteriaQuery.from(Specification.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery , pageable);
    }

	@Override
	public List<Specification> findByName(String name, Supplier supplier, ProductCategory productCategory) {
		try {
			String jpql = "select specification from Specification specification where specification.supplier =:supplier and specification.deleted=0 and specification.name =:name and specification.productCategory =:productCategory";
			return entityManager.createQuery(jpql, Specification.class).setParameter("supplier", supplier).setParameter("name", name).setParameter("productCategory", productCategory).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Specification> findByName(String name, Member member, ProductCategory productCategory) {
		try {
			String jpql = "select specification from Specification specification where specification.member =:member and specification.deleted=0 and specification.name =:name and specification.productCategory =:productCategory";
			return entityManager.createQuery(jpql, Specification.class).setParameter("member", member).setParameter("name", name).setParameter("productCategory", productCategory).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<Specification> findPage(Pageable pageable, Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Specification> criteriaQuery = criteriaBuilder.createQuery(Specification.class);
        Root<Specification> root = criteriaQuery.from(Specification.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types"), Types.local));
        if (null != member) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery , pageable);
	}

	@Override
	public List<Specification> existName(Member member, Types types, String name, ProductCategory productCategory) {
		try {
			String jpql = "select specification from Specification specification where specification.member =:member and specification.types=:types and specification.deleted=0 and specification.name =:name and specification.productCategory =:productCategory";
			return entityManager.createQuery(jpql, Specification.class).setParameter("member", member).setParameter("types", types).setParameter("name", name).setParameter("productCategory", productCategory).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}