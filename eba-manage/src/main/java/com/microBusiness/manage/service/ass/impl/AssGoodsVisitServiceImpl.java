package com.microBusiness.manage.service.ass.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodsVisitDao;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;
import com.microBusiness.manage.service.ass.AssGoodsVisitService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service("assGoodsVisitServiceImpl")
public class AssGoodsVisitServiceImpl extends BaseServiceImpl<AssGoodsVisit, Long> implements AssGoodsVisitService{

	@Resource
	private AssGoodsVisitDao assGoodsVisitDao;
	
	@Override
	public Long getCountByRelation(AssCustomerRelation assCustomerRelation) {
		// TODO Auto-generated method stub
		return assGoodsVisitDao.getCountByRelation(assCustomerRelation);
	}

	@Override
	public Page<GoodsVisitDto> findPage(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return assGoodsVisitDao.findPage(assCustomerRelation, assChildMember, pageable);
	}

	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assGoodsVisitDao.getShareAllVisit(assChildMember);
	}

	@Override
	public List<GoodsVisitDto> findList(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assGoodsVisitDao.findList(assCustomerRelation, assChildMember);
	}

}
