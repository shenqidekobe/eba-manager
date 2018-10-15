/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssGoods;

public interface GoodsDao extends BaseDao<Goods, Long> {

	boolean snExists(String sn, Supplier supplier);

	Goods findBySn(String sn, Supplier supplier);

	List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count);

	Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable);

	Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable);

	Page<Goods> findPage(Member member, Pageable pageable);

	Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert);

	void clearAttributeValue(Attribute attribute);

	/**
	 *
	 * @param type
	 * @param productCategory
	 * @param brand
	 * @param promotion
	 * @param tag
	 * @param attributeValueMap
	 * @param startPrice
	 * @param endPrice
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param hasPromotion
	 * @param orderType
	 * @param pageable
	 * @param supplier 供应商
	 * @param sourceFlag true查询自有商品
	 * @return
	 */
	Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
						 Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable , Supplier supplier, Boolean sourceFlag, Boolean branchFlag);
	/**
	 * 获取正式供应中的商品
	 * @param goodIds
	 * @param pageable
	 * @return
	 */
	Page<Goods> findBySupplierSupplierPage(List<Long> goodIds, Pageable pageable , Long productCategoryId);


	/**
	 * 前端获取分销供应列表
	 * @param needId 收货点id
	 * @param supplierId 供应商id
	 * @param keywords 搜索关键字
	 * @param isMarketable 是否上架
	 * @return
	 */
	List<Goods> getGoodsByTemporary(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable , Long relationId);
	
	/**
	 * 前端获取正式供应列表
	 * @param needId 收货点id
	 * @param supplierId 供应商id
	 * @param keywords 搜索关键字
	 * @param isMarketable 是否上架
	 * @return
	 */
	List<Goods> getGoodsByFormal(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable , Long relationId);
	
	/**
	 * 查询分销商品
	 * @param supplier
	 * @param bySupplier
	 * @return
	 */
	List<Goods> queryDistributionGoods(Supplier supplier , Long supplierSupplierId);
	
	/**
	 * 查询当前分类下面是否存在
	 * @param supplier
	 * @param productCategory
	 * @return
	 */
	List<Goods> findByProductCategoryId(Supplier supplier , ProductCategory productCategory , SupplierSupplier supplierSupplier);

	Page<Goods> findPage(Supplier supplier, String goodName,ProductCategory productCategory, Pageable pageable);
	
	List<Goods> findByName(String name, Supplier supplier, Types types);

	Page<Goods> findPage(AssGoodDirectory assGoodDirectory, String goodName, Pageable pageable);
	
	void removeDirectoryGoods(Long goodId);
	
	/**
	 * 
	 * @Title: supplyOfGoods
	 * @author: yuezhiwei
	 * @date: 2018年2月7日下午6:40:57
	 * @Description: 查询供应中的商品（包括企业供应和个体供应）
	 * @id 商品id
	 * @return: List<Goods>
	 */
	List<Goods> supplyOfGoods(Long id);
	
	/**
	 * 
	 * @Title: findPage
	 * @author: yuezhiwei
	 * @date: 2018年3月8日下午8:21:41
	 * @Description: 定货么小程序查询本地商品列表
	 * @return: Page<Goods>
	 */
	Page<Goods> findPage(Supplier supplier , ProductCategory productCategory , Pageable pageable , String searchName);
	
	/**
	 * 
	 * @Title: findLocalGoodsList
	 * @author: yuezhiwei
	 * @date: 2018年3月27日下午1:46:17
	 * @Description: 小程序查询本地商品
	 * @return: List<Goods>
	 */
	List<Goods> findLocalGoodsList(Supplier supplier , Types types , ProductCategory productCategory, String searchName);
	
	/**
	 * 
	 * @Title: existLocalDistributionGoods
	 * @author: yuezhiwei
	 * @date: 2018年3月28日上午11:11:54
	 * @Description: 判断本地商品是否已经分配
	 * @return: boolean
	 */
	boolean existLocalDistributionGoods(Supplier supplier , Goods goods);

	/**
	 * 查询给店铺分配的
	 * @param supplyNeed
	 * @param shop
	 * @param supplier
	 * @param goodsName
	 * @param productCategoryId
	 * @param pageable
	 * @return
	 */
    Page<Goods> findPageByNeedShopProduct(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, Long productCategoryId, Pageable pageable);
    /**
     * 企业-->企业-->门店 分配的商品类别
     * @param supplierSupplier
     * @param need
     * @param goodsName
     * @param productCategoryId
     * @param pageable
     * @return
     */
	Page<Goods> findPageBySupplierSupplier(SupplierSupplier supplierSupplier, Need need, String goodsName, Long productCategoryId, Pageable pageable);

	/**
	 * 企业之间供应的商品列表
	 * @param supplierSupplier
	 * @param goodsName
	 * @param productCategoryId
	 * @param pageable
	 * @return
	 */
	Page<Goods> findPageBySupplierSupplierGoods(SupplierSupplier supplierSupplier, String goodsName, Long productCategoryId, Pageable pageable);
	
	/**
	 * 门店供应的商品列表
	 * @param supplyNeed
	 * @param goodsName
	 * @param productCategoryId
	 * @param pageable
	 * @return
	 */
	Page<Goods> findPageBySupplyNeed(SupplyNeed supplyNeed,String goodsName, Long productCategoryId, Pageable pageable);
	
	/**
	 * 
	 * @Title: findbyProductCategoryList
	 * @author: yuezhiwei
	 * @date: 2018年4月22日下午3:50:16
	 * @Description: 根据分类查询商品
	 * @return: List<Goods>
	 */
	List<Goods> findbyProductCategoryList(ProductCategory productCategory);
	
	
	List<Goods> findByMember(Member member , String goodsName);
}