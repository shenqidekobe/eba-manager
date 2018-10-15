package com.microBusiness.manage.controller.shop.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.CompanyGoods.GenerateMethod;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.entity.CompanyGoods.PubfromSource;
import com.microBusiness.manage.entity.CompanyGoods.Status;
import com.microBusiness.manage.entity.CompanyGoods.Type;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CategoryService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopMemberCompanyGoodsController")
@RequestMapping("/shop/member/supply")
public class MemberCompanyGoodsController extends BaseController {
	
	@Resource
	private CompanyGoodsService companyGoodsService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private AdminService adminService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value = "/addIndex", method = RequestMethod.GET)
	public String addIndex(ModelMap model) {
		model.addAttribute("nav", "addIndex");
		Supplier supplier = adminService.getCurrentSupplier();
		model.addAttribute("supplier", supplier);
		return "/shop/member/supply/index";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		
		model.addAttribute("categoryTree", categoryService.findTree());
		
		model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;
		
		model.addAttribute("sourceTypes", CompanyGoods.SourceType.values());
		model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
		model.addAttribute("units", CompanyGoods.Unit.values());
		model.addAttribute("nav", "addIndex");
		return "/shop/member/supply/add";
	}
	
	@RequestMapping(value = "/addPur", method = RequestMethod.GET)
	public String addPur(ModelMap model) {
		
		model.addAttribute("categoryTree", categoryService.findTree());
		
		model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;
		
		model.addAttribute("sourceTypes", CompanyGoods.SourceType.values());
		model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
		model.addAttribute("units", CompanyGoods.Unit.values());
		model.addAttribute("nav", "addPur");
		return "/shop/member/purchase/add";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model, Long id) {
		CompanyGoods companyGoods = companyGoodsService.find(id);
		model.addAttribute("goods", companyGoods);
		
		Category category = companyGoods.getCategory();
		if(category != null && category.getGrade() == 0){
			model.addAttribute("ca1", category.getId());
		}
		if(category != null && category.getGrade() == 1){
			model.addAttribute("ca2", category.getId());
			model.addAttribute("ca1", category.getParent().getId());
		}
		if(category != null && category.getGrade() == 2){
			model.addAttribute("ca3", category.getId());
			model.addAttribute("ca2", category.getParent().getId());
			model.addAttribute("ca1", category.getParent().getParent().getId());
		}
		
		model.addAttribute("categoryTree", categoryService.findTree());
		
		model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;
		
		model.addAttribute("sourceTypes", CompanyGoods.SourceType.values());
		model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
		model.addAttribute("units", CompanyGoods.Unit.values());
		model.addAttribute("nav", "addIndex");
		return "/shop/member/supply/add";
	}
	
	@RequestMapping(value = "/editPur", method = RequestMethod.GET)
	public String editPur(ModelMap model, Long id) {
		CompanyGoods companyGoods = companyGoodsService.find(id);
		model.addAttribute("goods", companyGoods);
		
		Category category = companyGoods.getCategory();
		if(category != null && category.getGrade() == 0){
			model.addAttribute("ca1", category.getId());
		}
		if(category != null && category.getGrade() == 1){
			model.addAttribute("ca2", category.getId());
			model.addAttribute("ca1", category.getParent().getId());
		}
		if(category != null && category.getGrade() == 2){
			model.addAttribute("ca3", category.getId());
			model.addAttribute("ca2", category.getParent().getId());
			model.addAttribute("ca1", category.getParent().getParent().getId());
		}
		
		model.addAttribute("categoryTree", categoryService.findTree());
		
		model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;
		
		model.addAttribute("sourceTypes", CompanyGoods.SourceType.values());
		model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
		model.addAttribute("units", CompanyGoods.Unit.values());
		model.addAttribute("nav", "addPur");
		return "/shop/member/purchase/add";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(ModelMap model, Long id) {
		CompanyGoods companyGoods = companyGoodsService.find(id);
		model.addAttribute("companyGoods", companyGoods);
		return "/shop/supplyGoods/details";
	}
	
	@RequestMapping(value = "/purdetail", method = RequestMethod.GET)
	public String purdetail(ModelMap model, Long id) {
		CompanyGoods companyGoods = companyGoodsService.find(id);
		model.addAttribute("goods", companyGoods);
		return "/shop/needGoods/details";
	}
	
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model, Pageable pageable) {
		List<Filter> filterList = pageable.getFilters();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Filter.Operator.eq);
		filter.setProperty("pubType");
		filter.setValue(PubType.pub_supply);
		filterList.add(filter);
		Supplier supplier = adminService.getCurrentSupplier();
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Filter.Operator.eq);
		filter.setProperty("supplier");
		filter.setValue(supplier);
		filterList.add(filter);
		pageable.setOrderDirection(Direction.desc);
		pageable.setOrderProperty("createDate");
		
		Page<CompanyGoods> goodsPage = companyGoodsService.findPage(pageable);
		model.addAttribute("page", goodsPage);
		model.addAttribute("nav", "list");
		return "/shop/member/supply/list";
	}

	@RequestMapping(value = "/purchaseList", method = RequestMethod.GET)
	public String purchaseList(ModelMap model, Pageable pageable) {
		List<Filter> filterList = pageable.getFilters();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Filter.Operator.eq);
		filter.setProperty("pubType");
		filter.setValue(PubType.pub_need);
		filterList.add(filter);
		Supplier supplier = adminService.getCurrentSupplier();
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Filter.Operator.eq);
		filter.setProperty("supplier");
		filter.setValue(supplier);
		filterList.add(filter);
		pageable.setOrderDirection(Direction.desc);
		pageable.setOrderProperty("createDate");
		Page<CompanyGoods> goodsPage = companyGoodsService.findPage(pageable);
		model.addAttribute("page", goodsPage);
		model.addAttribute("nav", "purchaseList");
		return "/shop/member/purchase/list";
	}
	
	
	@RequestMapping(value = "/dinghuomeList", method = RequestMethod.GET)
	public String dhmlist(Goods.Type type, Long productCategoryId, String productCategoryIdStr, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		pageable.setOrderDirection(Direction.desc);
		pageable.setOrderProperty("createDate");
		Supplier supplier = adminService.getCurrentSupplier();
		List<Filter> filterList = pageable.getFilters();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Filter.Operator.eq);
		filter.setProperty("goods.supplier");
		filter.setValue(supplier);
		filterList.add(filter);

		if(productCategoryId != null && productCategoryId!=0){
			filter = new Filter();
			filter.setIgnoreCase(true);
			filter.setOperator(Filter.Operator.in);
			filter.setProperty("goods.productCategory");
			List<ProductCategory> list = new ArrayList<ProductCategory>();
			list.addAll(productCategoryService.findLike(productCategoryId));
			list.add(productCategoryService.find(productCategoryId));
			filter.setValue(list);
			filterList.add(filter);
		}

		Page<Product> goodsPage = productService.findPage(pageable);
