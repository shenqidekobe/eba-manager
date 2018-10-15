package com.microBusiness.manage.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.SupplierService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 新增企业信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/enterpriseInfo")
public class EnterpriseInfoController {

	@Resource
	private AdminService adminService;
	
	@Resource
	private AreaService areaService;
	
	@Resource
	private SupplierService supplierService;
	/**
     * 企业信息
     * @param model
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String supplierEnterpriseInfo(ModelMap model) {
    	 model.addAttribute("supplier" , adminService.getCurrent().getSupplier());
	   	 model.addAttribute("industrys" , Supplier.Industry.values());
    	 return "/admin/enterpriseInfo/index";
    }
    
    /**
     * 更新企业信息
     * @param supplier 企业信息实体类
     * @param areaId 当前企业id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateEnterpriseInfo",method = RequestMethod.POST)
    public Map<String, Object> updateEnterpriseInfo(Supplier supplier,Long areaId) {
    	supplier.setArea(areaService.find(areaId));
    	supplierService.update(supplier, "admins","needs","supplyNeeds","goods","orders","businessCard","status","inviteCode","name","appId","appKey");
	   	Map<String, Object> model=new HashMap<>();
	   	model.put("isSuccess", true);
	   	return model;
    }
    
    
    /*
     * 修改时判断企业用户名是否重复
     */
    @ResponseBody
    @RequestMapping(value="/nameExists",method = RequestMethod.GET)
    public Map<String, Object> nameExists(Supplier supplier) {
    	Map<String, Object> model = new HashMap<>();
    	model.put("exists", supplierService.existaName(supplier.getName(), supplier.getId()));
    	return model;
    }
}
