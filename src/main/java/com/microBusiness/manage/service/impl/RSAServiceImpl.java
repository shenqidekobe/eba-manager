/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.service.RSAService;
import com.microBusiness.manage.util.RSAUtils;

import com.microBusiness.manage.service.RSAService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {

	private static final int KEY_SIZE = 1024;

	private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

	@Transactional(readOnly = true)
	public RSAPublicKey generateKey(HttpServletRequest request) {
		Assert.notNull(request);

		KeyPair keyPair = RSAUtils.generateKeyPair(KEY_SIZE);
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		request.getSession().setAttribute(PRIVATE_KEY_ATTRIBUTE_NAME, privateKey);
		return publicKey;
	}

	@Transactional(readOnly = true)
	public void removePrivateKey(HttpServletRequest request) {
		Assert.notNull(request);

		request.getSession().removeAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
	}

	@Transactional(readOnly = true)
	public String decryptParameter(String name, HttpServletRequest request) {
		Assert.notNull(request);

		if (StringUtils.isEmpty(name)) {
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey) request.getSession().getAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
		String parameter = request.getParameter(name);
		if (privateKey != null && StringUtils.isNotEmpty(parameter)) {
			try {
				return new String(RSAUtils.decrypt(privateKey, Base64.decodeBase64(parameter)));
			} catch (RuntimeException e) {
				return null;
			}
		}
		return null;
	}

}