package com.microBusiness.manage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.dao.AreaDao;
import com.microBusiness.manage.dao.CustomerRelationDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.CustomerRelation.ClientType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Supplier.Status;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.CustomerRelationService;

import org.springframework.stereotype.Service;

/**
 * Created by yuezhiwei on 2017/6/27.
 * 功能描述：
 * 修改记录：
 */
@Service("customerRelationServiceImp")
public class CustomerRelationServiceImp extends BaseServiceImpl<CustomerRelation, Long> implements CustomerRelationService {
	
	@Resource(name = "customerRelationDaoImpl")
	private CustomerRelationDao customerRelationDao;
	@Resource
	private AdminDao adminDao;
	@Resource
	private AreaDao areaDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private AssChildMemberDao assChildMemberDao;
	@Resource
	private AssUpdateTipsDao assUpdateTipsDao;

	@Override
	public Page<CustomerRelation> findPage(String searchName, Date startDate,
			Date endDate, CustomerRelation.ClientType clientType, Pageable pageable,
			Supplier supplier) {
		return customerRelationDao.findPage(searchName, startDate, endDate, clientType, pageable, supplier);
	}

	@Override
	public boolean inviteCodeExists(String inviteCode , Supplier supplier) {
		return customerRelationDao.inviteCodeExists(inviteCode , supplier);
	}

	@Override
	public List<Supplier> findByOfficialCustomers(Supplier supplier , Supplier.Status status) {
		return customerRelationDao.findByOfficialCustomers(supplier , status);
	}

	/**
	 * 获取供应商列表
	 *
	 * @param searchName
	 * @param startDate
	 * @param endDate
	 * @param clientType
	 * @param pageable
	 * @param bySupplier
	 * @return
	 */
	@Override
	public Page<CustomerRelation> findSupplierPage(String searchName, Date startDate, Date endDate, ClientType clientType, Pageable pageable, Supplier bySupplier) {
		return customerRelationDao.findSupplierPage(searchName, startDate, endDate, clientType, pageable, bySupplier);
	}

	@Override
	public List<Supplier> findByTemporarySupply(Supplier supplier, Status status) {
		return customerRelationDao.findByTemporarySupply(supplier, status);
	}

	@Override
	public List<CustomerRelation> findByNoSupplyRelationship(Long id) {
		return customerRelationDao.findByNoSupplyRelationship(id);
	}

	@Override
	public Page<CustomerRelation> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		return customerRelationDao.findPage(assChildMember, pageable);
	}

	@Override
	public CustomerRelation save(CustomerRelation customerRelation,
			Long areaId, Long supplierId, Long adminId, Supplier supplier) {
		
		customerRelation.setAdmin(adminDao.find(adminId));
		customerRelation.setArea(areaDao.find(areaId));
		customerRelation.setSupplier(supplier);
		customerRelation.setBySupplier(supplierDao.find(supplierId));
		customerRelation.setSupplierName(supplier.getName());
		customerRelation.setSupplierArea(supplier.getArea());
		customerRelation.setSupplierAddress(supplier.getAddress());
		customerRelation.setSupplierUserName(supplier.getUserName());
		customerRelation.setSupplierTel(supplier.getTel());
		customerRelation.setSupplierEmail(supplier.getEmail());
		customerRelationDao.persist(customerRelation);
		
		//添加更新标志
		List<AssChildMember> assChildMembers = assChildMemberDao.findList(supplier);
		for(AssChildMember assChildMember : assChildMembers) {
			AssUpdateTips updateTips = new AssUpdateTips();
			updateTips.setAssChildMember(assChildMember);
			updateTips.setType(AssUpdateTips.Type.businessCustomers);
			updateTips.setCustomerRelation(customerRelation);
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
			assUpdateTipsDao.persist(updateTips);
		}
		return customerRelation;
	}

	@Override
	public List<CustomerRelation> findList(AssChildMember assChildMember) {
		return customerRelationDao.findList(assChildMember);
	}

	@Override
	public CustomerRelation updateCustomerRelation(CustomerRelation customerRelation) {
		CustomerRelation relation = this.update(customerRelation, "inviteCode" , "clientName" , "supplier" , "bySupplier","supplierName","supplierCode",
				"supplierArea","supplierAddress","supplierUserName","supplierTel","supplierEmail","supAccountName","supplierInvoice",
				"supplierBank","supBankAccountNum");
		return relation;
		
	}
}
