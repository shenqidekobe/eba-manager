/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import com.microBusiness.manage.BaseAttributeConverter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "xx_specification_center_import")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_specification_center_import")
public class SpecificationCenterImport extends OrderEntity<Long> {


	private String name;

	private List<String> options = new ArrayList<String>();

	private CategoryCenterImport productCategoryImport ;

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
	public CategoryCenterImport getProductCategoryImport() {
		return productCategoryImport;
	}

	public void setProductCategoryImport(CategoryCenterImport productCategoryImport) {
		this.productCategoryImport = productCategoryImport;
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

}
