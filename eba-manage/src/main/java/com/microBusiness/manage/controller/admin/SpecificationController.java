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
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Specification;
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

@Controller("adminSpecificationController")
@RequestMapping("/admin/specification")
public class SpecificationController extends BaseController {

	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", specificationService.find(sampleId));
		model.addAttribute("productCategoryTree", productCategoryService.findTreeList(super.getCurrentSupplier() , null));
		return "/admin/specification/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Specification specification, Long productCategoryId, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		specification.setProductCategory(productCategoryService.find(productCategoryId));
		if (!isValid(specification, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}

		Supplier currSupplier = super.getCurrentSupplier() ;
		specification.setSupplier(currSupplier);
		specification.setTypes(Types.platform);

		specificationService.save(specification);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("specification", specificationService.find(id));
		return "/admin/specification/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Specification specification, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if (!isValid(specification)) {
			return ERROR_VIEW;
		}
		specificationService.update(specification, "productCategory" , "supplier" , "types");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Supplier currSupplier = super.getCurrentSupplier() ;
		model.addAttribute("page", specificationService.findPage(pageable , currSupplier));
		return "/admin/specification/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		specificationService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/getSpecification" , method = RequestMethod.GET)
	@ResponseBody
	public boolean getSpecification(String name, String oldName ,Long parentId, ModelMap model) {
		if (name.equals(oldName)) {
			return true;
		}
		
		List<Specification> list = specificationService.findByName(name, this.getCurrentSupplier(), productCategoryService.find(parentId));
		if (list.size() > 0) {
			return false;
		}else {
			return true;
		}
	}

}