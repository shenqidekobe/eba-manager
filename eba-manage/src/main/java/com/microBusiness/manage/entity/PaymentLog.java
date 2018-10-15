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

@Entity
@Table(name = "xx_payment_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_payment_log")
public class PaymentLog extends BaseEntity<Long> {

	private static final long serialVersionUID = 2337640937681340667L;

	public enum Type {

		recharge,

		payment
	}

	public enum Status {

		wait,

		success,

		failure
	}

	private String sn;

	private Type type;

	private Status status;

	private BigDecimal fee;

	private BigDecimal amount;

	private String paymentPluginId;

	private String paymentPluginName;

	private Member member;

	private Order order;

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@JoinColumn(nullable = false, updatable = false)
	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	@JoinColumn(nullable = false, updatable = false)
	public String getPaymentPluginName() {
		return paymentPluginName;
	}

	public void setPaymentPluginName(String paymentPluginName) {
		this.paymentPluginName = paymentPluginName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Transient
	public BigDecimal getEffectiveAmount() {
		BigDecimal effectiveAmount = getAmount().subtract(getFee());
		return effectiveAmount.compareTo(BigDecimal.ZERO) >= 0 ? effectiveAmount : BigDecimal.ZERO;
	}

}
