/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.*;

import java.util.Set;

public interface CartItemService extends BaseService<CartItem, Long> {

	public CartItem deleteByCartItem(CartItem cartItem);

	CartItem deleteByCartItem(CartItem cartItem , Long supplierId);

	/**
	 * 根据供应类型获取购物车详细
	 * @param cart			购物车主表
	 * @param shop			店铺
	 * @param supplierId	供应商id
	 * @param relationId	供应关系id
	 * @param supplierType	供应类型
	 * @param types			平台  or  本地
	 * @return
	 */
	Set<CartItem> getCartItems(Cart cart,Shop shop, Long supplierId,Long relationId,SupplierType supplierType,Types types);
	
	void refreshCartItem(CartItem cartItem);

}