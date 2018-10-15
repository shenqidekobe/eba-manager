package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.Supplier;

public interface OrderNewsPushService extends BaseService<OrderNewsPush, Long> {
	
	Page<OrderNewsPush> findPageOrder(Supplier supplier , OrderNewsPush.Status status , Pageable pageable);
	
	Page<OrderNewsPush> findPageByPurchaseOrder(Supplier supplier ,  Pageable pageable);
	
	Page<OrderNewsPush> findPage(Supplier supplier, OrderNewsPush.Status status, OrderNewsPush.OrderStatus orderStatus, OrderNewsPush.NoticeObject noticeObject,Pageable pageable);
	
	boolean updateByOrder(Supplier supplier);
	
	boolean updateByPurchase(Supplier supplier);
	
	//标记为已读
	boolean update(Supplier supplier, OrderNewsPush.NoticeObject noticeObject);

}
