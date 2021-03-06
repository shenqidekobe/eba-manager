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

import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.service.AdService;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("adDirective")
public class AdDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "adList";

	@Resource(name = "adServiceImpl")
	private AdService adService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long id = getId(params);
		List<Ad> adList = adService.query(id);
		setLocalVariable(VARIABLE_NAME, adList, env, body);
	}
}