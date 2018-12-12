package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单报表
 * @author pengtianwen
 *
 */
public class OrderReportDto  implements Serializable {
	
	private static final long serialVersionUID = -8090634378052069675L;
	//日期
	private String reportDate;
	//订货单数-购货单数
	private Integer orderNumber;
	//订货客户数-采购供应商
	private Integer customersNumber;
	//订货单金额-采购单金额
	private BigDecimal amount;
	
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getCustomersNumber() {
		return customersNumber;
	}
	public void setCustomersNumber(Integer customersNumber) {
		this.customersNumber = customersNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
