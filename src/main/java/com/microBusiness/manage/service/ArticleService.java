/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;

public interface ArticleService extends BaseService<Article, Long> {

	List<Article> findList(ArticleCategory articleCategory, Tag tag, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders);

	List<Article> findList(Long articleCategoryId, Long tagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	List<Article> findList(ArticleCategory articleCategory, Boolean isPublication, Article.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count);

	Page<Article> findPage(ArticleCategory articleCategory, Tag tag, Boolean isPublication, Pageable pageable);

	long viewHits(Long id);

}