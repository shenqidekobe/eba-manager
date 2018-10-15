package com.microBusiness.manage.dto;

import java.io.Serializable;

/**
 * 商品同步日志
 * 
 * @author 吴战波
 *
 */
public class GoodsSyncDto  implements Serializable {
	
	private static final long serialVersionUID = -2478078501283023486L;
	//日期
	private String reportDate;
	//订货单数-购货单数
	private Integer syncNumber;
	
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public Integer getSyncNumber() {
		return syncNumber;
	}
	public void setSyncNumber(Integer syncNumber) {
		this.syncNumber = syncNumber;
	}
	
}
