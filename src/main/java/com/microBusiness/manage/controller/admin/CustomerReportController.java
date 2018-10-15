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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.microBusiness.manage.dto.CustomerReportDto;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.CustomerReportService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

@Controller("adminCustomerReportController")
@RequestMapping("/admin/customerReport")
public class CustomerReportController  extends BaseController{

	@Resource(name="customerReportServiceImpl")
	private CustomerReportService customerReportService;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index() {

		return "/admin/customerReport/index";
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(Order.Status[] statuses, Date startDate, Date endDate,String type) {
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
		List<CustomerReportDto> list=customerReportService.queryCustomerReportByDate(statusList, startDate, endDate, supplier);
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("startDate", DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2));
		map.put("endDate",  DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2));
		map.put("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return map;
	}
	
	@RequestMapping(value = "/exportOrderReport", method = RequestMethod.GET)
	public ModelAndView exportOrderReport(Order.Status[] statuses, Date startDate, Date endDate,HttpServletRequest request , HttpServletResponse response){
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
		
		return new ModelAndView(customerReportService.exportCustomerReport(statusList, startDate, endDate, supplier, request, response));
		
	}
}
