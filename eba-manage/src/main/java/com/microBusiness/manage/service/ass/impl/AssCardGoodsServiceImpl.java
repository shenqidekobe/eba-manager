	/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;


import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCardGoodsDao;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.service.ass.AssCardGoodsService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;

@Service("assCardGoodsServiceImpl")
public class AssCardGoodsServiceImpl extends BaseServiceImpl<AssCardGoods, Long> implements AssCardGoodsService {

	@Resource
	private AssCardGoodsDao assCardGoodsDao;
	
	@Override
	public List<AssCardGoods> getAssCardGoodsByCard(AssCard assCard) {
		return assCardGoodsDao.getAssCardGoodsByCard(assCard);
	}

	@Override
	public Page<AssCardGoods> findPage(AssCard assCard, Pageable pageable) {
		// TODO Auto-generated method stub
		return assCardGoodsDao.findPage(assCard, pageable);
	}

	@Override
	public AssCardGoods get(AssCard assCard, AssCustomerRelation assCustomerRelation) {
		return assCardGoodsDao.get(assCard, assCustomerRelation);
	}

}