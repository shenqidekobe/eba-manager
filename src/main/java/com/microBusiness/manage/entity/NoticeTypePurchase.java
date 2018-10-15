package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/7/21.
 * 功能描述：采购单消息通知类型
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_type_purchase")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_type_purchase")
public class NoticeTypePurchase extends BaseEntity<Long> {
    public enum Type {
        order_create("下单成功通知"),
        order_review("订单审核通知"),
        order_remark("订单备注通知"),
        order_shipped("订单发货通知"),
        order_update("订单修改通知"),
        order_cancel("订单取消通知"),
        user_apply_cancel("用户申请取消"),
        order_complete("订单完成");
        private String desc;

        Type(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    private NoticeUser noticeUser ;

    private Type type ;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public NoticeUser getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(NoticeUser noticeUser) {
        this.noticeUser = noticeUser;
    }

}
