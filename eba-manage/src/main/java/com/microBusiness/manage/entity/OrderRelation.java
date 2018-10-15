package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 参与订单 关系
 * @author yuezhiwei
 *
 */
@Entity
@Table(name = "t_order_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_relation")
public class OrderRelation extends BaseEntity<Long> {

	private static final long serialVersionUID = -2227550724518846125L;

	public enum Type {
		participant,//参与人
		sponsor   //发起人
	}
	
	private Type type;
	
	private ChildMember childMember;
	
	private Order order;
	
	private Member member;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "childMember", nullable = false, updatable = false)
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
}
