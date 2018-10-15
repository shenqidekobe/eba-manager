/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.ProxyUser;

public interface OrderItemService extends BaseService<OrderItem, Long> {
	
	List<OrderItem> findPageForGroupGoods(Pageable pageable, ProxyUser proxyUser, Date startDate, Date endDate);
	
	int sumPageForGroupGoods(ProxyUser proxyUser, Date startDate, Date endDate);
	
	List<OrderItem> findPageForGroupGoods(Pageable pageable, List<ProxyUser> proxyUserList, Date startDate, Date endDate);
	
	int sumPageForGroupGoods(List<ProxyUser> proxyUserList, Date startDate, Date endDate);	
	
}