package com.microBusiness.manage.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microBusiness.manage.dto.DictJson;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Dict;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.TemplateInfo;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.DictService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;
import com.microBusiness.manage.util.JsonUtils;

/**
 * 订单相关定时任务处理
 * */
@Lazy(false)
@Component("orderJob")
public class OrderJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource
	private MemberService memberService ;
	@Resource
	private WeChatService weChatService ;
	@Value("${order.template.longNotice.templateId}")
	private String noticeTemplateId;
	@Resource(name = "dictServiceImpl")
	private DictService dictService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	//每隔60分钟执行：0 0/60 * * * ?
	//结算返佣、已完成未返佣的订单、在已完成时间加15再返
	@Scheduled(cron = "0 0/60 * * * ?")
	public void jiesuan() {
		Dict dict=dictService.find(Dict.DEFAULT_ID);
		Integer intervalDayCommision=15;
		if(dict!=null&&StringUtils.isNotEmpty(dict.getJson())) {
			DictJson json=JsonUtils.toObject(dict.getJson(),DictJson.class);
			intervalDayCommision=(json.getIntervalDayCommision());
		}
		List<Order> list=orderService.findNoRakeBackList();//已完成未返佣的订单
		logger.info("定时返佣任务执行器，本次需要处理："+list.size()+" 条未返佣订单数据.");
		for(Order order:list) {
			if(order.getCompleteDate()==null)continue;
			Date completeTime = order.getCompleteDate();
			Calendar cal=Calendar.getInstance();
			cal.setTime(completeTime);
			cal.add(Calendar.DATE,intervalDayCommision);//加N天
			if (new Date().after(cal.getTime())) {
				orderService.distributionSettlement(order);//完成时间N天后再进行结算
			}
		}
	}

	//已发货未确认的订单15日后自动完成
	@Scheduled(cron = "0 0 1 * * ?")
	public void complete() {
		List<Order> list=orderService.findList(null, Order.Status.shipped, null, null, null, null, null, null, null, null, null, null, null);
		Admin admin=this.adminService.find(1L);
		for(Order order:list) {
			if(order.getShippedDate()==null)continue;
			Date shippedDate = order.getShippedDate();
			Calendar cal=Calendar.getInstance();
			cal.setTime(shippedDate);
			cal.add(Calendar.DATE,15);//加15天
			if (new Date().after(cal.getTime())) {
				orderService.receive(order, admin);
				//收货就标示完成
				orderService.complete(order, admin);
			}
		}
	}

	//@Scheduled(cron = "${job.order_expired_processing.cron}")
	@Deprecated
	public void expiredProcessing() {
		orderService.undoExpiredUseCouponCode();
		orderService.undoExpiredExchangePoint();
		orderService.releaseExpiredAllocatedStock();
	}

	/**
	 * 发送 长时间未下单提醒
	 * 现在使用一个sql直接查询，对性能方面有影响，后期可优化改造，使用队列等
	 * 这里的写法在dao中将下单间隔也返回了
	 */

	public void sendNoticeByMember(){


		//查出供应关系中用户下的单
		int startPage = 0;
		int startRow = 0;
		int offset = 100 ;

		while (true){
			startRow = startPage * offset ;
			Date compareDate = new Date();

			List<Object[]> results = memberService.getMemberToNotice(startRow , offset , compareDate);

			this.sendNoticeByMember(results , compareDate);

			if(results.size() < offset){
				break;
			}

			startRow ++;
		}

	}

	//@Scheduled(cron = "${job.order_soon_order_notice.cron}")
	//@Scheduled(cron = "0 0/2 * * * ? ")
	public void sendNoticeByOrder(){
		orderService.sendNoOrderNotice(this.noticeTemplateId);
	}


	private void sendNoticeByMember(List<Object[]> results , final Date compareDate){

		if (CollectionUtils.isEmpty(results)) {
			return ;
		}

		for(Object[] objects  : results){
			final Integer noticeDay = (Integer) objects[0];
			Member member = (Member)objects[1] ;
			Set<ChildMember> childMembers = member.getChildMembers() ;

			if(CollectionUtils.isEmpty(childMembers)){
				continue ;
			}

			TemplateInfo templateInfo = new TemplateInfo();
			templateInfo.setTemplateId(this.noticeTemplateId);
			templateInfo.setData(new HashMap<String, Map<String, String>>(){{

				this.put("first" , new HashMap<String, String>(){{
					this.put("value", "您有"+ noticeDay +"天未订货了，请及时盘点商品库存进行订货，以免造成运营麻烦！");
				}});

				this.put("keyword1", new HashMap<String, String>() {{
					this.put("value", "盘点下单提醒");
				}});

				this.put("keyword2", new HashMap<String, String>() {{
					this.put("value", DateUtils.formatDateToString(compareDate , DateformatEnum.yyyy年MM月dd日) + " 9:00");
				}});

				this.put("remark", new HashMap<String, String>() {{
					this.put("value", "微商小管理温馨提示！");
				}});

			}});


			for (ChildMember childMember : childMembers){
				templateInfo.setToUser(childMember.getOpenId());
				weChatService.sendTemplateMessage(templateInfo , weChatService.getGlobalToken());
			}

		}

	}


	private void sendNoticeByOrder(List<Order> orders , final Date compareDate){
		if (CollectionUtils.isEmpty(orders)) {
			return ;
		}
		try {

			for (Order order : orders) {
				Member member = order.getMember();
				Set<ChildMember> childMembers = member.getChildMembers();

				final int noOrderDays = Days.daysBetween(new DateTime(DateFormatUtils.format(order.getCreateDate(), "yyyy-MM-dd 00:00:00")), new DateTime(DateFormatUtils.format(compareDate, "yyyy-MM-dd 00:00:00"))).getDays();

				if (CollectionUtils.isEmpty(childMembers)) {
					continue;
				}

				TemplateInfo templateInfo = new TemplateInfo();
				templateInfo.setTemplateId(this.noticeTemplateId);
				templateInfo.setData(new HashMap<String, Map<String, String>>() {{

					this.put("first", new HashMap<String, String>() {{
						this.put("value", "您有" + noOrderDays + "天未订货了，请及时盘点商品库存进行订货，以免造成运营麻烦！");
					}});

					this.put("keyword1", new HashMap<String, String>() {{
						this.put("value", "盘点下单提醒");
					}});

					this.put("keyword2", new HashMap<String, String>() {{
						this.put("value", DateUtils.formatDateToString(compareDate, DateformatEnum.yyyy年MM月dd日) + " 9:00");
					}});

					this.put("remark", new HashMap<String, String>() {{
						this.put("value", "微商小管理温馨提示！");
					}});

				}});


				for (ChildMember childMember : childMembers) {
					templateInfo.setToUser(childMember.getOpenId());
					weChatService.sendTemplateMessage(templateInfo, weChatService.getGlobalToken());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}