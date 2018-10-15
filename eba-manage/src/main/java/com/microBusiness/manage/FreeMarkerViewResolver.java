/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage;

import com.microBusiness.manage.util.FreeMarkerUtils;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class FreeMarkerViewResolver extends AbstractTemplateViewResolver {

	public FreeMarkerViewResolver() {
		setViewClass(requiredViewClass());
	}

	@Override
	protected Class<FreeMarkerView> requiredViewClass() {
		return FreeMarkerView.class;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		return super.buildView(FreeMarkerUtils.process(viewName));
	}

}