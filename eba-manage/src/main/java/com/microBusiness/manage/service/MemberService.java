/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.PointLog;

public interface MemberService extends BaseService<Member, Long> {

	boolean usernameExists(String username);

	boolean usernameDisabled(String username);

	boolean emailExists(String email);

	boolean emailUnique(String previousEmail, String currentEmail);

	Member find(String loginPluginId, String openId);

	Member findByUsername(String username);

	List<Member> findListByEmail(String email);

	Page<Member> findPage(Member.RankingType rankingType, Pageable pageable);

	boolean isAuthenticated();

	Member getCurrent();

	Member getCurrent(boolean lock);

	String getCurrentUsername();

	void addBalance(Member member, BigDecimal amount, DepositLog.Type type, Admin operator, String memo);

	void addPoint(Member member, long amount, PointLog.Type type, Admin operator, String memo);

	void addAmount(Member member, BigDecimal amount);

	Member findByOpenId(String openId);

	Member findByMobile(String mobile);

	/**
	 * 这种写法比较别扭，应该有更好的办法
	 * @param startRow
	 * @param offset
	 * @param compareDate
	 * @return
	 */
	List<Object[]> getMemberToNotice(int startRow , int offset ,  Date compareDate);
	
	Member addMember(String tel);

	void refreshMember(Member member);
}