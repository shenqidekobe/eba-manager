/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Promotion;

public interface PromotionService extends BaseService<Promotion, Long> {

	boolean isValidPriceExpression(String priceExpression);

	boolean isValidPointExpression(String pointExpression);

	List<Promotion> findList(MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

	List<Promotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}