package com.microBusiness.manage.controller.ass;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Sms;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.Code;

/**
 * 个体客户
 * 
 */
@Controller("assNeedCustomerController")
@RequestMapping("/ass/need")
public class AssNeedCustomerController extends BaseController {

	@Resource
	private NeedService needService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private AreaService areaService;
	@Resource
	private SmsService smsService;
	@Resource
	private MemberService memberService;
	@Resource
    private ChildMemberService childMemberService ;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JsonEntity save(String unionId, ModelMap model, Long supplierId, Need need, Long areaId, String code, String tel, RedirectAttributes redirectAttributes) {
		//AssChildMember assChildMember = this.getAssChildMember(unionId);
		Supplier supplier = supplierService.find(supplierId);

		if (need == null || areaId == null || supplier == null) {
			return new JsonEntity("010502", "参数错误");
		}
		if (needService.telExists(need.getTel(), supplier)) {
			return JsonEntity.error(Code.code18800, Code.code18800.getDesc());
		}

		need.setArea(areaService.find(areaId));
		need.setSupplier(supplier);
		needService.save(need);

		return checkSms(unionId, tel, code);
		
	}

	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity sendSms(HttpServletRequest request, String tel) {

		// 查询需要发送的手机号是否存在收货点主账号
		Member member = memberService.findByMobile(tel);

		if (null == member) {
			return new JsonEntity("101001", "请核对手机号是否绑定收货点！");
		}

		// 判断是否超过次数
		boolean isOver = isOver(tel, member);
		if (isOver) {
			return new JsonEntity("101002", "每天只能发送:" + MAX_SEND);
		}

		String opt = RandomStringUtils.randomNumeric(6);

		smsService.sendSms(tel, "验证码：" + opt + "，请在2分钟内输入验证");

		Sms sms = new Sms();
		sms.setUserId(member.getId());
		sms.setType(Sms.SmsType.BINDING);
		sms.setSendTime(new Date());
		sms.setMobile(tel);
		sms.setCode(opt);
		sms.setStatus(Sms.Status.EFFECTIVE);
		smsService.save(sms);

		return JsonEntity.successMessage();
	}

	public JsonEntity checkSms(String unionId, String tel, String code) {
		//AssChildMember assChildMember = this.getAssChildMember(unionId);
		Member member = memberService.findByMobile(tel);

		if (null == member) {
			return new JsonEntity("101001", "请核对手机号是否绑定收货点！");
		}

		Sms sms = new Sms();

		sms.setUserId(member.getId());
		sms.setCode(code);
		sms.setType(Sms.SmsType.BINDING);
		sms.setStatus(Sms.Status.EFFECTIVE);
		Sms findSms = smsService.findSms(sms);

		if (null == findSms || !code.equals(findSms.getCode())) {
			return new JsonEntity("101003", "验证码错误");
		}

		if (!tel.equals(findSms.getMobile())) {
			return new JsonEntity("101005", "手机号错误");
		}
		// 判断用户是否绑定过主账号
		ChildMember childMember = childMemberService.findByUnionId(unionId);
		if (null != childMember.getMember()) {
			return new JsonEntity("101006", "该微信号已经绑定过收货点主账号");
		}

		if (this.isExpired(findSms)) {
			findSms.setStatus(Sms.Status.EXPIRED);
			smsService.update(findSms);
			return new JsonEntity("101004", "验证码过期");
		}

		findSms.setStatus(Sms.Status.USED);
		smsService.update(findSms);

		childMember.setMember(member);
		childMemberService.update(childMember);

		return JsonEntity.successMessage();
	}

	private static final Long MAX_SEND = 100L;
	private static final Long EXPIRED_TIME = 2 * 60 * 1000L;

	private boolean isOver(String tel, Member currMember) {

		Sms findSms = new Sms();
		// findSms.setUserId(currMember.getId());
		findSms.setType(Sms.SmsType.BINDING);
		findSms.setMobile(tel);
		Long count = smsService.countSms(findSms, true);

		return count >= MAX_SEND ? true : false;
	}

	/**
     * 判断是否过期
     * @return
     */
    private boolean isExpired(Sms sms){
        System.out.println(System.currentTimeMillis() - sms.getSendTime().getTime());
        if(System.currentTimeMillis() - sms.getSendTime().getTime() > EXPIRED_TIME){
            return true ;
        }
        return false ;
    }

}