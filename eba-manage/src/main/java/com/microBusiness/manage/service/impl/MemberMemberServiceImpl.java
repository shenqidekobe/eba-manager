package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberMemberDao;
import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberMember;
import com.microBusiness.manage.service.MemberMemberService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service("memberMemberServiceImpl")
public class MemberMemberServiceImpl extends BaseServiceImpl<MemberMember, Long> implements MemberMemberService {
	
	@Resource
	private MemberMemberDao memberMemberDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Override
	public Page<MemberMember> findPage(Pageable pageable, Member member,
			Member byMember) {
		return memberMemberDao.findPage(pageable, member, byMember);
	}

	@Override
	public MemberMember save(Member member, String tel, String name) {
		Member byMember = memberDao.findByUsername(tel);
        if (byMember == null) {
        	byMember = new Member();

        	byMember.setUsername(tel);
        	byMember.setNickname(name);
        	byMember.setMobile(tel);
        	byMember.setOpenId("-1");
	        //member.setNeed(entity);

        	byMember.setPassword(DigestUtils.md5Hex("123456"));
        	byMember.setEmail("afeijackcool@163.com");
        	byMember.setPoint(BigDecimal.ZERO);
        	byMember.setBalance(BigDecimal.ZERO);
        	byMember.setIncome(BigDecimal.ZERO);
        	byMember.setYesterdayIncome(BigDecimal.ZERO);
        	byMember.setAmount(BigDecimal.ZERO);
        	byMember.setIsEnabled(true);
        	byMember.setIsLocked(false);
        	byMember.setLoginFailureCount(0);
        	byMember.setLockedDate(null);
        	byMember.setRegisterIp("");
        	byMember.setLoginIp("");
        	byMember.setLoginDate(new Date());
        	byMember.setLoginPluginId(null);
        	byMember.setLockKey(null);
        	byMember.setSafeKey(null);
        	byMember.setMemberRank(memberRankDao.findDefault());
        	byMember.setCart(null);
        	byMember.setOrders(null);
        	byMember.setPaymentLogs(null);
        	byMember.setDepositLogs(null);
        	byMember.setCouponCodes(null);
        	byMember.setReceivers(null);
        	byMember.setReviews(null);
        	byMember.setConsultations(null);
        	byMember.setFavoriteGoods(null);
        	byMember.setProductNotifies(null);
        	byMember.setInMessages(null);
        	byMember.setOutMessages(null);
        	byMember.setPointLogs(null);
            memberDao.persist(byMember);
        }
        
        MemberMember memberMember = new MemberMember();
        memberMember.setMember(member);
        memberMember.setByMember(byMember);
        memberMember.setName(name);
        super.save(memberMember);
		return memberMember;
	}

	@Override
	public MemberMember update(MemberMember memberMember, String tel) {
		Member byMember = memberDao.findByUsername(tel);
        if (byMember == null) {
        	byMember = new Member();

        	byMember.setUsername(tel);
        	byMember.setNickname(memberMember.getName());
        	byMember.setMobile(tel);
        	byMember.setOpenId("-1");
	        //member.setNeed(entity);

        	byMember.setPassword(DigestUtils.md5Hex("123456"));
        	byMember.setEmail("afeijackcool@163.com");
        	byMember.setPoint(BigDecimal.ZERO);
        	byMember.setBalance(BigDecimal.ZERO);
        	byMember.setIncome(BigDecimal.ZERO);
        	byMember.setYesterdayIncome(BigDecimal.ZERO);
        	byMember.setAmount(BigDecimal.ZERO);
        	byMember.setIsEnabled(true);
        	byMember.setIsLocked(false);
        	byMember.setLoginFailureCount(0);
        	byMember.setLockedDate(null);
        	byMember.setRegisterIp("");
        	byMember.setLoginIp("");
        	byMember.setLoginDate(new Date());
        	byMember.setLoginPluginId(null);
        	byMember.setLockKey(null);
        	byMember.setSafeKey(null);
        	byMember.setMemberRank(memberRankDao.findDefault());
        	byMember.setCart(null);
        	byMember.setOrders(null);
        	byMember.setPaymentLogs(null);
        	byMember.setDepositLogs(null);
        	byMember.setCouponCodes(null);
        	byMember.setReceivers(null);
        	byMember.setReviews(null);
        	byMember.setConsultations(null);
        	byMember.setFavoriteGoods(null);
        	byMember.setProductNotifies(null);
        	byMember.setInMessages(null);
        	byMember.setOutMessages(null);
        	byMember.setPointLogs(null);
            memberDao.persist(byMember);
        }
        memberMember.setByMember(byMember);
        memberMemberDao.merge(memberMember);
		return memberMember;
	}

	@Override
	public boolean telExists(Member member, Member byMember) {
		return memberMemberDao.telExists(member, byMember);
	}

	@Override
	public MemberMember verifyTelExists(String tel, Member member, MemberMember memberMember) {
		return memberMemberDao.verifyTelExists(tel, member, memberMember);
	}

	@Override
	public MemberMember findByTel(Member member, String tel) {
		return memberMemberDao.findByTel(member, tel);
	}

}
