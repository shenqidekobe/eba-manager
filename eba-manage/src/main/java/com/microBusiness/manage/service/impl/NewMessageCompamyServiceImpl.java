/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NewMessageCompamyDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessageCompamy;
import com.microBusiness.manage.service.NewMessageCompamyService;

import org.springframework.stereotype.Service;

@Service("newMessageCompamyServiceImpl")
public class NewMessageCompamyServiceImpl extends BaseServiceImpl<NewMessageCompamy, Long> implements NewMessageCompamyService {

	@Resource(name = "newMessageCompamyDaoImpl")
	private NewMessageCompamyDao newMessageCompamyDao;
	
	@Override
	public Page<NewMessageCompamy> query(Admin admin, Boolean readBoolean, Pageable pageable) {
		return newMessageCompamyDao.query(admin, readBoolean, pageable);
	}

	@Override
	public NewMessageCompamy read(Long id, Admin admin, HttpServletRequest request) {
		NewMessageCompamy newMessageCompamy = this.find(id);
		newMessageCompamy.setReceiverIp(request.getRemoteAddr());
		newMessageCompamy.setReceiver(admin);
		newMessageCompamy.setReceiverRead(true);
		newMessageCompamyDao.merge(newMessageCompamy);
		return newMessageCompamy;
	}

}