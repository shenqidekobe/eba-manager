package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 盘点单操作记录
 *
 * @author wuzhanbo
 * @date 2018-3-1  
 * @version 1.0
 */
@Entity
@Table(name = "t_inventory_from_Log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_inventory_from_Log")
public class InventoryFormLog extends BaseEntity<Long>{

	private static final long serialVersionUID = 5170954413769869037L;

	private Member member;
	
	/*
	 * 门店 
	 */
	private Shop shop;

	private InventoryForm inventoryForm;
	
	public enum Record {
		// 创建
		create,
				
		// 已取消
		canceled,
		
		// 编辑
		edit,
		
		// 已完成
		completed
	}
	
	private Record record;

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public InventoryForm getInventoryForm() {
		return inventoryForm;
	}

	public void setInventoryForm(InventoryForm inventoryForm) {
		this.inventoryForm = inventoryForm;
	}
	
}
