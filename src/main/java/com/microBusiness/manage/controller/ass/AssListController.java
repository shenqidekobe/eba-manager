package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.AssPurchaseListStatisticsDto;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssForm;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListItem;
import com.microBusiness.manage.entity.ass.AssListMemberStatus;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssFormService;
import com.microBusiness.manage.service.ass.AssListRelationService;
import com.microBusiness.manage.service.ass.AssListRemarksService;
import com.microBusiness.manage.service.ass.AssListService;
import com.microBusiness.manage.service.ass.AssShippingAddressService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.DateUtils;

@Controller("smallAssListController")
@RequestMapping("/ass/assList")
public class AssListController extends BaseController {

	@Resource(name = "assListServiceImpl")
	private AssListService assListService;
	@Resource(name = "assListRemarksServiceImpl")
	private AssListRemarksService assListRemarksService;
	@Resource(name = "assListRelationServiceImpl")
	private AssListRelationService assListRelationService;
	@Resource
	private NeedService needService;
	@Resource
	private WeChatService weChatService ;
	@Resource(name="assFormServiceImpl")
	private AssFormService assFormService ;
	@Resource
	private AssCustomerRelationService assCustomerRelationService;
	@Resource
	private AssShippingAddressService assShippingAddressService;
	
	@Value("${small.template.common.templateId}")
	private String smallCommonTemplateId;
	/**
	 * 清单列表
	 * @param unionId
	 * @param request
	 * @param response
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity list(String unionId,String searchValue,HttpServletRequest request, HttpServletResponse response , Pageable pageable) {
		if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Member member = assChildMember.getMember();
		Page<AssList> page=assListService.findPage(pageable,searchValue, assChildMember, member);
		List<Map<String, Object>> assLists=this.dealAssList(page, assChildMember, member);
		Map<String, Object> map=new HashMap<>();
		map.put("assLists", assLists);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 清单详情
	 * @param unionId
	 * @param assListId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAssListDetail",method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getAssListDetail(String unionId,Long assListId,HttpServletRequest request, HttpServletResponse response, String type) {
		AssList assList=assListService.find(assListId);
		if(null == unionId || null == assListId || null == assList ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (assList.isDeleted()) {
			return new JsonEntity(Code.code_asslist_11115, Code.code_asslist_11115.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Member member = assChildMember.getMember();
		Map<String, Object> map=getCommonassListDetail(assList, assChildMember, member, type);
		return JsonEntity.successMessage(map);
	}
	/**
	 * 获取留言列表
	 * @param assListId
	 * @param assListRemarksId
	 * @param pageable
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAssListRemarks",method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getAssListRemarks(String unionId,Long assListId,Long assListRemarksId, Pageable pageable,HttpServletRequest request, HttpServletResponse response) {
		AssList assList=assListService.find(assListId);
		if(null == assListId || null == assList || unionId == null){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		//AssChildMember assChildMember = super.getAssChildMember(unionId);
		Page<AssListRemarks> page=assListRemarksService.getAssListRemarks(pageable, assList, assListRemarksId);
		List<Map<String, Object>> assListRemarks=this.dealAssListRemarks(page.getContent());
		Map<String, Object> map=new HashMap<>();
		map.put("assListRemarks", assListRemarks);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 获取参与列表
	 * @param assListId
	 * @param assListRelationsId
	 * @param pageable
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getAssListRelations",method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getAssListRelations(String unionId,Long assListId,Long assListRelationsId, Pageable pageable,HttpServletRequest request, HttpServletResponse response) {
		AssList assList=assListService.find(assListId);
		if(null == assListId || null == assList || unionId == null){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		//AssChildMember assChildMember = super.getAssChildMember(unionId);
		Page<AssListRelation> page=assListRelationService.getAssListRelations(pageable, assList, assListRelationsId);
		List<Map<String, Object>> assListRelations=this.dealAssListRelation(page.getContent());
		Map<String, Object> map=new HashMap<>();
		map.put("assListRelations", assListRelations);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 分享、参与、终结、退出
	 * @param assListId
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity updateStatus(String unionId,Long assListId,AssListMemberStatus status,HttpServletRequest request, HttpServletResponse response) {
		AssList assList=assListService.find(assListId);
		if (null == assListId || null == assList || status == null || unionId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (!status.equals(AssListMemberStatus.share)&&!status.equals(AssListMemberStatus.end)&&!status.equals(AssListMemberStatus.participate)&&!status.equals(AssListMemberStatus.noparticipate)) {
			return new JsonEntity(Code.code_asslist_11110, Code.code_asslist_11110.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		//分享和终结订单判断该清单是否是登录用户的清单
		if (status.equals(AssListMemberStatus.end) || status.equals(AssListMemberStatus.share)) {
			if (assList.getType().equals(AssList.Type.self) && !assChildMember.equals(assList.getAssChildMember())) {
				return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
			}
			if (assList.getType().equals(AssList.Type.supplier) && assChildMember.getMember() != null && !assChildMember.getMember().equals(assList.getMember())) {
				return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
			}
		}
		//只能在清单状态为分享的时候才能参与
		if (status.equals(AssListMemberStatus.participate) && !assList.getStatus().equals(AssList.Status.share)) {
			return new JsonEntity(Code.code_asslist_11112, Code.code_asslist_11112.getDesc());
		}
		//只能在清单状态为分享和终结的时候才能退出参与
		if (status.equals(AssListMemberStatus.noparticipate) && (!assList.getStatus().equals(AssList.Status.share) && !assList.getStatus().equals(AssList.Status.end))) {
			return new JsonEntity(Code.code_asslist_11112, Code.code_asslist_11112.getDesc());
		}
		assListService.updateStatus(assList, status, assChildMember);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 添加留言
	 * @param unionId
	 * @param assListId
	 * @param formId
	 * @param assListRemarks
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addAssListRemarks",method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity addAssListRemarks(String unionId,Long assListId,AssListRemarks assListRemarks,HttpServletRequest request, HttpServletResponse response) {
		AssList assList=assListService.find(assListId);
		if (null == assListId || null == assList || unionId == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (assList.getStatus().equals(AssList.Status.end)) {
			return new JsonEntity(Code.code_asslist_11112, Code.code_asslist_11112.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		assListRemarks.setAssList(assList);
		assListRemarks.setAssChildMember(assChildMember);
		assListRemarks.setName(assChildMember.getNickName());
		assListRemarksService.save(assListRemarks);
		//发送模版消息
		if (StringUtils.isNotBlank(assListRemarks.getDescription())) {
			for (AssListRelation assListRelation : assList.getAssListRelations()) {
				AssChildMember aMember= assListRelation.getAssChildMember();
				//不给当前登录用户发
				if (!assChildMember.equals(aMember)) {
					AssForm assForm=aMember.getAssFormOne();
					Date now=new Date();
					if (assForm!=null) {
						//formId是否过期
						if (DateUtils.daysBetween(assForm.getCreateDate(),now) < 7 ) {
							weChatService.sendSmallTemplateMessageToInitiator(assList, aMember, weChatService.getAssSmallGlobalToken(), smallCommonTemplateId, assListRemarks.getDescription(), assForm.getFormId());
						}
						//删除formId
						assFormService.delete(assForm);
					}
				}
			}
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 订单创建
	 * @param unionId
	 * @param assCustomerRelationId 供应商id
	 * @param assShippingAddressId	收货地址id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity create(String unionId,Long assCustomerRelationId,Long addressId,AssListRemarks assListRemarks,HttpServletRequest request, HttpServletResponse response){
		if (unionId == null || assCustomerRelationId == null ) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if(null == addressId) {
			return new JsonEntity(Code.code_asslist_11118, Code.code_asslist_11118.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		//供应商
		AssCustomerRelation assCustomerRelation=assCustomerRelationService.find(assCustomerRelationId);
		//购物车
		AssCart assCart=assChildMember.getAssCart();
		if (assCart == null) {
			return new JsonEntity(Code.code11101, Code.code11101.getDesc());
		}
		if (assCustomerRelation.getSourceType().equals(AssCustomerRelation.SourceType.MOBILE) && assChildMember.getAssShippingAddress() == null && assChildMember.getAssShippingAddress().size() == 0) {
			return new JsonEntity(Code.code_asslist_11114, Code.code_asslist_11114.getDesc());
		}
		Set<AssCartItem> assCartItems=assCart.getCartItems(assCustomerRelation.getId());
		if (assCartItems.size() <= 0) {
			return new JsonEntity(Code.code11105, Code.code11105.getDesc());
		}
		//获取地址
		AssShippingAddress assShippingAddress = assShippingAddressService.find(addressId);
		if(null == assShippingAddress || null == assShippingAddress.getId()) {
			return new JsonEntity(Code.code_asslist_11118, Code.code_asslist_11118.getDesc());
		}
		AssList assList=assListService.create(assCartItems, assChildMember, assCustomerRelation,assListRemarks, assShippingAddress);
		
		if (assList == null) {
			return new JsonEntity(Code.code_asslist_11113, Code.code_asslist_11113.getDesc());
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", assList.getId());
		map.put("sn", assList.getSn());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 再来一单
	 * @return
	 */
	@RequestMapping(value = "/comeAgain",method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity comeAgain(String unionId,Long assListId,HttpServletRequest request, HttpServletResponse response){
		if (unionId == null || assListId == null ) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		AssList assList=assListService.find(assListId);
		if (assChildMember.getAssCart() == null) {
			return new JsonEntity(Code.code11101, Code.code11101.getDesc());
		}
		if (assList.getAssChildMember() != null && !assList.getAssChildMember().equals(assChildMember)) {
			return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
		}
		if (assList.getAssCustomerRelation().isDeleted()) {
			return new JsonEntity(Code.code_asslist_11116, Code.code_asslist_11116.getDesc());
		}
		assListService.comeAgain(assChildMember,assList);
//		if (assChildMember.getAssCart().getCartItems(assList.getAssCustomerRelation().getId()).size() <= 0) {
//			return new JsonEntity(Code.code_asslist_11117, Code.code_asslist_11117.getDesc());
//		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 删除清单
	 * @return
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity delete(String unionId,Long assListId,HttpServletRequest request, HttpServletResponse response){
		if (unionId == null || assListId == null ) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		AssList assList=assListService.find(assListId);
		if (assList.getAssChildMember() != null && !assList.getAssChildMember().equals(assChildMember)) {
			return new JsonEntity(Code.code_asslist_11111, Code.code_asslist_11111.getDesc());
		}
		assListService.deleted(assList);
		return JsonEntity.successMessage();
	}
	
	private List<Map<String, Object>> dealAssList(Page<AssList> page,AssChildMember assChildMember, Member member) {
		List<Map<String, Object>> assLists=new ArrayList<>();
		for (AssList assList : page.getContent()) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("assListId", assList.getId());
			map.put("sn", assList.getSn());
			map.put("singlePerson", assList.getSinglePerson());
			map.put("addressName", assList.getAddressName());
			map.put("clientName", assList.getClientName());
			map.put("assListDate", assList.getCreateDate());
			if (assChildMember != null && assList.getAssChildMember() != null && assChildMember.equals(assList.getAssChildMember())) {
				if (assList.getStatus().equals(AssList.Status.share)) {
					map.put("assListstatus", AssListMemberStatus.share);
				}else if (assList.getStatus().equals(AssList.Status.noshare)) {
					map.put("assListstatus", AssListMemberStatus.noshare);
				}else if (assList.getStatus().equals(AssList.Status.end)) {
					map.put("assListstatus", AssListMemberStatus.end);
				}
			}else if (member != null && assList.getMember() != null && member.equals(assList.getMember())) {
				if (assList.getStatus().equals(AssList.Status.share)) {
					map.put("assListstatus", AssListMemberStatus.share);
				}else if (assList.getStatus().equals(AssList.Status.noshare)) {
					map.put("assListstatus", AssListMemberStatus.noshare);
				}else if (assList.getStatus().equals(AssList.Status.end)) {
					map.put("assListstatus", AssListMemberStatus.end);
				}
			}else {
				map.put("assListstatus", AssListMemberStatus.participate);
			}
			assLists.add(map);
		}
		return assLists;
	}
	
	private Map<String, Object> getCommonassListDetail(AssList assList,AssChildMember assChildMember,Member member,String type) {
		 Map<String, Object> map=new HashMap<String, Object>();
		 //订单基本信息
		 map.put("assListId", assList.getId());
		 map.put("sn", assList.getSn());
		 map.put("singlePerson", assList.getSinglePerson());
		 map.put("assListDate", assList.getCreateDate());
		 map.put("assListstatus", assList.getStatus());
		 //供应商名称
		 AssCustomerRelation assCustomerRelation=assList.getAssCustomerRelation();
		 map.put("clientName", assList.getClientName());
		 map.put("assCustomerRelationId", assCustomerRelation != null?assCustomerRelation.getId():"");
		 //采购方名称 
		 map.put("addressName", assList.getAddressName());
		 
		 //收货信息
		 map.put("receiver", assList.getName());//收货人
		 map.put("receiverTel", assList.getTel());//收货人电话
		 map.put("address", assList.getAddress());//收货地址
		 
		 //当前用户相对于清单的角色  创建人  参与人  未参与人
		 AssListMemberStatus memberStatus;
		 if (assChildMember != null && assList.getAssChildMember() != null && assChildMember.equals(assList.getAssChildMember())) {
			 memberStatus=AssListMemberStatus.self;
		 }else if (member != null && assList.getMember() != null && member.equals(assList.getMember())) {
			 memberStatus=AssListMemberStatus.self;
		 }else {
			 if (assList.participate(assChildMember)) {
				 memberStatus=AssListMemberStatus.participate;
			}else {
				 memberStatus=AssListMemberStatus.noparticipate;
			}
		 }
		 map.put("memberStatus", memberStatus);
		 
		 //清单商品列表
		 List<Map<String, Object>> assListItems=new ArrayList<>();
		 List<AssListItem> aItems=assList.getAssListItems();
		 Collections.sort(aItems, new Comparator<AssListItem>() {

		 	/*  
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，  
             * 返回负数表示：o1 小于o2，  
             * 返回0 表示：o1和o2相等，  
             * 返回正数表示：o1大于o2。  
             */  
			@Override
			public int compare(AssListItem o1, AssListItem o2) {
				if (o1.getAssProduct().getAssGoods().getId()>o2.getAssProduct().getAssGoods().getId()) {
					return 1;  
				}
				if (o1.getAssProduct().getAssGoods().getId()==o2.getAssProduct().getAssGoods().getId()) {
					return 0;  
				}
				return -1;
			}
			 
		 });
		 if (aItems != null) {
			 for (AssListItem assListItem : aItems) {
				 Map<String, Object> itemMap=new HashMap<>();
				 if(null == type) {
					 itemMap.put("goodsId", assListItem.getAssProduct().getAssGoods().getId());
					 itemMap.put("productId", assListItem.getAssProduct().getId());
					 itemMap.put("productName", assListItem.getName());
					 itemMap.put("unit", assListItem.getUnit());
					 itemMap.put("specs", assListItem.getSpecification());
					 itemMap.put("quantity", assListItem.getQuantity());
					 itemMap.put("image", assListItem.getThumbnail());
					 assListItems.add(itemMap);
				 }else {
					 if(type.equalsIgnoreCase("statistics")) {
						 AssGoods assGoods=assListItem.getAssProduct().getAssGoods().getSource();
						 if (assGoods != null) {
							 AssCustomerRelation relation = assGoods.getAssCustomerRelation();
							 map.put("clientName", relation.getClientName());
							 AssCustomerRelation relation2 = assList.getAssCustomerRelation();
							 if(relation.getShareType() == AssCustomerRelation.ShareType.share && relation.getAssChildMember().equals(assChildMember)) {
								 itemMap.put("goodsId", assListItem.getAssProduct().getAssGoods().getId());
								 itemMap.put("productId", assListItem.getAssProduct().getId());
								 itemMap.put("productName", assListItem.getName());
								 itemMap.put("unit", assListItem.getUnit());
								 itemMap.put("specs", assListItem.getSpecification());
								 itemMap.put("quantity", assListItem.getQuantity());
								 itemMap.put("image", assListItem.getThumbnail());
								 assListItems.add(itemMap);
							 }else if(relation2.getAssChildMember().equals(assChildMember) && !assList.getAssChildMember().equals(assChildMember)){
								 itemMap.put("goodsId", assListItem.getAssProduct().getAssGoods().getId());
								 itemMap.put("productId", assListItem.getAssProduct().getId());
								 itemMap.put("productName", assListItem.getName());
								 itemMap.put("unit", assListItem.getUnit());
								 itemMap.put("specs", assListItem.getSpecification());
								 itemMap.put("quantity", assListItem.getQuantity());
								 itemMap.put("image", assListItem.getThumbnail());
								 assListItems.add(itemMap);
							 }
						}
					 }
				 }
			 }
		}
		map.put("assListItems", assListItems);
		//参与人
		List<Map<String, Object>> assListRelations=new ArrayList<>();
		List<AssListRelation> relations=assList.getAssListRelations();
		for (AssListRelation assListRelation : relations) {
			Map<String, Object> relationsMap=new HashMap<>();
			relationsMap.put("headImgUrl", assListRelation.getAssChildMember().getHeadImgUrl());
			assListRelations.add(relationsMap);
		}
		map.put("assListRelations", assListRelations);
		
		return map;
	}
	
	
	private List<Map<String, Object>> dealAssListRemarks(List<AssListRemarks> list) {
		List<Map<String, Object>> assRemarksList=new ArrayList<>();
		for (AssListRemarks assListRemarks : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("assRemarksListId", assListRemarks.getId());
			map.put("name", assListRemarks.getName());
			map.put("annex" , assListRemarks.getAnnex());
			map.put("headImgUrl", assListRemarks.getAssChildMember().getHeadImgUrl());
			map.put("remarksDate" , assListRemarks.getCreateDate());
			map.put("description" , assListRemarks.getDescription());
			assRemarksList.add(map);
		}
		return assRemarksList;
	}
	
	
	private List<Map<String, Object>> dealAssListRelation(List<AssListRelation> list){
		List<Map<String, Object>> assListRelations=new ArrayList<>();
		for (AssListRelation assListRelation : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("assListRelationId", assListRelation.getId());
			map.put("headImgUrl", assListRelation.getAssChildMember().getHeadImgUrl());
			map.put("name", assListRelation.getAssChildMember().getNickName());
			map.put("type", assListRelation.getType());
			map.put("assListRelationDate", assListRelation.getCreateDate());
			assListRelations.add(map);
		}
		return assListRelations;
	}
	
	/**
	 * 采购清单看板
	 * @param unionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/purchaseListKanban" , method = RequestMethod.GET)
	public JsonEntity purchaseListKanban(String unionId) {
		if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<Integer , List<AssListStatisticsDto>> assListStatisticsDtos = assListService.purchaseListKanban(assChildMember);
		
		return JsonEntity.successMessage(assListStatisticsDtos);
	}
	
	/**
	 * 采购清单看板中按月查询采购清单
	 * @param pageable
	 * @param unionId
	 * @param searchValue 查询条件
	 * @param startDate 月开始时间
	 * @param endDate 月结束时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findPressMonthPurchaseList" , method = RequestMethod.GET)
	public JsonEntity findPressMonthPurchaseList(Pageable pageable,String unionId,String searchValue,int year, int month) {
		if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Date startDate = DateUtils.specifyMonthStartTime(year, month);
		Date endDate = DateUtils.specifyMonthEndTime(year, month);
		Page<AssList> page = assListService.findPage(pageable, searchValue, assChildMember, startDate, endDate);
		List<AssList> assLists = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		for(AssList assList : assLists) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", assList.getId());
			map.put("sn", assList.getSn());
			map.put("clientName", assList.getClientName());
			map.put("createDate", assList.getCreateDate());
			map.put("theme", assList.getAssCustomerRelation().getTheme());
			maps.add(map);
		}
		resultMap.put("assLists", maps);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 采购清单统计(商品看板)
	 * @param unionId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/assListStatistics" , method = RequestMethod.GET)
	public JsonEntity assListStatistics(String unionId , String type) {
		if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Date startDate = null;
		Date endDate = null;
		if (StringUtils.isNotBlank(type)) {
			if (StringUtils.equalsIgnoreCase(type, "thisWeek")) {//本周
				startDate=DateUtils.startThisWeek();
				endDate=DateUtils.endOfTheWeek();
			}else if (StringUtils.equalsIgnoreCase(type, "lastWeek")) {//上周
				startDate=DateUtils.lastWeekStartTime();
				endDate=DateUtils.lastWeekEndTime();
			}else if (StringUtils.equalsIgnoreCase(type, "lastMonth")) {//上月
				startDate=DateUtils.lastMonthStartTime();
				endDate=DateUtils.lastMonthEndTime();
			}else if (StringUtils.equalsIgnoreCase(type, "thisMonth")) {//本月
				startDate=DateUtils.startThisMonth();
				endDate=DateUtils.theEndOfTheMonth();
			}
		}else {
			startDate=DateUtils.startThisWeek();
			endDate=DateUtils.endOfTheWeek();
		}
		List<AssPurchaseListStatisticsDto> dtos = assListService.assListStatistics(startDate, endDate, assChildMember, AssCustomerRelation.ShareType.share);
		Integer count = 0;
		for(AssPurchaseListStatisticsDto dto : dtos) {
			count+=dto.getNumber();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dtos", dtos);
		map.put("count", count);
		return JsonEntity.successMessage(map);
	}
	
	/**
	 *  采购清单统计详情(商品看板)
	 * @param unionId
	 * @param startDate
	 * @param endDate
	 * @param searchValue
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/assListStatisticsDetails" , method = RequestMethod.GET)
	public JsonEntity assListStatisticsDetails(String unionId, String startDate, String endDate, String searchValue, Pageable pageable) {
		if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Page<AssListAndCustomerRelationDto> page = assListService.assListStatisticsDetails(assChildMember, startDate, endDate, searchValue, AssCustomerRelation.ShareType.share, pageable);
		List<AssListAndCustomerRelationDto> dtos = page.getContent();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		for(AssListAndCustomerRelationDto dto : dtos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dto.getAssList().getId());
			map.put("clientName", dto.getpClientName());
			map.put("theme", dto.getpTheme());
			map.put("createDate", dto.getAssList().getCreateDate());
			map.put("sn", dto.getAssList().getSn());
			map.put("singlePerson", dto.getAssList().getSinglePerson());
			resultMap.add(map);
		}
		result.put("assLists", resultMap);
		result.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(result);
	}
	
}
