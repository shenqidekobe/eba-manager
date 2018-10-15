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

@Entity
@Table(name = "xx_message_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_message_config")
public class MessageConfig extends BaseEntity<Long> {

	private static final long serialVersionUID = -4975245143148285017L;

	public enum Type {

		registerMember,

		createOrder,

		updateOrder,

		cancelOrder,

		reviewOrder,

		paymentOrder,

		refundsOrder,

		shippingOrder,

		returnsOrder,

		receiveOrder,

		completeOrder,

		failOrder
	}

	private Type type;

	private Boolean isMailEnabled;

	private Boolean isSmsEnabled;

	@Column(nullable = false, updatable = false, unique = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsMailEnabled() {
		return isMailEnabled;
	}

	public void setIsMailEnabled(Boolean isMailEnabled) {
		this.isMailEnabled = isMailEnabled;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsSmsEnabled() {
		return isSmsEnabled;
	}

	public void setIsSmsEnabled(Boolean isSmsEnabled) {
		this.isSmsEnabled = isSmsEnabled;
	}

}
