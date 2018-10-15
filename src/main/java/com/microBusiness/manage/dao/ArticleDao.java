/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

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
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.ArticleCategory;
import com.microBusiness.manage.entity.Tag;

public interface ArticleDao extends BaseDao<Article, Long> {

	List<Article> findList(ArticleCategory articleCategory, Tag tag, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders);

	List<Article> findList(ArticleCategory articleCategory, Boolean isPublication, Article.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count);

	Page<Article> findPage(ArticleCategory articleCategory, Tag tag, Boolean isPublication, Pageable pageable);

}