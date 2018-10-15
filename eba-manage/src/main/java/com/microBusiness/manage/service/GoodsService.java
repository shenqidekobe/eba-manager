/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.Filter;

public interface GoodsService extends BaseService<Goods, Long> {

	boolean snExists(String sn, Supplier supplier);

	Goods findBySn(String sn, Supplier supplier);

	List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	List<Goods> findList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
                         Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count);

	Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable);

	Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable);

	Page<Goods> findPage(Member member, Pageable pageable);
	
	Page<Goods> findPage(Supplier supplier,String goodName,ProductCategory productCategory, Pageable pageable);
	
	Page<Goods> findPage(AssGoodDirectory assGoodDirectory,String goodName,Pageable pageable);
	

	Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert);

	long viewHits(Long id);

	void addHits(Goods goods, long amount);

	void addSales(Goods goods, long amount);

	Goods save(Goods goods, Product product, Admin operator);
	
	Goods save(Goods goods, List<Product> products, Admin operator);
	
	Goods save(Goods goods, Supplier supplier);

	Goods update(Goods goods, Product product, Admin operator);
	
	Goods update(Goods goods, Supplier supplier);

	Goods update(Goods goods, List<Product> products, Admin operator);


	Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
						 Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable , Supplier supplier, Boolean sourceFlag, Boolean branchFlag);

	Page<Goods> findBySupplierSupplierPage(List<Long> goodIds, Pageable pageable , Long productCategoryId);

	/**
	 * 获取正式供应的商品
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @return
	 */
	List<Goods> getGoodsByFormal(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable) ;

	/**
	 * 处理供应商品
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @param pageable
	 * @return
	 */
	List<Map<String , Object>> getFormalGoodsDealed(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable , SupplyType supplyType , Long relationId) ;

	/**
	 * 处理过的临时供应商品
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @param pageable
	 * @return
	 */
	Set<Map<String , Object>> getTemporaryGoodsDealed(Long needId, Long supplierId, String keywords, Integer isMarketable, Pageable pageable) ;
	
	/**
	 * 处理分销商品
	 * @param supplierSupplier
	 * @param goodsList
	 * @param supplier
	 * @return
	 */
	List<Product> copySupplierGoods(SupplierSupplier supplierSupplier , List<Long> goodsList , Supplier supplier);

	/**
	 * 查询分销商品
	 * @param supplier
	 * @param bySupplier
	 * @return
	 */
	List<Goods> queryDistributionGoods(Supplier supplier , Long supplierSupplierId);
	
	void removeGood(Long[] ids , Supplier supplier);
	
	GoodsImportLog dealImportMore(MultipartFile multipartFile , Admin admin , Supplier supplier);

	boolean saveMore(GoodsImportLog goodsImportLog, Admin operator);
	
	boolean saveMore(String batch, Admin operator);
	
	List<Goods> findByName(String name, Supplier supplier,Types types);
	
	Map<String, Object> uploadImage(String fileName, String url, Supplier supplier, String batch);
	
	/**
	 * 
	 * @Title: supplyOfGoods
	 * @author: yuezhiwei
	 * @date: 2018年2月7日下午6:38:52
	 * @Description: 查询供应中的商品（包括企业供应和个体供应）
	 * @id 商品id
	 * @return: List<Goods>
	 */
	List<Goods> supplyOfGoods(Long id);
	
	/**
	 * 
	 * @Title: findPage
	 * @author: yuezhiwei
	 * @date: 2018年3月8日下午8:20:10
	 * @Description: 定货么小程序查询本地商品列表
	 * @return: Page<Goods>
	 */
	Page<Goods> findPage(Supplier supplier , ProductCategory productCategory , Pageable pageable , String searchName);
	
	/**
	 * 
	 * @Title: save
	 * @author: yuezhiwei
	 * @date: 2018年3月14日上午10:58:36
	 * @Description: 小程序添加本地商品
	 * @return: Goods
	 */
	Goods save(Goods goods, List<Product> products , Supplier supplier);
	
	/**
	 * 
	 * @Title: save
	 * @author: yuezhiwei
	 * @date: 2018年3月14日上午11:03:23
	 * @Description: 小程序添加本地商品
	 * @return: Goods
	 */
	Goods save(Goods goods, Product product, Supplier supplier);
	
	/**
	 * 
	 * @Title: update
	 * @author: yuezhiwei
	 * @date: 2018年3月15日上午11:38:02
	 * @Description: 小程序更新本地商品
	 * @return: Goods
	 */
	Goods update(Goods goods, Product product, Supplier supplier);
	
	/**
	 * 
	 * @Title: update
	 * @author: yuezhiwei
	 * @date: 2018年3月15日下午1:48:22
	 * @Description: 小程序更新本地商品
	 * @return: Goods
	 */
	Goods update(Goods goods, List<Product> products, Supplier supplier);
	
	/**
	 * 小程序编辑商品
	 * 
	 * @param goods
	 * @param products
	 * @param supplier
	 * @return
	 */
	Goods updateSmall(Goods goods, List<Product> products, Supplier supplier);
	
	/**
	 * 
	 * @Title: findLocalGoodsList
	 * @author: yuezhiwei
	 * @date: 2018年3月27日下午1:45:25
	 * @Description: 小程序查询本地商品
	 * @return: List<Goods>
	 */
	List<Goods> findLocalGoodsList(Supplier supplier , Types types , ProductCategory productCategory, String searchName);

	/**
	 * 查询给店铺分配的
	 * @param relationId
	 * @param shop
	 * @param supplier
	 * @param supplierType
	 * @param goodsName
	 * @param productCategoryId
	 * @param pageable
	 * @return
	 */
	Map<String,Object> findPageByAssign(Long relationId,Shop shop,Supplier supplier,SupplierType supplierType,String goodsName,Long productCategoryId,Pageable pageable);
	
	/**
	 * 查询给供应的商品
	 * @param relationId
	 * @param shop
	 * @param supplier
	 * @param supplierType
	 * @param goodsName
	 * @param productCategoryId
	 * @param pageable
	 * @return
	 */
	Map<String,Object> findPageBySupply(Long relationId,Supplier supplier,SupplierType supplierType,String goodsName,ProductCategory productCategory,Pageable pageable);
	
	/**
	 * 
	 * @Title: existLocalDistributionGoods
	 * @author: yuezhiwei
	 * @date: 2018年3月28日上午11:10:52
	 * @Description: 判断本地商品是否已经分配
	 * @return: boolean
	 */
	boolean existLocalDistributionGoods(Supplier supplier , Goods goods);
	
	
	Map<String, Object> barCodeSave(ProductCenter productCenter,ProductCategory productCategory,List<Long> supplierIds);
	
	/**
	 * 
	 * @Title: findbyProductCategoryList
	 * @author: yuezhiwei
	 * @date: 2018年4月22日下午3:50:16
	 * @Description: 根据分类查询商品
	 * @return: List<Goods>
	 */
	List<Goods> findbyProductCategoryList(ProductCategory productCategory);
	
}