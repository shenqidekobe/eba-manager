/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Supplier;

public interface ReceiverDao extends BaseDao<Receiver, Long> {

	Receiver findDefault(Member member);

	Page<Receiver> findPage(Member member, Pageable pageable);

	void setDefault(Receiver receiver);
	
	List<Receiver> find(Member member , boolean isDefault);
	
	Receiver find(Member member , Supplier supplier);
	
	List<Receiver> findList(Member member , Supplier supplier);
	
}