/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.security.interfaces.RSAPublicKey;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.RSAService;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.RSAService;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminLoginController")
@RequestMapping("/admin/login")
public class LoginController extends BaseController {

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping
	public String index(HttpServletRequest request, ModelMap model) {
		String redirectUrl = request.getParameter("redirectUrl");
		model.addAttribute("redirectUrl", redirectUrl);
		request.getSession().setAttribute("redirectUrl", redirectUrl);
		String loginToken = WebUtils.getCookie(request, Admin.LOGIN_TOKEN_COOKIE_NAME);
		if (!StringUtils.equalsIgnoreCase(loginToken, adminService.getLoginToken())) {
			return "redirect:/";
		}
		if (adminService.isAuthenticated()) {
			/*//如果是手机端，则跳转到 ／homePage/index.jhtml
			if(DeviceUtils.getCurrentDevice(request).isMobile()){
				return "redirect:homePage/index.jhtml";
			}*/
			return "redirect:common/deviceOauth.jhtml";
		}
		Message failureMessage = null;
		String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (StringUtils.isNotEmpty(loginFailure)) {
			if (loginFailure.equals("com.microBusiness.manage.exception.IncorrectCaptchaException")) {
				failureMessage = Message.error("admin.captcha.invalid");
			} else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
				failureMessage = Message.error("admin.login.unknownAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
				failureMessage = Message.error("admin.login.disabledAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
				failureMessage = Message.error("admin.login.lockedAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
				Setting setting = SystemUtils.getSetting();
				if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.admin)) {
					failureMessage = Message.error("admin.login.accountLockCount", setting.getAccountLockCount());
				} else {
					failureMessage = Message.error("admin.login.incorrectCredentials");
				}
			} else if (loginFailure.equals("com.microBusiness.manage.exception.IncorrectLicenseException")) {
				failureMessage = Message.error("admin.login.incorrectLicense");
			} else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
				failureMessage = Message.error("admin.login.authentication");
			}
		}
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("failureMessage", failureMessage);
		if(StringUtils.isNotBlank(redirectUrl)){
			return "/shop/member/login/index";
		}
		return "/admin/login/index";
	}

}