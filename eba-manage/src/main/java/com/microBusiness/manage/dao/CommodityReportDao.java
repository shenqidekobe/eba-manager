package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.dto.CommodityReportDto;
import com.microBusiness.manage.dto.CommodityStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Supplier;

public interface CommodityReportDao {

	List<CommodityReportDto> findBycommodityReport(Date startDate,Date endDate,Supplier supplier,List<Integer> status, ChildMember childMember);
	
	List<CommodityReportDto> findBycommodityReport2(Date startDate,Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember);
	
	CommodityStatisticsDto findByCommodityStatistics(Date startDate,Date endDate,Supplier supplier,List<Integer> status, ChildMember childMember);
	
	/**
	 * 按分类查询
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @param status
	 * @return
	 */
	List<CommodityReportDto> categoryQuery(Date startDate,Date endDate,Supplier supplier,List<Integer> status, ChildMember childMember);
	
	List<CommodityReportDto> categoryQuery2(Date startDate, Date endDate,Supplier supplier, List<Integer> status, ChildMember childMember);
	
	/**
	 * 采购单商品数据
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @param status
	 * @return
	 */
	List<CommodityReportDto> purchaseOrderData(Date startDate,Date endDate,Supplier supplier,List<Integer> status);
	
	/**
	 * 采购单商品统计
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @param status
	 * @return
	 */
	CommodityStatisticsDto purchasingCommodityStatistics(Date startDate,Date endDate,Supplier supplier,List<Integer> status);

	


}
