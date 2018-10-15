/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.dao.NewMessageCompamyDao;
import com.microBusiness.manage.dao.NewMessageDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessage;
import com.microBusiness.manage.entity.NewMessageCompamy;
import com.microBusiness.manage.service.NewMessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("newMessageServiceImpl")
public class NewMessageServiceImpl extends BaseServiceImpl<NewMessage, Long> implements NewMessageService {

	@Resource(name = "newMessageDaoImpl")
	private NewMessageDao newMessageDao;
	
	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "newMessageCompamyDaoImpl")
	private NewMessageCompamyDao newMessageCompamyDao;
	
	@Transactional
	@Override
	public Boolean send(Admin adminOld, NewMessage newMessageOld, List<Admin> adminList, HttpServletRequest request) {
		NewMessage newMessage = this.save(newMessageOld);
		if (newMessage != null) {
			try {
				for (Admin admin : adminList) {
					if (admin.getId() == adminOld.getId()) {
						continue;
					}
					NewMessageCompamy newMessageCompamy = new NewMessageCompamy();
					newMessageCompamy.setSender(adminOld);
					newMessageCompamy.setNewMessage(newMessage);
					newMessageCompamy.setSenderIp(request.getRemoteAddr());
					newMessageCompamy.setReceiver(admin);
					newMessageCompamy.setReceiverRead(false);
					newMessageCompamy.setSenderDelete(false);
					newMessageCompamy.setReceiverDelete(false);
					newMessageCompamyDao.persist(newMessageCompamy);
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public Page<NewMessage> query(NewMessage newMessage, Pageable pageable) {
		return newMessageDao.query(newMessage, pageable);
	}
	

}