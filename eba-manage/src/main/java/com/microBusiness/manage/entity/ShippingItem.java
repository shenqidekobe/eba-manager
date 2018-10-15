/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;

@Entity
@Table(name = "xx_shipping_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_shipping_item")
public class ShippingItem extends BaseEntity<Long> {

	private static final long serialVersionUID = 294225605790468781L;

	private String sn;

	private String name;

	private Integer quantity;

	private Boolean isDelivery;

	private Product product;

	private Shipping shipping;

	private List<String> specifications = new ArrayList<String>();

	/**
	 * 实际收货数量
	 */
	private Integer realQuantity ;

	@Column(nullable = false, updatable = false)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(nullable = false, updatable = false)
	public Boolean getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	@Column(updatable = false, length = 4000)
	@Convert(converter = SpecificationConverter.class)
	public List<String> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<String> specifications) {
		this.specifications = specifications;
	}

	@Converter
	public static class SpecificationConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

	public Integer getRealQuantity() {
		return realQuantity;
	}

	public void setRealQuantity(Integer realQuantity) {
		this.realQuantity = realQuantity;
	}
	
	@Transient
	public Integer getTrueRealQuantity() {
		return null == this.realQuantity||!this.shipping.getStatus().equals(Shipping.Status.senderChecked)? 0:this.realQuantity;
	}
	
}
