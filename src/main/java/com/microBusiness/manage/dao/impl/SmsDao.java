package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.Sms;

/**
 * Created by mingbai on 2017/2/7.
 * 功能描述：
 * 修改记录：
 */
public interface SmsDao extends BaseDao<Sms, Long> {
    Sms findSms(Sms sms);

    Long countSms(Sms sms , boolean isToday);

}
