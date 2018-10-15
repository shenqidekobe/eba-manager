package com.microBusiness.manage.dao.ass;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.AssPurchaseListStatisticsDto;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssList.Status;

public interface AssListDao extends BaseDao<AssList, Long>{

	/**
	 * 后台获取清单列表
	 * @param status
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @return
	 */
	Page<AssList> findPage(Status status, Pageable pageable, Supplier supplier,
			Date startDate, Date endDate, String searchName);
	
	
	/**
	 * 小程序端获取清单列表
	 * @param pageable
	 * @param name 
	 * @param sn 
	 * @param assChildMember
	 * @param member
	 * @return
	 */
	Page<AssList> findPage(Pageable pageable,String searchValue, AssChildMember assChildMember,Member member);
	
	boolean assListItem(List<Long> ids);
	
	/**
	 * sn是否纯在    true 存在   false 不存在
	 * @param sn
	 * @return
	 */
	boolean hasSn(String sn);
	
	/**
	 * 按月份统计每月创建多少笔采购清单
	 * @param assChildMember 
	 * @param startDate 月开始时间
	 * @param endDate 月结束时间
	 * @return
	 */
	Long pressMonthCountPurchaseList(AssChildMember assChildMember , Date startDate , Date endDate);
	
	/**
	 * 采购清单中看板中列表
	 * @param pageable
	 * @param searchValue 查询条件
	 * @param assChildMember
	 * @param startDate 月开始时间
	 * @param endDate 月结束时间
	 * @return
	 */
	Page<AssList> findPage(Pageable pageable,String searchValue,AssChildMember assChildMember,Date startDate , Date endDate);
	
	List<AssListStatisticsDto> findByList(AssChildMember assChildMember);
	
	/**
	 * 采购清单统计(商品看板)
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param assChildMember
	 * @param shareType 分享类型
	 * @return
	 */
	List<AssPurchaseListStatisticsDto> assListStatistics(Date startDate, Date endDate, AssChildMember assChildMember, AssCustomerRelation.ShareType shareType);
	
	/**
	 * 采购清单统计详情(商品看板)
	 * @param assChildMember
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param searchValue 查询条件
	 * @param shareType 分享类型
	 * @return
	 */
	Page<AssListAndCustomerRelationDto> assListStatisticsDetails(AssChildMember assChildMember, String startDate, String endDate, String searchValue, AssCustomerRelation.ShareType shareType,Pageable pageable);
	
	/**
	 * 后台统计清单
	 * @param assChildMember
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param searchValue 查询条件
	 * @param shareType 分享类型
	 * @return
	 */
	Page<AssListAndCustomerRelationDto> findPageBySupplier(Supplier supplier,  Date startDate, Date endDate, String searchValue,Pageable pageable);
	
	List<AssListAndCustomerRelationDto> findListBySupplier(Supplier supplier,  Date startDate, Date endDate, String searchValue);


	AssListAndCustomerRelationDto findDetailsById(Long id);
	List<AssListAndCustomerRelationDto> findDetailsListById(Long[] ids);

}
