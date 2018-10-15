package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierSupplierService extends BaseService<SupplierSupplier, Long>{

	Page<SupplierSupplier> findPage(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName);
	
	Page<SupplierSupplier> supplyDistributionList(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName);

	List<SupplierSupplier> findByDateList(Supplier supplier, Supplier bySupplier, Date startDate, Date endDate,List<SupplierSupplier.Status> status);

	boolean save(SupplierSupplier supplierSupplier, List<SupplierProduct> supplierProductList);

	SupplierSupplier updateStatus(SupplierSupplier supplierSupplier);

	/**
	 * 处理过期的供应关系
	 * @param compareDate
	 * @param status
	 */
	void dealExpiredSupply(Date compareDate , SupplierSupplier.Status status);


	SupplierSupplier update(SupplierSupplier supplierSupplier);

	/**
	 * 处理未开始的未供应中
	 * @param compareDate
	 */
	void dealWillSupplyToSupply(Date compareDate);

	SupplierSupplier getSupplierSupplier(Supplier bySupplier,Supplier supplier,Date orderTime,List<SupplierSupplier.Status> status);

	List<SupplierSupplier> getSupplierSupplierList(Supplier bySupplier,Supplier supplier);

	/**
	 * 根据企业查询供应商
	 * @param bySupplier
	 * @param status
	 * @return
	 */
	List<SupplierSupplier> getListBySupplier(Supplier bySupplier,List<SupplierSupplier.Status> status);
	
	/**
	 * 
	 * @Title: updateUnconfirmed
	 * @author: yuezhiwei
	 * @date: 2018年3月9日下午7:37:29
	 * @Description: 未确认状态下修改企业供应
	 * @return: boolean
	 */
	boolean updateUnconfirmed(SupplierSupplier supplierSupplier, List<SupplierProduct> supplierProductList);

	/**
	 *
	 * @Title: getSupplyBatch
	 * @author: yuezhiwei
	 * @date: 2018年3月27日下午4:28:26
	 * @Description: 获取批次
	 * @return: Integer
	 */
	Integer getSupplyBatch(Supplier supplier , Supplier bySupplier);


	void refreshSupplierSupplier(SupplierSupplier supplierSupplier);
}
