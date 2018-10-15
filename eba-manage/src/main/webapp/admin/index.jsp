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
		response.sendRedirect(base + "/admin/login.jhtml?redirectUrl=" + redirectUrl);
		return;
	}
	response.sendRedirect(base + "/admin/login.jhtml");
	return;
} else {
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>提示信息 - Powered By UTLZ</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="<%=base%>/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=base%>/resources/admin/css/login.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<fieldset>
		<legend>系统出现异常</legend>
	</fieldset>
</body>
</html>
<%
}
%>