package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.Supplier;

public interface OrderNewsPushDao extends BaseDao<OrderNewsPush, Long> {

	Page<OrderNewsPush> findPageOrder(Supplier supplier , OrderNewsPush.Status status , Pageable pageable);
	
	Page<OrderNewsPush> findPageByPurchaseOrder(Supplier supplier , Pageable pageable);
	
	Page<OrderNewsPush> findPage(Supplier supplier, OrderNewsPush.Status status, OrderNewsPush.OrderStatus orderStatus, OrderNewsPush.NoticeObject noticeObject,Pageable pageable);
	
	boolean updateByOrder(Supplier supplier);
	
	boolean updateByPurchase(Supplier supplier);
	
	public OrderNewsPush addOrderNewPush(Supplier supplier,Order order,OrderNewsPush.OrderStatus orderStatus,Need need,String send,String receive,OrderNewsPush.NoticeObject noticeObject);
	
	
	public OrderNewsPush addWithdrawPush(String send,String receive,Long linkId);
	
	
	public boolean update(Supplier supplier, OrderNewsPush.NoticeObject noticeObject);
}
