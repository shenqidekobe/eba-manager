/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.OrderItemService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

/**
 * 我的代理
 * @author suxiaozhen
 *
 */
@Controller("apiProxyUserController")
@RequestMapping("/api/small/proxy_user")
public class ProxyUserController extends BaseController {

	@Resource(name = "proxyUserServiceImpl")
	private ProxyUserService proxyUserService;
	@Resource
	private ChildMemberService childMemberService;
	@Resource
	private ReceiverService receiverService;
	@Resource
	private OrderService orderService;
	@Resource
	private OrderItemService orderItemService;
	

	
	@SuppressWarnings("serial")
	@ResponseBody
    @RequestMapping(value = "/getAllProxyUser", method = RequestMethod.GET)
    public JsonEntity getAllProxyUser(String unionId, String smOpenId, Long supplierId) {
    	//Member member = childMemberService.findByUnionId(unionId).getMember();
		//Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		if(supplierId == null){
			supplierId = 1l;
		}
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("childMember");
		filter.setValue(childMember.getId());
		filters.add(filter);
		List<ProxyUser> proxyUserList = proxyUserService.findList(1, filters, null);
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		if(proxyUserList != null && !proxyUserList.isEmpty()){
			ProxyUser proxyUserSelf = proxyUserList.get(0);
			logger.info("myself:" + proxyUserSelf.getId() + ":" + proxyUserSelf.getName());
			List<ProxyUser> proxyUsers = proxyUserService.findChildren(proxyUserSelf, true, 10000);
			if(proxyUsers != null){
				logger.info("myself children size:" +  proxyUsers.size());
				for(final ProxyUser pc : proxyUsers) {
		    		resultMap.add(new HashMap<String, Object>(){{
		    			this.put("id", pc.getId());
		    			this.put("grade", pc.getGrade());
		    			this.put("name", pc.getName());
		    			this.put("parentId", pc.getParent() == null ? null : pc.getParent().getId());
		    			if(pc.getChildMember() != null){
		    				this.put("image", pc.getChildMember().getHeadImgUrl());
		    			}
		    		}});
		    	}
			}
	    	//List<ProxyUser> proxyUsers = proxyUserService.findByAllSupplier(supplierId);
		}
		
