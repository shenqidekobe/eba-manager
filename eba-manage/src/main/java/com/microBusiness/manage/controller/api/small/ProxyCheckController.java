/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.api.small;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ProxyCheck;
import com.microBusiness.manage.entity.ProxyCheckStatus;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.ProxyCheckService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.Code;

/**
 * 申请代理
 * @author suxiaozhen
 *
 */
@Controller("apiProxyCheckController")
@RequestMapping("/api/small/proxy_check")
public class ProxyCheckController extends BaseController {
	
	@Resource(name = "proxyCheckServiceImpl")
	private ProxyCheckService proxyCheckService;
	
	@Resource(name = "proxyUserServiceImpl")
	private ProxyUserService proxyUserService;
	@Resource
	private AreaService areaService;
	@Resource
	private ChildMemberService childMemberService;
	@Resource
	private SupplierService supplierService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity save(String smOpenId, ProxyCheck proxyCheck, Long parentProxyUserId, Long areaId) {
		if(StringUtils.isEmpty(smOpenId)){
			return JsonEntity.error(Code.code_smOpenId_null_99899);
		}
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("childMember");
		filter.setValue(childMember.getId());
		filters.add(filter);
		List<ProxyCheck> proxyCheckList = proxyCheckService.findList(1, filters, null);
		if(proxyCheckList != null && !proxyCheckList.isEmpty()){
			//return JsonEntity.error(Code.code_public_check_260000);
		}
		proxyCheck.setChildMember(childMember);
		if(parentProxyUserId != null){
			ProxyUser proxyUser = proxyUserService.find(parentProxyUserId);
			proxyCheck.setParentProxyUser(proxyUser);
		}
		if(areaId != null){
			Area area = areaService.find(areaId);
			proxyCheck.setArea(area);
		}
		Supplier supplier = supplierService.find(1l);
		proxyCheck.setSupplier(supplier);
		proxyCheck.setProxyCheckStatus(ProxyCheckStatus.wait);
		proxyCheckService.save(proxyCheck);
		return JsonEntity.successMessage();
	}
	
	@RequestMapping(value = "/getBaseDatas", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getBaseDatas(String smOpenId) {
		if(StringUtils.isEmpty(smOpenId)){
			return JsonEntity.error(Code.code_smOpenId_null_99899);
		}
		//ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("levels", ProxyCheck.Level.values());
		map.put("SourceTypes", ProxyCheck.SourceType.values());
		return JsonEntity.successMessage(map);
	}
	
	



}