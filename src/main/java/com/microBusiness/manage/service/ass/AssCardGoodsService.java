/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.service.BaseService;

public interface AssCardGoodsService extends BaseService<AssCardGoods, Long> {
	
	List<AssCardGoods> getAssCardGoodsByCard(AssCard assCard);
	
	Page<AssCardGoods> findPage(AssCard assCard,Pageable pageable);

	AssCardGoods get(AssCard assCard, AssCustomerRelation assCustomerRelation);
	
}