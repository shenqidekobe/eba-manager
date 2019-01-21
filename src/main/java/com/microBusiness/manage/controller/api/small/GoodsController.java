package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.AttributeService;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.CartItemService;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.GoodsCenterService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.OrderSettingService;
import com.microBusiness.manage.service.ParameterValueService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.SpecificationItemService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.TagService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Code;

/**
 * 商流供应商Controller
 */
@Controller("apiGoodsController")
@RequestMapping("/api/small/goods")
public class GoodsController extends BaseController {

	@Resource
	private CategoryCenterService categoryCenterService ;
	@Resource
    private MemberService memberService;
    @Resource
    private ProductService productService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private SupplierService supplierService;
    @Resource
    private ChildMemberService childMemberService ;
    @Resource
	private SupplierProductService supplierProductService;
    @Resource
	private WeChatService weChatService;
    @Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
    @Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
    @Resource
	private PaymentMethodService paymentMethodService;
    @Resource(name = "orderServiceImpl")
	private OrderService orderService;
    @Resource
	private OrderSettingService orderSettingService;
    @Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
    @Resource
   	private SupplyNeedService supplyNeedService;
    @Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
    @Resource
    private ProductCategoryService productCategoryService ;
    @Resource
	private NeedService needService;
    @Resource
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
	@Resource
	private GoodsCenterService goodsCenterService;
	@Resource
	private CartItemService cartItemService;

