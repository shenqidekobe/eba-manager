package com.microBusiness.manage.controller.admin.ass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListItem;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.service.ass.AssListRemarksService;
import com.microBusiness.manage.service.ass.AssListService;
import com.microBusiness.manage.util.DateUtils;

@Controller("assListStatistics")
@RequestMapping("/admin/ass/listStatistics")
public class AssListStatisticsController extends BaseController{

	@Resource(name = "assListServiceImpl")
	private AssListService assListService;
	@Resource(name = "assListRemarksServiceImpl")
	private AssListRemarksService assListRemarksService;
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String add(String searchValue, Date startDate, Date endDate, Pageable pageable,ModelMap model) {
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier=getCurrentSupplier();
		Page<AssListAndCustomerRelationDto> page=assListService.findPageBySupplier(supplier, startDate, endDate, searchValue, pageable);
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("page", page);
		return "/admin/assListStatistics/list";
	}
	
	@RequestMapping(value = "/details" , method = RequestMethod.GET)
	public String details(Long id,ModelMap model) {
		Supplier supplier=this.getCurrentSupplier();
		AssListAndCustomerRelationDto assListDto=assListService.findDetailsById(id);
		AssList assList=assListDto.getAssList();
		List<AssListItem> assListItems=assList.getAssListItems();
		List<AssListRemarks> assListRemarks=assListRemarksService.getRemarksForList(assList);
		List<AssListRelation> assListRelations=assList.getAssListRelations();
		
		List<AssListItem> list=new ArrayList<>();
		for (AssListItem assListItem : assListItems) {
			AssGoodDirectory assGoodDirectory=assListItem.getAssProduct().getAssGoods().getAssCustomerRelation().getAssGoodDirectory();
			if (assGoodDirectory != null) {
				if (supplier.equals(assGoodDirectory.getSupplier())) {
					list.add(assListItem);
				}
			}
		}
		model.addAttribute("assList", assList);
		model.addAttribute("assListItems", list);
		model.addAttribute("assListRemarks", assListRemarks);
		model.addAttribute("assListRelations", assListRelations);
		model.addAttribute("adminName", assListDto.getAdminName());
		model.addAttribute("theme", assListDto.getpTheme());
		return "/admin/assListStatistics/details";
	}
	
	/**
	 * 导出选中的清单
	 * @return
	 */
	@RequestMapping(value = "/getOutSelectedReport", method = RequestMethod.GET)
	public void selectedReport(Long[] ids , HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		assListService.selectedReport(ids, supplier, request, response);
	}
	
	/**
	 * 导出清单
	 * @return
	 */
	@RequestMapping(value = "/getOutReportDownload", method = RequestMethod.GET)
	public void reportDownload(String searchValue, Date startDate, Date endDate, HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
        assListService.reportDownload(searchValue,startDate,endDate, supplier, request, response);
	}
}
