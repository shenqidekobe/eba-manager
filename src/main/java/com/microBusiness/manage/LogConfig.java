/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage;

import java.io.Serializable;

public class LogConfig implements Serializable {

	private static final long serialVersionUID = -1108848647938408402L;

	public static final String CACHE_NAME = "logConfig";

	private String operation;

	private String urlPattern;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

}