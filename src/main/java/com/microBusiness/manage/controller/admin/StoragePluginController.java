/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.service.PluginService;

import com.microBusiness.manage.service.PluginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminStoragePluginController")
@RequestMapping("/admin/storage_plugin")
public class StoragePluginController extends BaseController {

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("storagePlugins", pluginService.getStoragePlugins());
		return "/admin/storage_plugin/list";
	}

}