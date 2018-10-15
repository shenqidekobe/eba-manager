package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;

public interface FavorCompanyDao extends BaseDao<FavorCompany, Long>{

	public FavorCompany getFavorCompanyIsNull(Long adminId, Long supplierId);
	
	public boolean updateDelflag(Long supplierId , Long adminId , FavorCompany.Delflag delflag);

	public List<FavorCompany> getFavorCompany(Supplier supplier);

}
