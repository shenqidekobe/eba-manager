/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryGoods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;

public interface InventoryFormService extends BaseService<InventoryForm, Long> {
	
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
	  * 新增盘点单
	  * 
	  * @param inventoryForm
	  * @param inventoryGoods
	  * @return
	  */
	 boolean save(InventoryForm inventoryForm, List<InventoryGoods> inventoryGoods);
	 
	 /**
	  * 修改盘点单,建立盘点单商品关系
	  * 
	  * @param inventoryForm
	  * @param inventoryGoods
	  * @return
	  */
	 boolean update(InventoryForm inventoryForm, List<InventoryGoods> inventoryGoods);
	 
	 /**
	  * 取消盘点单
	  * 
	  * @param inventoryForm
	  * @return
	  */
	 boolean cancel(InventoryForm inventoryForm, Member member);
	 
	 /**
	  * 盘点单是否存在
	  * 
	  * @param shop
	  * @param sfIfStatus
	  * @return
	  */
	 boolean exists(Shop shop, SfIfStatus sfIfStatus);
}