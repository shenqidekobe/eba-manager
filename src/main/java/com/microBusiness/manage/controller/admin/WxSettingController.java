package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;

/**
 * 微信端后台设置
 * @author yuezhiwei
 *
 */
@Controller("wxSettingController")
@RequestMapping("/admin/wxSetting")
public class WxSettingController extends BaseController{
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("admin", adminService.getCurrent());
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/wxSetting/index";
	}
	
}
