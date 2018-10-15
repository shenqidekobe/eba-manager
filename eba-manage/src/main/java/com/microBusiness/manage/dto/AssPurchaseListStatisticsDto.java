package com.microBusiness.manage.dto;

import java.io.Serializable;

/**
 * 小程序采购清单统计(商品看板)
 * 
 * @author yuezhiwei
 *
 */
public class AssPurchaseListStatisticsDto implements Serializable {

	private static final long serialVersionUID = -5075263122468153920L;
	// 日期
	private String createDate;
	// 采购清单数
	private Integer number;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
