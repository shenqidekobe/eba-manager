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

@Entity
@Table(name = "xx_sn")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_sn")
public class Sn extends BaseEntity<Long> {

	private static final long serialVersionUID = -2935391509345231337L;

	public enum Type {

		goods,

		order,

		paymentLog,

		payment,

		refunds,

		shipping,

		returns,

		ass_goods,
		
		relation,
		
		card,
		
		goodsImage
	}

	private Type type;

	private Long lastValue;

	@Column(nullable = false, updatable = false, unique = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Long getLastValue() {
		return lastValue;
	}

	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

}
