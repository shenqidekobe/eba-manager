package com.microBusiness.manage.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.SearchService;

@Controller("adminIndexController")
@RequestMapping("/admin/index")
public class IndexController extends BaseController {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	public enum GenerateType {

		article,

		goods
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", GenerateType.values());
		return "/admin/index/generate";
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(GenerateType generateType, Boolean isPurge, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case article:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Article.class);
			}
			List<Article> articles = articleService.findList(first, count, null, null);
			for (Article article : articles) {
				searchService.index(article);
				generateCount++;
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Product.class);
			}
			List<Goods> goodsList = goodsService.findList(first, count, null, null);
			for (Goods goods : goodsList) {
				searchService.index(goods);
				generateCount++;
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
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