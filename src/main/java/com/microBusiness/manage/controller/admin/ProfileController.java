/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.Constant;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminProfileController")
@RequestMapping("/admin/profile")
public class ProfileController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;

	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		Admin admin = adminService.getCurrent();
		return StringUtils.equals(DigestUtils.md5Hex(currentPassword), admin.getPassword());
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		model.addAttribute("admin", adminService.getCurrent());
		return "/admin/profile/edit";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(String currentPassword, String password, String email, RedirectAttributes redirectAttributes) {
		if (!isValid(Admin.class, "email", email)) {
			return Message.error("数据异常");
		}
		Admin pAdmin = adminService.getCurrent();
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(password)) {
			if (!isValid(Admin.class, "password", password)) {
				return Message.error("数据异常");
			}
			if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), pAdmin.getPassword())) {
				return Message.warn("原密码错误");
			}
			pAdmin.setPassword(DigestUtils.md5Hex(password));
		}
		pAdmin.setEmail(email);
		adminService.update(pAdmin);
		return Message.success("修改成功");
	}
	
	/**
     * 发送验证码
     * @param request
     * @param tel
     * @param smsType 验证码类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendSms" , method = RequestMethod.GET)
    public JsonEntity sendSms(String tel , BindPhoneSms.SmsType smsType) {
    	Admin admin = adminService.getCurrent();
    	if(tel == null) {
    		return new JsonEntity("101001" , "请输入绑定手机号");
    	}
    	//判断是否超过次数
    	boolean isOver = isOver(tel, admin , smsType);
    	if(isOver) {
    		return new JsonEntity("101002" , "每天只能发送:" + MAX_SEND) ;
    	}
    	String opt = RandomStringUtils.randomNumeric(6);
    	
    	bindPhoneSmsService.sendSms(tel, "验证码："+opt+"，请在4分钟内输入验证");
    	
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setAdminId(admin.getId());
    	bindPhoneSms.setSmsType(smsType);
    	bindPhoneSms.setSendTime(new Date());
    	bindPhoneSms.setMobile(tel);
    	bindPhoneSms.setCode(opt);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	bindPhoneSmsService.save(bindPhoneSms);
    	return JsonEntity.successMessage();
    }
    
    private static final Long MAX_SEND = 5L;
    private static final Long EXPIRED_TIME = 4*60*1000L;
    
    private boolean isOver(String tel , Admin admin , BindPhoneSms.SmsType smsType) {
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setSmsType(smsType);
    	bindPhoneSms.setMobile(tel);
    	Long count = bindPhoneSmsService.countBindPhoneSms(bindPhoneSms, true);
    	return count > MAX_SEND ? true : false;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/checkSms" , method = RequestMethod.GET)
    public JsonEntity checkSms(String tel , String code) {
    	Admin admin = adminService.getCurrent();
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setAdminId(admin.getId());
    	bindPhoneSms.setCode(code);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.bindPhoneNum);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
    	
    	if(null == findSms || !code.equals(findSms.getCode())) {
    		return new JsonEntity("101003" , "验证码错误");
    	}
    	if(!tel.equals(findSms.getMobile())) {
    		return new JsonEntity("101004" , "手机号与验证码不匹配");
    	}
    	if(this.isExpired(findSms)) {
    		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findSms);
    		return new JsonEntity("101005" , "验证码过期");
    	}
    	admin.setBindPhoneNum(tel);
    	adminService.update(admin);
    	findSms.setStatus(BindPhoneSms.Status.USED);
    	bindPhoneSmsService.update(findSms);
    	return JsonEntity.successMessage();
    }
    
    /**
     * 判断验证码是否过期
     * @param bindPhoneSms
     * @return
     */
    private boolean isExpired(BindPhoneSms bindPhoneSms) {
    	System.out.println(System.currentTimeMillis() - bindPhoneSms.getSendTime().getTime());
    	if(System.currentTimeMillis() - bindPhoneSms.getSendTime().getTime() > EXPIRED_TIME) {
    		return true;
    	}
    	return false;
    }
    
    
    /**
     * 更换手机号
     * @param originalPhone 原手机号
     * @param originalCode 原手机号验证码
     * @param newPhone 新手机号
     * @param newCode 新手机号验证码
     * @param redirectAttributes
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/replacePhoneNumber" , method = RequestMethod.POST)
    public JsonEntity replacePhoneNumber(String originalPhone , String originalCode , String newPhone , String newCode) {
    	Admin admin = adminService.getCurrent();
    	if(originalPhone == null || originalCode == null || newPhone == null || newCode == null || admin == null) {
    		return new JsonEntity("101006" , "数据错误");
    	}
    	if(!originalPhone.equals(admin.getBindPhoneNum())) {
    		return new JsonEntity("101007" , "原手机号错误");
    	}
    	//判断新手机号是否已经绑定过
    	Admin newadmin = adminService.findBybindPhoneNum(newPhone);
    	if(null != newadmin) {
    		return new JsonEntity("101008" , "新手机号已经绑定过账号，不能在绑定了！");
    	}
    	
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setAdminId(admin.getId());
    	bindPhoneSms.setCode(originalCode);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.replacePhoneNum);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findSms = null;
    	
    	//假手机号逻辑
		if(originalPhone.startsWith(Constant.tel_pre_num2)){
			findSms = new BindPhoneSms();
    		String code2 =  CommonUtils.getInnerCheckCode(originalPhone);
    		findSms.setCode(code2);
    		findSms.setMobile(originalPhone);
		}else{
			findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
		}
    	
    	if(null == findSms || !originalCode.equals(findSms.getCode())) {
    		return new JsonEntity("101003" , "验证码错误");
    	}
    	if(!originalPhone.equals(findSms.getMobile())) {
    		return new JsonEntity("101004" , "手机号错误");
    	}
    	if(originalPhone.startsWith(Constant.tel_pre_num1)){
    		if(this.isExpired(findSms)) {
        		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
        		bindPhoneSmsService.update(findSms);
        		return new JsonEntity("101005" , "验证码过期");
        	}
    	}
    	
    	
    	BindPhoneSms newbindPhoneSms = new BindPhoneSms();
    	newbindPhoneSms.setAdminId(admin.getId());
    	newbindPhoneSms.setCode(newCode);
    	newbindPhoneSms.setSmsType(BindPhoneSms.SmsType.replacePhoneNum);
    	newbindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findNewSms = bindPhoneSmsService.findBindPhoneSms(newbindPhoneSms);
    	
    	if(null == findNewSms || !newCode.equals(findNewSms.getCode())) {
    		return new JsonEntity("101003" , "验证码错误");
    	}
    	if(!newPhone.equals(findNewSms.getMobile())) {
    		return new JsonEntity("101004" , "手机号与验证码不匹配");
    	}
    	if(this.isExpired(findNewSms)) {
    		findNewSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findNewSms);
    		return new JsonEntity("101005" , "验证码过期");
    	}
    	
    	admin.setBindPhoneNum(newPhone);
    	adminService.update(admin);
    	findSms.setStatus(BindPhoneSms.Status.USED);
    	findNewSms.setStatus(BindPhoneSms.Status.USED);
    	if(originalPhone.startsWith(Constant.tel_pre_num1)){
    		bindPhoneSmsService.update(findSms);
    	}
    	bindPhoneSmsService.update(findNewSms);
    	return JsonEntity.successMessage();
    }
    
    /**
     * 验证手机号是否绑定
     * @param tel
     * @return
     */
    @RequestMapping(value = "/checkBindTel", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkBindTel(String tel){
        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        Admin admin = adminService.findBybindPhoneNum(tel);
        return null == admin? true : false ;

    }

}