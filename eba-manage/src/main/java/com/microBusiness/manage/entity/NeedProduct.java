package com.microBusiness.manage.entity;

import javax.persistence.*;
import javax.persistence.Transient;
import java.beans.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：收货点商品关系表
 * 修改记录：
 */

@Entity
@Table(name = "t_need_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need_product")
public class NeedProduct extends BaseEntity<Long> {
    private static final long serialVersionUID = -6003809858180050619L;

    private Product products ;

    private BigDecimal supplyPrice ;

    public enum Status{
        WAIT_CHECK,
        CHECKED ,
        REFUSE
    }

    private Status status ;

    private SupplyNeed supplyNeed ;

    //最小起订量
    private Integer minOrderQuantity ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products" )
    public Product getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products = products;
    }


    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    @Transient
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public SupplyNeed getSupplyNeed() {
        return supplyNeed;
    }

    public void setSupplyNeed(SupplyNeed supplyNeed) {
        this.supplyNeed = supplyNeed;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }
}
