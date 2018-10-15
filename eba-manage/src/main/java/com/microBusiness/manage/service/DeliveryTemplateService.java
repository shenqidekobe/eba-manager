/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.DeliveryTemplate;
import com.microBusiness.manage.entity.DeliveryTemplate;

public interface DeliveryTemplateService extends BaseService<DeliveryTemplate, Long> {

	DeliveryTemplate findDefault();

}