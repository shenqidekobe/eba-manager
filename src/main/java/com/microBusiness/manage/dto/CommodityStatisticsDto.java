package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品统计
 * @author yuezhiwei
 *
 */
public class CommodityStatisticsDto implements Serializable {

	private static final long serialVersionUID = -1243267797792581664L;
	//订货商品SKU
	private Long numberOfGoods;
	//订货商品数
	private String total;
	//订货商品金额
	private BigDecimal totalAmount;
	//采购供应商数
	private Long numberOfSuppliers;
	//订货客户数
	private Long numberOfCustomers;
	//订货单数
	private Long orderTotal;
	
	public Long getNumberOfGoods() {
		return numberOfGoods;
	}
	public void setNumberOfGoods(Long numberOfGoods) {
		this.numberOfGoods = numberOfGoods;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
    public Long getNumberOfSuppliers() {
        return numberOfSuppliers;
    }
    public void setNumberOfSuppliers(Long numberOfSuppliers) {
        this.numberOfSuppliers = numberOfSuppliers;
    }
    public Long getNumberOfCustomers() {
        return numberOfCustomers;
    }
    public void setNumberOfCustomers(Long numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }
	public Long getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Long orderTotal) {
		this.orderTotal = orderTotal;
	}

}
