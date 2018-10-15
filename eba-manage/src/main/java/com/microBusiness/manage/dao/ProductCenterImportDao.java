package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.ProductCenterImport;

public interface ProductCenterImportDao extends BaseDao<ProductCenterImport, Long> {
    boolean snExists(String sn);
}
