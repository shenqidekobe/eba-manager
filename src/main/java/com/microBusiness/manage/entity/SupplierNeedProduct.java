package com.microBusiness.manage.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * 供应商对自己收货点分配商品
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_supplier_need_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supplier_need_product")
public class SupplierNeedProduct  extends BaseEntity<Long>{
	private static final long serialVersionUID = 1765211212106676344L;
	
	private SupplierSupplier supplyRelation;
	
	private Need need;
	
	private Product products;
	
	private BigDecimal supplyPrice;
	
	private SupplierAssignRelation assignRelation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public SupplierSupplier getSupplyRelation() {
		return supplyRelation;
	}

	public void setSupplyRelation(SupplierSupplier supplyRelation) {
		this.supplyRelation = supplyRelation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
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
	@JoinColumn(nullable = false, updatable = false)
	public SupplierAssignRelation getAssignRelation() {
		return assignRelation;
	}

	public void setAssignRelation(SupplierAssignRelation assignRelation) {
		this.assignRelation = assignRelation;
	}


}
