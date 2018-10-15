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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "t_payment_customer")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_payment_customer")
public class PayCustomer extends BaseEntity<Long> {

	private static final long serialVersionUID = -7744615698397358953L;

	public enum Method {

		online,

		offline,

		deposit
	}

	private String sn;

	private Method method;

	private String paymentMethod;

	private String bank;

	private String account;

	/**
	 * 金额
	 */
	private BigDecimal amount;
	
	private String partnerTradeNo;

	private String operator;

	/**
	 * 企业付款描述信息
	 */
	private String memo;

	private ChildMember childMember;
	
	private String ip;
	
	/**
	 * NO_CHECK：不校验真实姓名 
		FORCE_CHECK：强校验真实姓名
	 */
	private String checkName;
	
	/**
	 * 收款用户姓名
	 */
	private String reUserName;
	
	
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getReUserName() {
		return reUserName;
	}

	public void setReUserName(String reUserName) {
		this.reUserName = reUserName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}
	
	
	
	

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@NotNull
	@Column(nullable = false, updatable = false)
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Column(updatable = false)
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Length(max = 200)
	@Column(updatable = false)
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Length(max = 200)
	@Column(updatable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}


	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	@Column(updatable = false)
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


	@Transient
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
	}

	@Transient
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}

}
