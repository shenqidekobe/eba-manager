package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.Shop;

public interface InventoryFormLogDao extends BaseDao<InventoryFormLog, Long> {

	/**
	 * 根据店铺，盘点单获取操作记录
	 * 
	 * @param shop
	 * @param inventoryForm
	 * @return
	 */
	List<InventoryFormLog> query(Shop shop, InventoryForm inventoryForm);
}