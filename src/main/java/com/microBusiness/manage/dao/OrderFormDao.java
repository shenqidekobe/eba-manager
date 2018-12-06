package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.OrderForm;

public interface OrderFormDao extends BaseDao<OrderForm, Long> {
	
	int clearExpired();
	
	OrderForm getByFormId(String formId);
	
	OrderForm getDoOrderForm(ChildMember childMember);
}
