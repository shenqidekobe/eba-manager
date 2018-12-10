package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Principal;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.DepositLogDao;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.dao.PointLogDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberRank;
import com.microBusiness.manage.entity.PointLog;
import com.microBusiness.manage.service.MailService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.util.SystemUtils;

@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements MemberService {

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;
	@Resource(name = "depositLogDaoImpl")
	private DepositLogDao depositLogDao;
	@Resource(name = "pointLogDaoImpl")
	private PointLogDao pointLogDao;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);

		Setting setting = SystemUtils.getSetting();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		}
		return !memberDao.emailExists(currentEmail);
	}

	@Transactional(readOnly = true)
	public Member find(String loginPluginId, String openId) {
		return memberDao.find(loginPluginId, openId);
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		return memberDao.findPage(rankingType, pageable);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		return requestAttributes != null && requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) != null;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		return getCurrent(false);
	}

	@Transactional(readOnly = true)
	public Member getCurrent(boolean lock) {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		Long id = principal != null ? principal.getId() : null;
		if (lock) {
			return memberDao.find(id, LockModeType.PESSIMISTIC_WRITE);
		} else {
			return memberDao.find(id);
		}
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		return principal != null ? principal.getUsername() : null;
	}

	public void addBalance(Member member, BigDecimal amount, DepositLog.Type type, Admin operator, String memo) {
		Assert.notNull(member);
		Assert.notNull(amount);
		Assert.notNull(type);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getBalance());
		Assert.state(member.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0);

		member.setBalance(member.getBalance().add(amount));
		memberDao.flush();

		DepositLog depositLog = new DepositLog();
		depositLog.setType(type);
		depositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		depositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		depositLog.setBalance(member.getBalance());
		depositLog.setOperator(operator);
		depositLog.setMemo(memo);
		depositLog.setMember(member);
		depositLogDao.persist(depositLog);
	}

	public void addPoint(Member member, long amount, PointLog.Type type, Admin operator, String memo) {
		Assert.notNull(member);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getPoint());
		Assert.state(member.getPoint().add(new BigDecimal(amount)).compareTo(BigDecimal.ZERO) > 0);

		member.setPoint(member.getPoint().add(new BigDecimal(amount)));
		memberDao.flush();

		PointLog pointLog = new PointLog();
		pointLog.setType(type);
		pointLog.setCredit(amount > 0 ? amount : 0L);
		pointLog.setDebit(amount < 0 ? Math.abs(amount) : 0L);
		pointLog.setBalance(member.getPoint().longValue());
		pointLog.setOperator(operator);
		pointLog.setMemo(memo);
		pointLog.setMember(member);
		pointLogDao.persist(pointLog);
	}

	public void addAmount(Member member, BigDecimal amount) {
		Assert.notNull(member);
		Assert.notNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getAmount());
		Assert.state(member.getAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0);

		member.setAmount(member.getAmount().add(amount));
		MemberRank memberRank = member.getMemberRank();
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			MemberRank newMemberRank = memberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank);
			}
		}
		memberDao.flush();
	}

	@Override
	@Transactional
	public Member save(Member member) {
		Assert.notNull(member);

		Member pMember = super.save(member);
		mailService.sendRegisterMemberMail(pMember);
		smsService.sendRegisterMemberSms(pMember);
		return pMember;
	}

	@Transactional(readOnly = true)
	public Member findByOpenId(String openId) {
		return memberDao.findByOpenId(openId);
	}

	@Transactional(readOnly = true)
	@Override
	public Member findByMobile(String mobile) {
		return memberDao.findByMobile(mobile);
	}

	/**
	 *
	 * @param startRow
	 * @param offset
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Object[]> getMemberToNotice(int startRow, int offset , Date compareDate) {
		return memberDao.getMemberToNotice(startRow , offset , compareDate);
	}

	@Override
	public Member addMember(String tel) {
		Member member=new Member();
    	member.setUsername(tel);
    	member.setNickname(tel);
    	member.setMobile(tel);
    	member.setOpenId("-1");

    	member.setPassword(DigestUtils.md5Hex("123456"));
    	member.setEmail("afeijackcool@163.com");
    	member.setPoint(BigDecimal.ZERO);
    	member.setBalance(BigDecimal.ZERO);
    	member.setIncome(BigDecimal.ZERO);
    	member.setYesterdayIncome(BigDecimal.ZERO);
    	member.setAmount(BigDecimal.ZERO);
    	member.setIsShoper(false);
    	member.setIsEnabled(true);
    	member.setIsLocked(false);
    	member.setLoginFailureCount(0);
    	member.setLockedDate(null);
    	member.setRegisterIp("");
    	member.setLoginIp("");
    	member.setLoginDate(new Date());
    	member.setLoginPluginId(null);
    	member.setLockKey(null);
    	member.setSafeKey(null);
    	member.setMemberRank(memberRankDao.findDefault());
    	member.setCart(null);
    	member.setOrders(null);
        member.setPaymentLogs(null);
        member.setDepositLogs(null);
        member.setCouponCodes(null);
        member.setReceivers(null);
        member.setReviews(null);
        member.setConsultations(null);
        member.setFavoriteGoods(null);
        member.setProductNotifies(null);
        member.setInMessages(null);
        member.setOutMessages(null);
        member.setPointLogs(null);
        memberDao.persist(member);
		return member;
	}

	@Transactional
	@Override
	public void refreshMember(Member member) {
		memberDao.persist(member);

	}

}