package com.microBusiness.manage.controller.api.small;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.*;
import com.microBusiness.manage.util.Code;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：商品分类接口
 * 修改记录：
 */
@Controller("smallProductCategoryController")
@RequestMapping(value = "/api/small/category")
public class ProductCategoryController extends BaseController {

    @Resource
    private ProductCategoryService productCategoryService ;
    @Resource
    private SupplierDao supplierDao ;
    @Resource
    private ChildMemberService childMemberService;
    
    @Resource
    private SupplierService supplierService;
    @Resource
	private NeedService needService;
    @Resource
	private SupplyNeedService supplyNeedService;
	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
	@Resource
	private ShopService shopService;
	@Resource
	private GoodsService goodsService;

    @RequestMapping(value = "/get" , params="supplyType=formal" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getFormal(String unionId, Long supplierId ,Long relationId, HttpServletRequest request , HttpServletResponse response){

        Member member = childMemberService.findByUnionId(unionId).getMember();
        
		SupplierSupplier supplierSupplier=supplierSupplierService.find(relationId);
		Need need = needService.findNeedByMemberSupplier(supplierSupplier.getBySupplier(), member);

        List<ProductCategory> productCategories = productCategoryService.findByFormal(need,relationId);

        return JsonEntity.successMessage(this.dealResult(productCategories));
    }

    @RequestMapping(value = "/get" , params="supplyType=temporary" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getTemporary(String unionId, Long supplierId ,Long relationId, HttpServletRequest request , HttpServletResponse response){


        SupplyNeed supplyNeed=supplyNeedService.find(relationId);
		Need need = supplyNeed.getNeed();

        List<ProductCategory> productCategories = productCategoryService.findByTemporary(need , relationId);

        return JsonEntity.successMessage(this.dealResult(productCategories));
    }

    private Set<Map<String , Object>> dealResult(List<ProductCategory> categories){
        Set<Map<String , Object>> result = new HashSet<>();
        for(ProductCategory category : categories){
            Map<String , Object> categoryMap = new HashMap<>();
            categoryMap.put("id" , category.getId()) ;
            categoryMap.put("name" , category.getName());

            result.add(categoryMap);
        }

        return result ;
    }
    
    /**
     * 
     * @Title: getCategories
     * @date: 2018年3月7日下午2:02:53
     * @Description: 查询分类
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/getCategories", method = RequestMethod.GET)
    public JsonEntity getCategories(String unionId, String smOpenId, Long supplierId) {
    	//Member member = childMemberService.findByUnionId(unionId).getMember();
    	supplierId = 1l;
    	List<Filter> filterscategory = new ArrayList<Filter>();
		Filter fi = new Filter();
		fi.setOperator(Operator.eq);
		fi.setProperty("supplier");
		fi.setValue(supplierId);
		filterscategory.add(fi);
		
		fi = new Filter();
		fi.setOperator(Operator.eq);
		fi.setProperty("supplier");
		fi.setValue(supplierId);
		filterscategory.add(fi);
		
		List<Order> orderscategory = new ArrayList<Order>();
		Order order = new Order();
		order.setDirection(Direction.asc);
		order.setProperty("order");
		orderscategory.add(order);
    	List<Map<String, Object>> productCategoriesMapList = new ArrayList<Map<String, Object>>();
        List<ProductCategory> productCategories = productCategoryService.findList(0, 100, 
        		filterscategory, orderscategory);
        if(productCategories != null){
        	for (ProductCategory productCategory : productCategories) {
        		Map<String, Object> pmap = new HashMap<String, Object>();
        		pmap.put("name", productCategory.getName());
        		pmap.put("id", productCategory.getId());
        		pmap.put("grade", productCategory.getGrade());
        		pmap.put("parentId", productCategory.getParent() == null ? null : productCategory.getParent().getId());
        		productCategoriesMapList.add(pmap);
			}
        }
    	return JsonEntity.successMessage(productCategoriesMapList);
    }
    
    /**
     * 
     * @Title: save
     * @author: yuezhiwei
     * @date: 2018年3月6日下午2:22:16
     * @Description: 新增本地分类
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonEntity save(ProductCategory productCategory, Long parentId , String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	ProductCategory category = productCategoryService.find(parentId);
    	if(category != null) {
    		if(category.getGrade() >= 2) {
    			return JsonEntity.error(Code.code_small_category_100002, Code.code_small_category_100002.getDesc());
    		}
    	}
    	
    	List<ProductCategory> list = productCategoryService.findByParent(member, category, productCategory.getName(), Types.local);
    	if(list.size() > 0) {
    		return JsonEntity.error(Code.code_small_category_100005, Code.code_small_category_100005.getDesc());
    	}
    	productCategory.setParent(category);
		
		productCategory.setMember(member);
		productCategory.setTypes(Types.local);
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setGoods(null);
		productCategory.setParameters(null);
		productCategory.setAttributes(null);
		productCategory.setSpecifications(null);
		productCategoryService.save(productCategory);
    	return JsonEntity.successMessage();
    }
    
    /**
     * 
     * @Title: edit
     * @author: yuezhiwei
     * @date: 2018年3月6日下午2:33:06
     * @Description: TODO
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public JsonEntity edit(Long id ,String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	if(null == id) {
    		return JsonEntity.error(Code.code_small_category_100001, Code.code_small_category_100001.getDesc());
    	}
    	ProductCategory productCategory = productCategoryService.find(id);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("id", productCategory.getId());
    	resultMap.put("name", productCategory.getName());
    	ProductCategory parent = productCategory.getParent();
    	Long parentId = null;
    	if(parent != null) {
    		parentId = parent.getId();
    	}
    	List<ProductCategory> productCategories = productCategoryService.findChildren(productCategory, true, null);
    	List<Map<String, Object>> childrenList = new ArrayList<Map<String,Object>>();
    	for(final ProductCategory category : productCategories) {
    		childrenList.add(new HashMap<String, Object>(){{
    			this.put("id", category.getId());
    			this.put("name", category.getName());
    		}});
    	}
    	resultMap.put("childrenList", childrenList);
    	resultMap.put("parentId", parentId);
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 
     * @Title: update
     * @author: yuezhiwei
     * @date: 2018年3月7日下午7:33:00
     * @Description: 更新本地分类
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonEntity update(ProductCategory productCategory, Long parentId , String unionId , String oldName) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	ProductCategory category = productCategoryService.find(parentId);
    	if(category != null) {
    		if(category.getGrade() >= 2) {
    			return JsonEntity.error(Code.code_small_category_100002, Code.code_small_category_100002.getDesc());
    		}
    	}
    	if (!productCategory.getName().equals(oldName)) {
    		List<ProductCategory> list = productCategoryService.findByParent(member, category, productCategory.getName(), Types.local);
        	if(list.size() > 0) {
        		return JsonEntity.error(Code.code_small_category_100005, Code.code_small_category_100005.getDesc());
        	}
		}
    	productCategory.setParent(category);
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
			}
			List<ProductCategory> children = productCategoryService.findLocalChildren(parent, true, null);
			if (children != null && children.contains(parent)) {
				return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
			}
		}
		productCategoryService.update(productCategory, "treePath", "grade", "children", "goods","member","types");
    	return JsonEntity.successMessage();
    }
    
    /**
     * 
     * @Title: delete
     * @author: yuezhiwei
     * @date: 2018年3月13日下午7:30:20
     * @Description: 删除
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonEntity delete(Long id , String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		Set<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return JsonEntity.error(Code.code_small_category_100003, Code.code_small_category_100003.getDesc());
		}
		//Set<Goods> goods = productCategory.getGoods();
		List<Goods> goods = goodsService.findbyProductCategoryList(productCategory);
		if (goods != null && !goods.isEmpty()) {
			return JsonEntity.error(Code.code_small_category_100004, Code.code_small_category_100004.getDesc());
		}
		productCategoryService.deleted(id);
    	return JsonEntity.successMessage();
    }

	/**
	 * 供应分配时商品分类接口
	 * @param supplierId					供应商id
	 * @param supplierType			供应商类型
	 * @param relationId			供应id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAssignProductCategory", method = RequestMethod.GET)
    public JsonEntity getAssignProductCategory(Long supplierId,SupplierType supplierType,Long relationId){
		List<Map<String  , Object>> list=new ArrayList<>();
		if (SupplierType.ONE.equals(supplierType)){
			Supplier supplier=supplierService.find(supplierId);//供应商
			list=ProductCategory.getCategoryIterator(new HashSet<>(productCategoryService.findRoots(null, supplier)));
		}else if (SupplierType.TWO.equals(supplierType)){
			Set<ProductCategory> set= new HashSet<>(productCategoryService.findBySupplierSuppler(relationId));
			for (ProductCategory productCategory : set) {
				list=ProductCategory.getCategorySmallToLarge(list,new HashMap<String, Object>(),productCategory);
			}
		}else if (SupplierType.THREE.equals(supplierType)){
			Set<ProductCategory> set= new HashSet<>(productCategoryService.findBySupplyNeedId(relationId));
			for (ProductCategory productCategory : set) {
				list=ProductCategory.getCategorySmallToLarge(list,new HashMap<String, Object>(),productCategory);
			}
		}else if (SupplierType.FOUR.equals(supplierType)){
			Supplier supplier=supplierService.find(supplierId);//供应商
			Member member=supplier.getMember();
			list=ProductCategory.getCategoryIterator(new HashSet<>(productCategoryService.findRootByMember(member)));
		}
		return JsonEntity.successMessage(list);
	}

	/**
	 * 下单时 获取已分配商品分类接口
	 * @param supplierId					供应商id
	 * @param shopId                    店铺id
	 * @param supplierType			供应商类型
	 * @param relationId			供应id
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "/getProductsCategory", method = RequestMethod.GET)
//	public JsonEntity getProductsCategory(Long supplierId,Long shopId,SupplierType supplierType,Long relationId){
//		List<Map<String  , Object>> list=new ArrayList<>();
//		if (SupplierType.ONE.equals(supplierType) || SupplierType.THREE.equals(supplierType)){
//			Set<ProductCategory> set= new HashSet<>(productCategoryService.findBySupplyNeedShop(relationId,shopId,null));
//			for (ProductCategory productCategory : set) {
//				list=ProductCategory.getCategorySmallToLarge(list,new HashMap<String, Object>(),productCategory);
//			}
//		}else if (SupplierType.TWO.equals(supplierType)){
//			Shop shop=shopService.find(shopId);
//			//由于能选择供应类型2 企业-->企业-->店铺 只能是直营店  直营店只对应一个need  所以可以这样拿need
//			Need need=shop.getNeeds().iterator().next();
//			Set<ProductCategory> set= new HashSet<>(productCategoryService.findBySupplierSupplerNeed(relationId,need));
//			for (ProductCategory productCategory : set) {
//				list=ProductCategory.getCategorySmallToLarge(list,new HashMap<String, Object>(),productCategory);
//			}
//		}else if (SupplierType.FOUR.equals(supplierType)){
//			Set<ProductCategory> set= new HashSet<>(productCategoryService.findBySupplyNeedShop(null,shopId,supplierId));
//			for (ProductCategory productCategory : set) {
//				list=ProductCategory.getCategorySmallToLarge(list,new HashMap<String, Object>(),productCategory);
//			}
//		}
//		return JsonEntity.successMessage(list);
//	}
	


}
