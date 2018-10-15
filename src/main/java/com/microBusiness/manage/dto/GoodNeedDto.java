package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.util.Date;

import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Need;

public class GoodNeedDto implements Serializable {

	private static final long serialVersionUID = -8090634378052069675L;

	public Goods goods;
	
	public String fullName;
	
	public String address;
	
	public Integer quantity;
	
	private Date createDate;
	
	private String needName;
	
	private String phone;
	

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
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

	public String getNeedName() {
		return needName;
	}

	public void setNeedName(String needName) {
		this.needName = needName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
