/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssForm;

public interface AssFormDao extends BaseDao<AssForm, Long> {
	int clearExpired();
}