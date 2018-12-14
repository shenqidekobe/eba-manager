package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

/**
 * 应商 给自己的 收货点分配 商品产生的关系
 * @author Administrator
 *
 */
@Indexed
@Entity
@Table(name = "t_supplier_assign_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supplier_assign_relation")
public class SupplierAssignRelation extends BaseEntity<Long>{

	private static final long serialVersionUID = 1765211212106676344L;
	
	private Need need;
	
	private SupplierSupplier supplyRelation;
	
	private List<SupplierNeedProduct> supplierNeedProducts=new ArrayList<>();

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
	public SupplierSupplier getSupplyRelation() {
		return supplyRelation;
	}

	public void setSupplyRelation(SupplierSupplier supplyRelation) {
		this.supplyRelation = supplyRelation;
	}
	
	@OneToMany(mappedBy = "assignRelation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<SupplierNeedProduct> getSupplierNeedProducts() {
		return supplierNeedProducts;
	}

	public void setSupplierNeedProducts(List<SupplierNeedProduct> supplierNeedProducts) {
		this.supplierNeedProducts = supplierNeedProducts;
	}
	
	
}
