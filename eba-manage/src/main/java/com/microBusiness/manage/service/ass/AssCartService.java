/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass;

import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.BaseService;

public interface AssCartService extends BaseService<AssCart, Long> {
	
	AssCart getCurrent(AssChildMember assChildMember);

	AssCart add(AssChildMember assChildMember, AssProduct assProduct, Long assCustomerRelationId, Integer quantity);

//	AssCart add(AssChildMember assChildMember, AssProduct assproduct, int quantity , Long assCustomerRelationId);
	
//	AssCart add(Member member, Product product, int quantity , Long assCustomerRelationId , Long relationId);

}