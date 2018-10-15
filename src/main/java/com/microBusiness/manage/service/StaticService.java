/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;
import java.util.Map;

import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;
import com.microBusiness.manage.entity.ProductCategory;

public interface StaticService {

	int generate(String templatePath, String staticPath, Map<String, Object> model);

	int generate(Article article);

	int generate(Goods goods);

	int generateArticle(ArticleCategory articleCategory, Boolean isPublication, Article.GenerateMethod generateMethod, Date beginDate, Date endDate);

	int generateGoods(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate);

	int generateIndex();

	int generateSitemap();

	int generateOther();

	int generateAll();

	int delete(String staticPath);

	int delete(Article article);

	int delete(Goods goods);

	int deleteIndex();

	int deleteOther();

}