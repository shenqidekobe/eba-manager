package com.microBusiness.manage.controller.shop.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.SupplierService;

@Controller("shopCertification")
@RequestMapping("/shop/member/certification")
public class CertificationController extends BaseController {

	@Resource
	private AdminService adminService ;
	@Resource
    private SupplierService supplierService ;
    @Resource
    private AreaService areaService ;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String supplierCertification(ModelMap model ,HttpServletRequest request){
		 Admin admin = this.getCurrentAdmin(request);
		 if(admin == null) {
			 return ERROR_VIEW ;
		 }
		 Supplier supplier = admin.getSupplier();
		 if(supplier.getStatus() == Supplier.Status.notCertified) {//未认证
			 model.addAttribute("supplier" , supplier);
		   	 model.addAttribute("industrys" , Supplier.Industry.values());
		   	 model.addAttribute("nav", "certification");
		   	 return "/shop/member/supplierCertification/index";
		 }else if(supplier.getStatus() == Supplier.Status.authenticationFailed) {//认证失败
			 model.addAttribute("supplier" , supplier);
		   	 model.addAttribute("industrys" , Supplier.Industry.values());
		   	 model.addAttribute("exist", 0);
		   	 model.addAttribute("nav", "certification");
		   	 return "/shop/member/supplierCertification/index";
		 }else {
			 model.addAttribute("nav", "certification");
			 model.addAttribute("supplier" , supplier);
			 return "/shop/member/supplierCertification/reviewing";
		 }
    }
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(Supplier supplier,Long areaId){
	   	supplier.setArea(areaService.find(areaId));
	   	supplier.setStatus(Supplier.Status.certification);
	   	supplierService.update(supplier ,"admins","needs","supplyNeeds","goods","orders","email","inviteCode","favorCompany");
	   	Map<String, Object> model=new HashMap<>();
		model.put("isSuccess", true);
		return model;
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
