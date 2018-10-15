package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

/**
 * 名片收藏关系表
 *
 */
@Entity
@Table(name = "ass_collection_card")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_collection_card")
public class AssCollectionCard extends BaseEntity<Long>{

	private static final long serialVersionUID = -7050844968524211401L;
	
	private AssCard assCard;
	
	private AssChildMember assChildMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assCard", nullable = false)
	public AssCard getAssCard() {
		return assCard;
	}

	public void setAssCard(AssCard assCard) {
		this.assCard = assCard;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assChildMember", nullable = false)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}
	
	
	
}
