package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/10/16.
 * 功能描述：订货单消息通知 的 个体客户
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_user_order_need")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_user_order_need")
public class NoticeUserOrderNeed extends BaseEntity<Long> {


    private static final long serialVersionUID = -1518839096772160916L;

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
