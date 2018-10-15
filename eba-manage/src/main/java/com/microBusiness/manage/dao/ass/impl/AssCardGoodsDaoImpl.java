/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCardGoodsDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;

import org.springframework.stereotype.Repository;

@Repository("assCardGoodsDaoImpl")
public class AssCardGoodsDaoImpl extends BaseDaoImpl<AssCardGoods, Long> implements AssCardGoodsDao {

	@Override
	public List<AssCardGoods> getAssCardGoodsByCard(AssCard assCard) {
		try {
			String jpql = "select assCardGoods from AssCardGoods assCardGoods where assCardGoods.assCard =:assCard and assCardGoods.deleted=0";
			return entityManager.createQuery(jpql, AssCardGoods.class).setParameter("assCard", assCard).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<AssCardGoods> findPage(AssCard assCard,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCardGoods> criteriaQuery = criteriaBuilder.createQuery(AssCardGoods.class);
		Root<AssCardGoods> root = criteriaQuery.from(AssCardGoods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assCard != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assCard"), assCard));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public AssCardGoods get(AssCard assCard, AssCustomerRelation assCustomerRelation) {
		try {
			String jpql = "select assCardGoods from AssCardGoods assCardGoods where assCardGoods.assCard =:assCard and assCardGoods.deleted=0 and assCardGoods.assCustomerRelation =:assCustomerRelation";
			return entityManager.createQuery(jpql, AssCardGoods.class).setParameter("assCard", assCard).setParameter("assCustomerRelation", assCustomerRelation).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}