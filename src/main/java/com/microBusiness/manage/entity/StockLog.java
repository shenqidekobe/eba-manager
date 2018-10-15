/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "xx_stock_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_stock_log")
public class StockLog extends BaseEntity<Long> {

	private static final long serialVersionUID = 2635370795217957574L;

	public enum Type {

		stockIn,

		stockOut
	}

	private Type type;

	private Integer inQuantity;

	private Integer outQuantity;

	private Integer stock;

	private String operator;

	private String memo;

	private Product product;

	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false, updatable = false)
	public Integer getInQuantity() {
		return inQuantity;
	}

	public void setInQuantity(Integer inQuantity) {
		this.inQuantity = inQuantity;
	}

	@Column(nullable = false, updatable = false)
	public Integer getOutQuantity() {
		return outQuantity;
	}

	public void setOutQuantity(Integer outQuantity) {
		this.outQuantity = outQuantity;
	}

	@Column(nullable = false, updatable = false)
	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Column(nullable = false, updatable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(updatable = false)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transient
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}

}
