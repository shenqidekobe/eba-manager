package com.microBusiness.manage.interceptor;

import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.microBusiness.manage.Principal;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.AdminService;

public class ShopMemberInterceptor extends HandlerInterceptorAdapter {

	private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";

	private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

	private static final String MEMBER_ATTRIBUTE_NAME = "member";

	private static final String DEFAULT_LOGIN_URL = "/shop";

	private String loginUrl = DEFAULT_LOGIN_URL;

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;
	@Resource
	private AdminService adminService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	//	Admin admin = (Admin)request.getSession().getAttribute("adminSession");
		Admin admin = adminService.getCurrent();
		if (admin != null) {
			return true;
		} else {
			String requestType = request.getHeader("X-Requested-With");
			if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
				//response.addHeader("loginStatus", "accessDenied");
				//response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return true;
			} else {
				if (request.getMethod().equalsIgnoreCase("GET")) {
					String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
					response.sendRedirect(request.getContextPath() + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset));
				} else {
					response.sendRedirect(request.getContextPath() + loginUrl);
				}
				return false;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (!StringUtils.startsWith(viewName, REDIRECT_VIEW_NAME_PREFIX)) {
				Admin admin = adminService.getCurrent();
				modelAndView.addObject(MEMBER_ATTRIBUTE_NAME, admin);
			}
		}
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}