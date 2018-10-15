package com.microBusiness.manage.controller.admin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.SupplierService;

/**
 * 企业认证
 * @author Administrator
 *
 */
@Controller("certificationController")
@RequestMapping("/admin/certification")
public class CertificationController extends BaseController{
	
		@Resource
	    private SupplierService supplierService ;
	    @Resource
	    private AreaService areaService ;
	    @Resource
		private AdminService adminService ;
	    
	
	   @RequestMapping(value = "/index", method = RequestMethod.GET)
	    public String supplierCertification(ModelMap model){
		   	 model.addAttribute("supplier" , adminService.getCurrent().getSupplier());
		   	 model.addAttribute("industrys" , Supplier.Industry.values());
	    	 return "/admin/supplierCertification/index";
	    }
	   
	   @ResponseBody
	   @RequestMapping(value = "/update", method = RequestMethod.POST)
	    public Map<String, Object> update(Supplier supplier,Long areaId){
		   	supplier.setArea(areaService.find(areaId));
		   	supplier.setStatus(Supplier.Status.certification);
		   	supplierService.update(supplier, "name","admins","needs","supplyNeeds","goods","orders","email","inviteCode","types");
		   	Map<String, Object> model=new HashMap<>();
			model.put("isSuccess", true);
			return model;
	    }
	   
	   @RequestMapping(value = "/reviewing", method = RequestMethod.GET)
	   public String reviewing(){
		  return "/admin/supplierCertification/reviewing";
	   }
	   
	   @RequestMapping(value = "/reviewMiss", method = RequestMethod.GET)
	   public String reviewMiss(ModelMap model){
		   model.addAttribute("supplier" , adminService.getCurrent().getSupplier());
		  return "/admin/supplierCertification/reviewMiss";
	   }
	   
	   /*
	    * 修改时判断判断企业名称是否重复
	    */
	   @ResponseBody
	   @RequestMapping(value="/nameExists",method = RequestMethod.GET)
	   public Map<String, Object> nameExists(Supplier supplier) {
		   Map<String, Object> model = new HashMap<>();
		   model.put("exists", supplierService.existaName(supplier.getName(), supplier.getId()));
		   return model;
	   }
}
