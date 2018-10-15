package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/3/28.
 * 功能描述：订货单消息通知类型
 * 修改记录：
 */
@Entity
@Table(name = "t_notice_type")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_notice_type")
public class NoticeType extends BaseEntity<Long> {


    private static final long serialVersionUID = -6801900345351024525L;

    public enum Type {
        user_order("用户下单通知"),
        user_apply_cancel("用户申请取消"),
        user_order_remark("用户备注通知"),
        user_order_update("订单修改通知"),
        user_order_complete("订单完成通知"),
        user_order_review("订单审核"),
        order_shipping("订单发货"),
        order_cancel("订单取消");

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


    public boolean isEqualType(Type type){
        return this.getType().equals(type);
    }

}
