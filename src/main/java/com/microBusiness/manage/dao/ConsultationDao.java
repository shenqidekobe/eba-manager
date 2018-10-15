/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Consultation;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Consultation;
import com.microBusiness.manage.entity.Member;

public interface ConsultationDao extends BaseDao<Consultation, Long> {

	List<Consultation> findList(Member member, Goods goods, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	Page<Consultation> findPage(Member member, Goods goods, Boolean isShow, Pageable pageable);

	Long count(Member member, Goods goods, Boolean isShow);

}