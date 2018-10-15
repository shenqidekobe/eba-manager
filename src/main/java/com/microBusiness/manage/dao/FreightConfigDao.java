/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.FreightConfig;
import com.microBusiness.manage.entity.ShippingMethod;

public interface FreightConfigDao extends BaseDao<FreightConfig, Long> {

	boolean exists(ShippingMethod shippingMethod, Area area);

	Page<FreightConfig> findPage(ShippingMethod shippingMethod, Pageable pageable);

}