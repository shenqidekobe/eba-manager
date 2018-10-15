/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.awt.image.BufferedImage;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.Setting;

public interface CaptchaService {

	BufferedImage buildImage(String captchaId);

	boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha);

	
}