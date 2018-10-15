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
import com.microBusiness.manage.entity.Consultation;
import com.microBusiness.manage.service.ConsultationService;
import com.microBusiness.manage.util.FreeMarkerUtils;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Consultation;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("consultationListDirective")
public class ConsultationListDirective extends BaseDirective {

	private static final String MEMBER_ID_PARAMETER_NAME = "memberId";

	private static final String GOODS_ID_PARAMETER_NAME = "goodsId";

	private static final String VARIABLE_NAME = "consultations";

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long memberId = FreeMarkerUtils.getParameter(MEMBER_ID_PARAMETER_NAME, Long.class, params);
		Long goodsId = FreeMarkerUtils.getParameter(GOODS_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Consultation.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<Consultation> consultations = consultationService.findList(memberId, goodsId, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, consultations, env, body);
	}

}