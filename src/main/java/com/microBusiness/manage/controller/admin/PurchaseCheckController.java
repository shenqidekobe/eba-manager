package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CategoryService;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

/**
 * Created by mingbai on 2017/8/7.
 * 功能描述：采购商品审核
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/purchaseCheck")
public class PurchaseCheckController extends BaseController {
    @Resource
    private CompanyGoodsService companyGoodsService ;

    @Resource
    private CategoryService categoryService ;

    @Resource
    private AdminService adminService ;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model, Pageable pageable , Long  categoryId, String searchValue , CompanyGoods.Status status) {

        Category category = categoryService.find(categoryId);

        Page<CompanyGoods> page = companyGoodsService.findPage(pageable , category , status , searchValue , CompanyGoods.PubType.pub_need);

        model.addAttribute("page" , page);
        model.addAttribute("pubTypes" , CompanyGoods.PubType.values());
        model.addAttribute("searchValue" , searchValue);
        model.addAttribute("statuses" , CompanyGoods.Status.values());
        model.addAttribute("categoryTree" , categoryService.findTree()) ;
        model.addAttribute("categoryId" , categoryId) ;
        model.addAttribute("status" , status) ;

        return "/admin/purchase_check/list";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {

        CompanyGoods companyGoods = companyGoodsService.find(id) ;

        Category category = companyGoods.getCategory() ;



        model.addAttribute("categoryTree", categoryService.findTree());
        model.addAttribute("goods", companyGoods);

        model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
        model.addAttribute("weightUnits", CompanyGoods.WeightUnit.values());
        model.addAttribute("volumeUnits", CompanyGoods.VolumeUnit.values());
        model.addAttribute("natures", CompanyGoods.Nature.values());
        model.addAttribute("units", CompanyGoods.Unit.values());
        model.addAttribute("types", CompanyGoods.Type.values());
        model.addAttribute("sourceTypes", CompanyGoods.SourceType.values());

        model.addAttribute("categoryRoots" , JsonUtils.toJson(Category.getCategoryIterator(new HashSet<Category>(categoryService.findRoots())))) ;

        model.addAttribute("categoryCurrIds" , companyGoodsService.getCategoryIds(category)) ;

        return "/admin/purchase_check/edit";
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(CompanyGoods goods , Long id, Long categoryId, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        goods.setCategory(categoryService.find(categoryId));

        Admin admin = adminService.getCurrent();

        companyGoodsService.update(goods , admin);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

        return "redirect:list.jhtml";
    }


    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {

        model.addAttribute("categoryTree", categoryService.findTree());
        model.addAttribute("goods", categoryService.find(id));

        model.addAttribute("storageConditions", CompanyGoods.StorageConditions.values());
        model.addAttribute("weightUnits", CompanyGoods.WeightUnit.values());
        model.addAttribute("volumeUnits", CompanyGoods.VolumeUnit.values());
        model.addAttribute("natures", CompanyGoods.Nature.values());
        model.addAttribute("units", CompanyGoods.Unit.values());
        model.addAttribute("types", CompanyGoods.Type.values());

        return "/admin/purchase_check/view";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        companyGoodsService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public @ResponseBody
    Message check(Long[] ids , CompanyGoods.Status status) {
        companyGoodsService.check(ids , status);
        return SUCCESS_MESSAGE;
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ModelAndView export(ModelMap model, Pageable pageable , Long  categoryId, String searchValue , CompanyGoods.Status status) {

        /*Category category = categoryService.find(categoryId);

        List<CompanyGoods> supplyGoods = companyGoodsService.findList(pageable , category , status , searchValue , CompanyGoods.PubType.pub_supply);*/

        return new ModelAndView(companyGoodsService.exportPurchaseGoods(pageable , categoryId , status , searchValue , CompanyGoods.PubType.pub_need));
    }
}
