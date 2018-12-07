/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.WithdrawDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;
import com.microBusiness.manage.service.WithdrawService;

@Service("withdrawServiceImpl")
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw, Long> implements WithdrawService {

	@Resource(name = "withdrawDaoImpl")
	private WithdrawDao withdrawDao;
	
	@Override
	@Transactional
	public Withdraw save(Withdraw Withdraw) {
		return super.save(Withdraw);
	}

	@Override
	@Transactional
	public Withdraw update(Withdraw Withdraw) {
		return super.update(Withdraw);
	}

	@Override
	@Transactional
	public Withdraw update(Withdraw Withdraw, String... ignoreProperties) {
		return super.update(Withdraw, ignoreProperties);
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
	public void delete(Withdraw Withdraw) {
		super.delete(Withdraw);
	}

	@Override
	public List<Withdraw> query(ChildMember m) {
		return withdrawDao.query(m);
	}
	
	@Override
	public Page<Withdraw> findPage(Withdraw_Status status,ChildMember member,Date startDate,
			Date endDate,String timeSearch,Pageable pageable){
		return withdrawDao.findPage(status, member, startDate, endDate, timeSearch, pageable);
	}

}