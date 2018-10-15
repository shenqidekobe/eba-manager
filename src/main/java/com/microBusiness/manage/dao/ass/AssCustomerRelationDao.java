package com.microBusiness.manage.dao.ass;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.dto.AssShareStatisticsDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssCustomerRelationDao extends BaseDao<AssCustomerRelation, Long> {

	Page<AssCustomerRelation> findPage(String searchName , Date startDate , Date endDate ,Pageable pageable , Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember);
	
	List<Supplier> findByOfficialCustomers(Supplier supplier , Supplier.Status status);

	Page<AssCustomerRelation> findSupplierPage(String searchName , Date startDate , Date endDate , Pageable pageable , Supplier bySupplier);
	
	AssCustomerRelation inviteNameExists(String searchName , Supplier supplier);
	
	AssCustomerRelation inviteNameExists(String searchName , String theme, AssChildMember assChildMember);
	
	Page<AssCustomerRelation> findPage(Pageable pageable , Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember);
	
	AssCustomerRelation findByCustomerRelation(Long id);
	
	List<AssCustomerRelation> findList( AssCustomerRelation.SourceType sourceType,AssCustomerRelation.ShareType shareType,AssCustomerRelation source, AssChildMember assChildMember);
	
	List<AssCustomerRelation> findCustomerRelationList(AssChildMember assChildMember , Long assCustomerRelationId);

	AssCustomerRelation findBySn(String sn);
	
	Page<AssCustomerRelation> findPageByShareType(AssChildMember assChildMember,AssCustomerRelation.ShareType shareType,Pageable pageable);

	BigInteger getShareAllVisit(AssChildMember assChildMember);
	
	List<AssCustomerRelation> findAll();

	Page<AssShareStatisticsDto> findSharePage(String orderBy,String searchName, Date startDate, Date endDate, Pageable pageable,
			Supplier supplier,AssCustomerRelation.ShareType shareType,AssCustomerRelation.SourceType sourceType);
	
	AssCustomerRelation inviteNameExists(String clientName , String theme , AssCustomerRelation.SourceType sourceType , AssChildMember assChildMember);
}
