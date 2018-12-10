package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderNewsPushService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("orderNewsPushController")
@RequestMapping("/admin/orderNewsPush")
public class OrderNewsPushController extends BaseController {
	
	@Resource
	private OrderNewsPushService newsPushService;
	
	/**
	 * 订货单
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/supply" , method = RequestMethod.GET)
	public String supply(Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.order, pageable));
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);
		return "/admin/orderNewsPush/supply";
	}
	
	/**
	 * 采购单
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/purchase", method = RequestMethod.GET)
	public String purchase(Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		/*model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.purchase, pageable));
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);*/
		
		model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.withdraw, pageable));
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.withdraw, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);
		return "/admin/orderNewsPush/purchase";
	}
	
	
	/**
	 * 订货单全部标记为已读
	 * @return
	 */
	@RequestMapping(value = "/allMarkAsReadByOrder" , method = RequestMethod.POST)
	public String allMarkAsReadByOrder(Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		if(null == supplier) {
			return ERROR_VIEW;
		}
		newsPushService.update(supplier, OrderNewsPush.NoticeObject.order);
		model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.order, pageable));

		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);
		return "/admin/orderNewsPush/supply";
	}
	
	/**
	 * 采购单全部标记为已读
	 * @return
	 */
	@RequestMapping(value = "/allMarkAsReadByPurchase" , method = RequestMethod.POST)
	public String allMarkAsReadByPurchase(Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		if(null == supplier) {
			return ERROR_VIEW;
		}
		/*newsPushService.update(supplier, OrderNewsPush.NoticeObject.purchase);
		model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.purchase, pageable));
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);*/
		
		newsPushService.update(supplier, OrderNewsPush.NoticeObject.withdraw);
		model.addAttribute("page", newsPushService.findPage(supplier, null, null, OrderNewsPush.NoticeObject.withdraw, pageable));
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.withdraw, null).getTotal();
		model.addAttribute("orderTotal", orderTotal);
		model.addAttribute("purchaseTotal", purchaseTotal);
		return "/admin/orderNewsPush/purchase";
	}
	
	/**
	 * 点击时标记为已读
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStatus" , method = RequestMethod.POST)
	public JsonEntity updateStatus(Long id) {
		if(null == id) {
			return new JsonEntity("101001" , "数据错误");
		}
		OrderNewsPush newsPush = newsPushService.find(id);
		if(!newsPush.getStatus().equals(OrderNewsPush.Status.haveRead)) {
			newsPush.setStatus(OrderNewsPush.Status.haveRead);
			newsPushService.update(newsPush);
		}
		return JsonEntity.successMessage();
	}
	
	
	/**
	 * 订货单点击时标记为已读
	 * @param id
	 */
	/*@ResponseBody
	@RequestMapping(value = "/updateARecordByOrder" , method = RequestMethod.POST)
	public JsonEntity updateARecordByOrder(Long id) {
		if(null == id) {
			return new JsonEntity("101001" , "数据错误");
		}
		OrderNewsPush newsPush = newsPushService.find(id);
		if(!newsPush.getStatus().equals(OrderNewsPush.Status.haveRead)) {
			newsPush.setStatus(OrderNewsPush.Status.haveRead);
			newsPushService.update(newsPush);
		}
		return JsonEntity.successMessage();
	}*/
	
	/**
	 * 采购单点击时标记为已读
	 * @param id
	 */
	/*@ResponseBody
	@RequestMapping(value = "/updateARecordByPurchase" , method = RequestMethod.POST)
	public JsonEntity updateARecordByPurchase(Long id) {
		if(null == id) {
			return new JsonEntity("101001" , "数据错误");
		}
		OrderNewsPush newsPush = newsPushService.find(id);
		if(!newsPush.getPurchaseViewStatus().equals(OrderNewsPush.PurchaseViewStatus.haveRead)) {
			newsPush.setPurchaseViewStatus(OrderNewsPush.PurchaseViewStatus.haveRead);
			newsPushService.update(newsPush);
		}
		return JsonEntity.successMessage();
	}*/
	
	/**
	 * 主页订货单全部标记为已读
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/allMarkAsReadByOrders" , method = RequestMethod.POST)
	public JsonEntity allMarkAsReadByOrders(Pageable pageable) {
		Supplier supplier = super.getCurrentSupplier();
		if(null == supplier) {
			return new JsonEntity("101001" , "用户不存在");
		}
		newsPushService.update(supplier, OrderNewsPush.NoticeObject.order);
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderTotal", orderTotal);
		map.put("purchaseTotal", purchaseTotal);
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 主页采购单全部标记为已读
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/allMarkAsReadByPurchases" , method = RequestMethod.POST)
	public JsonEntity allMarkAsReadByPurchases(Pageable pageable , ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		if(null == supplier) {
			return new JsonEntity("101001" , "用户不存在");
		}
		/*newsPushService.update(supplier, OrderNewsPush.NoticeObject.purchase);
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.purchase, null).getTotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderTotal", orderTotal);
		map.put("purchaseTotal", purchaseTotal);*/
		
		newsPushService.update(supplier, OrderNewsPush.NoticeObject.withdraw);
		Long orderTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, null).getTotal();
		Long purchaseTotal = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.withdraw, null).getTotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderTotal", orderTotal);
		map.put("purchaseTotal", purchaseTotal);
		return JsonEntity.successMessage(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/homeIconData" , method = RequestMethod.GET)
	public JsonEntity homeIconData(Pageable pageable) {
		Supplier supplier = super.getCurrentSupplier();
		Page<OrderNewsPush> orderpage = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.order, pageable);
		//Page<OrderNewsPush> orderpage = newsPushService.findPageOrder(supplier,OrderNewsPush.Mark.purchaseOrder,OrderNewsPush.Status.unread, pageable);
		List<Object> orderList = new ArrayList<Object>();
		
		for(OrderNewsPush orderNewsPush : orderpage.getContent()) {
			Map<String, Object> orderMap = new HashMap<String, Object>();
			OrderNewsPush.OrderStatus orderStatus = orderNewsPush.getOrderStatus();
			orderMap.put("supplier", orderNewsPush.getSupplier().getName());
			//orderMap.put("need",orderNewsPush.getNeed().getName());
			orderMap.put("orderId",orderNewsPush.getOrder().getId());
			orderMap.put("orderSn",orderNewsPush.getOrder().getSn());
			orderMap.put("id",orderNewsPush.getId());
			orderMap.put("orderStatus", orderStatus);
			orderMap.put("status", orderNewsPush.getStatus());
			orderMap.put("createDate", orderNewsPush.getCreateDate());
			orderMap.put("send", orderNewsPush.getSend());
			orderMap.put("receive", orderNewsPush.getReceive());
			if(orderNewsPush.getOrder().getType() == Order.Type.distribution) {
				orderMap.put("orderType", 1);
			}else {
				orderMap.put("orderType", 0);
			}
			
			if(orderStatus == OrderNewsPush.OrderStatus.placeAnOrder) {
				orderMap.put("statusName", "下单");
			}else if(orderStatus == OrderNewsPush.OrderStatus.applicationCancel) {
				orderMap.put("statusName", "订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.leaveAMessage) {
				orderMap.put("statusName", "订单留言");
			}else if(orderStatus == OrderNewsPush.OrderStatus.modify) {
				orderMap.put("statusName", "订单进行了修改");
			}else if(orderStatus == OrderNewsPush.OrderStatus.complete) {
				orderMap.put("statusName", "订单完成");
			}else if(orderStatus == OrderNewsPush.OrderStatus.reviewBy) {
				orderMap.put("statusName", "订单审核通过");
			}else if(orderStatus == OrderNewsPush.OrderStatus.refuse) {
				orderMap.put("statusName", "订单审核拒绝");
			}else if(orderStatus == OrderNewsPush.OrderStatus.deliverGoods){
				orderMap.put("statusName", "订单发货");
			}else if(orderStatus == OrderNewsPush.OrderStatus.agreeApplicationCancel) {
				orderMap.put("statusName", "同意订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.refuseApplicationCancel) {
				orderMap.put("statusName", "拒绝订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.cancel) {
				orderMap.put("statusName", "取消订单");
			}
			orderList.add(orderMap);
		}
		Page<OrderNewsPush> purchaseOrderpage = newsPushService.findPage(supplier, OrderNewsPush.Status.unread, null, OrderNewsPush.NoticeObject.withdraw, pageable);
		//Page<OrderNewsPush> purchaseOrderpage = newsPushService.findPageByPurchaseOrder(supplier,OrderNewsPush.Mark.order,OrderNewsPush.PurchaseViewStatus.unread, pageable);
		List<Object> purchaseList = new ArrayList<Object>();
		
		for(OrderNewsPush orderNewsPush : purchaseOrderpage.getContent()) {
			Map<String, Object> purchaseMap = new HashMap<String, Object>();
			OrderNewsPush.OrderStatus orderStatus = orderNewsPush.getOrderStatus();
			purchaseMap.put("supplier", orderNewsPush.getSupplier().getName());
			//purchaseMap.put("need", orderNewsPush.getNeed().getName());
			purchaseMap.put("orderId", orderNewsPush.getLinkId());
			purchaseMap.put("orderSn", orderNewsPush.getSn());
			purchaseMap.put("id", orderNewsPush.getId());
			purchaseMap.put("orderStatus", orderStatus);
			purchaseMap.put("status", orderNewsPush.getStatus());
			purchaseMap.put("createDate", orderNewsPush.getCreateDate());
			purchaseMap.put("send", orderNewsPush.getSend());
			purchaseMap.put("receive", orderNewsPush.getReceive());
			
			if(orderStatus == OrderNewsPush.OrderStatus.placeAnOrder) {
				purchaseMap.put("statusName", "下单");
			}else if(orderStatus == OrderNewsPush.OrderStatus.applicationCancel) {
				purchaseMap.put("statusName", "订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.leaveAMessage) {
				purchaseMap.put("statusName", "提现申请");
			}else if(orderStatus == OrderNewsPush.OrderStatus.modify) {
				purchaseMap.put("statusName", "订单进行了修改");
			}else if(orderStatus == OrderNewsPush.OrderStatus.complete) {
				purchaseMap.put("statusName", "订单完成");
			}else if(orderStatus == OrderNewsPush.OrderStatus.reviewBy) {
				purchaseMap.put("statusName", "订单审核通过");
			}else if(orderStatus == OrderNewsPush.OrderStatus.refuse) {
				purchaseMap.put("statusName", "订单审核拒绝");
			}else if(orderStatus == OrderNewsPush.OrderStatus.deliverGoods){
				purchaseMap.put("statusName", "订单发货");
			}else if(orderStatus == OrderNewsPush.OrderStatus.agreeApplicationCancel) {
				purchaseMap.put("statusName", "同意订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.refuseApplicationCancel) {
				purchaseMap.put("statusName", "拒绝订单申请取消");
			}else if(orderStatus == OrderNewsPush.OrderStatus.cancel) {
				purchaseMap.put("statusName", "取消订单");
			}
			purchaseList.add(purchaseMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderTotal", orderpage.getTotal());
		map.put("orderList", orderList);
		map.put("purchaseTotal", purchaseOrderpage.getTotal());
		map.put("purchaseList", purchaseList);
		return JsonEntity.successMessage(map);
	}
	
	

}
