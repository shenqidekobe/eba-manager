package com.microBusiness.manage.entity;

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
 * @author yuezhiwei
 * 分享备注信息
 *
 */
@Entity
@Table(name = "t_share_notes")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_share_notes")
public class ShareNotes extends BaseEntity<Long> {

	private static final long serialVersionUID = -6154854712265848266L;
	
	//备注信息
	private String description;
	
	//备注人
	private String name;

	//备注附件
	private List<RemarksFile> annex;
	
	//备注人账号
	private ChildMember childMember;
	
	//备注所属订单
	private Order order;

	@Column(nullable = true)
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

	@Column(updatable = false,length = 4000)
	@Convert(converter = RemarksUrlConverter.class)
	public List<RemarksFile> getAnnex() {
		return annex;
	}

	public void setAnnex(List<RemarksFile> annex) {
		this.annex = annex;
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
	
	@Converter
	public static class RemarksUrlConverter extends BaseAttributeConverter<List<RemarksFile>> implements AttributeConverter<Object, String> {
		
	}
	
}
