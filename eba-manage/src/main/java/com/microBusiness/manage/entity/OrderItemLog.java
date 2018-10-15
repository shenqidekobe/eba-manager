package com.microBusiness.manage.entity;

/**
 * Created by mingbai on 2017/5/25.
 * 功能描述：
 * 修改记录：
 */

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_order_item_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_item_log")
public class OrderItemLog extends BaseEntity<Long> {
    private static final long serialVersionUID = -1892575998698067058L;

    private String operatorName ;

    private Order order ;
    //之前的记录 自关联
    private OrderItemLog beforeLog ;

    private OrderItemLog afterLog ;

    List<OrderItemInfo> orderItemInfos = new ArrayList<>();
    //操作方来源
    public enum Type{
        admin,//后台企业
        custom//前台用户
    }
    

    public enum OperatorType{
        create ,
        update
    }

    private Type type ;
    
    private OperatorType operatorType ;

    private String supplierName ;
    
    private LogType logType;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders" , nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @OneToOne(fetch = FetchType.LAZY)
    public OrderItemLog getBeforeLog() {
        return beforeLog;
    }

    public void setBeforeLog(OrderItemLog beforeLog) {
        this.beforeLog = beforeLog;
    }

    @OneToMany(mappedBy = "orderItemLog", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<OrderItemInfo> getOrderItemInfos() {
        return orderItemInfos;
    }

    public void setOrderItemInfos(List<OrderItemInfo> orderItemInfos) {
        this.orderItemInfos = orderItemInfos;
    }

    @OneToOne(mappedBy = "beforeLog", fetch = FetchType.LAZY)
    public OrderItemLog getAfterLog() {
        return afterLog;
    }

    public void setAfterLog(OrderItemLog afterLog) {
        this.afterLog = afterLog;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}
    
}
