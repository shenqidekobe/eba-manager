/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Parameter;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AttributeService;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ParameterValueService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SpecificationItemService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.service.TagService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("goodsInitController")
@RequestMapping("/adminlaod/goods")
public class GoodsInitController extends BaseController {

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "parameterValueServiceImpl")
	private ParameterValueService parameterValueService;
	@Resource(name = "specificationItemServiceImpl")
	private SpecificationItemService specificationItemService;
	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		return !goodsService.snExists(sn, this.getCurrentSupplier());
	}

	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> parameters(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
			return data;
		}
		for (Parameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("group", parameter.getGroup());
			item.put("names", parameter.getNames());
			data.add(item);
		}
		return data;
	}

	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> attributes(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
			return data;
		}
		for (Attribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptions());
			data.add(item);
		}
		return data;
	}

	@RequestMapping(value = "/specifications", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> specifications(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return data;
		}
		for (Specification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptions());
			data.add(item);
		}
		return data;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		return "/admin/goods/add";
	}

	@RequestMapping(value = "/save")
	public String save(Goods goods, ProductForm productForm, ProductListForm productListForm, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		goods.setProductCategory(productCategoryService.find(productCategoryId));
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
//		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
//			String value = request.getParameter("attribute_" + attribute.getId());
//			String attributeValue = attributeService.toAttributeValue(attribute, value);
//			goods.setAttributeValue(attribute, attributeValue);
//		}

		if (!isValid(goods, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(goods.getSn()) && goodsService.snExists(goods.getSn(), this.getCurrentSupplier())) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(goods.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			goodsService.save(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(goods.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			goodsService.save(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("goods", goodsService.find(id));
		return "/admin/goods/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Goods goods, ProductForm productForm, ProductListForm productListForm, Long id, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		Goods pGoods = goodsService.find(id);
		goods.setType(pGoods.getType());
		goods.setProductCategory(productCategoryService.find(productCategoryId));
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(goods.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(goods.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable));
		return "/admin/goods/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		goodsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	private Class<?> getValidationGroup(Goods.Type type) {
		Assert.notNull(type);

		switch (type) {
		case general:
			return Product.General.class;
		case exchange:
			return Product.Exchange.class;
		case gift:
			return Product.Gift.class;
		}
		return null;
	}

	public static class ProductForm {

		private Product product;

		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}

	}

	public static class ProductListForm {

		private List<Product> productList;

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}

	}

}