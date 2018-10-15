/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessageCompamy;

public interface NewMessageCompamyService extends BaseService<NewMessageCompamy, Long> {

	public Page<NewMessageCompamy> query(Admin admin, Boolean readBoolean, Pageable pageable);

	public NewMessageCompamy read(Long id, Admin admin, HttpServletRequest request);
}