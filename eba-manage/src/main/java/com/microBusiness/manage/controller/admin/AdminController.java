/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.*;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Role;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.RoleService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@Resource
	private SupplierService supplierService ;

	@Resource
	private DepartmentService departmentService ;

	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !adminService.usernameExists(username);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Supplier supplier=this.getCurrentSupplier();
		model.addAttribute("roles", supplier.getRoles());
		/*List<Supplier> supplierList = supplierService.findAll();
		model.addAttribute("suppliers" , supplierList) ;*/

		model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));

		return "/admin/admin/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes , Long departmentId) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));

		admin.setSupplier(super.getCurrentSupplier());

		admin.setDepartment(departmentService.find(departmentId));

		if (!isValid(admin, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (adminService.usernameExists(admin.getUsername())) {
			return ERROR_VIEW;
		}
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		admin.setLockKey(null);
		adminService.save(admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Supplier supplier=this.getCurrentSupplier();
		model.addAttribute("roles",  supplier.getRoles());
		model.addAttribute("admin", adminService.find(id));
		model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
		return "/admin/admin/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes , Long departmentId) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		admin.setDepartment(departmentService.find(departmentId));
		if (!isValid(admin)) {
			return ERROR_VIEW;
		}
		Admin pAdmin = adminService.find(admin.getId());
		if (pAdmin == null) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(admin.getPassword())) {
			admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		} else {
			admin.setPassword(pAdmin.getPassword());
		}
		if (pAdmin.getIsLocked() && !admin.getIsLocked()) {
			admin.setLoginFailureCount(0);
			admin.setLockedDate(null);
		} else {
			admin.setIsLocked(pAdmin.getIsLocked());
			admin.setLoginFailureCount(pAdmin.getLoginFailureCount());
			admin.setLockedDate(pAdmin.getLockedDate());
		}
		adminService.update(admin, "username", "loginDate", "loginIp", "lockKey");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model,String searchValue) {
		model.addAttribute("page", adminService.findPage(searchValue,pageable,super.getCurrentSupplier(),adminService.getCurrent().getId()));
		return "/admin/admin/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
    Message delete(Long[] ids) {
		if (ids.length >= adminService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		adminService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 *
	 * @param bindPhoneNum
	 * @return
	 */
	@RequestMapping(value = "/telIsBind", method = RequestMethod.GET)
	@ResponseBody
	public boolean telIsBind(String bindPhoneNum){

		Admin admin = adminService.findBybindPhoneNum(bindPhoneNum);
		if(null == admin){
			return true ;
		}
		return false ;
	}
	
	/**
	 *
	 * @param bindPhoneNum
	 * @return
	 */
	@RequestMapping(value = "/getListByDepartment", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getListByDepartment(Long departmentId){
		List<Map<String, Object>> list=new ArrayList<>();
		Department department=departmentService.find(departmentId);
		if (department != null) {
			List<Admin> admins=adminService.getListByDepartment(department,this.getCurrentSupplier());
			for (Admin admin : admins) {
				Map<String, Object> map=new HashMap<>();
				map.put("id", admin.getId());
				map.put("name", admin.getName());
				list.add(map);
			}
		}
		return JsonEntity.successMessage(list);
	}

}