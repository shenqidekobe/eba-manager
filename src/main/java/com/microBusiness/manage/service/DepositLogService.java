/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;

public interface DepositLogService extends BaseService<DepositLog, Long> {

	Page<DepositLog> findPage(Member member, Pageable pageable);

}