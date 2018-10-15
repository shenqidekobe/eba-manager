/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ProxyCheckDao;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.entity.ProxyCheck;
import com.microBusiness.manage.entity.ProxyCheckStatus;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.service.ProxyCheckService;
import com.microBusiness.manage.service.ProxyUserService;

@Service("proxyCheckServiceImpl")
public class ProxyCheckServiceImpl extends BaseServiceImpl<ProxyCheck, Long> implements ProxyCheckService {

	@Resource(name = "proxyCheckDaoImpl")
	private ProxyCheckDao proxyCheckDao;
	@Resource
	private ProxyUserService proxyUserService;
	@Resource
	private ReceiverDao receiverDao;
	
	@Override
	@Transactional
	public ProxyCheck save(ProxyCheck proxyCheck) {
		return super.save(proxyCheck);
	}

	@Override
	@Transactional
	public ProxyCheck update(ProxyCheck proxyCheck) {
		return super.update(proxyCheck);
	}

	@Override
	@Transactional
	public ProxyCheck update(ProxyCheck proxyCheck, String... ignoreProperties) {
		return super.update(proxyCheck);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(ProxyCheck proxyCheck) {
		super.delete(proxyCheck);
	}

	@Override
	@Transactional
	public ProxyCheck check(ProxyCheck proxyCheck) {
		proxyCheck.setProxyCheckStatus(ProxyCheckStatus.finish);
		super.update(proxyCheck);
		
		ProxyUser proxyUser = new ProxyUser();
		proxyUser.setSupplier(proxyCheck.getSupplier());
		proxyUser.setParent(proxyCheck.getParentProxyUser());
		proxyUser.setName(proxyCheck.getName());
		proxyUser.setAddress(proxyCheck.getAddress());
		if(StringUtils.isNotEmpty(proxyCheck.getAddress())){
			Receiver receiver = new Receiver();
			receiver.setAddress(proxyCheck.getAddress());
			receiver.setArea(proxyCheck.getArea());
			receiver.setAreaName(proxyCheck.getArea().getFullName());
			receiver.setConsignee(proxyCheck.getName());
			receiver.setIsDefault(true);
			receiver.setSupplier(proxyCheck.getSupplier());
			receiver.setMember(proxyCheck.getChildMember().getMember());
			receiver.setPhone(proxyCheck.getTel());
			receiver.setZipCode("");
			receiverDao.persist(receiver);
		}
		proxyUser.setArea(proxyCheck.getArea());
		proxyUser.setChildMember(proxyCheck.getChildMember());
		proxyUser.setCreateDate(new Date());
		proxyUser.setGender(proxyCheck.getGender());
		proxyUser.setDeleted(false);
		if(proxyCheck.getParentProxyUser() != null){
			proxyUser.setSupplier(proxyCheck.getParentProxyUser().getSupplier());
		}
		proxyUser.setIdenNo(proxyCheck.getIdenNo());
		proxyUser.setProxyJoinType(proxyCheck.getProxyJoinType());
		proxyUser.setTel(proxyCheck.getTel());
		proxyUser.setWebchat(proxyCheck.getWebchat());
		proxyUserService.save(proxyUser);
		return proxyCheck;
	}
	
	@Override
	@Transactional
	public Page<ProxyCheck> findPage(Pageable pageable, String dateType, Date startDate, Date endDate){
		return proxyCheckDao.findPage(pageable,  dateType,  startDate,  endDate);
	}


}