package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;

public class ProductSupplierDto implements Serializable {

	private static final long serialVersionUID = -3459095887578892222L;
	
	public Product product;
	
	public Supplier supplier;
	//商品名称
	private String goodsName;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

}
