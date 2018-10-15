/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.DepositLogService;
import com.microBusiness.manage.service.MemberService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminDepositController")
@RequestMapping("/admin/deposit")
public class DepositController extends BaseController {

	@Resource(name = "depositLogServiceImpl")
	private DepositLogService depositLogService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@RequestMapping(value = "/check_member", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> checkMember(String username) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.deposit.memberNotExist"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("balance", member.getBalance());
		return data;
	}

	@RequestMapping(value = "/adjust", method = RequestMethod.GET)
	public String adjust() {
		return "/admin/deposit/adjust";
	}

	@RequestMapping(value = "/adjust", method = RequestMethod.POST)
	public String adjust(String username, BigDecimal amount, String memo, RedirectAttributes redirectAttributes) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return ERROR_VIEW;
		}
		if (member.getBalance() == null || member.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		memberService.addBalance(member, amount, DepositLog.Type.adjustment, admin, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Long memberId, Pageable pageable, ModelMap model) {
		Member member = memberService.find(memberId);
		if (member != null) {
			model.addAttribute("member", member);
			model.addAttribute("page", depositLogService.findPage(member, pageable));
		} else {
			model.addAttribute("page", depositLogService.findPage(pageable));
		}
		return "/admin/deposit/log";
	}

}