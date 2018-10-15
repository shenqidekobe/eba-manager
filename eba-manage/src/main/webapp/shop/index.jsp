<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.microBusiness.manage.entity.Admin"%>
<%@page import="com.microBusiness.manage.service.AdminService"%>
<%@page import="com.microBusiness.manage.util.SpringUtils"%>
<%@page import="com.microBusiness.manage.util.WebUtils"%>
<%
String base = request.getContextPath();
String redirectUrl = request.getParameter("redirectUrl");
ApplicationContext applicationContext = SpringUtils.getApplicationContext();
if (applicationContext != null) {
	AdminService adminService = SpringUtils.getBean("adminServiceImpl", AdminService.class);
	WebUtils.addCookie(request, response, Admin.LOGIN_TOKEN_COOKIE_NAME, adminService.getLoginToken());
	if(redirectUrl != null && redirectUrl != ""){
		response.sendRedirect(base + "/shop/login/index.jhtml?redirectUrl=" + java.net.URLEncoder.encode(redirectUrl,"utf-8"));
		return;
	}
	response.sendRedirect(base + "/shop/login/index.jhtml");
	return;
} else {
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0);
%>

<%
}
%>