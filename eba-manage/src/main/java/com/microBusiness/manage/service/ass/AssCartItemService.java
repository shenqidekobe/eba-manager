/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.service.BaseService;

public interface AssCartItemService extends BaseService<AssCartItem, Long> {

	public AssCartItem deleteByCartItem(AssCartItem cartItem);

	AssCartItem deleteByCartItem(AssCartItem cartItem , Long assCustomerRelationId);
	
	List<AssCartItem> findByList(List<Long> assProductids);

}