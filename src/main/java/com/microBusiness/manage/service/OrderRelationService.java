package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRelation;

public interface OrderRelationService extends BaseService<OrderRelation, Long> {

	Page<OrderRelation> findPage(Pageable pageable , Order order , Long orderRelationId);
}
