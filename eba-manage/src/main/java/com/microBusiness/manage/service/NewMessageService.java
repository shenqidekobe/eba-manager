/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessage;

public interface NewMessageService extends BaseService<NewMessage, Long> {

	public Boolean send(Admin admin, NewMessage newMessage, List<Admin> adminList, HttpServletRequest request);

	public Page<NewMessage> query(NewMessage newMessage, Pageable pageable);
}