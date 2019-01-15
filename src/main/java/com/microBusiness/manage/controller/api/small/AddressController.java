package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.Code;
/**
 * 地址管理
 */
@Controller("smallAddressController")
@RequestMapping("/api/small/address")
public class AddressController extends BaseController {

	@Resource
	private ReceiverService receiverService;
	@Resource
	private AreaService areaService;
	@Resource
    private ChildMemberService childMemberService ;
	@Resource
	private SupplierService supplierService;
	
	/**
	 * 获取地址列表
	 * @param request
	 * @return
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public JsonEntity list(String unionId, String smOpenId, Pageable pageable) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		Page<Receiver> page = receiverService.findPage(member, pageable);
		List<Receiver> list = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> lis = new ArrayList<Map<String,Object>>();
		for(final Receiver receiver : list) {
			lis.add(new HashMap<String, Object>(){{
				this.put("id", receiver.getId());
				this.put("address", receiver.getAddress());
				this.put("areaName", receiver.getAreaName());
				this.put("areaId", receiver.getArea().getId());
				this.put("isDefault", receiver.getIsDefault());
				this.put("phone", receiver.getPhone());
				this.put("type", receiver.getType());
				this.put("consignee", receiver.getConsignee());
			}});
		}
		resultMap.put("list", lis);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 新增地址
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	public JsonEntity save(String unionId, String smOpenId, String consignee, 
			String address, String phone, Long areaId) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		if(null == consignee) {
			return JsonEntity.error(Code.code_small_address_100002, Code.code_small_address_100002.getDesc());
		}
		if(null == phone) {
			return JsonEntity.error(Code.code_small_address_100003, Code.code_small_address_100003.getDesc());
		}
		if(null == address) {
			return JsonEntity.error(Code.code_small_address_100005, Code.code_small_address_100005.getDesc());
		}
		if(null == areaId) {
			return JsonEntity.error(Code.code_small_address_100006, Code.code_small_address_100006.getDesc());
		}
		if(!phone.matches("^1[3|4|5|7|8]\\d{9}$")) {
			return JsonEntity.error(Code.code_small_address_100004, Code.code_small_address_100004.getDesc());
		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Member member = childMember.getMember();
		if(StringUtils.isEmpty(childMember.getPhone())){
			childMember.setPhone(phone);
			childMemberService.update(childMember);//更新会员手机号码
		}
		Area area = areaService.find(areaId);
		Receiver receiver = new Receiver();
		receiver.setConsignee(consignee);
		receiver.setPhone(phone);
		receiver.setAddress(address);
		receiver.setAreaName(area.getFullName());
		receiver.setMember(member);
		//如果没有默认地址，则当前保存的设为默认地址，否则非默认
		List<Receiver> receivers = receiverService.find(member, true);
		if(receivers.isEmpty()) {
			receiver.setIsDefault(true);
		}else {
			receiver.setIsDefault(false);
		}
		receiver.setType(Receiver.Type.frontDesk);
		receiver.setArea(area);
		receiver.setZipCode("200000");
		receiverService.save(receiver);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", receiver.getId());
		resultMap.put("consignee", receiver.getConsignee());
		resultMap.put("areaName", receiver.getAreaName());
		resultMap.put("address", receiver.getAddress());
		resultMap.put("phone", receiver.getPhone());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 查看地址
	 * @param id 地址id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/edit" , method = RequestMethod.GET)
	public JsonEntity edit(String unionId , String smOpenId,  Long id) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		if(null == id) {
			return JsonEntity.error(Code.code_small_address_100001, Code.code_small_address_100001.getDesc());
		}
		Receiver receiver = receiverService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", receiver.getId());
		resultMap.put("consignee", receiver.getConsignee());
		resultMap.put("address", receiver.getAddress());
		resultMap.put("areaName", receiver.getAreaName());
		resultMap.put("isDefault", receiver.getIsDefault());
		resultMap.put("phone", receiver.getPhone());
		resultMap.put("areaId", receiver.getArea().getId());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 修改地址
	 * @param id 地址id
	 * @param userName 收货人姓名
	 * @param tel 收货人手机号
	 * @param address 详细地址
	 * @param areaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public JsonEntity update(String unionId, Long id , String consignee , String phone , String address , Long areaId) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		if(null == id) {
			return JsonEntity.error(Code.code_small_address_100001, Code.code_small_address_100001.getDesc());
		}
		if(null == consignee) {
			return JsonEntity.error(Code.code_small_address_100002, Code.code_small_address_100002.getDesc());
		}
		if(null == phone) {
			return JsonEntity.error(Code.code_small_address_100003, Code.code_small_address_100003.getDesc());
		}
		if(null == address) {
			return JsonEntity.error(Code.code_small_address_100005, Code.code_small_address_100005.getDesc());
		}
		if(null == areaId) {
			return JsonEntity.error(Code.code_small_address_100006, Code.code_small_address_100006.getDesc());
		}
		if(!phone.matches("^1[3|4|5|7|8]\\d{9}$")) {
			return JsonEntity.error(Code.code_small_address_100004, Code.code_small_address_100004.getDesc());
		}
		Area area = areaService.find(areaId);
		Receiver receiver = receiverService.find(id);
		receiver.setPhone(phone);
		receiver.setConsignee(consignee);
		receiver.setArea(area);
		receiver.setAreaName(area.getFullName());
		receiver.setAddress(address);
		receiverService.update(receiver);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 设置默认地址
	 * @param id 地址id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setDefaultAddress" , method = RequestMethod.POST)
	public JsonEntity setDefaultAddress(String unionId , String smOpenId, Long id) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		if(null == id) {
			return JsonEntity.error(Code.code_small_address_100001, Code.code_small_address_100001.getDesc());
		}
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Member member = childMember.getMember();
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		
		List<Receiver> receivers = receiverService.find(member, true);
		for(Receiver receiver : receivers) {
			receiver.setIsDefault(false);
			receiverService.update(receiver);
		}
		Receiver receiver = receiverService.find(id);
		if(null != receiver) {
			receiver.setIsDefault(true);
			receiverService.update(receiver);
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 删除地址
	 * @param id 地址id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public JsonEntity delete(String unionId , String smOpenId, Long id) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		if(null == id) {
			return JsonEntity.error(Code.code_small_address_100001, Code.code_small_address_100001.getDesc());
		}
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		Receiver receiver = receiverService.find(id);
		receiverService.deleted(receiver);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 创建订单时选择地址列表
	 * @param unionId
	 * @param supplier
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderAddressList", method = RequestMethod.GET)
	public JsonEntity orderAddressList(String unionId , String smOpenId, Long supplierId) {
//		if(null == unionId ){
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		if(null == supplierId) {
			return JsonEntity.error(Code.code_small_address_100007, Code.code_small_address_100007.getDesc());
		}
		Supplier supplier = supplierService.find(supplierId);
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		List<Receiver> receivers = receiverService.findList(member, supplier);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Receiver receiver : receivers) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", receiver.getId());
			map.put("address", receiver.getAddress());
			map.put("areaName", receiver.getAreaName());
			map.put("areaId", receiver.getArea().getId());
			map.put("isDefault", receiver.getIsDefault());
			map.put("phone", receiver.getPhone());
			map.put("type", receiver.getType());
			map.put("consignee", receiver.getConsignee());
			resultList.add(map);
		}
		return JsonEntity.successMessage(resultList);
	}
	
	/**
	 * 选择地址
	 * @param unionId
	 * @param id 地址id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/chooseAddress" , method = RequestMethod.GET)
	public JsonEntity chooseAddress(String unionId , String smOpenId, Long id) {
//		if(null == unionId || null == id) {
//			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
//		}
		if( null == id) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Receiver receiver = receiverService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null != receiver) {
			resultMap.put("receiverId", receiver.getId());
			resultMap.put("consignee", receiver.getConsignee());
			resultMap.put("areaName", receiver.getAreaName());
			resultMap.put("address", receiver.getAddress());
			resultMap.put("phone", receiver.getPhone());
			
			ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
			childMember.setPhone(receiver.getPhone());
			childMemberService.update(childMember);//更新会员手机号码
		}
		return JsonEntity.successMessage(resultMap);
	}
}
