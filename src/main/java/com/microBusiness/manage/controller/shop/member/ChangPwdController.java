package com.microBusiness.manage.controller.shop.member;

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

import com.microBusiness.manage.Message;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.service.AdminService;

/**
 * Created by yuezhiwei on 2017/8/2.
 * 功能描述：修改密码
 * 修改记录：
 */
@Controller("shopChangPwd")
@RequestMapping("/shop/member/changPwd")
public class ChangPwdController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@RequestMapping(value = "index" , method = RequestMethod.GET)
	public String index(ModelMap model , HttpServletRequest request) {
		Admin admin = this.getCurrentAdmin(request);
		model.addAttribute("admin", admin);
		model.addAttribute("nav", "changPwd");
		return "shop/member/changPwd/index";
	}
	
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword , HttpServletRequest request) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		Admin admin = this.getCurrentAdmin(request);
		return StringUtils.equals(DigestUtils.md5Hex(currentPassword), admin.getPassword());
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(String currentPassword, String password , HttpServletRequest request) {
		Admin pAdmin = this.getCurrentAdmin(request);
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(password)) {
			if (!isValid(Admin.class, "password", password)) {
				return ERROR_MESSAGE;
			}
			if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), pAdmin.getPassword())) {
				return ERROR_MESSAGE;
			}
			pAdmin.setPassword(DigestUtils.md5Hex(password));
		}
		adminService.update(pAdmin);
		return SUCCESS_MESSAGE;
	}
	
}
