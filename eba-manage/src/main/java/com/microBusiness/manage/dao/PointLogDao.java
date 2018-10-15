/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;

public interface PointLogDao extends BaseDao<PointLog, Long> {

	Page<PointLog> findPage(Member member, Pageable pageable);

}