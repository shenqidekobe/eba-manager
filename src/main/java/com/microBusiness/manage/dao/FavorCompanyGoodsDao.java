package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.CompanyGoodsSupplierDto;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.FavorCompanyGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;

public interface FavorCompanyGoodsDao extends BaseDao<FavorCompanyGoods, Long>{

	public FavorCompanyGoods getFavorCompanyGoodsIsNull(Long adminId, Long goodsId);
	
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

	public List<FavorCompanyGoods> getFavorCompanyGoods(Supplier supplier);
}
