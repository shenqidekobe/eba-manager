package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.ass.AssGoodsVisit;

public class GoodsVisitDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3786750674942490629L;

	private AssGoodsVisit assGoodsVisit;
	
	private Long goodsVisit;

	public AssGoodsVisit getAssGoodsVisit() {
		return assGoodsVisit;
	}

	public void setAssGoodsVisit(AssGoodsVisit assGoodsVisit) {
		this.assGoodsVisit = assGoodsVisit;
	}

	public Long getGoodsVisit() {
		return goodsVisit;
	}

	public void setGoodsVisit(Long goodsVisit) {
		this.goodsVisit = goodsVisit;
	}
	
	
	
}
