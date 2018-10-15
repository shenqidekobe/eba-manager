package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Supplier;

public interface OrderReportService {
	/**
	 * 查询订货单或采购单报表
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @param queyType 查询类型     orderForm：订货单报表    purchaseForm：购货单报表
	 * @return
	 */
	List<OrderReportDto> queryOrderReportByDate(List<Integer> status,Date startDate, Date endDate, 
			Supplier supplier, String queyType, ChildMember childMember);
	
	/**
	 * 获取订货单的总客户数或采购单的总采购供应商数
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @param queyType 查询类型     orderForm：订货单报表    purchaseForm：购货单报表
	 * @return
	 */
	Integer getCustomersNumber(List<Integer> status,Date startDate, Date endDate,Supplier supplier,String queyType);
	
	/**
	 * 导出订货单或购货单报表
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @param queyType 查询类型     orderForm：订货单报表    purchaseForm：购货单报表
	 */
	ExcelView exportOrderReport(List<Integer> status, Date startDate, Date endDate,Supplier supplier,String queyType,HttpServletRequest request , HttpServletResponse response);

}
