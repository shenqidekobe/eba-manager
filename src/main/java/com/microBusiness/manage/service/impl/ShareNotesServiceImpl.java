package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ShareNotesDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ShareNotes;
import com.microBusiness.manage.service.ShareNotesService;

@Service
public class ShareNotesServiceImpl extends BaseServiceImpl<ShareNotes, Long> implements ShareNotesService {

	@Resource
	private ShareNotesDao shareNotesDao;

	@Override
	public Page<ShareNotes> findPage(Pageable pageable, Order order,
			Long shareNotesId) {
		return shareNotesDao.findPage(pageable, order, shareNotesId);
	}
}
