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
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.util.FreeMarkerUtils;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.service.ArticleService;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("articleListDirective")
public class ArticleListDirective extends BaseDirective {

	private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

	private static final String TAG_ID_PARAMETER_NAME = "tagId";

	private static final String VARIABLE_NAME = "articles";

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long tagId = FreeMarkerUtils.getParameter(TAG_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Article.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<Article> articles = articleService.findList(articleCategoryId, tagId, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, articles, env, body);
	}

}