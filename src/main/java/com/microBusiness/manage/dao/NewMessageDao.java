/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.NewMessage;

public interface NewMessageDao extends BaseDao<NewMessage, Long> {

	public Page<NewMessage> query(NewMessage newMessage, Pageable pageable);
}