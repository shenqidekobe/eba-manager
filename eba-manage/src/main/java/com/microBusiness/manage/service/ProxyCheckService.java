/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ProxyCheck;

public interface ProxyCheckService extends BaseService<ProxyCheck, Long> {
	
	
	ProxyCheck check(ProxyCheck proxyCheck);
	
	
	Page<ProxyCheck> findPage(Pageable pageable, String dateType, Date startDate, Date endDate);

}