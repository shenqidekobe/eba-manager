package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.NeedSupplyController.NeedProductForm;
import com.microBusiness.manage.entity.*;

/**
 * Created by mingbai on 2017/2/5.
 * 功能描述：
 * 修改记录：
 */
public interface SupplyNeedService extends BaseService<SupplyNeed , Long> {
    Page<SupplyNeed> findPage(Pageable pageable , SupplyNeed supplyNeed , Date startDate , Date endDate , String searchName);

    boolean save(SupplyNeed supplyNeed , List<NeedProduct> needProducts);
    
    SupplyNeed findSupplyNeed(SupplyNeed supplyNeed);

    SupplyNeed updateStatus(SupplyNeed supplyNeed);

    boolean update(SupplyNeed supplyNeed , List<NeedProduct> needProducts);
    
    List<SupplyNeed> findByDateList(Supplier supplier, Need need);
    
    List<SupplyNeed> findByDateList(Supplier supplier, Long[] needIds);

    List<SupplyNeed> findByNeedSupplier(Long needId, Supplier supplier);
    
    /**
	 * 处理过期的供应关系
	 * @param compareDate
	 * @param status
	 */
	void dealExpiredSupply(Date compareDate , SupplyNeed.Status status);
	
	/**
	 * 处理未开始的未供应中
	 * @param compareDate
	 */
	void dealWillSupplyToSupply(Date compareDate);

	/**
	 * 根据状态和企业查询 个体分配供应
	 * @param supplier
	 * @param status
	 * @param assignedModel
	 * @return
	 */
	List<SupplyNeed> findBySupplier(Supplier supplier , List<SupplyNeed.Status> status , SupplyNeed.AssignedModel assignedModel) ;
	
	
	SupplyNeed findSupplyNeedOnSupply(SupplyNeed supplyNeed);
	
	void batchSave(Supplier supplier,SupplyNeed.AssignedModel assignedModel,Long[] needIds, NeedProductForm NeedProductForm , Integer noticeDay , Boolean openNotice);

	List<SupplyNeed> findByMember(Member member);

	List<SupplyNeed> findByNeeds(List<Need> needs);
	
	boolean updateNeedSupply(SupplyNeed supplyNeed , List<NeedProduct> needProducts);

	void refreshSupplyNeed(SupplyNeed supplyNeed);
	
	/**
	 * 
	 * @Title: batchAllocationGoods
	 * @author: yuezhiwei
	 * @date: 2018年4月17日下午2:42:44
	 * @Description: 小程序扫码批量分配本地商品
	 * @return: void
	 */
	void batchAllocationGoods(Product product, List<Long> shopIds, Supplier supplier);
}
