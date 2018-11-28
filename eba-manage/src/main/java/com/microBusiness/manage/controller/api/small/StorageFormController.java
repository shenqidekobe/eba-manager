package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageFormLog;
import com.microBusiness.manage.entity.StorageGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.SystemSetting;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.InventoryFormService;
import com.microBusiness.manage.service.NeedShopProductService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.StockGoodsService;
import com.microBusiness.manage.service.StorageFormLogService;
import com.microBusiness.manage.service.StorageFormService;
import com.microBusiness.manage.service.SupplierNeedProductService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.Code;

/**
 * 入库单Controller
 * 
 * @author wuzhanbo
 * @date 2018-3-5
 * @version 1.0
 */
@Controller("storageFormController")
@RequestMapping("/api/small/storageForm")
public class StorageFormController extends BaseController {

	@Resource
	private InventoryFormService inventoryFormService;
	
	@Resource
	private StorageFormService storageFormService;

	@Resource
	private ChildMemberService childMemberService;

	@Resource
	private ShopService shopService;
	
	@Resource
	private StockGoodsService stockGoodsService;
	
	@Resource
	private SupplierService supplierService;
	
	@Resource
	private StorageFormLogService storageFormLogService;
	
	@Resource
	private SupplyNeedService  supplyNeedService;
	
	@Resource
	private SupplierSupplierService supplierSupplierService;

	@Resource
	private ProductService productService;
	
	@Resource
	private ProductCategoryService productCategoryService;
	
	@Resource
	private SupplierNeedProductService supplierNeedProductService;
	
	@Resource
	private NeedShopProductService needShopProductService;
	
	@Resource
	private SupplierProductService supplierProductService;
	
	@Resource
	private HostingShopService hostingShopService;
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 获取入库单列表
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonEntity list(String unionId, String keyword, Long shopId, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		if (shopId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Page<StorageForm> page = storageFormService.findPage(pageable, keyword, shopService.find(shopId));

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StorageForm storageForm : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", storageForm.getId());
			map.put("storageCode", storageForm.getStorageCode());
			map.put("createDate", storageForm.getCreateDate());
			map.put("status", storageForm.getStatus());
			map.put("supplierName", storageForm.getSupplier().getName());
			if (storageForm.getOrder() != null) {
				map.put("sn", storageForm.getOrder().getSn());
			}else {
				map.put("sn", "");
			}
			
			
			lis.add(map);
		}

		resultMap.put("list", lis);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());

