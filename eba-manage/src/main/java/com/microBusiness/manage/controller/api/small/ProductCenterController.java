package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.SpecificationCenter;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.ProductCenterService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/7 下午7:08
 * Describe:
 * Update:
 */
@Controller(value = "apiProductCenterController")
@RequestMapping(value = "/api/small/productCenter")
public class ProductCenterController extends BaseController {

    @Resource
    private ProductCenterService productCenterService ;

    @Resource
    private CategoryCenterService categoryCenterService ;

    /**
     * 获取产品库规格商品
     * @param categoryId 栏目id
     * @param goodsName
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getProduct(Long categoryId , String goodsName , Pageable pageable){

        CategoryCenter categoryCenter = categoryCenterService.find(categoryId) ;

        Page<ProductCenter> page = productCenterService.findPage(categoryCenter, goodsName , pageable);

        //处理结果
//      List<ProductCenter> productCenters = result.getContent() ;
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (ProductCenter product :page.getContent()) {
			dealProduct(resultList,product,null);
		}

        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        resultMap.put("resultList", resultList);
        resultMap.put("pageNumber", page.getPageNumber());
        resultMap.put("totalPages", page.getTotalPages());

        return JsonEntity.successMessage(resultMap);
    }

    /**
     * 获取产品库规格商品
     * @param categoryId 栏目id
     * @param goodsName
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/getProductbyCode" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getProduct(Long categoryId , String goodsName , String barCode, Pageable pageable){

        CategoryCenter categoryCenter = categoryCenterService.find(categoryId) ;

        List<ProductCenter> productCenters = productCenterService.findList(categoryCenter, goodsName, barCode);

        //处理结果
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (ProductCenter product :productCenters) {
			dealProduct(resultList,product,null);
		}


        return JsonEntity.successMessage(resultList);
    }
    
    /**
     * 获取分类下的规格
     * 
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/getCategory" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getCategory(Long categoryId){
    	CategoryCenter categoryCenter = categoryCenterService.find(categoryId);
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	if (categoryCenter.getGrade() == 0) {
    		resultMap.put("categoryName1", categoryCenter.getName());
    		resultMap.put("categoryName2", "");
    		resultMap.put("categoryName3", "");
		}
    	if (categoryCenter.getGrade() == 1) {
    		resultMap.put("categoryName1", categoryCenter.getParent().getName());
    		resultMap.put("categoryName2", categoryCenter.getName());
    		resultMap.put("categoryName3", "");
		}
    	if (categoryCenter.getGrade() == 2) {
    		resultMap.put("categoryName1", categoryCenter.getParent().getParent().getName());
    		resultMap.put("categoryName2", categoryCenter.getParent().getName());
        	resultMap.put("categoryName3", categoryCenter.getName());
		}
    	
    	List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
    	for (SpecificationCenter specificationCenter : categoryCenter.getSpecificationCenters()) {
    		Map<String, Object> specification = new HashMap<String, Object>();
    		specification.put("name", specificationCenter.getName());
    		
    		List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
    		for (String option : specificationCenter.getOptions()) {
    			Map<String, Object> optionMap = new HashMap<String, Object>();
    			optionMap.put("option", option);
    			
    			options.add(optionMap);
			}
    		
    		specification.put("options", options);
    		
    		lis.add(specification);
		}
    	
    	resultMap.put("specificationCenters", lis);
    	
    	return JsonEntity.successMessage(resultMap);
    }
    
    public void dealProduct(List<Map<String,Object>> list,ProductCenter product,Integer minOrderQuantity){
		Map<String,Object> productMap=new HashMap<>();
		productMap.put("productId",product.getId());
		productMap.put("specifications",product.getSpecifications());
		productMap.put("minOrderQuantity",minOrderQuantity);
		
		GoodsCenter goods=product.getGoodsCenter();
		for (Map<String,Object> map:list) {
			if (map.get("goodsId").equals(goods.getId())){
				List<Map<String,Object>> products= (List<Map<String, Object>>) map.get("products");
				products.add(productMap);
				return;
			}
		}
		Map<String,Object> goodsMap=new HashMap<>();
		goodsMap.put("categoryId", product.getGoodsCenter().getCategoryCenter().getId());
		goodsMap.put("goodsId",goods.getId());
		goodsMap.put("image",goods.getImage());
		goodsMap.put("name",goods.getName());
		goodsMap.put("labels", goods.getLabels());
		if(product.getSpecifications().size()>0){
			goodsMap.put("hasSpecifications",true);
		}else {
			goodsMap.put("hasSpecifications",false);
		}
		List<Map<String,Object>> products=new ArrayList<>();
		products.add(productMap);
		goodsMap.put("products",products);

		list.add(goodsMap);
	}
}
