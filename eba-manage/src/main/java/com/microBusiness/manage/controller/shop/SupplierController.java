package com.microBusiness.manage.controller.shop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyService;
import com.microBusiness.manage.service.RoleService;
import com.microBusiness.manage.service.SupplierService;

/**
 * 
 * 商流供应商Controller
 * 
 */
@Controller("ShopSupplierController")
@RequestMapping("/shop/supplie")
public class SupplierController extends BaseController {

	@Resource
	private SupplierService supplierService;
	@Resource
	private FavorCompanyService favorCompanyService;
	@Resource
	private CompanyGoodsService companyGoodsService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@Resource
	private AreaService areaService ;

	@RequestMapping(value = "/getSupplierList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSupplierList(Pageable pageable, String name, Supplier.Status status, HttpServletRequest request, ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageSupplier", supplierService.findPage(adminService.getCurrent(), pageable, name, status));
		
		return data;
	}
	
	@RequestMapping(value = "/jumpSupplierList", method = RequestMethod.GET)
	public String jumpSupplierList(Pageable pageable, String name, Supplier.Status status, HttpServletRequest request, ModelMap model) {
		Page<Supplier> sup = supplierService.findPage(adminService.getCurrent(), pageable, name, status);
		model.addAttribute("page", sup);
		model.addAttribute("name", name);
		model.addAttribute("status", status);
		model.addAttribute("type", "supplier");
		
		if (this.getCurrentAdmin(request) != null) {
			Supplier supplier = this.getCurrentAdmin(request).getSupplier();
			if (supplier != null) {
				model.addAttribute("favorCompanyL", favorCompanyService.getFavorCompany(supplier));
			}
		}
		
		return "/shop/supplier/supplierList";
	}
	
	@RequestMapping(value = "/recommendSupplierList", method = RequestMethod.GET)
	public String recommendSupplierList(Long count, ModelMap model) {
		model.addAttribute("supplierList", supplierService.recommendSupplierList(count));
		return "/shop/";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable,Supplier.Status status,ModelMap model) {
        model.addAttribute("page", supplierService.findPage(pageable,status));
        model.addAttribute("status", status);
        return "/shop/supplier/list";
    }
	
	@RequestMapping(value = "/look", method = RequestMethod.GET)
    public String look(ModelMap model,Long id) {
    	Supplier supplier=supplierService.find(id);
    	model.addAttribute("supplier" ,supplier);
        return "/shop/supplier/look";
    }
	
	@RequestMapping(value = "/review", method = RequestMethod.GET)
    public String review(ModelMap model,Long id) {
    	Supplier supplier=supplierService.find(id);
    	Set<Admin> admins=supplier.getAdmins();
    	Admin admin=new Admin();
    	Iterator<Admin> iter = admins.iterator();
    	while (iter.hasNext()) {
    		admin = (Admin) iter.next();
    	}
    	model.addAttribute("supplier" ,supplier);
    	model.addAttribute("admin" ,admin);
        return "/shop/supplier/review";
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(Supplier supplier,Long adminId) {
        // FIXME: 2017/3/14 需要将提前到业务中，否则事务无法回滚，将预定义参数进行配置，不要写在程序中
        /*supplierService.update(supplier, "name","admins","needs","supplyNeeds","goods","orders","userName","tel","area","address","businessCard");
    	if(supplier.getStatus() == Supplier.Status.verified){
    		Long[] roleIds={5L};
        	Admin admin=new Admin();
            admin.setId(adminId);
            admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
            adminService.update(admin,"username","password","email","name","department","isEnabled","isLocked","loginFailureCount","lockedDate","loginDate","loginIp","lockKey","supplier");
    	}*/

        supplierService.update(supplier , adminId , "name","admins","needs","supplyNeeds","goods","orders","userName","tel","area","address","businessCard","imagelogo","email","companyProfile","industry" , "inviteCode") ;

    	return "redirect:list.jhtml";
    }

	/**
	 * 收藏企业
	 * 
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/favorCompany", method = RequestMethod.POST)
	public @ResponseBody Message favorCompany(Long supplierId) {
		if (supplierId == null) {
			return ERROR_MESSAGE;
		}

		FavorCompany favorCompany = favorCompanyService.favorCompany(adminService.getCurrent(), supplierId);
		if (favorCompany == null) {
			return ERROR_MESSAGE;
		}
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 企业详细
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/supplierLook", method = RequestMethod.GET)
    public String supplierLook(ModelMap model, Long id, HttpServletRequest request) {
    	
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
