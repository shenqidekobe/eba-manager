/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import com.microBusiness.manage.entity.SpecificationItem;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.service.SpecificationValueService;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("specificationValueServiceImpl")
public class SpecificationValueServiceImpl implements SpecificationValueService {

	public boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues) {
		Assert.notEmpty(specificationItems);
		Assert.notEmpty(specificationValues);

		if (specificationValues.size() != specificationValues.size()) {
			return false;
		}
		for (int i = 0; i < specificationValues.size(); i++) {
			SpecificationItem specificationItem = specificationItems.get(i);
			SpecificationValue specificationValue = specificationValues.get(i);
			if (specificationItem == null || specificationValue == null || !specificationItem.isValid(specificationValue)) {
				return false;
			}
		}
		return true;
	}

}