/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.util.WebUtils;

import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.util.WebUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CartQuantityInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		WebUtils.removeCookie(request, response, Cart.QUANTITY_COOKIE_NAME);
		return super.preHandle(request, response, handler);
	}

}