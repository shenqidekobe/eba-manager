/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.microBusiness.manage.dao.ProxyCheckDao;
import com.microBusiness.manage.dao.ProxyUserDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProxyCheck;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.ProxyUserService;

@Service("proxyUserServiceImpl")
public class ProxyUserServiceImpl extends BaseServiceImpl<ProxyUser, Long> implements ProxyUserService {

	@Resource(name = "proxyUserDaoImpl")
	private ProxyUserDao proxyUserDao;
	
	@Resource(name = "proxyCheckDaoImpl")
	private ProxyCheckDao proxyCheckDao;
	
	
	@Transactional(readOnly = true)
	public List<ProxyUser> findRoots() {
		return proxyUserDao.findRoots(null);
	}
	
	@Transactional(readOnly = true)
	public List<ProxyUser> findRoots(Integer count, Supplier supplier) {
		return proxyUserDao.findRoots(count, supplier);
	}

	@Transactional(readOnly = true)
	public List<ProxyUser> findRoots(Integer count) {
		return proxyUserDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	//@Cacheable(value = "ProxyUser", condition = "#useCache")
	public List<ProxyUser> findRoots(Integer count, boolean useCache) {
		return proxyUserDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count) {
		return proxyUserDao.findParents(ProxyUser, recursive, count);
	}

	@Transactional(readOnly = true)
	//@Cacheable(value = "proxyUser", condition = "#useCache")
	public List<ProxyUser> findParents(Long ProxyUserId, boolean recursive, Integer count, boolean useCache) {
		ProxyUser ProxyUser = proxyUserDao.find(ProxyUserId);
		if (ProxyUserId != null && ProxyUser == null) {
			return Collections.emptyList();
		}
		return proxyUserDao.findParents(ProxyUser, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<ProxyUser> findTree() {
		return proxyUserDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	public List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count) {
		return proxyUserDao.findChildren(ProxyUser, recursive, count);
	}

	@Transactional(readOnly = true)
	//@Cacheable(value = "proxyUser", condition = "#useCache")
	public List<ProxyUser> findChildren(Long proxyUserId, boolean recursive, Integer count, boolean useCache) {
		ProxyUser proxyUser = proxyUserDao.find(proxyUserId);
		if (proxyUserId != null && proxyUser == null) {
			return Collections.emptyList();
		}
		return proxyUserDao.findChildren(proxyUser, recursive, count);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "proxyUser" }, allEntries = true)
	public ProxyUser save(ProxyUser ProxyUser) {
		Assert.notNull(ProxyUser);

		setValue(ProxyUser);
		return super.save(ProxyUser);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "proxyUser" }, allEntries = true)
	public ProxyUser update(ProxyUser ProxyUser) {
		Assert.notNull(ProxyUser);

		setValue(ProxyUser);
		for (ProxyUser children : proxyUserDao.findChildren(ProxyUser, true, null)) {
			setValue(children);
		}
		return super.update(ProxyUser);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "goods", "proxyUser" }, allEntries = true)
	public ProxyUser update(ProxyUser ProxyUser, String... ignoreProperties) {
		return super.update(ProxyUser, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "goods", "proxyUser" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "goods", "proxyUser" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = { "goods", "proxyUser" }, allEntries = true)
	public void delete(ProxyUser ProxyUser) {
		super.delete(ProxyUser);
	}

	private void setValue(ProxyUser proxyUser) {
		if (proxyUser == null) {
			return;
		}
		ProxyUser parent = proxyUser.getParent();
		if (parent != null) {
			proxyUser.setTreePath(parent.getTreePath() + parent.getId() + ProxyUser.TREE_PATH_SEPARATOR);
		} else {
			proxyUser.setTreePath(ProxyUser.TREE_PATH_SEPARATOR);
		}
		proxyUser.setGrade(proxyUser.getParentIds().length);
	}

	@Transactional(readOnly = true)
	public List<ProxyUser> findTree(Supplier supplier , String searchName) {
		return proxyUserDao.findChildren(null, true, null , supplier , searchName);
	}



	@Override
	public List<ProxyUser> findByAllSupplier(Long supplierId) {
		return proxyUserDao.findByAllSupplier(supplierId);
	}

	@Override
	public List<ProxyUser> findLike(Long ProxyUserId) {
		return proxyUserDao.findLike(ProxyUserId);
	}

	

	@Override
	public List<ProxyUser> findTreeList(Supplier supplier,
			String searchName) {
		return proxyUserDao.findChildrenList(null, true, null , supplier , searchName);
	}
	

	@Override
	public List<ProxyUser> findByParent(Supplier supplier, ProxyUser parent, String name) {
		return proxyUserDao.findByParent(supplier, parent, name);
	}

	@Override
	public List<ProxyUser> findByParentMember(Member member, ProxyUser parent, String name) {
		return proxyUserDao.findByParent(member, parent, name);
	}
	
	@Override
	public List<ProxyUser> findRootByMember(Member member) {
		return proxyUserDao.findRootByMember(member);
	}

	@Override
	public List<ProxyUser> findTree(Member member, String searchName) {
		return proxyUserDao.findTree(member, searchName, null);
	}


	
	private ProxyUser saveProxyUser(ProxyUser proxyUser) {
		setValue(proxyUser);
		//proxyUser.setTypes(Types.local);
		proxyUserDao.persist(proxyUser);
		return proxyUser;
	}
	


	@Override
	public List<ProxyUser> findByParent(Member member,
			ProxyUser parent, String name, Types types) {
		return proxyUserDao.findByParent(member, parent, name, types);
	}

	@Override
	public ProxyCheck saveProxyCheck(ProxyCheck proxyCheck) {
		proxyCheckDao.persist(proxyCheck);
		return proxyCheck;
	}

}