package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.CategoryCenterImport;
import com.microBusiness.manage.entity.SpecificationCenterImport;

public interface SpecificationCenterImportDao extends BaseDao<SpecificationCenterImport , Long> {
	
	List<SpecificationCenterImport> findByName(String name, CategoryCenterImport categoryCenterImport);
	
	void delete(String name, CategoryCenterImport categoryCenterImport);
}
