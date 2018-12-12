package com.microBusiness.manage.controller.shop.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.SupplierService;

@Controller
@RequestMapping("/shop/member/bindqq")
public class BindQQController extends BaseController {

	@Resource
	private AdminService adminService;
	@Resource
	private SupplierService supplierService;
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(ModelMap model , HttpServletRequest request) {
		Supplier supplier = super.getCurrentSupplier();
		String qq = supplier.getQqCustomerService();
		if(qq == null || qq == "") {
			model.addAttribute("exist", 0);
		}else {
			model.addAttribute("exist", 1);
			model.addAttribute("supplier", supplier);
		}
		model.addAttribute("nav", "bindQQ");
		return "/shop/member/bindqq/index";
	}
	
	/**
	 * 绑定QQ号
	 * @param qqCustomerService QQ号
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public JsonEntity update(String qqCustomerService , HttpServletRequest request) {
		Supplier supplier = super.getCurrentSupplier();
		if(qqCustomerService == null || qqCustomerService == "") {
			return new JsonEntity("101001" , "qq号不能为空");
		}
		if(supplier == null) {
			return new JsonEntity("101001" , "操作错误");
		}
		supplier.setQqCustomerService(qqCustomerService);
		supplierService.update(supplier);
		return JsonEntity.successMessage();
		
	}
	
	/**
	 * 解绑QQ
	 * @param id 
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public String delete(Long id , RedirectAttributes redirectAttributes) {
		Supplier supplier = supplierService.find(id);
		if(supplier == null) {
			return ERROR_VIEW;
		}
		supplier.setQqCustomerService("");
		supplierService.update(supplier);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:index.jhtml";
	}
	
	
	
}
