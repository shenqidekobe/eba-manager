package com.microBusiness.manage.dao;

import java.util.Date;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Log;

public interface LogDao extends BaseDao<Log, Long> {

	void removeAll();
	
	Page<Log> findPage(String operator,String ip,Date startDate,
			Date endDate,Pageable pageable);

}