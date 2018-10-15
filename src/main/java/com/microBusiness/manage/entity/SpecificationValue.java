/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class SpecificationValue implements Serializable {

	private static final long serialVersionUID = 3452938625509529786L;

	private Integer id;

	private String value;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotEmpty
	@Length(max = 200)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
