package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mingbai on 2017/2/7.
 * 功能描述：
 * 修改记录：
 */
@Entity
@Table(name = "t_sms")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_sms")
public class Sms extends BaseEntity<Long> {
    /**
     * 用户ID
     */
    private Long userId;

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
        //手机号绑定
        BINDING
    }
    /**
     * 类型
     */
    private SmsType type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public SmsType getType() {
        return type;
    }

    public void setType(SmsType type) {
        this.type = type;
    }
}
