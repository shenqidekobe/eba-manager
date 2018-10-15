package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.OrderFormDao;
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

}
