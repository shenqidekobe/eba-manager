package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.CompanyGoodsSupplierDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.FavorCompanyGoods;

public interface FavorCompanyGoodsService extends BaseService<FavorCompanyGoods, Long> {

	/**
	 * 收藏产品
	 * 
	 * @param member
	 * 			用户
	 * @param supplier
	 * 			企业
	 * @return 收藏企业关联
	 */
	public FavorCompanyGoods favorCompanyGoods(Admin member, Long companyGoodsId);
	
	
	/**
	 * 收藏商品列表
	 * @param pageable
	 * @param adminId
	 * @param delflag
	 * @return
	 */
	public Page<CompanyGoodsSupplierDto> findPage(Pageable pageable , Long adminId , CompanyGoods.Delflag delflag);
	
	/**
	 * 删除收藏的产品
	 * @param adminId
	 * @param companyGoodsId
	 * @param delflag
	 */
	public void updateDelflag(Long adminId,Long companyGoodsId , Delflag delflag);
	
	/**
	 * 获取当前用户收藏产品
	 * 
	 * @param admin
	 * @return
	 */
	public List<FavorCompanyGoods> getFavorCompanyGoods(Supplier supplier);
}
