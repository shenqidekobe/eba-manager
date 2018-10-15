package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberMember;

public interface MemberMemberService extends BaseService<MemberMember, Long> {

	Page<MemberMember> findPage(Pageable pageable, Member member, Member byMember);
	
	MemberMember save(Member member , String tel , String name);
	
	MemberMember update(MemberMember memberMember , String tel);
	
	/**
	 * 托管账号是否存在
	 * 
	 * @param memberMember
	 * @param byMember
	 * 			店员电话
	 * @return
	 */
	boolean telExists(Member member, Member byMember);
	
	/**
	 * 
	 * @Title: verifyTelExists
	 * @author: yuezhiwei
	 * @date: 2018年3月13日上午10:49:43
	 * @Description: 判断手机号是否存在
	 * @return: boolean
	 */
	MemberMember verifyTelExists(String tel , Member member , MemberMember memberMember);
	
	/**
	 * 
	 * @Title: findByTel
	 * @author: yuezhiwei
	 * @date: 2018年3月30日上午10:10:16
	 * @Description: TODO
	 * @return: MemberMember
	 */
	MemberMember findByTel(Member member , String tel);
}
