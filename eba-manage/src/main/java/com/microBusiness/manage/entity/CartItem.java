/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.math.BigDecimal;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.util.SystemUtils;

@Entity
@Table(name = "xx_cart_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_cart_item")
public class CartItem extends BaseEntity<Long> {

	private static final long serialVersionUID = 1445548892193439413L;

	public static final Integer MAX_QUANTITY = 10000;

	private Integer quantity;

	private Product product;

	private Cart cart;
	
	private BigDecimal newPrice;
	
	private BigDecimal newPriceUnit;
	
	private BigDecimal newBToBPrice;
	
	private BigDecimal newBToBPriceUnit;


	private Supplier supplier ;

	private SupplyType supplyType ;
	
	private SupplyNeed supplyNeed;
	
	private SupplierSupplier supplierSupplier;

	//2018-3-10 新增
	private Shop shop;
	//2018-3-10 新增，下单类型 平台or本地
	private Types types;
	//2018-3-29 新增,是否有效   默认有效
	private Boolean valid=true;
	
	
	private CartType cartType;
	
	public enum CartType{
		personal,
		proxy
	}
	
	public CartType getCartType() {
		return cartType;
	}

	public void setCartType(CartType cartType) {
		this.cartType = cartType;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
	public SupplyNeed getSupplyNeed() {
		return supplyNeed;
	}
	
	public void setSupplyNeed(SupplyNeed supplyNeed) {
		this.supplyNeed = supplyNeed;
	}

	
	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Transient
	public int getWeight() {
		if (getProduct() != null && getProduct().getWeight() != null && getQuantity() != null) {
			return getProduct().getWeight() * getQuantity();
		} else {
			return 0;
		}
	}

	@Transient
	public long getRewardPoint() {
		if (getProduct() != null && getProduct().getRewardPoint() != null && getQuantity() != null) {
			return getProduct().getRewardPoint() * getQuantity();
		} else {
			return 0L;
		}
	}

	@Transient
	public long getExchangePoint() {
		if (getProduct() != null && getProduct().getExchangePoint() != null && getQuantity() != null) {
			return getProduct().getExchangePoint() * getQuantity();
		} else {
			return 0L;
		}
	}

	@Transient
	public BigDecimal getPrice() {
		if (getProduct() != null && getProduct().getPrice() != null) {
			Setting setting = SystemUtils.getSetting();
			if (getCart() != null && getCart().getMember() != null && getCart().getMember().getMemberRank() != null) {
				MemberRank memberRank = getCart().getMember().getMemberRank();
				if (memberRank.getScale() != null) {
					return setting.setScale(getProduct().getPrice().multiply(new BigDecimal(String.valueOf(memberRank.getScale()))));
				}
			}
			return setting.setScale(getProduct().getPrice());
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Transient
	public BigDecimal getSubtotal() {
		if (getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getQuantity()));
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Transient
	public boolean getIsMarketable() {
		return getProduct() != null && getProduct().getIsMarketable();
	}

	@Transient
	public boolean getIsDelivery() {
		return getProduct() != null && getProduct().getIsDelivery();
	}

	@Transient
	public boolean getIsLowStock() {
		return getQuantity() != null && getProduct() != null && getQuantity() > getProduct().getAvailableStock();
	}

	@Transient
	public void add(int quantity) {
		if (quantity < 1) {
			return;
		}
		if (getQuantity() != null) {
			setQuantity(getQuantity() + quantity);
		} else {
			setQuantity(quantity);
		}
	}

	@Transient
	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}
	
	@Transient
	public BigDecimal getNewBToBPrice() {
		return newBToBPrice;
	}

	public void setNewBToBPrice(BigDecimal newBToBPrice) {
		this.newBToBPrice = newBToBPrice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}
	@Transient
	public BigDecimal getNewPriceUnit() {
		return newPriceUnit;
	}

	public void setNewPriceUnit(BigDecimal newPriceUnit) {
		this.newPriceUnit = newPriceUnit;
	}
	@Transient
	public BigDecimal getNewBToBPriceUnit() {
		return newBToBPriceUnit;
	}

	public void setNewBToBPriceUnit(BigDecimal newBToBPriceUnit) {
		this.newBToBPriceUnit = newBToBPriceUnit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public SupplierSupplier getSupplierSupplier() {
		return supplierSupplier;
	}

	public void setSupplierSupplier(SupplierSupplier supplierSupplier) {
		this.supplierSupplier = supplierSupplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
}
