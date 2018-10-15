/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;

public interface MessageService extends BaseService<Message, Long> {

	Page<Message> findPage(Member member, Pageable pageable);

	Page<Message> findDraftPage(Member sender, Pageable pageable);

	Long count(Member member, Boolean read);

	void delete(Long id, Member member);

}