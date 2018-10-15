/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "xx_order_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_log")
public class OrderLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -5487851265876313328L;

	public enum Type {
		
		pendingPayment,	//0待支付

		pendingShipment, //2已支付(待发货)

		create,

		update,

		cancel,

		review,

		payment,

		refunds,

		shipping,

		returns,

		receive,

		complete,

		fail,

		//申请取消
		applyCancel,
		//通过取消
		passedCancel,
		//拒绝取消
		deniedCancel,
		//商品数量修改
		updateItems,
		//作废发货
		cancelShipped
	}

	private Type type;
	
	private String operator;

	private String content;
	
	//企业名称
	private String supplierName;

	private Order order;
	
	private LogType logType;

	public OrderLog() {
	}

	public OrderLog(Type type, String operator) {
		this.type = type;
		this.operator = operator;
	}

	public OrderLog(Type type, String operator, String content) {
		this.type = type;
		this.operator = operator;
		this.content = content;
	}

	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(updatable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Transient
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
		setSupplierName(operator != null ? operator.getSupplier().getName() : null);
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

}
