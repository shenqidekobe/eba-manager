package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/7/27.
 * 功能描述：批量下单的商品信息
 * 修改记录：
 */
@Entity
@Table(name = "t_batch_order_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_batch_order_item")
public class BatchOrderItem extends BaseEntity<Long> {

    private static final long serialVersionUID = -4442436493129990405L;

    private OrderItem orderItem ;

    private BatchOrderLog batchOrderLog ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderItem" , nullable = false, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public BatchOrderLog getBatchOrderLog() {
        return batchOrderLog;
    }

    public void setBatchOrderLog(BatchOrderLog batchOrderLog) {
        this.batchOrderLog = batchOrderLog;
    }
}
