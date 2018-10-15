package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageFormLog;

public interface StorageFormLogDao extends BaseDao<StorageFormLog, Long> {

	/**
	 * 根据店铺，入库单获取操作记录
	 * 
	 * @param shop
	 * @param inventoryForm
	 * @return
	 */
	List<StorageFormLog> query(Shop shop, StorageForm storageForm);

	/**
	 * 根据订单查询入库操作记录
	 * 
	 * @param shop
	 * @param storageForm
	 * @param order
	 * @return
	 */
	List<StorageFormLog> query(Shop shop, Order order, StorageFormLog.Record record);
}