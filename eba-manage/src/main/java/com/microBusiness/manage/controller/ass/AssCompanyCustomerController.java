package com.microBusiness.manage.controller.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.SourceType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;
import com.microBusiness.manage.util.Code;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 企业客户
 * 
 */
@Controller("assCompanyCustomerController")
@RequestMapping("/ass/company")
public class AssCompanyCustomerController extends BaseController {

	@Resource
	private SupplierService supplierService;
	@Resource
	private CustomerRelationService relationService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource
	private AssChildMemberService assChildMemberService;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;
	
	/**
	 * 根据企业邀请码查询企业信息
	 * @param inviteCode
	 * @return
	 */
	@RequestMapping(value="/findBySupplierToCode" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity findBySupplierToCode(String inviteCode, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map=new HashMap<>();
		if(!supplierService.inviteCodeExists(inviteCode)) {
			map.put("exist", 0);
			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
		}
		Supplier supplier = supplierService.getSupplierByInviteCode(inviteCode);
		map.put("exist", "1");
		map.put("name", supplier.getName());
		map.put("id", supplier.getId());
		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
	}

	/**
	 * 新建客户
	 * @param customerRelation
	 * @param areaId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity save(CustomerRelation customerRelation, Long customerId, Long adminId, String inviteCode , ModelMap model , RedirectAttributes redirectAttributes) {
		if(customerId == null || adminId == null) {
			 return new JsonEntity("010502" , "参数错误");
		}
		Supplier supplier = adminService.find(adminId).getSupplier();
		
		if(relationService.inviteCodeExists(inviteCode , supplier)) {
			return JsonEntity.error(Code.code17801, Code.code17801.getDesc());
		}
		if(supplier.getInviteCode().equalsIgnoreCase(inviteCode)) {
			return JsonEntity.error(Code.code17802, Code.code17802.getDesc());
		}

		customerRelation.setSupplier(supplier);
		customerRelation.setBySupplier(supplierService.find(customerId));
		customerRelation.setClientName(customerRelation.getClientName());
		customerRelation.setSupplierUserName(supplier.getUserName());
		customerRelation.setSupplierTel(supplier.getTel());
		customerRelation.setSourceType(SourceType.small);
		customerRelation.setAdmin(adminService.find(adminId));
		CustomerRelation relation = relationService.save(customerRelation);
		
		//添加更新标志
		List<AssChildMember> assChildMembers = assChildMemberService.findList(relation);
		for(AssChildMember assChildMember : assChildMembers) {
			AssUpdateTips updateTips = new AssUpdateTips();
			updateTips.setAssChildMember(assChildMember);
			updateTips.setType(AssUpdateTips.Type.businessCustomers);
			updateTips.setCustomerRelation(relation);
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
			assUpdateTipsService.save(updateTips);
		}
		
		Map<String, Object> map=new HashMap<>();
		map.put("supplierName", supplier.getName());
		return JsonEntity.successMessage(map);
	}

	
}