/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.MemberAttribute;
import com.microBusiness.manage.Filter;

public interface MemberAttributeService extends BaseService<MemberAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders);

	List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	List<MemberAttribute> findList(Boolean isEnabled, boolean useCache);

	boolean isValid(MemberAttribute memberAttribute, String[] values);

	Object toMemberAttributeValue(MemberAttribute memberAttribute, String[] values);

}