package com.microBusiness.manage.controller.admin.ass;

import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 功能描述：
 * 修改记录：
 */
@Controller("assCustomerRelation")
@RequestMapping("/admin/ass/customerRelation")
public class AssCustomerRelationController extends BaseController {

	@Resource
	private AssCustomerRelationService assRelationService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private AreaService areaService;
	@Resource
	private SupplierSupplierService supplierSupplierService;
	@Resource
	private AssGoodsService assGoodsService;
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list(String searchName , Date startDate , Date endDate , Pageable pageable , ModelMap model) {
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		model.addAttribute("searchName", searchName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("page", assRelationService.findPage(searchName, startDate, endDate, pageable, supplier, AssCustomerRelation.SourceType.syncBackstage, null));
		return "/admin/assCustomerRelation/list";
	}
	
	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/assCustomerRelation/add";
	}
	
	/**
	 * 新建供应商
	 * @param customerRelation
	 * @param areaId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	public String save(AssCustomerRelation customerRelation , Long areaId, String clientName , ModelMap model , RedirectAttributes redirectAttributes) {
		Supplier supplier = super.getCurrentSupplier();
		if(customerRelation == null || areaId == null || clientName == null) {
			addFlashMessage(redirectAttributes, Message.error("参数错误"));
			return "redirect:add.jhtml";
		}
		if(assRelationService.inviteNameExists(clientName , supplier) != null) {
			addFlashMessage(redirectAttributes, Message.error("不能重复添加同一个供应商"));
			return "redirect:add.jhtml";
		}
		if(supplier.getName().equals(clientName)) {
			addFlashMessage(redirectAttributes, Message.error("不能添加自己企业为供应商"));
			return "redirect:add.jhtml";
		}

		customerRelation.setSourceType(AssCustomerRelation.SourceType.syncBackstage);
		customerRelation.setArea(areaService.find(areaId));
		customerRelation.setSupplier(supplier);
		assRelationService.save(customerRelation);
		return "redirect:list.jhtml";
	}
	
	@RequestMapping("/edit")
	public String edit(ModelMap model , Long id, String oldClientName) {
		model.addAttribute("customerRelation", assRelationService.find(id));
		model.addAttribute("oldClientName", oldClientName);
		return "/admin/assCustomerRelation/edit";
	}
	
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public String update(AssCustomerRelation customerRelation , Long areaId , RedirectAttributes redirectAttributes , ModelMap mode) {
		customerRelation.setArea(areaService.find(areaId));
		assRelationService.update(customerRelation);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if(ids == null) {
			return ERROR_MESSAGE;
		}

		assRelationService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/checkName", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkName(String clientName, String oldClientName){
    	Supplier supplier = super.getCurrentSupplier();
    	
        if (StringUtils.isEmpty(clientName)) {
            return false;
        }
        AssCustomerRelation assCustomerRelation = assRelationService.inviteNameExists(clientName, supplier);
        if(assCustomerRelation != null) {
        	if(assCustomerRelation.getClientName().equals(oldClientName)) {
        		return true;
        	}
        	if(!assCustomerRelation.getClientName().equals(oldClientName)) {
        		return false;
        	}
		}
        return true;

    }

}
