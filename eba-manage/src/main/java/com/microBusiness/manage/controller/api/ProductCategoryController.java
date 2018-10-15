package com.microBusiness.manage.controller.api;

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

import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.SupplierService;

/**
 * Created by mingbai on 2017/3/29.
 * 功能描述：商品分类接口
 * 修改记录：
 */
@Controller
@RequestMapping(value = "/api/category")
public class ProductCategoryController extends BaseController {

    @Resource
    private ProductCategoryService productCategoryService ;
    @Resource
    private SupplierDao supplierDao ;
    @Resource
    private SupplierService supplierService;
    @Resource
    private NeedService needService;

    @RequestMapping(value = "/get" , params="supplyType=formal" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getFormal(Long supplierId , HttpServletRequest request , HttpServletResponse response){

        Member member = super.getUserInfo(request) ;
        
    	//获取个体客户关系
		Supplier sup = supplierService.find(supplierId);
		Need need = needService.findNeedByMemberSupplier(sup, member);


        Supplier supplier = supplierDao.find(supplierId);

        List<ProductCategory> productCategories = productCategoryService.findByFormal(need , null);

        return JsonEntity.successMessage(this.dealResult(productCategories));
    }

    @RequestMapping(value = "/get" , params="supplyType=temporary" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getTemporary(Long supplierId , HttpServletRequest request , HttpServletResponse response){

        Member member = super.getUserInfo(request) ;
        
        //获取个体客户关系
      	Supplier sup = supplierService.find(supplierId);
      	Need need = needService.findNeedByMemberSupplier(sup, member);


        Supplier supplier = supplierDao.find(supplierId);

        List<ProductCategory> productCategories = productCategoryService.findByTemporary(need , null);

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
}
