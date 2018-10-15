/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.ProductCategory;

public interface BrandDao extends BaseDao<Brand, Long> {

	List<Brand> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders);

}