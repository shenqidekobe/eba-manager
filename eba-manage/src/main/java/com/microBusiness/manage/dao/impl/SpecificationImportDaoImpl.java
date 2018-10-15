/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.microBusiness.manage.dao.SpecificationImportDao;
import com.microBusiness.manage.entity.CategoryCenterImport;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCategoryImport;
import com.microBusiness.manage.entity.SpecificationImport;
import com.microBusiness.manage.entity.Supplier;

import org.springframework.stereotype.Repository;

@Repository("specificationImportDaoImpl")
public class SpecificationImportDaoImpl extends BaseDaoImpl<SpecificationImport, Long> implements SpecificationImportDao {

	@Override
	public List<SpecificationImport> findByName(String name, Supplier supplier, ProductCategoryImport productCategoryImport) {
		try {
			String jpql = "select specification from SpecificationImport specification where specification.supplier =:supplier and specification.deleted=0 and specification.name =:name and specification.productCategoryImport =:productCategory";
			return entityManager.createQuery(jpql, SpecificationImport.class).setParameter("supplier", supplier).setParameter("name", name).setParameter("productCategory", productCategoryImport).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void delete(String name, ProductCategoryImport productCategoryImport) {
		String sql=" delete from xx_specification_import where name =:name and product_category_import =:productCategoryImport ";
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("name", name);
		query.setParameter("productCategoryImport", productCategoryImport.getId());
		query.executeUpdate();
	}
}