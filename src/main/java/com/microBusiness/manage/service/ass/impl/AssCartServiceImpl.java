/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.

 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.ass.AssCartDao;
import com.microBusiness.manage.dao.ass.AssCartItemDao;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.ass.AssCartService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("assCartServiceImpl")
public class AssCartServiceImpl extends BaseServiceImpl<AssCart, Long> implements AssCartService {

	@Resource
	private AssCartDao assCartDao;
	@Resource
	private AssCartItemDao assCartItemDao;
	@Resource
	private AssCustomerRelationDao assCustomerRelationDao;
	
	public AssCart getCurrent(AssChildMember assChildMember) {
		return assChildMember.getAssCart();
	}

	public AssCart add(AssChildMember assChildMember, AssProduct assproduct, int quantity) {
		Assert.notNull(assproduct);
		Assert.state(quantity > 0);

		//购物车如果为空，创建一个新购物车
		AssCart cart = getCurrent(assChildMember);
		if (cart == null) {
			cart = new AssCart();
			if (assChildMember != null && assChildMember.getAssCart() == null) {
				cart.setAssChildMember(assChildMember);
			}
			assCartDao.persist(cart);
		}
		if (cart.contains(assproduct)) {
			AssCartItem cartItem = cart.getCartItem(assproduct);
			cartItem.setQuantity(quantity);
		} else {
			AssCartItem cartItem = new AssCartItem();
			cartItem.setQuantity(quantity);
			cartItem.setAssproduct(assproduct);
			cartItem.setCart(cart);
			assCartItemDao.persist(cartItem);
			cart.getCartItems().add(cartItem);
		}
		return cart;
	}

	@Override
	public AssCart add(AssChildMember assChildMember, AssProduct product, Long assCustomerRelationId,Integer quantity) {

		Assert.notNull(product);
		Assert.state(quantity > 0);
		//购物车如果为空，创建一个新购物车
		AssCart cart = getCurrent(assChildMember);
		if (cart == null) {
			cart = new AssCart();
			if (assChildMember != null && assChildMember.getAssCart() == null) {
				cart.setAssChildMember(assChildMember);
			}
			assCartDao.persist(cart);
		}

		AssCustomerRelation assCustomerRelation = assCustomerRelationDao.find(assCustomerRelationId);
		if (cart.contains(product, assCustomerRelation )) {
			AssCartItem cartItem = cart.getCartItem(product , assCustomerRelation);
			cartItem.setQuantity(quantity);
		} else {
			AssCartItem cartItem = new AssCartItem();
			cartItem.setQuantity(quantity);
			cartItem.setAssproduct(product);
			cartItem.setCart(cart);
			cartItem.setAssCustomerRelation(assCustomerRelation);
			assCartItemDao.persist(cartItem);
			cart.getCartItems().add(cartItem);
		}
		return cart;
	}

//	@Override
//	public Cart add(Member member, Product product, int quantity,
//			Long supplierId, SupplyType supplyType, Long relationId) {
//		Assert.notNull(product);
//		Assert.state(quantity > 0);
//		//购物车如果为空，创建一个新购物车
//		Cart cart = getCurrent(member);
//		if (cart == null) {
//			cart = new Cart();
//			if (member != null && member.getCart() == null) {
//				cart.setMember(member);
//			}
//			cartDao.persist(cart);
//		}
//
//		Supplier supplier = supplierDao.find(supplierId) ;
//		
//		if(supplyType.equals(SupplyType.formal)) {
//			SupplierSupplier supplierSupplier = supplierSupplierDao.find(relationId);
//			CartItem cartItem = cart.getCartItem(product , supplier , supplyType , relationId);
//			if (cartItem != null) {
//				cartItem.setQuantity(quantity);
//				cartItem.setSupplierSupplier(supplierSupplier);
//			} else {
//				cartItem = new CartItem();
//				cartItem.setQuantity(quantity);
//				cartItem.setProduct(product);
//				cartItem.setCart(cart);
//				cartItem.setSupplier(supplier);
//				cartItem.setSupplyType(supplyType);
//				cartItem.setSupplierSupplier(supplierSupplier);
//				cartItemDao.persist(cartItem);
//				cart.getCartItems().add(cartItem);
//			}
//		}else {
//			SupplyNeed supplyNeed = supplyNeedDao.find(relationId);
//			if (cart.contains(product, supplier , supplyType , relationId)) {
//				CartItem cartItem = cart.getCartItem(product , supplier , supplyType , relationId);
//				cartItem.setQuantity(quantity);
//				cartItem.setSupplyNeed(supplyNeed);
//			} else {
//				CartItem cartItem = new CartItem();
//				cartItem.setQuantity(quantity);
//				cartItem.setProduct(product);
//				cartItem.setCart(cart);
//				cartItem.setSupplier(supplier);
//				cartItem.setSupplyType(supplyType);
//				cartItem.setSupplyNeed(supplyNeed);
//				cartItemDao.persist(cartItem);
//				cart.getCartItems().add(cartItem);
//			}
//		}
//		return cart;
//	}
}