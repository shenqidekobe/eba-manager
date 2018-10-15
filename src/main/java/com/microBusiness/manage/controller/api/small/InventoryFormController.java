package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.InventoryGoods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.InventoryFormLogService;
import com.microBusiness.manage.service.InventoryFormService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.StockGoodsService;
import com.microBusiness.manage.service.StorageFormService;
import com.microBusiness.manage.util.Code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 盘点单Controller
 * 
 * @author Administrator
 * @date 2018-3-5
 * @version 1.0
 */
@Controller("inventoryFormController")
@RequestMapping("/api/small/inventoryForm")
public class InventoryFormController extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(InventoryFormController.class);

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
	private InventoryFormLogService inventoryFormLogService;
	
	@Resource
	private HostingShopService hostingShopService;
	
	@Resource
	private ProductService productService;
	
	/**
	 * 获取盘点单列表
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonEntity list(String unionId, String inventoryCode, Long shopId, SfIfStatus status, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();

		if (shopId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}

		Page<InventoryForm> page = inventoryFormService.findPage(pageable, inventoryCode, shopService.find(shopId), status);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (InventoryForm inventoryForm : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", inventoryForm.getId());
			map.put("inventoryCode", inventoryForm.getInventoryCode());
			map.put("createDate", inventoryForm.getCreateDate());
			map.put("status", inventoryForm.getStatus());

			lis.add(map);
		}

		resultMap.put("list", lis);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());

		return JsonEntity.successMessage(resultMap);
	}

	/**
	 * 新建盘点单
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JsonEntity save(String unionId, InventoryForm inventoryForm, InventoryGoodsForm inventoryGoodsForm, Long shopId, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Shop shop = shopService.find(shopId);
		if (shop == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (inventoryFormService.exists(shop, SfIfStatus.inInventory)) {
			return new JsonEntity(Code.code18801, Code.code18801.getDesc());
		}
		
		inventoryForm.setShop(shop);
		inventoryForm.setMember(member);
		
		boolean flag = inventoryFormService.save(inventoryForm, inventoryGoodsForm.getInventoryGoodsList());
		if (flag) {
			return JsonEntity.successMessage();
		} else {
			return new JsonEntity(Code.code18803, Code.code18803.getDesc());
		}
	}

	public static class InventoryGoodsForm {

        private List<InventoryGoods> InventoryGoodsList;

		public List<InventoryGoods> getInventoryGoodsList() {
			return InventoryGoodsList;
		}

		public void setInventoryGoodsList(List<InventoryGoods> inventoryGoodsList) {
			InventoryGoodsList = inventoryGoodsList;
		}

    }
	
	/**
	 *	查看盘点单详情
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public JsonEntity view(String unionId, Long InventoryFormId, HttpServletRequest request, HttpServletResponse response) {
		if (InventoryFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		InventoryForm inventoryForm = inventoryFormService.find(InventoryFormId);
		Map<String, Object> formMap = new HashMap<>();
		formMap.put("id", inventoryForm.getId());
		formMap.put("shop", inventoryForm.getShop().getName());
		formMap.put("inventoryCode", inventoryForm.getInventoryCode());
		formMap.put("createDate", inventoryForm.getCreateDate());
		formMap.put("remarks", inventoryForm.getRemarks());
		formMap.put("status", inventoryForm.getStatus());
		
		// 盘点单商品
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (InventoryGoods inventoryGoods : inventoryForm.getInventoryGoods()) {
			Map<String, Object> goodsMap = new HashMap<>();
			goodsMap.put("id", inventoryGoods.getProduct().getId());
			goodsMap.put("actualStock", inventoryGoods.getActualStock());
			goodsMap.put("inventoryStock", inventoryGoods.getInventoryStock());
			goodsMap.put("product", inventoryGoods.getProduct().getSpecifications());
			goodsMap.put("name", inventoryGoods.getProduct().getGoods().getName());
			goodsMap.put("image", inventoryGoods.getProduct().getGoods().getImage());
			
			lis.add(goodsMap);
		}
		
		resultMap.put("inventoryForm", formMap);
		resultMap.put("goodsList", lis);
		
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 查看盘点单操作记录
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/viewLog", method = RequestMethod.GET)
	public JsonEntity viewLog(String unionId, Long InventoryFormId, Long shopId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (InventoryFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		Shop shop = shopService.find(shopId);
		
		List<InventoryFormLog> inventoryFormLogs = inventoryFormLogService.query(shop, inventoryFormService.find(InventoryFormId));
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (InventoryFormLog inventoryFormLog : inventoryFormLogs) {
			Map<String, Object> map = new HashMap<>();
			if (shop.getMember().equals(inventoryFormLog.getMember())) {
				map.put("operator", "超级管理员");
			}else {
				HostingShop hostingShop=hostingShopService.findByShopAndByMember(shop,inventoryFormLog.getMember());
				map.put("operator", hostingShop.getMemberMember().getName());
				
			}
			map.put("createDate", inventoryFormLog.getCreateDate());
			map.put("record", inventoryFormLog.getRecord());
			
			lis.add(map);
		}
		
		return JsonEntity.successMessage(lis);
	}
	
	/**
	 * 修改盘点单
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonEntity edit(String unionId, InventoryForm inventoryForm, InventoryGoodsForm inventoryGoodsForm, Long shopId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Shop shop = shopService.find(shopId);
		if (shop == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
//		if (inventoryForm.getStatus() == SfIfStatus.completed) {
//			if (inventoryFormService.exists(shop, SfIfStatus.inInventory)) {
//				return new JsonEntity(Code.code18801, Code.code18801.getDesc());
//			}
//		}
		
		inventoryForm.setMember(member);
		inventoryForm.setShop(shopService.find(shopId));
		
		inventoryFormService.update(inventoryForm, inventoryGoodsForm.getInventoryGoodsList());
		return JsonEntity.successMessage();
	}

	/**
	 * 取消盘点单
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public JsonEntity cancel(String unionId, Long inventoryFormId, Long shopId, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (inventoryFormId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		inventoryFormService.cancel(inventoryFormService.find(inventoryFormId), member);
		
		return JsonEntity.successMessage();
	}

	/**
	 * 盘点单选择商品，查询库存状况
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/stockGoodsList", method = RequestMethod.GET)
	public JsonEntity stockGoodsList(String unionId, Long shopId, String keyword, StockGoods.Status status, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Page<StockGoods> page = stockGoodsService.page(shopService.find(shopId), keyword, status, pageable);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StockGoods stockGoods : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", stockGoods.getProduct().getId());
			map.put("name", stockGoods.getProduct().getGoods().getName());
			map.put("actualStock", stockGoods.getActualStock());
			map.put("product", stockGoods.getProduct().getSpecifications());
			map.put("image", stockGoods.getProduct().getGoods().getImage());
			
			lis.add(map);
		}
		
		resultMap.put("list", lis);
		resultMap.put("totalPages", page.getTotalPages());

		return JsonEntity.successMessage(resultMap);
	}

	/**
	 * 条形码，查询库存状况
	 * 
	 * @param request
	 * @returns
	 */
	@ResponseBody
	@RequestMapping(value = "/getStockGoods", method = RequestMethod.GET)
	public JsonEntity getStockGoods(String unionId, Long shopId, String barCode, StockGoods.Status status, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		List<StockGoods> list = stockGoodsService.findByBarCode(barCode, shopService.find(shopId), status);
		List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
		for (StockGoods stockGoods : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", stockGoods.getProduct().getId());
			map.put("name", stockGoods.getProduct().getGoods().getName());
			map.put("actualStock", stockGoods.getActualStock());
			map.put("product", stockGoods.getProduct().getSpecifications());
			map.put("image", stockGoods.getProduct().getGoods().getImage());
			
			lis.add(map);
		}
		
		return JsonEntity.successMessage(lis);
	}
	
	@ResponseBody
	@RequestMapping(value = "/byProduct", method = RequestMethod.GET)
	public JsonEntity byProduct(String unionId, Long shopId, Long productId, HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		StockGoods stockGoods = stockGoodsService.findByProduct(productService.find(productId), shopService.find(shopId));
		
		Map map = new HashMap<>();
		
		if (stockGoods != null) {
			map.put("image", stockGoods.getProduct().getGoods().getImage());
			map.put("product", stockGoods.getProduct().getSpecifications());
			map.put("name", stockGoods.getProduct().getGoods().getName());
			map.put("id", stockGoods.getProduct().getId());
			map.put("actualStock", stockGoods.getActualStock());
			
		}
		
		return JsonEntity.successMessage(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getShop", method = RequestMethod.GET)
	public JsonEntity getShop(Long productId, String unionId , Long shopId , HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		if (productId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		List<Map<String, Object>> list=new ArrayList<>();
		
		List<StockGoods> stockGoods = stockGoodsService.getStockGoodsByMember(productService.find(productId), member, shopService.find(shopId));
		
		for (StockGoods stockGoods2 : stockGoods) {
			Shop shop = stockGoods2.getShop();
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
		
		List<HostingShop> hostingShops = hostingShopService.findListByMember(member);
		if (hostingShops.size() > 0) {
			for (HostingShop hostingShop : hostingShops) {
				List<StockGoods> stockGoodsNew = stockGoodsService.getStockGoodsByMember(productService.find(productId), hostingShop.getMember(), hostingShop.getShop());
				for (StockGoods stockGoods2 : stockGoodsNew) {
					Shop shop = stockGoods2.getShop();
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
			}
		}
		
		return JsonEntity.successMessage(list);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/getShopL", method = RequestMethod.GET)
	public JsonEntity getShopL(Long productId, String unionId , Long shopId , HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		if (productId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		List<Map<String, Object>> list=new ArrayList<>();
		
		List<StockGoods> stockGoods = stockGoodsService.getStockGoodsByMember(productService.find(productId), null, shopService.find(shopId));
		
		for (StockGoods stockGoods2 : stockGoods) {
			Shop shop = stockGoods2.getShop();
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
}
