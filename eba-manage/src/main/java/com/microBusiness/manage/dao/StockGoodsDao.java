package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;

public interface StockGoodsDao extends BaseDao<StockGoods, Long> {

	/**
	 * 根据Product和商铺获取库存商品
	 * 
	 * @param product
	 * @param shop
	 * @return
	 */
	StockGoods findByProduct(Product product, Shop shop);

	List<StockGoods> findByBarCode(String barCode, Shop shop, StockGoods.Status status);
	
	/**
	 * 分页查询库存状况
	 * 
	 * @param shop
	 * @param Keyword
	 * @param status
	 * @return
	 */
	Page<StockGoods> page(Shop shop, String Keyword, StockGoods.Status status, Pageable pageable);
	
	/**
	 * 根据member获取库存状况
	 * 
	 * @param product
	 * @param member
	 * @return
	 */
	List<StockGoods> getStockGoodsByMember(Product product, Member member, Shop shop);
}