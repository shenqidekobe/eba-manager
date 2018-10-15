package com.microBusiness.manage.job;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import com.microBusiness.manage.service.ass.AssFormService;

public class FormIdJob {
	@Resource(name = "assFormServiceImpl")
	private AssFormService assFormService;
	
	@Scheduled(cron = "${job.static_generate_lazy_formId.delay}")
	public void clearExpired() {
		assFormService.clearExpired();
	}
}
