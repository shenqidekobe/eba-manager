package com.microBusiness.manage.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.NewMessage;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.NewMessageService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 消息管理Controller
 * 
 * @author Administrator
 *
 */
@Controller("messageManageController")
@RequestMapping("/admin/messageManage")
public class MessageManageController extends BaseController{

	@Resource(name = "newMessageServiceImpl")
	private NewMessageService newMessageService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(NewMessage newMessage, HttpServletRequest request, Model model){
		List<Admin> adminList = adminService.findAll();
		
		Boolean flag = newMessageService.send(this.getCurrentAdmin(request), newMessage, adminList, request);
		if (flag) {
			return "redirect:/admin/messageManage/list.jhtml";
		}else {
			model.addAttribute("newMessageNew", newMessage);
			return "/admin/message/send";
		}
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(){
		return "/admin/message/send";
	}
	
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public String queryById(Long id, Model model){
		model.addAttribute("newMessage", newMessageService.find(id));
		return "/admin/message/send";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(NewMessage newMessage, Pageable pageable, Model model){
		model.addAttribute("page", newMessageService.query(newMessage, pageable));
		model.addAttribute("newMessage", newMessage);
		return "/admin/message/list";
	}
}
