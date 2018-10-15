/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.service.CaptchaService;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.octo.captcha.service.CaptchaServiceException;

@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;

	public BufferedImage buildImage(String captchaId) {
		Assert.hasText(captchaId);

		return (BufferedImage) imageCaptchaService.getChallengeForID(captchaId);
	}

	public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha) {
		Assert.notNull(captchaType);

		Setting setting = SystemUtils.getSetting();
		if (!ArrayUtils.contains(setting.getCaptchaTypes(), captchaType)) {
			return true;
		}
		if (StringUtils.isEmpty(captchaId) || StringUtils.isEmpty(captcha)) {
			return false;
		}
		try {
			return imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
		} catch (CaptchaServiceException e) {
			return false;
		}
	}

}