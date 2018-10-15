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
@Table(name = "t_specification_center")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_specification_center")
public class SpecificationCenter extends OrderEntity<Long> {

	private static final long serialVersionUID = -6559554822029438998L;

	private String name;

	private CategoryCenter categoryCenter;

	private List<String> options = new ArrayList<String>();

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
	public CategoryCenter getCategoryCenter() {
		return categoryCenter;
	}

	public void setCategoryCenter(CategoryCenter categoryCenter) {
		this.categoryCenter = categoryCenter;
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
