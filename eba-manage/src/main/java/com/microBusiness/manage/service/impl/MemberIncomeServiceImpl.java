package com.microBusiness.manage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberIncomeDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.MemberIncome;
import com.microBusiness.manage.service.MemberIncomeService;

@Service("memberIncomeServiceImpl")
public class MemberIncomeServiceImpl extends BaseServiceImpl<MemberIncome, Long> implements MemberIncomeService {

	@Resource(name = "memberIncomeDaoImpl")
	private MemberIncomeDao memberIncomeDao;
	
	@Override
	@Transactional
	public MemberIncome save(MemberIncome MemberIncome) {
		return super.save(MemberIncome);
	}

	@Override
	@Transactional
	public MemberIncome update(MemberIncome MemberIncome) {
		return super.update(MemberIncome);
	}

	@Override
	@Transactional
	public MemberIncome update(MemberIncome MemberIncome, String... ignoreProperties) {
		return super.update(MemberIncome, ignoreProperties);
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
	public void delete(MemberIncome MemberIncome) {
		super.delete(MemberIncome);
	}

	@Override
	public List<MemberIncome> query(ChildMember member) {
		return memberIncomeDao.query(member);
	}
	
	
	@Override
	public Page<MemberIncome> findPage(String type,ChildMember member,Date startDate,
			Date endDate,Pageable pageable){
		return memberIncomeDao.findPage(type, member, startDate, endDate, pageable);
				
	}

}