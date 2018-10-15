package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.OrderNewsPush.NoticeObject;
import com.microBusiness.manage.entity.OrderNewsPush.OrderStatus;
import com.microBusiness.manage.entity.OrderNewsPush.Status;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderNewsPushService;

@Service("orderNewsPushServiceImpl")
public class OrderNewsPushServiceImpl extends BaseServiceImpl<OrderNewsPush, Long> implements OrderNewsPushService {

	@Resource
	private OrderNewsPushDao orderNewsPushDao;
	
	@Override
	public Page<OrderNewsPush> findPageOrder(Supplier supplier , OrderNewsPush.Status status,
			Pageable pageable) {
		return orderNewsPushDao.findPageOrder(supplier , status , pageable);
	}

	@Override
	public Page<OrderNewsPush> findPageByPurchaseOrder(Supplier supplier,Pageable pageable) {
		return orderNewsPushDao.findPageByPurchaseOrder(supplier, pageable);
	}

	@Override
	public boolean updateByOrder(Supplier supplier) {
		return orderNewsPushDao.updateByOrder(supplier);
	}

	@Override
	public boolean updateByPurchase(Supplier supplier) {
		return orderNewsPushDao.updateByPurchase(supplier);
	}

	@Override
	public Page<OrderNewsPush> findPage(Supplier supplier, Status status,
			OrderStatus orderStatus, NoticeObject noticeObject,
			Pageable pageable) {
		return orderNewsPushDao.findPage(supplier, status, orderStatus, noticeObject, pageable);
	}

	@Override
	public boolean update(Supplier supplier, OrderNewsPush.NoticeObject noticeObject) {
		return orderNewsPushDao.update(supplier, noticeObject);
	}

}
