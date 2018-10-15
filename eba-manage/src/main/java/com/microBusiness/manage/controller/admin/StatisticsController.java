/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.service.CacheService;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.service.CacheService;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("statisticsController")
@RequestMapping("/admin/statistics")
public class StatisticsController extends BaseController {

	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("cnzzSiteId", setting.getCnzzSiteId());
		model.addAttribute("cnzzPassword", setting.getCnzzPassword());
		return "/admin/statistics/view";
	}

	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("isCnzzEnabled", setting.getIsCnzzEnabled());
		return "/admin/statistics/setting";
	}

	@RequestMapping(value = "/setting", method = RequestMethod.POST)
	public String setting(@RequestParam(defaultValue = "false") Boolean isEnabled, RedirectAttributes redirectAttributes) {
		Setting setting = SystemUtils.getSetting();
		if (isEnabled) {
			if (StringUtils.isEmpty(setting.getCnzzSiteId()) || StringUtils.isEmpty(setting.getCnzzPassword())) {
				String domain = setting.getSiteUrl().replaceAll("(^[\\s\\S]*?[^a-zA-Z0-9-.]+)|([^a-zA-Z0-9-.][\\s\\S]*$)", "");
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("domain", domain);
				parameterMap.put("key", DigestUtils.md5Hex(domain + "Lfg4uP0H"));
				String content = WebUtils.get("http://intf.cnzz.com/user/companion/utlz.php", parameterMap);
				if (StringUtils.contains(content, "@")) {
					setting.setCnzzSiteId(StringUtils.substringBefore(content, "@"));
					setting.setCnzzPassword(StringUtils.substringAfter(content, "@"));
				}
			}
		}
		setting.setIsCnzzEnabled(isEnabled);
		SystemUtils.setSetting(setting);
		cacheService.clear();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:setting.jhtml";
	}

}