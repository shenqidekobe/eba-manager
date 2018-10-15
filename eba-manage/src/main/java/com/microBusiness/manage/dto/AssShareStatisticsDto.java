package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.ass.AssCustomerRelation;

/**
 * 后台分享统计查询实体
 * @author pengtianwen
 *
 */
public class AssShareStatisticsDto implements Serializable {

	private static final long serialVersionUID = -4572197227660326151L;

	private AssCustomerRelation assCustomerRelation;
	
	//页面访问次数
	private Long pageVisit;
	//商品浏览次数
	private Long goodsVisit;
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}
	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}
	public Long getPageVisit() {
		return pageVisit;
	}
	public void setPageVisit(Long pageVisit) {
		this.pageVisit = pageVisit;
	}
	public Long getGoodsVisit() {
		return goodsVisit;
	}
	public void setGoodsVisit(Long goodsVisit) {
		this.goodsVisit = goodsVisit;
	}
	
	
}
