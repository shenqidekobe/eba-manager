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

import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.util.FreeMarkerUtils;

import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.ProductCategoryService;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productCategoryParentListDirective")
public class ProductCategoryParentListDirective extends BaseDirective {

	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	private static final String VARIABLE_NAME = "productCategories";

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Boolean recursive = FreeMarkerUtils.getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, params);
		Integer count = getCount(params);
		boolean useCache = useCache(env, params);
		List<ProductCategory> productCategories = productCategoryService.findParents(productCategoryId, recursive != null ? recursive : true, count, useCache);
		setLocalVariable(VARIABLE_NAME, productCategories, env, body);
	}

}