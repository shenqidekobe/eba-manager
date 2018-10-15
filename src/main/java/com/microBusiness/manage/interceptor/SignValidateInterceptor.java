package com.microBusiness.manage.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.OutApiJsonEntity;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.exception.OutApiException;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.Code;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by afei. User: mingbai Date: 2018/1/30 下午2:03 Describe: Update:
 */
public class SignValidateInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(SignValidateInterceptor.class);

	private static final Long EXPIRE_TIME = 10L * 60L * 1000L;

	@Resource
	private SupplierService supplierService;

	/**
	 * This implementation always returns {@code true}.
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		/*
		 * if(true){ 如果有异常的话，异常会在BaseController中进行捕获 ， 这里可以不使用response进行处理 throw
		 * new RuntimeException(); }
		 */

//		PrintWriter out = null;

		// 获取请求参数
		Map<String, String[]> params = new HashMap<>();
		params.putAll(request.getParameterMap());

		// 在拦截器中进行json返回 , 配置response 头，编码和返回格式
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("application/json; charset=utf-8");

		// 对参数进行校验
		String appId = request.getParameter("appId");
		String timeInMillis = request.getParameter("timeInMillis");
		String sign = request.getParameter("sign");

		LOGGER.info("SignValidateInterceptor 参数校验appId=" + appId + "; timeInMillis=" + timeInMillis + "; sign=" + sign);

		if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(timeInMillis) || StringUtils.isEmpty(sign)) {
			throw new OutApiException(OutApiJsonEntity.error(Code.code100004));
		}

		// 校验这次请求的时间是否超过范围，api文档中规定为10分钟内的请求
		Long millis = System.currentTimeMillis();
		Long timeInMillisLong = Long.parseLong(timeInMillis);
		// 请求为 EXPIRE_TIME 之前的请求
		if (millis - timeInMillisLong > EXPIRE_TIME) {
			throw new OutApiException(OutApiJsonEntity.error(Code.code100006));
		}

		// 校验 appId 是否存在 ， appId 存放在 企业表中
		Supplier supplier = supplierService.findByAppId(appId);
		if (supplier == null) {
			throw new OutApiException(OutApiJsonEntity.error(Code.code100000));
		}
		request.setAttribute("supplier", supplier);

		// 校验sign是否合法

		// 参数校验完成后，是否有需要将不加密的参数剔除,有则剔除
		params.remove("timeInMillis");
		params.remove("sign");

		// 对key进行排序
		Set<String> keys = params.keySet();
		String[] keysArr = new String[keys.size()];
		keys.toArray(keysArr);
		Arrays.sort(keysArr);

		StringBuffer paramsSb = new StringBuffer();
		for (String key : keysArr) {
			paramsSb.append(key).append(request.getParameter(key));
			
			LOGGER.info("SignValidateInterceptor 参数校验key="+key+"; value="+request.getParameter(key));
		}

		// 在参数拼接最后加上 密钥和时间戳
		paramsSb.append(supplier.getAppKey()).append(timeInMillis);

		LOGGER.info("SignValidateInterceptor MD5加密前字符串：paramsSb="+paramsSb);
		
		// md5加密
		String ownSign = DigestUtils.md5Hex(paramsSb.toString()).toUpperCase();

		LOGGER.info("SignValidateInterceptor MD5加密后字符串：ownSign = {}" , ownSign);
		LOGGER.info("out SignValidateInterceptor MD5加密后字符串：sign = {}" ,sign);
		
		// sign进行校验
		if (!ownSign.equals(sign)) {
			// 加密串错误
			throw new OutApiException(OutApiJsonEntity.error(Code.code100002));
		}

		return true;
	}

}
