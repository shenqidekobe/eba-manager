package com.microBusiness.manage.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.BindPhoneSmsService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 找回密码
 * @author yuezhiwei
 *
 */
@Controller("retrievePasswordController")
@RequestMapping("/admin/retrievePassword")
public class RetrievePasswordController extends BaseController {
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;
	
	@RequestMapping(value = "/index" , method = RequestMethod.GET)
	public String index(String type, String redirectUrl , ModelMap model) {
		model.put("type", type);
		model.addAttribute("redirectUrl", redirectUrl);
		return "/admin/retrievePassword/index";
	}
	
	private static final Long EXPIRED_TIME = 2*60*1000L;
	
	/**
	 * 验证当前手机号对应的用户是否存在
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUserwhetherexist" , method = RequestMethod.GET)
	public boolean checkUserwhetherexist(String tel) {
		if (StringUtils.isEmpty(tel)) {
            return false;
        }
        Admin admin = adminService.findBybindPhoneNum(tel);
        return null == admin? false : true ;      
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
    public JsonEntity sendSms(String tel) {
    	if(tel == null) {
    		return new JsonEntity("101001" , "请输入手机号");
    	}
    	String opt = RandomStringUtils.randomNumeric(6);
    	
    	bindPhoneSmsService.sendSms(tel, "验证码："+opt+"，请在2分钟内输入验证");
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.getBackPassword);
    	bindPhoneSms.setSendTime(new Date());
    	bindPhoneSms.setMobile(tel);
    	bindPhoneSms.setCode(opt);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	bindPhoneSmsService.save(bindPhoneSms);
    	return JsonEntity.successMessage();
    }

    
    @ResponseBody
    @RequestMapping(value = "/checkSms" , method = RequestMethod.GET)
    public JsonEntity checkSms(String tel , String code) {
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setCode(code);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.getBackPassword);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
    	
    	if(null == findSms || !code.equals(findSms.getCode())) {
    		return new JsonEntity("101002" , "验证码错误");
    	}
    	if(!tel.equals(findSms.getMobile())) {
    		return new JsonEntity("101003" , "手机号与验证码不匹配");
    	}
    	if(this.isExpired(findSms)) {
    		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findSms);
    		return new JsonEntity("101004" , "验证码过期");
    	}
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
     * 重置密码
     * @param tel
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public JsonEntity update(String tel , String code , String password) {
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setCode(code);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.getBackPassword);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
    	
    	if(null == findSms || !code.equals(findSms.getCode())) {
    		return new JsonEntity("101002" , "验证码错误");
    	}
    	if(!tel.equals(findSms.getMobile())) {
    		return new JsonEntity("101003" , "手机号与验证码不匹配");
    	}
    	if(this.isExpired(findSms)) {
    		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findSms);
    		return new JsonEntity("101004" , "验证码过期");
    	}
    	findSms.setStatus(BindPhoneSms.Status.USED);
    	bindPhoneSmsService.update(findSms);
    	
    	Admin admin = adminService.findBybindPhoneNum(tel);
    	if(null == admin) {
    		return new JsonEntity("101006" , "该账号不存在");
    	}
    	admin.setPassword(DigestUtils.md5Hex(password));
    	adminService.update(admin);
    	return JsonEntity.successMessage();
    }

}