    	return JsonEntity.successMessage(resultMap);
    }
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity list(String smOpenId, String searchName) {
		ChildMember childMember  = childMemberService.findBySmOpenId(smOpenId);
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("childMember");
		filter.setValue(childMember.getId());
		filters.add(filter);
		ProxyUser proxyUserSelf = null;
		List<ProxyUser> proxyUserList = proxyUserService.findList(1, filters, null);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("image",childMember.getHeadImgUrl());
		if(proxyUserList != null && !proxyUserList.isEmpty()){
			proxyUserSelf = proxyUserList.get(0);
			rootMap.put("name", proxyUserSelf.getName());
			
			List<Filter> searchfilters = new ArrayList<Filter>();
			Filter filter2 = new Filter();
			filter2.setIgnoreCase(true);
			filter2.setOperator(Operator.eq);
			filter2.setProperty("parent");
			filter2.setValue(proxyUserSelf.getId());
			searchfilters.add(filter2);
			List<com.microBusiness.manage.Order> searchOrders = new ArrayList<com.microBusiness.manage.Order>();
			if(StringUtils.isNotEmpty(searchName)){
				filter2 = new Filter();
				filter2.setIgnoreCase(true);
				filter2.setOperator(Operator.like);
				filter2.setProperty("name");
				filter2.setValue("%" + searchName + "%");
				searchfilters.add(filter2);
			}
			com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
			order.setDirection(Direction.asc);
			order.setProperty("order");
			searchOrders.add(order);
			List<ProxyUser> plist = proxyUserService.findList(0, 1000, searchfilters, searchOrders);
			if(plist == null || plist.isEmpty()){
				if(StringUtils.isNotEmpty(searchName)){
					filter2 = new Filter();
					filter2.setIgnoreCase(true);
					filter2.setOperator(Operator.like);
					filter2.setProperty("tel");
					filter2.setValue("%" + searchName + "%");
					searchfilters.add(filter2);
					plist = proxyUserService.findList(0, 1000, searchfilters, searchOrders);
				}
			}
			//List<ProxyUser> plist = proxyUserService.findChildren(proxyUserSelf, false, 10000);
			if(plist != null){
				for (ProxyUser proxyUser : plist) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", proxyUser.getId());
					map.put("name", proxyUser.getName());
					map.put("tel", proxyUser.getTel());
					map.put("childrenSize", proxyUser.getChildren().size());
					if(proxyUser.getChildMember() != null){
						map.put("image", proxyUser.getChildMember().getHeadImgUrl());
					}
					list.add(map);
				}
			}
		}
		List<ProxyUser> pAllChildrenList = proxyUserService.findChildren(proxyUserSelf, true, 10000);
		int no2Count = 0;
		int no3Count = 0;
		if(list != null){
			for (ProxyUser proxyUser : pAllChildrenList) {
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
		proxyUserSelf.setGroupCount(pAllChildrenList.size());
		rootMap.put("groupCount", proxyUserSelf.getGroupCount());
		rootMap.put("no2Count", no2Count);
		rootMap.put("no3Count", no3Count);
		rootMap.put("list", list);
		return JsonEntity.successMessage(rootMap);
	}
	
	@RequestMapping(value = "/listChildren", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity listChildren(String smOpenId, Long proxyUserId, String searchName) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		//List<ProxyUser> plist =  proxyUserService.findChildren(proxyUserId, false, 1000, false);
		List<Filter> searchfilters = new ArrayList<Filter>();
		Filter filter2 = new Filter();
		filter2.setIgnoreCase(true);
		filter2.setOperator(Operator.eq);
		filter2.setProperty("parent");
		filter2.setValue(proxyUserId);
		searchfilters.add(filter2);
		List<com.microBusiness.manage.Order> searchOrders = new ArrayList<com.microBusiness.manage.Order>();
		if(StringUtils.isNotEmpty(searchName)){
			filter2 = new Filter();
			filter2.setIgnoreCase(true);
			filter2.setOperator(Operator.like);
			filter2.setProperty("name");
			filter2.setValue("%" + searchName + "%");
			searchfilters.add(filter2);
		}
		com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
		order.setDirection(Direction.asc);
		order.setProperty("order");
		searchOrders.add(order);
		List<ProxyUser> plist = proxyUserService.findList(0, 1000, searchfilters, searchOrders);
		if(plist == null || plist.isEmpty()){
			if(StringUtils.isNotEmpty(searchName)){
				filter2 = new Filter();
				filter2.setIgnoreCase(true);
				filter2.setOperator(Operator.like);
				filter2.setProperty("tel");
				filter2.setValue("%" + searchName + "%");
				searchfilters.add(filter2);
				plist = proxyUserService.findList(0, 1000, searchfilters, searchOrders);
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(plist != null){
			for (ProxyUser proxyUser : plist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", proxyUser.getId());
				map.put("name", proxyUser.getName());
				map.put("tel", proxyUser.getTel());
				if(proxyUser.getChildMember() != null){
					map.put("image", proxyUser.getChildMember().getHeadImgUrl());
				}
				list.add(map);
			}
		}
		rootMap.put("childrenSize", plist.size());
		rootMap.put("list", list);
		return JsonEntity.successMessage(rootMap);
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity detail(String smOpenId, Long proxyUserId) {
		//ChildMember childMember  = childMemberService.findBySmOpenId(smOpenId);
		//Member member = childMember.getMember();
		ProxyUser proxyUser = proxyUserService.find(proxyUserId);
		Member me = proxyUser.getChildMember().getMember();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Receiver> receiverList = receiverService.find(me, true);
		if(receiverList != null && !receiverList.isEmpty()){
			Receiver receiver = receiverList.get(0);
			map.put("address", receiver.getAreaName() + receiver.getAddress());
		}
		map.put("id", proxyUser.getId());
		map.put("name", proxyUser.getName());
		map.put("createDate", DateUtils.formatDateToString(proxyUser.getCreateDate(), DateformatEnum.yyyyMMdd2));
		map.put("tel", proxyUser.getTel());
		if(proxyUser.getChildMember() != null){
			map.put("image", proxyUser.getChildMember().getHeadImgUrl());
		}
		map.put("gender", proxyUser.getGender());
		
		
		long orderCount = orderService.count(null, Order.Status.shipped, me,
				null, null, null,
				null, null, null, null);
		long orderCount2 = orderService.count(null, Order.Status.received, me,
				null, null, null,
				null, null, null, null);
		long orderCount3 = orderService.count(null, Order.Status.completed, me,
				null, null, null,
				null, null, null, null);
		
		List<Order> allOrderList = orderService.findList(null, Order.Status.shipped, me,
				null, null, null, null,
				null, null, null, 
				20000, null, null);
		List<Order> allOrderList2 = orderService.findList(null, Order.Status.received, me,
				null, null, null, null,
				null, null, null, 
				20000, null, null);
		List<Order> allOrderList3 = orderService.findList(null, Order.Status.completed, me,
				null, null, null, null,
				null, null, null, 
				20000, null, null);
		int saleGoodCount = 0;
		if(allOrderList != null){
			if(allOrderList2 != null){
				allOrderList.addAll(allOrderList2);
			}
			if(allOrderList3 != null){
				allOrderList.addAll(allOrderList3);
			}
			for (Order order : allOrderList) {
				List<OrderItem> items = order.getOrderItems();
				for (OrderItem orderItem : items) {
					saleGoodCount += orderItem.getQuantity();
				}
			}
		}
		map.put("saleGoodCount", saleGoodCount);
		map.put("orderCount", orderCount+orderCount2+orderCount3);
		return JsonEntity.successMessage(map);
	}


	
	@RequestMapping(value = "/getSaleGoodsListSelf", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getSaleGoodsListSelf(String smOpenId, Long proxyUserId, Pageable pageable, Date startDate, Date endDate) {
		//ChildMember childMember  = childMemberService.findBySmOpenId(smOpenId);
		//Member member = childMember.getMember();
		ProxyUser proxyUser = proxyUserService.find(proxyUserId);
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<OrderItem> list = orderItemService.findPageForGroupGoods(pageable, proxyUser, startDate, endDate);
		//int totalCount = orderItemService.sumPageForGroupGoods(proxyUser, startDate, endDate);
		int totalCount = 0;
		if(list != null){
			for (OrderItem orderItem : list) {
				Map<String, Object> itemmap = new HashMap<String, Object>();
				Product product = orderItem.getProduct();
				itemmap.put("goodsId", product.getGoods().getId());
				itemmap.put("productId", product.getId());
				itemmap.put("quantity", orderItem.getQuantity());
				itemmap.put("name", orderItem.getName());
				itemmap.put("image", product.getImage());
				itemmap.put("specification", product.getSpecificationValues());
				maplist.add(itemmap);
				totalCount+= orderItem.getQuantity();
			}
		}
		
		map.put("totalCount", totalCount);
		map.put("itemList", maplist);
		
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 出货统计
	 * @param smOpenId
	 * @param proxyUserId
	 * @param pageable
	 * @param startDate
	 * @param endDate
	 * @param ts
	 * @return
	 */
	@RequestMapping(value = "/getSaleGoodsList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getSaleGoodsList(String smOpenId, Long proxyUserId, Pageable pageable, 
			Date startDate, Date endDate, String ts) {
		//ChildMember childMember  = childMemberService.findBySmOpenId(smOpenId);
		//Member member = childMember.getMember();
		//ProxyUser proxyUser = proxyUserService.find(proxyUserId);
		
		if(null != startDate || null != endDate) {
			startDate = DateUtils.specifyDateZero(startDate);
			endDate = DateUtils.specifyDatetWentyour(endDate);
		}
		if(null != ts) {
			if(ts.equalsIgnoreCase("thisWeek")) {
				startDate = DateUtils.startThisWeek();
				endDate = DateUtils.endOfTheWeek();
			};
			if(ts.equalsIgnoreCase("lastWeek")) {
				startDate = DateUtils.lastWeekStartTime();
				endDate = DateUtils.lastWeekEndTime();
			};
			if(ts.equalsIgnoreCase("lastMonth")) {
				startDate = DateUtils.lastMonthStartTime();
				endDate = DateUtils.lastMonthEndTime();
			};
			if(ts.equalsIgnoreCase("thisMonth")) {
				startDate = DateUtils.startThisMonth();
				endDate = DateUtils.theEndOfTheMonth();
			};
		}
		if(null == startDate || null == endDate) {
			startDate = DateUtils.startThisWeek();
			endDate = DateUtils.endOfTheWeek();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ProxyUser> proxyList = proxyUserService.findChildren(proxyUserId, true, 10000, false);
		
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<OrderItem> list = orderItemService.findPageForGroupGoods(pageable, proxyList, startDate, endDate);
		int totalCount = orderItemService.sumPageForGroupGoods(proxyList, startDate, endDate);
		if(list != null){
			for (OrderItem orderItem : list) {
				Map<String, Object> itemmap = new HashMap<String, Object>();
				Product product = orderItem.getProduct();
				itemmap.put("goodsId", product.getGoods().getId());
				itemmap.put("productId", product.getId());
				itemmap.put("quantity", orderItem.getQuantity());
				itemmap.put("name", orderItem.getName());
				itemmap.put("image", product.getImage());
				maplist.add(itemmap);
			}
		}
		
		map.put("totalCount", totalCount);
		map.put("itemList", maplist);
		
		return JsonEntity.successMessage(map);
	}
	
	
	
	



}