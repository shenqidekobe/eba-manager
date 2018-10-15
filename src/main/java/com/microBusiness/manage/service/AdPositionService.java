/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.AdPosition;
import com.microBusiness.manage.entity.AdPosition;

public interface AdPositionService extends BaseService<AdPosition, Long> {

	AdPosition find(Long id, boolean useCache);

}