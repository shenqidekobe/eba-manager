/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class SafeKey implements Serializable {

	private static final long serialVersionUID = -3601147070361996939L;

	private String value;

	private Date expire;

	@Column(name = "safe_key_value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "safe_key_expire")
	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	@Transient
	public boolean hasExpired() {
		return getExpire() != null && !getExpire().after(new Date());
	}

}
