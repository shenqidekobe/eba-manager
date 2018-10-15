package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mingbai on 2017/5/25.
 * 功能描述：修改记录
 * 修改记录：
 */
@Entity
@Table(name = "t_order_item_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_item_info")
public class OrderItemInfo extends BaseEntity<Long> {

    private static final long serialVersionUID = 3633251515700163169L;

    private Product product ;
    //修改前商品数量
    private Integer beforeQuantity ;
    //修改后商品数量
    private Integer afterQuantity ;
    //修改前商品价格--客户
    private BigDecimal beforePrice ;
    //修改后商品价格--客户
    private BigDecimal afterPrice ;
    
    //修改前商品价格--企业
    private BigDecimal beforePriceB ;
    //修改后商品价格--企业
    private BigDecimal afterPriceB ;

    private OrderItemLog orderItemLog ;

    private BigDecimal beforeTotalPrice ;

    private BigDecimal afterTotalPrice ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false , foreignKey = @ForeignKey(name = "null"))
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getBeforeQuantity() {
        return beforeQuantity;
    }

    public void setBeforeQuantity(Integer beforeQuantity) {
        this.beforeQuantity = beforeQuantity;
    }

    public Integer getAfterQuantity() {
        return afterQuantity;
    }

    public void setAfterQuantity(Integer afterQuantity) {
        this.afterQuantity = afterQuantity;
    }

    public BigDecimal getBeforePrice() {
        return beforePrice;
    }

    public void setBeforePrice(BigDecimal beforePrice) {
        this.beforePrice = beforePrice;
    }

    public BigDecimal getAfterPrice() {
        return afterPrice;
    }

    public void setAfterPrice(BigDecimal afterPrice) {
        this.afterPrice = afterPrice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public OrderItemLog getOrderItemLog() {
        return orderItemLog;
    }

    public void setOrderItemLog(OrderItemLog orderItemLog) {
        this.orderItemLog = orderItemLog;
    }

    @Transient
    public BigDecimal getBeforeTotalPrice() {
        if (getBeforePrice() != null && getBeforeQuantity() != null) {
            return getBeforePrice().multiply(new BigDecimal(getBeforeQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Transient
    public BigDecimal getAfterTotalPrice() {
        if (getAfterPrice() != null && getAfterQuantity() != null) {
            return getAfterPrice().multiply(new BigDecimal(getAfterQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }

	public BigDecimal getBeforePriceB() {
		return beforePriceB;
	}

	public void setBeforePriceB(BigDecimal beforePriceB) {
		this.beforePriceB = beforePriceB;
	}

	public BigDecimal getAfterPriceB() {
		return afterPriceB;
	}

	public void setAfterPriceB(BigDecimal afterPriceB) {
		this.afterPriceB = afterPriceB;
	}
    
}
