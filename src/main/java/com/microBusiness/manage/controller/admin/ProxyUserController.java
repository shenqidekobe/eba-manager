/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.

 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;


import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.ProxyUserService;

/**
 * 代理
 * @author 
 *
 */
@Controller("adminProxyUserController")
@RequestMapping("/admin/proxy_user")
public class ProxyUserController extends BaseController {

	@Resource(name = "proxyUserServiceImpl")
	private ProxyUserService proxyUserService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("proxyUserTree", proxyUserService.findTreeList(super.getCurrentSupplier() , null));
		return "/admin/proxy_user/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProxyUser proxyUser, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		proxyUser.setParent(proxyUserService.find(parentId));
		if (!isValid(proxyUser)) {
			return ERROR_VIEW;
		}
		proxyUser.setSupplier(getCurrentSupplier());
		proxyUser.setTreePath(null);
		proxyUser.setGrade(null);
		proxyUser.setChildren(null);
		proxyUserService.save(proxyUser);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ProxyUser proxyUser = proxyUserService.find(id);
		model.addAttribute("proxyUserTree", proxyUserService.findTree(super.getCurrentSupplier() , null));
		model.addAttribute("proxyUser", proxyUser);
		model.addAttribute("children", proxyUserService.findChildren(proxyUser, true, null));
		return "/admin/proxy_user/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProxyUser proxyUser, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		proxyUser.setParent(proxyUserService.find(parentId));
		if (!isValid(proxyUser)) {
			return ERROR_VIEW;
		}
		if (proxyUser.getParent() != null) {
			ProxyUser parent = proxyUser.getParent();
			if (parent.equals(proxyUser)) {
				return ERROR_VIEW;
			}
			List<ProxyUser> children = proxyUserService.findChildren(parent, true, null);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		proxyUserService.update(proxyUser, "treePath", "grade", "children", "goods", "parameters", "attributes", "specifications","supplier","types");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model , String searchName) {
		//model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("searchName", searchName);
		List<ProxyUser> list = proxyUserService.findTree(super.getCurrentSupplier() , searchName);
		model.addAttribute("proxyUserTree", list);
		int totalCount = list.size();
		model.addAttribute("totalCount", totalCount);
		int no1Count = 0;
		int no2Count = 0;
		int no3Count = 0;
		if(list != null){
			for (ProxyUser proxyUser : list) {
				if(proxyUser.getGrade() == 0){
					no1Count++;
					List pList = proxyUserService.findChildren(proxyUser, true, 999999999);
					proxyUser.setGroupCount(pList.size());
				}
				if(proxyUser.getGrade() == 1){
					no2Count++;
					proxyUser.setGroupCount(proxyUser.getChildren().size());
				}
				if(proxyUser.getGrade() == 2){
					no3Count++;
					proxyUser.setGroupCount(0);
				}
			}
		}
		model.addAttribute("no1Count", no1Count);
		model.addAttribute("no2Count", no2Count);
		model.addAttribute("no3Count", no3Count);
		
		return "/admin/proxy_user/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ProxyUser proxyUser = proxyUserService.find(id);
		if (proxyUser == null) {
			return ERROR_MESSAGE;
		}
		Set<ProxyUser> children = proxyUser.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.proxyUser.deleteExistChildrenNotAllowed");
		}
		proxyUserService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/getProxyUser" , method = RequestMethod.GET)
	@ResponseBody
	public boolean getProxyUser(String name, String oldName, Long parentId) {
		if (name.equals(oldName)) {
			return true;
		}
		List<ProxyUser> list = proxyUserService.findByParent(super.getCurrentSupplier(), proxyUserService.find(parentId), name);
		if (list.size() > 0) {
			return false;
		}else {
			return true;
		}
	}

}