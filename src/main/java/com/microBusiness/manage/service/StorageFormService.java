package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageGoods;

public interface StorageFormService extends BaseService<StorageForm, Long> {
	
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
	 Page<StorageForm> findPage(Pageable pageable, String keyword, Shop shop) ;
	 
	 /**
	  * 新增入库单
	  * 
	  * @param inventoryForm
	  * @param inventoryGoods
	  * @return
	  */
	 boolean save(StorageForm StorageForm, List<StorageGoods> storageGoods);
	 
	 /**
	  * 修改入库单,建立入库单商品关系
	  * 
	  * @param inventoryForm
	  * @param inventoryGoods
	  * @return
	  */
	 boolean update(StorageForm storageForm, List<StorageGoods> storageGoods);
	 
	 /**
	  * 取消盘点单
	  * 
	  * @param inventoryForm
	  * @return
	  */
	 boolean cancel(StorageForm storageForm, Member member);
}