/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SpecificationItem implements Serializable {

	private static final long serialVersionUID = 7506996458276663826L;

	private String name;

	private List<Entry> entries = new ArrayList<Entry>();

	@NotEmpty
	@Length(max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Valid
	@NotEmpty
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	@JsonIgnore
	public boolean isSelected() {
		if (CollectionUtils.isNotEmpty(getEntries())) {
			for (Entry entry : getEntries()) {
				if (entry.getIsSelected()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValid(SpecificationValue specificationValue) {
		if (specificationValue != null && specificationValue.getId() != null && StringUtils.isNotEmpty(specificationValue.getValue()) && CollectionUtils.isNotEmpty(getEntries())) {
			for (Entry entry : getEntries()) {
				if (entry != null && entry.getIsSelected() && specificationValue.getId().equals(entry.getId()) && StringUtils.equals(entry.getValue(), specificationValue.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	public static class Entry implements Serializable {

		private static final long serialVersionUID = 3450488045828163309L;

		private Integer id;

		private String value;

		private Boolean isSelected;
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

		@NotNull
		public Boolean getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(Boolean isSelected) {
			this.isSelected = isSelected;
		}

	}

}
