package com.microBusiness.manage.service.ass;

import java.math.BigInteger;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssPageVisit;
import com.microBusiness.manage.service.BaseService;

public interface AssPageVisitService extends BaseService<AssPageVisit, Long>{
	Long getCountByRelation(AssCustomerRelation assCustomerRelation);
	
	Page<ShareUserPageDto> findPage(AssCustomerRelation assCustomerRelation,String orderBy,Pageable pageable);

	BigInteger getShareAllVisit(AssChildMember assChildMember);
	
	boolean isSynchronize(long memberId,List<Long> goodsIds);
}
