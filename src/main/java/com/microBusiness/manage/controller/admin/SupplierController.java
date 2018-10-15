package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.RoleService;
import com.microBusiness.manage.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

/**
 * Created by mingbai on 2017/2/10.
 * 功能描述：
 * 修改记录：
 */
@Controller("adminSupplierController")
@RequestMapping("/admin/supplier")
public class SupplierController extends BaseController {

    @Resource
    private SupplierService supplierService ;
    @Resource(name = "adminServiceImpl")
	private AdminService adminService;
    @Resource(name = "roleServiceImpl")
	private RoleService roleService;
    
    @Resource
    private AreaService areaService ;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable,Supplier.Status status,ModelMap model) {
        model.addAttribute("page", supplierService.findPage(pageable,status));
        model.addAttribute("status", status);
        return "/admin/supplier/list";
    }
    
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public String review(ModelMap model,Long id) {
    	Supplier supplier=supplierService.find(id);
    	Set<Admin> admins=supplier.getAdmins();
    	Admin admin=new Admin();
    	Iterator iter = admins.iterator();
    	while (iter.hasNext()) {
    		admin = (Admin) iter.next();
    	}
    	model.addAttribute("supplier" ,supplier);
    	model.addAttribute("admin" ,admin);
        return "/admin/supplier/review";
    }
    @RequestMapping(value = "/look", method = RequestMethod.GET)
    public String look(ModelMap model,Long id) {
    	Supplier supplier=supplierService.find(id);
    	model.addAttribute("supplier" ,supplier);
        return "/admin/supplier/look";
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

        supplierService.update(supplier , adminId , "name","admins","needs","supplyNeeds","goods","orders","userName","tel","area","address","businessCard","imagelogo","email","companyProfile","industry" , "inviteCode","legalPersonName","favorCompany","customerServiceTel","qqCustomerService") ;

    	return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        return "/admin/supplier/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Supplier supplier, Long areaId , RedirectAttributes redirectAttributes) {
        supplier.setArea(areaService.find(areaId));

        if (!isValid(supplier, BaseEntity.Save.class)) {
            return ERROR_VIEW;
        }
        supplier.setStatus(Supplier.Status.notCertified);
        supplierService.save(supplier);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }
    
}
