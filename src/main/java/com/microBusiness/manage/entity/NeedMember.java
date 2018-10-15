package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：收货点账号，用于登录微信公众号
 * 修改记录：
 */
@Deprecated
@Entity
@Table(name = "t_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need_member")
public class NeedMember extends BaseEntity<Long> {

    private static final long serialVersionUID = 5597813903033974660L;

    private String openId ;

    private String tel ;

    private Need need ;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="need" , updatable = false,foreignKey = @ForeignKey(name = "null"))
    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }
}
