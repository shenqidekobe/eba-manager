package com.microBusiness.manage.service;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;

public interface WithdrawService extends BaseService<Withdraw, Long> {

	List<Withdraw> query(ChildMember member);
	
	Page<Withdraw> findPage(Withdraw_Status status,ChildMember member,Date startDate,
			Date endDate,String timeSearch,Pageable pageable);
}