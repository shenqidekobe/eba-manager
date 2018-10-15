package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.dao.OrderRemarksDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.LogType;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.OrderRemarks;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.service.OrderRemarksService;

@Service("orderRemarksServiceImpl")
public class OrderRemarksServiceImpl extends BaseServiceImpl<OrderRemarks, Long> implements OrderRemarksService {

	@Resource(name = "orderRemarksDaoImpl")
	private OrderRemarksDao orderRemarksDao;
	@Resource
	private OrderNewsPushDao orderNewsPushDao;

	@Resource
	private NeedDao needDao;

	/**
	 * 添加备注信息
	 */
	@Override
	public void saveOrderRemarks(Order order, Admin admin, OrderRemarks orderRemarks,String supplierName) {
		orderRemarks.setName(admin.getUsername());
		orderRemarks.setOrder(order);
		orderRemarks.setSuppliper(supplierName);
		orderRemarks.setSource(OrderRemarks.Source.admin);
		if (order.getToSupplier()==null) {
			orderRemarks.setLogType(LogType.distributor);
			orderRemarks.setMsgType(MsgType.btoc);
		}else if (order.getToSupplier().getId().equals(admin.getSupplier().getId())) {
			orderRemarks.setLogType(LogType.distributor);
			orderRemarks.setMsgType(MsgType.btoc);
		}else if (order.getSupplier().getId().equals(admin.getSupplier().getId())) {
			orderRemarks.setLogType(LogType.supplier);
			orderRemarks.setMsgType(MsgType.btob);
		}
		orderRemarksDao.persist(orderRemarks);
		
		if(order.getType().equals(Order.Type.billDistribution)) {
			if(orderRemarks.getMsgType() == MsgType.btoc) {
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
			}else {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.purchase);
			}
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
		}

	}

	
	@Override
	public void saveOrderRemarks(OrderRemarks orderRemarks) {
		orderRemarksDao.persist(orderRemarks);
		
		OrderNewsPush orderNewsPush = new OrderNewsPush();
		Order order = orderRemarks.getOrder();

		//Need need = order.getMember().getNeed();
		Need need =order.getNeed();

//		orderNewsPush.setBuyers(need.getSupplier());
//		orderNewsPush.setSupplier(order.getSupplier());
//		orderNewsPush.setOrder(order);
//		orderNewsPush.setNeed(need);
//		orderNewsPush.setOrderStatus(OrderNewsPush.OrderStatus.leaveAMessage);
//		orderNewsPush.setStatus(OrderNewsPush.Status.unread);
//		orderNewsPush.setPurchaseViewStatus(OrderNewsPush.PurchaseViewStatus.unread);
//		orderNewsPush.setMark(OrderNewsPush.Mark.all);
//		orderNewsPush.setOperator(OrderNewsPush.Operator.microLetter);
//		orderNewsPushDao.persist(orderNewsPush);
		if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(orderRemarks.getOrder().getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, orderRemarks.getOrder().getSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, orderRemarks.getOrder().getSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(orderRemarks.getOrder().getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, need, orderRemarks.getOrder().getSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.order);
		}


	}


	/**
	 * 采购单添加备注
	 */
	@Override
	public void savePurchaseOrderRemarks(Order order, Admin admin,
			OrderRemarks orderRemarks, String supplierName) {
		orderRemarks.setName(admin.getUsername());
		orderRemarks.setOrder(order);
		orderRemarks.setSuppliper(supplierName);
		orderRemarks.setSource(OrderRemarks.Source.admin);
		orderRemarks.setLogType(LogType.distributor);
		orderRemarks.setMsgType(MsgType.btob);
		orderRemarksDao.persist(orderRemarks);
		
		if(order.getType() == Order.Type.billDistribution) {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.purchase);
		}else if(order.getType() == Order.Type.formal) {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
			orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getToSupplier().getName(), "", OrderNewsPush.NoticeObject.purchase);
		}else {
			orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.leaveAMessage, order.getNeed(), order.getSupplier().getName(), "", OrderNewsPush.NoticeObject.order);
		}



		
	}


	@Override
	public Page<OrderRemarks> getRemarksByOrder(Order order,Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRemarksDao.getRemarksByOrder(order,pageable);
	}
}
