/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import com.microBusiness.manage.entity.ass.AssForm;
import com.microBusiness.manage.service.BaseService;

public interface AssFormService extends BaseService<AssForm, Long> {
	/**
	 * 定时删除过期的formId
	 */
	void clearExpired();
}