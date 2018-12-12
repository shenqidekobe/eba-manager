package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;
import com.microBusiness.manage.util.Code;

/**
 * 我的客户
 * @author yuezhiwei
 *
 */
@Controller
@RequestMapping("/ass/myClient")
public class AssMyClientController extends BaseController {
	@Resource
	private AssChildMemberService assChildMemberService;
	@Resource
	private CustomerRelationService customerRelationService;
	@Resource
	private AreaService areaService;
	@Resource
	private NeedService needService;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;

	/**
	 * 企业客户列表
	 * @param unionId
	 * @param pageable
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/businessCustomersList" , method = RequestMethod.GET)
	public JsonEntity businessCustomersList(String unionId , Pageable pageable) {
		final AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == assChildMember || null == assChildMember.getId()) {
			return new JsonEntity(Code.code13003, Code.code13003.getDesc());
		}
		Page<CustomerRelation> page = customerRelationService.findPage(assChildMember, pageable);
		List<CustomerRelation> customerRelations = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> crl = new ArrayList<Map<String,Object>>();
		for(final CustomerRelation customerRelation : customerRelations) {
			crl.add(new HashMap<String, Object>(){{
				this.put("id", customerRelation.getId());
				this.put("clientName", customerRelation.getClientName());
				AssUpdateTips updateTips = assUpdateTipsService.find(assChildMember, customerRelation, null, AssUpdateTips.Type.businessCustomers);
				if(null != updateTips) {
					if(updateTips.getWhetherUpdate() == AssUpdateTips.WhetherUpdate.yes) {
						this.put("updateTips", true);
					} else {
						this.put("updateTips", false);
					}
				}else {
					this.put("updateTips", false);
				}
			}});
		}
		resultMap.put("list", crl);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 修改企业客户信息
	 * @param unionId
	 * @param id 企业客户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/businessCustomersEdit" , method = RequestMethod.GET)
	public JsonEntity businessCustomersEdit(String unionId , Long id) {
		if(null == id) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		CustomerRelation relation = customerRelationService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null != relation) {
			resultMap.put("id", relation.getId());
			resultMap.put("clientName", relation.getClientName());
			resultMap.put("inviteCode", relation.getInviteCode());
			resultMap.put("clientNum", relation.getClientNum());
			resultMap.put("fullName", relation.getArea()!=null?relation.getArea().getFullName():"");
			resultMap.put("address", relation.getAddress());
			resultMap.put("userName", relation.getUserName());
			resultMap.put("tel", relation.getTel());
			resultMap.put("email", relation.getEmail());
			resultMap.put("accountName", relation.getAccountName());
			resultMap.put("invoice", relation.getInvoice());
			resultMap.put("bank", relation.getBank());
			resultMap.put("bankAccountNum", relation.getBankAccountNum());
			resultMap.put("identifier", relation.getIdentifier());
			resultMap.put("areaId", relation.getArea()!=null?relation.getArea().getId():null);
		}
		
		AssUpdateTips assUpdateTips = assUpdateTipsService.find(assChildMember, relation, null, AssUpdateTips.Type.businessCustomers);
		if(null != assUpdateTips) {
			assUpdateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.no);
			assUpdateTipsService.update(assUpdateTips);
		}
		
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 更新企业客户
	 * @param unionId
	 * @param id 企业客户id
	 * @param clientNum 客户编号
	 * @param areaId 省市区id
	 * @param address 详细地址
	 * @param userName 联系人
	 * @param tel 联系方式
	 * @param email 邮箱
	 * @param accountName 开户名称
	 * @param invoice 发票抬头
	 * @param bank 开户银行
	 * @param bankAccountNum 银行账号
	 * @param identifier 纳税人识别码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/businessCustomersUpdate" , method = RequestMethod.POST)
	public JsonEntity businessCustomersUpdate(String unionId, Long id,String clientNum,Long areaId,String address,
			String userName,String tel,String email,String accountName,String invoice,String bank,String bankAccountNum,String identifier) {
		if(null == id) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		//AssChildMember assChildMember = super.getAssChildMember(unionId);
		CustomerRelation relation = customerRelationService.find(id);
		if(null != relation) {
			relation.setClientNum(clientNum);
			relation.setArea(areaService.find(areaId));
			relation.setAddress(address);
			relation.setUserName(userName);
			relation.setTel(tel);
			relation.setEmail(email);
			relation.setAccountName(accountName);
			relation.setInvoice(invoice);
			relation.setBank(bank);
			relation.setBankAccountNum(bankAccountNum);
			relation.setIdentifier(identifier);
			customerRelationService.update(relation);
		}else {
			return new JsonEntity(Code.code202, Code.code202.getDesc());
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 删除企业客户
	 * @param unionId
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/businessCustomersDelete" , method = RequestMethod.POST)
	public JsonEntity businessCustomersDelete(String unionId, Long id) {
		if(null == id) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		List<CustomerRelation> customerRelations = customerRelationService.findByNoSupplyRelationship(id);
		if(customerRelations.size() > 0) {
			return new JsonEntity(Code.code_business_customers_100001, Code.code_business_customers_100001.getDesc());
		}
		CustomerRelation customerRelation = customerRelationService.find(id);
		AssUpdateTips assUpdateTips = assUpdateTipsService.find(assChildMember, customerRelation, null, AssUpdateTips.Type.businessCustomers);
		if(null != assUpdateTips) {
			assUpdateTipsService.delete(assUpdateTips);
		}
		customerRelationService.delete(id);
		
		return JsonEntity.successMessage();
	}
	
	/**
	 * 个体客户列表
	 * @param unionId
	 * @param pageable
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/individualCustomersList" , method = RequestMethod.GET)
	public JsonEntity individualCustomersList(String unionId , Pageable pageable) {
		final AssChildMember assChildMember = super.getAssChildMember(unionId);
		Page<Need> page = needService.findPage(assChildMember, pageable);
		List<Need> needs = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> needList = new ArrayList<Map<String,Object>>();
		for(final Need need : needs) {
			needList.add(new HashMap<String, Object>(){{
				this.put("id", need.getId());
				this.put("name", need.getName());
				this.put("status", need.getNeedStatus());
				AssUpdateTips updateTips = assUpdateTipsService.find(assChildMember, null, need, AssUpdateTips.Type.individualCustomers);
				if(null != updateTips) {
					if(updateTips.getWhetherUpdate() == AssUpdateTips.WhetherUpdate.yes) {
						this.put("updateTips", true);
					} else {
						this.put("updateTips", false);
					}
				}else {
					this.put("updateTips", false);
				}
			}});
		}
		resultMap.put("needList", needList);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 修改个体客户
	 * @param unionId
	 * @param id 个体客户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/individualCustomersEdit" , method = RequestMethod.GET)
	public JsonEntity individualCustomersEdit(String unionId , Long id) {
		if(null == id) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Need need = needService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null != need) {
			resultMap.put("id", need.getId());
			resultMap.put("name", need.getName());
			resultMap.put("userName", need.getUserName());
			resultMap.put("fullName", need.getArea().getFullName());
			resultMap.put("address", need.getAddress());
			resultMap.put("tel", need.getTel());
			resultMap.put("clientNum", need.getClientNum());
			resultMap.put("contacts", need.getContacts());
			resultMap.put("contactsTel", need.getContactsTel());
			resultMap.put("email", need.getEmail());
			resultMap.put("accountName", need.getAccountName());
			resultMap.put("invoice", need.getInvoice());
			resultMap.put("bank", need.getBank());
			resultMap.put("bankAccountNum", need.getBankAccountNum());
			resultMap.put("identifier", need.getIdentifier());
			resultMap.put("areaId", need.getArea().getId());
		}
		AssUpdateTips assUpdateTips = assUpdateTipsService.find(assChildMember, null, need, AssUpdateTips.Type.individualCustomers);
		if(null != assUpdateTips) {
			assUpdateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.no);
			assUpdateTipsService.update(assUpdateTips);
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 更新个体客户
	 * @param unionId
	 * @param need 个体客户
	 * @param areaId 省市区id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/individualCustomersUpdate" , method = RequestMethod.POST)
	public JsonEntity individualCustomersUpdate(String unionId, Need need, Long areaId) {
		//AssChildMember assChildMember = super.getAssChildMember(unionId);
		need.setArea(areaService.find(areaId));
		needService.updateIndividualCustomers(need);
		return JsonEntity.successMessage();
	}
	
}