		return JsonEntity.successMessage(resultMap);
	}

	/**
	 * 新建入库单	
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JsonEntity save(String unionId, StorageForm storageForm, StorageGoodsForm storageGoodsForm, Long shopId, Long supplierId,Long orderId , Long supplyNeedId, Long supplierSupplierId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Shop shop = shopService.find(shopId);
		if (shop == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (storageForm.getStatus() == SfIfStatus.completed) {
			if (inventoryFormService.exists(shop, SfIfStatus.inInventory)) {
				return new JsonEntity(Code.code18802, Code.code18802.getDesc());
			}
		}
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		storageForm.setOrder(orderService.find(orderId));
		storageForm.setMember(member);
		storageForm.setSupplier(supplierService.find(supplierId));
		storageForm.setShop(shopService.find(shopId));
		storageForm.setSupplyNeed(supplyNeedService.find(supplyNeedId));
		storageForm.setSupplierSupplier(supplierSupplierService.find(supplierSupplierId));
//		try {
//			storageForm.setStorageDate(sdf.parse(storageDateStr));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		storageFormService.save(storageForm, storageGoodsForm.getStorageGoodsList());
		
		return JsonEntity.successMessage();
	}

	public static class StorageGoodsForm {

        private List<StorageGoods> storageGoodsList;

		public List<StorageGoods> getStorageGoodsList() {
			return storageGoodsList;
		}

		public void setStorageGoodsList(List<StorageGoods> storageGoodsList) {
			this.storageGoodsList = storageGoodsList;
		}

    }
	
	/**
	 * 查看入库单详情
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public JsonEntity view(String unionId, Long storageFormId, HttpServletRequest request, HttpServletResponse response) {
		if (storageFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StorageForm storageForm = storageFormService.find(storageFormId);
		Map<String, Object> formMap = new HashMap<>();
		formMap.put("id", storageForm.getId());
		formMap.put("storageCode", storageForm.getStorageCode());
		formMap.put("createDate", storageForm.getStorageDate());
		formMap.put("remarks", storageForm.getRemarks());
		formMap.put("status", storageForm.getStatus());
		formMap.put("supplier", storageForm.getSupplier().getId());
		if (storageForm.getSupplyNeed() != null) {
			formMap.put("relationId", storageForm.getSupplyNeed().getId());
		}
		if (storageForm.getSupplierSupplier() != null) {
			formMap.put("relationId", storageForm.getSupplierSupplier().getId());
		}
		formMap.put("supplierType", storageForm.getSupplierType());
		formMap.put("supplyType", storageForm.getSupplyType());
		formMap.put("supplierName", storageForm.getSupplier().getName());
		
		// 入库单单商品
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StorageGoods storageGoods : storageForm.getStorageGoods()) {
			Map<String, Object> goodsMap = new HashMap<>();
			goodsMap.put("productId", storageGoods.getProduct().getId());
			goodsMap.put("actualStock", storageGoods.getActualStock());
			goodsMap.put("specifications", storageGoods.getProduct().getSpecifications());
			goodsMap.put("name", storageGoods.getProduct().getGoods().getName());
			goodsMap.put("image", storageGoods.getProduct().getGoods().getImage());
			
			lis.add(goodsMap);
		}
		
		resultMap.put("storageForm", formMap);
		resultMap.put("goodsList", lis);
		
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 查看入库单操作记录
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/viewLog", method = RequestMethod.GET)
	public JsonEntity viewLog(String unionId, Long storageFormId, Long shopId, HttpServletRequest request, HttpServletResponse response) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		if (storageFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Shop shop = shopService.find(shopId);
		
		List<StorageFormLog> storageFormLogs = storageFormLogService.query(shop, storageFormService.find(storageFormId));
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StorageFormLog storageFormLog : storageFormLogs) {
			Map<String, Object> map = new HashMap<>();
			
			if (shop.getMember().equals(storageFormLog.getMember())) {
				map.put("operator", "超级管理员");
			}else {
				HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,storageFormLog.getMember());
				map.put("operator", hostingShop.getMemberMember().getName());
				
			}
			map.put("createDate", storageFormLog.getCreateDate());
			map.put("record", storageFormLog.getRecord());
			map.put("id", storageFormLog.getId());
			
//			List<Map<String, Object>> goods = new ArrayList<Map<String, Object>>();
//			for (StorageFormLog.Entry entry : storageFormLog.getEntries()) {
//				Map<String,Object> mapGoods = new HashMap<>();
//				mapGoods.put("name", entry.getName());
//				mapGoods.put("url", entry.getUrl());
//				mapGoods.put("product", entry.getProduct());
//				mapGoods.put("number", entry.getNumber());
//				
//				goods.add(mapGoods);
//			}
			
			lis.add(map);
		}
		
		return JsonEntity.successMessage(lis);
	}
	
	/**
	 * 查看入库单操作记录
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/viewLogByOrder", method = RequestMethod.GET)
	public JsonEntity viewLogByOrder(String unionId,Long shopId, Long orderId, HttpServletRequest request, HttpServletResponse response) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		if (orderId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Shop shop = shopService.find(shopId);
		
		List<StorageFormLog> storageFormLogs = storageFormLogService.query(shop, orderService.find(orderId), StorageFormLog.Record.create);
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StorageFormLog storageFormLog : storageFormLogs) {
			Map<String, Object> map = new HashMap<>();
			
			if (shop.getMember().equals(storageFormLog.getMember())) {
				map.put("operator", "超级管理员");
			}else {
				HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,storageFormLog.getMember());
				map.put("operator", hostingShop.getMemberMember().getName());
			}
			map.put("createDate", storageFormLog.getCreateDate());
			map.put("record", storageFormLog.getRecord());
			map.put("id", storageFormLog.getId());
			
			List<Map<String, Object>> goods = new ArrayList<Map<String, Object>>();
			for (StorageFormLog.Entry entry : storageFormLog.getEntries()) {
				Map<String,Object> mapGoods = new HashMap<>();
				mapGoods.put("name", entry.getName());
				mapGoods.put("url", entry.getUrl());
				mapGoods.put("product", entry.getProduct());
				mapGoods.put("number", entry.getNumber());
				
				goods.add(mapGoods);
			}
			
			map.put("goodsLog", goods);
			
			lis.add(map);
		}
		
		return JsonEntity.successMessage(lis);
	}
	
	/**
	 * 修改入库单
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonEntity edit(String unionId, StorageForm storageForm, StorageGoodsForm storageGoodsForm, Long shopId, Long supplierId, Long supplyNeedId, Long supplierSupplierId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Shop shop = shopService.find(shopId);
		if (shop == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (storageForm.getStatus() == SfIfStatus.completed) {
			if (inventoryFormService.exists(shop, SfIfStatus.inInventory)) {
				return new JsonEntity(Code.code18802, Code.code18802.getDesc());
			}
		}
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		storageForm.setMember(member);
		storageForm.setSupplier(supplierService.find(supplierId));
		storageForm.setShop(shopService.find(shopId));
		storageForm.setSupplyNeed(supplyNeedService.find(supplyNeedId));
		storageForm.setSupplierSupplier(supplierSupplierService.find(supplierSupplierId));
//		try {
//			storageForm.setStorageDate(sdf.parse(storageDateStr));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		storageFormService.update(storageForm, storageGoodsForm.getStorageGoodsList());
		
		return JsonEntity.successMessage();
	}

	/**
	 * 取消入库单
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public JsonEntity cancel(String unionId, Long storageFormId, Long shopId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (storageFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		storageFormService.cancel(storageFormService.find(storageFormId), member);
		
		return JsonEntity.successMessage();
	}

	/**
	 * 下单时获取已分配的供应商列表
	 * @param shopId   店铺id
	 * @param types		平台订单 or  本地订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAssignSupplier" , method = RequestMethod.GET)
	public JsonEntity getAssignSupplier(Long shopId){
		List<Map<String,Object>> resultList=new ArrayList<>();
		Shop shop=shopService.find(shopId);
		//直营店
		if (ShopType.direct.equals(shop.getShopType())){
			//供应类型1
			Need need=shop.getNeeds().iterator().next();
			Supplier supplier=shop.getMember().getAdmin().getSupplier();
			SupplyNeed supplyNeed=new SupplyNeed();
			supplyNeed.setNeed(need);
			supplyNeed.setSupplier(supplier);
			supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);
			if (supplyNeed != null && supplyNeed.getNeedProducts().size()>0){
				Map<String,Object> map=new HashMap<>();
				map.put("id", supplier.getId());
				map.put("name", supplier.getName());
				map.put("supplierType", SupplierType.ONE);
				map.put("supplyType", SupplyType.temporary);
				map.put("relationId", supplyNeed.getId());
				resultList.add(map);
			}
			//供应类型2
			List<SupplierSupplier.Status> statuses=new ArrayList<>();
			statuses.add(SupplierSupplier.Status.inTheSupply);
			List<SupplierSupplier> list=supplierSupplierService.getListBySupplier(supplier,statuses);
			for (SupplierSupplier supplierSupplier : list) {
				if (supplierNeedProductService.findBySupplier(supplierSupplier,need).size() > 0){
					Map<String,Object> map=new HashMap<>();
					map.put("id", supplierSupplier.getSupplier().getId());
					map.put("name", supplierSupplier.getSupplier().getName());
					map.put("supplierType", SupplierType.TWO);
					map.put("supplyType", SupplyType.formal);
					map.put("relationId", supplierSupplier.getId());
					resultList.add(map);
				}
			}
		}else if (ShopType.affiliate.equals(shop.getShopType())){
			//供应类型3
			Set<Need> needs=shop.getNeeds();
			List<SupplyNeed> list=supplyNeedService.findByNeeds(new ArrayList<Need>(needs));
			for (SupplyNeed supplyNeed : list ) {
				if (needShopProductService.getList(supplyNeed,shop,null).size()>0){
					Map<String,Object> map=new HashMap<>();
					map.put("id", supplyNeed.getSupplier().getId());
					map.put("name", supplyNeed.getSupplier().getName());
					map.put("supplierType", SupplierType.THREE);
					map.put("supplyType", SupplyType.temporary);
					map.put("relationId", supplyNeed.getId());
					resultList.add(map);
				}
			}
		}

		//供应类型4
		List<Supplier> list=supplierService.getSupplierListByMember(shop.getMember());
		for (Supplier supplier : list ) {
			if (needShopProductService.getList(null,shop,supplier).size()>0){
				Map<String,Object> map=new HashMap<>();
				map.put("id", supplier.getId());
				map.put("name", supplier.getName());
				map.put("supplierType", SupplierType.FOUR);
				map.put("supplyType", SupplyType.temporary);
				resultList.add(map);
			}
		}

		return JsonEntity.successMessage(resultList);
	}

	/**
	 * 获取已分配的商品信息、供应信息
	 * @param supplierId					供应商id
	 * @param shopId				店铺id
	 * @param supplierType			供应类型
	 * @param relationId			供应id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAssignProducts" , method = RequestMethod.GET)
	public JsonEntity getAssignProducts(Long supplierId,Long shopId,SupplierType supplierType,Long relationId, String goodsName, Long productCategoryId, Pageable pageable){
		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,Object>> resultList = new ArrayList<>();
		Shop shop=shopService.find(shopId);
		Supplier supplier=supplierService.find(supplierId);//供应商
		if (SupplierType.ONE.equals(supplierType)){
			//供应类型1  本企业
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			SupplyNeed supplyNeed=new SupplyNeed();
			supplyNeed.setSupplier(supplier);
			supplyNeed.setNeed(need);
			supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);
			if (supplyNeed != null){
//				List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null, goodsName, productCategoryService.find(productCategoryId));
				Page<NeedShopProduct> page=needShopProductService. getPage(supplyNeed,shop,null, goodsName, productCategoryId, pageable);
				for(NeedShopProduct needShopProduct:page.getContent()){
					Product product=needShopProduct.getProducts();
					Map<String,Object> map=new HashMap<>();
					map.put("productId",product.getId());
					map.put("specifications",product.getSpecifications());
					map.put("name",product.getGoods().getName());
					map.put("image",product.getGoods().getImage());
					map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
					map.put("supplyPrice",needShopProduct.getSupplyPrice());
					resultList.add(map);
				}
				resultMap.put("relationId",supplyNeed.getId());
				resultMap.put("assignedModel",supplyNeed.getAssignedModel());
				resultMap.put("exits",true);
			}else {
				resultMap.put("exits",false);
			}
			SystemSetting systemSetting=supplier.getSystemSetting();
			resultMap.put("isDistributionModel",systemSetting.getIsDistributionModel());
			resultMap.put("list",resultList);
		}else if (SupplierType.TWO.equals(supplierType)){
			//供应类型2 企业-->企业-->门店 只能分配给直营店
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
//			List<SupplierNeedProduct> list=supplierNeedProductService.findBySupplier(supplierSupplier,need, goodsName, productCategoryService.find(productCategoryId));
			Page<SupplierNeedProduct> page=supplierNeedProductService.findBySupplierPage(supplierSupplier,need, goodsName, productCategoryId, pageable);
			for (SupplierNeedProduct supplierNeedProduct: page.getContent()) {
				Product product=supplierNeedProduct.getProducts();
				SupplierProduct supplierProduct=supplierProductService.getSupplierProduct(supplierSupplier,product);
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("specifications",product.getSpecifications());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("minOrderQuantity",supplierProduct.getMinOrderQuantity());
				map.put("supplyPrice",supplierProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("pageNumber", page.getPageNumber());
			resultMap.put("totalPages", page.getTotalPages());
			resultMap.put("relationId",relationId);
			resultMap.put("list",resultList);
		}else if (SupplierType.THREE.equals(supplierType)){
			//供应类型3  企业-->门店  加盟店
			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
//			List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null, goodsName, productCategoryService.find(productCategoryId));
			Page<NeedShopProduct> page=needShopProductService.getPage(supplyNeed,shop,null, goodsName, productCategoryId, pageable);
			for (NeedShopProduct needShopProduct : page.getContent()) {
				Product product=needShopProduct.getProducts();
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("specifications",product.getSpecifications());
				map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
				map.put("supplyPrice",needShopProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("pageNumber", page.getPageNumber());
			resultMap.put("totalPages", page.getTotalPages());
			resultMap.put("assignedModel",supplyNeed.getAssignedModel());
			resultMap.put("relationId",relationId);
			resultMap.put("list",resultList);
		}else if (SupplierType.FOUR.equals(supplierType)){
			//供应类型4   本地供应
//			List<NeedShopProduct> list=needShopProductService.getList(null,shop,supplier, goodsName, productCategoryService.find(productCategoryId));
			Page<NeedShopProduct> page=needShopProductService.getPage(null,shop,supplier, goodsName, productCategoryId, pageable);
			for(NeedShopProduct needShopProduct:page.getContent()){
				Map<String,Object> map=new HashMap<>();
				Product product=needShopProduct.getProducts();
				map.put("productId",product.getId());
				map.put("specifications",product.getSpecifications());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
				map.put("supplyPrice",needShopProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("pageNumber", page.getPageNumber());
			resultMap.put("totalPages", page.getTotalPages());
			resultMap.put("list",resultList);
		}
		return  JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 获取已分配的商品信息、供应信息根据条形码
	 * @param supplierId					供应商id
	 * @param shopId				店铺id
	 * @param supplierType			供应类型
	 * @param relationId			供应id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAssignProductsByBarCode" , method = RequestMethod.GET)
	public JsonEntity getAssignProductsByBarCode(Long supplierId,Long shopId,SupplierType supplierType,Long relationId, String goodsName, Long productCategoryId, String barCode){
		Map<String,Object> resultMap=new HashMap<>();
		List<Map<String,Object>> resultList = new ArrayList<>();
		Shop shop=shopService.find(shopId);
		Supplier supplier=supplierService.find(supplierId);//供应商
		if (SupplierType.ONE.equals(supplierType)){
			//供应类型1  本企业
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			SupplyNeed supplyNeed=new SupplyNeed();
			supplyNeed.setSupplier(supplier);
			supplyNeed.setNeed(need);
			supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);
			if (supplyNeed != null){
//				List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null, goodsName, productCategoryService.find(productCategoryId));
//				Page<NeedShopProduct> page=needShopProductService. getPage(supplyNeed,shop,null, goodsName, productCategoryId, pageable);
				List<NeedShopProduct> list = needShopProductService.getByBarCode(supplyNeed, shop, null, barCode);
				for(NeedShopProduct needShopProduct:list){
					Product product=needShopProduct.getProducts();
					Map<String,Object> map=new HashMap<>();
					map.put("productId",product.getId());
					map.put("specifications",product.getSpecifications());
					map.put("name",product.getGoods().getName());
					map.put("image",product.getGoods().getImage());
					map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
					map.put("supplyPrice",needShopProduct.getSupplyPrice());
					resultList.add(map);
				}
				resultMap.put("relationId",supplyNeed.getId());
				resultMap.put("assignedModel",supplyNeed.getAssignedModel());
				resultMap.put("exits",true);
			}else {
				resultMap.put("exits",false);
			}
			SystemSetting systemSetting=supplier.getSystemSetting();
			resultMap.put("isDistributionModel",systemSetting.getIsDistributionModel());
			resultMap.put("list",resultList);
		}else if (SupplierType.TWO.equals(supplierType)){
			//供应类型2 企业-->企业-->门店 只能分配给直营店
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
//			List<SupplierNeedProduct> list=supplierNeedProductService.findBySupplier(supplierSupplier,need, goodsName, productCategoryService.find(productCategoryId));
//			Page<SupplierNeedProduct> page=supplierNeedProductService.findBySupplierPage(supplierSupplier,need, goodsName, productCategoryId, pageable);
			
			List<SupplierNeedProduct> list = supplierNeedProductService.findBySupplierBarCode(supplierSupplier, need, barCode);
			
			for (SupplierNeedProduct supplierNeedProduct: list) {
				Product product=supplierNeedProduct.getProducts();
				SupplierProduct supplierProduct=supplierProductService.getSupplierProduct(supplierSupplier,product);
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("specifications",product.getSpecifications());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("minOrderQuantity",supplierProduct.getMinOrderQuantity());	
				map.put("supplyPrice",supplierProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("relationId",relationId);
			resultMap.put("list",resultList);
		}else if (SupplierType.THREE.equals(supplierType)){
			//供应类型3  企业-->门店  加盟店
			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
//			List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null, goodsName, productCategoryService.find(productCategoryId));
//			Page<NeedShopProduct> page=needShopProductService.getPage(supplyNeed,shop,null, goodsName, productCategoryId, pageable);
			
			List<NeedShopProduct> list=needShopProductService.getByBarCode(supplyNeed, shop, null, barCode);
			for (NeedShopProduct needShopProduct : list) {
				Product product=needShopProduct.getProducts();
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("specifications",product.getSpecifications());
				map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
				map.put("supplyPrice",needShopProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("assignedModel",supplyNeed.getAssignedModel());
			resultMap.put("relationId",relationId);
			resultMap.put("list",resultList);
		}else if (SupplierType.FOUR.equals(supplierType)){
			//供应类型4   本地供应
//			List<NeedShopProduct> list=needShopProductService.getList(null,shop,supplier, goodsName, productCategoryService.find(productCategoryId));
//			Page<NeedShopProduct> page=needShopProductService.getPage(null,shop,supplier, goodsName, productCategoryId, pageable);
			
			List<NeedShopProduct> list=needShopProductService.getByBarCode(null, shop, supplier, barCode);
			for(NeedShopProduct needShopProduct:list){
				Map<String,Object> map=new HashMap<>();
				Product product=needShopProduct.getProducts();
				map.put("productId",product.getId());
				map.put("specifications",product.getSpecifications());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
				map.put("supplyPrice",needShopProduct.getSupplyPrice());
				resultList.add(map);
			}
			resultMap.put("list",resultList);
		}
		return  JsonEntity.successMessage(resultMap);
	}

	@ResponseBody
	@RequestMapping(value = "/goodsFlag", method = RequestMethod.GET)
	public JsonEntity goodsFlag(String unionId, Long shopId, Long productId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (productId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Map<Long, Shop> shopMap = new HashMap<>();
		
		List<NeedShopProduct> needShopProducts = needShopProductService.getProduct(shopService.find(shopId), productService.find(productId), member);
		
		List<SupplierNeedProduct> supplierNeedProducts = supplierNeedProductService.getProduct(productService.find(productId), member);
		
		List<Map<String, Object>> list=new ArrayList<>();
		if (needShopProducts.size() > 0 || supplierNeedProducts.size() > 0) {
			for (NeedShopProduct needShopProduct : needShopProducts) {
				shopMap.put(needShopProduct.getShop().getId(), needShopProduct.getShop());
			}
			
			// 	根据need拿shop?
			for (SupplierNeedProduct supplierNeedProduct : supplierNeedProducts) {
				Shop shop = supplierNeedProduct.getNeed().getShops().get(0);
				
				if (shopId != null && shop.getId() == shop.getId()) {
					shopMap.put(shop.getId(), shop);
				}
				if (shopId == null) {
					shopMap.put(shop.getId(), shop);
				}
				
			}
		}
		
		List<HostingShop> hostingShops = hostingShopService.findListByMember(member);
		if (hostingShops.size() > 0) {
			for (HostingShop hostingShop : hostingShops) {
				List<NeedShopProduct> needShopProductsMember = needShopProductService.getProduct(hostingShop.getShop(), productService.find(productId), null);
				List<SupplierNeedProduct> supplierNeedProductsMember = supplierNeedProductService.getProduct(productService.find(productId), hostingShop.getMember());
				if (needShopProductsMember.size() > 0 || supplierNeedProductsMember.size() > 0) {
					for (NeedShopProduct needShopProduct : needShopProductsMember) {
						shopMap.put(needShopProduct.getShop().getId(), needShopProduct.getShop());
					}
					
					for (SupplierNeedProduct supplierNeedProduct : supplierNeedProductsMember) {
						Shop shop = supplierNeedProduct.getNeed().getShops().get(0);
						
						if (shop == hostingShop.getShop()) {
							shopMap.put(shop.getId(), shop);
						}
						
//						if (shopId != null && shop.getId() == shop.getId()) {
//							shopMap.put(shop.getId(), shop);
//						}
//						if (shopId == null) {
//							shopMap.put(shop.getId(), shop);
//						}
						
					}
					
				}
			}
		}
		
		for (Long id : shopMap.keySet()) {
			
			Shop shop = shopMap.get(id);
			Map<String, Object> map=new HashMap<>();
			map.put("id", shop.getId());
			map.put("name", shop.getName());
			map.put("shopType", shop.getShopType());
			map.put("userName", shop.getUserName());
			if (shop.getMember().equals(member)) {
				map.put("myself", true);
			}else {
				map.put("myself", false);
			}
			list.add(map);
		}
		
		return JsonEntity.successMessage(list);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/goodsFlagShop", method = RequestMethod.GET)
	public JsonEntity goodsFlagShop(String unionId, Long shopId, Long productId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (productId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Map<Long, Shop> shopMap = new HashMap<>();
		
		List<NeedShopProduct> needShopProducts = needShopProductService.getProduct(shopService.find(shopId), productService.find(productId), null);
		
		List<SupplierNeedProduct> supplierNeedProducts = supplierNeedProductService.getProduct(productService.find(productId), null);
		
		List<Map<String, Object>> list=new ArrayList<>();
		if (needShopProducts.size() > 0 || supplierNeedProducts.size() > 0) {
			for (NeedShopProduct needShopProduct : needShopProducts) {
				shopMap.put(needShopProduct.getShop().getId(), needShopProduct.getShop());
			}
			
			// 	根据need拿shop?
			for (SupplierNeedProduct supplierNeedProduct : supplierNeedProducts) {
				Shop shop = supplierNeedProduct.getNeed().getShops().get(0);
				
				if (shop.getId() == shop.getId()) {
					shopMap.put(shop.getId(), shop);
				}
				
			}
		}
		
		for (Long id : shopMap.keySet()) {
			
			Shop shop = shopMap.get(id);
			Map<String, Object> map=new HashMap<>();
			map.put("id", shop.getId());
			map.put("name", shop.getName());
			map.put("shopType", shop.getShopType());
			map.put("userName", shop.getUserName());
			if (shop.getMember().equals(member)) {
				map.put("myself", true);
			}else {
				map.put("myself", false);
			}
			list.add(map);
		}
		
		return JsonEntity.successMessage(list);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/getProduct", method = RequestMethod.GET)
	public JsonEntity getProduct(Long productId, String unionId , HttpServletRequest request, HttpServletResponse response) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		
		if (productId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Product product = productService.find(productId);
		
		Map<String,Object> map=new HashMap<>();
		map.put("productId",product.getId());
		map.put("specifications",product.getSpecifications());
		map.put("name",product.getGoods().getName());
		map.put("image",product.getGoods().getImage());
		
		return JsonEntity.successMessage(map);
		
	}
	
}
