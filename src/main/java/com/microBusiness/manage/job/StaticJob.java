/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.job;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.service.StaticService;

import com.microBusiness.manage.entity.Article;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component("staticJob")
public class StaticJob {

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	//@Scheduled(fixedDelayString = "${job.static_generate_other.delay}")
	public void generateOther() {
		staticService.generateOther();
	}

}