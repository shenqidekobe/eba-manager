package com.microBusiness.manage.dto;

import java.io.Serializable;

import com.microBusiness.manage.entity.Order;

/**
 * 小程序采购清单统计
 * 
 * @author yuezhiwei
 *
 */
public class AssListStatisticsDto implements Serializable {

	private static final long serialVersionUID = 6056980669269329995L;
	// 年
	private Integer years;
	// 月
	private Integer months;
	// 采购清单数
	private Integer counts;
	
	private Integer statu;

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public Integer getStatu() {
		return statu;
	}

	public void setStatu(Integer statu) {
		this.statu = statu;
	}

}
