package com.microBusiness.manage.entity;

import javax.persistence.*;

/**
 * Created by mingbai on 2017/7/27.
 * 功能描述：批量下单的订单
 * 修改记录：
 */
@Entity
@Table(name = "t_batch_order_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_batch_order_order")
public class BatchOrderOrder extends BaseEntity<Long> {

    private static final long serialVersionUID = 2559741256039517531L;

    private BatchOrderLog batchOrderLog ;

    private Order order ;

    @ManyToOne(fetch = FetchType.LAZY)
    public BatchOrderLog getBatchOrderLog() {
        return batchOrderLog;
    }

    public void setBatchOrderLog(BatchOrderLog batchOrderLog) {
        this.batchOrderLog = batchOrderLog;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders" , nullable = false, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
