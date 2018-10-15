package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 员工表（托管店员）
 * @author yuezhiwei
 *
 */
@Entity
@Table(name = "t_member_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_member_member")
public class MemberMember extends BaseEntity<Long> {

	private static final long serialVersionUID = 1118977331564914704L;
	//主账号
	private Member member;
	//托管账号
	private Member byMember;
	//店员姓名
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getByMember() {
		return byMember;
	}

	public void setByMember(Member byMember) {
		this.byMember = byMember;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
