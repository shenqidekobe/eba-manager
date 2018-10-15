/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.OrderLogDao;
import com.microBusiness.manage.entity.OrderLog;

import org.springframework.stereotype.Repository;

@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, Long> implements OrderLogDao {

}