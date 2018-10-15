/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Supplier;

public interface AdminService extends BaseService<Admin, Long> {

	boolean usernameExists(String username);

	Admin findByUsername(String username);

	List<String> findAuthorities(Long id);

	boolean isAuthenticated();

	Admin getCurrent();

	String getCurrentUsername();

	String getLoginToken();

	Supplier getCurrentSupplier();

	Page<Admin> findPage(String searchValue,Pageable pageable, Supplier currentSupplier, Long adminId);

	boolean register(Supplier supplier , Admin admin, Member member);
	
	Admin findBybindPhoneNum(String tel);
	
	List<Admin> getListByDepartment(Department department,Supplier supplier);
	
	Admin operationTips(Admin.Prompts prompts , Admin admin);
	
	/**
	 * 
	 * @Title: find
	 * @author: yuezhiwei
	 * @date: 2018年3月12日下午3:02:07
	 * @Description: TODO
	 * @return: Admin
	 */
	Admin find(Supplier supplier , boolean isSystem);
	
	void refreshAdmin(Admin admin);

}