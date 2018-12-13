package com.microBusiness.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 提现数据
 * */
@Indexed
@Entity
@Table(name = "xx_withdraw")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_withdraw")
public class Withdraw extends BaseEntity<Long>{
	
	private static final long serialVersionUID = 1709980369605052257L;
	
	
	public static final String WAY_WECHAT="wechat";
	public static final String WAY_ALIPAY="alipay";
	public static final String WAY_UNIONPAY="unionpay";

	public enum Withdraw_Status{
		await("待审核"),
		complete("提现成功"),
		fail("提现失败"),
		repeal("已取消");
		private String val;
		Withdraw_Status(String val){
			this.val=val;
		}
		public String getLabel(){
			return val;
		}
	}
	
	private String sn;
	private ChildMember member;
	private Withdraw_Status status;//状态
	private BigDecimal amount;//提现金额
	private BigDecimal fee;//提现手续费
	private String way;//提现方式
	private String phone;//联系电话
	private String account;//提现账户
	private String accountName;//账户姓名
	
	private Date processTime;//处理时间
	private String remark;//备注说明
	
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Date getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	@Column(nullable = false)
	public Withdraw_Status getStatus() {
		return status;
	}

	public void setStatus(Withdraw_Status status) {
		this.status = status;
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
	
	@Transient
	public String getStatusName() {
		if(status==null)return "待审核";
		String val="";
		switch (status) {
		case await:
			val="待审核";
			break;
		case complete:
			val="已完成";
			break;
		case fail:
			val="操作失败";
			break;
		case repeal:
			val="已取消";
			break;
		}
		return val;
	}
	@Transient
	public String getWayName() {
		if(way==null)return "微信";
		if(WAY_WECHAT.equals(way))return "微信";
		if(WAY_ALIPAY.equals(way))return "支付宝";
		if(WAY_UNIONPAY.equals(way))return "银行卡";
		return "微信";
	}
}
