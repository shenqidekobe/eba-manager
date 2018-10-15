/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Date;
import java.util.Map;

import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;

public interface SmsService extends BaseService<Sms , Long> {

	void send(String[] mobiles, String content, Date sendTime, boolean async);

	void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async);

	void send(String mobile, String content);

	void send(String mobile, String templatePath, Map<String, Object> model);

	void sendRegisterMemberSms(Member member);

	void sendCreateOrderSms(Order order);

	void sendUpdateOrderSms(Order order);

	void sendCancelOrderSms(Order order);

	void sendReviewOrderSms(Order order);

	void sendPaymentOrderSms(Order order);

	void sendRefundsOrderSms(Order order);

	void sendShippingOrderSms(Order order);

	void sendReturnsOrderSms(Order order);

	void sendReceiveOrderSms(Order order);

	void sendCompleteOrderSms(Order order);

	void sendFailOrderSms(Order order);

	long getBalance();

	boolean sendSms(String mobile , String content);

	Sms findSms(Sms sms);

	Long countSms(Sms sms , boolean isToday);

	boolean isExpired(Sms sms , Long expireTime);

	boolean isOver(Long totalCount , Long maxCount);

	boolean sendContent(String mobile , String content);


}