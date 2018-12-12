package com.microBusiness.manage.controller.shop;

import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.RSAService;

@Controller("shopLoginController")
@RequestMapping("/shop/login")
public class LoginController extends BaseController{
	
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,String redirectUrl, ModelMap model, Pageable pageable) {
//		String redirectUrl = request.getParameter("redirectUrl");
		model.addAttribute("redirectUrl", redirectUrl);
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/member/login/index";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, ModelMap model) {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
		//String redirectUrl = request.getParameter("redirectUrl");
		//model.addAttribute("redirectUrl", redirectUrl);
		//if(StringUtils.isNotBlank(redirectUrl)){
		//	return "redirect:" + redirectUrl;
		//}
		return "redirect:/shop/index.jhtml";
	}
}
