package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "t_order_share_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_share_log")
public class OrderShareLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -8382798705998882730L;

	public enum Type {
		initiated, //发起
		participate,//参与
		dropOut, 	//退出
		theEnd, 	//终结
	}
	
	//记录人
	private String name;
	
	//操作类型
	private Type type;
	
	//备注所属订单
	private Order order;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
