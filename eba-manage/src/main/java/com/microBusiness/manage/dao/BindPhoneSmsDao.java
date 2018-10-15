package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.BindPhoneSms;

public interface BindPhoneSmsDao extends BaseDao<BindPhoneSms, Long> {

	BindPhoneSms findBindPhoneSms(BindPhoneSms bindPhoneSms);
	
	Long countBindPhoneSms(BindPhoneSms bindPhoneSms , boolean isToday);
}
