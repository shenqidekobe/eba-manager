/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.PromotionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTreeList(super.getCurrentSupplier() , null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		return "/admin/product_category/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProductCategory productCategory, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		productCategory.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setGoods(null);
		productCategory.setParameters(null);
		productCategory.setAttributes(null);
		productCategory.setSpecifications(null);
		productCategory.setSupplier(super.getCurrentSupplier());
		productCategory.setTypes(Types.platform);
		productCategoryService.save(productCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(id);
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("children", productCategoryService.findChildren(productCategory, true, null));
		return "/admin/product_category/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProductCategory productCategory, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		productCategory.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				return ERROR_VIEW;
			}
			List<ProductCategory> children = productCategoryService.findChildren(parent, true, null);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		productCategoryService.update(productCategory, "treePath", "grade", "children", "goods", "parameters", "attributes", "specifications","supplier","types");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model , String searchName) {
		//model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("searchName", searchName);
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , searchName));
		return "/admin/product_category/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		Set<Goods> goods = productCategory.getGoods();
		if (goods != null && !goods.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistProductNotAllowed");
		}
		productCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/getCategory" , method = RequestMethod.GET)
	@ResponseBody
	public boolean getCategory(String name, String oldName, Long parentId) {
		if (name.equals(oldName)) {
			return true;
		}
		
		List<ProductCategory> list = productCategoryService.findByParent(super.getCurrentSupplier(), productCategoryService.find(parentId), name);
		if (list.size() > 0) {
			return false;
		}else {
			return true;
		}
	}

}