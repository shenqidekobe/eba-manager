/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController {

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Tag.Type.values());
		return "/admin/tag/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Tag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		tag.setArticles(null);
		tag.setGoods(null);
		tagService.save(tag);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Tag.Type.values());
		model.addAttribute("tag", tagService.find(id));
		return "/admin/tag/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Tag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag)) {
			return ERROR_VIEW;
		}
		tagService.update(tag, "type", "articles", "goods");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tagService.findPage(pageable));
		return "/admin/tag/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		tagService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}