package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.InventoryGoods;

public interface InventoryGoodsDao extends BaseDao<InventoryGoods, Long> {

	/**
	 * 根据盘点单删除关联
	 * 
	 * @param inventoryForm
	 */
	void delete(Long inventoryForm);

}