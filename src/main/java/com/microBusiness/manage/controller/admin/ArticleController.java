/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.HashSet;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.ArticleCategoryService;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.service.TagService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.ArticleCategoryService;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Tag.Type.article));
		return "/admin/article/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setHits(0L);
		article.setGenerateMethod(Article.GenerateMethod.eager);
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Tag.Type.article));
		model.addAttribute("article", articleService.find(id));
		return "/admin/article/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		articleService.update(article, "hits", "generateMethod");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleService.findPage(pageable));
		return "/admin/article/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}