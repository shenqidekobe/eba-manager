package com.microBusiness.manage.controller.api.small;

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
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.util.Code;

@Controller("smallShopController")
@RequestMapping("/api/small/shop")
public class ShopController extends BaseController{
	
	@Resource
	private ShopService shopService ;
	@Resource
	private ChildMemberService childMemberService ;
	@Resource
	private AdminService adminService ;
	@Resource
	private AreaService areaService;
	@Resource
	private MemberService memberService;
	@Resource
	private HostingShopService hostingShopService;

	
	/**
	 * 店铺列表
	 * @param unionId
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity list(String unionId,Pageable pageable){
		Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Page<Shop> page=shopService.findPage(pageable, member);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for (Shop shop : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", shop.getId());
			map.put("name", shop.getName());
			map.put("shopType", shop.getShopType());
			map.put("userName", shop.getUserName());
			if (shop.getMember().equals(member)) {
				map.put("myself", true);
			}else {
				map.put("myself", false);
			}
			list.add(map);
		}
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());
		
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 当前手机号是否注册企业
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/isRegister", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity isRegister(String unionId){
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Admin admin=adminService.findBybindPhoneNum(member.getMobile());
		Map<String, Object> map=new HashMap<>();
		if (admin!=null && admin.getIsSystem()) {
			member.setAdmin(admin);
			memberService.refreshMember(member);
			map.put("isRegister", true);
			map.put("supplierName", admin.getSupplier().getName());
		}else {
			map.put("isRegister", false);
			map.put("supplierName", "");
		}
		map.put("tel", member.getMobile());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 添加店铺
	 * @param unionId
	 * @param shopType 店铺类型
	 * @param name		店铺名称
	 * @param userName  店长名称
	 * @param areaId	地区id
	 * @param address 	详细地址
	 * @param ids		托管人id
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity add(String unionId,ShopType shopType,String name,String userName,Long areaId,String address,Long[] ids,String receiverTel){
		Member member = childMemberService.findByUnionId(unionId).getMember();
		if (shopService.exitShopNameByMember(member, name, null)) {
			return new JsonEntity(Code.code11716, Code.code11716.getDesc());
		}
		Area area=areaService.find(areaId);
		shopService.add(shopType,name, userName, area, address, ids,member, receiverTel);
		return JsonEntity.successMessage();
	}

    /**
     * 根据id获取店铺信息
     * @param shopId 店铺id
     * @return
     */
	@RequestMapping(value = "/getShopById", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getShopById(Long shopId){
		Shop shop=shopService.find(shopId);
		Member member = shop.getMember();
		Map<String, Object> map=new HashMap<>();
		map.put("id", shop.getId());
		map.put("name", shop.getName());
		map.put("userName", shop.getUserName());
		map.put("shopType", shop.getShopType());
        map.put("areaId", shop.getArea().getId());
        map.put("address", shop.getAddress());
        map.put("receiverTel", shop.getReceiverTel());
        if (ShopType.direct.equals(shop.getShopType())){
            map.put("supplierName",member.getAdmin().getSupplier().getName());
        }
        List<Map<String,Object>> list=new ArrayList<>();
		List<HostingShop> hostingShops=hostingShopService.findListByShop(shop);
		for (HostingShop hostingShop:hostingShops) {
			Map<String,Object> hostingShopMap=new HashMap<>();
			hostingShopMap.put("id",hostingShop.getMemberMember().getId());
			hostingShopMap.put("name",hostingShop.getMemberMember().getName());
			hostingShopMap.put("tel",hostingShop.getMemberMember().getByMember().getMobile());
			list.add(hostingShopMap);
		}
		map.put("shopAssistant",list);
		return JsonEntity.successMessage(map);
	}

	/**
	 * 店铺信息修改
	 * @param name		店铺名称
	 * @param userName  店长名称
	 * @param areaId	地区id
	 * @param address 	详细地址
	 * @param ids		托管人id
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity update(Long shopId,String name,String userName,Long areaId,String address,Long[] ids,String receiverTel){
		Shop shop=shopService.find(shopId);
		if (shopService.exitShopNameByMember(shop.getMember(), name, shop)) {
			return new JsonEntity(Code.code11716, Code.code11716.getDesc());
		}
		Area area=areaService.find(areaId);
		shopService.update(shop,name, userName, area, address, ids, receiverTel);
		return JsonEntity.successMessage();
	}

	/**
	 * 判断是否超过两家店
	 * 
	 * @param shopId
	 * @param name
	 * @param userName
	 * @param areaId
	 * @param address
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/shopFlag", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity shopFlag(String unionId){
		Member member = childMemberService.findByUnionId(unionId).getMember();
		List<Shop> shops = shopService.findShopList(member);
		
		if (shops.size() >= 2) {
			Boolean direct = false;
			
			for (Shop shop : shops) {
				if (shop.getShopType() == ShopType.direct) {
					direct = true;
				}
			}
			
			
		
			if (direct == true) {
				if (member.getAdmin().getSupplier().getStatus() != Supplier.Status.verified) {
					return new JsonEntity(Code.code11717, Code.code11717.getDesc());
				}
			}
			
			if (!direct) {
				if (member.getAdmin() == null) {
					return new JsonEntity(Code.code11718, Code.code11718.getDesc());
				}
				
				if (member.getAdmin().getSupplier().getStatus() != Supplier.Status.verified) {
					return new JsonEntity(Code.code11717, Code.code11717.getDesc());
				}
			}
			
		}
		
		return JsonEntity.successMessage();
	}

}
