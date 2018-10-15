package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.dao.OrderShareLogDao;
import com.microBusiness.manage.entity.OrderShareLog;
import com.microBusiness.manage.service.OrderShareLogService;

@Service
public class OrderShareLogServiceImpl extends BaseServiceImpl<OrderShareLog, Long> implements OrderShareLogService {

	@Resource
	private OrderShareLogDao orderShareLogDao;
}
