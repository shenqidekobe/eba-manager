package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @description:店铺分配商品表
 * @author: pengtianwen
 * @create: 2018-03-08 11:25
 **/

@Entity
@Table(name = "t_need_shop_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need_shop_product")
public class NeedShopProduct extends BaseEntity<Long> {
    private static final long serialVersionUID = -5938567109839848373L;

    private Product products ;

    //价格
    private BigDecimal supplyPrice ;

    //店铺供应关系
    private SupplyNeed supplyNeed ;

    //最小起订量
    private Integer minOrderQuantity ;

    //店铺
    private Shop shop;

    //本地企业  用于本地企业分配商品
    private Supplier supplier;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplyNeed" )
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop" )
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier" )
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
