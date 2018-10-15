/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.Map;

import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ProductNotify;
import com.microBusiness.manage.entity.SafeKey;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ProductNotify;
import com.microBusiness.manage.entity.SafeKey;

public interface MailService {

	void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content, boolean async);

	void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async);

	void send(String[] toMails, String subject, String content, boolean async);

	void send(String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async);

	void send(String toMail, String subject, String content);

	void send(String toMail, String subject, String templatePath, Map<String, Object> model);

	void sendTestSmtpMail(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String toMail);

	void sendFindPasswordMail(String toMail, String username, SafeKey safeKey);

	void sendProductNotifyMail(ProductNotify productNotify);

	void sendRegisterMemberMail(Member member);

	void sendCreateOrderMail(Order order);

	void sendUpdateOrderMail(Order order);

	void sendCancelOrderMail(Order order);

	void sendReviewOrderMail(Order order);

	void sendPaymentOrderMail(Order order);

	void sendRefundsOrderMail(Order order);

	void sendShippingOrderMail(Order order);

	void sendReturnsOrderMail(Order order);

	void sendReceiveOrderMail(Order order);

	void sendCompleteOrderMail(Order order);

	void sendFailOrderMail(Order order);

}