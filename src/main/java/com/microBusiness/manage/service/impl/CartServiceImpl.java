/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.

 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.microBusiness.manage.dao.CartDao;
import com.microBusiness.manage.dao.CartItemDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.dao.SupplyNeedDao;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.CartItem.CartType;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.CartService;
import com.microBusiness.manage.service.MemberService;

@Service("cartServiceImpl")
public class CartServiceImpl extends BaseServiceImpl<Cart, Long> implements CartService {

	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;
	@Resource(name = "cartItemDaoImpl")
	private CartItemDao cartItemDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource
	private SupplierDao supplierDao ;
	@Resource
	private SupplierSupplierDao supplierSupplierDao;
	@Resource
	private SupplyNeedDao supplyNeedDao;

	/*public Cart getCurrent() {
		Cart cart;
		Member member = memberService.getCurrent(true);
		if (member != null) {
			cart = member.getCart();
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
			if (request == null) {
				return null;
			}
			String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
			cart = cartDao.findByKey(key);
		}
		if (cart != null) {
			Date expire = DateUtils.addSeconds(new Date(), Cart.TIMEOUT);
			if (!DateUtils.isSameDay(cart.getExpire(), expire)) {
				cart.setExpire(expire);
			}
		}
		return cart;
	}*/
	
	public Cart getCurrent(Member member) {
		return member.getCart();
	}

