/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Review;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Review;

public interface ReviewService extends BaseService<Review, Long> {

	List<Review> findList(Member member, Goods goods, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	List<Review> findList(Long memberId, Long goodsId, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	Page<Review> findPage(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable);

	Long count(Member member, Goods goods, Review.Type type, Boolean isShow);

	boolean hasPermission(Member member, Goods goods);

}