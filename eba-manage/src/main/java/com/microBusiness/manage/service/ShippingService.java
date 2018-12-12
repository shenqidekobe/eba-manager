package com.microBusiness.manage.service;

import java.util.List;
import java.util.Map;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Shipping;

public interface ShippingService extends BaseService<Shipping, Long> {

	Shipping findBySn(String sn);

	List<Map<String, String>> getTransitSteps(Shipping shipping);


	String getQrPath(Shipping shipping);


	void customerCheck(Shipping shipping);

	void senderCheck(Long id , Boolean passed , String code);

	void customerCheck(Shipping shipping , ChildMember childMember);
	
	Page<Shipping> getShippingByOrder(Order order,Pageable pageable);

}