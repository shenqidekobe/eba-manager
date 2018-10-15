package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.OrderFormDao;
import com.microBusiness.manage.entity.OrderForm;
import com.microBusiness.manage.service.OrderFormService;
@Service
public class OrderFormServiceImpl extends BaseServiceImpl<OrderForm, Long> implements OrderFormService {

	@Resource
	private OrderFormDao orderFormDao;
	
	@Override
	public void clearExpired() {
		orderFormDao.clearExpired();
	}

}
