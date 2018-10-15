/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.StorageFormLogDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageFormLog;
import com.microBusiness.manage.service.StorageFormLogService;

import org.springframework.stereotype.Service;

@Service("storageFormLogServiceImpl")
public class StorageFormLogServiceImpl extends BaseServiceImpl<StorageFormLog, Long> implements StorageFormLogService {

	@Resource
	private StorageFormLogDao storageFormLogDao;
	
	@Override
	public List<StorageFormLog> query(Shop shop, StorageForm storageForm) {
		return storageFormLogDao.query(shop, storageForm);
	}

	@Override
	public List<StorageFormLog> query(Shop shop, Order order, StorageFormLog.Record record) {
		return storageFormLogDao.query(shop, order, record);
	}

}