package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.OrderForm;

public interface OrderFormService extends BaseService<OrderForm, Long> {

	/**
	 * 定时删除过期的formId
	 */
	void clearExpired();
}
