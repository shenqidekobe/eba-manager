package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

/**
 * 分享页面访问记录
 * @author admin
 *
 */
@Entity
@Table(name = "ass_page_visit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_page_visit")
public class AssPageVisit extends BaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5786808057516461644L;
	//用户名称
	private String name;
	//访问的分享源
	private AssCustomerRelation assCustomerRelation;
	//访问用户
	private AssChildMember assChildMember;
	//访问用户的ip
	private String ip;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}
	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}
	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
}
