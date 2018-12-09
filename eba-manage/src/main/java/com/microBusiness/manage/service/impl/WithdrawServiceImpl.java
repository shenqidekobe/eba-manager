/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.WithdrawDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;
import com.microBusiness.manage.service.WithdrawService;
import com.microBusiness.manage.util.DateUtils;

@Service("withdrawServiceImpl")
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw, Long> implements WithdrawService {

	@Resource(name = "withdrawDaoImpl")
	private WithdrawDao withdrawDao;
	@Resource
	private MemberDao memberDao;
	
	@Override
	@Transactional
	public Withdraw createWithdraw(Withdraw obj){
		//扣减余额
		Member member1 = obj.getMember().getMember();
		member1.setBalance(member1.getBalance().subtract(obj.getAmount()));
		memberDao.persist(member1);
		
		obj.setCreateDate(new Date());
		obj.setSn(obj.getMember().getId()+DateUtils.convertToString(new Date(), DateUtils.DEFAULT_TIMENO_FORMAT));
		obj.setFee(BigDecimal.ZERO);
		obj.setStatus(Withdraw.Withdraw_Status.await);
		withdrawDao.persist(obj);
		
		return obj;
	}
	
	@Override
	@Transactional
	public Withdraw auditWithdraw(Withdraw obj){
		if(!Withdraw_Status.complete.equals(obj.getStatus())
				&&!Withdraw_Status.await.equals(obj.getStatus())){
			//提现失败退回余额给会员账户
			Member member1 = obj.getMember().getMember();
			member1.setBalance(member1.getBalance().add(obj.getAmount()));
			memberDao.persist(member1);
		}
		obj.setProcessTime(new Date());
		withdrawDao.persist(obj);
		return obj;
	}
	
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