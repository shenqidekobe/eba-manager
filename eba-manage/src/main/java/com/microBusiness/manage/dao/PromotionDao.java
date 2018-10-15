/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;

public interface PromotionDao extends BaseDao<Promotion, Long> {

	List<Promotion> findList(MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

}