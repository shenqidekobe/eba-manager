/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.DeliveryTemplateDao;
import com.microBusiness.manage.entity.DeliveryTemplate;
import com.microBusiness.manage.service.DeliveryTemplateService;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("deliveryTemplateServiceImpl")
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplate, Long> implements DeliveryTemplateService {

	@Resource(name = "deliveryTemplateDaoImpl")
	private DeliveryTemplateDao deliveryTemplateDao;

	@Transactional(readOnly = true)
	public DeliveryTemplate findDefault() {
		return deliveryTemplateDao.findDefault();
	}

	@Override
	@Transactional
	public DeliveryTemplate save(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.setDefault(deliveryTemplate);
		}
		return super.save(deliveryTemplate);
	}

	@Override
	@Transactional
	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.setDefault(deliveryTemplate);
		}
		return super.update(deliveryTemplate);
	}

}