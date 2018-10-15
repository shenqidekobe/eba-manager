/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.job;

import javax.annotation.Resource;

import com.microBusiness.manage.service.CartService;

import com.microBusiness.manage.service.CartService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Lazy(false)
//@Component("cartJob")
public class CartJob {

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		cartService.evictExpired();
	}

}