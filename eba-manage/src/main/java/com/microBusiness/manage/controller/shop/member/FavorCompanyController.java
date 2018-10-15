package com.microBusiness.manage.controller.shop.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyService;
import com.microBusiness.manage.service.SupplierService;

/**
 * 收藏企业Controller
 * @author yuezhiwei
 *
 */
@Controller("shopMemberFavorCompany")
@RequestMapping("/shop/member/favorCompany")
public class FavorCompanyController extends BaseController {

	@Resource
	private FavorCompanyService favorCompanyService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private CompanyGoodsService companyGoodsService;
	@Resource
	private AdminService adminService;
	
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(Pageable pageable , HttpServletRequest request, ModelMap model) {
		Admin admin = this.getCurrentAdmin(request);
		if(admin == null || admin.getId() == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("nav", "favorSupplier");
		model.addAttribute("page", favorCompanyService.findPage(admin.getId(), FavorCompany.Delflag.delflag_no, pageable));
		return "/shop/member/favorCompany/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public Message delete(Long id ,HttpServletRequest request) { 
		Admin admin = this.getCurrentAdmin(request);
		favorCompanyService.updateDelflag(id, admin.getId(), FavorCompany.Delflag.delflag_had);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/supplierLook" , method = RequestMethod.GET)
	public String supplierLook(Long id , HttpServletRequest request , ModelMap model) {
		//  企业信息
		model.addAttribute("supplier" ,supplierService.find(id));

		CompanyGoods companyGoods = new CompanyGoods();
		Pageable pageable = new Pageable();
		pageable.setPageSize(6);
		companyGoods.setPubType(PubType.pub_supply);
		
		Supplier supplier = new Supplier();
		supplier.setId(id);
		
		model.addAttribute("supplierId", id);
		
		if (this.getCurrentAdmin(request) != null) {
			Supplier supplierNew = this.getCurrentAdmin(request).getSupplier();
			if (supplierNew != null) {
				model.addAttribute("favorCompanyL", favorCompanyService.getFavorCompany(supplierNew));
			}
		}
		
		// 供应产品
		companyGoods.setSupplier(supplier);
		model.addAttribute("supplyCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, null, null, null, pageable));
		
		// 采购产品
		companyGoods.setPubType(PubType.pub_need);
		model.addAttribute("needCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, null, null, null, pageable));
		return "/shop/supplier/details";
	}
	
}
