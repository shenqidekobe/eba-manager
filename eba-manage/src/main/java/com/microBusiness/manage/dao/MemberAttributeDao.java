/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.MemberAttribute;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.MemberAttribute;

public interface MemberAttributeDao extends BaseDao<MemberAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders);

}