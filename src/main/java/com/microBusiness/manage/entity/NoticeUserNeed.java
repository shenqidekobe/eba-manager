package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/7/26.
 * 功能描述：
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_user_need")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_user_need")
public class NoticeUserNeed extends BaseEntity<Long> {

    private static final long serialVersionUID = 5212829146330589141L;

    private NoticeUser noticeUser ;

    private Need need ;

    @ManyToOne(fetch = FetchType.LAZY)
    public NoticeUser getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(NoticeUser noticeUser) {
        this.noticeUser = noticeUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="need" , nullable = false, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }
}
