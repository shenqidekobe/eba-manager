/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.*;

public interface ProductCategoryService extends BaseService<ProductCategory, Long> {

	List<ProductCategory> findRoots();

	List<ProductCategory> findRoots(Integer count);
	
	List<ProductCategory> findRoots(Integer count, Supplier supplier);

	List<ProductCategory> findRoots(Integer count, boolean useCache);

	List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count);

	List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache);

	List<ProductCategory> findTree();

	List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count);

	List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache);

	List<ProductCategory> findTree(Supplier supplier , String searchName);
	
	List<ProductCategory> findTreeList(Supplier supplier , String searchName);
	
	List<ProductCategory> findByGradeAndSupplierSuppler(SupplierSupplier supplierSupplier);
	
	List<ProductCategory> findBySupplyNeed(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution);
	

	List<ProductCategory> findByTemporary(Need need , Long relationId);

	List<ProductCategory> findByFormal(Need need , Long relationId);

	List<ProductCategory> findByTemporary(Long needId , Long supplierId);

	List<ProductCategory> findByFormal(Long needId , Long supplierId);

	List<ProductCategory> findByAllSupplier(Long supplierId);

	/**
	 *
	 * @param supplierRelationId
	 * @return
	 */
	List<ProductCategory> findByAssSupplier(Long supplierRelationId);
	
	List<ProductCategory> findLike(Long productCategoryId);
	
	List<ProductCategory> findByGradeAndNeedByFormal(SupplierSupplier supplierSupplier,Need need);

	List<ProductCategory> findByAssSupplier(Long supplierId, Long id);
	
	List<ProductCategory> findByParent(Supplier supplier, ProductCategory parent, String name);
	
	List<ProductCategory> findByParentMember(Member member, ProductCategory parent, String name);

	/**
	 * 本地分类一级分类
	 * @param member
	 * @return
	 */
	List<ProductCategory> findRootByMember(Member member);
	/**
	 * 
	 * @Title: findTree
	 * @author: yuezhiwei
	 * @date: 2018年3月6日上午10:48:52
	 * @Description: 定货么小程序本地分类查询
	 * @return: List<ProductCategory>
	 */
	List<ProductCategory> findTree(Member member , String searchName);
	
	/**
	 * 
	 * @Title: findLocalChildren
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午7:27:12
	 * @Description: TODO
	 * @return: List<ProductCategory>
	 */
	List<ProductCategory> findLocalChildren(ProductCategory productCategory, boolean recursive, Integer count);

	/**
	 * 根据企业供应查询供应的商品的分类
	 * @param supplierSupplierId  企业供应id
	 * @return
	 */
	List<ProductCategory> findBySupplierSuppler(Long supplierSupplierId);

	/**
	 * 根据门店供应查询供应的商品的分类
	 * @param supplyNeedId  门店供应id
	 * @return
	 */
	List<ProductCategory> findBySupplyNeedId(Long supplyNeedId);

	/**
	 * 根据企业供应、need查询给门店分配的商品的分类
	 * @param supplierSupplierId  企业供应id
	 * @param need
	 * @return
	 */
	List<ProductCategory> findBySupplierSupplerNeed(Long supplierSupplierId,Need need);

	/**
	 * 根据门店供应查询供应的商品的分类
	 * @param supplyNeedId  门店供应id
	 * @param shopId  门店id
	 * @return
	 */
	List<ProductCategory> findBySupplyNeedShop(Long supplyNeedId, Long shopId,Long supplierId);
	
	ProductCategory save(CategoryCenter categoryCenter, Member member);
	
	List<ProductCategory> findByParent(Member member, ProductCategory parent, String name , Types types);
	
}