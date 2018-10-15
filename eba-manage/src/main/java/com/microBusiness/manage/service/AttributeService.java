/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.ProductCategory;

public interface AttributeService extends BaseService<Attribute, Long> {

	Integer findUnusedPropertyIndex(ProductCategory productCategory);

	List<Attribute> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders);

	List<Attribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	String toAttributeValue(Attribute attribute, String value);

}