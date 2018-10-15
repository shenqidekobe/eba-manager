package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/7/26.
 * 功能描述：
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_user_supplier")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_user_supplier")
public class NoticeUserSupplier extends BaseEntity<Long> {

    private static final long serialVersionUID = -9198945641387252719L;

    private NoticeUser noticeUser ;

    private Supplier supplier ;

    @ManyToOne(fetch = FetchType.LAZY)
    public NoticeUser getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(NoticeUser noticeUser) {
        this.noticeUser = noticeUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier" , nullable = false, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
