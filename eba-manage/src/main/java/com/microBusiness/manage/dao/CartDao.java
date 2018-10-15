/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Cart;

public interface CartDao extends BaseDao<Cart, Long> {

	Cart findByKey(String key);

	List<Cart> findList(Boolean hasExpired, Integer count);

}