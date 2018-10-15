/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Role.Switchs;

public interface RoleDao extends BaseDao<Role, Long> {

	List<Role> findAll(Switchs switchs,Supplier supplier);
	Page<Role> findPage(Pageable pageable, Supplier supplier);

}