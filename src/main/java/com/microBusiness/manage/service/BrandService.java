/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCategory;

public interface BrandService extends BaseService<Brand, Long> {

	List<Brand> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders);

	List<Brand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}