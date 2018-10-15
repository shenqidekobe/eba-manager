package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderRelationDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRelation;

@Repository
public class OrderRelationDaoImpl extends BaseDaoImpl<OrderRelation, Long> implements OrderRelationDao{

	@Override
	public OrderRelation findByChildMember(ChildMember childMember, Order order) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderRelation> criteriaQuery = criteriaBuilder.createQuery(OrderRelation.class);
		Root<OrderRelation> root = criteriaQuery.from(OrderRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (childMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("childMember"), childMember));
		}
		if (order != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order"), order));
		}
		criteriaQuery.where(restrictions);
		List<OrderRelation> orderRelations=this.findList(criteriaQuery, null, null, null, null);
		if (orderRelations.size()>0) {
			return orderRelations.get(0);
		}
		return null;
	}

	@Override
	public Page<OrderRelation> findPage(Pageable pageable, Order order,
			Long orderRelationId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderRelation> criteriaQuery = criteriaBuilder.createQuery(OrderRelation.class);
		Root<OrderRelation> root = criteriaQuery.from(OrderRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (orderRelationId != null) {
			//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Long>get("id"),orderRelationId));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("id"),orderRelationId));
		}
		if (order != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order"), order));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

}
