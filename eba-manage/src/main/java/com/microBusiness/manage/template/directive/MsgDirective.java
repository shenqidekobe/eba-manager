/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessageCompamy;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.NewMessageCompamyService;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("msgDirective")
public class MsgDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "msgPage";

	@Resource
	private NewMessageCompamyService newMessageCompamyService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Pageable pageable = new Pageable();
		
		Admin admin = adminService.getCurrent();
		Page<NewMessageCompamy> msgPage = null;
		if (admin != null) {
			msgPage = newMessageCompamyService.query(admin, true, pageable);
		}
		
		setLocalVariable(VARIABLE_NAME, msgPage, env, body);
	}
}