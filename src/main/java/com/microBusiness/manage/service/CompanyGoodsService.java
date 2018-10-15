package com.microBusiness.manage.service;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import java.util.List;

public interface CompanyGoodsService extends BaseService<CompanyGoods, Long> {
	
	public CompanyGoods saveFromDhm(CompanyGoods companyGoods, String productIds, String categoryIds);

	/**
	 * 查询
	 * 
	 * @param pageable
	 * @return 商品列表
	 */
	public Page<CompanyGoods> getCompanyGoodsList(Admin admin, CompanyGoods companyGoods, Boolean priceBoolean, Boolean popularityBoolean, Boolean packagesNumBoolean, Pageable pageable);

	/**
	 * 更新
	 * 
	 * @return
	 */
	public CompanyGoods updateCompanyGoods(Long id);
	
	public CompanyGoods findById(Long id, Admin admin);

	/**
	 * 后台审核列表
	 * @param pageable
	 * @param category 分类
	 * @param status 审核状态
	 * @param searchValue 混合查询的内容
	 * @param pubType
	 * @return
	 */
	Page<CompanyGoods> findPage(Pageable pageable , Category category , CompanyGoods.Status status , String searchValue , CompanyGoods.PubType pubType);

	/**
	 *
	 * @param companyGoods
	 * @param operator
	 * @return
	 */
	CompanyGoods update(CompanyGoods companyGoods, Admin operator);

	boolean check(Long[] ids , CompanyGoods.Status status) ;

	void delete(Long[] ids);

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

	/**
	 *
	 * @param pageable
	 * @param categoryId
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	ExcelView exportSupplyGoods(Pageable pageable , Long categoryId , CompanyGoods.Status status , String searchValue , CompanyGoods.PubType pubType);

	ExcelView exportPurchaseGoods(Pageable pageable , Long categoryId , CompanyGoods.Status status , String searchValue , CompanyGoods.PubType pubType);

	/**
	 * 获取层级分类
	 * @param category
	 * @return
	 */
	String getCategoryIds(Category category);

}
