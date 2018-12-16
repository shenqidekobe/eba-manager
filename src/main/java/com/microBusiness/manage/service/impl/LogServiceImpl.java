package com.microBusiness.manage.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.LogDao;
import com.microBusiness.manage.entity.Log;
import com.microBusiness.manage.service.LogService;

@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

	@Resource(name = "logDaoImpl")
	private LogDao logDao;

	public void clear() {
		logDao.removeAll();
	}
	
	@Override
	public void createLog(String operation, String operator, String content, String parameter, String ip) {
		Log log = new Log();
		log.setOperation(operation);
		log.setOperator(operator);
		log.setContent(content);
		log.setParameter(parameter);		
		log.setIp(ip);
		logDao.persist(log);
	}
	
	
	@Override
	public Page<Log> findPage(String operator,String ip,Date startDate,
			Date endDate,Pageable pageable){
		return logDao.findPage(operator, ip, startDate, endDate, pageable);
	}

}