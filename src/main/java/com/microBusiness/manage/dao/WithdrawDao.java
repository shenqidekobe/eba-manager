package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;

public interface WithdrawDao extends BaseDao<Withdraw, Long> {

	public List<Withdraw> query(ChildMember member);
	
	public Page<Withdraw> findPage(Withdraw_Status status, ChildMember member,Date startDate,
			Date endDate,String timeSearch,Pageable pageable);
}