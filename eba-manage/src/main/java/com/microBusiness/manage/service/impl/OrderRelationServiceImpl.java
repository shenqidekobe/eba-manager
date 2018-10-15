package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderRelationDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRelation;
import com.microBusiness.manage.service.OrderRelationService;

@Service
public class OrderRelationServiceImpl extends BaseServiceImpl<OrderRelation, Long> implements OrderRelationService {

	@Resource
	private OrderRelationDao orderRelationDao;
	
	@Override
	public Page<OrderRelation> findPage(Pageable pageable, Order order,
			Long orderRelationId) {
		return orderRelationDao.findPage(pageable, order, orderRelationId);
	}

}
