/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Supplier;

public interface ReceiverService extends BaseService<Receiver, Long> {

	Receiver findDefault(Member member);

	Page<Receiver> findPage(Member member, Pageable pageable);
	
	List<Receiver> find(Member member , boolean isDefault);
	
	Receiver find(Member member , Supplier supplier);
	
	List<Receiver> findList(Member member , Supplier supplier);
	
	Receiver findDefault(Member member , Supplier supplier);

}