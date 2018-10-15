/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.InventoryFormLogDao;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.service.InventoryFormLogService;

import org.springframework.stereotype.Service;

@Service("inventoryFormLogServiceImpl")
public class InventoryFormLogServiceImpl extends BaseServiceImpl<InventoryFormLog, Long> implements InventoryFormLogService {

	@Resource
	private InventoryFormLogDao inventoryFormLogDao;
	
	@Override
	public List<InventoryFormLog> query(Shop shop, InventoryForm inventoryForm) {
		return inventoryFormLogDao.query(shop, inventoryForm);
	}

	
}