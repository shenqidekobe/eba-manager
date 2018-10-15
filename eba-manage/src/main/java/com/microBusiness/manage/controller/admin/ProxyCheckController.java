/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ProxyCheck;
import com.microBusiness.manage.entity.ProxyCheckStatus;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.service.ProxyCheckService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.util.DateUtils;

@Controller("adminProxyCheckController")
@RequestMapping("/admin/proxy_check")
public class ProxyCheckController extends BaseController {

	@Resource(name = "proxyCheckServiceImpl")
	private ProxyCheckService proxyCheckService;
	
	@Resource(name = "proxyUserServiceImpl")
	private ProxyUserService proxyUserService;
	
	

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/proxy_check/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProxyCheck proxyCheck, RedirectAttributes redirectAttributes) {
		if (!isValid(proxyCheck)) {
			return ERROR_VIEW;
		}
		proxyCheckService.save(proxyCheck);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("proxyCheck", proxyCheckService.find(id));
		return "/admin/proxy_check/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProxyCheck proxyCheck, RedirectAttributes redirectAttributes) {
		if (!isValid(proxyCheck)) {
			return ERROR_VIEW;
		}
		proxyCheckService.update(proxyCheck);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, Long proxyUserId, ProxyCheckStatus proxyCheckStatus, Date startDate , Date endDate , String searchName , String timeSearch, ModelMap model) {
		ProxyUser proxyUser = proxyUserService.find(proxyUserId);
		model.addAttribute("proxyUserTree", proxyUserService.findTree(super.getCurrentSupplier() , null));
		model.addAttribute("proxyUser", proxyUser);
		List<Filter> filters = new ArrayList<Filter>();
		if(proxyCheckStatus != null){
			Filter filter = new Filter();
			filter.setOperator(Operator.eq);
			filter.setProperty("proxyCheckStatus");
			filter.setValue(proxyCheckStatus);
			filters.add(filter);
		}
		if(StringUtils.isNotEmpty(searchName)){
			Filter filter = new Filter();
			filter.setOperator(Operator.like);
			filter.setProperty("name");
			filter.setValue("%" + searchName + "%");
			filters.add(filter);
		}
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		if(StringUtils.isBlank(timeSearch)){
			timeSearch = "createDate";
		}
		
		List<Order> orders = new ArrayList<Order>();
		Order order = new Order();
		order.setDirection(Direction.desc);
		order.setProperty("createDate");
		orders.add(order);
		pageable.setOrders(orders);
		pageable.setFilters(filters);
		model.addAttribute("page", proxyCheckService.findPage(pageable, timeSearch, startDate, endDate));
		model.addAttribute("proxyCheckStatus", proxyCheckStatus);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("timeSearch", timeSearch);
		model.addAttribute("searchName", searchName);
		return "/admin/proxy_check/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		proxyCheckService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Message check(ProxyCheck proxyCheck, Long[] ids){
		for(int i = 0 ; i < ids.length; i++) {
			proxyCheck = proxyCheckService.find(ids[i]);
			if(!proxyCheck.getProxyCheckStatus().equals(ProxyCheckStatus.wait)){
				continue;
			}
			proxyCheckService.check(proxyCheck);
		}
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/detailCheck", method = RequestMethod.POST)
	public String detailCheck(Long id, RedirectAttributes redirectAttributes){
		ProxyCheck proxyCheck = proxyCheckService.find(id);
		if(proxyCheck.getProxyCheckStatus().equals(ProxyCheckStatus.wait)){
			proxyCheckService.check(proxyCheck);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	

}