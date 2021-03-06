package com.microBusiness.manage.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.microBusiness.manage.AuthenticationToken;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.RSAService;

public class AuthenticationFilter extends FormAuthenticationFilter {

	private static final String DEFAULT_EN_PASSWORD_PARAM = "enPassword";

	private static final String DEFAULT_CAPTCHA_ID_PARAM = "captchaId";

	private static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String enPasswordParam = DEFAULT_EN_PASSWORD_PARAM;

	private String captchaIdParam = DEFAULT_CAPTCHA_ID_PARAM;

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Override
	protected org.apache.shiro.authc.AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
		String username = getUsername(servletRequest);
		String password = getPassword(servletRequest);
		String captchaId = getCaptchaId(servletRequest);
		String captcha = getCaptcha(servletRequest);
		boolean rememberMe = isRememberMe(servletRequest);
		String host = getHost(servletRequest);
		String redirectUrl = servletRequest.getParameter("redirectUrl");
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		request.getSession().setAttribute("redirectUrl", redirectUrl);
		return new AuthenticationToken(username, password, captchaId, captcha, rememberMe, host);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest")) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		/*String loginToken = com.microBusiness.manage.util.WebUtils.getCookie(request, Admin.LOGIN_TOKEN_COOKIE_NAME);
		if (!StringUtils.equalsIgnoreCase(loginToken, adminService.getLoginToken())) {
			WebUtils.issueRedirect(request, response, "/");
			return false;
		}*/
		return super.onAccessDenied(request, response);
	}

	@Override
	protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		Session session = subject.getSession();
		Map<Object, Object> attributes = new HashMap<Object, Object>();
		Collection<Object> keys = session.getAttributeKeys();
		for (Object key : keys) {
			attributes.put(key, session.getAttribute(key));
		}
		session.stop();
		session = subject.getSession();
		for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		//重写登陆成功后跳转的页面，这里不使用之前保存的地址，直接跳转到成功页面
		WebUtils.issueRedirect(servletRequest, servletResponse, super.getSuccessUrl(), null, true);

		return false;
	}

	@Override
	protected String getPassword(ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String password = rsaService.decryptParameter(enPasswordParam, request);
		rsaService.removePrivateKey(request);
		return password;
	}

	protected String getCaptchaId(ServletRequest servletRequest) {
		String captchaId = WebUtils.getCleanParam(servletRequest, captchaIdParam);
		if (captchaId == null) {
			captchaId = ((HttpServletRequest) servletRequest).getSession().getId();
		}
		return captchaId;
	}

	protected String getCaptcha(ServletRequest servletRequest) {
		return WebUtils.getCleanParam(servletRequest, captchaParam);
	}

	public String getEnPasswordParam() {
		return enPasswordParam;
	}

	public void setEnPasswordParam(String enPasswordParam) {
		this.enPasswordParam = enPasswordParam;
	}

	public String getCaptchaIdParam() {
		return captchaIdParam;
	}

	public void setCaptchaIdParam(String captchaIdParam) {
		this.captchaIdParam = captchaIdParam;
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

}