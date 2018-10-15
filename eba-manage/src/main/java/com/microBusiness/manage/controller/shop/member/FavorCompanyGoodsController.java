package com.microBusiness.manage.controller.shop.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyService;

/**
 * 收藏产品Controller
 * @author Administrator
 *
 */
@Controller("shopMemberFavorCompanyGoods")
@RequestMapping("/shop/member/favorCompanyGoods")
public class FavorCompanyGoodsController extends BaseController {
	
	@Resource
	private FavorCompanyGoodsService favorCompanyGoodsService;
	
	@Resource
	private CompanyGoodsService companyGoodsService;
	
	@Resource
	private FavorCompanyService favorCompanyService;
	
	@Resource
	private AdminService adminService;
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(Pageable pageable , ModelMap model , HttpServletRequest request) {
		Admin admin = super.getCurrentAdmin(request);
		if(admin == null || admin.getId() == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("nav", "favorGood");
		model.addAttribute("page", favorCompanyGoodsService.findPage(pageable, admin.getId(), Delflag.delflag_no));
		return "/shop/member/favorCompanyGoods/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public com.microBusiness.manage.Message delete(Long companyGoodsId , HttpServletRequest request) {
		Admin admin = super.getCurrentAdmin(request);
		if(companyGoodsId == null || admin.getId() == null) {
			return ERROR_MESSAGE;
		}
		favorCompanyGoodsService.updateDelflag(admin.getId(), companyGoodsId, Delflag.delflag_had);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/companyGoodsLook", method = RequestMethod.GET)
    public String companyGoodsLook(ModelMap model, Long companyGoodsId, HttpServletRequest request) {

		CompanyGoods companyGoodsOld = companyGoodsService.findById(companyGoodsId, adminService.getCurrent());

		// 产品信息
		model.addAttribute("companyGoods" ,companyGoodsOld);

		// 更新人气
		//companyGoodsService.updateCompanyGoods(companyGoodsId);

		Pageable pageable = new Pageable();
		pageable.setPageSize(4);

		CompanyGoods companyGoods = new CompanyGoods();
		companyGoods.setPubType(companyGoodsOld.getPubType());
		companyGoods.setSupplier(companyGoodsOld.getSupplier());
		companyGoods.setId(companyGoodsOld.getId());

		// 收藏企业和收藏供应
		if (this.getCurrentAdmin(request) != null) {
			Supplier supplierNew = this.getCurrentAdmin(request).getSupplier();
			if (supplierNew != null) {
				model.addAttribute("favorCompanyL", favorCompanyService.getFavorCompany(supplierNew));
				model.addAttribute("favorCompanyGoodsL", favorCompanyGoodsService.getFavorCompanyGoods(supplierNew));
				/*if (PubType.pub_supply == pubType) {
					model.addAttribute("favorCompanyGoodsL", favorCompanyGoodsService.getFavorCompanyGoods(supplierNew));
				}*/
			}
		}
		// 供应产品
		model.addAttribute("supplyCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(), companyGoods, null, null, null, pageable));
		
		return "/shop/supplyGoods/details";
		
		/*if (PubType.pub_supply == pubType) {
			// 供应产品
			model.addAttribute("supplyCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(), companyGoods, null, null, null, pageable));
			
			return "/shop/supplyGoods/details";
		}else {
			// 采购产品
			model.addAttribute("needCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(), companyGoods, null, null, null, pageable));
			return "/shop/needGoods/details";
		}*/

    }
	
}