	public Cart add(Member member, Product product, int quantity) {
		Assert.notNull(product);
		Assert.state(quantity > 0);

		//购物车如果为空，创建一个新购物车
		Cart cart = getCurrent(member);
		if (cart == null) {
			cart = new Cart();
			if (member != null && member.getCart() == null) {
				cart.setMember(member);
			}
			cartDao.persist(cart);
		}
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			cartItem.setQuantity(quantity);
		} else {
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItemDao.persist(cartItem);
			cart.getCartItems().add(cartItem);
		}
		return cart;
	}

	public void merge(Member member, Cart cart) {
		if (member == null || cart == null || cart.getMember() != null) {
			return;
		}
		Cart memberCart = member.getCart();
		if (memberCart != null) {
			if (cart.getCartItems() != null) {
				for (CartItem cartItem : cart.getCartItems()) {
					Product product = cartItem.getProduct();
					if (memberCart.contains(product)) {
						CartItem memberCartItem = memberCart.getCartItem(product);
						if (CartItem.MAX_QUANTITY != null && memberCartItem.getQuantity() + cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						memberCartItem.add(cartItem.getQuantity());
					} else {
						if (Cart.MAX_CART_ITEM_COUNT != null && memberCart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
							continue;
						}
						if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						CartItem item = new CartItem();
						item.setQuantity(cartItem.getQuantity());
						item.setProduct(cartItem.getProduct());
						item.setCart(memberCart);
						cartItemDao.persist(item);
						memberCart.getCartItems().add(cartItem);
					}
				}
			}
			cartDao.remove(cart);
		} else {
			cart.setMember(member);
			member.setCart(cart);
		}
	}

	public void evictExpired() {
		while (true) {
			List<Cart> carts = cartDao.findList(true, 100);
			if (CollectionUtils.isNotEmpty(carts)) {
				for (Cart cart : carts) {
					cartDao.remove(cart);
				}
				cartDao.flush();
				cartDao.clear();
			}
			if (carts.size() < 100) {
				break;
			}
		}
	}

	@Override
	public Cart add(Member member, Product product, int quantity, Long supplierId, SupplyType supplyType, SupplyNeed supplyNeed , Long relationId) {

		Assert.notNull(product);
		Assert.state(quantity > 0);
		//购物车如果为空，创建一个新购物车
		Cart cart = getCurrent(member);
		if (cart == null) {
			cart = new Cart();
			if (member != null && member.getCart() == null) {
				cart.setMember(member);
			}
			cartDao.persist(cart);
		}

		Supplier supplier = supplierDao.find(supplierId) ;

		if (cart.contains(product, supplier , supplyType , relationId)) {
			CartItem cartItem = cart.getCartItem(product , supplier , supplyType , relationId);
			cartItem.setQuantity(quantity);
			cartItem.setSupplyNeed(supplyNeed);
		} else {
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setSupplier(supplier);
			cartItem.setSupplyType(supplyType);
			cartItem.setSupplyNeed(supplyNeed);
			cartItemDao.persist(cartItem);
			cart.getCartItems().add(cartItem);
		}
		return cart;
	}

	@Transactional
	@Override
	public Cart add(Member member,Shop shop,Types types, Product product, int quantity,
			Long supplierId, SupplyType supplyType, Long relationId, 
			SupplierType supplierType, CartType cartType) {
		Assert.notNull(product);
		//购物车如果为空，创建一个新购物车
		Cart cart = getCurrent(member);
		if (cart == null) {
			cart = new Cart();
			if (member != null && member.getCart() == null) {
				cart.setMember(member);
			}
			cartDao.persist(cart);
		}
		Supplier supplier = supplierDao.find(supplierId);
		CartItem cartItem = cartItemDao.getCartItem(cart,shop,types,null,null,supplier,product);
		if (cartItem != null){
			//数量为0就删除
			if (quantity <= 0){
				cartItemDao.remove(cartItem);
			}else {
				cartItem.setQuantity(quantity);
			}
		}else {
			//数量大于0才添加到购物车
			if (quantity > 0){
				cartItem=new CartItem();
				cartItem.setQuantity(quantity);
				cartItem.setProduct(product);
				cartItem.setCart(cart);
				cartItem.setShop(shop);
				cartItem.setTypes(types);
				cartItem.setSupplyType(supplyType);
				cartItem.setSupplier(supplier);
				cartItem.setCartType(cartType);
//				if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
//					SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
//					cartItem.setSupplyNeed(supplyNeed);
//				}else if (SupplierType.TWO.equals(supplierType)){
//					SupplierSupplier supplierSupplier = supplierSupplierDao.find(relationId);
//					cartItem.setSupplierSupplier(supplierSupplier);
//				}
				cartItemDao.persist(cartItem);
				cart.getCartItems().add(cartItem);
			}
		}

		return cart;
	}

	@Override
	public void addMore(Member member, List<OrderItem> orderItems,
			Long supplierId, SupplyType supplyType, Long relationId, 
			SupplierType supplierType,Types types,Shop shop) {
		
		//购物车如果为空，创建一个新购物车
		Cart cart = getCurrent(member);
		if (cart == null) {
			cart = new Cart();
			if (member != null && member.getCart() == null) {
				cart.setMember(member);
			}
			cartDao.persist(cart);
		}
		Supplier supplier = supplierDao.find(supplierId);
		
		for(OrderItem item : orderItems) {
			Product product = item.getProduct();
			int quantity = item.getQuantity();
			CartItem cartItem=cartItemDao.getCartItem(cart,shop,types,null,null,supplier,product);
			if (cartItem != null){
				//数量为0就删除
				if (quantity <= 0){
					cartItemDao.remove(cartItem);
				}else {
					cartItem.setQuantity(quantity);
				}
			}else {
				//数量大于0才添加到购物车
				if (quantity > 0){
					cartItem=new CartItem();
					cartItem.setQuantity(quantity);
					cartItem.setProduct(product);
					cartItem.setCart(cart);
					cartItem.setShop(shop);
					cartItem.setTypes(types);
					cartItem.setSupplyType(supplyType);
					cartItem.setSupplier(supplier);
					if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
						SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
						cartItem.setSupplyNeed(supplyNeed);
					}else if (SupplierType.TWO.equals(supplierType)){
						SupplierSupplier supplierSupplier = supplierSupplierDao.find(relationId);
						cartItem.setSupplierSupplier(supplierSupplier);
					}
					cartItemDao.persist(cartItem);
					cart.getCartItems().add(cartItem);
				}
			}
		}
	}
}