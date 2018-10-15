package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 入库单商品关系表
 *
 * @author wuzhanbo
 * @date 2018-3-5  
 * @version 1.0  
 */
@Entity
@Table(name = "t_storage_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_storage_goods")
public class StorageGoods extends BaseEntity<Long> {

	private static final long serialVersionUID = 477446100548733858L;

	private StorageForm storageForm;
	
	private Product product;

	private int actualStock;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public StorageForm getStorageForm() {
		return storageForm;
	}

	public void setStorageForm(StorageForm storageForm) {
		this.storageForm = storageForm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getActualStock() {
		return actualStock;
	}

	public void setActualStock(int actualStock) {
		this.actualStock = actualStock;
	}
	
}
