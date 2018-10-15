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
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "t_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_message_config")
public class NewMessage extends BaseEntity<Long> {

	private static final long serialVersionUID = 3634947582650426703L;

	private String title;
	
	private String content;
	
	public enum Delflag{
		delflag_no,//0.未删除 
		delflag_had,//1.已删除
	}
	
	public Delflag delflag;

	@Column(nullable = false, updatable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotEmpty
	@Length(max = 4000)
	@Column(nullable = false, updatable = false, length = 4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Delflag getDelflag() {
		return delflag;
	}

	public void setDelflag(Delflag delflag) {
		this.delflag = delflag;
	}
	
}
