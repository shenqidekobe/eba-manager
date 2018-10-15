/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;

public interface AssCardGoodsDao extends BaseDao<AssCardGoods, Long> {

	List<AssCardGoods> getAssCardGoodsByCard(AssCard assCard);
	
	Page<AssCardGoods> findPage(AssCard assCard,Pageable pageable);
	
	AssCardGoods get(AssCard assCard, AssCustomerRelation assCustomerRelation);
}