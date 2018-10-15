package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRemarks;

public interface OrderRemarksDao extends BaseDao<OrderRemarks, Long> {

	Page<OrderRemarks> getRemarksByOrder(Order order,Pageable pageable);

}
