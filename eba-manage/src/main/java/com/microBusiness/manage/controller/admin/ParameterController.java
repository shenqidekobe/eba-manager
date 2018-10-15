/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Parameter;
import com.microBusiness.manage.service.ParameterService;
import com.microBusiness.manage.service.ProductCategoryService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Parameter;
import com.microBusiness.manage.service.ProductCategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminParameterController")
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	@Resource(name = "parameterServiceImpl")
	private ParameterService parameterService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", parameterService.find(sampleId));
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , null));
		return "/admin/parameter/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Parameter parameter, Long productCategoryId, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNames(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		parameter.setProductCategory(productCategoryService.find(productCategoryId));
		if (!isValid(parameter, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		parameterService.save(parameter);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("parameter", parameterService.find(id));
		return "/admin/parameter/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Parameter parameter, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNames(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		if (!isValid(parameter, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}
		parameterService.update(parameter, "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", parameterService.findPage(pageable));
		return "/admin/parameter/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		parameterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}