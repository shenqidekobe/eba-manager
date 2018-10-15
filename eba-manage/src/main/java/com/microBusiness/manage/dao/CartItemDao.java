/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.CartItem;

import java.util.List;

public interface CartItemDao extends BaseDao<CartItem, Long> {
	
	public CartItem deleteByCartItem(CartItem cartItem);

	/**
	 * 根据店铺获取购物车详细
	 * @param cart					购物车主表            	必须
	 * @param shop					店铺					必须
	 * @param types					平台订单 or 本地订单	必须
	 * @param supplyNeed			供应类型为1、3的时候必须 	三选一
	 * @param supplierSupplier		供应类型为 2 的时候必须		三选一
	 * @param supplier				供应类型为 4 的时候必须		三选一
	 * @return
	 */
	List<CartItem> getCartItems(Cart cart, Shop shop,Types types,SupplyNeed supplyNeed,SupplierSupplier supplierSupplier,Supplier supplier);

	/**
	 * 根据店铺和商品获取购物车里面的商品
	 * @param cart					购物车主表            	必须
	 * @param shop					店铺					必须
	 * @param types					平台订单 or 本地订单	必须
	 * @param supplyNeed			供应类型为1、3的时候必须 	三选一
	 * @param supplierSupplier		供应类型为 2 的时候必须		三选一
	 * @param supplier				供应类型为 4 的时候必须		三选一
	 * @param product               商品      				必须
	 * @return
	 */
	CartItem getCartItem(Cart cart, Shop shop,Types types,SupplyNeed supplyNeed,SupplierSupplier supplierSupplier,Supplier supplier,Product product);

}