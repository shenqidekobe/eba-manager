/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import com.microBusiness.manage.dao.ass.AssCardDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCard;

import org.springframework.stereotype.Repository;

@Repository("assCardDaoImpl")
public class AssCardDaoImpl extends BaseDaoImpl<AssCard, Long> implements AssCardDao {

	@Override
	public AssCard findBySn(String sn) {
		if (null == sn) {
			return null;
		}
		try {
			String jpql = "select assCard from AssCard assCard where assCard.sn =:sn and assCard.deleted=0";
			AssCard assCard = entityManager.createQuery(jpql, AssCard.class).setParameter("sn", sn).getSingleResult();
			return assCard;
		} catch (Exception e) {
			return null;
		}
	}

}