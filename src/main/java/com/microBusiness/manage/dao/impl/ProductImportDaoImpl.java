/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.ProductImportDao;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductImport;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("productImportDaoImpl")
public class ProductImportDaoImpl extends BaseDaoImpl<ProductImport, Long> implements ProductImportDao {

	public boolean snExists(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String jpql = "select count(*) from ProductImport product where lower(product.sn) = lower(:sn)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
		return count > 0;
	}

	public Product findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}

		String jpql = "select product from ProductImport product where lower(product.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Product.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	
}