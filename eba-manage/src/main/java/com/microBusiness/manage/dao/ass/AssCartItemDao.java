/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssCartItem;

public interface AssCartItemDao extends BaseDao<AssCartItem, Long> {
	
	public AssCartItem deleteByCartItem(AssCartItem cartItem);
	
	List<AssCartItem> findByList(List<Long> assProductids);

}