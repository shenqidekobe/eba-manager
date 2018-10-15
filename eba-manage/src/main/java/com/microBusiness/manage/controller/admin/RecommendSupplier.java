package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller("recommendSupplier")
@RequestMapping("/admin/adSupplier")
public class RecommendSupplier extends BaseController {

    @Resource
    private SupplierService supplierService ;
    @Resource(name = "adminServiceImpl")
	private AdminService adminService;

    @RequestMapping(value = "/recommendlist", method = RequestMethod.GET)
    public String recommendlist(Pageable pageable,Supplier.Status status,ModelMap model) {
        model.addAttribute("page", supplierService.findPage(pageable,status));
        model.addAttribute("status", status);
        model.addAttribute("statusEnum", Supplier.Status.values());
        return "/admin/supplier/recommendlist";
    }
    
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public @ResponseBody Message recommend(Long[] ids, Long[] idsNo, ModelMap model) {
    	if (ids.length == 0) {
    		return ERROR_MESSAGE;
		}
    	if (idsNo.length == 0) {
    		return ERROR_MESSAGE;
		}
    	
    	Boolean flag = supplierService.updateSupplier(ids, idsNo);
    	if (!flag) {
    		return ERROR_MESSAGE;
		}
    	return SUCCESS_MESSAGE;
    }
}
