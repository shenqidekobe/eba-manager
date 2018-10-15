package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;

/**
 * Created by yuezhiwei on 2017/6/27.
 * 功能描述：
 * 修改记录：
 */
public interface CustomerRelationDao extends BaseDao<CustomerRelation, Long> {

	Page<CustomerRelation> findPage(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , Supplier supplier);
	
	boolean inviteCodeExists(String inviteCode , Supplier supplier);
	
	/**
	 * 查询正式供应有客户关系的企业
	 * @param supplier
	 * @return
	 */
	List<Supplier> findByOfficialCustomers(Supplier supplier , Supplier.Status status);

	/**
	 * 供应商关系 列表
	 * @param searchName
	 * @param startDate
	 * @param endDate
	 * @param clientType
	 * @param pageable
	 * @param bySupplier
	 * @return
	 */
	Page<CustomerRelation> findSupplierPage(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , Supplier bySupplier);
	
	List<Supplier> findByTemporarySupply(Supplier supplier , Supplier.Status status);
	
	/**
	 * 查询没有供应关系的企业
	 * @param supplier
	 * @param bysupplier
	 * @return
	 */
	List<CustomerRelation> findByNoSupplyRelationship(Long id);
	
	/**
	 * 订货助理企业客户列表
	 * @param assChildMember
	 * @param pageable
	 * @return
	 */
	Page<CustomerRelation> findPage(AssChildMember assChildMember , Pageable pageable);
	
	List<CustomerRelation> findList(AssChildMember assChildMember);
}
