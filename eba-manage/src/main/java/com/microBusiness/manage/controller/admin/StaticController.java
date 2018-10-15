/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.ArticleCategoryService;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.service.CacheService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.StaticService;

import com.microBusiness.manage.entity.ProductCategory;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("adminStaticController")
@RequestMapping("/admin/static")
public class StaticController extends BaseController {

	public enum GenerateType {

		index,

		article,

		goods,

		other
	}

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", GenerateType.values());
		model.addAttribute("defaultBeginDate", DateUtils.addDays(new Date(), -7));
		model.addAttribute("defaultEndDate", new Date());
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "/admin/static/generate";
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(GenerateType generateType, Long articleCategoryId, Long productCategoryId, Date beginDate, Date endDate, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		if (first == null || first < 0) {
			first = 0;
			cacheService.clear();
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case index:
			generateCount = staticService.generateIndex();
			break;
		case article:
			ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
			List<Article> articles = articleService.findList(articleCategory, true, null, beginDate, endDate, first, count);
			for (Article article : articles) {
				generateCount += staticService.generate(article);
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
			List<Goods> goodsList = goodsService.findList(productCategory, true, null, beginDate, endDate, first, count);
			for (Goods goods : goodsList) {
				generateCount += staticService.generate(goods);
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		case other:
			generateCount = staticService.generateOther();
			break;
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		return data;
	}

}