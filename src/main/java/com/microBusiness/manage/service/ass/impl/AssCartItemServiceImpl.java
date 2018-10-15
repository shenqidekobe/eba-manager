/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.ass.AssCartItemDao;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.service.ass.AssCartItemService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;

@Service("assCartItemServiceImpl")
public class AssCartItemServiceImpl extends BaseServiceImpl<AssCartItem, Long> implements AssCartItemService {
	
	@Resource
	private AssCartItemDao assCartItemDao;
	@Resource
	private AssCustomerRelationDao assCustomerRelationDao;
	
	@Override
	public AssCartItem deleteByCartItem(AssCartItem assCartItem) {
		assCartItemDao.deleteByCartItem(assCartItem);
		return assCartItem;
	}

	@Override
	public AssCartItem deleteByCartItem(AssCartItem cartItem, Long assCustomerRelationId) {
		cartItem.setAssCustomerRelation(assCustomerRelationDao.find(assCustomerRelationId));
		assCartItemDao.deleteByCartItem(cartItem);
		return cartItem;
	}

	@Override
	public List<AssCartItem> findByList(List<Long> assProductids) {
		return assCartItemDao.findByList(assProductids);
	}
}