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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_navigation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_navigation")
public class Navigation extends OrderEntity<Long> {

	private static final long serialVersionUID = -5858697471501064178L;

	public enum Position {

		top,

		middle,

		bottom
	}

	private String name;

	private Position position;

	private String url;

	private Boolean isBlankTarget;

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@NotEmpty
	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|ftp:\\/\\/|mailto:|\\/|#).*$")
	@Column(nullable = false)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsBlankTarget() {
		return isBlankTarget;
	}

	public void setIsBlankTarget(Boolean isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}

}
