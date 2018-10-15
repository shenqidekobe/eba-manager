package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;

public interface FavorCompanyService extends BaseService<FavorCompany, Long> {

	/**
	 * 收藏企业
	 * 
	 * @param member
	 * 			用户
	 * @param supplier
	 * 			企业
	 * @return 收藏企业关联
	 */
	public FavorCompany favorCompany(Admin admin, Long supplierId);
	
	
	/**
	 * 查询当前用户所收藏的企业信息
	 * @param adminId
	 * @param delflag 伪删除标识
	 * @param pageable
	 * @return
	 */
	public Page<Supplier> findPage(Long adminId , FavorCompany.Delflag delflag , Pageable pageable);
	
	public boolean updateDelflag(Long supplierId , Long adminId , FavorCompany.Delflag delflag);
	
	/**
	 * 获取当前用户收藏企业
	 * 
	 * @param admin
	 * @return
	 */
	public List<FavorCompany> getFavorCompany(Supplier supplier);
}
