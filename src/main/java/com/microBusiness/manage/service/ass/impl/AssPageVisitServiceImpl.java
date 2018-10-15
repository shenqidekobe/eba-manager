package com.microBusiness.manage.service.ass.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssPageVisitDao;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssPageVisit;
import com.microBusiness.manage.service.ass.AssPageVisitService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assPageVisitServiceImpl")
public class AssPageVisitServiceImpl extends BaseServiceImpl<AssPageVisit, Long> implements AssPageVisitService{
	@Resource
	private AssPageVisitDao assPageVisitDao;
	
	@Override
	public Long getCountByRelation(AssCustomerRelation assCustomerRelation) {
		// TODO Auto-generated method stub
		return assPageVisitDao.getCountByRelation(assCustomerRelation);
	}

	@Override
	public Page<ShareUserPageDto> findPage(AssCustomerRelation assCustomerRelation, String orderBy, Pageable pageable) {
		// TODO Auto-generated method stub
		return assPageVisitDao.findPage(assCustomerRelation, orderBy, pageable);
	}

	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assPageVisitDao.getShareAllVisit(assChildMember);
	}

	@Override
	public boolean isSynchronize(long memberId, List<Long> goodsIds) {
		// TODO Auto-generated method stub
		return assPageVisitDao.isSynchronize(memberId, goodsIds);
	}
}
