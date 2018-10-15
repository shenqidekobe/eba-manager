/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.FileType;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.GoodsImportImageInfo;
import com.microBusiness.manage.entity.GoodsImportInfo;
import com.microBusiness.manage.entity.GoodsImportLog;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Parameter;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.AdService;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AttributeService;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.service.GoodsImportImageInfoService;
import com.microBusiness.manage.service.GoodsImportInfoService;
import com.microBusiness.manage.service.GoodsImportLogService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.ParameterValueService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SnService;
import com.microBusiness.manage.service.SpecificationItemService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.TagService;
import com.microBusiness.manage.util.CommonUtils;

@Controller("adminGoodsController")
@RequestMapping("/admin/goods")
public class GoodsController extends BaseController {

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
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
	private NeedProductService needProductService;

	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;

	@Resource(name = "goodsImportLogServiceImpl")
	private GoodsImportLogService goodsImportLogService;

	@Resource(name = "goodsImportInfoServiceImpl")
	private GoodsImportInfoService goodsImportInfoService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource
	private SnService snService;
	@Resource
	private GoodsImportImageInfoService goodsImportImageInfoService;
	@Resource
	private SupplierProductService supplierProductService;
	@Resource
	private AdService adService;

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
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getParameters())) {
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
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getAttributes())) {
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
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getSpecifications())) {
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
		model.addAttribute("productCategoryTree", productCategoryService
				.findTreeList(super.getCurrentSupplier(), null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());

		model.addAttribute("storageConditions",
				Goods.StorageConditions.values());
		model.addAttribute("weightUnits", Goods.WeightUnit.values());
		model.addAttribute("volumeUnits", Goods.VolumeUnit.values());
		model.addAttribute("natures", Goods.Nature.values());
		model.addAttribute("units", Goods.Unit.values());
		
		
		List<Order> adorders = new ArrayList<Order>();
		Order adorder = new Order();
		adorder.setDirection(Direction.asc);
		adorder.setProperty("order");
		adorders.add(adorder);
		List<Filter> actfilters = new ArrayList<Filter>();
		Filter actfilter = new Filter();
		actfilter.setIgnoreCase(true);
		actfilter.setOperator(Operator.eq);
		actfilter.setProperty("adPosition");
		actfilter.setValue(4);//活动广告位ID
		actfilters.add(actfilter);
		List<Ad> actList = adService.findList(4, actfilters, adorders);
		model.addAttribute("actList", actList);

		return "/admin/goods/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Goods goods, ProductForm productForm,
			ProductListForm productListForm, Long productCategoryId,
			Long brandId, Long[] promotionIds, Long[] tagIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		goods.setProductCategory(productCategoryService.find(productCategoryId));
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService
				.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_"
					+ attribute.getId());
			String attributeValue = attributeService.toAttributeValue(
					attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(goods.getSn())
				&& goodsService.snExists(goods.getSn(),
						this.getCurrentSupplier())) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		// TODO: 2017/1/23 新增时候，添加所属供应商
		goods.setSupplier(admin.getSupplier());

		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products)
					|| !isValid(products, getValidationGroup(goods.getType()),
							BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			goodsService.save(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null
					|| !isValid(product, getValidationGroup(goods.getType()),
							BaseEntity.Save.class)) {
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
		/**
		 * 2.3.0版本修改
		 * 2018.2.7
		 * yuezhiwei
		 */
		/*model.addAttribute("productCategoryTree", productCategoryService
				.findTree(super.getCurrentSupplier(), null));*/
		model.addAttribute("productCategoryTree", productCategoryService
				.findTreeList(super.getCurrentSupplier(), null));
		//此处判断商品是否在供应中（包括企业供应和个体供应），在供应中的商品不能修改商品分类
		List<Goods> goodsList = goodsService.supplyOfGoods(id);
		if(goodsList.size() > 0) {
			model.addAttribute("supplyStatus", "inSupply");
		}else {
			model.addAttribute("supplyStatus", "notSupply");
		}
		
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		Goods goods = goodsService.find(id);
		model.addAttribute("goods", goods);

		model.addAttribute("storageConditions",
				Goods.StorageConditions.values());
		model.addAttribute("weightUnits", Goods.WeightUnit.values());
		model.addAttribute("volumeUnits", Goods.VolumeUnit.values());
		model.addAttribute("natures", Goods.Nature.values());
		model.addAttribute("units", Goods.Unit.values());
		model.addAttribute("labels", goods.getLabels());
		
		List<Order> adorders = new ArrayList<Order>();
		Order adorder = new Order();
		adorder.setDirection(Direction.asc);
		adorder.setProperty("order");
		adorders.add(adorder);
		List<Filter> actfilters = new ArrayList<Filter>();
		Filter actfilter = new Filter();
		actfilter.setIgnoreCase(true);
		actfilter.setOperator(Operator.eq);
		actfilter.setProperty("adPosition");
		actfilter.setValue(4);//活动广告位ID
		actfilters.add(actfilter);
		List<Ad> actList = adService.findList(4, actfilters, adorders);
		model.addAttribute("actList", actList);

		return "/admin/goods/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Goods goods, ProductForm productForm,
			ProductListForm productListForm, Long id, Long productCategoryId,
			Long brandId, Long[] promotionIds, Long[] tagIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes , String supplyStatus) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		Goods pGoods = goodsService.find(id);
		goods.setType(pGoods.getType());
		if(supplyStatus.equalsIgnoreCase("notSupply")) {
			goods.setProductCategory(productCategoryService.find(productCategoryId));
		}else {
			goods.setProductCategory(pGoods.getProductCategory());
		}
		
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService
				.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_"
					+ attribute.getId());
			String attributeValue = attributeService.toAttributeValue(
					attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products)
					|| !isValid(products, getValidationGroup(goods.getType()),
							BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null
					|| !isValid(product, getValidationGroup(goods.getType()),
							BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Goods.Type type, Long productCategoryId, Long brandId,
			Long promotionId, Long tagId, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService
				.findTree(super.getCurrentSupplier(), null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
//		model.addAttribute("orSearchValue", pageable.getOrSearchValue());
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", goodsService.findPage(type, productCategory,
				brand, promotion, tag, null, null, null, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert, null, null, pageable,
				adminService.getCurrentSupplier(), null, null));



		model.addAttribute("currSupplier", super.getCurrentSupplier());
		return "/admin/goods/list";
	}

	@ResponseBody
	@RequestMapping(value = "/asynclist", method = RequestMethod.GET)
	public JsonEntity asynclist(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		Page<Goods> page = goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable , adminService.getCurrentSupplier(), null, null);
		List<Goods> list = page.getContent();
		List<Object> goodList = new ArrayList<Object>();
		for(Goods goods : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", goods.getId());
			map.put("name", goods.getName());
			map.put("price", goods.getPrice());
			//多规格（返回true/false）
			map.put("specification", goods.hasSpecification());
			map.put("image", goods.getImage());
			goodList.add(map);
		}
		return JsonEntity.successMessage(goodList);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		Supplier supplier = super.getCurrentSupplier();
		goodsService.removeGood(ids, supplier);
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

	public enum GoodListStatus {
		add, edit
	}

	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
	public String selectList(Goods.Type type, Long productCategoryId,
			Long brandId, Long promotionId, Long tagId, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Pageable pageable, ModelMap model,
			GoodListStatus goodListStatus) {
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService
				.findTree(super.getCurrentSupplier(), null));
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
		model.addAttribute("goodListStatus", goodListStatus);
		model.addAttribute("page", goodsService.findPage(type, productCategory,
				brand, promotion, tag, null, null, null, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert, null, null, pageable,
				adminService.getCurrentSupplier(), true, null));
		return "/admin/goods/goods_list";
	}

	@RequestMapping(value = "/distribution", method = RequestMethod.GET)
	public String distribution(ModelMap model, Pageable pageable,
			SupplierSupplier.Status status) {
		Supplier supplier = super.getCurrentSupplier();
		model.put("page", supplierSupplierService.supplyDistributionList(
				pageable, null, supplier, SupplierSupplier.Status.inTheSupply,
				null));
		return "/admin/goods/distribution";
	}

	@ResponseBody
	@RequestMapping(value = "/querySupplierSupplier", method = RequestMethod.GET)
	public JsonEntity querySupplierSupplier(Long id) {
		Supplier supplier = super.getCurrentSupplier();

		SupplierSupplier supplierSupplier = supplierSupplierService.find(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", supplierSupplier.getId());
		map.put("startDate", supplierSupplier.getStartDate());
		map.put("endDate", supplierSupplier.getEndDate());

		List<Long> goodIdList = new ArrayList<Long>();

		List<Goods> goods = goodsService.queryDistributionGoods(supplier, id);
		for (Goods gd : goods) {
			goodIdList.add(gd.getSource().getId());
		}
		map.put("goodIdList", goodIdList);

		return JsonEntity.successMessage(map);
	}

	@RequestMapping(value = "/distributionGoodList", method = RequestMethod.GET)
	public String distributionGoodList(String supplierSupplierId, Long needId,
			ModelMap model, Pageable pageable, Long productCategoryId) {
		SupplierSupplier supplierSupplier = supplierSupplierService.find(Long
				.parseLong(supplierSupplierId));
		Set<SupplierProduct> supplierProducts = supplierSupplier
				.getSupplierProducts();
		List<Long> goodIds = new ArrayList<>();
		for (SupplierProduct supplierProduct : supplierProducts) {
			// FIXME: 2017/3/14 这里不建议这样写，而且并没有去重 ， 分页总数应该重新计算
			if (!goodIds.contains(supplierProduct.getProducts().getGoods()
					.getId())) {
				goodIds.add(supplierProduct.getProducts().getGoods().getId());
			}
		}
		Page<Goods> page = goodsService.findBySupplierSupplierPage(goodIds,
				pageable, productCategoryId);
		List<Goods> goods = page.getContent();
		for (Goods good : goods) {
			for (SupplierProduct supplierProduct : supplierProducts) {
				// FIXME: 2017/3/14 这里不能这样比较，应该使用compareTo 或 使用 equals
				if (good.getId().compareTo(
						supplierProduct.getProducts().getGoods().getId()) == 0) {
					good.getSupplierProducts().add(supplierProduct);
				}
			}
		}
		model.put("page", page);
		model.put("needId", needId);
		model.put("supplierSupplier", supplierSupplier);
		model.put("productCategoryTree", productCategoryService
				.findByAssSupplier(Long.parseLong(supplierSupplierId)));
		model.put("productCategoryId", productCategoryId);
		return "/admin/goods/distributionGoods_list";
	}

	/**
	 * 复制分销商品
	 *
	 * @param supplierSupplierId
	 * @param goodsList
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copySupplierGoods", method = RequestMethod.POST)
	public Message copySupplierGoods(Long supplierSupplierId, Long[] goodsList) {
		try {
			Supplier supplier = super.getCurrentSupplier();
			SupplierSupplier supplierSupplier = supplierSupplierService
					.find(supplierSupplierId);

			// 查询已经复制的商品
			List<Goods> goods = goodsService.queryDistributionGoods(supplier,
					supplierSupplierId);
			List<Long> goodIdList = new ArrayList<Long>(
					Arrays.asList(goodsList));
			for (Goods gd : goods) {
				Long goodId = gd.getSource().getId();
				for (int i = 0; i < goodIdList.size(); i++) {
					if (goodId.equals(goodIdList.get(i))) {
						goodIdList.remove(i);
					}
				}
			}
			goodsService.copySupplierGoods(supplierSupplier, goodIdList,
					supplier);
			return SUCCESS_MESSAGE;
		} catch (Exception e) {

			return ERROR_MESSAGE;
		}

	}

	@ResponseBody
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public JsonEntity queryById(Long id) {
		Goods goods = goodsService.find(id);
		return JsonEntity.successMessage(goods);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService
				.findTree(super.getCurrentSupplier(), null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("goods", goodsService.find(id));

		model.addAttribute("storageConditions",
				Goods.StorageConditions.values());
		model.addAttribute("weightUnits", Goods.WeightUnit.values());
		model.addAttribute("volumeUnits", Goods.VolumeUnit.values());
		model.addAttribute("natures", Goods.Nature.values());
		model.addAttribute("units", Goods.Unit.values());
		return "/admin/goods/view";
	}

	@RequestMapping(value = "/distributionView", method = RequestMethod.GET)
	public String distributionView(Long id, ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService
				.findTree(super.getCurrentSupplier(), null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("goods", goodsService.find(id));

		model.addAttribute("storageConditions",
				Goods.StorageConditions.values());
		model.addAttribute("weightUnits", Goods.WeightUnit.values());
		model.addAttribute("volumeUnits", Goods.VolumeUnit.values());
		model.addAttribute("natures", Goods.Nature.values());
		model.addAttribute("units", Goods.Unit.values());
		return "/admin/goods/distributionView";
	}

	/**
	 * 批量删除验证，有个体供应关系的不能删除
	 *
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value = "/checkBatchDelete", method = RequestMethod.POST)
	public @ResponseBody
	Message checkBatchDelete(Long[] ids, ModelMap modelMap) {
		if (ids == null) {
			return ERROR_MESSAGE;
		}
		Supplier currSupplier = super.getCurrentSupplier();
		for (Long id : ids) {
			Goods goods = goodsService.find(id);
			boolean b=true;
			if (goods.getSupplierSupplier() != null) {
				Date now=new Date();
				if (goods.getSupplierSupplier().getStatus() != SupplierSupplier.Status.inTheSupply || 
						now.getTime()<goods.getSupplierSupplier().getStartDate().getTime() ||
						now.getTime() >goods.getSupplierSupplier().getEndDate().getTime() ) {
					b=false;
				}
			}
			if (b) {
				for (Product product : goods.getProducts()) {
					List<NeedProduct> needProducts = needProductService
							.findNeedProductByProduct(product, currSupplier,
									new ArrayList<SupplyNeed.Status>() {
								{
									this.add(SupplyNeed.Status.SUPPLY);
								}
							});
					
					if (CollectionUtils.isNotEmpty(needProducts)) {
						return Message.error("商品处于门店供应中，不可被删除");
					}
					List<SupplierProduct> supplierProducts=supplierProductService.findByProduct(product);
					if (CollectionUtils.isNotEmpty(supplierProducts)) {
						return Message.error("商品处于企业供应中，不可被删除");
					}
				}
			}
			
		}
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/importMore", method = RequestMethod.GET)
	public String importMore(ModelMap model) {
		return "/admin/goods/import";
	}

	@RequestMapping(value = "/viewMore", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity viewMore(ModelMap model, MultipartFile multipartFile) {

		GoodsImportLog goodsImportLog = goodsService.dealImportMore(
				multipartFile, adminService.getCurrent(),
				super.getCurrentSupplier());
		if (null == goodsImportLog || null == goodsImportLog.getId()) {
			return new JsonEntity("15001", "导入失败,尝试检查是否使用下载模板");
		}
		return JsonEntity.successMessage(goodsImportLog.getId());
	}

	@RequestMapping(value = "/importList", method = RequestMethod.GET)
	public String importList(Pageable pageable, ModelMap model, Long logId) {

		if (null == logId) {
			return ERROR_VIEW;
		}

		GoodsImportLog goodsImportLog = goodsImportLogService.find(logId);

		Page<GoodsImportInfo> page = goodsImportInfoService.findPage(pageable,
				goodsImportLog);

		model.put("page", page);
		model.put("goodsImportLog", goodsImportLog);

		return "/admin/goods/importList";
	}

	@RequestMapping(value = "/saveMore", method = RequestMethod.POST)
	public String saveMore(ModelMap model, Long logId,
			RedirectAttributes redirectAttributes) {

		if (null == logId) {
			return ERROR_VIEW;
		}

		GoodsImportLog goodsImportLog = goodsImportLogService.find(logId);

		if (null == goodsImportLog) {
			return ERROR_VIEW;
		}

		boolean isOk = goodsService.saveMore(goodsImportLog,
				adminService.getCurrent());
		if (isOk) {
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		} else {
			addFlashMessage(redirectAttributes, ERROR_MESSAGE);
		}
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/getGoods", method = RequestMethod.GET)
	@ResponseBody
	public boolean getGoods(String name, String oldName) {
		if (name.equals(oldName)) {
			return true;
		}
		List<Goods> goods = goodsService.findByName(name,
				this.getCurrentSupplier(),Types.platform);
		if (goods.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@RequestMapping(value = "/importImg", method = RequestMethod.GET)
	public String importImg(ModelMap model) {
		String batch = snService.generate(Sn.Type.goodsImage);
		model.addAttribute("batch", batch);
		return "/admin/goods/importImg";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> upload(FileType fileType, MultipartFile file, String batch) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (fileType == null || file == null || file.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			data.put("state", message("admin.message.error"));
			return data;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
			data.put("state", message("admin.upload.invalid"));
			return data;
		}

		String sn = FilenameUtils.getBaseName(file.getOriginalFilename());
//		String sn = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4);
		if (CommonUtils.fileNameTest(sn) || CommonUtils.fileNameTestSpace(sn)) {
			String snStr = sn.substring(0, sn.length() - 3);
			Goods goods = goodsService.findBySn(snStr, this.getCurrentSupplier());
			if (goods == null) {
				data.put("message", "未找到对应商品");
				data.put("state", "admin.upload.error");
				return data;
			}
		} else {
			// 主图
			if (!CommonUtils.snTest(sn) && !CommonUtils.snTestSpace(sn)) {
				data.put("message", "商品编号有误");
				data.put("state", "admin.upload.error");
				return data;
			}

			Goods goods = goodsService.findBySn(sn, this.getCurrentSupplier());
			if (goods == null) {
				data.put("message", "未找到对应商品");
				data.put("state", "admin.upload.error");
				return data;
			}
		}

		String url = fileService.upload(fileType, file, false, true);
		if (StringUtils.isEmpty(url)) {
			data.put("message", Message.warn("admin.upload.error"));
			data.put("state", message("admin.upload.error"));
			return data;
		}
		Map<String, Object> map = new HashMap();
		synchronized(this){
			map = goodsService.uploadImage(sn, url, this.getCurrentSupplier(), batch);
		}

		return map;
	}

	@RequestMapping(value = "/importImageList", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> importImageList(Pageable pageable, ModelMap model,
			String batch) {

		if (StringUtils.isEmpty(batch)) {
			return null;
		}

		Page<GoodsImportImageInfo> page = goodsImportImageInfoService.findPage(pageable, batch);

		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

		for (GoodsImportImageInfo goodsImportImageInfo : page.getContent()) {
			Map<String, Object> retMap = new HashMap<>();
			retMap.put("sn", goodsImportImageInfo.getSn());
			retMap.put("image", goodsImportImageInfo.getImage());
			retMap.put("images", goodsImportImageInfo.getImages());
			retMap.put("name", goodsImportImageInfo.getName());
			reList.add(retMap);
		}

		return reList;
	}

	@RequestMapping(value = "/saveMoreImage", method = RequestMethod.GET)
	public String saveMoreImage(ModelMap model, String batch,RedirectAttributes redirectAttributes) {

		if (StringUtils.isEmpty(batch)) {
			return ERROR_VIEW;
		}

		boolean isOk = goodsService.saveMore(batch, adminService.getCurrent());
		if (isOk) {
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		} else {
			addFlashMessage(redirectAttributes, ERROR_MESSAGE);
		}
		return "redirect:list.jhtml";
	}

}