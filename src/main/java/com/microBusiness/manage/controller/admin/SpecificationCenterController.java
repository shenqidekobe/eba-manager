package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.SpecificationCenter;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.SpecificationCenterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午8:23
 * Describe:
 * Update:
 */
@Controller
@RequestMapping("/admin/specificationCenter")
public class SpecificationCenterController extends BaseController {

    @Resource
    private SpecificationCenterService specificationCenterService;
    @Resource
    private CategoryCenterService categoryCenterService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Long sampleId, ModelMap model) {
        model.addAttribute("sample", specificationCenterService.find(sampleId));
        model.addAttribute("productCategoryTree", categoryCenterService.findTree(null));
        return "/admin/specification_center/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(SpecificationCenter specification, Long productCategoryId, RedirectAttributes redirectAttributes) {
        CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
            public boolean evaluate(Object object) {
                String option = (String) object;
                return StringUtils.isNotEmpty(option);
            }
        }));
        specification.setCategoryCenter(categoryCenterService.find(productCategoryId));
        if (!isValid(specification, BaseEntity.Save.class)) {
            return ERROR_VIEW;
        }

        specificationCenterService.save(specification);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("productCategoryTree", categoryCenterService.findTree());
        model.addAttribute("specification", specificationCenterService.find(id));
        return "/admin/specification_center/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(SpecificationCenter specification, RedirectAttributes redirectAttributes) {
        CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
            public boolean evaluate(Object object) {
                String option = (String) object;
                return StringUtils.isNotEmpty(option);
            }
        }));
        if (!isValid(specification)) {
            return ERROR_VIEW;
        }
        specificationCenterService.update(specification, "categoryCenter");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", specificationCenterService.findPage(pageable));
        return "/admin/specification_center/list";
    }
}
