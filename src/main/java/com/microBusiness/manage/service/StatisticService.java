/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.entity.Statistic;

public interface StatisticService extends BaseService<Statistic, Long> {

	boolean exists(int year, int month, int day);

	Statistic collect(int year, int month, int day);

	List<Statistic> analyze(Statistic.Period period, Date beginDate, Date endDate);

}