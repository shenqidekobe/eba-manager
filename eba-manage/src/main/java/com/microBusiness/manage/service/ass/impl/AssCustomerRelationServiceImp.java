package com.microBusiness.manage.service.ass.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.dto.AssShareStatisticsDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.ShareType;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.SourceType;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;

/**
 * 功能描述：
 * 修改记录：
 */
@Service("assCustomerRelationServiceImp")
public class AssCustomerRelationServiceImp extends BaseServiceImpl<AssCustomerRelation, Long> implements AssCustomerRelationService {
	
	@Resource(name = "assCustomerRelationDaoImpl")
	private AssCustomerRelationDao assCustomerRelationDao;

	@Override
	public Page<AssCustomerRelation> findPage(String searchName, Date startDate,
			Date endDate, Pageable pageable, Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember) {
		return assCustomerRelationDao.findPage(searchName, startDate, endDate, pageable, supplier, sourceType, assChildMember);
	}

	@Override
	public Page<AssCustomerRelation> findSupplierPage(String searchName, Date startDate, Date endDate, Pageable pageable, Supplier bySupplier) {
		return assCustomerRelationDao.findSupplierPage(searchName, startDate, endDate, pageable, bySupplier);
	}

	@Override
	public AssCustomerRelation inviteNameExists(String searchName, Supplier supplier) {
		return assCustomerRelationDao.inviteNameExists(searchName, supplier);
	}

	@Override
	public AssCustomerRelation inviteNameExists(String searchName, String theme, AssChildMember assChildMember) {
		return assCustomerRelationDao.inviteNameExists(searchName, theme, assChildMember);
	}

	@Override
	public Page<AssCustomerRelation> findPage(Pageable pageable, Supplier supplier, SourceType sourceType, AssChildMember assChildMember) {
		return assCustomerRelationDao.findPage(pageable, supplier, sourceType, assChildMember);
	}

	@Override
	public AssCustomerRelation findByCustomerRelation(Long id) {
		return assCustomerRelationDao.find(id);
	}

	@Override
	public List<AssCustomerRelation> findList(SourceType sourceType,
			ShareType shareType, AssCustomerRelation source,
			AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assCustomerRelationDao.findList(sourceType, shareType, source, assChildMember);
	}

	@Override
	public List<AssCustomerRelation> findCustomerRelationList(
			AssChildMember assChildMember, Long assCustomerRelationId) {
		return assCustomerRelationDao.findCustomerRelationList(assChildMember , assCustomerRelationId);
	}

	@Override
	public AssCustomerRelation findBySn(String sn) {
		// TODO Auto-generated method stub
		return assCustomerRelationDao.findBySn(sn);
	}

	@Override
	public Page<AssCustomerRelation> findPageByShareType(AssChildMember assChildMember, ShareType shareType,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return assCustomerRelationDao.findPageByShareType(assChildMember, shareType, pageable);
	}

	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assCustomerRelationDao.getShareAllVisit(assChildMember);
	}

	@Override
	public Page<AssShareStatisticsDto> findSharePage(String orderBy,String searchName, Date startDate, Date endDate, Pageable pageable,
			Supplier supplier,AssCustomerRelation.ShareType shareType,AssCustomerRelation.SourceType sourceType) {
		// TODO Auto-generated method stub
		return assCustomerRelationDao.findSharePage(orderBy,searchName, startDate, endDate, pageable, supplier, shareType, sourceType);
	}
}
