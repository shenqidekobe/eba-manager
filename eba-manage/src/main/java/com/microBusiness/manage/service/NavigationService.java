/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Navigation;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Navigation;

public interface NavigationService extends BaseService<Navigation, Long> {

	List<Navigation> findList(Navigation.Position position);

	List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}