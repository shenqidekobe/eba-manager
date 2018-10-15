/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.dao.SupplyNeedDao;
import com.microBusiness.manage.entity.*;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.CartItemDao;
import com.microBusiness.manage.service.CartItemService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("cartItemServiceImpl")
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, Long> implements CartItemService {
	
	@Resource
	private CartItemDao cartItemDao;
	@Resource
	private SupplierDao supplierDao ;
	@Resource
	private SupplyNeedDao supplyNeedDao;
	@Resource
	private SupplierSupplierDao supplierSupplierDao;


	@Override
	public CartItem deleteByCartItem(CartItem cartItem) {
		cartItemDao.deleteByCartItem(cartItem);
		return cartItem;
	}

	@Override
	public CartItem deleteByCartItem(CartItem cartItem, Long supplierId) {
		cartItem.setSupplier(supplierDao.find(supplierId));
		cartItemDao.deleteByCartItem(cartItem);
		return cartItem;
	}

	@Override
	public Set<CartItem> getCartItems(Cart cart, Shop shop, Long supplierId, Long relationId, SupplierType supplierType,Types types) {
		List<CartItem> cartItems=new ArrayList<>();
//		if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
//			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
//			cartItems=cartItemDao.getCartItems(cart,shop,types,supplyNeed,null,null);
//		}else if (SupplierType.TWO.equals(supplierType)){
//			SupplierSupplier supplierSupplier=supplierSupplierDao.find(relationId);
//			cartItems=cartItemDao.getCartItems(cart,shop,types,null,supplierSupplier,null);
//		}else if (SupplierType.FOUR.equals(supplierType)){
//			Supplier supplier=supplierDao.find(supplierId);
//			cartItems=cartItemDao.getCartItems(cart,shop,types,null,null,supplier);
//		}
		Supplier supplier=supplierDao.find(supplierId);
		cartItems=cartItemDao.getCartItems(cart,shop,types,null,null,supplier);
		return new HashSet<>(cartItems);
	}

	@Override
	public void refreshCartItem(CartItem cartItem) {
		cartItemDao.persist(cartItem);
	}
}