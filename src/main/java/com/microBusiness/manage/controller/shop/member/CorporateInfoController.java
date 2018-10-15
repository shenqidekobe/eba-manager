package com.microBusiness.manage.controller.shop.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.service.SupplierService;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 企业信息controller
 * @author yuezhiwei 2017/8/7
 *
 */
@Controller("shopMemberCorporateInfo")
@RequestMapping("/shop/member/corporateInfo")
public class CorporateInfoController extends BaseController {

	@Resource
	private AdminService adminService;
	
	@Resource
	private AreaService areaService;
	
	@Resource
	private SupplierService supplierService;
	
	@Resource
    private SmsService smsService ;
	
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;
	/**
     * 企业信息
     * @param model
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String supplierEnterpriseInfo(ModelMap model , HttpServletRequest request) {
    	Admin admin = super.getCurrentAdmin(request);
    	model.addAttribute("supplier" , admin.getSupplier());
	   	model.addAttribute("industrys" , Supplier.Industry.values());
	   	model.addAttribute("admin", admin);
	   	model.addAttribute("nav", "corporateInfo");
    	return "/shop/member/corporateInfo/index";
    }
    
    /**
     * 更新企业信息
     * @param supplier 企业信息实体类
     * @param areaId 当前企业id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateEnterpriseInfo",method = RequestMethod.POST)
    public Map<String, Object> updateEnterpriseInfo(Supplier supplier,Long areaId) {
    	supplier.setArea(areaService.find(areaId));
    	supplierService.update(supplier, "admins","needs","supplyNeeds","goods","orders","businessCard","status","inviteCode","orderSetting","recommendFlag","favorCompany");
	   	Map<String, Object> model=new HashMap<>();
	   	model.put("isSuccess", true);
	   	return model;
    }
    
    
    /*
     * 修改时判断企业用户名是否重复
     */
    @ResponseBody
    @RequestMapping(value="/nameExists",method = RequestMethod.GET)
    public Map<String, Object> nameExists(Supplier supplier) {
    	Map<String, Object> model = new HashMap<>();
    	model.put("exists", supplierService.existaName(supplier.getName(), supplier.getId()));
    	return model;
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
    public JsonEntity sendSms(HttpServletRequest request ,String tel , BindPhoneSms.SmsType smsType) {
    	Admin admin = super.getCurrentAdmin(request);
    	if(tel == null) {
    		return new JsonEntity("101001" , "请输入绑定手机号");
    	}
    	//判断是否超过次数
    	boolean isOver = isOver(tel, admin , smsType);
    	if(isOver) {
    		return new JsonEntity("101002" , "每天只能发送:" + MAX_SEND) ;
    	}
    	String opt = RandomStringUtils.randomNumeric(6);
    	
    	bindPhoneSmsService.sendSms(tel, "验证码："+opt+"，请在2分钟内输入验证");
    	
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
    private static final Long EXPIRED_TIME = 2*60*1000L;
    
    private boolean isOver(String tel , Admin admin , BindPhoneSms.SmsType smsType) {
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setSmsType(smsType);
    	bindPhoneSms.setMobile(tel);
    	Long count = bindPhoneSmsService.countBindPhoneSms(bindPhoneSms, true);
    	return count > MAX_SEND ? true : false;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/checkSms" , method = RequestMethod.GET)
    public JsonEntity checkSms(HttpServletRequest request , String tel , String code) {
    	Admin admin = super.getCurrentAdmin(request);
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
    public JsonEntity replacePhoneNumber(String originalPhone , String originalCode , String newPhone , String newCode , HttpServletRequest request) {
    	Admin admin = this.getCurrentAdmin(request);
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
    	BindPhoneSms findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
    	
    	if(null == findSms || !originalCode.equals(findSms.getCode())) {
    		return new JsonEntity("101003" , "验证码错误");
    	}
    	if(!originalPhone.equals(findSms.getMobile())) {
    		return new JsonEntity("101004" , "手机号错误");
    	}
    	if(this.isExpired(findSms)) {
    		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findSms);
    		return new JsonEntity("101005" , "验证码过期");
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
    	bindPhoneSmsService.update(findSms);
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
    public boolean checkBindTel(String bindPhoneNum1){
        if (StringUtils.isEmpty(bindPhoneNum1)) {
            return false;
        }
        Admin admin = adminService.findBybindPhoneNum(bindPhoneNum1);
        return null == admin? true : false ;

    }
    
}
