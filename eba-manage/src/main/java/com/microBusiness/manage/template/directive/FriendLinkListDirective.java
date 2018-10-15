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
import com.microBusiness.manage.entity.FriendLink;
import com.microBusiness.manage.service.FriendLinkService;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.FriendLink;
import com.microBusiness.manage.service.FriendLinkService;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("friendLinkListDirective")
public class FriendLinkListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "friendLinks";

	@Resource(name = "friendLinkServiceImpl")
	private FriendLinkService friendLinkService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, FriendLink.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<FriendLink> friendLinks = friendLinkService.findList(count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, friendLinks, env, body);
	}

}