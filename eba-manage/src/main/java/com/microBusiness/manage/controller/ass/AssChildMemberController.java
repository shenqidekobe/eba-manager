package com.microBusiness.manage.controller.ass;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BindPhoneSms;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Sms;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssGoodDirectoryService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assChildMemberController")
@RequestMapping("/ass/assChildMember")
public class AssChildMemberController extends BaseController{

	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private WeChatService weChatService;
	@Resource
    private AssChildMemberService assChildMemberService ;
	@Resource
    private SmsService smsService;
	@Resource
	private MemberService memberService;
	@Resource
	private SupplierService supplierService ;
	@Resource
	private BindPhoneSmsService bindPhoneSmsService;
	@Resource
	private AdminDao adminDao;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;
	@Resource
	private AssGoodDirectoryService assGoodDirectoryService;
	@Resource
	private CustomerRelationService customerRelationService;
	@Resource
	private NeedService needService;

	@Value("${ass.small.key}")
	private String assKey;
 
	@Value("${ass.small.secret}")
	private String assSecret;

	@ResponseBody
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public JsonEntity index(String iv, String encryptedData, String code, final HttpServletRequest request, final HttpServletResponse response, ModelMap model) {
	   if (StringUtils.isEmpty(encryptedData) || StringUtils.isEmpty(iv)|| StringUtils.isEmpty(code)) {
		  return new JsonEntity("010502" , "参数错误");
	   }
	   
	   // 获取sessionKey
	   Map<Object, Object> map = ApiSmallUtils.getAssInfo(code, assKey, assSecret);
	   if (map == null) {
		   return new JsonEntity("010503" , "获取sessionKey失败");
	   }
	   String sessionKey = map.get("sessionKey").toString();

	   // 获取用户基本信息
	   ChildMember childMember = ApiSmallUtils.getUserInfo(encryptedData, iv, sessionKey);

	   if (childMember == null) {
		   return new JsonEntity("010504" , "解析用户信息失败");
	   }

       // 通过unionId 获取用户信息
       AssChildMember userInfo = assChildMemberService.findByUnionId(childMember.getUnionId());

       Map<String , Object> retMap = new HashMap<>();
       
       retMap.put("unionId", childMember.getUnionId());
       retMap.put("nickName", childMember.getNickName());
       retMap.put("headImgUrl", childMember.getHeadImgUrl());
       
       // 新用户逻辑
       if (null == userInfo) {
    	  userInfo = new AssChildMember();
    	  userInfo.setUnionId(childMember.getUnionId());
    	  userInfo.setNickName(childMember.getNickName());
    	  userInfo.setHeadImgUrl(childMember.getHeadImgUrl());
    	  userInfo.setSmOpenId(childMember.getSmOpenId());
    	  userInfo.setSourceType(AssChildMember.SourceType.wx_small);
    	  userInfo.setBindFlag(AssChildMember.BindFlag.unbind);
          userInfo = assChildMemberService.save(userInfo);

          retMap.put("bindFlag", AssChildMember.BindFlag.unbind);
          return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),retMap);
       }
       
       retMap.put("bindFlag", userInfo.getBindFlag());

       return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),retMap);
    }

	@RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
	public JsonEntity get(String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		
		Map<String , Object> retMap = new HashMap<>();
	    retMap.put("id", assChildMember.getId());
	    retMap.put("name", assChildMember.getName());
	    retMap.put("phone", assChildMember.getPhone());
	    retMap.put("companyName", assChildMember.getCompanyName());
	    retMap.put("position", assChildMember.getPosition());
	    retMap.put("email", assChildMember.getEmail());
	    retMap.put("wxNum", assChildMember.getWxNum());
	    
	    return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),retMap);
	}
	
	/**
	 * 保存名片
	 * 
	 * @param assChildMember
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public JsonEntity update(AssChildMember assChildMember, HttpServletRequest request, HttpServletResponse response) {
		if (assChildMember == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		
		try {
			assChildMemberService.update(assChildMember, "openId", "member", "nickName", "headImgUrl", "unionId", "smOpenId", "assForm", "assShippingAddress", "sourceType", "bindFlag", "assCart");
			return JsonEntity.successMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code15000, Code.code15000.getDesc());
		}
		
	}
	
	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity sendSms(HttpServletRequest request , String tel) {

        //查询需要发送的手机号是否存在收货点主账号
        Member member = memberService.findByMobile(tel);

        if(null == member){
            return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        }

        //判断是否超过次数
        boolean isOver = isOver(tel , member);
        if(isOver){
            return new JsonEntity("101002" , "每天只能发送:" + MAX_SEND) ;
        }

        String opt = RandomStringUtils.randomNumeric(6);

        smsService.sendSms(tel , "验证码："+opt+"，请在2分钟内输入验证");

        Sms sms = new Sms();
        sms.setUserId(member.getId());
        sms.setType(Sms.SmsType.BINDING);
        sms.setSendTime(new Date());
        sms.setMobile(tel);
        sms.setCode(opt);
        sms.setStatus(Sms.Status.EFFECTIVE);
        smsService.save(sms);

        return JsonEntity.successMessage() ;
    }

	@RequestMapping(value = "/checkSms", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity checkSms(HttpServletRequest request , String unionId, String tel, String code) {
        Member member = memberService.findByMobile(tel);

        if(null == member){
            return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        }

        Sms sms = new Sms();

        sms.setUserId(member.getId());
        sms.setCode(code);
        sms.setType(Sms.SmsType.BINDING);
        sms.setStatus(Sms.Status.EFFECTIVE);
        Sms findSms = smsService.findSms(sms) ;

        if(null == findSms || !code.equals(findSms.getCode())){
            return new JsonEntity("101003" , "验证码错误");
        }

        if(!tel.equals(findSms.getMobile())){
            return new JsonEntity("101005" , "手机号错误");
        }
        //判断用户是否绑定过主账号了
        AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
        if(null != childMember.getMember()){
            return new JsonEntity("101006" , "该微信号已经绑定过收货点主账号");
        }

        if(this.isExpired(findSms)){
            findSms.setStatus(Sms.Status.EXPIRED);
            smsService.update(findSms) ;
            return new JsonEntity("101004" , "验证码过期");
        }

        findSms.setStatus(Sms.Status.USED);
        smsService.update(findSms) ;

        childMember.setMember(member);
        assChildMemberService.update(childMember) ;

        return JsonEntity.successMessage() ;
    }

   private static final Long MAX_SEND = 100L;
   private static final Long EXPIRED_TIME = 2*60*1000L;
   private boolean isOver(String tel , Member currMember){

	        Sms findSms = new Sms();
	        //findSms.setUserId(currMember.getId());
	        findSms.setType(Sms.SmsType.BINDING);
	        findSms.setMobile(tel);
	        Long count = smsService.countSms(findSms , true);

	        return count >= MAX_SEND ? true : false ;
	}

	/**
     * 判断是否过期
     * @return
     */
    private boolean isExpired(Sms sms){
        System.out.println(System.currentTimeMillis() - sms.getSendTime().getTime());
        if(System.currentTimeMillis() - sms.getSendTime().getTime() > EXPIRED_TIME){
            return true ;
        }
        return false ;
    }
	
    /**
     * 解除用户的绑定
     * @param request
     * @return
     */
    @RequestMapping(value = "/unBind", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity unBind(String unionId, HttpServletRequest request) {
        AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
        if(null == childMember.getMember()){
            return new JsonEntity("101011" , "您已经解除了绑定");
        }
        assChildMemberService.unBind(childMember) ;

        return JsonEntity.successMessage() ;
    }
    
    /**
     * 微商小管理分享助手绑定手机号-发送验证码
     * @param tel 手机号
     * @param smsType 验证码类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendVerifCode" , method = RequestMethod.POST)
    public JsonEntity sendVerifCode(String unionId , String tel) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
    	if(null == tel) {
    		return new JsonEntity(Code.code_assbindphome_100001 , Code.code_assbindphome_100001.getDesc());
    	}
    	Admin admin = adminDao.findBybindPhoneNum(tel);
    	if(null == admin || null == admin.getId()) {
    		return new JsonEntity(Code.code_assbindphome_100002 , Code.code_assbindphome_100002.getDesc());
    	}
    	AssChildMember queryChildMember = assChildMemberService.find(admin);
    	if(null != queryChildMember) {
    		return new JsonEntity(Code.code_assbindphome_100006 , Code.code_assbindphome_100006.getDesc());
    	}
    	String opt = RandomStringUtils.randomNumeric(6);
    	
    	bindPhoneSmsService.sendSms(tel, "验证码："+opt+"，请在2分钟内输入验证");
    	
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setAdminId(childMember.getId());
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.assBindPhoneNum);
    	bindPhoneSms.setSendTime(new Date());
    	bindPhoneSms.setMobile(tel);
    	bindPhoneSms.setCode(opt);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	bindPhoneSmsService.save(bindPhoneSms);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("adminId", admin.getId());
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 微商小管理分享助手-验证验证码是否正确
     * @param unionId
     * @param tel 手机号
     * @param code 验证码
     * @param adminId admin的id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkVerifCode" , method = RequestMethod.POST)
    public JsonEntity checkVerifCode(String unionId , String tel , String code , Long adminId) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
    	if(null == adminId) {
    		return new JsonEntity(Code.code13003 , Code.code13003.getDesc());
    	}
    	Admin admin = adminDao.find(adminId);
    	BindPhoneSms bindPhoneSms = new BindPhoneSms();
    	bindPhoneSms.setAdminId(childMember.getId());
    	bindPhoneSms.setCode(code);
    	bindPhoneSms.setSmsType(BindPhoneSms.SmsType.assBindPhoneNum);
    	bindPhoneSms.setStatus(BindPhoneSms.Status.EFFECTIVE);
    	BindPhoneSms findSms = bindPhoneSmsService.findBindPhoneSms(bindPhoneSms);
    	
    	if(null == findSms || !code.equals(findSms.getCode())) {
    		return new JsonEntity(Code.code_assbindphome_100003 , Code.code_assbindphome_100003.getDesc());
    	}
    	if(!tel.equals(findSms.getMobile())) {
    		return new JsonEntity(Code.code_assbindphome_100004 , Code.code_assbindphome_100004.getDesc());
    	}
    	if(this.VerifCodeIsExpired(findSms)) {
    		findSms.setStatus(BindPhoneSms.Status.EXPIRED);
    		bindPhoneSmsService.update(findSms);
    		return new JsonEntity(Code.code_assbindphome_100005 , Code.code_assbindphome_100005.getDesc());
    	}
    	childMember.setAdmin(admin);
    	assChildMemberService.update(childMember);
    	findSms.setStatus(BindPhoneSms.Status.USED);
    	bindPhoneSmsService.update(findSms);
    	
    	//更新操作提示
    	List<AssGoodDirectory> assGoodDirectories = assGoodDirectoryService.findList(childMember);
    	for(AssGoodDirectory goodDirectory : assGoodDirectories) {
    		AssUpdateTips updateTips = new AssUpdateTips();
    		updateTips.setAssChildMember(childMember);
    		updateTips.setAssGoodDirectory(goodDirectory);
    		updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
    		updateTips.setType(AssUpdateTips.Type.companyGoods);
    		assUpdateTipsService.save(updateTips);
    	}
    	List<CustomerRelation> customerRelations = customerRelationService.findList(childMember);
    	for(CustomerRelation relation : customerRelations) {
    		AssUpdateTips updateTips = new AssUpdateTips();
    		updateTips.setAssChildMember(childMember);
    		updateTips.setCustomerRelation(relation);
    		updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
    		updateTips.setType(AssUpdateTips.Type.businessCustomers);
    		assUpdateTipsService.save(updateTips);
    	}
    	List<Need> needs = needService.findList(childMember);
    	for(Need need : needs) {
    		AssUpdateTips updateTips = new AssUpdateTips();
    		updateTips.setAssChildMember(childMember);
    		updateTips.setNeed(need);
    		updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
    		updateTips.setType(AssUpdateTips.Type.individualCustomers);
    		assUpdateTipsService.save(updateTips);
    	}
    	return JsonEntity.successMessage();
    }
    
    /**
     * 判断验证码是否过期
     * @param bindPhoneSms
     * @return
     */
    private boolean VerifCodeIsExpired(BindPhoneSms bindPhoneSms) {
    	System.out.println(System.currentTimeMillis() - bindPhoneSms.getSendTime().getTime());
    	if(System.currentTimeMillis() - bindPhoneSms.getSendTime().getTime() > EXPIRED_TIME) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 微商小管理分享助手-解绑手机号
     * @param unionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unbundledPhone" , method = RequestMethod.POST)
    public JsonEntity unbundledPhone(String unionId) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
    	childMember.setAdmin(null);
    	assChildMemberService.update(childMember);
    	//删除更新操作提示
    	List<AssUpdateTips> assUpdateTips = assUpdateTipsService.findList(childMember, null, null,null);
    	for(AssUpdateTips updateTips : assUpdateTips) {
    		assUpdateTipsService.delete(updateTips);
    	}
    	
    	return JsonEntity.successMessage();
    }
    
    /**
     * 获取个人信息
     * @param unionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/personalInfo" , method = RequestMethod.GET)
    public JsonEntity personalInfo(String unionId) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
    	List<AssUpdateTips> updateTips = assUpdateTipsService.findList(childMember, null, AssUpdateTips.WhetherUpdate.yes, AssUpdateTips.Type.companyGoods);
    	List<Integer> types = new ArrayList<Integer>();
    	types.add(AssUpdateTips.Type.businessCustomers.ordinal());
    	types.add(AssUpdateTips.Type.individualCustomers.ordinal());
    	List<AssUpdateTips> updateTips2 = assUpdateTipsService.findList(childMember, AssUpdateTips.WhetherUpdate.yes, types);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Admin admin = childMember.getAdmin();
    	if(null != admin && admin.getId() != null) {
    		resultMap.put("exist", true);
    		resultMap.put("tel", admin.getBindPhoneNum());
    		if(updateTips.size() > 0) {
    			resultMap.put("updateTips", true);
    		}else {
    			resultMap.put("updateTips", false);
    		}
    		if(updateTips2.size() > 0) {
    			resultMap.put("customerTips", true);
    		}else {
    			resultMap.put("customerTips", false);
    		}
    	}else {
    		resultMap.put("exist", false);
    		resultMap.put("tel", "");
    	}
    	return JsonEntity.successMessage(resultMap);
    }
    
}
