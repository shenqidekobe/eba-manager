package com.microBusiness.manage.dao;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;

import java.util.List;

public interface CompanyGoodsDao  extends BaseDao<CompanyGoods , Long> {

	/**
	 * 查询
	 * 
	 * @param pageable
	 * @return 商品列表
	 */
	public Page<CompanyGoods> query(CompanyGoods companyGoods, Boolean price, Boolean popularity, Boolean packagesNumBoolean, Pageable pageable);

	/**
	 *
	 * @param pageable
	 * @param category
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	Page<CompanyGoods> findPage(Pageable pageable, Category category, CompanyGoods.Status status, String searchValue , CompanyGoods.PubType pubType) ;

	/**
	 *
	 * @param pageable
	 * @param category
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	List<CompanyGoods> findList(Pageable pageable , Category category , CompanyGoods.Status status , String searchValue , CompanyGoods.PubType pubType);
}
