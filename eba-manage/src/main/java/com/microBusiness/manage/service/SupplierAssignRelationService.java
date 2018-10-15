package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierAssignRelationService extends BaseService<SupplierAssignRelation, Long>{

	List<SupplierAssignRelation> findListBySupplier(SupplierSupplier supplierSupplier,Need need);

	boolean save(SupplierAssignRelation supplierAssignRelation, List<SupplierNeedProduct> supplierNeedProductList);

	boolean deleteNeed(SupplierAssignRelation supplierAssignRelation);

}
