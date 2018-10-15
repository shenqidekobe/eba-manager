/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.ProductSupplierDto;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.entity.Types;

public interface ProductDao extends BaseDao<Product, Long> {

	boolean snExists(String sn, Supplier supplier);

	Product findBySn(String sn, Supplier supplier);

	List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count);

	List<Product> getproductSpecifications(Long goodsId);

	/**
	 * 获取商品正式供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	List<Product> getFormalProduct(Long goodsId , Long supplierId , Need need);

	/**
	 * 获取商品临时供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	List<Product> getTemporaryProduct(Long goodsId , Long supplierId , Need need);

	Page<Product> getTemporaryProductPage(Long supplierId, Long needId, Pageable pageable , Long productCategoryId,String searchName);

	Page<Product> getSupplierAssignProducts(Long supplierId, Pageable pageable , Long productCategoryId);

	//getFormalProductByAss();

	/**
	 * 获取商品正式供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	List<SupplyProductDto> getFormalSupplyProduct(Long goodsId , Long supplierId , Need need);

	/**
	 * 获取商品临时供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @return
	 */
	List<SupplyProductDto> getTemporarySupplyProduct(Long goodsId , Long supplierId , Need need);

	/**
	 *
	 * @param product
	 * @param supplierId
	 * @param need
	 * @return
	 */
	Integer getTemporarySupplyProduct(Product product , Long supplierId , Need need);

	/**
	 *
	 * @param product
	 * @param supplierId
	 * @param need
	 * @return
	 */
	Integer getFormalSupplyProduct(Product product , Long supplierId , Need need);


	/**
	 * 获取商品正式供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @param needId
	 * @return
	 */
	List<Product> getFormalProduct(Long goodsId , Long supplierId , Long needId);

	/**
	 * 获取商品临时供应的规格
	 * @param goodsId
	 * @param supplierId
	 * @param needId
	 * @return
	 */
	List<Product> getTemporaryProduct(Long goodsId , Long supplierId , Long needId);

	/**
	 * 获取供应商品列表
	 * @param supplyNeed 个体供应关系
	 * @param hasDistribution true 只查分销商品      false 只查自己的商品
	 * @param productCategory	分类
	 * @param goodName		商品名称
	 * @return
	 */

	Page<Product> getGoodListPage(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,ProductCategory productCategory,String goodName,Pageable pageable,Long[] ids);

	List<Product> getGoodList(SupplyNeed supplyNeed, Supplier supplier,Supplier cruuSupplier,
			Boolean hasDistribution, ProductCategory productCategory,
			String goodName, Pageable pageable);

	Page<Product> getGoodListPageByFormal(SupplierSupplier supplierSupplier,
			Need need, ProductCategory productCategory, String goodName,
			Pageable pageable,Long[] ids);

	List<Product> getGoodListByFormal(SupplierSupplier supplierSupplier,
			Need need);

	Page<Product> getFormalProductPage(Long supplierId, Long needId,
			Pageable pageable, Long productCategoryId,String searchName);

	Page<Product> getSupplierAssignProductsFormal(Long supplierId,
			Long bySupplierId, Pageable pageable, Long productCategoryId);
	
	Page<Product> findPage(Goods.Type type,
			ProductCategory productCategory, Brand brand, Promotion promotion,
			Tag tag, Map<Attribute, String> attributeValueMap,
			BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion,
			Goods.OrderType orderType, Pageable pageable, Supplier supplier,
			Boolean sourceFlag, Boolean branchFlag,String searchName);
	
	Page<NeedProduct> findPage(SupplyNeed supplyNeed ,Pageable pageable, Long productCategoryId);
	
	/**
	 * supplierSupplier 企业供应关系
	 * productIds product的id
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年2月5日下午4:46:02
	 * @Description: 商品列表同步分销商品会用到，查询企业供应中的product
	 * @return: List<Product>
	 */
	List<Product> findList(SupplierSupplier supplierSupplier,List<Long> productIds);

	
	/**
	 * 
	 * @Title: supplyDistributionProducts
	 * @author: yuezhiwei
	 * @date: 2018年3月10日下午4:43:06
	 * @Description: 查询企业供应中给个体客户分配的商品
	 * @return: List<Product>
	 */
	List<Product> supplyDistributionProducts(Need need , SupplierSupplier supplierSupplier);
	
	/**
	 * 
	 * @Title: supplyProducts
	 * @author: yuezhiwei
	 * @date: 2018年3月10日下午5:06:10
	 * @Description: 查询企业供应中SupplierProduct中的商品
	 * @return: List<Product>
	 */
	List<Product> supplyProducts(SupplierSupplier supplierSupplier);

	Page<Product> findPageSmall(Supplier supplier, ProductCategory productCategory, String goodsName, Long[] ids, Pageable pageable);

	
	List<Product> findList(String goodsSn , Supplier supplier);
	
	
	List<ProductSupplierDto> findByProductAndSupplier(Member member , String barCode , String goodsName, Types types, Long shopId);
	
	List<ProductSupplierDto> findByProductAndSupplier(Member member , String searchName, Types types);
	
	List<ProductSupplierDto> findShopByProductAndSupplier(Member member , String barCode , Types types, Shop shop);


}