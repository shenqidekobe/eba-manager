package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.microBusiness.manage.entity.BaseEntity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "ass_card")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_card")
public class AssCard extends BaseEntity<Long> {

	private static final long serialVersionUID = 6177189538322317697L;

    // 姓名
    private String name;
    
    // 电话
    private String phone;
    
    // 公司名称
    private String companyName;
    
    // 职位
    private String position;
    
    // 邮箱
    private String email;
    
    // 微信号
    private String wxNum;

    //简介
  	private String profiles;

  	private String sn;
  	
  	public enum ShareType{
		share,
		noshare
	}
  	
  	private ShareType shareType;
  	
  	private AssChildMember assChildMember;
  	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWxNum() {
		return wxNum;
	}

	public void setWxNum(String wxNum) {
		this.wxNum = wxNum;
	}

	public String getProfiles() {
		return profiles;
	}

	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
	}

	public ShareType getShareType() {
		return shareType;
	}

	public void setShareType(ShareType shareType) {
		this.shareType = shareType;
	}
 
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
