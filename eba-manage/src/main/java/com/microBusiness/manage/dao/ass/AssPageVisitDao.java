package com.microBusiness.manage.dao.ass;

import java.math.BigInteger;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssPageVisit;

public interface AssPageVisitDao extends BaseDao<AssPageVisit, Long>{
	Long getCountByRelation(AssCustomerRelation assCustomerRelation);
	Page<ShareUserPageDto> findPage(AssCustomerRelation assCustomerRelation,String orderBy,Pageable pageable);
	
	BigInteger getShareAllVisit(AssChildMember assChildMember);
	
	boolean isSynchronize(long memberId,List<Long> goodsIds);
}
