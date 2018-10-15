/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;

import com.microBusiness.manage.dao.MessageConfigDao;
import com.microBusiness.manage.entity.MessageConfig;

import com.microBusiness.manage.dao.MessageConfigDao;
import com.microBusiness.manage.entity.MessageConfig;
import org.springframework.stereotype.Repository;

@Repository("messageConfigDaoImpl")
public class MessageConfigDaoImpl extends BaseDaoImpl<MessageConfig, Long> implements MessageConfigDao {

	public MessageConfig find(MessageConfig.Type type) {
		if (type == null) {
			return null;
		}
		try {
			String jpql = "select messageConfig from MessageConfig messageConfig where messageConfig.type = :type";
			return entityManager.createQuery(jpql, MessageConfig.class).setParameter("type", type).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}