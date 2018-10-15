package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderRelation;

public interface OrderRelationDao extends BaseDao<OrderRelation, Long> {

	OrderRelation findByChildMember(ChildMember childMember , Order order);
	
	Page<OrderRelation> findPage(Pageable pageable , Order order , Long orderRelationId);
}
