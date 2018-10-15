package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

@Entity
@Table(name = "ass_goods_sync_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_goods_sync_log")
public class AssGoodsSyncLog extends BaseEntity<Long>{

	private static final long serialVersionUID = -8451758765923878025L;

	// 分享人
	private AssChildMember assChildMember;
	
	// 同步人
	private AssChildMember beAssChildMember;
	
	// 商品条目
	private AssCustomerRelation assCustomerRelation;

	@ManyToOne(fetch = FetchType.LAZY)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssChildMember getBeAssChildMember() {
		return beAssChildMember;
	}

	public void setBeAssChildMember(AssChildMember beAssChildMember) {
		this.beAssChildMember = beAssChildMember;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}

	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}

}
