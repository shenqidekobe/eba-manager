package com.microBusiness.manage.service;

import java.util.Date;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Log;

public interface LogService extends BaseService<Log, Long> {

	void clear();
	
	
	void createLog(String operation, String operator, String content, String parameter, String ip);
	
	
	Page<Log> findPage(String operator,String ip,Date startDate,
			Date endDate,Pageable pageable);

}