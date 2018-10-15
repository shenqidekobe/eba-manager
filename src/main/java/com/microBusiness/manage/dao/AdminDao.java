/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Supplier;

public interface AdminDao extends BaseDao<Admin, Long> {

	boolean usernameExists(String username);

	Admin findByUsername(String username);

	Page<Admin> findPage(String searchValue,Pageable pageable, Supplier currentSupplier, Long adminId);

	Admin findBybindPhoneNum(String tel);

	List<Admin> getListByDepartment(Department department,Supplier supplier);
	
	/**
	 * 
	 * @Title: find
	 * @author: yuezhiwei
	 * @date: 2018年3月12日下午3:02:30
	 * @Description: TODO
	 * @return: Admin
	 */
	Admin find(Supplier supplier , boolean isSystem);
	
}