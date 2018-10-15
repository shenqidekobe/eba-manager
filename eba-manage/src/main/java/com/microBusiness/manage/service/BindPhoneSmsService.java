package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.BindPhoneSms;

public interface BindPhoneSmsService extends BaseService<BindPhoneSms, Long> {
	
	boolean sendSms(String mobile , String content);
	
	BindPhoneSms findBindPhoneSms(BindPhoneSms bindPhoneSms);
	
	Long countBindPhoneSms(BindPhoneSms bindPhoneSms , boolean isToday);
}
