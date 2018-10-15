package com.microBusiness.manage.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.OrderSetting;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.OrderSettingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("orderSettingController")
@RequestMapping("/admin/orderSetting")
public class OrderSettingController extends BaseController {

	@Resource(name = "orderSettingServiceImpl")
	private OrderSettingService orderSettingService;
	
	@Resource
	private AdminService adminService;
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Supplier supplier = super.getCurrentSupplier();
		if(supplier == null) {
			return ERROR_VIEW;
		}
		OrderSetting orderSetting = orderSettingService.findBySupplier(supplier.getId());
		if(orderSetting != null) {
			modelMap.addAttribute("orderSetting", orderSetting);
		}
		return "/admin/orderSetting/index";
	}
	
	/**
	 * 更新订单设置
	 * @param orderSetting 订单设置实体类
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public String update(Long id , OrderSetting orderSetting , RedirectAttributes redirectAttributes) {
		if(orderSetting == null ||orderSetting.getStartTime() == null || orderSetting.getEndTime() == null ) {
			return ERROR_VIEW;
		}
		/*
		 * 如果id不存在，证明所在企业不存在订单设置记录，直接创建一条
		 */
		if(id == null) {
			Supplier supplier = super.getCurrentSupplier();
			orderSetting.setSupplier(supplier);
			orderSettingService.save(orderSetting);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return "redirect:list.jhtml";
		}
		//如果存在订单设置记录，就修改
		orderSettingService.update(orderSetting, "supplier");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:index.jhtml";
	}
}
