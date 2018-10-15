/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.service.ReceiverService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("receiverServiceImpl")
public class ReceiverServiceImpl extends BaseServiceImpl<Receiver, Long> implements ReceiverService {

	@Resource(name = "receiverDaoImpl")
	private ReceiverDao receiverDao;

	@Transactional(readOnly = true)
	public Receiver findDefault(Member member) {
		return receiverDao.findDefault(member);
	}

	@Transactional(readOnly = true)
	public Page<Receiver> findPage(Member member, Pageable pageable) {
		return receiverDao.findPage(member, pageable);
	}

	@Override
	@Transactional
	public Receiver save(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.save(receiver);
	}

	@Override
	@Transactional
	public Receiver update(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.update(receiver);
	}

	@Override
	public List<Receiver> find(Member member, boolean isDefault) {
		return receiverDao.find(member, isDefault);
	}

	@Override
	public Receiver find(Member member, Supplier supplier) {
		return receiverDao.find(member, supplier);
	}

	@Override
	public List<Receiver> findList(Member member, Supplier supplier) {
		return receiverDao.findList(member, supplier);
	}

	@Override
	public Receiver findDefault(Member member, Supplier supplier) {
		Assert.notNull(member);
		Assert.notNull(supplier);
		List<Receiver> receivers = receiverDao.findList(member, supplier);
		Receiver receiver = null;
		if(CollectionUtils.isNotEmpty(receivers)) {
			for(Receiver rece : receivers) {
				if(rece.getIsDefault() == true) {
					receiver = rece;
					break;
				}
			}
			if(null == receiver) {
				for(Receiver rece : receivers) {
					if(rece.getMember()  != null && rece.getSupplier() != null) {
						receiver = rece;
						break;
					}
				}
			}
			if(null == receiver) {
				receiver = receivers.get(0);
			}
		}
		return receiver;
	}

}