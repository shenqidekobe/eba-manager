package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

/**
 * 名片商品条目关联表
 * 
 * @author 吴战波
 */
@Entity
@Table(name = "ass_card_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_card_goods")
public class AssCardGoods extends BaseEntity<Long>{

	private static final long serialVersionUID = 6759819573985263062L;

	private AssCard assCard;
	
	private AssCustomerRelation assCustomerRelation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assCard", nullable = false, updatable = false)
	public AssCard getAssCard() {
		return assCard;
	}

	public void setAssCard(AssCard assCard) {
		this.assCard = assCard;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assCustomerRelation", nullable = false, updatable = false)
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}

	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}
}
