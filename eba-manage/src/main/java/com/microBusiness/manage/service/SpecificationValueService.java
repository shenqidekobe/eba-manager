/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.SpecificationItem;
import com.microBusiness.manage.entity.SpecificationValue;

public interface SpecificationValueService {

	boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues);

}