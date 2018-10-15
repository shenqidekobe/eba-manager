/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_shipping")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_shipping")
public class Shipping extends BaseEntity<Long> {

	private static final long serialVersionUID = -7190570648577770156L;

	public interface Delivery extends Default {

	}

	private String sn;

	private String shippingMethod;

	private String deliveryCorp;

	private String deliveryCorpUrl;

	private String deliveryCorpCode;

	private String trackingNo;

	private BigDecimal freight;

	private String consignee;

	private String area;

	private String address;

	private String zipCode;

	private String phone;

	private String operator;

	private String memo;

	private Order order;

	private List<ShippingItem> shippingItems = new ArrayList<ShippingItem>();


	public enum Status{
		//等待用户确认
		waitCustomerCheck,
		//客户已确认
		customerChecked,
		//等待送货人确认
		waitSenderCheck,
		//送货人已确认
		senderChecked,
		//送货人拒绝
		senderDenied
	}

	private Status status ;

	/**
	 * 送货码
	 */
	private String deliveryCode ;


	private ChildMember childMember ;

	// 枚举是否为自己发货
	public enum DeliverType{
		Own,
		NoOwn,
	}

	private DeliverType deliverType;
	
	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(columnDefinition = "TINYINT default 0")
	public DeliverType getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(DeliverType deliverType) {
		this.deliverType = deliverType;
	}

	@Column(updatable = false)
	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@NotEmpty(groups = Delivery.class)
	@Column(updatable = false)
	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	@Column(updatable = false)
	public String getDeliveryCorpUrl() {
		return deliveryCorpUrl;
	}

	public void setDeliveryCorpUrl(String deliveryCorpUrl) {
		this.deliveryCorpUrl = deliveryCorpUrl;
	}

	@Column(updatable = false)
	public String getDeliveryCorpCode() {
		return deliveryCorpCode;
	}

	public void setDeliveryCorpCode(String deliveryCorpCode) {
		this.deliveryCorpCode = deliveryCorpCode;
	}

	@Length(max = 200)
	@Column(updatable = false)
	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(updatable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Column(updatable = false)
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@NotEmpty(groups = Delivery.class)
	@Column(updatable = false)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Column(updatable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{6}$")
	@Column(updatable = false)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
	@Column(updatable = false)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(nullable = false, updatable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Length(max = 200)
	@Column(updatable = false)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "shipping", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<ShippingItem> getShippingItems() {
		return shippingItems;
	}

	public void setShippingItems(List<ShippingItem> shippingItems) {
		this.shippingItems = shippingItems;
	}

	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getShippingItems() != null) {
			for (ShippingItem shippingItem : getShippingItems()) {
				if (shippingItem != null && shippingItem.getQuantity() != null) {
					quantity += shippingItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	@Transient
	public boolean getIsDelivery() {
		return CollectionUtils.exists(getShippingItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				ShippingItem shippingItem = (ShippingItem) object;
				return shippingItem != null && BooleanUtils.isTrue(shippingItem.getIsDelivery());
			}
		});
	}

	@Transient
	public void setShippingMethod(ShippingMethod shippingMethod) {
		setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
	}

	@Transient
	public void setDeliveryCorp(DeliveryCorp deliveryCorp) {
		setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
		setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp.getUrl() : null);
		setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
	}

	@Transient
	public void setArea(Area area) {
		setArea(area != null ? area.getFullName() : null);
	}

	@Transient
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(length = 20)
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="chargeMember" , foreignKey = @ForeignKey(name = "null"))
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}
}
