/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.service.AttributeService;
import com.microBusiness.manage.util.FreeMarkerUtils;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.util.FreeMarkerUtils;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("attributeListDirective")
public class AttributeListDirective extends BaseDirective {

	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	private static final String VARIABLE_NAME = "attributes";

	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Attribute.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<Attribute> attributes = attributeService.findList(productCategoryId, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, attributes, env, body);
	}

}