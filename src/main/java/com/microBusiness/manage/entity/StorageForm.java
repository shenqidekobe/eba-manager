package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 入库单
 *
 * @author 吴战波
 * @date 2018-3-1  
 * @version 1.0
 */
@Entity
@Table(name = "t_storage_from")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_storage_from")
public class StorageForm extends BaseEntity<Long>{

	private static final long serialVersionUID = -4847525819456256264L;

	/*
	 * 店铺
	 */
	private Shop shop;
	
	/*
	 * 供应商
	 */
	private Supplier supplier;
	
	private SupplierType supplierType;
	
	private SupplyNeed supplyNeed;
	
	private SupplierSupplier supplierSupplier;
	
	private SupplyType supplyType;
	
	private Member member;
	
	private Need need;
	
	/*
	 * 入库单号 
	 */
	private String storageCode;
	
	/*
	 * 入库时间
	 */
	private Date storageDate;
	
	/*
	 * 备注
	 */
	private String remarks;

	private SfIfStatus status;
	
	/*
	 * 入库商品
	 */
	private List<StorageGoods> StorageGoods = new ArrayList<>();
	
	/*
	 * 入库单记录
	 */
	private List<StorageFormLog> StorageFormLogs = new ArrayList<>();
	
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public SfIfStatus getStatus() {
		return status;
	}

	public void setStatus(SfIfStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="storageForm")
	public List<StorageGoods> getStorageGoods() {
		return StorageGoods;
	}

	public void setStorageGoods(List<StorageGoods> storageGoods) {
		StorageGoods = storageGoods;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="storageForm")
	public List<StorageFormLog> getStorageFormLogs() {
		return StorageFormLogs;
	}

	public void setStorageFormLogs(List<StorageFormLog> storageFormLogs) {
		StorageFormLogs = storageFormLogs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public SupplierType getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public SupplyNeed getSupplyNeed() {
		return supplyNeed;
	}

	public void setSupplyNeed(SupplyNeed supplyNeed) {
		this.supplyNeed = supplyNeed;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public SupplierSupplier getSupplierSupplier() {
		return supplierSupplier;
	}

	public void setSupplierSupplier(SupplierSupplier supplierSupplier) {
		this.supplierSupplier = supplierSupplier;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
