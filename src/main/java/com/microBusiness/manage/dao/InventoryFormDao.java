package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;

public interface InventoryFormDao extends BaseDao<InventoryForm, Long> {

	/**
	 * 分页查询盘点单列表	
	 * 
	 * @param pageable
	 * @param inventoryCode
	 * 			盘点单号
	 * @param shop
	 * 			店铺
	 * @return
	 */
	 Page<InventoryForm> findPage(Pageable pageable, String inventoryCode, Shop shop, SfIfStatus status) ;
	 
	 /**
	  * 获取每天盘点单数
	  * 
	  * @return
	  */
	 Long getCount(Member member);
	 
	 /**
	  * 盘点单是否存在
	  * 
	  * @param shop
	  * @param sfIfStatus
	  * @return
	  */
	 boolean exists(Shop shop, SfIfStatus sfIfStatus);
	 
	 /**
	  * 根据盘点单号查询盘点单
	  * 
	  * @param inventoryCode
	  * @return
	  */
	 InventoryForm findByInventoryCode(String inventoryCode, Member member);
}