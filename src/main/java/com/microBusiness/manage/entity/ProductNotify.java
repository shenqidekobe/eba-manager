/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_product_notify")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product_notify")
public class ProductNotify extends BaseEntity<Long> {

	private static final long serialVersionUID = 4840137591578930330L;

	private String email;

	private Boolean hasSent;

	private Member member;

	private Product product;

	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false)
	public Boolean getHasSent() {
		return hasSent;
	}

	public void setHasSent(Boolean hasSent) {
		this.hasSent = hasSent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
