package com.microBusiness.manage.service.ass;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.AssPurchaseListStatisticsDto;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListMemberStatus;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.BaseService;

public interface AssListService extends BaseService<AssList, Long> {
	
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
	Page<AssList> findPage( AssList.Status status,Pageable pageable , Supplier supplier , Date startDate , Date endDate , String searchName);
	
	
	/**
	 * 小程序端获取清单列表
	 * @param pageable
	 * @param assChildMember
	 * @param member
	 * @return
	 */
	Page<AssList> findPage(Pageable pageable,String searchValue,AssChildMember assChildMember,Member member);
	
	/**
	 *  分享、参与、终结、退出
	 * @param assList
	 * @param status
	 * @param assChildMember
	 */
	void updateStatus(AssList assList,AssListMemberStatus status,AssChildMember assChildMember);
	
	/**
	 * 创建清单
	 * @param assListRemarks 
	 */
	
	AssList create(Set<AssCartItem> assCartItems,AssChildMember assChildMember,AssCustomerRelation assCustomerRelation, AssListRemarks assListRemarks,AssShippingAddress assShippingAddress);


	void comeAgain(AssChildMember assChildMember, AssList assList);
	
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
	 * @param searchValue 
	 * @param assChildMember
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Page<AssList> findPage(Pageable pageable,String searchValue,AssChildMember assChildMember,Date startDate , Date endDate);
	
	
	List<AssListStatisticsDto> findByList(AssChildMember assChildMember);
	
	/**
	 * 采购清单看板
	 * @param assChildMember
	 * @return
	 */
	Map<Integer , List<AssListStatisticsDto>> purchaseListKanban(AssChildMember assChildMember);
	
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
	Page<AssListAndCustomerRelationDto> assListStatisticsDetails(AssChildMember assChildMember, String startDate, String endDate, String searchValue, AssCustomerRelation.ShareType shareType, Pageable pageable);
	
	
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
	
	/**
	 * 后台查看清单详情
	 * @param assChildMember
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param searchValue 查询条件
	 * @param shareType 分享类型
	 * @return
	 */
	AssListAndCustomerRelationDto findDetailsById(Long id);


	void selectedReport(Long[] ids, Supplier supplier, HttpServletRequest request, HttpServletResponse response);


	void reportDownload(String searchValue, Date startDate, Date endDate, Supplier supplier, HttpServletRequest request,
			HttpServletResponse response);

}
