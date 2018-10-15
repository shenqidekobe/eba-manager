/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.service.BrandService;

import com.microBusiness.manage.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminBrandController")
@RequestMapping("/admin/brand")
public class BrandController extends BaseController {

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Brand.Type.values());
		return "/admin/brand/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Brand brand, RedirectAttributes redirectAttributes) {
		if (!isValid(brand)) {
			return ERROR_VIEW;
		}
		if (Brand.Type.text.equals(brand.getType())) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			return ERROR_VIEW;
		}
		brand.setGoods(null);
		brand.setProductCategories(null);
		brandService.save(brand);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Brand.Type.values());
		model.addAttribute("brand", brandService.find(id));
		return "/admin/brand/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Brand brand, RedirectAttributes redirectAttributes) {
		if (!isValid(brand)) {
			return ERROR_VIEW;
		}
		if (Brand.Type.text.equals(brand.getType())) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			return ERROR_VIEW;
		}
		brandService.update(brand, "goods", "productCategories");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", brandService.findPage(pageable));
		return "/admin/brand/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		brandService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}