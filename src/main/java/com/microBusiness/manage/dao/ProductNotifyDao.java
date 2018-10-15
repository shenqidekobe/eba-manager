/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductNotify;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductNotify;

public interface ProductNotifyDao extends BaseDao<ProductNotify, Long> {

	boolean exists(Product product, String email);

	Page<ProductNotify> findPage(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable);

	Long count(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent);

}