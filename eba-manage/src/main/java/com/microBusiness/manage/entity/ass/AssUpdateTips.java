package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;

/**
 * 更新操作提示实体
 * @author yuezhiwei
 *
 */
@Entity
@Table(name = "ass_update_tips")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_update_tips")
public class AssUpdateTips extends BaseEntity<Long> {

	private static final long serialVersionUID = -4444138999523812155L;

	private AssGoodDirectory assGoodDirectory;
	
	private AssChildMember assChildMember;
	
	//更新提示
	public enum WhetherUpdate {
		yes,
		no
	}
	
	private WhetherUpdate whetherUpdate;
	
	public enum Type {
		//本企业商品
		companyGoods,
		//个体客户
		individualCustomers,
		//企业客户
		businessCustomers
		
	}
	
	private Type type;
	
	private Need need;
	
	private CustomerRelation customerRelation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public AssGoodDirectory getAssGoodDirectory() {
		return assGoodDirectory;
	}

	public void setAssGoodDirectory(AssGoodDirectory assGoodDirectory) {
		this.assGoodDirectory = assGoodDirectory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	public WhetherUpdate getWhetherUpdate() {
		return whetherUpdate;
	}

	public void setWhetherUpdate(WhetherUpdate whetherUpdate) {
		this.whetherUpdate = whetherUpdate;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public CustomerRelation getCustomerRelation() {
		return customerRelation;
	}

	public void setCustomerRelation(CustomerRelation customerRelation) {
		this.customerRelation = customerRelation;
	}
	
}
