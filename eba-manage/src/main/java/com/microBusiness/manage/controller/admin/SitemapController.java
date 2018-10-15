/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.service.StaticService;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.util.SystemUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminSitemapController")
@RequestMapping("/admin/sitemap")
public class SitemapController extends BaseController {

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("sitemapIndex");
		model.addAttribute("sitemapIndexPath", templateConfig.getRealStaticPath());
		return "/admin/sitemap/generate";
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public String generate(RedirectAttributes redirectAttributes) {
		staticService.generateSitemap();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:generate.jhtml";
	}

}