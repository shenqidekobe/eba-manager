package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.MemberIncome;

public interface MemberIncomeService extends BaseService<MemberIncome, Long> {

	List<MemberIncome> query(ChildMember member);
	
	Page<MemberIncome> findPage(String type,ChildMember member,Date startDate,
			Date endDate,Pageable pageable);
}