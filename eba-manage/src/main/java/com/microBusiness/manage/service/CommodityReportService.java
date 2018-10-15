package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.dto.CommodityReportDto;
import com.microBusiness.manage.dto.CommodityStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Supplier;

public interface CommodityReportService {
	
	List<CommodityReportDto> findBycommodityReport(Date startDate,Date endDate,Supplier supplier,List<Integer> status,  ChildMember childMember);
	
	CommodityStatisticsDto findByCommodityStatistics(Date startDate,Date endDate,Supplier supplier,List<Integer> status, ChildMember childMember);
	
	List<CommodityReportDto> categoryQuery(Date startDate,Date endDate,Supplier supplier,List<Integer> status, ChildMember childMember);
	
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
	
	/**
	 * 订货单导出
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @param status
	 * @return
	 */
	ExcelView orderExport(Date startDate,Date endDate,Supplier supplier,List<Integer> status,String exportType);
	
	ExcelView downOrder(String fileName , Map<Object, Object> data , String[] titles);
	
	/**
	 * 采购单导出
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @param status
	 * @return
	 */
	ExcelView purchasedGoodsExported(Date startDate,Date endDate,Supplier supplier,List<Integer> status);
	
	ExcelView downpurchasedGoods(String fileName , Map<Object, Object> data , String[] titles);
	
	ExcelView downOrder(String fileName , List<CommodityReportDto> commodityReportDtos , String[] titles);

}
