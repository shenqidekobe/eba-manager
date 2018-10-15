package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;

/**
 * 
 * Created by yuezhiwei on 2017/6/2.
 * 功能描述：订单下的备注信息
 */
@Entity
@Table(name = "t_order_remarks")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_remarks")
public class OrderRemarks extends BaseEntity<Long> {

	private static final long serialVersionUID = 3484211789883155151L;
	
	//备注信息
	@Column(nullable = false)
	private String description;
	
	//备注人
	private String name;
	
	//备注附件
	private List<RemarksFile> annex;
	
	public enum Source {
		admin,
		customer;
	}
	
	//备注信息来源
	private Source source;

	//备注人所在企业
	private String suppliper;
	
	//关联订单
	private Order order;
	
	public enum MsgType{
	    btob,//企业对企业
	    btoc//企业对个体客户
	}
	private MsgType msgType;
	
	private LogType logType;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(updatable = false,length = 4000)
	@Convert(converter = RemarksUrlConverter.class)
	public List<RemarksFile> getAnnex() {
		return annex;
	}

	public void setAnnex(List<RemarksFile> annex) {
		this.annex = annex;
	}
	
	@Converter
	public static class RemarksUrlConverter extends BaseAttributeConverter<List<RemarksFile>> implements AttributeConverter<Object, String> {
		
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getSuppliper() {
		return suppliper;
	}

	public void setSuppliper(String suppliper) {
		this.suppliper = suppliper;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	
}
