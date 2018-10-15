/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.FreightConfig;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.FreightConfig;
import com.microBusiness.manage.entity.ShippingMethod;

public interface FreightConfigService extends BaseService<FreightConfig, Long> {

	boolean exists(ShippingMethod shippingMethod, Area area);

	boolean unique(ShippingMethod shippingMethod, Area previousArea, Area currentArea);

	Page<FreightConfig> findPage(ShippingMethod shippingMethod, Pageable pageable);

}