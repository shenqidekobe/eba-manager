package com.microBusiness.manage.controller.shop;

import javax.annotation.Resource;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.SupplierService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("shopIndexController")
@RequestMapping("/shop")
public class IndexController extends BaseController {

	@Resource
	private CompanyGoodsService companyGoodsService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private AdminService adminService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		CompanyGoods companyGoods = new CompanyGoods();

		Pageable pageable = new Pageable();
		pageable.setPageSize(9);
		companyGoods.setPubType(PubType.pub_supply);
		model.addAttribute("supplyCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, null, null, null, pageable));

		companyGoods.setPubType(PubType.pub_need);
		model.addAttribute("needCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, null, null, null, pageable));

		model.addAttribute("recommendSupplierList", supplierService.recommendSupplierList(20L));

		model.addAttribute("admin", adminService.getCurrent());

		model.addAttribute("type", "index");

		return "/shop/index";
	}
	
	@RequestMapping(value = "/jumpPubType", method = RequestMethod.GET)
	public String jumpPubType(CompanyGoods.PubType pubType, ModelMap model) {
		if (pubType == CompanyGoods.PubType.pub_need) {
			return "/shop/helpcenter/purchase";
		}else {
			return "/shop/helpcenter/supply";
		}
		
	}
	
	@RequestMapping(value = "/jumpAboutMe", method = RequestMethod.GET)
	public String jumpAboutMe(ModelMap model) {
		return "/shop/helpcenter/aboutMe";
		
	}
	
	@RequestMapping(value = "/jumpIntroduce", method = RequestMethod.GET)
	public String jumpIntroduce(ModelMap model) {
		return "/shop/ad/introduce";
	}
	
}
