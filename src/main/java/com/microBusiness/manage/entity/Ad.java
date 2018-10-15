/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_ad")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ad")
public class Ad extends OrderEntity<Long> {

	private static final long serialVersionUID = -8324121858877750223L;

	public enum Type {

		text,

		image
	}

	private String title;

	private Type type;

	private String content;

	//图片路径
	private String path;

	private Date beginDate;

	private Date endDate;

	//链接
	private String url;

	private String backgroundColor;
	
	private AdPosition adPosition;

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@NotNull
	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Lob
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|ftp:\\/\\/|mailto:|\\/|#).*$")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public AdPosition getAdPosition() {
		return adPosition;
	}

	public void setAdPosition(AdPosition adPosition) {
		this.adPosition = adPosition;
	}

	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || !getBeginDate().after(new Date());
	}

	@Transient
	public boolean hasEnded() {
		return getEndDate() != null && !getEndDate().after(new Date());
	}

}
