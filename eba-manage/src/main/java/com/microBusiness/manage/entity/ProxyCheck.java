/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_proxy_check")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_proxy_check")
public class ProxyCheck extends OrderEntity<Long> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1874025584791580388L;

	/**
	 * 上级代理
	 */
	private ProxyUser parentProxyUser;
	
	/**
	 * 微信用户
	 */
	private ChildMember childMember;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 身份证号
	 */
	private String idenNo;
	
	/**
	 * 联系方式
	 */
	private String tel;
	
	/**
	 * 联系地址
	 */
	private String address;
	
	/**
	 * 性别
	 */
	private Gender gender;
	
	
	/**
	 * 微信号
	 */
	private String webchat;
	
	/**
	 * 省市区
	 */
	private Area area;
	
	private Level level;
	
	public enum Level {

		一级代理("一级代理"),
		二级代理("二级代理"),
		三级代理("三级代理");
		
		private String name;
		
		Level(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	/**
	 * 从事微商时间
	 */
	private String workTime;
	
	/**
	 * 月销售额
	 */
	private String monthMoney;
	
	/**
	 * 目前经营的品牌品类
	 */
	private String nowManageCategory;
	
	/**
	 * 申请理由
	 */
	private String reason;
	
	
	/**
	 * 了解途径
	 */
	private SourceType sourceType;
	
	

	public enum SourceType {

		微信朋友圈,
		百度搜索,
		朋友推荐,
		线下活动,
		贴吧,
		论坛,
		微博,
		电视广告,
		官方网站,
		其他;
	}
	
	private ProxyJoinType proxyJoinType;
	/**
	 * 审核状态
	 */
	private ProxyCheckStatus proxyCheckStatus;
	
	private  Date completeDate;
	
	private Supplier supplier;
	

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}
	
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}



	public Date getCompleteDate() {
		return completeDate;
	}



	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}



	public ProxyCheckStatus getProxyCheckStatus() {
		return proxyCheckStatus;
	}



	public void setProxyCheckStatus(ProxyCheckStatus proxyCheckStatus) {
		this.proxyCheckStatus = proxyCheckStatus;
	}



	public ProxyJoinType getProxyJoinType() {
		return proxyJoinType;
	}



	public void setProxyJoinType(ProxyJoinType proxyJoinType) {
		this.proxyJoinType = proxyJoinType;
	}



	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public ProxyUser getParentProxyUser() {
		return parentProxyUser;
	}



	public void setParentProxyUser(ProxyUser parentProxyUser) {
		this.parentProxyUser = parentProxyUser;
	}


	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public ChildMember getChildMember() {
		return childMember;
	}



	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getIdenNo() {
		return idenNo;
	}



	public void setIdenNo(String idenNo) {
		this.idenNo = idenNo;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public Gender getGender() {
		return gender;
	}



	public void setGender(Gender gender) {
		this.gender = gender;
	}



	public String getWebchat() {
		return webchat;
	}



	public void setWebchat(String webchat) {
		this.webchat = webchat;
	}


	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, updatable = false)
	public Area getArea() {
		return area;
	}



	public void setArea(Area area) {
		this.area = area;
	}



	public Level getLevel() {
		return level;
	}



	public void setLevel(Level level) {
		this.level = level;
	}



	public String getWorkTime() {
		return workTime;
	}



	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}



	public String getMonthMoney() {
		return monthMoney;
	}



	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}



	public String getNowManageCategory() {
		return nowManageCategory;
	}



	public void setNowManageCategory(String nowManageCategory) {
		this.nowManageCategory = nowManageCategory;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public SourceType getSourceType() {
		return sourceType;
	}



	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}



}
