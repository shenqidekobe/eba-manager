package com.microBusiness.manage.entity.ass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Member;

/**
 * 参与清单  关系
 * @author Administrator
 *
 */
@Entity
@Table(name = "ass_list_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_list_relation")
public class AssListRelation  extends BaseEntity<Long>{

	private static final long serialVersionUID = 4482648388817095276L;

	public enum Type{
		participant,//参与人
		sponsor   //发起人
	}
	
	private Type type;
	
	private AssChildMember assChildMember;
	
	private AssList assList;
	
	private Member member;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assChildMember", nullable = false, updatable = false)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assList", nullable = false, updatable = false)
	public AssList getAssList() {
		return assList;
	}

	public void setAssList(AssList assList) {
		this.assList = assList;
	}

	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
}
