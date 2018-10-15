package com.microBusiness.manage.service.impl;

import java.net.URLEncoder;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.BindPhoneSmsDao;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.util.SmsClient;

@Service("bindPhoneSmsServiceImpl")
public class BindPhoneSmsServiceImpl extends BaseServiceImpl<BindPhoneSms, Long> implements BindPhoneSmsService {
	
	@Resource
	private BindPhoneSmsDao bindPhoneSmsDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean sendSms(String mobile, String content) {
		try {
			SmsClient client = new SmsClient();
			//String result_mt = client.mdsmssend(mobile, URLEncoder.encode(content , "utf-8"), "", "", "", "");
			logger.info("sms/sendSms:phone="+mobile+"&content"+content+"&result_mt"+"");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
	}

	@Override
	public BindPhoneSms findBindPhoneSms(BindPhoneSms bindPhoneSms) {
		return bindPhoneSmsDao.findBindPhoneSms(bindPhoneSms);
	}

	@Override
	public Long countBindPhoneSms(BindPhoneSms bindPhoneSms, boolean isToday) {
		return bindPhoneSmsDao.countBindPhoneSms(bindPhoneSms, isToday);
	}

}
