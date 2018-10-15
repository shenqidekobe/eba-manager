package com.microBusiness.manage.controller.shop.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.service.MessageService;
import com.microBusiness.manage.service.NewMessageCompamyService;

@Controller("shopMemberMsgController")
@RequestMapping("/shop/member/msg")
public class MsgController extends BaseController {
	
	@Resource
	private MessageService messageService;

	@Resource
	private NewMessageCompamyService newMessageCompamyService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model, HttpServletRequest request, Pageable pageable) {
		model.addAttribute("page", newMessageCompamyService.query(this.getCurrentAdmin(request), null, pageable));
		model.addAttribute("nav", "msglist");
		return "/shop/member/news/list";
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(ModelMap model, Long id, HttpServletRequest request) {
		model.addAttribute("newMessageCompamy", newMessageCompamyService.read(id, this.getCurrentAdmin(request), request));
		model.addAttribute("nav", "msglist");
		return "/shop/member/news/view";
	}
	
}
