/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.dao.ass.AssCartItemDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCartItem;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

@Repository("assCartItemDaoImpl")
public class AssCartItemDaoImpl extends BaseDaoImpl<AssCartItem, Long> implements AssCartItemDao {

	@Override
	public AssCartItem deleteByCartItem(AssCartItem cartItem) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<AssCartItem> criteriaDelete = criteriaBuilder.createCriteriaDelete(AssCartItem.class);
        Root<AssCartItem> root = criteriaDelete.from(AssCartItem.class);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assproduct") , cartItem.getAssproduct()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("cart") , cartItem.getCart()));

        if(null != cartItem.getAssCustomerRelation()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assCustomerRelation") , cartItem.getAssCustomerRelation()));
        }

        criteriaDelete.where(restrictions);
        Query query = entityManager.createQuery(criteriaDelete);
        query.executeUpdate();
        return cartItem;
	}

	@Override
	public List<AssCartItem> findByList(List<Long> assProductids) {
		if (!CollectionUtils.isNotEmpty(assProductids)) {
			return null;
		}
		StringBuffer findSql = new StringBuffer("select * from ass_cart_item where assproduct in(:ids)");
		Query query = entityManager.createNativeQuery(findSql.toString() , AssCartItem.class);
		query.setParameter("ids", assProductids);
		List<AssCartItem> list = query.getResultList();
		return list;
	}

}