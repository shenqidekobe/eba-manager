package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.service.DepartmentService;
import org.apache.commons.collections.CollectionUtils;
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
 * Created by mingbai on 2017/8/24.
 * 功能描述：
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/department")
public class DepartmentController extends BaseController {

    @Resource
    private DepartmentService departmentService ;
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
        return "/admin/department/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Department department, Long parentId, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
        department.setParent(departmentService.find(parentId));
        if (!isValid(department)) {
            return ERROR_VIEW;
        }
        department.setTreePath(null);
        department.setGrade(null);
        department.setChildren(null);
        department.setSupplier(super.getCurrentSupplier());
        departmentService.save(department);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Department department = departmentService.find(id);
        model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
        model.addAttribute("department", department);
        model.addAttribute("children", departmentService.findChildren(department, true, null));
        return "/admin/department/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Department department, Long parentId , RedirectAttributes redirectAttributes) {
        department.setParent(departmentService.find(parentId));
        if (!isValid(department)) {
            return ERROR_VIEW;
        }
        if (department.getParent() != null) {
            Department parent = department.getParent();
            if (parent.equals(department)) {
                return ERROR_VIEW;
            }
            List<Department> children = departmentService.findChildren(parent, true, null);
            if (children != null && children.contains(parent)) {
                return ERROR_VIEW;
            }
        }
        departmentService.update(department, "treePath", "grade", "children" , "supplier");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model , String searchName) {
        model.addAttribute("searchName", searchName);
        model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , searchName));
        return "/admin/department/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long id) {
        Department department = departmentService.find(id);
        if (department == null) {
            return ERROR_MESSAGE;
        }
        Set<Department> children = department.getChildren();
        if (children != null && !children.isEmpty()) {
            return Message.error("存在下属部门");
        }

        Set<Admin> admins = department.getAdmins() ;

        if (CollectionUtils.isNotEmpty(admins)) {
            return Message.error("存在员工");
        }

        departmentService.delete(id);
        return SUCCESS_MESSAGE;
    }
}
