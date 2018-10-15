package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 盘点单
 *
 * @author wuzhanbo
 * @date 2018-3-1  
 * @version 1.0
 */
@Entity
@Table(name = "t_inventory_from")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_inventory_from")
public class InventoryForm extends BaseEntity<Long>{

	private static final long serialVersionUID = -1949623861888690922L;

	private Member member;
	
	private Need need;
	
	/*
	 * 门店 
	 */
	private Shop shop;
	
	/*
	 * 盘点单号
	 */
	private String inventoryCode;
	
	private SfIfStatus status;
	
	private String remarks;
	
	/*
	 *  盘点商品
	 */
	private List<InventoryGoods> inventoryGoods = new ArrayList<>();
	
	/*
	 * 操作记录
	 */
	private List<InventoryFormLog> inventoryFormLogs = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy="inventoryForm")
	public List<InventoryGoods> getInventoryGoods() {
		return inventoryGoods;
	}

	public void setInventoryGoods(List<InventoryGoods> inventoryGoods) {
		this.inventoryGoods = inventoryGoods;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="inventoryForm")
	public List<InventoryFormLog> getInventoryFormLogs() {
		return inventoryFormLogs;
	}

	public void setInventoryFormLogs(List<InventoryFormLog> inventoryFormLogs) {
		this.inventoryFormLogs = inventoryFormLogs;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
