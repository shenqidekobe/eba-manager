package com.microBusiness.manage.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by yuezhiwei on 2017/8/8.
 * 功能描述：绑定手机号发送验证码
 * 修改记录：
 */
@Entity
@Table(name = "t_bindPhoneSms")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_bindPhoneSms")
public class BindPhoneSms extends BaseEntity<Long> {

	private static final long serialVersionUID = -8108828971488548432L;

	/**
	 * 用户ID
	 */
	private Long adminId;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 验证码
	 */
	private String code;
	
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	public enum Status{
		//有效
        EFFECTIVE,
        //过期
        EXPIRED,
        //使用过
        USED
	}
	
	/**
	 * 状态 0.有效 1.过期
	 */
	private Status status;
	
	public enum SmsType{
		//绑定手机号
		bindPhoneNum,
		//更换手机号
		replacePhoneNum,
		//找回密码
		getBackPassword,
		//微商小管理分享助手绑定手机号
		assBindPhoneNum
	}
	
	/**
	 * 类型
	 */
	private SmsType smsType;

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SmsType getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsType smsType) {
		this.smsType = smsType;
	}
	
}
