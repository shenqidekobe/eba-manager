/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity.ass;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Coupon;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

@Entity
@Table(name = "ass_cart")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_cart")
public class AssCart extends BaseEntity<Long> {

	private static final long serialVersionUID = -2037605296404167869L;

	public static final int TIMEOUT = 604800;

	public static final Integer MAX_CART_ITEM_COUNT = 100;

	public static final String KEY_COOKIE_NAME = "cartKey";

	public static final String QUANTITY_COOKIE_NAME = "cartQuantity";

	private AssChildMember assChildMember;
	
	private Set<AssCartItem> cartItems = new HashSet<AssCartItem>();

	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	public Set<AssCartItem> getCartItems() {
		return cartItems;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	public void setCartItems(Set<AssCartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@Transient
	public int getProductQuantity() {
		int productQuantity = 0;
		if (getCartItems() != null) {
			for (AssCartItem cartItem : getCartItems()) {
				if (cartItem.getQuantity() != null) {
					productQuantity += cartItem.getQuantity();
				}
			}
		}
		return productQuantity;
	}

	@Transient
	public AssCartItem getCartItem(AssProduct product) {
		if (product != null && getCartItems() != null) {
			for (AssCartItem cartItem : getCartItems()) {
				if (cartItem.getAssproduct() != null && cartItem.getAssproduct().equals(product)) {
					return cartItem;
				}
			}
		}
		return null;
	}
	
	@Transient
	public boolean contains(AssProduct product) {
		return getCartItem(product) != null;
	}

	@Transient
	public boolean contains(AssCartItem cartItem) {
		if (cartItem != null && getCartItems() != null) {
			return getCartItems().contains(cartItem);
		}
		return false;
	}


	@Transient
	public boolean isValid(Coupon coupon) {
		if (coupon == null || !coupon.getIsEnabled() || !coupon.hasBegun() || coupon.hasExpired()) {
			return false;
		}
		if ((coupon.getMinimumQuantity() != null && coupon.getMinimumQuantity() > getProductQuantity()) || (coupon.getMaximumQuantity() != null && coupon.getMaximumQuantity() < getProductQuantity())) {
			return false;
		}
		return true;
	}

	@Transient
	public boolean isValid(CouponCode couponCode) {
		if (couponCode == null || couponCode.getIsUsed() || couponCode.getCoupon() == null) {
			return false;
		}
		return isValid(couponCode.getCoupon());
	}

	@Transient
	public boolean hasNotMarketable() {
		return CollectionUtils.exists(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				AssCartItem cartItem = (AssCartItem) object;
				return cartItem != null ;// && !cartItem.getIsMarketable();
			}
		});
	}

	@Transient
	public boolean isEmpty() {
		return CollectionUtils.isEmpty(getCartItems());
	}

	@Transient
	public AssCartItem getCartItem(AssProduct product , AssCustomerRelation assCustomerRelation) {
		if (product != null && getCartItems() != null) {
			for (AssCartItem cartItem : getCartItems()) {
				if (cartItem.getAssproduct().getId().equals(product.getId()) && cartItem.getAssCustomerRelation().getId().equals(assCustomerRelation.getId())) {
					return cartItem;
				}
			}
		}
		return null;
	}

	@Transient
	public boolean contains(AssProduct product , AssCustomerRelation assCustomerRelation) {
		return getCartItem(product , assCustomerRelation) != null;
	}

	@Transient
	public int getQuantity(Supplier supplier , SupplyType supplyType,Long relationId) {
		int productQuantity = 0;
		Set<AssCartItem> cartItems=getCartItems();
		if (cartItems != null) {
			for (AssCartItem cartItem : cartItems) {
				if (cartItem.getQuantity() != null) {
					productQuantity += cartItem.getQuantity();
				}
			}
		}
		return productQuantity;
	}

	@Transient
	public AssCartItem getCartItem(AssCustomerRelation assCustomerRelation) {
		for (AssCartItem cartItem : getCartItems()) {
			if (cartItem.getAssCustomerRelation().getId().equals(assCustomerRelation.getId())) {
				return cartItem;
			}
		}
		return null;
	}
	
	public Set<AssCartItem> getCartItems(Long assCustomerRelationId) {
		Set<AssCartItem> cartItems = new HashSet<AssCartItem>();
		for(AssCartItem cartItem : this.cartItems){
			if (cartItem.getAssCustomerRelation().getId().equals(assCustomerRelationId) ) {
				cartItems.add(cartItem) ;
			}
		}
		return cartItems;
	}
}
