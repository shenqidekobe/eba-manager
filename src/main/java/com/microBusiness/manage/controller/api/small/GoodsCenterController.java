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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.GoodsCenterService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductCenterService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.util.Code;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/7 下午3:06
 * Describe: 产品库接口
 * Update:
 */
@Controller(value = "apiGoodsCenterController")
@RequestMapping(value = "/api/small/goodsCenter")
public class GoodsCenterController extends BaseController {

    @Resource
    private GoodsCenterService goodsCenterService;

    @Resource
    private CategoryCenterService categoryCenterService;

    @Resource
    private ProductCenterService productCenterService ;

    @Resource
	private ChildMemberService childMemberService;
    
    @Resource
    private ProductCategoryService productCategoryService ;
    
    @Resource
    private SpecificationService specificationService;
    
    /**
     * 获取产品库商品
     *
     * @param categoryId 栏目id
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getGoods(Long categoryId, Pageable pageable) {

        CategoryCenter categoryCenter = categoryCenterService.find(categoryId);

        Page<GoodsCenter> page = goodsCenterService.findPage(null, categoryCenter, true, null, null, null, pageable);

        List<Map<String, Object>> datas = new ArrayList<>();

        List<GoodsCenter> goodsCenters = page.getContent();

        for (GoodsCenter goodsCenter : goodsCenters) {
            Map<String, Object> goodsMap = new HashMap<String, Object>();
            goodsMap.put("goodsId", goodsCenter.getId());
            goodsMap.put("name", goodsCenter.getName());
            goodsMap.put("image", goodsCenter.getImage());
            goodsMap.put("specificationItems", goodsCenter.getSpecificationItems());
            goodsMap.put("productImages", goodsCenter.getProductImages());
            goodsMap.put("categoryId", goodsCenter.getCategoryCenter().getId());

            datas.add(goodsMap);

        }

        Map<String, Object> result = this.dealPage(page.getPageNumber(), page.getTotalPages(), datas);

        return JsonEntity.successMessage(result);
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getOne(Long goodsId) {

        if(null == goodsId){
            return JsonEntity.error(Code.code019998);
        }

        GoodsCenter goodsCenter = goodsCenterService.find(goodsId);

        if(null == goodsCenter){
            return JsonEntity.error(Code.code011401);
        }

        GoodsCenter tempGoodsCenter = new GoodsCenter() ;

        goodsCenterService.copyEntity(goodsCenter , tempGoodsCenter , "categoryCenter" , "productCenters");

        Set<ProductCenter> productCenters = new HashSet<>() ;

        Set<ProductCenter> sourceProducts = goodsCenter.getProductCenters() ;

        for(ProductCenter productCenter : sourceProducts){
            ProductCenter tempProduct = new ProductCenter() ;
            productCenterService.copyEntity(productCenter , tempProduct , "goodsCenter");

            productCenters.add(tempProduct) ;
        }

        tempGoodsCenter.setProductCenters(productCenters);


        CategoryCenter categoryCenter = new CategoryCenter() ;

        CategoryCenter sourceCategoryCenter = goodsCenter.getCategoryCenter() ;

        System.out.println(sourceCategoryCenter.getName());
        categoryCenterService.copyEntity(sourceCategoryCenter , categoryCenter , "parent" , "children");

        tempGoodsCenter.setCategoryCenter(categoryCenter);


        return JsonEntity.successMessage(tempGoodsCenter) ;
    }

    /**
	 * 从资源库选择商品,复制分类
	 * 
	 * @param unionId
	 * @param storageForm
	 * @param storageGoodsForm
	 * @param shopId
	 * @param supplierId
	 * @param supplyNeedId
	 * @param supplierSupplierId
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	public JsonEntity copy(String unionId, Long categoryId, Boolean flag, HttpServletRequest request, HttpServletResponse response) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Map<String, Object> goodsResultMap = new HashMap<>();
		
		if (flag != null) {
			if (flag == true) {
				
				CategoryCenter categoryCenter = categoryCenterService.find(categoryId);
				
				ProductCategory productCategory = productCategoryService.save(categoryCenter, member);
	
				if (productCategory == null) {
					return new JsonEntity(Code.code18804, Code.code18804.getDesc());
				}
				goodsResultMap.put("categoryId", productCategory.getId());
				
			}
		}
		
		return JsonEntity.successMessage(goodsResultMap);
	}
	
	/**
	 * 本地分类是否存在
	 * 
	 * @param id
	 * @param unionId
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value = "/flagBox" , method = RequestMethod.GET)
	 public JsonEntity flagBox(Long categoryId , String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
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
							return JsonEntity.successMessage(resultMap);
						}
						
						resultMap.put("flag", true);
						resultMap.put("productCategoryId", "");
						resultMap.put("productCategoryName", categoryName3);
						return JsonEntity.successMessage(resultMap);
					}
					
					productCategoryService.save(categoryCenter, member);
					
					resultMap.put("flag", false);
					resultMap.put("productCategoryId", twoCategory.getId());
					return JsonEntity.successMessage(resultMap);
				}
				
				resultMap.put("flag", true);
				resultMap.put("productCategoryId", "");
				resultMap.put("productCategoryName", categoryName2);
				return JsonEntity.successMessage(resultMap);
			}
			
			productCategoryService.save(categoryCenter, member);
			
			resultMap.put("flag", false);
			resultMap.put("productCategoryId", oneCategory.getId());
			return JsonEntity.successMessage(resultMap);
			
		}
		
		resultMap.put("flag", true);
		resultMap.put("productCategoryId", "");
		resultMap.put("productCategoryName", categoryName1);
		return JsonEntity.successMessage(resultMap);
	 }

	 @ResponseBody
	 @RequestMapping(value = "/query" , method = RequestMethod.GET)
	 public JsonEntity query(Long goodsId , String unionId) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		 
		GoodsCenter goods = goodsCenterService.find(goodsId);
		Map<String, Object> goodMap = new HashMap<String, Object>();
		goodMap.put("id", goods.getId());
		goodMap.put("name", goods.getName());
		goodMap.put("image", goods.getImage());
		goodMap.put("images", goods.getImages());
		goodMap.put("unit", goods.getUnit());
		goodMap.put("barCode", goods.getBarCode());
		goodMap.put("labels", goods.getLabels());
		goodMap.put("introduction", goods.getIntroduction());
		goodMap.put("specificationItem", goods.getSpecificationItems());
		resultMap.put("goodMap", goodMap);
		Set<ProductCenter> products = goods.getProductCenters();

		List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
		for(final ProductCenter product : products) {
			productList.add(new HashMap<String, Object>(){/**
				 * 
				 */
				private static final long serialVersionUID = 3528498956250623793L;

			{
				this.put("productId", product.getId());
				this.put("barCode", product.getBarCode());
				this.put("cost", product.getCost());
				this.put("price", product.getPrice());
				this.put("specification", product.getSpecificationValues());
			}});
		}
		 
		resultMap.put("productList", productList);
		return JsonEntity.successMessage(resultMap); 
	 }

}
