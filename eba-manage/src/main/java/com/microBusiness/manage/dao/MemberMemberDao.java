package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberMember;

public interface MemberMemberDao extends BaseDao<MemberMember, Long> {

	Page<MemberMember> findPage(Pageable pageable, Member member, Member byMember);
	
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
	 * @date: 2018年3月13日上午10:50:27
	 * @Description: 判断手机号是否存在
	 * @return: boolean
	 */
	MemberMember verifyTelExists(String tel , Member member , MemberMember memberMember);
	
	/**
	 * 
	 * @Title: find
	 * @author: yuezhiwei
	 * @date: 2018年3月14日下午2:10:10
	 * @Description: TODO
	 * @return: MemberMember
	 */
	MemberMember find(Member member, Member byMember);
	
	/**
	 * 
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年3月14日下午5:05:02
	 * @Description: TODO
	 * @return: List<MemberMember>
	 */
	List<MemberMember> findList(Member member , Member byMember);
	
	/**
	 * 
	 * @Title: findByTel
	 * @author: yuezhiwei
	 * @date: 2018年3月29日下午8:00:01
	 * @Description: TODO
	 * @return: MemberMember
	 */
	MemberMember findByTel(Member member , String tel);
}
