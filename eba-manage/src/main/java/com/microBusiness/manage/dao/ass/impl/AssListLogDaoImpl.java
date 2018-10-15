package com.microBusiness.manage.dao.ass.impl;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.ass.AssListLogDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssListLog;

@Repository("assListLogDaoImpl")
public class AssListLogDaoImpl extends BaseDaoImpl<AssListLog, Long> implements AssListLogDao{

}
