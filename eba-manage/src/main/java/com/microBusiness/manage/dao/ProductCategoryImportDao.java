/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.ProductCategoryImport;
import com.microBusiness.manage.entity.Supplier;

public interface ProductCategoryImportDao extends BaseDao<ProductCategoryImport, Long> {

	List<ProductCategoryImport> findByParent(Supplier supplier, ProductCategoryImport parent, String name);
}