package com.microBusiness.manage.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.RoleService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.Constant;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("registeredController")
@RequestMapping("/admin/registered")
public class RegisteredController extends BaseController{
	
	@Resource(name = "supplierServiceImpl")
	private SupplierService supplierService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;
	@Resource
	private MemberService memberService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(String type, String redirectUrl, ModelMap model) {
		
		model.put("type", type);
		model.addAttribute("redirectUrl", redirectUrl);
		return "/admin/registered/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getSupplierByName", method = RequestMethod.GET)
	public Map<String, Object> getSupplierByName(Supplier supplier){
		Map<String, Object> model=new HashMap<>();
		model.put("exists", supplierService.exists(supplier.getName()));
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/usernameExists", method = RequestMethod.GET)
	public Map<String, Object> usernameExists(Admin admin){
		Map<String, Object> model=new HashMap<>();
		model.put("exists", adminService.usernameExists(admin.getUsername()));
		return model;
	}

	/**
	 *
	 * @param supplier
	 * @param admin
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, Object> register(Supplier supplier,Admin admin){
		// FIXME: 2017/3/14 待修改，需要将保存相关步骤放到业务层，否则出现无法回滚的情况
		supplier.setStatus(Supplier.Status.notCertified);
		supplier=supplierService.save(supplier);
		Long[] roleIds={4L};
		admin.setSupplier(supplier);
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		admin.setLockKey(null);
		admin.setName(null);
		admin.setIsEnabled(true);
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		adminService.save(admin);

		adminService.register(supplier , admin);

		Map<String, Object> model=new HashMap<>();
		model.put("isRegister", true);
		return model;
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JsonEntity register(Supplier supplier,Admin admin,String code){
		String tel = admin.getBindPhoneNum();
		if (StringUtils.isEmpty(tel)) {
			return new JsonEntity("101002" , "手机号不能为空！");
        }
		if (StringUtils.isEmpty(code)) {
			return new JsonEntity("101003" , "验证码不能为空！");
        }
		BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setCode(code);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.bindPhoneNum);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	
    	BindPhoneSms findSms = null;
    	//假手机号逻辑
		if(tel.startsWith(Constant.tel_pre_num2)){
			findSms = new BindPhoneSms();
			String code2 =  CommonUtils.getInnerCheckCode(tel);
    		findSms.setCode(code2);
    		findSms.setMobile(tel);
		}else{
			findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
		}
		
    	if(null == findSms || !code.equals(findSms.getCode())) {
    		return new JsonEntity("101004" , "验证码错误");
    	}
    	if(!tel.equals(findSms.getMobile())) {
    		return new JsonEntity("101005" , "手机号与验证码不匹配");
    	}
    	
    	Admin adminEntity = adminService.findBybindPhoneNum(tel);
    	if(adminEntity != null){
    		return new JsonEntity("101015" , "该手机号已存在！");
    	}
    	
    	if(tel.startsWith(Constant.tel_pre_num1)){
    		if(this.isExpired(findSms)) {
        		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
        		bindPhoneSmsService.update(findSms);
        		return new JsonEntity("101006" , "验证码过期");
        	}
        	findSms.setStatus(BindPhoneSms.Status.USED);
        	bindPhoneSmsService.update(findSms);
    	}

    	Member member = memberService.findByMobile(tel);
    	
		adminService.register(supplier , admin,member);
		
		if (member == null) {
			member = memberService.addMember(tel);
			
			member.setAdmin(admin);
			
			memberService.update(member);
			
			System.out.println();
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 验证手机号是否存在
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkTel" , method = RequestMethod.GET)
	public boolean checkTel(String bindPhoneNum){
		if (StringUtils.isEmpty(bindPhoneNum)) {
            return false;
        }
		Admin admin = adminService.findBybindPhoneNum(bindPhoneNum);
		return null == admin? true : false ;
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
    public JsonEntity sendSms(HttpServletRequest request ,String tel) {
    	if(tel == null) {
    		return new JsonEntity("101001" , "请输入绑定手机号");
    	}
    	//判断是否超过次数
    	/*boolean isOver = isOver(tel , BindPhoneSms.SmsType.bindPhoneNum);
    	if(isOver) {
    		return new JsonEntity("101002" , "每天只能发送:" + MAX_SEND) ;
    	}*/
    	String opt = RandomStringUtils.randomNumeric(6);
    	
    	bindPhoneSmsService.sendSms(tel, "验证码："+opt+"，请在2分钟内输入验证");
    	
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.bindPhoneNum);
    	bindPhoneSms.setSendTime(new Date());
    	bindPhoneSms.setMobile(tel);
    	bindPhoneSms.setCode(opt);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	bindPhoneSmsService.save(bindPhoneSms);
    	return JsonEntity.successMessage();
    }
    
    //private static final Long MAX_SEND = 5L;
    private static final Long EXPIRED_TIME = 2*60*1000L;
    
    /*private boolean isOver(String tel , BindPhoneSms.SmsType smsType) {
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setSmsType(smsType);
    	bindPhoneSms.setMobile(tel);
    	Long count = bindPhoneSmsService.countBindPhoneSms(bindPhoneSms, true);
    	return count > MAX_SEND ? true : false;
    }*/
    
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

}
