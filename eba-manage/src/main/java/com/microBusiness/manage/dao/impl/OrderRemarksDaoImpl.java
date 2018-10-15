package com.microBusiness.manage.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderRemarksDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRemarks;

@Repository("orderRemarksDaoImpl")
public class OrderRemarksDaoImpl extends BaseDaoImpl<OrderRemarks, Long> implements OrderRemarksDao {

	@Override
	public Page<OrderRemarks> getRemarksByOrder(Order order, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderRemarks> criteriaQuery = criteriaBuilder.createQuery(OrderRemarks.class);
		Root<OrderRemarks> root = criteriaQuery.from(OrderRemarks.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (order != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order"), order));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}
