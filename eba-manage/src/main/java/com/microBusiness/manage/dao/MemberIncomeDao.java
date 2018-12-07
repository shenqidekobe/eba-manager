package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.MemberIncome;

public interface MemberIncomeDao extends BaseDao<MemberIncome, Long> {

	public List<MemberIncome> query(ChildMember member);
	
	public Page<MemberIncome> findPage(String type, ChildMember member,Date startDate,
			Date endDate,Pageable pageable);
}