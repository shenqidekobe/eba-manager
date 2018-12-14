package com.microBusiness.manage.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

/**
 * 供应商之间产生关系
 * @author Administrator
 *
 */
@Indexed
@Entity
@Table(name = "t_supplier_supplier")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_supplier_supplier")
public class SupplierSupplier extends BaseEntity<Long>{
	
	private static final long serialVersionUID = 1765211212106676344L;

	public enum Status{
		toBeConfirmed,//待确认
		inTheSupply,//供应中（已确认）
		rejected,//已拒绝
		suspendSupply,//暂停供应
		expired,//已过期(已失效)
		willSupply//未开始
    }
	
	private Supplier supplier;//供应商
	
	private Supplier bySupplier;//被供应企业
	
	private Date startDate;
	
	private Date endDate;
	
	private Status status;

	private Set<SupplierProduct> supplierProducts=new HashSet<SupplierProduct>();
	
	private Set<SupplierNeedProduct>  supplierNeedProducts=new HashSet<SupplierNeedProduct>();
	
	private Set<SupplierAssignRelation> supplierAssignRelations=new HashSet<SupplierAssignRelation>();

	private boolean openNotice ;

	private Integer noticeDay ;
	//供应批次
	private Integer supplyBatch;

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getBySupplier() {
		return bySupplier;
	}

	public void setBySupplier(Supplier bySupplier) {
		this.bySupplier = bySupplier;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false)
	@OneToMany(mappedBy = "supplyRelation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<SupplierProduct> getSupplierProducts() {
		return supplierProducts;
	}

	public void setSupplierProducts(Set<SupplierProduct> supplierProducts) {
		this.supplierProducts = supplierProducts;
	}

	@Column(nullable = false)
	@OneToMany(mappedBy = "supplyRelation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<SupplierNeedProduct> getSupplierNeedProducts() {
		return supplierNeedProducts;
	}

	@Column(nullable = false)
	@OneToMany(mappedBy = "supplyRelation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<SupplierAssignRelation> getSupplierAssignRelations() {
		return supplierAssignRelations;
	}

	public void setSupplierAssignRelations(Set<SupplierAssignRelation> supplierAssignRelations) {
		this.supplierAssignRelations = supplierAssignRelations;
	}

	public void setSupplierNeedProducts(Set<SupplierNeedProduct> supplierNeedProducts) {
		this.supplierNeedProducts = supplierNeedProducts;
	}

	public boolean getOpenNotice() {
		return openNotice;
	}

	public void setOpenNotice(boolean openNotice) {
		this.openNotice = openNotice;
	}

	public Integer getNoticeDay() {
		return noticeDay;
	}

	public void setNoticeDay(Integer noticeDay) {
		this.noticeDay = noticeDay;
	}

	public Integer getSupplyBatch() {
		return supplyBatch;
	}

	public void setSupplyBatch(Integer supplyBatch) {
		this.supplyBatch = supplyBatch;
	}
	
	 /**
     * 获取企业下所有product数量
     * @return
     */
    @Transient
    public Integer getProductCount(){
    	Set<SupplierProduct> supplierProducts=this.getSupplierProducts();
        Integer productSize=0;
        for (SupplierProduct supplierProduct : supplierProducts) {
        	Product product=supplierProduct.getProducts();
        	if (!product.getGoods().isDeleted()) {
        		productSize++;
        	}
        }
        return productSize;
    }
}
