package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.dto.CustomerReportDto;
import com.microBusiness.manage.entity.Supplier;

public interface CustomerReportService {
	/**
	 * 查询订货客户报表
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @return
	 */
	List<CustomerReportDto> queryCustomerReportByDate(List<Integer> status, Date startDate, Date endDate,	Supplier supplier);
	
	

	/**
	 * 导出订货单或购货单报表
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @param queyType 查询类型     orderForm：订货单报表    purchaseForm：购货单报表
	 */
	ExcelView exportCustomerReport(List<Integer> status, Date startDate, Date endDate,Supplier supplier,HttpServletRequest request , HttpServletResponse response);
}
