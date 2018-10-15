package com.microBusiness.manage.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by yzw on 2017/6/23.
 * 功能描述：订单设置
 * 
 */
@Entity
@Table(name = "t_orderSetting")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_orderSetting")
public class OrderSetting extends BaseEntity<Long> {
	
	private static final long serialVersionUID = 602893983167651252L;

	//下单开始时间
	private String startTime;
	
	//下单截至时间
	private String endTime;
	
	//收货时间(n天后可以收货)
	private int timeReceipt;
	
	//每天下单次数
	private int numberTimes;
	
	private Supplier supplier;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getTimeReceipt() {
		return timeReceipt;
	}

	public void setTimeReceipt(int timeReceipt) {
		this.timeReceipt = timeReceipt;
	}

	public int getNumberTimes() {
		return numberTimes;
	}

	public void setNumberTimes(int numberTimes) {
		this.numberTimes = numberTimes;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


}
