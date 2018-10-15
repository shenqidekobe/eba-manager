/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MessageDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Message;
import com.microBusiness.manage.service.MessageService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Message;
import com.microBusiness.manage.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl<Message, Long> implements MessageService {

	@Resource(name = "messageDaoImpl")
	private MessageDao messageDao;

	@Transactional(readOnly = true)
	public Page<Message> findPage(Member member, Pageable pageable) {
		return messageDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Message> findDraftPage(Member sender, Pageable pageable) {
		return messageDao.findDraftPage(sender, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Boolean read) {
		return messageDao.count(member, read);
	}

	public void delete(Long id, Member member) {
		messageDao.remove(id, member);
	}

}