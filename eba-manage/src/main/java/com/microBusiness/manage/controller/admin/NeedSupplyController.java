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

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.GoodsController.GoodListStatus;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.TagService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * 个体供应
 * 
 * @author Administrator
 *
 */
@Controller("needSupplyController")
@RequestMapping("/admin/needSupply")
public class NeedSupplyController extends BaseController {

	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
	@Resource(name = "supplierProductServiceImpl")
	private SupplierProductService supplierProductService;
	@Resource
	private SupplierService supplierService;
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
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource
	private NeedService needService;
	@Resource
	private CustomerRelationService customerRelationService;
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
    private AdminService adminService ;
	@Resource
    private NeedProductService needProductService ;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model, Pageable pageable, SupplyNeed supplyNeed, String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		supplyNeed.setSupplier(supplier);
		model.put("status", supplyNeed.getStatus());
		model.put("searchName", searchName);
		model.put("page", supplyNeedService.findPage(pageable, supplyNeed, null, null, searchName));
		model.put("statusEnum", SupplyNeed.Status.values());
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/needSupply/list";
	}
	
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncList(Pageable pageable, SupplyNeed supplyNeed, String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		supplyNeed.setSupplier(supplier);
		Page<SupplyNeed> page=supplyNeedService.findPage(pageable, supplyNeed, null, null, searchName);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for (SupplyNeed supplyNeed1 : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", supplyNeed1.getId());
			map.put("name", supplyNeed1.getNeed().getName());
			map.put("status", supplyNeed1.getStatus());
			map.put("assignedModel", supplyNeed1.getAssignedModel());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
		
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		// 获取所有个体
		//List<Need> needList = needService.findBySupplier(supplier, Need.Type.general);
		List<Need> needList = needService.findByList(supplier, Need.Type.general);
		model.addAttribute("needList", needList);
		model.addAttribute("currSupplier", super.getCurrentSupplier());

		return "/admin/needSupply/add";
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id,ModelMap model) {
		SupplyNeed supplyNeed=supplyNeedService.find(id);
		model.put("supplyNeed", supplyNeed);
		return "/admin/needSupply/view";
	}
	
	/**ajax请求 分页获取个体供应商品列表
	 * 
	 * @param supplierSupplierId
	 * @param model
	 * @param pageable
	 * @param productCategoryId
	 * @return
	 */
	@RequestMapping(value = "/asyncViewGoods", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncViewGoods(Long supplyNeedId,Pageable pageable) {
		SupplyNeed supplyNeed=supplyNeedService.find(supplyNeedId);
		
		Page<NeedProduct> page=needProductService.findPage(supplyNeed, pageable);
		List<Map<String, Object>> list=new ArrayList<>();
		for (NeedProduct needProduct : page.getContent()) {
			Product product=needProduct.getProducts();
			Map<String, Object> map=new HashMap<>();
			map.put("id", product.getId());
			map.put("image", product.getGoods().getImage());
			map.put("name", product.getGoods().getName());
			map.put("specificationValues", product.getSpecificationValues());
			map.put("supplyPrice",needProduct.getSupplyPrice());
			map.put("minOrderQuantity", needProduct.getMinOrderQuantity());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
	public String selectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model, GoodListStatus goodListStatus, String searchName) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , null));
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
		model.addAttribute("searchName", searchName);
//		Page<Goods> page = goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
//				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), null, true);
		Page<Product> page = productService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), null, true,searchName);
		
		/*for (Goods goods : page.getContent()) {
			if (goods.getSupplierSupplier() != null) {
				for (Product product : goods.getProducts()) {
					SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(goods.getSupplierSupplier(), product.getSource());
					product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
				}
			}
		}*/
		for (Product product : page.getContent()) {
			SupplierSupplier supplierSupplier = product.getGoods().getSupplierSupplier();
			if( supplierSupplier != null) {
				SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierSupplier, product.getSource());
				product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
				product.setSupplyPrice(supplierProduct.getSupplyPrice());
			}else {
				product.setMinOrderQuantity(1);
				product.setSupplyPrice(product.getPrice());
			}
		}
		
		model.addAttribute("page", page);

		return "/admin/needSupply/goods_list";
	}
	
	/**
	 * 移动端用  ajax分页加载商品列表
	 * @param type
	 * @param productCategoryId
	 * @param brandId
	 * @param promotionId
	 * @param tagId
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param pageable
	 * @param goodListStatus
	 * @param searchName
	 * @return
	 */
	@RequestMapping(value = "/asyncSelectList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncSelectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, GoodListStatus goodListStatus, String searchName) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		Page<Product> page = productService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), null, true,searchName);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for (Product product : page.getContent()) {
			SupplierSupplier supplierSupplier = product.getGoods().getSupplierSupplier();
			if( supplierSupplier != null) {
				SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierSupplier, product.getSource());
				product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
				product.setSupplyPrice(supplierProduct.getSupplyPrice());
			}else {
				product.setMinOrderQuantity(1);
				product.setSupplyPrice(product.getPrice());
			}
			Map<String, Object> map=new HashMap<>();
			map.put("id", product.getId());
			map.put("image", product.getGoods().getImage());
			map.put("name", product.getGoods().getName());
			map.put("specificationValues", product.getSpecificationValues());
			map.put("price", product.getSupplyPrice());
			map.put("minOrderQuantity", product.getMinOrderQuantity());
			list.add(map);
		}

		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(SupplyNeed.AssignedModel assignedModel,Long needId, NeedProductForm NeedProductForm , Integer noticeDay , Boolean openNotice) {
        try{
            Supplier supplier=super.getCurrentSupplier();
            Need need=needService.find(needId);
            SupplyNeed supplyNeed=new SupplyNeed();
            supplyNeed.setSupplier(supplier);
            supplyNeed.setNeed(need);
            //supplyNeed.setStartDate(DateUtils.specifyDateZero(startDate));
            //supplyNeed.setEndDate(DateUtils.specifyDatetWentyour(endDate));
            supplyNeed.setAssignedModel(assignedModel);
            supplyNeed.setOpenNotice(openNotice);
            supplyNeed.setNoticeDay(noticeDay);
            supplyNeedService.save(supplyNeed, NeedProductForm.getNeedProductList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }
	
	public static class NeedProductForm {

        private List<NeedProduct> needProductList;

		public List<NeedProduct> getNeedProductList() {
			return needProductList;
		}

		public void setNeedProductList(List<NeedProduct> needProductList) {
			this.needProductList = needProductList;
		}

    }
	
	@ResponseBody
	@RequestMapping(value = "/validDate", method = RequestMethod.GET)
	public Map<String, Object> validDate(Date startDate,Date endDate,Long id) {

		Supplier supplier = super.getCurrentSupplier();
		SupplyNeed supplyNeed = supplyNeedService.find(id) ;

		List<SupplyNeed.Status> status=new ArrayList<>();
		status.add(SupplyNeed.Status.SUPPLY);
		status.add(SupplyNeed.Status.WILLSUPPLY);
		List<SupplyNeed> list=supplyNeedService.findByDateList(supplier,supplyNeed.getNeed());

		if(list.contains(supplyNeed)){
			list.remove(supplyNeed);
		}
		
		Map<String, Object> map=new HashMap<>();
		if (list.size()>0) {
			map.put("isTrue", false);
		}else {
			map.put("isTrue", true);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public Map<String, Object> verification(Long needId) {
		Supplier supplier = super.getCurrentSupplier();
		Need need=needService.find(needId);
		
		List<SupplyNeed> list=supplyNeedService.findByDateList(supplier,need);
		Map<String, Object> map=new HashMap<>();
		if (list.size()>0) {
			map.put("isTrue", false);
		}else {
			map.put("isTrue", true);
		}
		return map;
	}
	
	@RequestMapping(value = "/viewGoods", method = RequestMethod.GET)
	public String viewGoods(Long supplyNeedId, ModelMap model, Pageable pageable, Long productCategoryId) {
		SupplyNeed supplyNeed=supplyNeedService.find(supplyNeedId);
		/*Set<NeedProduct> needProducts=supplyNeed.getNeedProducts();
		List<Long> goodIds=new ArrayList<>();
		for (NeedProduct needProduct : needProducts) {
			goodIds.add(needProduct.getProducts().getGoods().getId());
		}
		//去掉重复的id
		List<Long> newgoodids = new ArrayList<>();
		for(Long cd : goodIds) {
			if(!newgoodids.contains(cd)) {
				newgoodids.add(cd);
			}
		}
		Page<Goods> page=goodsService.findBySupplierSupplierPage(newgoodids, pageable , productCategoryId);
		List<Goods> goods=page.getContent();
		for (Goods good : goods) {
			for (NeedProduct needProduct : needProducts) {
				if (good.getId().compareTo(needProduct.getProducts().getGoods().getId()) == 0) {
					good.getNeedProducts().add(needProduct);
				}
			}
		}*/
		Page<NeedProduct> page = productService.findPage(supplyNeed, pageable, productCategoryId);
		model.put("page", page);
		model.put("supplyNeed", supplyNeed);
		return "/admin/needSupply/good_list_view";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id,ModelMap model) {
		SupplyNeed supplyNeed=supplyNeedService.find(id);
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		List<NeedProduct> needProducts = needProductService.findList(supplyNeed);
		for(NeedProduct needProduct : needProducts) {
			Map<String, Object> map = new HashMap<String, Object>();
			Long products = needProduct.getProducts().getId();
			map.put("products", products);
			map.put("supplyPrice", needProduct.getSupplyPrice());
			map.put("minOrderQuantity", needProduct.getMinOrderQuantity());
			map.put("min", 1);
			
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put(products.toString(), map);
			result.put(products.toString(), map2);
		}
		model.addAttribute("result", JsonUtils.toJson(result));
		model.addAttribute("supplyNeed", supplyNeed);
		model.addAttribute("currSupplier", super.getCurrentSupplier());
		return "/admin/needSupply/edit";
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(SupplyNeed supplyNeed , NeedProductForm needProductForm , ModelMap model , RedirectAttributes redirectAttributes) {
		try {
			if(null == supplyNeed || supplyNeed.getId() == null){
				model.addAttribute("errorMessage" , "参数错误");
				return ERROR_MESSAGE;
			}
			supplyNeedService.updateNeedSupply(supplyNeed, needProductForm.getNeedProductList());
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return SUCCESS_MESSAGE;
		} catch (Exception e) {
			return ERROR_MESSAGE;
		}
		
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		supplyNeedService.deleted(id);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/batchAddIndex", method = RequestMethod.GET)
	public String batchAddIndex(ModelMap model,Long[] needIds,Integer pageNumber) {
		if (needIds == null) {
			needIds=new Long[]{};
		}
		if (pageNumber == null) {
			pageNumber=1;
		}
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("needIds", Arrays.asList(needIds));
		return "/admin/needSupply/batch_add_index";
	}
	
	@RequestMapping(value = "/getNeedList", method = RequestMethod.GET)
    public String getNeedList(Pageable pageable, ModelMap model , Need need , String searchName, Date startDate, Date endDate) {
        Admin admin = adminService.getCurrent();
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(admin.getSupplier());
        need.setType(Need.Type.general);
        model.addAttribute("searchName", searchName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
        model.addAttribute("page", needService.findPage(pageable , need , searchName, startDate, endDate));
        return "/admin/needSupply/batch_add_needs";
    }
	
	@RequestMapping(value = "/batchAdd", method = RequestMethod.GET)
	public String batchAdd(ModelMap model,Long[] needIds,Integer pageNumber) {
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("currSupplier", supplier);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("needIds", Arrays.asList(needIds));
		return "/admin/needSupply/batch_add";
	}
	
	@ResponseBody
	@RequestMapping(value = "/batchVerification", method = RequestMethod.GET)
	public JsonEntity batchVerification(Date startDate,Date endDate,Long[] needIds) {
		Supplier supplier = super.getCurrentSupplier();
		List<String> result=new ArrayList<>();
		List<SupplyNeed> list=supplyNeedService.findByDateList(supplier,needIds);
		for (SupplyNeed supplyNeed : list) {
			result.add(supplyNeed.getNeed().getName());
		}
		return JsonEntity.successMessage(result);
	}
	
	@RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    @ResponseBody
    public Message batchSave(SupplyNeed.AssignedModel assignedModel,Long[] needIds, NeedProductForm NeedProductForm , Integer noticeDay , Boolean openNotice) {
        try{
            Supplier supplier=super.getCurrentSupplier();
            supplyNeedService.batchSave(supplier, assignedModel, needIds, NeedProductForm, noticeDay, openNotice);
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }
    }
	
	@RequestMapping(value = "/needsMobile", method = RequestMethod.GET)
	public String needsMobile(String searchName , Pageable pageable , ModelMap model) {
		Page<Need> page= needService.findPage(pageable, getCurrentSupplier(),Need.Type.general,searchName);
		model.addAttribute("searchName", searchName);
		model.addAttribute("page", page);
		return "/admin/needSupply/needs";
	}
	
	@RequestMapping(value = "/asyncNeedsMobile", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncNeedsMobile(String searchName , Pageable pageable , ModelMap model) {
		Page<Need> page= needService.findPage(pageable, getCurrentSupplier(),Need.Type.general,searchName);
		List<Map<String, Object>> list=new ArrayList<>();
		for (Need need : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", need.getId());
			map.put("name", need.getName());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/searchMobile" , method = RequestMethod.GET)
	public String searchMobile() {
		return "/admin/needSupply/search";
	}
	
}
