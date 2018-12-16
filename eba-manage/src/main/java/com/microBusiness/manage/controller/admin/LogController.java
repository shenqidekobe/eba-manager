/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.service.LogService;
import com.microBusiness.manage.util.DateUtils;

@Controller("adminLogController")
@RequestMapping("/admin/log")
public class LogController extends BaseController {

	@Resource(name = "logServiceImpl")
	private LogService logService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable,Date startDate , Date endDate ,
			String operator , String ip, ModelMap model) {
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("operator", operator);
        model.addAttribute("ip", ip);
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		model.addAttribute("page", logService.findPage(operator, ip, startDate, endDate, pageable));
		return "/admin/log/list";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("log", logService.find(id));
		return "/admin/log/view";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		logService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		logService.clear();
		return SUCCESS_MESSAGE;
	}

}