package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.util.Date;

import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Supplier;

public class GoodSupplierDto implements Serializable {

	private static final long serialVersionUID = -8090634378052069675L;

	public Goods goods;
	
	public Supplier supplier;
	
	public Integer quantity;
	
	private Date createDate;
	
	private String supplierName;

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}
