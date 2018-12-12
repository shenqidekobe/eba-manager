package com.microBusiness.manage.controller.shop;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyService;

/**
 * 
 * 商流供应商Controller
 *
 */
@Controller("CompanyGoodsController")
@RequestMapping("/shop/companyGoods")
public class CompanyGoodsController extends BaseController {

	@Resource
	private CompanyGoodsService companyGoodsService;
	@Resource
	private FavorCompanyGoodsService favorCompanyGoodsService;
	@Resource
	private FavorCompanyService favorCompanyService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping(value = "/getCompanyGoodsList", method = RequestMethod.GET)
	public String getCompanyGoodsList(CompanyGoods companyGoods, Long categoryId, String categoryOneId, String categoryTwoId, String categoryThreeId, Long supplierId, Boolean priceBoolean, Boolean popularityBoolean, Boolean packagesNumBoolean, Pageable pageable, HttpServletRequest request, ModelMap model) {

		if (categoryId != null) {
			Category category = new Category();
			category.setId(categoryId);
			companyGoods.setCategory(category);
		}
		if (supplierId != null) {
			Supplier supplier = new Supplier();
			supplier.setId(supplierId);
			companyGoods.setSupplier(supplier);
		}
		
		model.addAttribute("name", companyGoods.getName());
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryOneId", categoryOneId);
		model.addAttribute("categoryTwoId", categoryTwoId);
		model.addAttribute("categoryThreeId", categoryThreeId);
		
		model.addAttribute("supplierId", supplierId);

		model.addAttribute("priceBoolean", priceBoolean);
		model.addAttribute("popularityBoolean", popularityBoolean);
		model.addAttribute("packagesNumBoolean", packagesNumBoolean);
		
		if (this.getCurrentAdmin(request) != null) {
			Supplier supplier = this.getCurrentAdmin(request).getSupplier();
			if (supplier != null) {
				model.addAttribute("favorCompanyL", favorCompanyService.getFavorCompany(supplier));
			}
		}
		
		if (companyGoods.getPubType() == CompanyGoods.PubType.pub_supply) {
			companyGoods.setPubType(PubType.pub_supply);
			model.addAttribute("type", "pub_supply");
			model.addAttribute("page", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, priceBoolean, popularityBoolean, packagesNumBoolean, pageable));
			return "/shop/supplyGoods/list";
		}else {
			companyGoods.setPubType(PubType.pub_need);
			model.addAttribute("type", "pub_need");
			model.addAttribute("page", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(),companyGoods, priceBoolean, popularityBoolean, packagesNumBoolean, pageable));
			return "/shop/needGoods/list";
		}

	}

	@RequestMapping(value = "/look", method = RequestMethod.GET)
    public String look(ModelMap model,Long id) {
		CompanyGoods companyGoods= companyGoodsService.find(id);
    	model.addAttribute("companyGoods" ,companyGoods);
        return "/shop/companyGoods/look";
    }

	@RequestMapping(value = "/companyGoodsLook", method = RequestMethod.GET)
    public String companyGoodsLook(ModelMap model, Long id, PubType pubType, HttpServletRequest request) {

		CompanyGoods companyGoodsOld = companyGoodsService.findById(id, adminService.getCurrent());

		// 产品信息
		model.addAttribute("companyGoods" ,companyGoodsOld);

		// 更新人气
		companyGoodsService.updateCompanyGoods(id);

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
				if (PubType.pub_supply == pubType) {
					model.addAttribute("favorCompanyGoodsL", favorCompanyGoodsService.getFavorCompanyGoods(supplierNew));
				}
			}
		}
		
		if (PubType.pub_supply == pubType) {
			// 供应产品
			model.addAttribute("supplyCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(), companyGoods, null, null, null, pageable));
			
			return "/shop/supplyGoods/details";
		}else {
			// 采购产品
			model.addAttribute("needCompanyGoodsList", companyGoodsService.getCompanyGoodsList(adminService.getCurrent(), companyGoods, null, null, null, pageable));
			return "/shop/needGoods/details";
		}

    }
}
