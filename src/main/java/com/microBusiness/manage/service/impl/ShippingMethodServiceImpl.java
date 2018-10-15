/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.FreightConfig;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("shippingMethodServiceImpl")
public class ShippingMethodServiceImpl extends BaseServiceImpl<ShippingMethod, Long> implements ShippingMethodService {

	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(ShippingMethod shippingMethod, Area area, Integer weight) {
		Assert.notNull(shippingMethod);

		Setting setting = SystemUtils.getSetting();
		BigDecimal firstPrice = shippingMethod.getDefaultFirstPrice();
		BigDecimal continuePrice = shippingMethod.getDefaultContinuePrice();
		if (area != null && CollectionUtils.isNotEmpty(shippingMethod.getFreightConfigs())) {
			List<Area> areas = new ArrayList<Area>();
			areas.addAll(area.getParents());
			areas.add(area);
			for (int i = areas.size() - 1; i >= 0; i--) {
				FreightConfig freightConfig = shippingMethod.getFreightConfig(areas.get(i));
				if (freightConfig != null) {
					firstPrice = freightConfig.getFirstPrice();
					continuePrice = freightConfig.getContinuePrice();
					break;
				}
			}
		}
		if (weight == null || weight <= shippingMethod.getFirstWeight() || continuePrice.compareTo(BigDecimal.ZERO) == 0) {
			return setting.setScale(firstPrice);
		} else {
			double contiuneWeightCount = Math.ceil((weight - shippingMethod.getFirstWeight()) / (double) shippingMethod.getContinueWeight());
			return setting.setScale(firstPrice.add(continuePrice.multiply(new BigDecimal(String.valueOf(contiuneWeightCount)))));
		}
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(ShippingMethod shippingMethod, Receiver receiver, Integer weight) {
		return calculateFreight(shippingMethod, receiver != null ? receiver.getArea() : null, weight);
	}

}