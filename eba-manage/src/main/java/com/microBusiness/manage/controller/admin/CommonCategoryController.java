package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.CategoryService;
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
 * Created by mingbai on 2017/7/31.
 * 功能描述：商流的商品分类，所有企业通用
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/common_category")
public class CommonCategoryController extends BaseController {
    @Resource
    private CategoryService categoryService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("categoryTree", categoryService.findTree());
        return "/admin/common_category/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Category category, Long parentId , RedirectAttributes redirectAttributes) {

        category.setParent(categoryService.find(parentId));

        if (!isValid(category)) {
            return ERROR_VIEW;
        }
        category.setTreePath(null);
        category.setGrade(null);
        category.setChildren(null);
        categoryService.save(category);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Category category = categoryService.find(id);
        model.addAttribute("categoryTree", categoryService.findTree());
        model.addAttribute("editCategory", category);
        model.addAttribute("children", categoryService.findChildren(category, true, null));
        return "/admin/common_category/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Category category, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
        category.setParent(categoryService.find(parentId));
        if (!isValid(category)) {
            return ERROR_VIEW;
        }
        if (category.getParent() != null) {
            Category parent = category.getParent();
            if (parent.equals(category)) {
                return ERROR_VIEW;
            }
            List<Category> children = categoryService.findChildren(parent, true, null);
            if (children != null && children.contains(parent)) {
                return ERROR_VIEW;
            }
        }
        categoryService.update(category, "treePath", "grade", "children");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model , String searchName) {
        model.addAttribute("categoryTree", categoryService.findTree(searchName));
        model.addAttribute("searchName", searchName);
        return "/admin/common_category/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long id) {
        Category category = categoryService.find(id);
        if (category == null) {
            return ERROR_MESSAGE;
        }	
        Set<Category> children = category.getChildren();
        if (children != null && !children.isEmpty()) {
            return Message.error("请先删除子分类！");
        }
        //删除需要做软删除
       /* if (goods != null && !goods.isEmpty()) {
            return Message.error("admin.category.deleteExistProductNotAllowed");
        }*/
        categoryService.delete(id);
        return SUCCESS_MESSAGE;
    }

}
