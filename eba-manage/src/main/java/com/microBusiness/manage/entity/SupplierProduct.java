package com.microBusiness.manage.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

/**
 * 供应商供应的商品
 * @author Administrator
 *
 */
@Indexed
@Entity
@Table(name = "t_supplier_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supplier_product")
public class SupplierProduct extends BaseEntity<Long>{

	private static final long serialVersionUID = -5717391440275992276L;
	
	private BigDecimal supplyPrice;
	
	private Product products;
	
	private SupplierSupplier supplyRelation;
	//最小起订量
	private Integer minOrderQuantity ;
	
	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    public Product getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products = products;
    }

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public SupplierSupplier getSupplyRelation() {
		return supplyRelation;
	}

	public void setSupplyRelation(SupplierSupplier supplyRelation) {
		this.supplyRelation = supplyRelation;
	}

	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}
}
