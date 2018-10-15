package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderReportService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

@Controller("adminOrderReportController")
@RequestMapping("/admin/orderReport")
public class OrderReportController extends BaseController {
	@Resource(name="orderReportServiceImpl")
	private OrderReportService orderReportService;
	
	@Resource
	private ProxyUserService proxyUserService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		List<ProxyUser> list = proxyUserService.findTree(super.getCurrentSupplier(), null);
		model.addAttribute("proxyUserTree", list);
		return "/admin/orderReport/orderForm";
	}
	
	@RequestMapping(value = "purchaseFormIndex", method = RequestMethod.GET)
	public String purchaseFormIndex() {
		
		return "/admin/orderReport/purchaseForm";
	}

	@RequestMapping(value = "orderList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderList(Order.Status[] statuses, Date startDate, 
			Date endDate,String type,String queyType, Long proxyUserId) {
		
		ChildMember childMember = null;
		if(proxyUserId != null && proxyUserId.longValue() != 0){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}
		
		List<Integer> statusList=new ArrayList<>();
		if (statuses != null) {
			for (int i = 0; i < statuses.length; i++) {
				statusList.add(statuses[i].ordinal());
			}
		}
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
			//设置开始时间为当天00:00:00
			startDate=DateUtils.specifyDateZero(startDate);
			//设置结束时间为当天23:59:59
			endDate=DateUtils.specifyDatetWentyour(endDate);
		}
		Supplier supplier = super.getCurrentSupplier();
		List<OrderReportDto> list = orderReportService.queryOrderReportByDate(statusList, startDate, endDate,
				supplier, queyType, childMember);
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("customersSum", orderReportService.getCustomersNumber(statusList, startDate, endDate, supplier, queyType));
		map.put("startDate", DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2));
		map.put("endDate",  DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2));
		map.put("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return map;
	}
	
	@RequestMapping(value = "/exportOrderReport", method = RequestMethod.GET)
	public ModelAndView exportOrderReport(Order.Status[] statuses, Date startDate, Date endDate, String queyType,HttpServletRequest request , HttpServletResponse response){
		List<Integer> statusList=new ArrayList<>();
		if (statuses != null) {
			for (int i = 0; i < statuses.length; i++) {
				statusList.add(statuses[i].ordinal());
			}
		}
		
		//设置开始时间为当天00:00:00
		startDate=DateUtils.specifyDateZero(startDate);
		//设置结束时间为当天23:59:59
		endDate=DateUtils.specifyDatetWentyour(endDate);
		
		Supplier supplier = super.getCurrentSupplier();
		
		return new ModelAndView(orderReportService.exportOrderReport(statusList, startDate, endDate, supplier, queyType, request, response));
		
	}
	
	
	@RequestMapping(value = "purchaseList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> purchaseList(Order.Status[] statuses, Date startDate, Date endDate, 
			String type, String queyType) {
		List<Integer> statusList=new ArrayList<>();
		if (statuses != null) {
			for (int i = 0; i < statuses.length; i++) {
				statusList.add(statuses[i].ordinal());
			}
		}
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
			//设置开始时间为当天00:00:00
			startDate=DateUtils.specifyDateZero(startDate);
			//设置结束时间为当天23:59:59
			endDate=DateUtils.specifyDatetWentyour(endDate);
		}
		Supplier supplier = super.getCurrentSupplier();
		List<OrderReportDto> list=orderReportService.queryOrderReportByDate(statusList, startDate, endDate, supplier,queyType, null);
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("customersSum", orderReportService.getCustomersNumber(statusList, startDate, endDate, supplier, queyType));
		map.put("startDate", DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2));
		map.put("endDate",  DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2));
		map.put("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return map;
	}
}

