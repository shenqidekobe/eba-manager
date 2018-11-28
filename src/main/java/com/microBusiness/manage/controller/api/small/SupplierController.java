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
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.SystemSetting;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedShopProductService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.SupplierNeedProductService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.Code;

//供应商-店主
@Controller("smallSupplierController")
@RequestMapping("/api/small/supplier")
public class SupplierController extends BaseController{
	@Resource
	private SupplierService supplierService ;
	@Resource
	private ChildMemberService childMemberService ;
	@Resource
	private AdminService adminService ;
	@Resource
	private ShopService shopService ;
	@Resource
	private SupplierSupplierService supplierSupplierService ;
	@Resource
	private NeedShopProductService needShopProductService;
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
	private SupplierNeedProductService supplierNeedProductService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private SupplierProductService supplierProductService;
	@Resource
	private GoodsService goodsService;

	/**
	 * 获取客户供应关系的企业列表
	 * @param unionId
	 * @param request
	 * @param response
	 * @param supplierId 扫码所属企业id   2018/3/8 暂时不用扫码功能
	 * @return
	 */
	@RequestMapping(value = "/formal")
    @ResponseBody
    public JsonEntity selectSupplier(String unionId, HttpServletRequest request,HttpServletResponse response , Long supplierId){
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Member member = childMemberService.findByUnionId(unionId).getMember();
		/**
		 * 直营店供应类型  1、2、4
		 * 本企业
		 */
		Admin admin = member.getAdmin();
		if(admin != null) {
			Supplier supplier=admin.getSupplier();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("supplierId", supplier.getId());
			map.put("name", supplier.getName());
			map.put("supplierType", SupplierType.ONE);
			resultList.add(map);
		
		
			/**
			 * 企业-->企业-->门店
			 */
			List<SupplierSupplier.Status> statuses=new ArrayList<>();
			statuses.add(SupplierSupplier.Status.inTheSupply);
			List<SupplierSupplier> list=supplierSupplierService.getListBySupplier(supplier,statuses);
			for (SupplierSupplier supplierSupplier : list ) {
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("supplierId", supplierSupplier.getSupplier().getId());
				map2.put("name", supplierSupplier.getSupplier().getName());
				map2.put("relationId", supplierSupplier.getId());
				map2.put("supplierType", SupplierType.TWO);
				resultList.add(map2);
			}
		}
		
		/**
		 * 加盟店供应类型  3、4
		 * 企业-->门店
		 */
		List<SupplyNeed> list3=supplyNeedService.findByMember(member);
		for (SupplyNeed supplyNeed : list3) {
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("supplierId", supplyNeed.getSupplier().getId());
			map1.put("name", supplyNeed.getSupplier().getName());
			map1.put("relationId", supplyNeed.getId());
			map1.put("supplierType", SupplierType.THREE);
			resultList.add(map1);
		}
		
		/**
		 * 本地供应商
		 */
		List<Supplier> suppliers=supplierService.getSupplierListByMember(member);
		for (Supplier supplier1 : suppliers) {
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("supplierId", supplier1.getId());
			map2.put("name", supplier1.getName());
			map2.put("supplierType", SupplierType.FOUR);
			resultList.add(map2);
		}
		return JsonEntity.successMessage(resultList);
		
    }
	
