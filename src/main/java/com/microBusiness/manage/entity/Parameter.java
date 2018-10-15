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
@Table(name = "xx_parameter")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_parameter")
public class Parameter extends OrderEntity<Long> {

	private static final long serialVersionUID = -1411124830279048291L;

	private String group;

	private ProductCategory productCategory;

	private List<String> names = new ArrayList<String>();

	@NotEmpty
	@Length(max = 200)
	@Column(name = "parameter_group", nullable = false)
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
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
	@Convert(converter = NameConverter.class)
	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	@Converter
	public static class NameConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