	@RequestMapping(value = "/getTemporaryGoodsDealed")
    @ResponseBody
    public JsonEntity getTemporaryGoodsDealed(Long supplierId, String unionId, String keyword,Integer isMarketable, HttpServletRequest request, HttpServletResponse response , Pageable pageable){
        Set<Map<String,Object>> products = new HashSet<Map<String,Object>>();
    	try {
            ChildMember userInfo = childMemberService.findByUnionId(unionId);
            if (userInfo != null) {
            	Member member = memberService.find(userInfo.getMember().getId());
            	Supplier sup = supplierService.find(supplierId);
        		Need need = needService.findNeedByMemberSupplier(sup, member);
        		
         		Supplier supplier2 = supplierService.find(supplierId);
         		Set<SupplyNeed> supplyNeeds = supplier2.getSupplyNeeds();
         		for (SupplyNeed supplyNeed : supplyNeeds) {
         			if(supplyNeed.getNeed().getId().equals(need.getId())){
     					products = goodsService.getTemporaryGoodsDealed(need.getId(),supplierId, keyword, isMarketable, pageable);
         			}
     			}
             	return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),products);
			}
            return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	@RequestMapping(value = "/getFormalGoodsDealed")
    @ResponseBody
    public JsonEntity getFormalGoodsDealed(Long supplierId, String unionId, String keyword, Integer isMarketable, SupplyType supplyType,Long relationId, HttpServletRequest request, HttpServletResponse response , Pageable pageable){
		List<Map<String,Object>> products = new ArrayList<Map<String,Object>>();
    	try {
            ChildMember userInfo = childMemberService.findByUnionId(unionId);
            if (userInfo != null) {
            	Member member = memberService.find(userInfo.getMember().getId());
        		Need need = null;
        		if (supplyType.equals(SupplyType.temporary)) {
        			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
        			need = supplyNeed.getNeed();
				}else {
					SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
        			need = needService.findNeedByMemberSupplier(supplierSupplier.getBySupplier(), member);
				}
        		
         		Supplier supplier2 = supplierService.find(supplierId);
         		Set<SupplyNeed> supplyNeeds = supplier2.getSupplyNeeds();
         		for (SupplyNeed supplyNeed : supplyNeeds) {
         			if(supplyNeed.getNeed().getId().equals(need.getId())){
     					products = goodsService.getFormalGoodsDealed(need.getId(),supplierId, keyword, isMarketable , pageable , supplyType , relationId);
         			}
     			}
             	return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),products);
			}
            return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	 /**
     * 供应商品列表
     * @param supplierId
     * @param keyword
     * @param isMarketable
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @SuppressWarnings("static-access")
	@RequestMapping(value = "/goodsList")
    @ResponseBody
    public JsonEntity goodsListFormal(Long supplierId,String keyword, String unionId, Integer isMarketable,SupplyType supplyType,Long relationId,HttpServletRequest request,HttpServletResponse response , Pageable pageable){
        try {
            Member member = childMemberService.findByUnionId(unionId).getMember();
            Supplier sup = supplierService.find(supplierId);
            Need need = null;
    		if (supplyType.equals(SupplyType.temporary)) {
    			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
    			need = supplyNeed.getNeed();
			}else {
				SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
    			need = needService.findNeedByMemberSupplier(supplierSupplier.getBySupplier(), member);
			}

    		List<Map<String , Object>> result = goodsService.getFormalGoodsDealed(need.getId() , supplierId , keyword , isMarketable , pageable , supplyType , relationId) ;
			Map<String , Object> rootMap = new HashMap<String , Object>();
			rootMap.put("goodsList", result);
			rootMap.put("supplierName", sup.getName());
			rootMap.put("customerTel", sup.getCustomerServiceTel());
			boolean canRemark=true;
			if (supplyType.equals(SupplyType.temporary)) {
				SupplyNeed supplyNeed = need.getSupplyNeedBySupply();
				if (supplyNeed != null && supplyNeed.getAssignedModel().equals(SupplyNeed.AssignedModel.BRANCH)) {
					canRemark=false;
				}
			}
			rootMap.put("canRemark", canRemark);
            return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), rootMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonEntity().error(Code.code11103, Code.code11103.getDesc());
        }
    }

	

	 @SuppressWarnings("static-access")
	@RequestMapping(value = "/productSpecifications")
	    @ResponseBody
	    public JsonEntity productSpecifications(String unionId, Long id, SupplyType supplyType ,  Long supplierId ,Long relationId, HttpServletRequest request, HttpServletResponse response){
	    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
	    	
	    	try {
				if(null == id || null == supplierId || null == supplyType){
					return new JsonEntity(Code.code019998, Code.code019998.getDesc()) ;
				}
				
				Member member = childMemberService.findByUnionId(unionId).getMember();
				
				Need need = null;
	    		if (supplyType.equals(SupplyType.temporary)) {
	    			SupplyNeed supplyNeed=supplyNeedService.find(relationId);
	    			need = supplyNeed.getNeed();
				}else {
					SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
	    			need = needService.findNeedByMemberSupplier(supplierSupplier.getBySupplier(), member);
				}
	    		
				//处理商品规格
	    		List<SupplyProductDto> supplyProductDtos = productService.getSupplyProduct(id , supplierId , supplyType , need);
	    		for (SupplyProductDto productDto : supplyProductDtos) {
	    			Product product = productDto.getProduct() ;
	    			Map<String,Object> map = new HashMap<String,Object>();
	    			map.put("productId", product.getId());
	    			map.put("marketPrice", product.getMarketPrice());
	    			map.put("price", product.getSupplyPrice());
	    			map.put("rewardPoint", product.getRewardPoint());
	    			map.put("specificationValues", product.getSpecificationValues());
	    			map.put("minOrderQuantity" , productDto.getMinOrderQuantity());
	    			resultList.add(map);
				}
				return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),resultList);
			} catch (Exception e) {
				e.printStackTrace();
				return new JsonEntity().error(Code.code011401, Code.code011401.getDesc());
			}
	    }
	 
	 /**
	  * 
	  * @Title: add
	  * @author: yuezhiwei
	  * @date: 2018年3月8日下午5:22:18
	  * @Description: TODO
	  * @return: JsonEntity
	  */
	 @ResponseBody
	 @RequestMapping(value = "/add" , method = RequestMethod.GET)
	 public JsonEntity add(String unionId , Long supplierId) {
		 //Member member = childMemberService.findByUnionId(unionId).getMember();
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 if(null == supplierId) {
			 return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		 }
		 resultMap.put("units", Goods.Unit.values());
		 return JsonEntity.successMessage(resultMap);
	 }
	 
