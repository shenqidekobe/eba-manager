/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Role.Switchs;
import com.microBusiness.manage.entity.Supplier;

public interface RoleService extends BaseService<Role, Long> {

	List<Role> findAll(Switchs switchs,Supplier supplier);
	
	Page<Role> findPage(Pageable pageable,Supplier supplier);

}