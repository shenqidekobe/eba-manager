package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.SupplyNeed.AssignedModel;
import com.microBusiness.manage.entity.SupplyNeed.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
public interface SupplyNeedDao extends BaseDao<SupplyNeed, Long> {
    Page<SupplyNeed> findPage(Pageable pageable, SupplyNeed supplyNeed, Date startDate, Date endDate, String searchName) ;
    
    SupplyNeed findSupplyNeed(SupplyNeed supplyNeed);
    
    List<SupplyNeed> findByDateList(Supplier supplier, Need need);
    
    List<SupplyNeed> findByDateList(Supplier supplier, Long[] needIds);

    void updateExpiredSupply(Date compareDate , SupplyNeed.Status status);

	void dealWillSupplyToSupply(Date compareDate) ;
	
	List<SupplyNeed> findByNeedSupplier(Long needId, Supplier supplier);

    List<SupplyNeed> findBySupplier(Supplier supplier, List<SupplyNeed.Status> status , SupplyNeed.AssignedModel assignedModel) ;
    
    SupplyNeed findSupplyNeedOnSupply(SupplyNeed supplyNeed);

    List<SupplyNeed> findByMember(Member member);

    List<SupplyNeed> findByNeeds(List<Need> needs);
}
