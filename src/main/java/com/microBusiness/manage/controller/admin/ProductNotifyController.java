/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ProductNotify;
import com.microBusiness.manage.service.ProductNotifyService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ProductNotify;
import com.microBusiness.manage.service.ProductNotifyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("adminProductNotifyntroller")
@RequestMapping("/admin/product_notify")
public class ProductNotifyController extends BaseController {

	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody
	Message send(Long[] ids) {
		List<ProductNotify> productNotifies = productNotifyService.findList(ids);
		int count = productNotifyService.send(productNotifies);
		return Message.success("admin.productNotify.sentSuccess", count);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable, ModelMap model) {
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("hasSent", hasSent);
		model.addAttribute("page", productNotifyService.findPage(null, isMarketable, isOutOfStock, hasSent, pageable));
		return "/admin/product_notify/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		productNotifyService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}