/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.MemberService;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminMemberRankingController")
@RequestMapping("/admin/member_ranking")
public class MemberRankingController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Member.RankingType rankingType, Pageable pageable, Model model) {
		if (rankingType == null) {
			rankingType = Member.RankingType.amount;
		}
		model.addAttribute("rankingTypes", Member.RankingType.values());
		model.addAttribute("rankingType", rankingType);
		model.addAttribute("page", memberService.findPage(rankingType, pageable));
		return "/admin/member_ranking/list";
	}

}