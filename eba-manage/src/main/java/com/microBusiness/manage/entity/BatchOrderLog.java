package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingbai on 2017/7/27.
 * 功能描述：批量下单记录实体，用户微信端批量查看订单信息
 * 修改记录：
 */
@Entity
@Table(name = "t_batch_order_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_batch_order_log")
public class BatchOrderLog extends BaseEntity<Long> {

    private static final long serialVersionUID = -7376608086243859811L;

    private List<BatchOrderOrder> batchOrderOrders = new ArrayList<>();

    private List<BatchOrderItem> batchOrderItems = new ArrayList<>() ;

    @OneToMany(mappedBy = "batchOrderLog" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<BatchOrderOrder> getBatchOrderOrders() {
        return batchOrderOrders;
    }

    public void setBatchOrderOrders(List<BatchOrderOrder> batchOrderOrders) {
        this.batchOrderOrders = batchOrderOrders;
    }

    @OneToMany(mappedBy = "batchOrderLog" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    public List<BatchOrderItem> getBatchOrderItems() {
        return batchOrderItems;
    }

    public void setBatchOrderItems(List<BatchOrderItem> batchOrderItems) {
        this.batchOrderItems = batchOrderItems;
    }
}
