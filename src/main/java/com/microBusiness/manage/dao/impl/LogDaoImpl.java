/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.LogDao;
import com.microBusiness.manage.entity.Log;

import com.microBusiness.manage.dao.LogDao;
import com.microBusiness.manage.entity.Log;
import org.springframework.stereotype.Repository;

@Repository("logDaoImpl")
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {

	public void removeAll() {
		String jpql = "delete from Log log";
		entityManager.createQuery(jpql).executeUpdate();
	}

}