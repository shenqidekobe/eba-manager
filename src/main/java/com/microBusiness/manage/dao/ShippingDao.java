/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.Shipping;

public interface ShippingDao extends BaseDao<Shipping, Long> {

	Shipping findBySn(String sn);

	Page<Shipping> getShippingByOrder(Order order, Pageable pageable);

}