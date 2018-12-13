package com.microBusiness.manage.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 会员收益记录
 * */
@Entity
@Table(name = "xx_member_income")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_member_incomes")
public class MemberIncome extends BaseEntity<Long> {

	private static final long serialVersionUID = 1108913572196082302L;
	
	public static final String TYPE_INCOME="income";
	public static final String TYPE_WITHDRAW="withdraw";
	
	private String title;
	private String types;//收益类型{income、withdraw}
	private BigDecimal amount;//收益金额
	private ChildMember member;//收益人
	private Integer level;//几级收益
	private String remark;
	
	private Long orderId;//收益来源的订单
	private Long correId;//关联ID
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	@Column(nullable = false, updatable = true, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public ChildMember getMember() {
		return member;
	}
	public void setMember(ChildMember member) {
		this.member = member;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getCorreId() {
		return correId;
	}
	public void setCorreId(Long correId) {
		this.correId = correId;
	}
	
	
	@Transient
	public String getLobm() {
		if(types==null)return "+";
		if(TYPE_INCOME.equals(types))return "+";
		if(TYPE_WITHDRAW.equals(types))return "-";
		return "+";
	}
	
}
