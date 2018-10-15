/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "xx_seo")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_seo")
public class Seo extends BaseEntity<Long> {

	private static final long serialVersionUID = -5092553376554591887L;

	public enum Type {

		index,

		articleList,

		articleSearch,

		articleContent,

		goodsList,

		goodsSearch,

		goodsContent,

		brandList,

		brandContent
	}

	private Type type;

	private String title;

	private String keywords;

	private String description;

	@Column(nullable = false, updatable = false, unique = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(max = 200)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		if (keywords != null) {
			keywords = keywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keywords = keywords;
	}

	@Length(max = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