//		Page<Goods> goodsPage = goodsService.findPage(type, null, null, null, null, 
//				null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable , 
//				supplier);
		model.addAttribute("page", goodsPage);
		model.addAttribute("productCategoryIdStr", productCategoryIdStr);
		model.addAttribute("nav", "addIndex");
		model.addAttribute("searchValue", pageable.getSearchValue());

		model.addAttribute("productCategory", JsonUtils.toJson(ProductCategory.getCategoryIterator(new HashSet<ProductCategory>(productCategoryService.findRoots(8, supplier)))));

		model.addAttribute("categoryTree", categoryService.findTree());

		model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;

		return "/shop/member/supply/dinghuomeList";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ModelMap model, CompanyGoods companyGoods, Long categoryId, HttpServletRequest request) {
		Category category = categoryService.find(categoryId);
		if(category != null){
			companyGoods.setCategory(category);
		}
		Supplier supplier = this.getCurrentAdmin(request).getSupplier();
		companyGoods.setSupplier(supplier);
		companyGoods.setHits(0l);
		companyGoods.setPubfromSource(PubfromSource.pub_diect);
		companyGoods.setCreateDate(new Date());
		companyGoods.setDelflag(Delflag.delflag_no);
		companyGoods.setCreateDate(new Date());
		companyGoods.setGenerateMethod(GenerateMethod.none);
		companyGoods.setType(Type.general);
		companyGoods.setStatus(Status.status_wait);
		companyGoods.setPubType(PubType.pub_supply);
		if(companyGoods.getId() == null){
			companyGoodsService.save(companyGoods);
		}else{
			companyGoodsService.update(companyGoods);
		}
		return "redirect:list.jhtml";
	}
	
	@RequestMapping(value = "/saveFromDhm", method = RequestMethod.POST)
	public @ResponseBody Message saveFromDhm(ModelMap model, CompanyGoods companyGoods, String productIds, String categoryIds, HttpServletRequest request) {
		Supplier supplier = this.getCurrentAdmin(request).getSupplier();
		companyGoods.setSupplier(supplier);
		CompanyGoods companyGoodsFlag =companyGoodsService.saveFromDhm(companyGoods, productIds, categoryIds);
		if (companyGoodsFlag == null) {
			return ERROR_MESSAGE;
		}

		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/savePur", method = RequestMethod.POST)
	public String savePur(ModelMap model, CompanyGoods companyGoods, Long categoryId, HttpServletRequest request) {
		Category category = categoryService.find(categoryId);
		if(category != null){
			companyGoods.setCategory(category);
		}
		Supplier supplier = this.getCurrentAdmin(request).getSupplier();
		companyGoods.setSupplier(supplier);
		companyGoods.setHits(0l);
		companyGoods.setPubfromSource(PubfromSource.pub_diect);
		companyGoods.setCreateDate(new Date());
		companyGoods.setDelflag(Delflag.delflag_no);
		companyGoods.setCreateDate(new Date());
		companyGoods.setGenerateMethod(GenerateMethod.none);
		companyGoods.setType(Type.general);
		companyGoods.setPubType(PubType.pub_need);
		companyGoods.setMarketPrice(new BigDecimal(0));
		companyGoods.setStatus(Status.status_wait);
		if(companyGoods.getId() == null){
			companyGoodsService.save(companyGoods);
		}else{
			companyGoodsService.update(companyGoods);
		}
		return "redirect:purchaseList.jhtml";
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public Message remove(ModelMap model, CompanyGoods companyGoods) {
		companyGoodsService.delete(companyGoods.getId());
		return Message.success("删除成功！");
	}
	
	@RequestMapping(value = "/removePur", method = RequestMethod.POST)
	@ResponseBody
	public Message removePur(ModelMap model, CompanyGoods companyGoods) {
		companyGoodsService.delete(companyGoods.getId());
		return Message.success("删除成功！");
	}
	
	
}
