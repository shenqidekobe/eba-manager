package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.GoodsController.GoodListStatus;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.GoodsService;
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

import org.apache.avro.SchemaBuilder.LongDefault;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("formalSupplyController")
@RequestMapping("/admin/formalSupply")
public class FormalSupplyController extends BaseController {

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
	private CustomerRelationService customerRelationService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model, Pageable pageable , SupplierSupplier.Status status , String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		model.put("status", status);
		model.put("searchName", searchName);
		model.put("page", supplierSupplierService.findPage(pageable, supplier, null, status, searchName));
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/formalSupply/list";
	}
	
	/**
	 * ajax分页请求 企业供应列表
	 * @param pageable
	 * @param status
	 * @param searchName
	 * @return
	 */
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncList(Pageable pageable , SupplierSupplier.Status status , String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		Page<SupplierSupplier> page=supplierSupplierService.findPage(pageable, supplier, null, status, searchName);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for (SupplierSupplier supplierSupplier : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", supplierSupplier.getId());
			map.put("status", supplierSupplier.getStatus());
			map.put("bySupplierName", supplierSupplier.getBySupplier().getName());
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
		// 获取所有企业
		//List<Supplier> suppliers = supplierService.findByStatus(Supplier.Status.verified);
		List<Supplier> suppliers = customerRelationService.findByOfficialCustomers(supplier , null);
		model.addAttribute("suppliers", suppliers);
		model.addAttribute("currSupplier", super.getCurrentSupplier());

		return "/admin/formalSupply/add";
	}
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id,ModelMap model) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		model.put("supplierSupplier", supplierSupplier);
		return "/admin/formalSupply/view";
	}
	@RequestMapping(value = "/viewGoods", method = RequestMethod.GET)
	public String viewGoods(Long supplierSupplierId,ModelMap model,Pageable pageable , Long productCategoryId) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
		/*Set<SupplierProduct> supplierProducts=supplierSupplier.getSupplierProducts();
		List<Long> goodIds=new ArrayList<>();
		for (SupplierProduct supplierProduct : supplierProducts) {
			goodIds.add(supplierProduct.getProducts().getGoods().getId());
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
			for (SupplierProduct supplierProduct : supplierProducts) {
				if (good.getId().compareTo(supplierProduct.getProducts().getGoods().getId()) == 0) {
					good.getSupplierProducts().add(supplierProduct);
				}
			}
		}*/
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		model.put("page", page);
		model.put("supplierSupplier", supplierSupplier);
		return "/admin/formalSupply/good_list_view";
	}
	/**ajax请求 分页获取企业供应商品列表
	 * 
	 * @param supplierSupplierId
	 * @param model
	 * @param pageable
	 * @param productCategoryId
	 * @return
	 */
	@RequestMapping(value = "/asyncViewGoods", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncViewGoods(Long supplierSupplierId,Pageable pageable , Long productCategoryId) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		List<Map<String, Object>> list=new ArrayList<>();
		for (SupplierProduct supplierProduct : page.getContent()) {
			Product product=supplierProduct.getProducts();
			Map<String, Object> map=new HashMap<>();
			map.put("id", product.getId());
			map.put("image", product.getGoods().getImage());
			map.put("name", product.getGoods().getName());
			map.put("specificationValues", product.getSpecificationValues());
			map.put("supplyPrice",supplierProduct.getSupplyPrice());
			map.put("minOrderQuantity", supplierProduct.getMinOrderQuantity());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public Map<String, Object> verification(Date startDate,Date endDate,Long bysupplierId) {
		Supplier supplier = super.getCurrentSupplier();
		Supplier bySupplier=supplierService.find(bysupplierId);
		
		List<SupplierSupplier.Status> status=new ArrayList<>();
		status.add(SupplierSupplier.Status.toBeConfirmed);
		status.add(SupplierSupplier.Status.inTheSupply);
		status.add(SupplierSupplier.Status.suspendSupply);
		//status.add(SupplierSupplier.Status.willSupply);
		List<SupplierSupplier> list=supplierSupplierService.findByDateList(supplier,bySupplier,startDate,endDate,status);
		Map<String, Object> map=new HashMap<>();
		//未确认
		int unc = 0;
		//已确认
		int confirmed = 0;
		for(SupplierSupplier ss : list) {
			if(ss.getStatus().equals(SupplierSupplier.Status.toBeConfirmed)) {
				unc = unc+1;
			}else {
				confirmed = confirmed +1;
			}
		}
		
		if(unc >= 1) {
			map.put("isTrue", false);
		}else if(confirmed >= 2) {
			map.put("isTrue", false);
		}else {
			map.put("isTrue", true);
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
    public Message updateStatus(SupplierSupplier supplierSupplier) {
		supplierSupplierService.updateStatus(supplierSupplier);
        return SUCCESS_MESSAGE;
    }

	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
	public String selectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model, GoodListStatus goodListStatus,String searchName) {
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
//		model.addAttribute("page",
//				goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
//						isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), true, null));
		Page<Product> page = productService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, 
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), true, null,searchName);
		model.addAttribute("page", page);		
		return "/admin/formalSupply/goods_list";
	}
	
	@RequestMapping(value = "/asyncSelectList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncSelectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model, GoodListStatus goodListStatus,String searchName) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		Page<Product> page = productService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, 
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), true, null,searchName);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for (Product product : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", product.getId());
			map.put("image", product.getGoods().getImage());
			map.put("name", product.getGoods().getName());
			map.put("specificationValues", product.getSpecificationValues());
			map.put("price", product.getPrice());
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
    public Message save(Date startDate,Date endDate,Long bysupplierId,SupplierProductForm supplierProductForm , Integer noticeDay , boolean openNotice) {
        try{
            Supplier supplier=super.getCurrentSupplier();
            Supplier bySupplier=supplierService.find(bysupplierId);
            
            //获取批次
            Integer batch = supplierSupplierService.getSupplyBatch(supplier, bySupplier);
            
            SupplierSupplier supplierSupplier=new SupplierSupplier();
            supplierSupplier.setSupplier(supplier);
            supplierSupplier.setBySupplier(bySupplier);
            supplierSupplier.setStartDate(DateUtils.specifyDateZero(startDate));
            supplierSupplier.setEndDate(DateUtils.specifyDatetWentyour(endDate));
            supplierSupplier.setStatus(SupplierSupplier.Status.toBeConfirmed);
            supplierSupplier.setOpenNotice(openNotice);
            supplierSupplier.setNoticeDay(noticeDay);
            supplierSupplier.setSupplyBatch(batch);

            supplierSupplierService.save(supplierSupplier,supplierProductForm.getSupplierProductList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }
	
	public static class SupplierProductForm {

        private List<SupplierProduct> supplierProductList;

        public List<SupplierProduct> getSupplierProductList() {
            return supplierProductList;
        }

        public void setSupplierProductList(List<SupplierProduct> supplierProductList) {
            this.supplierProductList = supplierProductList;
        }
    }

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id,ModelMap model) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		model.put("supplierSupplier", supplierSupplier);
		return "/admin/formalSupply/edit";
	}


	@ResponseBody
	@RequestMapping(value = "/validDate", method = RequestMethod.GET)
	public Map<String, Object> validDate(Date startDate,Date endDate,Long id) {

		Supplier supplier = super.getCurrentSupplier();
		SupplierSupplier supplierSupplier = supplierSupplierService.find(id) ;

		List<SupplierSupplier.Status> status=new ArrayList<>();
		status.add(SupplierSupplier.Status.toBeConfirmed);
		status.add(SupplierSupplier.Status.inTheSupply);
		status.add(SupplierSupplier.Status.suspendSupply);
		status.add(SupplierSupplier.Status.willSupply);
		List<SupplierSupplier> list=supplierSupplierService.findByDateList(supplier,supplierSupplier.getBySupplier(),startDate,endDate,status);

		Map<String, Object> map=new HashMap<>();

		if(list.contains(supplierSupplier)){
			list.remove(supplierSupplier) ;
		}

		if (list.size()>1) {
			map.put("isTrue", false);
		}else {
			map.put("isTrue", true);
		}
		
		return map;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(SupplierSupplier supplierSupplier , ModelMap model , RedirectAttributes redirectAttributes) {
		if(null == supplierSupplier.getId()){
			model.addAttribute("errorMessage" , "参数错误");
			return ERROR_VIEW ;
		}
		if(null == supplierSupplier.getStartDate() || null == supplierSupplier.getEndDate()){
			model.addAttribute("errorMessage" , "参数错误");
			return ERROR_VIEW ;
		}
		supplierSupplier.setEndDate(DateUtils.specifyDatetWentyour(supplierSupplier.getEndDate()));
		supplierSupplierService.update(supplierSupplier);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		supplierSupplierService.deleted(id);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/consultationMobile", method = RequestMethod.GET)
	public String consultationMobile(String searchName , Pageable pageable , ModelMap model) {
		Page<CustomerRelation> page=customerRelationService.findPage(searchName, null, null,null, pageable, getCurrentSupplier());
		model.addAttribute("searchName", searchName);
		model.addAttribute("page", page);
		return "/admin/formalSupply/consultations";
	}
	
	@RequestMapping(value = "/asyncConsultationMobile", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncConsultationMobile(String searchName , Pageable pageable , ModelMap model) {
		Page<CustomerRelation> page=customerRelationService.findPage(searchName, null, null, null, pageable, getCurrentSupplier());
		List<Map<String, Object>> list=new ArrayList<>();
		for (CustomerRelation customerRelation : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", customerRelation.getId());
			map.put("clientName", customerRelation.getClientName());
			map.put("bySupplierId", customerRelation.getBySupplier().getId());
			
			list.add(map);
		}
		return JsonEntity.successMessage(list);
	}
	
	@RequestMapping(value = "/searchMobile" , method = RequestMethod.GET)
	public String searchMobile() {
		return "/admin/formalSupply/search";
	}
	
	/**
	 * 
	 * @Title: editUnconfirmed
	 * @author: yuezhiwei
	 * @date: 2018年3月9日上午11:17:15
	 * @Description: 修改未确认的企业供应
	 * @return: String
	 * id supplierSupplier的id
	 */
	@RequestMapping(value = "/editUnconfirmed", method = RequestMethod.GET)
	public String editUnconfirmed(Long id,ModelMap model) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		List<SupplierProduct> supplierProducts = supplierProductService.getSupplierProductList(supplierSupplier);
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		
		for(int i=0; i<supplierProducts.size(); i++) {
			SupplierProduct supplierProduct = supplierProducts.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			Long productId = supplierProduct.getProducts().getId();
			map.put("products", productId);
			map.put("supplyPrice", supplierProduct.getSupplyPrice().intValue());
			map.put("minOrderQuantity", supplierProduct.getMinOrderQuantity());
			
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put(productId.toString(), map);
			result.put(productId.toString(), map2);
			
		}
		model.put("productIds", JsonUtils.toJson(result));
		model.put("supplierSupplier", supplierSupplier);
		return "/admin/formalSupply/editUnconfirmed";
	}
	
	/**
	 * 
	 * @Title: updateUnconfirmed
	 * @author: yuezhiwei
	 * @date: 2018年3月9日下午8:22:31
	 * @Description: 更新未确认的企业供应
	 * @return: Message
	 */
	@RequestMapping(value = "/updateUnconfirmed", method = RequestMethod.POST)
    @ResponseBody
    public Message updateUnconfirmed(Date startDate,Date endDate,Long supplierSupplierId,SupplierProductForm supplierProductForm) {
        try{
            SupplierSupplier supplierSupplier = supplierSupplierService.find(supplierSupplierId);
            supplierSupplier.setStartDate(DateUtils.specifyDateZero(startDate));
            supplierSupplier.setEndDate(DateUtils.specifyDatetWentyour(endDate));

            supplierSupplierService.updateUnconfirmed(supplierSupplier, supplierProductForm.getSupplierProductList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }

}
