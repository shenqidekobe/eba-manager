/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.Log;
import com.microBusiness.manage.entity.Log;

public interface LogDao extends BaseDao<Log, Long> {

	void removeAll();

}