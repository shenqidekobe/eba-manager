/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.Log;

public interface LogService extends BaseService<Log, Long> {

	void clear();
	
	
	void createLog(String operation, String operator, String content, String parameter, String ip);

}