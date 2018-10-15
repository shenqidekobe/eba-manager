package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 盘点单商品关系表
 *
 * @author wuzhanbo
 * @date 2018-3-5  
 * @version 1.0  
 */
@Entity
@Table(name = "t_inventory_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_inventory_goods")
public class InventoryGoods extends BaseEntity<Long> {

	private static final long serialVersionUID = 477446100548733858L;

	private InventoryForm inventoryForm;
	
	private Product product;

	/*
	 * 盘点库存
	 */
	private int inventoryStock;
	
	/*
	 * 实际库存
	 */
	private int actualStock;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public InventoryForm getInventoryForm() {
		return inventoryForm;
	}

	public void setInventoryForm(InventoryForm inventoryForm) {
		this.inventoryForm = inventoryForm;
	}

	public int getInventoryStock() {
		return inventoryStock;
	}

	public void setInventoryStock(int inventoryStock) {
		this.inventoryStock = inventoryStock;
	}

	public int getActualStock() {
		return actualStock;
	}

	public void setActualStock(int actualStock) {
		this.actualStock = actualStock;
	}

}
