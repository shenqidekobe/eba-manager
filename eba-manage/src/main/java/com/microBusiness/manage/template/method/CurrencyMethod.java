/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.template.method;

import java.math.BigDecimal;
import java.util.List;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.SystemUtils;
import org.springframework.stereotype.Component;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

@Component("currencyMethod")
public class CurrencyMethod implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		BigDecimal amount = FreeMarkerUtils.getArgument(0, BigDecimal.class, arguments);
		Boolean showSign = FreeMarkerUtils.getArgument(1, Boolean.class, arguments);
		Boolean showUnit = FreeMarkerUtils.getArgument(2, Boolean.class, arguments);
		if (amount != null) {
			Setting setting = SystemUtils.getSetting();
			String price = setting.setScale(amount).toString();
			if (showSign != null && showSign) {
				price = setting.getCurrencySign() + price;
			}
			if (showUnit != null && showUnit) {
				price += setting.getCurrencyUnit();
			}
			return new SimpleScalar(price);
		}
		return null;
	}

}