package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 托管店铺关系表
 * @author pengtianwen	
 *	2018/3/1
 */
@Entity
@Table(name = "t_hosting_shop")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_hosting_shop")
public class HostingShop extends BaseEntity<Long> {

	private static final long serialVersionUID = 2263617044900584183L;

	//主账号
	private Member member;

	//托管账号
	private Member byMember;

	//店铺
	private Shop shop;

	//店员关系
	MemberMember memberMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="byMember")
	public Member getByMember() {
		return byMember;
	}

	public void setByMember(Member byMember) {
		this.byMember = byMember;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member")
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="shop")
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="memberMember")
	public MemberMember getMemberMember() {
		return memberMember;
	}

	public void setMemberMember(MemberMember memberMember) {
		this.memberMember = memberMember;
	}
}
