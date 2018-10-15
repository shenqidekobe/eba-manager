package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderStatisticsDto implements Serializable {

	private static final long serialVersionUID = -5950119764782251218L;

	// 订单总数
	private Integer orderTotal;
	// 订单总金额
	private BigDecimal totalAmount;
	//商品总数
	private Integer goodTotal;
	//供应商总数
	private Integer supplierTotal;
	//订单创建日期
	private String createDate;
	
	public Integer getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getGoodTotal() {
		return goodTotal;
	}

	public void setGoodTotal(Integer goodTotal) {
		this.goodTotal = goodTotal;
	}

	public Integer getSupplierTotal() {
		return supplierTotal;
	}

	public void setSupplierTotal(Integer supplierTotal) {
		this.supplierTotal = supplierTotal;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
