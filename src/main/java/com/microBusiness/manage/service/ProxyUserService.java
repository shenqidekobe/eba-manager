/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.*;

public interface ProxyUserService extends BaseService<ProxyUser, Long> {

	List<ProxyUser> findRoots();

	List<ProxyUser> findRoots(Integer count);
	
	List<ProxyUser> findRoots(Integer count, Supplier supplier);

	List<ProxyUser> findRoots(Integer count, boolean useCache);

	List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count);

	List<ProxyUser> findParents(Long ProxyUserId, boolean recursive, Integer count, boolean useCache);

	List<ProxyUser> findTree();

	List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count);

	List<ProxyUser> findChildren(Long ProxyUserId, boolean recursive, Integer count, boolean useCache);

	List<ProxyUser> findTree(Supplier supplier , String searchName);
	
	List<ProxyUser> findTreeList(Supplier supplier , String searchName);
	
	List<ProxyUser> findByAllSupplier(Long supplierId);

	List<ProxyUser> findLike(Long ProxyUserId);
	
	List<ProxyUser> findByParent(Supplier supplier, ProxyUser parent, String name);
	
	List<ProxyUser> findByParentMember(Member member, ProxyUser parent, String name);

	/**
	 * 一级代理
	 * @param member
	 * @return
	 */
	List<ProxyUser> findRootByMember(Member member);
	/**
	 * 
	 * @Title: findTree
	 * @return: List<ProxyUser>
	 */
	List<ProxyUser> findTree(Member member , String searchName);
	
	List<ProxyUser> findByParent(Member member, ProxyUser parent, String name , Types types);
	
	ProxyCheck saveProxyCheck(ProxyCheck proxyCheck);
	
}