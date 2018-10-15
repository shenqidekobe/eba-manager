/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity.ass;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.entity.BaseEntity;

@Entity
@Table(name = "ass_cart_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_cart_item")
public class AssCartItem extends BaseEntity<Long> {

	private static final long serialVersionUID = 1445548892193439413L;

	public static final Integer MAX_QUANTITY = 10000;

	private Integer quantity;

	private AssProduct assproduct;

	private AssCart cart;
	
	private BigDecimal newPrice;
	
	private BigDecimal newPriceUnit;
	
	private BigDecimal newBToBPrice;
	
	private BigDecimal newBToBPriceUnit;
	
	private AssCustomerRelation assCustomerRelation;
	
	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public AssProduct getAssproduct() {
		return assproduct;
	}
	public void setAssproduct(AssProduct assproduct) {
		this.assproduct = assproduct;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public AssCart getCart() {
		return cart;
	}

	public void setCart(AssCart cart) {
		this.cart = cart;
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
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}
	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}
	
}
