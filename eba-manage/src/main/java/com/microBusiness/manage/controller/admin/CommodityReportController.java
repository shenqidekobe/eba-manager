package com.microBusiness.manage.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.microBusiness.manage.dto.CommodityReportDto;
import com.microBusiness.manage.dto.CommodityStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.CommodityReportService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.util.DateUtils;

/**
 * Created by yuezhiwei on 2017/8/22.
 * 功能描述：订单报表
 * 修改记录：
 */
@Controller("adminCommodityReport")
@RequestMapping("/admin/commodityReport")
public class CommodityReportController extends BaseController {

	@Resource
	private CommodityReportService commodityReportService;
	@Resource
	private ProxyUserService proxyUserService;
	
	@RequestMapping(value = "index" , method = RequestMethod.GET)
	public String index(ModelMap model) {
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		List<ProxyUser> list = proxyUserService.findTree(super.getCurrentSupplier(), null);
		model.addAttribute("proxyUserTree", list);
		return "/admin/commodityReport/orderForm";
	}
	
	/**
	 * 订货单
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "orderForm" , method = RequestMethod.GET)
	public Map<String, Object> orderForm(Date startDate,Date endDate,Order.Status[] status,
			String ts,String search,String exportType, Long proxyUserId) {
		
		ChildMember childMember = null;
		if(proxyUserId != null && proxyUserId.longValue() != 0){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		if(null != search && search.equalsIgnoreCase("query")) {
			if(null != startDate || null != endDate) {
				startDate = DateUtils.specifyDateZero(startDate);
				endDate = DateUtils.specifyDatetWentyour(endDate);
			}
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
		List<Integer> list=new ArrayList<>();
		if(status.length > 0) {
			list = new ArrayList<Integer>();
			for(int i = 0; i < status.length; i++){
				list.add(status[i].ordinal());
		    }
		}
		if(exportType == null) {
			exportType = "good";
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("ts", ts);
		map.put("search", search);
		Supplier supplier = super.getCurrentSupplier();
		map.put("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		//总数
		CommodityStatisticsDto statisticsDto = commodityReportService.findByCommodityStatistics(startDate, endDate, 
				supplier, list, childMember);
		Integer total=0;
		BigDecimal totalAmount=BigDecimal.ZERO;
		if(exportType.equalsIgnoreCase("classify")) {
			List<CommodityReportDto> commodityReportDtos = commodityReportService.categoryQuery(startDate, endDate, 
					supplier, list, childMember);
			Map<String, Object> pieChart = new HashMap<String, Object>();
			List<String> pieList = new ArrayList<String>();
			Integer quantity = 0;
			for(int i=0;i<commodityReportDtos.size();i++) {
				CommodityReportDto dto = commodityReportDtos.get(i);
				if(i > 18) {
					quantity+=dto.getOrderQuantity();
				}else {
					if(dto.getSource() != null) {
						pieChart.put(dto.getName()+"(分销)", dto.getOrderQuantity());
						pieList.add(dto.getName()+"(分销)");
					}else {
						pieChart.put(dto.getName(), dto.getOrderQuantity());
						pieList.add(dto.getName());
					}
					
				}
			}
			if(quantity > 0) {
				pieChart.put("其他", quantity);
				pieList.add("其他");
			}
			map.put("commodityReportDtos", commodityReportDtos);
			map.put("pieChart", pieChart);
			map.put("pieList", pieList);
			
		}else {
			List<CommodityReportDto> commodityReportDtos = commodityReportService.findBycommodityReport(startDate,
					endDate, supplier,list,  childMember);
			Map<String, Object> pieChart = new HashMap<String, Object>();
			List<String> pieList = new ArrayList<String>();
			Integer quantity = 0;
			for(int i=0;i<commodityReportDtos.size();i++) {
				CommodityReportDto dto = commodityReportDtos.get(i);
				total+=dto.getOrderQuantity();
				totalAmount=totalAmount.add(dto.getOrderAmount());
				if(i > 18) {
					quantity+=dto.getOrderQuantity();
				}else {
					if(dto.getSpecification().equalsIgnoreCase("[]")) {
						if(dto.getSource() != null) {
							pieChart.put(dto.getName()+"(分销)", dto.getOrderQuantity());
							pieList.add(dto.getName()+"(分销)");
						}else {
							pieChart.put(dto.getName(), dto.getOrderQuantity());
							pieList.add(dto.getName());
						}
					}else {
						if(dto.getSource() != null) {
							pieChart.put(dto.getName()+dto.getSpecification()+"(分销)", dto.getOrderQuantity());
							pieList.add(dto.getName()+dto.getSpecification()+"(分销)");
						}else {
							pieChart.put(dto.getName()+dto.getSpecification(), dto.getOrderQuantity());
							pieList.add(dto.getName()+dto.getSpecification());
						}
						
					}
				}
			}
			if(quantity > 0) {
				pieChart.put("其他", quantity);
				pieList.add("其他");
			}
			map.put("commodityReportDtos", commodityReportDtos);
			map.put("pieChart", pieChart);
			map.put("pieList", pieList);
		}
		statisticsDto.setTotal(total.toString());
		statisticsDto.setTotalAmount(totalAmount);
		map.put("statisticsDto", statisticsDto);
		map.put("exportType", exportType);
		return map;
	}
	
	/**
	 * 订货单按分类查询
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "categoryQuery" , method = RequestMethod.GET)
	public Map<String, Object> categoryQuery(Date startDate,Date endDate,Order.Status[] status,
			String ts, String search, Long proxyUserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ChildMember childMember = null;
		if(proxyUserId != null && proxyUserId.longValue() != 0){
			ProxyUser proxyUser = proxyUserService.find(proxyUserId);
			childMember = proxyUser.getChildMember();
		}
		if(null != search && search.equalsIgnoreCase("query")) {
			if(null != startDate || null != endDate) {
				startDate = DateUtils.specifyDateZero(startDate);
				endDate = DateUtils.specifyDatetWentyour(endDate);
			}
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
		List<Integer> list=new ArrayList<>();
		if(status != null) {
			list = new ArrayList<Integer>();
			for(int i = 0; i < status.length; i++){
				list.add(status[i].ordinal());
		    }
		}
		Supplier supplier = super.getCurrentSupplier();
		List<CommodityReportDto> commodityReportDtos = commodityReportService.categoryQuery(startDate, endDate,
				supplier, list, childMember);
		Map<String, Object> pieChart = new HashMap<String, Object>();
		List<String> pieList = new ArrayList<String>();
		Integer quantity = 0;
		for(int i=0;i<commodityReportDtos.size();i++) {
			CommodityReportDto dto = commodityReportDtos.get(i);
			if(i > 18) {
				quantity+=dto.getOrderQuantity();
			}else {
				if(dto.getSource() != null) {
					pieChart.put(dto.getName()+"(分销)", dto.getOrderQuantity());
					pieList.add(dto.getName()+"(分销)");
				}else {
					pieChart.put(dto.getName(), dto.getOrderQuantity());
					pieList.add(dto.getName());
				}
			}
		}
		if(quantity > 0) {
			pieChart.put("其他", quantity);
			pieList.add("其他");
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("commodityReportDtos", commodityReportDtos);
		map.put("pieChart", pieChart);
		map.put("pieList", pieList);
		return map;
	}
	
	@RequestMapping(value = "supplyIndex" , method = RequestMethod.GET)
	public String supplyIndex() {
		return "/admin/commodityReport/purchaseOrder";
	}
	
	/**
	 * 采购单
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "purchaseOrder" , method = RequestMethod.GET)
	public Map<String, Object> purchaseOrder(Date startDate,Date endDate,Order.Status[] status,String ts,String search) {
	    Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", status);
            if(null != search && search.equalsIgnoreCase("query")) {
    			if(null != startDate || null != endDate) {
    				startDate = DateUtils.specifyDateZero(startDate);
    				endDate = DateUtils.specifyDatetWentyour(endDate);
    			}
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
    		List<Integer> list=new ArrayList<>();
            if(status != null) {
                    list = new ArrayList<Integer>();
                    for(int i = 0; i < status.length; i++){
                            list.add(status[i].ordinal());
                }
            }
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("ts", ts);
            Supplier supplier = super.getCurrentSupplier();
            map.put("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
            List<CommodityReportDto> commodityReportDtos = commodityReportService.purchaseOrderData(startDate, endDate, supplier, list);
            CommodityStatisticsDto statisticsDto = commodityReportService.purchasingCommodityStatistics(startDate, endDate, supplier, list);
            Map<String, Object> pieChart = new HashMap<String, Object>();
            List<String> pieList = new ArrayList<String>();
            
            Integer quantity = 0;
    		for(int i=0;i<commodityReportDtos.size();i++) {
    			CommodityReportDto dto = commodityReportDtos.get(i);
    			if(i > 18) {
    				quantity+=dto.getOrderQuantity();
    			}else {
    				if(dto.getSpecification().equalsIgnoreCase("[]")) {
    					if(dto.getSource() != null) {
    						pieChart.put(dto.getName()+"(分销)", dto.getOrderQuantity());
        					pieList.add(dto.getName()+"(分销)");
    					}else {
    						pieChart.put(dto.getName(), dto.getOrderQuantity());
        					pieList.add(dto.getName());
    					}
    					
    				}else {
    					if(dto.getSource() != null) {
    						pieChart.put(dto.getName()+dto.getSpecification()+"(分销)", dto.getOrderQuantity());
        					pieList.add(dto.getName()+dto.getSpecification()+"(分销)");
    					}else {
    						pieChart.put(dto.getName()+dto.getSpecification(), dto.getOrderQuantity());
        					pieList.add(dto.getName()+dto.getSpecification());
    					}
    					
    				}
    			}
    		}
    		if(quantity > 0) {
    			pieChart.put("其他", quantity);
    			pieList.add("其他");
    		}
            map.put("commodityReportDtos", commodityReportDtos);
            map.put("statisticsDto", statisticsDto);
            map.put("pieChart", pieChart);
            map.put("pieList", pieList);
            return map;
	}
	
	
	/**
	 * 订货单导出
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param ts
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "orderFormExport" , method = RequestMethod.GET)
	public ModelAndView download(Date startDate,Date endDate,Order.Status[] status,String ts,String search,String exportType) {
		if(null != search && search.equalsIgnoreCase("query")) {
			if(null != startDate || null != endDate) {
				startDate = DateUtils.specifyDateZero(startDate);
				endDate = DateUtils.specifyDatetWentyour(endDate);
			}
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
		List<Integer> list=new ArrayList<>();
        if(status != null) {
                list = new ArrayList<Integer>();
                for(int i = 0; i < status.length; i++){
                        list.add(status[i].ordinal());
            }
        }
        if(exportType == null || exportType == "") {
        	exportType = "good";
        }
        Supplier supplier = super.getCurrentSupplier();
        return new ModelAndView(commodityReportService.orderExport(startDate, endDate, supplier, list,exportType));
	}
	
	/**
	 * 采购单导出
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param ts
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "purchasedGoodsExported" , method = RequestMethod.GET)
	public ModelAndView purchasedGoodsExported(Date startDate,Date endDate,Order.Status[] status,String ts,String search) {
		if(null != search && search.equalsIgnoreCase("query")) {
			if(null != startDate || null != endDate) {
				startDate = DateUtils.specifyDateZero(startDate);
				endDate = DateUtils.specifyDatetWentyour(endDate);
			}
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
		List<Integer> list=new ArrayList<>();
        if(status != null) {
                list = new ArrayList<Integer>();
                for(int i = 0; i < status.length; i++){
                        list.add(status[i].ordinal());
            }
        }
        Supplier supplier = super.getCurrentSupplier();
        return new ModelAndView(commodityReportService.purchasedGoodsExported(startDate, endDate, supplier, list));
	}
}
