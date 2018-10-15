/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.BaseAttributeConverter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_specification")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_specification")
public class Specification extends OrderEntity<Long> {

	private static final long serialVersionUID = -6559554822029438998L;

	private String name;

	private ProductCategory productCategory;

	private List<String> options = new ArrayList<String>();

	private Supplier supplier ;
	
	//来源
	private Specification source;
	//类型（本地/平台）
	private Types types;
	
	private Member member;

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(groups = Save.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@NotEmpty
	@Column(nullable = false, length = 4000)
	@Convert(converter = OptionConverter.class)
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	@Converter
	public static class OptionConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Specification getSource() {
		return source;
	}

	public void setSource(Specification source) {
		this.source = source;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member" ,foreignKey = @ForeignKey(name = "null"))
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