	/**
	 * 判断企业名称是否已认证过
	 * @param name 企业名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/supplierExists", method = RequestMethod.GET)
	public JsonEntity supplierExists(String name){
		return JsonEntity.successMessage(supplierService.exists(name));
	}

	/**
	 * 判断注册企业时用户名是否重复
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/usernameExists", method = RequestMethod.GET)
	public JsonEntity usernameExists(String userName){
		return JsonEntity.successMessage(adminService.usernameExists(userName));
	}
	
	/**
	 * 注册企业
	 * @param unionId
	 * @param name   	企业名称
	 * @param userName  用户名
	 * @param password  密码
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity register(String unionId,String name,String userName,String password){
		try {
			Member member = childMemberService.findByUnionId(unionId).getMember();
			//判断手机号是否已被占用
			Admin admin=adminService.findBybindPhoneNum(member.getMobile());
			if (admin != null){
				return JsonEntity.error(Code.code010403, Code.code010403.getDesc());
			}
			Supplier supplier=new Supplier();
			supplier.setName(name);
			supplier.setTel(member.getMobile());
			supplier.setUserName(userName);
			
			admin=new Admin();
			admin.setBindPhoneNum(member.getMobile());
			admin.setUsername(userName);
			admin.setPassword(password);
			
			adminService.register(supplier , admin , member);
			return JsonEntity.successMessage();
		}catch (Exception e) {
			e.printStackTrace();
			return JsonEntity.error(Code.code010402, Code.code010402.getDesc());
		}
		
		
	}
	
	/**
	 * 
	 * @Title: addSupplier
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午4:20:56
	 * @Description: 添加本地供应商
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "addSupplier" , method = RequestMethod.POST)
	public JsonEntity addSupplier(String name , String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(null == name || "" == name) {
			return JsonEntity.error(Code.code_small_supplier_100001, Code.code_small_supplier_100001.getDesc());
		}
		boolean bool = supplierService.doesItExistName(name, Types.local,member);
		if(bool) {
			return JsonEntity.error(Code.code_small_supplier_100002, Code.code_small_supplier_100002.getDesc());
		}
		Supplier supplier = new Supplier();
		supplier.setName(name);
		supplier.setTypes(Types.local);
		supplier.setMember(member);
		supplierService.save(supplier);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: editSupplier
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午4:27:04
	 * @Description: 修改
	 * @return: JsonEntity
	 * id 企业id
	 */
	@ResponseBody
	@RequestMapping(value = "editSupplier" , method = RequestMethod.GET)
	public JsonEntity editSupplier(Long id , String unionId) {
		if(null == id) {
			return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		}
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Supplier supplier = supplierService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", supplier.getId());
		resultMap.put("name", supplier.getName());
		resultMap.put("types", supplier.getTypes());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: updateSupplier
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午4:32:31
	 * @Description: 更新本地供应商
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "updateSupplier" , method = RequestMethod.POST)
	public JsonEntity updateSupplier(Long id , String name , String unionId , String oldName) {
		if(null == id) {
			return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		}
		if(null == name || "" == name) {
			return JsonEntity.error(Code.code_small_supplier_100001, Code.code_small_supplier_100001.getDesc());
		}
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if(!name.equals(oldName)) {
			boolean bool = supplierService.doesItExistName(name, Types.local,member);
			if(bool) {
				return JsonEntity.error(Code.code_small_supplier_100002, Code.code_small_supplier_100002.getDesc());
			}
		}
		Supplier supplier = supplierService.find(id);
		supplier.setName(name);
		supplierService.update(supplier);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: deleteSupplier
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午4:42:44
	 * @Description: 删除本地供应商
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSupplier" , method = RequestMethod.POST)
	public JsonEntity deleteSupplier(Long id , String unionId) {
		if(null == id && null == unionId) {
			return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		}
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Supplier supplier = supplierService.find(id);
		List<NeedShopProduct> list=needShopProductService.getList(null,null,supplier);
		if(list.size() > 0) {
			return JsonEntity.error(Code.code_small_supplier_100003, Code.code_small_supplier_100003.getDesc());
		}
		List<Goods> goods = goodsService.findLocalGoodsList(supplier, Types.local, null, null);
		supplierService.deletedLocalSupplier(supplier, goods);
		return JsonEntity.successMessage();
	}

	/**
	 * 店铺商品管理供应商列表
	 * @param shopId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSupplierByShop" , method = RequestMethod.GET)
	public JsonEntity getSupplierByShop(Long shopId){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Shop shop=shopService.find(shopId);
		Member member = shop.getMember();
		if (ShopType.direct.equals(shop.getShopType())){
			//直营店供应类型  1、2、4
			//本企业
			Supplier supplier=member.getAdmin().getSupplier();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("supplierId", supplier.getId());
			map.put("name", supplier.getName());
			map.put("supplierType", SupplierType.ONE);
			//总商品数量
			map.put("productCount",supplier.getProductCount());
			//分配商品数
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			SupplyNeed supplyNeed=new SupplyNeed();
			supplyNeed.setSupplier(supplier);
			supplyNeed.setNeed(need);
			supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);
			if (supplyNeed != null){
				map.put("assignSize",needShopProductService.getList(supplyNeed,shop,null).size());
			}else {
				map.put("assignSize",0);
			}
			resultList.add(map);

			// 企业-->企业-->门店
			List<SupplierSupplier.Status> statuses=new ArrayList<>();
			statuses.add(SupplierSupplier.Status.inTheSupply);
			List<SupplierSupplier> list=supplierSupplierService.getListBySupplier(supplier,statuses);
			for (SupplierSupplier supplierSupplier : list ) {
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("supplierId", supplierSupplier.getSupplier().getId());
				map2.put("name", supplierSupplier.getSupplier().getName());
				map2.put("relationId", supplierSupplier.getId());
				map2.put("supplierType", SupplierType.TWO);
				//总商品数量
				map2.put("productCount",supplierSupplier.getProductCount());
				//分配商品数
				map2.put("assignSize",supplierNeedProductService.findBySupplier(supplierSupplier,need).size());
				resultList.add(map2);
			}
		}else {
			//加盟店供应类型  3、4
			//企业-->门店
			List<SupplyNeed> list=supplyNeedService.findByMember(member);
			for (SupplyNeed supplyNeed : list) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("supplierId", supplyNeed.getSupplier().getId());
				map.put("name", supplyNeed.getSupplier().getName());
				map.put("relationId", supplyNeed.getId());
				map.put("supplierType", SupplierType.THREE);
				//总商品数量
				map.put("productCount",supplyNeed.getNeedProducts().size());
				//分配商品数
				map.put("assignSize",needShopProductService.getList(supplyNeed,shop,null).size());
				resultList.add(map);
			}

		}
		//本地供应商
		List<Supplier> suppliers=supplierService.getSupplierListByMember(member);
		for (Supplier supplier : suppliers) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("supplierId", supplier.getId());
			map.put("name", supplier.getName());
			map.put("supplierType", SupplierType.FOUR);
			//总商品数量
			map.put("productCount",supplier.getProductCount());
			//分配商品数
			map.put("assignSize",needShopProductService.getList(null,shop,supplier).size());
			resultList.add(map);
		}
		return  JsonEntity.successMessage(resultList);
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
	public JsonEntity getAssignProducts(Long supplierId,Long shopId,SupplierType supplierType,Long relationId){
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
				List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null);
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
			List<SupplierNeedProduct> list=supplierNeedProductService.findBySupplier(supplierSupplier,need);
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
			List<NeedShopProduct> list=needShopProductService.getList(supplyNeed,shop,null);
			for (NeedShopProduct needShopProduct : list ) {
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
			List<NeedShopProduct> list=needShopProductService.getList(null,shop,supplier);
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

	/**
	 * 查询供应商品列表
	 * @param supplierId   					供应商id
	 * @param supplierType			供应商类型
	 * @param relationId			供应id
	 * @param pageable
	 * @param productCategoryId		分类id
	 * @param goodName				商品名称 模糊查询
	 * @param checkedIds			已选商品id 排除掉
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getProducts" , method = RequestMethod.GET)
	public JsonEntity getProducts(Long supplierId,SupplierType supplierType,Long relationId,Pageable pageable,Long productCategoryId,String goodName,Long[] checkedIds){
		List<Map<String,Object>> resultList = new ArrayList<>();
		Supplier supplier=supplierService.find(supplierId);//供应商
		ProductCategory productCategory=productCategoryService.find(productCategoryId);
		Page<Product> page=null;
		if (SupplierType.ONE.equals(supplierType) || SupplierType.FOUR.equals(supplierType)){
			page= productService.findPageSmall(supplier,productCategory,goodName,checkedIds,pageable);
			for (Product product : page.getContent() ) {
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("specifications",product.getSpecifications());
				map.put("supplyPrice",product.getPrice());
				resultList.add(map);
			}
		}else if (SupplierType.TWO.equals(supplierType)){
			SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
			page=productService.getGoodListPageByFormal(supplierSupplier,null,productCategory,goodName,pageable,checkedIds);
			for (Product product : page.getContent() ) {
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("specifications",product.getSpecifications());
				map.put("supplyPrice",product.getSupplyPrice());
				map.put("minOrderQuantity",product.getMinOrderQuantity());
				resultList.add(map);
			}
		}else if (SupplierType.THREE.equals(supplierType)){
			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
			page=productService.getGoodListPage(supplyNeed,null,null,false,productCategory,goodName,pageable,checkedIds);
			for (Product product : page.getContent() ) {
				Map<String,Object> map=new HashMap<>();
				map.put("productId",product.getId());
				map.put("name",product.getGoods().getName());
				map.put("image",product.getGoods().getImage());
				map.put("specifications",product.getSpecifications());
				map.put("minOrderQuantity",product.getMinOrderQuantity());
				map.put("supplyPrice",product.getSupplyPrice());
				resultList.add(map);
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("data",resultList);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}

	/**
	 *供应分配
	 * @param supplierId                    供应商id
	 * @param shopId				店铺id
	 * @param supplierType			供应商类型
	 * @param relationId			供应id
	 * @param assignedModel			直销or分销  仅在供应类型为1的时候使用
	 * @param productForm			分配的商品
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "assignProducts" , method = RequestMethod.POST)
	public JsonEntity assignProducts(Long supplierId, Long shopId, SupplierType supplierType, Long relationId, SupplyNeed.AssignedModel assignedModel, ProductForm productForm){
		Supplier supplier=supplierService.find(supplierId);//供应商
		Shop shop=shopService.find(shopId);
		supplierService.assignProducts(shop,supplierType,supplier,relationId,assignedModel,productForm.getProductList());
		return JsonEntity.successMessage();
	}

	public static class ProductForm {

		private List<Product> productList=new ArrayList<>();

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}
	}

	/**
	 * 下单时获取已分配的供应商列表
	 * @param shopId   店铺id
	 * @param types		平台订单 or  本地订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAssignSupplier" , method = RequestMethod.GET)
	public JsonEntity getAssignSupplier(Long shopId,Types types){
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

		//本地订单才包含供应类型4
		if (Types.local.equals(types)) {
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
		}
		return JsonEntity.successMessage(resultList);
	}


	/**
	 * 下单的时候获取商品列表
	 * @param supplierId					供应商id
	 * @param shopId				店铺id
	 * @param supplierType			供应类型
	 * @param relationId			供应id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAssignGoods" , method = RequestMethod.GET)
	public JsonEntity getAssignGoods(Long supplierId,Long shopId,SupplierType supplierType,Long relationId,String goodsName,Long productCategoryId,Pageable pageable){
		Shop shop=shopService.find(shopId);
		Supplier supplier=supplierService.find(supplierId);
		Map<String,Object> map=goodsService.findPageByAssign(relationId,shop,supplier,supplierType,goodsName,productCategoryId,pageable);
		map.put("customerTel" , supplier.getCustomerServiceTel());
		return  JsonEntity.successMessage(map);
	}

	/**
	 * 查询供应商品列表
	 * @param supplierId   			供应商id
	 * @param supplierType			供应商类型
	 * @param relationId			供应id
	 * @param pageable
	 * @param productCategoryId		分类id
	 * @param goodName				商品名称 模糊查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getGoods" , method = RequestMethod.GET)
	public JsonEntity getGoods(Long supplierId,SupplierType supplierType,Long relationId,Pageable pageable,Long productCategoryId,String goodName){
		Supplier supplier=supplierService.find(supplierId);//供应商
		ProductCategory productCategory=productCategoryService.find(productCategoryId);
		Map<String, Object> map=goodsService.findPageBySupply(relationId, supplier, supplierType, goodName, productCategory, pageable);
		return JsonEntity.successMessage(map);
	}

	@ResponseBody
	@RequestMapping(value = "getSupplier" , method = RequestMethod.GET)
	public JsonEntity getSupplier(String unionId, Pageable pageable){
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		Supplier supplier = member.getAdmin().getSupplier();
		
		if (supplier != null) {
			map.put("name", supplier.getName());
			map.put("status", supplier.getStatus());
			map.put("userName", member.getAdmin().getUsername());
		}
		
		return JsonEntity.successMessage(map);
	}

}
