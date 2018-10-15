/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage;

import org.apache.shiro.authc.UsernamePasswordToken;

public class AuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5898441540965086534L;

	private String captchaId;

	private String captcha;

	public AuthenticationToken(String username, String password, String captchaId, String captcha, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.captchaId = captchaId;
		this.captcha = captcha;
	}

	public String getCaptchaId() {
		return captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}