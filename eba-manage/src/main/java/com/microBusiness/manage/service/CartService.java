/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem.CartType;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Types;

public interface CartService extends BaseService<Cart, Long> {

	Cart getCurrent(Member member);

	Cart add(Member member, Product product, int quantity);

	void merge(Member member, Cart cart);

	void evictExpired();
	
	void clearCart(Cart cart);

	Cart add(Member member, Product product, int quantity , Long supplierId , SupplyType supplyType, SupplyNeed supplyNeed , Long relationId);
	
	Cart add(Member member,Shop shop,Types types, Product product, int quantity , Long supplierId , SupplyType supplyType, Long relationId, SupplierType supplierType, CartType cartType);
	
	void addMore(Member member, List<OrderItem> orderItems, Long supplierId,SupplyType supplyType, Long relationId,SupplierType supplierType,Types types,Shop shop);

}