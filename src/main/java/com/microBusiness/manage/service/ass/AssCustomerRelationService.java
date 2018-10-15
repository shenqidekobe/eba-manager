package com.microBusiness.manage.service.ass;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.AssShareStatisticsDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.service.BaseService;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssCustomerRelationService extends BaseService<AssCustomerRelation, Long> {

	Page<AssCustomerRelation> findPage(String searchName , Date startDate , Date endDate, Pageable pageable , Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember);
	
	Page<AssCustomerRelation> findSupplierPage(String searchName , Date startDate , Date endDate , Pageable pageable , Supplier bySupplier);

	AssCustomerRelation inviteNameExists(String searchName , Supplier supplier);
	
	AssCustomerRelation inviteNameExists(String searchName , String theme, AssChildMember assChildMember);

	/**
	 * 获取有商品的供应商
	 * 
	 * @param pageable
	 * @param supplier
	 * @param sourceType
	 * @param assChildMember
	 * @return
	 */
	Page<AssCustomerRelation> findPage(Pageable pageable , Supplier supplier, AssCustomerRelation.SourceType sourceType, AssChildMember assChildMember);
	
	AssCustomerRelation findByCustomerRelation(Long id);
	
	List<AssCustomerRelation> findList( AssCustomerRelation.SourceType sourceType,AssCustomerRelation.ShareType shareType,AssCustomerRelation source, AssChildMember assChildMember);
	
	/**
	 * 获取供应商列表
	 * @param assChildMember
	 * @return
	 */
	List<AssCustomerRelation> findCustomerRelationList(AssChildMember assChildMember , Long assCustomerRelationId);
	
	AssCustomerRelation findBySn(String sn);
	
	Page<AssCustomerRelation> findPageByShareType(AssChildMember assChildMember,AssCustomerRelation.ShareType shareType,Pageable pageable);
	
	BigInteger getShareAllVisit(AssChildMember assChildMember);
	
	Page<AssShareStatisticsDto> findSharePage(String orderBy,String searchName, Date startDate, Date endDate, Pageable pageable,
			Supplier supplier,AssCustomerRelation.ShareType shareType,AssCustomerRelation.SourceType sourceType);
	
}
