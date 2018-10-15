package com.microBusiness.manage.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.Code;

@Controller
@RequestMapping("/api/product")
public class ProductController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private MemberService memberService;
    @Resource
    private ProductService productService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private SupplierService supplierService;
    @Resource
    private SupplyNeedService supplyNeedService;
    @Resource
    private NeedProductService needProductService;

    @Resource
	private SupplierProductService supplierProductService ;
    @Resource
    private NeedService needService;

    @RequestMapping(value = "/goodsList" , params="supplyType=temporary")
    @ResponseBody
    public JsonEntity goodsList(Long supplierId,String keyword,Integer isMarketable,HttpServletRequest request,HttpServletResponse response , Pageable pageable){
        Set<Map<String,Object>> products = new HashSet<Map<String,Object>>();
    	try {
    		Member member = getUserInfo(request);
    		//获取个体客户关系
          	Supplier sup = supplierService.find(supplierId);
          	Need need = needService.findNeedByMemberSupplier(sup, member);
    		Supplier supplier2 = supplierService.find(supplierId);
    		Set<SupplyNeed> supplyNeeds = supplier2.getSupplyNeeds();
    		for (SupplyNeed supplyNeed : supplyNeeds) {
    			if(supplyNeed.getNeed().getId().equals(need.getId())){
    				/*if(StringUtil.isBlank(keyword)){
    					Set<NeedProduct> needProducts = supplyNeed.getNeedProducts();
    					for (NeedProduct needProduct : needProducts) {
    						Product product = needProduct.getProducts();
        					Goods goods = product.getGoods();
        					Map<String,Object> map = new HashMap<String,Object>();
        					map.put("id", goods.getId());
        					map.put("name", goods.getName());
        					map.put("caption", goods.getCaption());
        					products.add(map);
    					}
    				}else{*/
					products = goodsService.getTemporaryGoodsDealed(need.getId(),supplierId,keyword,isMarketable , pageable);
    			}
			}
        	return new JsonEntity(Code.code0.getDesc(),"",request.getRequestURL().toString(),products);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code11103, Code.code11103.getDesc());
		}
    }
    
    @RequestMapping(value = "/productDetails")
    @ResponseBody
    public JsonEntity productDetails(String supplierId,Goods goods,HttpServletRequest request,HttpServletResponse response){
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
    		Goods goods2 = goodsService.find(goods.getId());
    		map.put("goodsId", goods2.getId());
			map.put("name", goods2.getName());
			map.put("caption", goods2.getCaption());
			map.put("image", goods2.getImage());
			map.put("introduction", goods2.getIntroduction());
			map.put("keyword", goods2.getKeyword());
			map.put("marketPrice", goods2.getMarketPrice());
			map.put("price", goods2.getPrice());
			map.put("productImages", goods2.getProductImages());
			return new JsonEntity(Code.code0.getDesc(),"",request.getRequestURL().toString(),map);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code011401, Code.code011401.getDesc());
		}
    }
    
    @RequestMapping(value = "/productSpecifications")
    @ResponseBody
    public JsonEntity productSpecifications(Long id, SupplyType supplyType ,  Long supplierId , HttpServletRequest request, HttpServletResponse response){
    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    	
    	try {
			if(null == id || null == supplierId || null == supplyType){
				return new JsonEntity(Code.code019998, Code.code019998.getDesc()) ;
			}

			Member member = getUserInfo(request);
			//获取个体客户关系
          	Supplier sup = supplierService.find(supplierId);
          	Need need = needService.findNeedByMemberSupplier(sup, member);
          	
			//处理商品规格
          	List<NeedProduct> needProducts=productService.getNeeedProducts(id, supplierId, supplyType, need);
    		for (NeedProduct needProduct : needProducts) {
    			Product product = needProduct.getProducts() ;
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("productId", product.getId());
    			map.put("marketPrice", product.getMarketPrice());
    			map.put("price", needProduct.getSupplyPrice());
    			map.put("rewardPoint", product.getRewardPoint());
    			map.put("specificationValues", product.getSpecificationValues());
    			map.put("minOrderQuantity" , needProduct.getMinOrderQuantity());
    			resultList.add(map);
			}
    		/*List<Product> Products = productService.getproductSpecifications(goods.getId());
    		for (Product product : Products) {
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("id", product.getId());
    			map.put("marketPrice", product.getMarketPrice());
    			map.put("price", product.getPrice());
    			map.put("rewardPoint", product.getRewardPoint());
    			map.put("specificationValues", product.getSpecificationValues());
    			resultList.add(map);
			}*/
			return new JsonEntity(Code.code0.getDesc(),"",request.getRequestURL().toString(),resultList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code011401, Code.code011401.getDesc());
		}
    }


    /**
     * 正式供应商品列表
     * @param supplierId
     * @param keyword
     * @param isMarketable
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/goodsList" , params="supplyType=formal")
    @ResponseBody
    public JsonEntity goodsListFormal(Long supplierId,String keyword,Integer isMarketable,SupplyType supplyType ,Long relationId, HttpServletRequest request,HttpServletResponse response , Pageable pageable){
        try {
            Member member = getUserInfo(request);
            //获取个体客户关系
          	Supplier sup = supplierService.find(supplierId);
          	Need need = needService.findNeedByMemberSupplier(sup, member);

			List<Map<String , Object>> result = goodsService.getFormalGoodsDealed(need.getId() , supplierId , keyword , isMarketable , pageable , supplyType , relationId) ;

            return new JsonEntity(Code.code0.getDesc(),"",request.getRequestURL().toString(),result);

        } catch (Exception e) {
            e.printStackTrace();
            return new JsonEntity().error(Code.code11103, Code.code11103.getDesc());
        }
    }



    private Set<Map<String , Object>> dealResult(List<Goods> result){

		Set<Map<String,Object>> goods = new HashSet<Map<String,Object>>();

		for (Goods good : result) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("goodsId", good.getId());
			map.put("name", good.getName());
			map.put("caption", good.getCaption());
			map.put("keyword", good.getKeyword());
			map.put("image", good.getImage());
			map.put("introduction", good.getIntroduction());
			map.put("specificationItems", good.getSpecificationItems());
			map.put("productImages", good.getProductImages());
			Set<Product> products2 = good.getProducts();
			map.put("productsSize", products2.size());
			map.put("hasSpecification", good.hasSpecification());
			for (Product product : products2) {
				map.put("productId",product.getId());
				break;
			}
			goods.add(map);
		}
		return goods ;
	}
}
