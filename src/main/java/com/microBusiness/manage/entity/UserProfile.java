/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_profile")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_user_profile")
public class UserProfile extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492820069726773460L;

	private ChildMember childMember;
	
	//提现账号
	private String cashAccount;
	//提现金额
	private String cashMoney;
	
	private String payAccount;
	
	//提现时间
	private Date payTime;
	
	private Type type;
	
	enum Type{
		SYS,
		SELF
	}
	
	//到账时
	private Date arriveTime;
	
	private Status status;
	
	enum Status{
		提现中,
		已到账,
		提现失败
	}
	
	//失败原因
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}

	public String getCashAccount() {
		return cashAccount;
	}

	public void setCashAccount(String cashAccount) {
		this.cashAccount = cashAccount;
	}

	public String getCashMoney() {
		return cashMoney;
	}

	public void setCashMoney(String cashMoney) {
		this.cashMoney = cashMoney;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
	

}
