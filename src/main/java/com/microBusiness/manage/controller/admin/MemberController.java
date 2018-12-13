package com.microBusiness.manage.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberAttribute;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberAttributeService;
import com.microBusiness.manage.service.MemberIncomeService;
import com.microBusiness.manage.service.MemberRankService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.PluginService;
import com.microBusiness.manage.service.WithdrawService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.SystemUtils;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource
	private ChildMemberService childMemberService;
	@Resource(name = "memberIncomeServiceImpl")
	private MemberIncomeService memberIncomeService;
	@Resource(name = "withdrawServiceImpl")
	private WithdrawService withdrawService;

	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !memberService.usernameDisabled(username) && !memberService.usernameExists(username);
	}

	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmail(String previousEmail, String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return memberService.emailUnique(previousEmail, email);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		ChildMember member = childMemberService.find(id);
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		return "/admin/member/view";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		return "/admin/member/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Member member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankService.find(memberRankId));
		if (!isValid(member, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		if (member.getUsername().length() < setting.getUsernameMinLength() || member.getUsername().length() > setting.getUsernameMaxLength()) {
			return ERROR_VIEW;
		}
		if (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		if (memberService.usernameDisabled(member.getUsername()) || memberService.usernameExists(member.getUsername())) {
			return ERROR_VIEW;
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(member.getEmail())) {
			return ERROR_VIEW;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return ERROR_VIEW;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setPoint(BigDecimal.ZERO);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(null);
		member.setSafeKey(null);
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
		memberService.save(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.find(id);
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		model.addAttribute("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		return "/admin/member/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Member member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankService.find(memberRankId));
		if (!isValid(member)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		if (member.getPassword() != null && (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength())) {
			return ERROR_VIEW;
		}
		Member pMember = memberService.find(member.getId());
		if (pMember == null) {
			return ERROR_VIEW;
		}
		if (!setting.getIsDuplicateEmail() && !memberService.emailUnique(pMember.getEmail(), member.getEmail())) {
			return ERROR_VIEW;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return ERROR_VIEW;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		if (StringUtils.isEmpty(member.getPassword())) {
			member.setPassword(pMember.getPassword());
		} else {
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		}
		if (pMember.getIsLocked() && !member.getIsLocked()) {
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
		} else {
			member.setIsLocked(pMember.getIsLocked());
			member.setLoginFailureCount(pMember.getLoginFailureCount());
			member.setLockedDate(pMember.getLockedDate());
		}

		memberService.update(member, "username", "point", "balance", "amount", "registerIp", "loginIp", "loginDate", "loginPluginId", "openId", "lockKey", "safeKey", "cart", "orders", "paymentLogs", "depositLogs", "couponCodes", "receivers", "reviews", "consultations", "favoriteGoods",
				"productNotifies", "inMessages", "outMessages", "pointLogs");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable,Date startDate,Date endDate,String nickName,
			String smOpenId,ChildMember.SourceType type,Boolean isShoper,
			Long parentId,String searchName,ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findAll());
		//model.addAttribute("page", memberService.findPage(pageable));
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
        ChildMember parent=null;
        if(parentId!=null) {
        	parent=this.childMemberService.find(parentId);
        }
		model.addAttribute("page", childMemberService.findPage(nickName, smOpenId, type,isShoper, parent, startDate, endDate, pageable));
		return "/admin/member/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
    Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Member member = memberService.find(id);
				if (member != null && member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
					return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
				}
			}
			memberService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/listChildren", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity listChildren(String smOpenId, String searchName) {
    	ChildMember childMember = childMemberService.findByOpenId(smOpenId);
		Map<String, Object> rootMap = new HashMap<String, Object>();
		List<Filter> searchfilters = new ArrayList<Filter>();
		Filter filter2 = new Filter();
		filter2.setIgnoreCase(true);
		filter2.setOperator(Operator.eq);
		filter2.setProperty("parent");
		filter2.setValue(childMember.getId());
		searchfilters.add(filter2);
		List<com.microBusiness.manage.Order> searchOrders = new ArrayList<com.microBusiness.manage.Order>();
		if(StringUtils.isNotEmpty(searchName)){
			filter2 = new Filter();
			filter2.setIgnoreCase(true);
			filter2.setOperator(Operator.like);
			filter2.setProperty("nickName");
			filter2.setValue("%" + searchName + "%");
			searchfilters.add(filter2);
		}
		com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
		order.setDirection(Direction.asc);
		order.setProperty("order");
		searchOrders.add(order);
		List<ChildMember> plist = childMemberService.findList(0, 1000, searchfilters, searchOrders);
//		if(plist == null || plist.isEmpty()){
//			if(StringUtils.isNotEmpty(searchName)){
//				filter2 = new Filter();
//				filter2.setIgnoreCase(true);
//				filter2.setOperator(Operator.like);
//				filter2.setProperty("tel");
//				filter2.setValue("%" + searchName + "%");
//				searchfilters.add(filter2);
//				plist = childMemberService.findList(0, 1000, searchfilters, searchOrders);
//			}
//		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(plist != null){
			for (ChildMember cm1 : plist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", cm1.getId());
				map.put("name", cm1.getNickName());
				map.put("image", cm1.getHeadImgUrl());
				list.add(map);
			}
		}
		rootMap.put("childrenSize", plist.size());
		rootMap.put("list", list);
		return JsonEntity.successMessage(rootMap);
	}
	
	
	
	/*******************************************2018新加功能***************************************/
	@RequestMapping(value = "/withdraw/list", method = RequestMethod.GET)
	public String withdrawList(Pageable pageable,Withdraw_Status status,
			Date startDate , Date endDate , String searchName , String timeSearch,
			Long memberId, ModelMap model) {
		ChildMember member=null;
		if(memberId!=null) {
			member=this.childMemberService.find(memberId);
		}
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        model.addAttribute("timeSearch", timeSearch);
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		model.addAttribute("page", withdrawService.findPage(status, member, startDate, endDate, timeSearch, pageable));
		return "/admin/member/withdraw/list";
	}
	@RequestMapping(value = "/withdraw/edit", method = RequestMethod.GET)
	public String withdrawEdit(Long id, ModelMap model) {
		Withdraw withdraw = withdrawService.find(id);
		model.addAttribute("obj", withdraw);
		model.addAttribute("statusList", Withdraw.Withdraw_Status.values());
		return "/admin/member/withdraw/edit";
	}
	@RequestMapping(value = "/withdraw/update", method = RequestMethod.POST)
	public String withdrawUpdate(Withdraw withdraw,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (!isValid(withdraw)) {
			return ERROR_VIEW;
		}
		Withdraw pWithdraw = withdrawService.find(withdraw.getId());
		if (pWithdraw == null) {
			return ERROR_VIEW;
		}
		withdraw.setProcessTime(new Date());
		if(pWithdraw.getStatus().equals(withdraw.getStatus())){
			withdrawService.update(withdraw, "sn","member","createDate","account","accountName","phone","amount");
		}else{
			pWithdraw.setStatus(withdraw.getStatus());
			pWithdraw.setRemark(pWithdraw.getRemark()==null?withdraw.getRemark():pWithdraw.getRemark()+" | "+withdraw.getRemark());
			pWithdraw.setFee(withdraw.getFee());
			withdrawService.auditWithdraw(pWithdraw);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	@RequestMapping(value = "/income/list", method = RequestMethod.GET)
	public String incomeList(Pageable pageable,Date startDate , Date endDate ,
			String searchName , String type,Long memberId, ModelMap model) {
		ChildMember childMember=this.childMemberService.find(memberId);
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		model.addAttribute("page", memberIncomeService.findPage(type, childMember, startDate, endDate, pageable));
		return "/admin/member/income/list";
	}

}