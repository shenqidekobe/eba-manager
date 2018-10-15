package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplyDistributionService extends BaseService<SupplierAssignRelation, Long> {

	boolean supplyDistribution(SupplierSupplier supplierSupplier, Long[] needId, List<SupplierNeedProduct> supplierNeedProductList);

	boolean supplyDistribution(SupplierSupplier supplierSupplier, Need need,
			SupplierAssignRelation supplierAssignRelation, List<SupplierNeedProduct> supplierNeedProductList);
}
