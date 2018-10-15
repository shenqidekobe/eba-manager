package com.microBusiness.manage.controller.admin.ass;

import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.service.ass.AssListService;
import com.microBusiness.manage.util.DateUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assListController")
@RequestMapping("/admin/ass/assList")
public class AssListController extends BaseController {

	@Resource(name = "assListServiceImpl")
	private AssListService assListService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(AssList.Status status, Date startDate , Date endDate , String searchName,Pageable pageable, ModelMap model) {
		Supplier supplier=this.getCurrentSupplier(); 
		model.addAttribute("status", status);
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
		model.addAttribute("useExpired",""+supplier.isExpired());
		model.addAttribute("whetherCertify", ""+supplier.isProbation());
		model.addAttribute("supplierStatic", supplier.getStatus());
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
        model.addAttribute("page", assListService.findPage(status,pageable,supplier,startDate,endDate,searchName));
		return "/admin/assList/list";
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		assListService.deleted(ids);
		return SUCCESS_MESSAGE;
	}
}
