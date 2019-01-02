package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.dto.DictJson;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.entity.Dict;
import com.microBusiness.manage.service.DictService;
import com.microBusiness.manage.util.JsonUtils;

@Controller("adminDictController")
@RequestMapping("/admin/dict")
public class DictController extends BaseController {

	@Resource(name = "dictServiceImpl")
	private DictService dictService;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		if(id==null)id=Dict.DEFAULT_ID;
		model.addAttribute("types", Ad.Type.values());
		Dict obj=dictService.find(id);
		DictJson json=JsonUtils.toObject(obj.getJson(),DictJson.class);
		model.addAttribute("obj",json);
		model.addAttribute("id",id);
		return "/admin/dict/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(DictJson obj, Long id, RedirectAttributes redirectAttributes) {
		if (id==null) {
			return ERROR_VIEW;
		}
		Dict pojo=dictService.find(id);
		if (!isValid(pojo)) {
			return ERROR_VIEW;
		}
		String json=JsonUtils.toJson(obj);
		pojo.setJson(json);
		dictService.update(pojo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.jhtml";
	}

}