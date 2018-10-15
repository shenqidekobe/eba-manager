/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import com.microBusiness.manage.dao.ProductCategoryImportDao;
import com.microBusiness.manage.entity.ProductCategoryImport;
import com.microBusiness.manage.entity.Supplier;

import org.springframework.stereotype.Repository;

@Repository("productCategoryImportDaoImpl")
public class ProductCategoryImportDaoImpl extends BaseDaoImpl<ProductCategoryImport, Long> implements ProductCategoryImportDao {

	@Override
	public List<ProductCategoryImport> findByParent(Supplier supplier, ProductCategoryImport parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select productCategory from ProductCategoryImport productCategory where productCategory.supplier =:supplier and productCategory.deleted=0 and productCategory.parent =:parent and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategoryImport.class).setParameter("parent", parent).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			} else {
				String jpql = "select productCategory from ProductCategoryImport productCategory where productCategory.supplier =:supplier and productCategory.deleted=0 and productCategory.parent is null and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategoryImport.class).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}
	
}