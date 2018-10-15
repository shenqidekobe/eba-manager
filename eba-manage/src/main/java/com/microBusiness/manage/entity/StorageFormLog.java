package com.microBusiness.manage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 入库单操作记录
 *
 * @author wuzhanbo
 * @date 2018-3-1  
 * @version 1.0
 */
@Entity
@Table(name = "t_storage_from_Log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_storage_from_Log")
public class StorageFormLog extends BaseEntity<Long>{

	private static final long serialVersionUID = -5834779696838193670L;

	private Member member;
	
	/*
	 * 门店 
	 */
	private Shop shop;

	private StorageForm storageForm;
	
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

	/*
	 * 订单
	 */
	private Order order;
	
	private List<Entry> entries = new ArrayList<Entry>();
	
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
	public StorageForm getStorageForm() {
		return storageForm;
	}

	public void setStorageForm(StorageForm storageForm) {
		this.storageForm = storageForm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column(nullable = false, length = 4000)
	@Convert(converter = EntryConverter.class)
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	@Converter
	public static class EntryConverter extends BaseAttributeConverter<List<Entry>> implements AttributeConverter<Object, String> {
	}
	
	public static class Entry implements Serializable {

		private static final long serialVersionUID = -5791623464288063512L;

		private String name;

		private String url;

		private String product;
		
		private Integer number;
		
		@NotNull
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@NotEmpty
		@Length(max = 200)
		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}
		
	}

}
