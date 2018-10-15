/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessageCompamy;


public interface NewMessageCompamyDao extends BaseDao<NewMessageCompamy, Long> {
	
	public Page<NewMessageCompamy> query(Admin admin, Boolean readBoolean, Pageable pageable);
}