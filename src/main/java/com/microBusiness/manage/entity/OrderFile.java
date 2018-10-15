/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

public class OrderFile implements Serializable, Comparable<OrderFile> {


	private static final long serialVersionUID = -692847400852963808L;

	private String title;

	private String source;

	private Integer order;

	private long size ;

	private MultipartFile file;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@JsonIgnore
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getSource()));
	}

	public int compareTo(OrderFile orderFile) {
		if (orderFile == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), orderFile.getOrder()).toComparison();
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
