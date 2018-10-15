/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ArticleCategoryDao;
import com.microBusiness.manage.dao.RoleDao;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Role.Switchs;
import com.microBusiness.manage.service.RoleService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

	@Resource(name = "roleDaoImpl")
	private RoleDao roleDao;
	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Role save(Role role) {
		return super.save(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Role update(Role role) {
		return super.update(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Role update(Role role, String... ignoreProperties) {
		return super.update(role, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Role role) {
		super.delete(role);
	}

	@Override
	public List<Role> findAll(Switchs switchs,Supplier supplier) {
		
		return roleDao.findAll(switchs,supplier);
	}

	@Override
	public Page<Role> findPage(Pageable pageable, Supplier supplier) {
		// TODO Auto-generated method stub
		return roleDao.findPage(pageable,supplier);
	}

}