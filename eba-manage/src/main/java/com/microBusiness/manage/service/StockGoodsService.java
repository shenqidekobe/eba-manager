/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;

public interface StockGoodsService extends BaseService<StockGoods, Long> {
	
	/**
	 *  分页查询库存状况
	 * 
	 * @param shop
	 * @param Keyword
	 * @param status
	 * @return
	 */
	Page<StockGoods> page(Shop shop, String Keyword, StockGoods.Status status, Pageable pageable);
	
	List<StockGoods> findByBarCode(String barCode, Shop shop, StockGoods.Status status);
	
	/**
	 * 根据Product和商铺获取库存商品
	 * 
	 * @param product
	 * @param shop
	 * @return
	 */
	StockGoods findByProduct(Product product, Shop shop);
	
	/**
	 * 分配商品时添加库存状况
	 * 
	 * @param shop
	 * @param product
	 */
	void save(Shop shop, List<Product> products);
	
	/**
	 * 根据member获取库存状况
	 * 
	 * @param product
	 * @param member
	 * @return
	 */
	List<StockGoods> getStockGoodsByMember(Product product, Member member, Shop shop);
}