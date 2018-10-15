package com.microBusiness.manage.dao.ass;

import java.math.BigInteger;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;

public interface AssGoodsVisitDao extends BaseDao<AssGoodsVisit, Long> {
	Long getCountByRelation(AssCustomerRelation assCustomerRelation);
	
	Page<GoodsVisitDto> findPage(AssCustomerRelation assCustomerRelation,AssChildMember assChildMember,Pageable pageable);
	
	List<GoodsVisitDto> findList(AssCustomerRelation assCustomerRelation,AssChildMember assChildMember);

	BigInteger getShareAllVisit(AssChildMember assChildMember);
}
