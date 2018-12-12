package com.microBusiness.manage.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.microBusiness.manage.service.StaticService;

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