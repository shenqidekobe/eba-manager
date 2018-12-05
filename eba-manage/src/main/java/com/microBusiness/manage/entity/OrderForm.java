package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_form")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_form")
public class OrderForm extends BaseEntity<Long> {

	private static final long serialVersionUID = -3890769051174028219L;

	private String formId;
	
	private ChildMember childMember;
	
	private Integer useNum=0;//使用次数

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public ChildMember getChildMember() {
		return childMember;
	}

	public void setChildMember(ChildMember childMember) {
		this.childMember = childMember;
	}

	public Integer getUseNum() {
		return useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}
	
	
}
