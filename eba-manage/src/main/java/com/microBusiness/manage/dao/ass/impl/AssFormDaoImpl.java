/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import com.microBusiness.manage.dao.ass.AssFormDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssForm;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

@Repository("assFormDaoImpl")
public class AssFormDaoImpl extends BaseDaoImpl<AssForm, Long> implements AssFormDao {

	@Override
	public int clearExpired() {
		String sql="delete from ass_form where (to_days(now())-to_days(create_date))>7";
		try {
			return entityManager.createQuery(sql).executeUpdate();
		} catch (NoResultException e) {
			return 0;
		}
	}

}