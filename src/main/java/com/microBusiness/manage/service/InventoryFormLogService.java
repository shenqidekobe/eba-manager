/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.Shop;

public interface InventoryFormLogService extends BaseService<InventoryFormLog, Long> {
	
	/**
	 * 根据店铺，盘点单获取操作记录
	 * 
	 * @param shop
	 * @param inventoryForm
	 * @return
	 */
	List<InventoryFormLog> query(Shop shop, InventoryForm inventoryForm);
	
}