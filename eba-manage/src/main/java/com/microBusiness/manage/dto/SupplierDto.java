package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.Supplier;

public class SupplierDto implements Serializable {

	private static final long serialVersionUID = 6460672791560727147L;
	
	private Supplier supplier;
	
	//所属企业ID
    private Long supplierId;
    
    //所属企业名称
    private String supplierName;
    
    private Long relationId;

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
}
