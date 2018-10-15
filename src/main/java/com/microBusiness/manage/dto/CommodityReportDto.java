package com.microBusiness.manage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品报表
 * @author yuezhiwei
 *
 */
public class CommodityReportDto implements Serializable {

	private static final long serialVersionUID = 5419075977456992459L;
	//商品id
	private String productId;
	//商品sn
	private String productSn;
	//分类id
	private String categoryId;
	//商品名称
	private String name;
	//商品规格
	private String specification;
	//订货单数
	private Integer orderNumber;
	//订货商品数
	private Integer orderQuantity;
	//订货客户数
	private Integer customersNum;
	//订货单金额
	private BigDecimal orderAmount;
	//采购供应商数
	private Integer suppliersNum;
	//商品价格
	private BigDecimal goodAmount;
	//商品来源
	private Integer source;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Integer getCustomersNum() {
		return customersNum;
	}
	public void setCustomersNum(Integer customersNum) {
		this.customersNum = customersNum;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public Integer getSuppliersNum() {
		return suppliersNum;
	}
	public void setSuppliersNum(Integer suppliersNum) {
		this.suppliersNum = suppliersNum;
	}
	public BigDecimal getGoodAmount() {
		return goodAmount;
	}
	public void setGoodAmount(BigDecimal goodAmount) {
		this.goodAmount = goodAmount;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getProductSn() {
		return productSn;
	}
	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}
	
}
