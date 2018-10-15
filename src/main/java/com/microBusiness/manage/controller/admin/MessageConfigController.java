/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.MessageConfig;
import com.microBusiness.manage.service.MessageConfigService;

import com.microBusiness.manage.service.MessageConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminMessageConfigController")
@RequestMapping("/admin/message_config")
public class MessageConfigController extends BaseController {

	@Resource(name = "messageConfigServiceImpl")
	private MessageConfigService messageConfigService;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("messageConfig", messageConfigService.find(id));
		return "/admin/message_config/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(MessageConfig messageConfig, RedirectAttributes redirectAttributes) {
		if (!isValid(messageConfig)) {
			return ERROR_VIEW;
		}
		messageConfigService.update(messageConfig, "type");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("messageConfigs", messageConfigService.findAll());
		return "/admin/message_config/list";
	}

}