	 /**
	  * 
	  * @Title: specifications
	  * @author: yuezhiwei
	  * @date: 2018年3月19日上午10:09:41
	  * @Description: 获取规格
	  * @return: JsonEntity
	  */
	 @ResponseBody
	 @RequestMapping(value = "/specifications", method = RequestMethod.GET)
	 public JsonEntity specifications(Long productCategoryId) {
		 List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		 ProductCategory productCategory = productCategoryService.find(productCategoryId);
		 if(productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return JsonEntity.successMessage(data);
		 }
		 for(Specification specification : productCategory.getSpecifications()) {
			 Map<String, Object> item = new HashMap<String, Object>();
				item.put("name", specification.getName());
				item.put("options", specification.getOptions());
				data.add(item);
		 }
		 return JsonEntity.successMessage(data);
	 }
	 
	 /**
	  * 
	  * @Title: getGoods
	  * @author: yuezhiwei
	  * @date: 2018年3月19日下午5:59:07
	  * @Description: 验证商品名称
	  * @return: boolean
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getGoods" , method = RequestMethod.GET)
	 public boolean getGoods(String name,String oldName , Long supplierId) {
		 if(name.equals(oldName)) {
			 return true;
		 }
		 Supplier supplier = supplierService.find(supplierId);
		 List<Goods> goods = goodsService.findByName(name, supplier,Types.local);
		 if(goods.size() > 0) {
			 return false;
		 } else {
			 return true;
		 }
	 }
	 
	 
	 /**
	  * 
	  * @Title: save
	  * @author: yuezhiwei
	  * @date: 2018年3月14日上午11:06:41
	  * @Description: 添加商品
	  * @return: JsonEntity
	  */
	 @ResponseBody
	 @RequestMapping(value = "/save" , method = RequestMethod.POST)
	 public JsonEntity save(Goods goods, ProductForm productForm,ProductListForm productListForm, Long productCategoryId,
			 Long brandId, Long[] promotionIds, Long[] tagIds,
				HttpServletRequest request , Long supplierId) {
		 if(goods.getImage() == "") {
			 goods.setImage(null);
		 }
		 if(goods.getIntroduction() == "") {
			 goods.setIntroduction(null);
		 }
		 
		 productImageService.filter(goods.getProductImages());
		 parameterValueService.filter(goods.getParameterValues());
		 specificationItemService.filter(goods.getSpecificationItems());
		 productService.filter(productListForm.getProductList());

		 goods.setProductCategory(productCategoryService.find(productCategoryId));
		 goods.setBrand(brandService.find(brandId));
		 goods.setPromotions(new HashSet<Promotion>(promotionService
				.findList(promotionIds)));
		 goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		 
		 goods.setProductCategory(productCategoryService.find(productCategoryId));
		 
		 goods.removeAttributeValue();
		 for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			 String value = request.getParameter("attribute_" + attribute.getId());
			 String attributeValue = attributeService.toAttributeValue(attribute, value);
			 goods.setAttributeValue(attribute, attributeValue);
		 }

		 if (!isValid(goods, BaseEntity.Save.class)) {
			 return JsonEntity.error(Code.code019998, Code.code019998.getDesc());
		 }
		 Supplier supplier = supplierService.find(supplierId);
		 if (StringUtils.isNotEmpty(goods.getSn())&& goodsService.snExists(goods.getSn(),supplier)) {
			 return JsonEntity.error(Code.code_small_good_100001, Code.code_small_good_100001.getDesc());
		 }
		 		 
		 goods.setSupplier(supplier);
		 goods.setTypes(Types.local);

