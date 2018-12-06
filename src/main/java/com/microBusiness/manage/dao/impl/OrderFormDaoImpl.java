package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.OrderFormDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.OrderForm;

@Repository
public class OrderFormDaoImpl extends BaseDaoImpl<OrderForm, Long> implements OrderFormDao {

	@Override
	public int clearExpired() {
		String sql="delete from t_order_form where (to_days(now())-to_days(create_date))>7";
		try {
			return entityManager.createNativeQuery(sql).executeUpdate();
		} catch (NoResultException e) {
			return 0;
		}
	}

	@Override
	public OrderForm getByFormId(String formId) {
		if (StringUtils.isEmpty(formId)) {
			return null;
		}
		String jpql = "select orders from OrderForm orders where orders.formId = :formId";
		try {
			List<OrderForm> list=entityManager.createQuery(jpql, OrderForm.class)
					.setParameter("formId", formId).getResultList();
			return list.isEmpty()?null:list.get(0);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public OrderForm getDoOrderForm(ChildMember childMember) {
		String jpql = "select * from t_order_form where use_num <3 and (to_days(now())-to_days(create_date))<7 and child_member=:childMember";
		try {
			List<OrderForm> list=entityManager.createNativeQuery(jpql, OrderForm.class).setParameter("childMember", childMember.getId()).getResultList();
					//.createQuery(jpql, OrderForm.class).getResultList();
			return list.isEmpty()?null:list.get(0);
		} catch (NoResultException e) {
			return null;
		}
	}

}
