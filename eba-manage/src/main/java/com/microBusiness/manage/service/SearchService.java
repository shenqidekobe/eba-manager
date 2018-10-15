/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Goods;

public interface SearchService {

	void index();

	void index(Class<?> type);

	void index(Article article);

	void index(Goods goods);

	void purge();

	void purge(Class<?> type);

	void purge(Article article);

	void purge(Goods goods);

	Page<Article> search(String keyword, Pageable pageable);

	Page<Goods> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, Goods.OrderType orderType, Pageable pageable);

}