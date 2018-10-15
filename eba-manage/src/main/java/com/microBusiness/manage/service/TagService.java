/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Tag;

public interface TagService extends BaseService<Tag, Long> {

	List<Tag> findList(Tag.Type type);

	List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}