		 if (goods.hasSpecification()) {
			 List<Product> products = productListForm.getProductList();
			 if (CollectionUtils.isEmpty(products)|| !isValid(products, getValidationGroup(goods.getType()),
							 BaseEntity.Save.class)) {
				 return JsonEntity.error(Code.code019998, Code.code019998.getDesc());
			 }
			 goodsService.save(goods, products, supplier);
		 } else {
			 Product product = productForm.getProduct();
			 if (product == null
					 || !isValid(product, getValidationGroup(goods.getType()),
							 BaseEntity.Save.class)) {
				 return JsonEntity.error(Code.code019998, Code.code019998.getDesc());
			 }
			 goodsService.save(goods, product, supplier);
		 }
		 return JsonEntity.successMessage();
	 }
	 
	 /**
	  * 
	  * @Title: list
	  */
	 @SuppressWarnings("serial")
	 @ResponseBody
	 @RequestMapping(value = "/list" , method = RequestMethod.GET)
	 public JsonEntity list(String unionId, String smOpenId, Long productCategoryId , Long supplierId ,
			 Pageable pageable , String searchName) {
		 if(supplierId == null){
			 supplierId = 1l;
		 }
		 Supplier supplier = supplierService.find(supplierId);
		 ProductCategory productCategory = productCategoryService.find(productCategoryId);
		 
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("deleted");
		filter.setValue(false);
		filters.add(filter);
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isMarketable");
		filter.setValue(true);
		filters.add(filter);
		pageable.setFilters(filters);
		
		List<Order> orders = new ArrayList<Order>();
		Order orderDir = new Order();
		orderDir.setDirection(Direction.desc);
		orderDir.setProperty("hits");
		orders.add(orderDir);
		pageable.setOrders(orders);
		 
		 Page<Goods> page = goodsService.findPage(supplier, productCategory, pageable , searchName);
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 List<Map<String, Object>> goodsList = new ArrayList<Map<String,Object>>();
		 for(final Goods goods : page.getContent()) {
			 goodsList.add(new HashMap<String, Object>(){{
				 this.put("goodsId", goods.getId());
				 this.put("name", goods.getName());
				 this.put("image", goods.getImage());
				 this.put("specification", goods.getSpecificationItems());
				 this.put("price", goods.getPrice());
				 this.put("marketPrice", goods.getMarketPrice());
				 this.put("sales", goods.getSales());
				 this.put("hasSpecifications", goods.hasSpecification());
				 List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
				 for (Product product : goods.getProducts()) {
					 Map<String, Object> pmap = new HashMap<String, Object>();
					 pmap.put("productId", product.getId());
					 pmap.put("price", product.getPrice());
					 pmap.put("marketPrice", product.getMarketPrice());
					 pmap.put("minOrderQuantity", product.getMinOrderQuantity());
					 pmap.put("addValue", product.getAddValue());
					 pmap.put("specifications", product.getSpecifications());
					 pmap.put("sales", product.getSales());
					 productList.add(pmap);
					 this.put("products", productList);
				}
			 }});
		 }
		 resultMap.put("goodsList", goodsList);
		 resultMap.put("totalPages", page.getTotalPages());
		 return JsonEntity.successMessage(resultMap);
	 }
	 
	 
	 /**
	  * 代理购买的商品列表
	  * @param unionId
	  * @param smOpenId
	  * @param productCategoryId
	  * @param supplierId
	  * @param pageable
	  * @param searchName
	  * @return
	  */
	 @SuppressWarnings("serial")
	 @ResponseBody
	 @RequestMapping(value = "/proxyGoodsList" , method = RequestMethod.GET)
	 public JsonEntity proxyList(String unionId, String smOpenId, Long productCategoryId ,
			 Long supplierId , Pageable pageable , String searchName) {
		 if(StringUtils.isEmpty(smOpenId)){
			 return JsonEntity.error(Code.code_smOpenId_null_99899);
		 }
		 if(supplierId == null){
			 supplierId = 1l;
		 }
		
		 Supplier supplier = supplierService.find(supplierId);
		 ProductCategory productCategory = productCategoryService.find(productCategoryId);
		 
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isProxy");
		filter.setValue(true);
		filters.add(filter);
		
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("deleted");
		filter.setValue(false);
		filters.add(filter);
		
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isMarketable");
		filter.setValue(true);
		filters.add(filter);
		
		pageable.setFilters(filters);
		 
		 Page<Goods> page = goodsService.findPage(supplier, productCategory, pageable , searchName);
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 List<Map<String, Object>> goodsList = new ArrayList<Map<String,Object>>();
		 for(final Goods goods : page.getContent()) {
			 goodsList.add(new HashMap<String, Object>(){{
				 this.put("goodsId", goods.getId());
				 this.put("name", goods.getName());
				 this.put("image", goods.getImage());
				 this.put("specification", goods.getSpecificationItems());
				 this.put("price", goods.getDefaultProduct().getProxyPrice());
				 this.put("marketPrice", goods.getDefaultProduct().getMarketPrice());
				 this.put("sales", goods.getSales());
				 this.put("hasSpecifications", goods.hasSpecification());
				 List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
				 for (Product product : goods.getProducts()) {
					 Map<String, Object> pmap = new HashMap<String, Object>();
					 pmap.put("productId", product.getId());
					 pmap.put("price", product.getProxyPrice());
					 pmap.put("marketPrice", product.getMarketPrice());
					 pmap.put("sales", product.getSales());
					 pmap.put("minOrderQuantity", product.getMinOrderQuantity());
					 pmap.put("addValue", product.getAddValue());
					 pmap.put("specifications", product.getSpecifications());
					 productList.add(pmap);
					 this.put("products", productList);
				}
			 }});
		 }
		 resultMap.put("goodsList", goodsList);
		 resultMap.put("totalPages", page.getTotalPages());
		 return JsonEntity.successMessage(resultMap);
	 }
	 
	 
	 /**
	  * 商品详情接口
	  */
	 @SuppressWarnings("serial")
	 @ResponseBody
	 @RequestMapping(value = "/detail" , method = RequestMethod.GET)
	 public JsonEntity detail(Long goodsId, String unionId) {
		 //Member member = childMemberService.findByUnionId(unionId).getMember();
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 
		 Goods goods = goodsService.find(goodsId);
		 if(goods != null){
			 if(goods.getHits() == null){
				 goods.setHits(0l);
			 }
			 goods.setHits(goods.getHits()+1);
			 goodsService.update(goods);
		 }
		 Map<String, Object> goodMap = new HashMap<String, Object>();
		 goodMap.put("goodsId", goods.getId());
		 goodMap.put("name", goods.getName());
		 goodMap.put("image", goods.getImage());
		 goodMap.put("images", goods.getImages());
		 ProductCategory productCategory = goods.getProductCategory();
		 goodMap.put("productCategoryId", productCategory == null ? null : productCategory.getId());
		 goodMap.put("unit", goods.getUnit());
		 goodMap.put("labels", goods.getLabels());
		 goodMap.put("introduction", goods.getIntroduction());
		 goodMap.put("specification", goods.getSpecificationItems());
		 goodMap.put("price", goods.getPrice());
		 goodMap.put("marketPrice", goods.getMarketPrice());
		 goodMap.put("sales", goods.getSales());
		 goodMap.put("hasSpecifications", goods.hasSpecification());
		 Set<Product> products = goods.getProducts();
		 List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		 for(final Product product : products) {
			 productList.add(new HashMap<String, Object>(){{
				 this.put("productId", product.getId());
				 this.put("price", product.getPrice());
				 this.put("cost", product.getCost());
				 this.put("price", product.getPrice());
				 this.put("sales", product.getSales());
				 this.put("minOrderQuantity", product.getMinOrderQuantity());
				 this.put("addValue", product.getAddValue());
				 this.put("specifications", product.getSpecifications());
				 this.put("sales", product.getSales());
				 this.put("isDefault", product.getIsDefault());
			 }});
		 }
		 goodMap.put("products", productList);
		 resultMap.put("goodMap", goodMap);
		 return JsonEntity.successMessage(resultMap); 
	 }
	 
	 @SuppressWarnings("serial")
	 @ResponseBody
	 @RequestMapping(value = "/proxyGoodsDetail" , method = RequestMethod.GET)
	 public JsonEntity proxyGoodsDetail(Long goodsId , String unionId) {
		 //Member member = childMemberService.findByUnionId(unionId).getMember();
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 
		 Goods goods = goodsService.find(goodsId);
		 Map<String, Object> goodMap = new HashMap<String, Object>();
		 goodMap.put("goodsId", goods.getId());
		 goodMap.put("name", goods.getName());
		 goodMap.put("image", goods.getImage());
		 goodMap.put("images", goods.getImages());
		 ProductCategory productCategory = goods.getProductCategory();
		 goodMap.put("productCategoryId", productCategory == null ? null : productCategory.getId());
		 goodMap.put("unit", goods.getUnit());
		 goodMap.put("labels", goods.getLabels());
		 goodMap.put("introduction", goods.getIntroduction());
		 goodMap.put("specification", goods.getSpecificationItems());
		 goodMap.put("price", goods.getProxyPrice());
		 goodMap.put("sales", goods.getSales());
		 goodMap.put("hasSpecifications", goods.hasSpecification());
		 Set<Product> products = goods.getProducts();
		 List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		 for(final Product product : products) {
			 productList.add(new HashMap<String, Object>(){{
				 this.put("productId", product.getId());
				 this.put("price", product.getPrice());
				 this.put("cost", product.getCost());
				 this.put("price", product.getProxyPrice());
				 this.put("sales", product.getSales());
				 this.put("minOrderQuantity", product.getMinOrderQuantity());
				 this.put("addValue", product.getAddValue());
				 this.put("specifications", product.getSpecifications());
				 this.put("sales", product.getSales());
			 }});
		 }
		 goodMap.put("products", productList);
		 resultMap.put("goodMap", goodMap);
		 return JsonEntity.successMessage(resultMap); 
	 }
	 
	 
	 /**
	  * 
	  * @Title: update
	  */
	 @ResponseBody
	 @RequestMapping(value = "/update" , method = RequestMethod.POST)
	 public JsonEntity update(Goods goods, ProductForm productForm,ProductListForm productListForm, Long id,
			 Long productCategoryId,HttpServletRequest request , Long supplierId) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		if(goods.getImage() == "") {
			 goods.setImage(null);
		 }
		 if(goods.getIntroduction() == "") {
			 goods.setIntroduction(null);
		 }
		Goods pGoods = goodsService.find(id);
		goods.setType(pGoods.getType());
		goods.setTypes(Types.local);
		goods.setProductCategory(productCategoryService.find(productCategoryId));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Update.class)) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}

		Supplier supplier = supplierService.find(supplierId);
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products)|| !isValid(products, getValidationGroup(goods.getType()),BaseEntity.Update.class)) {
				return JsonEntity.error(Code.code_small_good_100001, Code.code_small_good_100001.getDesc());
			}
			 goodsService.update(goods, products, supplier);
		} else {
			Product product = productForm.getProduct();
			if (product == null|| !isValid(product, getValidationGroup(goods.getType()),BaseEntity.Update.class)) {
				return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
			}
			goodsService.update(goods, product, supplier);
		}
		 return JsonEntity.successMessage();
	 }
	 
	 /**
	  * 
	  * @Title: delete
	  */
	 @ResponseBody
	 @RequestMapping(value = "/delete" , method = RequestMethod.POST)
	 public JsonEntity delete(Long id , String unionId , Long supplierId) {
		 if(null == id || null == unionId || null == supplierId) {
			 return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		 }
		 //Member member = childMemberService.findByUnionId(unionId).getMember();
		 Goods goods = goodsService.find(id);
		 Supplier supplier = supplierService.find(supplierId);
		 boolean bool = goodsService.existLocalDistributionGoods(supplier, goods);
		 if(bool) {
			 return JsonEntity.error(Code.code_small_good_100002, Code.code_small_good_100002.getDesc());
		 }
		 goodsService.deleted(goods);
		 return JsonEntity.successMessage();
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
		default:
			break;
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
