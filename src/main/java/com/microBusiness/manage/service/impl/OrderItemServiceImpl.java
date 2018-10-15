/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderItemDao;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.service.OrderItemService;

@Service("orderItemServiceImpl")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {
	
	@Resource
	private OrderItemDao orderItemDao;

	@Override
	public List<OrderItem> findPageForGroupGoods(Pageable pageable, ProxyUser proxyUser, Date startDate, Date endDate){
		return orderItemDao.findPageForGroupGoods(pageable, proxyUser, startDate, endDate);
	}
	
	@Override
	public int sumPageForGroupGoods(ProxyUser proxyUser, Date startDate, Date endDate){
		return orderItemDao.sumPageForGroupGoods(proxyUser, startDate, endDate);
	}

	@Override
	public List<OrderItem> findPageForGroupGoods(Pageable pageable, List<ProxyUser> proxyUserList, Date startDate,
			Date endDate) {
		return orderItemDao.findPageForGroupGoods(pageable, proxyUserList, startDate,
				 endDate);
	}

	@Override
	public int sumPageForGroupGoods(List<ProxyUser> proxyUserList, Date startDate, Date endDate) {
		return orderItemDao.sumPageForGroupGoods(proxyUserList, startDate, endDate);
	}

}