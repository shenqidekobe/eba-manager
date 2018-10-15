/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.sun.xml.bind.v2.model.core.ID;

import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Types;

public interface ProductCategoryDao extends BaseDao<ProductCategory, Long> {

	List<ProductCategory> findRoots(Integer count);

	List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count);

	List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count);

	List<ProductCategory> findRoots(Integer count , Supplier supplier);

	List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count , Supplier supplier);

	List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count, Supplier supplier , String searchName);
	
	List<ProductCategory> findChildrenList(ProductCategory productCategory, boolean recursive, Integer count, Supplier supplier , String searchName);

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
	
	ProductCategory findBySourceAndSupplier(Supplier supplier , ProductCategory source , SupplierSupplier supplierSupplier);

	List<ProductCategory> findByGradeAndNeedByFormal(
			SupplierSupplier supplierSupplier, Need need);

	List<ProductCategory> findByAssSupplier(Long supplierId, Long bySupplierId);

	List<ProductCategory> findByParent(Supplier supplier, ProductCategory parent, String name);
	
	List<ProductCategory> findByParent(Member member, ProductCategory parent, String name);
	
	/**
	 * 
	 * @Title: findTree
	 * @author: yuezhiwei
	 * @date: 2018年3月6日上午10:50:24
	 * @Description: 定货么小程序本地分类查询
	 * @return: List<ProductCategory>
	 */
	List<ProductCategory> findTree(Member member , String searchName , ProductCategory productCategory);
	
	List<ProductCategory> findLocalChildren(ProductCategory productCategory, boolean recursive, Integer count);

	/**
	 * 根据企业供应查询企业供应的商品的分类
	 * @param supplierSupplierId  企业供应关系
	 * @return
	 */
    List<ProductCategory> findBySupplierSuppler(Long supplierSupplierId);

	/**
	 * 根据门店供应查询供应的商品的分类
	 * @param supplyNeedId  门店供应id
	 * @return
	 */
	List<ProductCategory> findBySupplyNeedId(Long supplyNeedId);

	List<ProductCategory> findRootByMember(Member member);

    List<ProductCategory> findBySupplierSupplerNeed(Long supplierSupplierId, Need need);

	List<ProductCategory> findBySupplyNeedShop(Long supplyNeedId, Long shopId,Long supplierId);
	
	List<ProductCategory> findByParent(Member member, ProductCategory parent, String name , Types types);
}