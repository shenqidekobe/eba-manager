/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.StorageFormDao;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.Supplier;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("storageFormDaoImpl")
public class StorageFormDaoImpl extends BaseDaoImpl<StorageForm, Long> implements StorageFormDao {

	@Override
	public boolean exists(Shop shop, SfIfStatus sfIfStatus) {
		String jpql = "select count(*) from StorageForm storageForm where storageForm.shop=:shop and storageForm.status=:sfIfStatus";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("shop", shop).setParameter("sfIfStatus", sfIfStatus).getSingleResult();
		return count > 0;
	}

	@Override
	public Page<StorageForm> findPage(Pageable pageable, String Keyword, Shop shop) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StorageForm> criteriaQuery = criteriaBuilder.createQuery(StorageForm.class);
		Root<StorageForm> root = criteriaQuery.from(StorageForm.class);
		criteriaQuery.select(root);
		Join<StorageForm, Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(!StringUtils.isEmpty(Keyword)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+Keyword+"%"), criteriaBuilder.like(root.<String>get("storageCode"), "%"+Keyword+"%")));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long getCount(Member member) {
		StringBuffer sql = new StringBuffer("select count(1) counts from t_storage_from where date(create_date) = curdate() and member =:menber");
		Query query = entityManager.createNativeQuery(sql.toString()).setParameter("menber" , member.getId());
		String count = query.getSingleResult().toString();
		
		return Long.valueOf(count)+1;
	}

	@Override
	public StorageForm findByStorageCode(String storageCode, Member member) {
		try {
	        String jpql = "select storageForm from StorageForm storageForm where storageForm.storageCode = :storageCode and storageForm.member =:member";
	        return entityManager.createQuery(jpql, StorageForm.class).setParameter("storageCode", storageCode).setParameter("member", member).getSingleResult();
	     } catch (NoResultException e) {
	        return null;
	     }
	}

}