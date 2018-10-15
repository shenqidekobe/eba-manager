package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CustomerRelationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by mingbai on 2017/6/29.
 * 功能描述：供应商关系
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/supplierRelation")
public class SupplierRelationController extends BaseController {

    @Resource
    private CustomerRelationService customerRelationService ;

    @Resource
    private AreaService areaService ;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , ModelMap model) {
        model.addAttribute("searchName", searchName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("clientTypes", CustomerRelation.ClientType.values());

        Supplier bySupplier = super.getCurrentSupplier();

        model.addAttribute("page", customerRelationService.findSupplierPage(searchName, startDate, endDate, clientType, pageable, bySupplier));

        return "/admin/supplier_relation/list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        if(null == id){
            return ERROR_VIEW ;
        }
        CustomerRelation supplierRelation = customerRelationService.find(id);
        if(null == supplierRelation){
            return ERROR_VIEW ;
        }
        model.addAttribute("supplierRelation" , supplierRelation);
        return "/admin/supplier_relation/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long areaId, CustomerRelation customerRelation , ModelMap model) {
        Area area = areaService.find(areaId);
        customerRelation.setSupplierArea(area);

        customerRelationService.update(customerRelation, "inviteCode", "clientName", "clientNum", "clientType", "area", "address", "userName", "tel", "email", "accountName", "invoice", "bank", "bankAccountNum", "identifier", "supplier", "bySupplier");
        return "redirect:list.jhtml";
    }

}
