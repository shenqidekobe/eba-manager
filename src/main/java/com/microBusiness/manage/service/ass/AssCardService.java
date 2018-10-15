/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.service.BaseService;

public interface AssCardService extends BaseService<AssCard, Long> {
	
	public AssCard copy(AssCard assCard, AssChildMember assChildMember, String sn);

	AssCard findBySn(String sn);
	
	AssCard save(AssCard assCard, String idStr);
	
	AssCard update(AssCard assCard, String idStr);
	
	void delete(Long id);
}