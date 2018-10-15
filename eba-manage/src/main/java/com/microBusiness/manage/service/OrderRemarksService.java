package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRemarks;

public interface OrderRemarksService extends BaseService<OrderRemarks, Long> {

	/**
	 * 管理端添加备注信息
	 * @param order
	 * @param admin 
	 * @param orderRemarks 备注实体类
	 * @param supplierName 企业名称
	 */
	void saveOrderRemarks(Order order,Admin admin,OrderRemarks orderRemarks,String supplierName);
	
	/**
	 * 微信端添加备注
	 * @param orderRemarks 备注实体类
	 */
	void saveOrderRemarks(OrderRemarks orderRemarks);
	
	/**
	 * 采购单添加备注
	 * @param order
	 * @param admin
	 * @param orderRemarks
	 * @param supplierName
	 */
	void savePurchaseOrderRemarks(Order order,Admin admin,OrderRemarks orderRemarks,String supplierName);
	
	Page<OrderRemarks> getRemarksByOrder(Order order,Pageable pageable);
}
