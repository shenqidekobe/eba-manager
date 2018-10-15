/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.StorageFormLogDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageFormLog;

import org.springframework.stereotype.Repository;

@Repository("storageFormLogDaoImpl")
public class StorageFormLogDaoImpl extends BaseDaoImpl<StorageFormLog, Long> implements  StorageFormLogDao {

	@Override
	public List<StorageFormLog> query(Shop shop, StorageForm storageForm) {
		try {
			String jpql = "select storageFormLog from StorageFormLog storageFormLog where storageFormLog.shop =:shop and storageFormLog.storageForm =:storageForm order by storageFormLog.createDate DESC";
			return entityManager.createQuery(jpql, StorageFormLog.class).setParameter("shop", shop).setParameter("storageForm", storageForm).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<StorageFormLog> query(Shop shop, Order order, StorageFormLog.Record record) {
		try {
			String jpql = "select storageFormLog from StorageFormLog storageFormLog where storageFormLog.shop =:shop and storageFormLog.order=:order and storageFormLog.record=:record order by storageFormLog.createDate DESC";
			return entityManager.createQuery(jpql, StorageFormLog.class).setParameter("shop", shop).setParameter("order", order).setParameter("record", record).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}