package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.dto.ProductSupplierDto;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.GoodsCenterService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductCenterService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.Code;

@Controller
@RequestMapping("/api/small/barCodeByGoods")
public class BarCodeByGoodsController extends BaseController {

	@Resource
	private ChildMemberService childMemberService;
	@Resource
	private ProductCenterService productCenterService;
	@Resource
	private ProductService productService;
	@Resource
	private ShopService shopService ;
	@Resource
	private SupplierService supplierService;
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
	private GoodsCenterService goodsCenterService;
	@Resource
    private ProductCategoryService productCategoryService ;
	@Resource
    private CategoryCenterService categoryCenterService;
	@Resource
	private GoodsService goodsService;
	
	/**
	 * 最外面扫码
	 * @param barCode
	 * @param goodsName
	 * @param unionId
	 * @param shopId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public JsonEntity list(String barCode, String goodsName, String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(null == barCode) {
			return new JsonEntity(Code.code13012, Code.code13012.getDesc());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		//先查本地商品，如果不存在再去查仓库商品
		List<ProductSupplierDto> productSupplierDtos = productService.findByProductAndSupplier(member, barCode, goodsName, Types.local, null);
		if(productSupplierDtos.size() > 0) {
			//遍历本地商品
			for(final ProductSupplierDto dto : productSupplierDtos) {
				productList.add(new HashMap<String, Object>(){{
					Product product = dto.getProduct();
					Supplier supplier = dto.getSupplier();
					this.put("productId", product == null ? null : product.getId());
					this.put("goodsName", dto.getGoodsName());
					this.put("specificationValues", product == null ? null : product.getSpecificationValues());
					this.put("supplierId", supplier == null ? null : supplier.getId());
					this.put("supplierName", supplier == null ? null : supplier.getName());
					this.put("type", "local");
				}});
			}
		}else {
			List<ProductCenter> list = productCenterService.findList(null, goodsName, barCode);
			//遍历仓库商品
			for(final ProductCenter center : list) {
				productList.add(new HashMap<String, Object>(){{
					this.put("productId", center.getId());
					this.put("goodsName", center.getGoodsCenter() == null ? null : center.getGoodsCenter().getName());
					this.put("specificationValues", center.getSpecificationValues());
					this.put("barCode", center.getBarCode());
					this.put("type", "center");//仓库商品类型
				}});
				
			}
		}
		resultMap.put("productList", productList);
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: queryGoodsDetails
	 * @author: yuezhiwei
	 * @date: 2018年4月13日下午1:50:27
	 * @Description: 查询商品详情
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/queryGoodsDetails" , method = RequestMethod.GET)
	public JsonEntity queryGoodsDetails(String unionId, Long productId , Long supplierId , String type) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(null == type || null == productId) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(type.equals("center")) {
			ProductCenter productCenter = productCenterService.find(productId);
			GoodsCenter goodsCenter = productCenter.getGoodsCenter();
			resultMap.put("goodsName", productCenter.getName());
			resultMap.put("image", goodsCenter.getImage());
			resultMap.put("images", goodsCenter.getImages());
			resultMap.put("barCode", productCenter.getBarCode());
			resultMap.put("unit", goodsCenter == null ? null : goodsCenter.getUnit());
			resultMap.put("labels", goodsCenter == null ? null : goodsCenter.getLabels());
			resultMap.put("introduction", goodsCenter == null ? null : goodsCenter.getIntroduction());
			resultMap.put("specificationItems", goodsCenter == null ? null : goodsCenter.getSpecificationItems());
			resultMap.put("specification", productCenter.getSpecificationValues());
			resultMap.put("productId", productCenter.getId());
			resultMap.put("goodsId", goodsCenter == null ? null : goodsCenter.getId());
			resultMap.put("categoryId", goodsCenter == null ? null : goodsCenter.getCategoryCenter().getId());
			resultMap.put("categoryName", goodsCenter == null ? null : goodsCenter.getCategoryCenter().getName());
			resultMap.put("type", "center");
		}else {
			if(null == supplierId) {
				return new JsonEntity(Code.code13003, Code.code13003.getDesc());
			}
			Product product = productService.find(productId);
			Goods goods = product.getGoods();
			resultMap.put("goodsName", product.getName());
			resultMap.put("image", product.getImage());
			resultMap.put("barCode", product.getBarCode());
			resultMap.put("unit", goods == null ? null : goods.getUnit());
			resultMap.put("labels", goods == null ? null : goods.getLabels());
			resultMap.put("specification", product.getSpecificationValues());
			resultMap.put("supplierId", goods == null ? null : goods.getSupplier().getId());
			resultMap.put("supplierName", goods == null ? null : goods.getSupplier().getName());
			resultMap.put("introduction", goods == null ? null : goods.getIntroduction());
			resultMap.put("productId", product.getId());
			resultMap.put("type", "local");
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: shopList
	 * @author: yuezhiwei
	 * @date: 2018年4月19日下午3:50:02
	 * @Description: 获取未分配的门店列表
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/shopList" , method = RequestMethod.GET)
	public JsonEntity shopList(String unionId, Long productId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(productId == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Product product = productService.find(productId);
		List<Shop> shops = shopService.findList(member, product);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(final Shop shop : shops) {
			resultList.add(new HashMap<String, Object>(){{
				this.put("shopId", shop.getId());
				this.put("name", shop.getName());
				this.put("shopType", shop.getShopType());
			}});
		}
		return JsonEntity.successMessage(resultList);
	}
	
	/**
	 * 
	 * @Title: supplierList
	 * @author: yuezhiwei
	 * @date: 2018年4月19日下午3:54:42
	 * @Description: 获取未添加此商品的本地供应商
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/supplierList" , method = RequestMethod.GET)
	public JsonEntity supplierList(String unionId, Long productId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(productId == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		//本地仓库商品
		ProductCenter productCenter = productCenterService.find(productId);
		GoodsCenter goodsCenter = productCenter.getGoodsCenter();
		//查询不存在的本地供应商
		List<Supplier> suppliers=supplierService.getSupplierListByMember(member, productCenter, 
				goodsCenter == null ? null : goodsCenter.getName());
		for(final Supplier supplier : suppliers) {
			resultList.add(new HashMap<String, Object>(){{
				this.put("supplierId", supplier.getId());
				this.put("name", supplier.getName());
			}});
		}
		return JsonEntity.successMessage(resultList);
	}
	
	/**
	 * 
	 * @Title: batchAllocationGoods
	 * @author: yuezhiwei
	 * @date: 2018年4月17日下午2:10:03
	 * @Description: 批量分配商品
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/batchAllocationGoods" , method = RequestMethod.POST)
	public JsonEntity batchAllocationGoods(String unionId, Long productId, Long supplierId, IdsForm idsForm) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(unionId == null || supplierId == null || productId == null || idsForm.getIds() == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Product product = productService.find(productId);
		Supplier supplier = supplierService.find(supplierId);
		supplyNeedService.batchAllocationGoods(product, idsForm.getIds(), supplier);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: barCodeSave
	 * @author: yuezhiwei
	 * @date: 2018年4月20日下午1:48:54
	 * @Description: 批量添加商品到本地供应商
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/barCodeSave" , method = RequestMethod.POST)
	public JsonEntity barCodeSave(String unionId, Long productId, IdsForm idsForm) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(unionId == null || productId == null || idsForm.getIds() == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		ProductCenter productCenter = productCenterService.find(productId);
		GoodsCenter goodsCenter = productCenter.getGoodsCenter();
		Map<String, Object> map = this.flagBox(goodsCenter.getCategoryCenter().getId(), member);
		ProductCategory productCategory = null;
		boolean flag = (boolean) map.get("flag");
		if(flag) {
			Map<String, Object> map2 = this.copy(member, goodsCenter.getCategoryCenter().getId(), flag);
			Long categoryId = (Long) map2.get("categoryId");
			productCategory = productCategoryService.find(categoryId);
		}else {
			Long productCategoryId = (Long) map.get("productCategoryId");
			productCategory = productCategoryService.find(productCategoryId);
		}
		if(productCategory == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Map<String, Object> resultMap = goodsService.barCodeSave(productCenter, productCategory, idsForm.getIds());
		return JsonEntity.successMessage(resultMap);
	}
	
	 /**
	  * 
	  * @Title: flagBox
	  * @author: yuezhiwei
	  * @date: 2018年4月20日上午11:36:10
	  * @Description: 判断本地分类是否存在
	  * @return: JsonEntity
	  */
	 public Map<String, Object> flagBox(Long categoryId , Member member) {
		CategoryCenter categoryCenter = categoryCenterService.find(categoryId);
		String categoryName1 = "";
		String categoryName2 = "";
		String categoryName3 = "";
		if (categoryCenter.getGrade() == 0) {
    		categoryName1 = categoryCenter.getName();
		}
    	if (categoryCenter.getGrade() == 1) {
    		categoryName1 = categoryCenter.getParent().getName();
    		categoryName2 = categoryCenter.getName();
		}
    	if (categoryCenter.getGrade() == 2) {
        	categoryName1 = categoryCenter.getParent().getParent().getName();
    		categoryName2 = categoryCenter.getParent().getName();
    		categoryName3 = categoryCenter.getName();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		ProductCategory oneCategory = new ProductCategory();
		ProductCategory twoCategory = new ProductCategory();
		ProductCategory threeCategory = new ProductCategory();
		
		List<ProductCategory> list = productCategoryService.findByParentMember(member, null, categoryName1);
		if (list.size() > 0) {
			oneCategory = list.get(0);
			
			if (!categoryName2.equals("")) {
				List<ProductCategory> list2 = productCategoryService.findByParentMember(member, oneCategory, categoryName2);
				if (list2.size() > 0) {
					twoCategory = list2.get(0);
					
					if (!categoryName3.equals("")) {
						List<ProductCategory> list3 = productCategoryService.findByParentMember(member, twoCategory, categoryName3);
						if (list3.size() > 0) {
							threeCategory = list3.get(0);
						
							productCategoryService.save(categoryCenter, member);
							
							resultMap.put("flag", false);
							resultMap.put("productCategoryId", threeCategory.getId());
							return resultMap;
						}
						
						resultMap.put("flag", true);
						resultMap.put("productCategoryId", "");
						resultMap.put("productCategoryName", categoryName3);
						return resultMap;
					}
					
					productCategoryService.save(categoryCenter, member);
					
					resultMap.put("flag", false);
					resultMap.put("productCategoryId", twoCategory.getId());
					return resultMap;
				}
				
				resultMap.put("flag", true);
				resultMap.put("productCategoryId", "");
				resultMap.put("productCategoryName", categoryName2);
				return resultMap;
			}
			
			productCategoryService.save(categoryCenter, member);
			
			resultMap.put("flag", false);
			resultMap.put("productCategoryId", oneCategory.getId());
			return resultMap;
			
		}
		
		resultMap.put("flag", true);
		resultMap.put("productCategoryId", "");
		resultMap.put("productCategoryName", categoryName1);
		return resultMap;
	 }
	 
	/**
	 * 
	 * @Title: copy
	 * @author: yuezhiwei
	 * @date: 2018年4月20日上午11:43:32
	 * @Description: 复制资源库分类
	 * @return: Map<String,Object>
	 */
	public Map<String, Object> copy(Member member, Long categoryId, Boolean flag) {
		Map<String, Object> goodsResultMap = new HashMap<>();
		if(flag != null && flag == true) {
			CategoryCenter categoryCenter = categoryCenterService.find(categoryId);
			ProductCategory productCategory = productCategoryService.save(categoryCenter, member);
			goodsResultMap.put("categoryId", productCategory == null ? null : productCategory.getId());
		}
		return goodsResultMap;
	}
	
	/**
	 * 
	 * @Title: exist
	 * @author: yuezhiwei
	 * @date: 2018年4月20日下午4:20:30
	 * @Description: 门店里面判断是否已经分配此商品
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/exist" , method = RequestMethod.GET)
	public JsonEntity exist(String unionId, Long shopId , Long productId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(unionId == null || shopId == null || productId == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Shop shop = shopService.find(shopId);
		Product product = productService.find(productId);
		Integer count = shopService.count(member, product, shop);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(count > 0) {
			resultMap.put("count", true);
		}else {
			resultMap.put("count", false);
		}
		
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: existShop
	 * @author: yuezhiwei
	 * @date: 2018年4月20日下午4:39:34
	 * @Description: 判断当前账号是否存在门店
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/existShop" , method = RequestMethod.GET)
	public JsonEntity existShop(String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(unionId == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		List<Shop> list = shopService.findShopList(member);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(list.size() > 0) {
			resultMap.put("exist", true);
		}else {
			resultMap.put("exist", false);
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: searchList
	 * @author: yuezhiwei
	 * @date: 2018年4月20日下午7:35:49
	 * @Description: 查询本地商品（模糊搜索）
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/localSearchQuery" , method = RequestMethod.GET)
	public JsonEntity localSearchQuery(String searchName, String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(StringUtils.isEmpty(searchName) || StringUtils.isEmpty(unionId)) {
			return new JsonEntity(Code.code13012, Code.code13012.getDesc());
		}
		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		//查询本地商品
		List<ProductSupplierDto> productSupplierDtos = productService.findByProductAndSupplier(member, searchName, Types.local);
		for(final ProductSupplierDto dto : productSupplierDtos) {
			productList.add(new HashMap<String, Object>(){{
				Product product = dto.getProduct();
				Supplier supplier = dto.getSupplier();
				this.put("productId", product == null ? null : product.getId());
				this.put("goodsName", dto.getGoodsName());
				this.put("specification", product == null ? null : product.getSpecificationValues());
				this.put("supplierId", supplier == null ? null : supplier.getId());
				this.put("supplierName", supplier == null ? null : supplier.getName());
				this.put("type", "local");
			}});
		}
		return JsonEntity.successMessage(productList);
	}
	
	/**
	 * 
	 * @Title: rawMaterialLibrarySearchQuery
	 * @author: yuezhiwei
	 * @date: 2018年4月23日下午2:10:53
	 * @Description: 原料库搜索查询（模糊搜索）
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/rawMaterialLibrarySearchQuery" , method = RequestMethod.GET)
	public JsonEntity rawMaterialLibrarySearchQuery(String searchName, String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(StringUtils.isEmpty(searchName) || StringUtils.isEmpty(unionId)) {
			return new JsonEntity(Code.code13012, Code.code13012.getDesc());
		}
		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		List<ProductCenter> list = productCenterService.findList(null, searchName);
		//遍历仓库商品
		for(final ProductCenter center : list) {
			productList.add(new HashMap<String, Object>(){{
				this.put("productId", center.getId());
				this.put("goodsName", center.getGoodsCenter() == null ? null : center.getGoodsCenter().getName());
				this.put("specification", center.getSpecificationValues());
				this.put("barCode", center.getBarCode());
				this.put("type", "center");//原料库商品类型
			}});
		}
		return JsonEntity.successMessage(productList);
	}
	
	/**
	 * 
	 * @Title: existHostingGoods
	 * @author: yuezhiwei
	 * @date: 2018年4月21日下午2:08:04
	 * @Description: 判断此商品是否是当前账号的商品
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/existHostingGoods" , method = RequestMethod.GET)
	public JsonEntity existHostingGoods(String unionId,Long productId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(productId == null || StringUtils.isEmpty(unionId)) {
			return new JsonEntity(Code.code13012, Code.code13012.getDesc());
		}
		Product product = productService.find(productId);
		Supplier supplier = product.getGoods().getSupplier();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(supplier.getMember().getId() == member.getId()) {
			resultMap.put("exist", true);
		}else {
			resultMap.put("exist", false);
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 店铺内扫码搜索
	 * @param barCode
	 * @param goodsName
	 * @param unionId
	 * @param shopId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listInshop" , method = RequestMethod.GET)
	public JsonEntity listInshop(String barCode, String unionId, Long shopId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(StringUtils.isEmpty(barCode)) {
			return new JsonEntity(Code.code13012, Code.code13012.getDesc());
		}
		if(shopId == null) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		Shop shop = shopService.find(shopId);
		//先查本地商品，如果不存在再去查仓库商品
		List<ProductSupplierDto> productSupplierDtos = productService.findShopByProductAndSupplier(member, barCode, Types.local, shop);
		if(productSupplierDtos.size() > 0) {
			//遍历本地商品
			for(final ProductSupplierDto dto : productSupplierDtos) {
				productList.add(new HashMap<String, Object>(){{
					Product product = dto.getProduct();
					Supplier supplier = dto.getSupplier();
					this.put("productId", product == null ? null : product.getId());
					this.put("goodsName", dto.getGoodsName());
					this.put("specificationValues", product == null ? null : product.getSpecificationValues());
					this.put("supplierId", supplier == null ? null : supplier.getId());
					this.put("supplierName", supplier == null ? null : supplier.getName());
					this.put("type", "local");
				}});
			}
		}else {
			List<ProductCenter> list = productCenterService.findList(null, null, barCode);
			//遍历仓库商品
			for(final ProductCenter center : list) {
				productList.add(new HashMap<String, Object>(){{
					this.put("productId", center.getId());
					this.put("goodsName", center.getGoodsCenter() == null ? null : center.getGoodsCenter().getName());
					this.put("specificationValues", center.getSpecificationValues());
					this.put("barCode", center.getBarCode());
					this.put("type", "center");//仓库商品类型
				}});
				
			}
		}
		resultMap.put("productList", productList);
		return JsonEntity.successMessage(resultMap);
	}
	
	public static class IdsForm {
		private List<Long> ids;

		public List<Long> getIds() {
			return ids;
		}

		public void setIds(List<Long> ids) {
			this.ids = ids;
		}
	}
	
}
