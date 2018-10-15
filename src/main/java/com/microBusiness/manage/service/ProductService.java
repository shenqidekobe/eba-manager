/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.ProductSupplierDto;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.*;

public interface ProductService extends BaseService<Product, Long> {

	boolean snExists(String sn, Supplier supplier);

	Product findBySn(String sn, Supplier supplier);

	List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count);

	void addStock(Product product, int amount, StockLog.Type type, Admin operator, String memo);

	void addAllocatedStock(Product product, int amount);

	void filter(List<Product> products);
	
	List<Product> getproductSpecifications(Long goodsId);

	boolean update(List<Product> productList);
	
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

	List<Product> getProduct(Long goodsId , Long supplierId , SupplyType supplyType , Need need);

	Page<Product> getProductsPage(Long supplierId , SupplyType supplyType , Need need);

	Page<Product> getTemporaryProductPage(Long supplierId , Long needId , Pageable pageable  , Long productCategoryId,String searchName);
	
	Page<Product> getFormalProductPage(Long supplierId , Long needId , Pageable pageable  , Long productCategoryId,String searchName);

	/**
	 * 获取供应商之间分配的商品
	 * @param supplierId
	 * @param bySupplierId
	 * @param pageable
	 * @return
	 */
	Page<Product> getSupplierAssignProducts(Long supplierId , Pageable pageable , Long productCategoryId);

	/**
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param supplyType
	 * @param need
	 * @return
	 */
	List<SupplyProductDto> getSupplyProduct(Long goodsId , Long supplierId , SupplyType supplyType , Need need);

	/**
	 *
	 * @param product
	 * @param supplierId
	 * @param supplyType
	 * @param need
	 * @return
	 */
	Integer getMinOrderQuantity(Product product , Long supplierId , SupplyType supplyType , Need need);

	/**
	 * 获取个体供应商品列表
	 * @param productCategory
	 * @param goodName
	 * @return
	 */
	Page<Product> getGoodListPage(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,ProductCategory productCategory,String goodName,Pageable pageable,Long[] ids);
	
	List<Product> getGoodList(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,ProductCategory productCategory,String goodName,Pageable pageable);

	List<NeedProduct> getNeeedProducts(Long goodsId, Long supplierId, SupplyType supplyType, Need need);
	
	/**
	 * 获取供应商商品列表
	 * @param supplierSupplier
	 * @param need 如果不为空，则查询给收货点分配的商品
	 * @param productCategory
	 * @param goodName
	 * @return
	 */

	Page<Product> getGoodListPageByFormal(SupplierSupplier supplierSupplier,Need need,ProductCategory productCategory,String goodName,Pageable pageable,Long[] ids);
	
	List<Product> getGoodListByFormal(SupplierSupplier supplierSupplier,Need need);

	Page<Product> getSupplierAssignProductsFormal(Long supplierId, Long id,
			Pageable pageable, Long productCategoryId);
	
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
	 * 
	 * @Title: supplyDistributionProducts
	 * @author: yuezhiwei
	 * @date: 2018年3月10日下午4:41:46
	 * @Description: 查询企业供应中给个体客户分配的商品
	 * @return: List<Product>
	 */
	List<Product> supplyDistributionProducts(Need need , SupplierSupplier supplierSupplier);
	
	/**
	 * 
	 * @Title: supplyProducts
	 * @author: yuezhiwei
	 * @date: 2018年3月10日下午5:05:24
	 * @Description: 查询企业供应中SupplierProduct中的商品
	 * @return: List<Product>
	 */
	List<Product> supplyProducts(SupplierSupplier supplierSupplier);

	/**
	 * 2018-3-10  根据分配信息获取最小起订量  ----最新
	 * @param shop
	 * @param supplierId
	 * @param supplierType
	 * @param relationId
	 * @param product
	 * @return   返回  -1 时代表本商品在分配表已不存在
	 */
	Integer getMinOrderQuantity(Shop shop,Long supplierId,SupplierType supplierType,Long relationId,Product product);

	Page<Product> findPageSmall(Supplier supplier,ProductCategory productCategory,String goodsName,Long[] ids,Pageable pageable);

	//Page<NeedProduct> findPage(SupplyNeed supplyNeed ,Pageable pageable, Long productCategoryId); 
	
	List<Product> findList(String goodsSn , Supplier supplier);

	/**
	 * 
	 * @Title: findByProductAndSupplier
	 * @author: yuezhiwei
	 * @date: 2018年4月12日下午6:10:01
	 * @Description: 根据条形码查询当前账号下的商品和供应商信息
	 * @return: List<ProductSupplierDto>
	 */
	List<ProductSupplierDto> findByProductAndSupplier(Member member , String barCode , String goodsName, Types types, Long shopId);
	
	List<ProductSupplierDto> findByProductAndSupplier(Member member , String searchName, Types types);
	
	/**
	 * 
	 * @Title: findShopByProductAndSupplier
	 * @author: yuezhiwei
	 * @date: 2018年4月21日下午11:48:22
	 * @Description: 店内扫码列表
	 * @return: List<ProductSupplierDto>
	 */
	List<ProductSupplierDto> findShopByProductAndSupplier(Member member , String barCode, Types types, Shop shop);
}