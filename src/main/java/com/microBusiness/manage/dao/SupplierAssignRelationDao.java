package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierAssignRelationDao extends BaseDao<SupplierAssignRelation, Long>{

	List<SupplierAssignRelation> findListBySupplier(SupplierSupplier supplierSupplier,Need need);

}
