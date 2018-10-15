package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.StorageGoods;

public interface StorageGoodsDao extends BaseDao<StorageGoods, Long> {

	/**
	 * 根据盘点单删除关联
	 * 
	 * @param inventoryForm
	 */
	void delete(Long storageForm);

}