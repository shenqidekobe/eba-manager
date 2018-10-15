package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 库存商品
 *
 * @author wuzhanbo
 * @date 2018-3-2  
 * @version 1.0  
 */
@Entity
@Table(name = "t_stock_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_stock_goods")
public class StockGoods extends BaseEntity<Long>{

	private static final long serialVersionUID = 6316900099101062534L;
	
	private Product product;
	
	/*
	 * 门店 
	 */
	private Shop shop;
	
	/*
	 * 实际库存
	 */
	private int actualStock;
	
	public enum Status {
		hide,
		
		display
	}
	
	// 库存状况（入库，或盘点显示）
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public int getActualStock() {
		return actualStock;
	}

	public void setActualStock(int actualStock) {
		this.actualStock = actualStock;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
