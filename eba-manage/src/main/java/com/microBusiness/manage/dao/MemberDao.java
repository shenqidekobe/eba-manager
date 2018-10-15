/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao;

import java.util.Date;
import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberAttribute;

public interface MemberDao extends BaseDao<Member, Long> {

	boolean usernameExists(String username);

	boolean emailExists(String email);

	Member find(String loginPluginId, String openId);

	Member findByUsername(String username);

	List<Member> findListByEmail(String email);

	Page<Member> findPage(Member.RankingType rankingType, Pageable pageable);

	Long registerMemberCount(Date beginDate, Date endDate);

	void clearAttributeValue(MemberAttribute memberAttribute);

	Member findByOpenId(String openId);

	Member findByMobile(String mobile);

	/**
	 *
	 * @param startRow
	 * @param offset
	 * @param compareDate
	 * @return
	 */

	List<Object[]> getMemberToNotice(int startRow , int offset , Date compareDate);

}