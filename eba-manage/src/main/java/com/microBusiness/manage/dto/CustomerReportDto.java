package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerReportDto implements Serializable{
	private static final long serialVersionUID = -8090634378052069675L;
	//客户id
	private String needId;
	//企业id
	private String supplierId;
	//客户名称
	private String name;
	//个体客户名称
	private String needName;
	//企业名称
	private String supplierName;
	//订货单数
	private Integer orderNumber;
	//订货商品SKU数
	private Integer goodsNumber;
	//订货商品数
	private Integer orderQuantity;
	//订货单金额
	private BigDecimal amount;
	
	public String getNeedId() {
		return needId;
	}
	public void setNeedId(String needId) {
		this.needId = needId;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNeedName() {
		return needName;
	}
	public void setNeedName(String needName) {
		this.needName = needName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
