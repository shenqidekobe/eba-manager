/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.CategoryCenterImport;
import com.microBusiness.manage.entity.ProductCategoryImport;
import com.microBusiness.manage.entity.SpecificationImport;
import com.microBusiness.manage.entity.Supplier;

public interface SpecificationImportDao extends BaseDao<SpecificationImport, Long> {
    
    List<SpecificationImport> findByName(String name, Supplier supplier, ProductCategoryImport productCategoryImport);
    
    void delete(String name, ProductCategoryImport productCategoryImport);
}