package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.service.CategoryCenterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午5:15
 * Describe:
 * Update:
 */
@Controller
@RequestMapping("/admin/categoryCenter")
public class CategoryCenterController extends BaseController {

    @Resource
    private CategoryCenterService categoryCenterService ;



    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("productCategoryTree", categoryCenterService.findTree(null));
        return "/admin/category_center/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(CategoryCenter categoryCenter, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
        categoryCenter.setParent(categoryCenterService.find(parentId));
        if (!isValid(categoryCenter)) {
            return ERROR_VIEW;
        }
        categoryCenter.setTreePath(null);
        categoryCenter.setGrade(null);
        categoryCenter.setChildren(null);
        categoryCenter.setGoodsCenters(null);
        categoryCenter.setSpecificationCenters(null);

        categoryCenterService.save(categoryCenter);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        CategoryCenter productCategory = categoryCenterService.find(id);
        model.addAttribute("productCategoryTree", categoryCenterService.findTree(null));
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("children", categoryCenterService.findChildren(productCategory, true, null));
        return "/admin/category_center/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(CategoryCenter productCategory, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
        productCategory.setParent(categoryCenterService.find(parentId));
        if (!isValid(productCategory)) {
            return ERROR_VIEW;
        }
        if (productCategory.getParent() != null) {
            CategoryCenter parent = productCategory.getParent();
            if (parent.equals(productCategory)) {
                return ERROR_VIEW;
            }
            List<CategoryCenter> children = categoryCenterService.findChildren(parent, true, null);
            if (children != null && children.contains(parent)) {
                return ERROR_VIEW;
            }
        }
        categoryCenterService.update(productCategory, "treePath", "grade", "children", "goodsCenters",  "specificationCenters");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model , String searchName) {
        model.addAttribute("searchName", searchName);
        model.addAttribute("productCategoryTree", categoryCenterService.findTree(searchName));
        return "/admin/category_center/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long id) {
        CategoryCenter productCategory = categoryCenterService.find(id);
        if (productCategory == null) {
            return ERROR_MESSAGE;
        }
        Set<CategoryCenter> children = productCategory.getChildren();
        if (children != null && !children.isEmpty()) {
            return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
        }
        Set<GoodsCenter> goods = productCategory.getGoodsCenters();
        if (goods != null && !goods.isEmpty()) {
            return Message.error("admin.productCategory.deleteExistProductNotAllowed");
        }
        categoryCenterService.delete(id);
        return SUCCESS_MESSAGE;
    }
}
