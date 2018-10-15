package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;

public interface SupplierSupplierDao extends BaseDao<SupplierSupplier, Long> {

	Page<SupplierSupplier> findPage(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName);

	Page<SupplierSupplier> supplyDistributionList(Pageable pageable, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status , String searchName);
	
	List<SupplierSupplier> findByDateList(Supplier supplier, Supplier bySupplier, Date startDate, Date endDate,List<SupplierSupplier.Status> status);

	void updateExpiredSupply(Date compareDate , SupplierSupplier.Status status);

	void dealWillSupplyToSupply(Date compareDate) ;

	SupplierSupplier getSupplierSupplier(Supplier bySupplier,Supplier supplier,Date orderTime,List<SupplierSupplier.Status> status);

	List<SupplierSupplier> getSupplierSupplierList(Supplier bySupplier, Supplier supplier);


	List<SupplierSupplier> getListBySupplier(Supplier bySupplier,List<SupplierSupplier.Status> status);
}
