/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.BaseAttributeConverter;

@Entity
@Table(name = "xx_order_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_item")
public class OrderItem extends BaseEntity<Long> {

	private static final long serialVersionUID = -3851446795548540158L;

	private String sn;

	private String name;

	private Goods.Type type;
	//个体客户总价
	private BigDecimal price;
	//个体客户单价
	private BigDecimal priceUnit;
	//企业用户总价
	private BigDecimal priceB;
	//企业用户单价
	private BigDecimal priceUnitB;

	private Integer weight;

	private Boolean isDelivery;

	private String thumbnail;

	private Integer quantity;

	private Integer shippedQuantity;

	private Integer returnedQuantity;

	private Product product;

	private Order order;

	private List<String> specifications = new ArrayList<String>();


	private Integer checkQuantity ;
	
	private Long productId;
	
	private BigDecimal supplyPrice;
	
	private Integer minOrderQuantity ;
	
	public enum Status {
		add,
		edit,
		delete
	}

	private Status status;
	
	private ProxyUser proxyUser;
	
	//积分返利
	private ChildMember uone;
	private BigDecimal uone_score;
	private ChildMember utwo;
	private BigDecimal utwo_score;
	private ChildMember uthree;
	private BigDecimal uthree_score;
	
	//分销利润
	private ChildMember done;
	private BigDecimal done_score;
	private ChildMember dtwo;
	private BigDecimal dtwo_score;
	private ChildMember dthree;
	private BigDecimal dthree_score;
	
	public BigDecimal getUone_score() {
		return uone_score;
	}

	public void setUone_score(BigDecimal uone_score) {
		this.uone_score = uone_score;
	}

	public BigDecimal getUtwo_score() {
		return utwo_score;
	}

	public void setUtwo_score(BigDecimal utwo_score) {
		this.utwo_score = utwo_score;
	}

	public BigDecimal getUthree_score() {
		return uthree_score;
	}

	public void setUthree_score(BigDecimal uthree_score) {
		this.uthree_score = uthree_score;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getUone() {
		return uone;
	}

	public void setUone(ChildMember uone) {
		this.uone = uone;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getUtwo() {
		return utwo;
	}

	public void setUtwo(ChildMember utwo) {
		this.utwo = utwo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getUthree() {
		return uthree;
	}

	public void setUthree(ChildMember uthree) {
		this.uthree = uthree;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDone() {
		return done;
	}

	public void setDone(ChildMember done) {
		this.done = done;
	}

	public BigDecimal getDone_score() {
		return done_score;
	}

	public void setDone_score(BigDecimal done_score) {
		this.done_score = done_score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDtwo() {
		return dtwo;
	}

	public void setDtwo(ChildMember dtwo) {
		this.dtwo = dtwo;
	}

	public BigDecimal getDtwo_score() {
		return dtwo_score;
	}

	public void setDtwo_score(BigDecimal dtwo_score) {
		this.dtwo_score = dtwo_score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true ,foreignKey = @ForeignKey(name = "null"))
	public ChildMember getDthree() {
		return dthree;
	}

	public void setDthree(ChildMember dthree) {
		this.dthree = dthree;
	}

	public BigDecimal getDthree_score() {
		return dthree_score;
	}

	public void setDthree_score(BigDecimal dthree_score) {
		this.dthree_score = dthree_score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public ProxyUser getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(ProxyUser proxyUser) {
		this.proxyUser = proxyUser;
	}
	
	
	
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

	@Column(nullable = false, updatable = false)
	public Goods.Type getType() {
		return type;
	}

	public void setType(Goods.Type type) {
		this.type = type;
	}

	@Column(nullable = false, updatable = true, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(updatable = true)
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Column(nullable = false, updatable = false)
	public Boolean getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	@Column(updatable = false)
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Column(nullable = false, updatable = true)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(nullable = false)
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	@Column(nullable = false)
	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column(updatable = false, length = 4000)
	@Convert(converter = SpecificationConverter.class)
	public List<String> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<String> specifications) {
		this.specifications = specifications;
	}

	@Transient
	public int getTotalWeight() {
		if (getWeight() != null && getQuantity() != null) {
			return getWeight() * getQuantity();
		} else {
			return 0;
		}
	}

	@Transient
	public BigDecimal getSubtotal() {
		if (getPrice() != null && getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getQuantity()));
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Transient
	public int getShippableQuantity() {
		int shippableQuantity = getQuantity() - getShippedQuantity();
		return shippableQuantity >= 0 ? shippableQuantity : 0;
	}

	@Transient
	public int getReturnableQuantity() {
		int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
		return returnableQuantity >= 0 ? returnableQuantity : 0;
	}

	@Converter
	public static class SpecificationConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

	@Transient
	public Integer getCheckQuantity() {
		return checkQuantity;
	}

	public void setCheckQuantity(Integer checkQuantity) {
		this.checkQuantity = checkQuantity;
	}
	
	@Transient
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Transient
	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	@Transient
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(BigDecimal priceUnit) {
		this.priceUnit = priceUnit;
	}

	public BigDecimal getPriceB() {
		return priceB;
	}

	public void setPriceB(BigDecimal priceB) {
		this.priceB = priceB;
	}

	public BigDecimal getPriceUnitB() {
		return priceUnitB;
	}

	public void setPriceUnitB(BigDecimal priceUnitB) {
		this.priceUnitB = priceUnitB;
	}
	
	@Transient
	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}
	
}
