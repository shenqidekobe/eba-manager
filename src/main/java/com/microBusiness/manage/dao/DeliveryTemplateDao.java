/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.DeliveryTemplate;
import com.microBusiness.manage.entity.DeliveryTemplate;

public interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate, Long> {

	DeliveryTemplate findDefault();

	void setDefault(DeliveryTemplate deliveryTemplate);

}