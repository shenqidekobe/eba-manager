package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

@Entity
@Table(name = "ass_form")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_form")
public class AssForm extends BaseEntity<Long> {

	private static final long serialVersionUID = -2460583436988516525L;
	
	private String formId;
	
	private AssChildMember assChildMember;
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

}
