package com.microBusiness.manage.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.util.DateUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("homePageController")
@RequestMapping("/admin/homePage")
public class HomePageController extends BaseController {

	@Resource
	private OrderService orderService;
	
	@RequestMapping(value = "index" , method = RequestMethod.GET)
	public String index(ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		/**
		 * 获取订货单信息
		 */
		//根据订单状态查询当前企业下待处理订单数
		//等待审核
		Integer moderated = orderService.searchByStatus(Order.Status.pendingReview, supplier);
		//等待发货
		Integer waitForDelivery = orderService.searchByStatus(Order.Status.pendingShipment, supplier);
		//发货中
		Integer InDelivery = orderService.searchByStatus(Order.Status.inShipment, supplier);
		//已取消
		Integer cancel = orderService.searchByStatus(Order.Status.canceled, supplier);
		model.addAttribute("moderated", moderated);
		model.addAttribute("waitForDelivery", waitForDelivery);
		model.addAttribute("InDelivery", InDelivery);
		model.addAttribute("cancel", cancel);
		/*
		 * 获取今天订货单数和订货单总金额
		 */
		Date startToday = DateUtils.currentStartTime();
		Date endToday = DateUtils.currentEndTime();
		OrderStatisticsDto todayOrderForm = orderService.todayOrderForm(supplier, startToday, endToday);
		model.addAttribute("todayOrderForm", todayOrderForm);
		/*
		 * 查询订货单相关内容
		 */
		OrderStatisticsDto orderRelated = orderService.orderRelated(supplier);
		model.addAttribute("orderRelated", orderRelated);
		/**
		 * 获取采购单信息
		 */
		//等待审核
		Integer moderateds = orderService.purchaseOrderByStaticQuery(Order.Status.pendingReview, supplier);
		//等待发货
		Integer waitForDeliverys = orderService.purchaseOrderByStaticQuery(Order.Status.pendingShipment, supplier);
		//发货中
		Integer InDeliverys = orderService.purchaseOrderByStaticQuery(Order.Status.inShipment, supplier);
		//已取消
		Integer cancels = orderService.purchaseOrderByStaticQuery(Order.Status.canceled, supplier);
		model.addAttribute("moderateds", moderateds);
		model.addAttribute("waitForDeliverys", waitForDeliverys);
		model.addAttribute("InDeliverys", InDeliverys);
		model.addAttribute("cancels", cancels);
		/*
		 * 获取今天采购单数和采购单金额
		 */
		OrderStatisticsDto todayPurchaseOrder = orderService.todayPurchaseOrder(supplier, startToday, endToday);
		model.addAttribute("todayPurchaseOrder", todayPurchaseOrder);
		/*
		 * 查询采购单相关内容
		 */
		OrderStatisticsDto purchaseOrderRelated = orderService.purchaseOrderRelated(supplier);
		model.addAttribute("purchaseOrderRelated", purchaseOrderRelated);
		return "/admin/homePage/index";
	}
	
	/**
	 * 订货单走势图
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "orderharts" , method = RequestMethod.GET)
	public Map<String, Object> orderharts(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Supplier supplier = super.getCurrentSupplier();
		Date startDate = null;
		Date endDate = null;
		if(type.equalsIgnoreCase("thisWeek")) {
			startDate = DateUtils.startThisWeek();
			endDate = DateUtils.endOfTheWeek();
		}
		if(type.equalsIgnoreCase("thisMonth")) {
			startDate = DateUtils.startThisMonth();
			endDate = DateUtils.theEndOfTheMonth();
		}
		List<OrderStatisticsDto> orderStatisticsDtos = orderService.orderharts(supplier, startDate, endDate);
		map.put("orderStatisticsDtos", orderStatisticsDtos);
		return map;
	}
	
	/**
	 * 采购单走势图
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "purchaseOrderCharts" , method = RequestMethod.GET)
	public Map<String, Object> purchaseOrderCharts(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Supplier supplier = super.getCurrentSupplier();
		Date startDate = null;
		Date endDate = null;
		if(type.equalsIgnoreCase("thisWeek")) {
			startDate = DateUtils.startThisWeek();
			endDate = DateUtils.endOfTheWeek();
		}
		if(type.equalsIgnoreCase("thisMonth")) {
			startDate = DateUtils.startThisMonth();
			endDate = DateUtils.theEndOfTheMonth();
		}
		List<OrderStatisticsDto> orderStatisticsDtos = orderService.purchaseOrderCharts(supplier, startDate, endDate);
		map.put("dto", orderStatisticsDtos);
		return map;
	}
	
}
