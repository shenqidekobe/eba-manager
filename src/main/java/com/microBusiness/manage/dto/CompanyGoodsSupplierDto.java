package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.CompanyGoods;

public class CompanyGoodsSupplierDto implements Serializable {

	private static final long serialVersionUID = -1818892684984948886L;
	
	private CompanyGoods companyGoods;
	
	private String supplierName;

	public CompanyGoods getCompanyGoods() {
		return companyGoods;
	}

	public void setCompanyGoods(CompanyGoods companyGoods) {
		this.companyGoods = companyGoods;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	

}
