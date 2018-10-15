package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;

public interface StorageFormDao extends BaseDao<StorageForm, Long> {

	/**
	 * 入库单是否存在
	 * 
	 * @param shop
	 * @param sfIfStatus
	 * @return
	 */
	boolean exists(Shop shop, SfIfStatus sfIfStatus);
	
	/**
	 * 分页查询入库单列表	
	 * 
	 * @param pageable
	 * @param inventoryCode
	 * 			盘点单号
	 * @param shop
	 * 			店铺
	 * @return
	 */
	 Page<StorageForm> findPage(Pageable pageable, String Keyword, Shop shop) ;
	 
	 /**
	  * 获取每天盘点单数
	  * 
	  * @return
	  */
	 Long getCount(Member member);
	 
	 /**
	  * 根据入库单号查询入库单
	  * 
	  * @param inventoryCode
	  * @return
	  */
	 StorageForm findByStorageCode(String storageCode, Member member);
}