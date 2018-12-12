package com.microBusiness.manage.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microBusiness.manage.service.CartService;

@Lazy(false)
@Component("cartJob")
public class CartJob {
	
	 public final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		logger.info("购物车超时清空任务执行........");
		cartService.evictExpired();
	}

}