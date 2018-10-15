/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Types;

public interface ProxyUserDao extends BaseDao<ProxyUser, Long> {

	List<ProxyUser> findRoots(Integer count);

	List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count);

	List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count);

	List<ProxyUser> findRoots(Integer count , Supplier supplier);

	List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count , Supplier supplier);

	List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count, Supplier supplier , String searchName);
	
	List<ProxyUser> findChildrenList(ProxyUser ProxyUser, boolean recursive, Integer count, Supplier supplier , String searchName);

	List<ProxyUser> findByAllSupplier(Long supplierId);

	List<ProxyUser> findLike(Long ProxyUserId);
	
	List<ProxyUser> findByParent(Supplier supplier, ProxyUser parent, String name);
	
	List<ProxyUser> findByParent(Member member, ProxyUser parent, String name);

	List<ProxyUser> findTree(Member member , String searchName , ProxyUser ProxyUser);
	
	List<ProxyUser> findRootByMember(Member member);

	List<ProxyUser> findByParent(Member member, ProxyUser parent, String name , Types types);
}