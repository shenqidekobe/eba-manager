package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.SourceType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.DepartmentService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by yuezhiwei on 2017/6/27.
 * 功能描述：
 * 修改记录：
 */
@Controller("adminCustomerRelation")
@RequestMapping("/admin/customerRelation")
public class CustomerRelationController extends BaseController {

	@Resource
	private CustomerRelationService relationService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private AreaService areaService;
	@Resource
	private SupplierSupplierService supplierSupplierService;
	@Resource
	private DepartmentService departmentService ;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;
	@Resource
	private AssChildMemberService assChildMemberService;

	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , ModelMap model) {
		model.addAttribute("searchName", searchName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("clientType", clientType);
		
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("page", relationService.findPage(searchName, startDate, endDate, clientType, pageable, supplier));
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/customerRelation/list";
	}
	
	/**
	 * 微信端后台列表（ajax分页请求）
	 */
	@ResponseBody
	@RequestMapping(value = "/asynclist" , method = RequestMethod.GET)
	public JsonEntity asynclist(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		//model.addAttribute("page", relationService.findPage(searchName, startDate, endDate, clientType, pageable, supplier));
		Page<CustomerRelation> page = relationService.findPage(searchName, startDate, endDate, clientType, pageable, supplier);
		List<Object> customerList = new ArrayList<Object>();
		for(CustomerRelation customerRelation : page.getContent()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", customerRelation.getId());
			map.put("clientName", customerRelation.getClientName());
			customerList.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", customerList);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}

	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	public String add(ModelMap model) {
//		model.addAttribute("clientTypes", CustomerRelation.ClientType.values());
		model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
		return "/admin/customerRelation/add";
	}
	
	/**
	 * 根据企业邀请码查询企业信息
	 * @param inviteCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findBySupplierToCode" , method = RequestMethod.GET)
	public Map<String, Object> findBySupplierToCode(String inviteCode) {
		Map<String, Object> map=new HashMap<>();
		if(!supplierService.inviteCodeExists(inviteCode)) {
			map.put("exist", 0);
			return map;
		}
		Supplier supplier = supplierService.getSupplierByInviteCode(inviteCode);
		map.put("exist", "1");
		map.put("name", supplier.getName());
		if(supplier.getArea() != null) {
			map.put("area", supplier.getArea().getId());
			map.put("areaName", supplier.getArea().getFullName());
			map.put("treePath", supplier.getArea().getTreePath());
		}
		map.put("address", supplier.getAddress());
		map.put("id", supplier.getId());
		return map;
	}
	
	
	/**
	 * 新建客户
	 * @param customerRelation
	 * @param areaId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	public String save(CustomerRelation customerRelation , Long areaId , Long supplierId ,Long adminId, String inviteCode , ModelMap model , RedirectAttributes redirectAttributes) {
		Supplier supplier = super.getCurrentSupplier();
		if(customerRelation == null || supplierId == null || inviteCode == null) {
			addFlashMessage(redirectAttributes, Message.error("参数错误"));
			return "redirect:add.jhtml";
		}
		if(relationService.inviteCodeExists(inviteCode , supplier)) {
			addFlashMessage(redirectAttributes, Message.error("不能重复添加同一个客户"));
			return "redirect:add.jhtml";
		}
		if(supplier.getInviteCode().equalsIgnoreCase(inviteCode)) {
			addFlashMessage(redirectAttributes, Message.error("不能添加自己企业为客户"));
			return "redirect:add.jhtml";
		}
		//relationService.save(customerRelation, areaId, supplierId, adminId, supplier);
		customerRelation.setAdmin(adminService.find(adminId));
		customerRelation.setArea(areaService.find(areaId));
		customerRelation.setSupplier(supplier);
		customerRelation.setBySupplier(supplierService.find(supplierId));
		customerRelation.setSupplierName(supplier.getName());
		customerRelation.setSupplierArea(supplier.getArea());
		customerRelation.setSupplierAddress(supplier.getAddress());
		customerRelation.setSupplierUserName(supplier.getUserName());
		customerRelation.setSupplierTel(supplier.getTel());
		customerRelation.setSupplierEmail(supplier.getEmail());
		customerRelation.setSourceType(SourceType.pc);
		CustomerRelation relation = relationService.save(customerRelation);

		List<AssChildMember> assChildMembers = assChildMemberService.findList(relation);
		for(AssChildMember assChildMember : assChildMembers) {
			AssUpdateTips updateTips = new AssUpdateTips();
			updateTips.setAssChildMember(assChildMember);
			updateTips.setType(AssUpdateTips.Type.businessCustomers);
			updateTips.setCustomerRelation(relation);
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
			assUpdateTipsService.save(updateTips);
		}
		return "redirect:list.jhtml";
	}
	
	@RequestMapping("/edit")
	public String edit(ModelMap model , Long id) {
		model.addAttribute("customerRelation", relationService.find(id));
		model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
		return "/admin/customerRelation/edit";
	}
	
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public String update(CustomerRelation customerRelation , Long areaId ,Long adminId, RedirectAttributes redirectAttributes , ModelMap mode) {
		customerRelation.setArea(areaService.find(areaId));
		customerRelation.setAdmin(adminService.find(adminId));
		CustomerRelation relation = relationService.updateCustomerRelation(customerRelation);
		List<AssUpdateTips> assUpdateTips = assUpdateTipsService.findList(customerRelation, null, AssUpdateTips.Type.businessCustomers);
		if(assUpdateTips.size() > 0) {
			AssChildMember assChildMember = assChildMemberService.find(relation.getAdmin());
			if(null != assChildMember) {
				for(AssUpdateTips updateTips : assUpdateTips) {
					updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
					updateTips.setAssChildMember(assChildMember);
					assUpdateTipsService.update(updateTips);
				}
			}else {
				for(AssUpdateTips updateTips : assUpdateTips) {
					assUpdateTipsService.delete(updateTips);
				}
			}


		}else {
			List<AssChildMember> assChildMembers = assChildMemberService.findList(relation);
			for(AssChildMember assChildMember : assChildMembers) {
				AssUpdateTips updateTips = new AssUpdateTips();
				updateTips.setAssChildMember(assChildMember);
				updateTips.setType(AssUpdateTips.Type.businessCustomers);
				updateTips.setCustomerRelation(relation);
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				assUpdateTipsService.save(updateTips);
			}
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if(ids == null) {
			return ERROR_MESSAGE;
		}
		for(Long id : ids) {
			List<CustomerRelation> customerRelations = relationService.findByNoSupplyRelationship(id);
			if(customerRelations.size() > 0) {
				return Message.warn("此客户存在供应关系不可删除！");
			}
		}
		for(Long id : ids) {
			CustomerRelation relation = relationService.find(id);
			List<AssUpdateTips> assUpdateTips = assUpdateTipsService.findList(relation, null, AssUpdateTips.Type.businessCustomers);
			for(AssUpdateTips updateTips : assUpdateTips) {
				assUpdateTipsService.delete(updateTips);
			}
		}
		relationService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/searchMobile" , method = RequestMethod.GET)
	public String searchMobile() {
		return "/admin/customerRelation/search";
	}
}
