package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberIncomeDao;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.WithdrawDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberIncome;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;
import com.microBusiness.manage.service.WithdrawService;
import com.microBusiness.manage.util.DateUtils;

@Service("withdrawServiceImpl")
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw, Long> implements WithdrawService {

	@Resource(name = "withdrawDaoImpl")
	private WithdrawDao withdrawDao;
	@Resource
	private MemberDao memberDao;
	@Resource(name = "memberIncomeDaoImpl")
	private MemberIncomeDao memberIncomeDao;
	@Resource
	private OrderNewsPushDao orderNewsPushDao;
	@Resource
	private SupplierDao supplierDao;
	
	
	
	@Override
	@Transactional
	public Withdraw createWithdraw(Withdraw obj){
		//扣减余额
		Member member1 = obj.getMember().getMember();
		member1.setBalance(member1.getBalance().subtract(obj.getAmount()));
		memberDao.persist(member1);
		
		obj.setCreateDate(new Date());
		obj.setSn(obj.getMember().getId()+RandomStringUtils.randomAlphabetic(1)+DateUtils.convertToString(new Date(), "yyMMddHHmmss"));
		obj.setFee(BigDecimal.ZERO);
		obj.setStatus(Withdraw.Withdraw_Status.await);
		withdrawDao.persist(obj);
		
		//插入提现记录
		MemberIncome income = new MemberIncome();
		income.setAmount(obj.getAmount());
		income.setTypes(MemberIncome.TYPE_WITHDRAW);
		income.setMember(obj.getMember());
		income.setTitle("提现");
		income.setCreateDate(new Date());
		income.setCorreId(obj.getId());
		memberIncomeDao.persist(income);
		
		//后台消息推送
		Supplier supplier=supplierDao.find(1l);
		OrderNewsPush newsPush = new OrderNewsPush();
		newsPush.setLinkId(obj.getId());
		newsPush.setSn(obj.getSn());
		newsPush.setNeed(null);
		newsPush.setSupplier(supplier);
		newsPush.setOrderStatus(OrderNewsPush.OrderStatus.leaveAMessage);
		newsPush.setStatus(OrderNewsPush.Status.unread);
		newsPush.setSend("会员用户");
		newsPush.setReceive(obj.getMember().getNickName());
		newsPush.setNoticeObject(OrderNewsPush.NoticeObject.withdraw);
		orderNewsPushDao.persist(newsPush);
		
		return obj;
	}
	
	@Override
	@Transactional
	public Withdraw auditWithdraw(Withdraw obj){
		if(!Withdraw_Status.complete.equals(obj.getStatus())
				&&!Withdraw_Status.await.equals(obj.getStatus())){
			//提现失败退回余额给会员账户
			Member member1 = obj.getMember().getMember();
			member1.setBalance(member1.getBalance().add(obj.getAmount()));
			memberDao.persist(member1);
		}
		if(Withdraw_Status.complete.equals(obj.getStatus())){
			MemberIncome income=this.memberIncomeDao.getByCorreId(obj.getId(), MemberIncome.TYPE_WITHDRAW);
			if(income!=null) {
				income.setRemark("提现成功");
				memberIncomeDao.persist(income);
			}
		}if(Withdraw_Status.fail.equals(obj.getStatus())){
			MemberIncome income=this.memberIncomeDao.getByCorreId(obj.getId(), MemberIncome.TYPE_WITHDRAW);
			if(income!=null) {
				income.setRemark("提现失败");
				memberIncomeDao.persist(income);
			}
		}if(Withdraw_Status.repeal.equals(obj.getStatus())){
			MemberIncome income=this.memberIncomeDao.getByCorreId(obj.getId(), MemberIncome.TYPE_WITHDRAW);
			if(income!=null) {
				income.setRemark("取消提现");
				memberIncomeDao.persist(income);
			}
		}
		obj.setProcessTime(new Date());
		withdrawDao.persist(obj);
		return obj;
	}
	
	@Override
	@Transactional
	public Withdraw save(Withdraw Withdraw) {
		return super.save(Withdraw);
	}

	@Override
	@Transactional
	public Withdraw update(Withdraw Withdraw) {
		return super.update(Withdraw);
	}

	@Override
	@Transactional
	public Withdraw update(Withdraw Withdraw, String... ignoreProperties) {
		return super.update(Withdraw, ignoreProperties);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(Withdraw Withdraw) {
		super.delete(Withdraw);
	}

	@Override
	public List<Withdraw> query(ChildMember m) {
		return withdrawDao.query(m);
	}
	
	@Override
	public Page<Withdraw> findPage(Withdraw_Status status,ChildMember member,Date startDate,
			Date endDate,String timeSearch,Pageable pageable){
		return withdrawDao.findPage(status, member, startDate, endDate, timeSearch, pageable);
	}

}