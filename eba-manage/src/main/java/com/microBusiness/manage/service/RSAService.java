/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;

public interface RSAService {

	RSAPublicKey generateKey(HttpServletRequest request);

	void removePrivateKey(HttpServletRequest request);

	String decryptParameter(String name, HttpServletRequest request);

}