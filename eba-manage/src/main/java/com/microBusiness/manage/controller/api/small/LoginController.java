package com.microBusiness.manage.controller.api.small;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Sms;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.SmsService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.Constant;

@Controller("smallLoginController")
@RequestMapping("/api/small/login")
public class LoginController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private WeChatService weChatService;
	@Resource
    private ChildMemberService childMemberService ;
	@Resource
    private SmsService smsService;
	@Resource
	private MemberService memberService;
	@Resource
	private SupplierService supplierService ;
	
	@Resource(name = "proxyUserServiceImpl")
	private ProxyUserService proxyUserService;
	
	@Value("${childMember.template.common.templateId}")
	private String templateId;

	
	/**
	 * 用户进入小程序
	 * 通过code换取openId和sessionKey  smOpenId即为用户的微信ID
	 * parentOpenId记录用户推荐人，保存用户和关系
	 * */
	@ResponseBody
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public JsonEntity index(String iv, String encryptedData, String code, 
			final HttpServletRequest request, final HttpServletResponse response, ModelMap model,
			String parentOpenId, String openId) {
	   logger.info("【进去小程序登录】。。。parentOpenId:" + parentOpenId + "...openId:" + openId);
	   if (StringUtils.isEmpty(code)) {
		  return new JsonEntity("010502" , "参数错误");
	   }
	   // 获取sessionKey
	   Map<Object, Object> map = ApiSmallUtils.getInfo(code);
	   if (map == null) {
		   return new JsonEntity("010503" , "获取sessionKey失败");
	   }
	   String sessionKey = map.get("sessionKey").toString();
	   
	   String smOpenId = map.get("openId").toString();
	   logger.info("【smOpenId】：" + smOpenId);
	   if (smOpenId == null) {
		   return new JsonEntity(Code.code_smOpenId_null_99899 , "获取smOpenId息失败");
	   }
	   Map<Object, Object> result = new HashMap<>();
	   ChildMember childMember = null; //通过openId 获取用户信息
	   if(StringUtils.isNotBlank(openId)){
		   childMember = childMemberService.findByOpenId(openId);//必须获取到公众号openId
	   }else{
		   childMember = childMemberService.findBySmOpenId(smOpenId);
	   }
       if(childMember == null){
    	   childMember = new ChildMember();
    	   childMember.setOpenId(openId);
    	   logger.info("【smOpenId】：" + smOpenId);
           childMember.setSmOpenId(smOpenId);
           childMember.setIsChecked(false);
           childMember.setSourceType(ChildMember.SourceType.wx_small);
           //如果是分享，更新为下属
           if(StringUtils.isNotEmpty(parentOpenId)&&!parentOpenId.equals(smOpenId)
        		   &&childMember.getParent()==null){
         	  ChildMember parent = childMemberService.findBySmOpenId(parentOpenId);
         	  childMember.setParent(parent);
         	  //weChatService.sendTemplateMessage2ChildMemberJoin(childMember, templateId, weChatService.getGlobalToken());
           }
           childMemberService.saveChildMember(childMember);
       }else{
    	   if(StringUtils.isEmpty(childMember.getOpenId()) || StringUtils.isEmpty(childMember.getSmOpenId())){
    		   childMember.setOpenId(openId);
        	   logger.info("【smOpenId】：" + smOpenId);
               childMember.setSmOpenId(smOpenId);
               //如果是分享，更新为下属
               if(StringUtils.isNotEmpty(parentOpenId)&&!parentOpenId.equals(smOpenId)
            		   &&childMember.getParent()!=null){
            	   ChildMember parent = childMemberService.findBySmOpenId(parentOpenId);
             	   childMember.setParent(parent);
             	   //weChatService.sendTemplateMessage2ChildMemberJoin(childMember, templateId, weChatService.getGlobalToken());
               }
         	  childMemberService.saveChildMember(childMember);
    	   }
    	   
       }
	   result.put("sessionKey", sessionKey);
       result.put("smOpenId", smOpenId);
       result.put("openId", childMember.getOpenId());
       result.put("unionId", childMember.getUnionId());
       result.put("flag", true);
       result.put("isShoper", childMember.getIsShoper());
       result.put("nickName", childMember.getNickName());
       result.put("proxyUserId", getProxyUserId(childMember.getSmOpenId()));
       
       return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), result);
    }
	
	
	private  Long getProxyUserId(String smOpenId){
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("childMember");
		filter.setValue(childMember.getId());
		filters.add(filter);
		List<ProxyUser> proxyUserList = proxyUserService.findList(1, filters, null);
		if(proxyUserList != null && !proxyUserList.isEmpty()){
			return proxyUserList.get(0).getId();
		}
		return null;
	}

	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity sendSms(HttpServletRequest request , String tel) {

        //查询需要发送的手机号是否存在收货点主账号
        Member member = memberService.findByMobile(tel);

        if(null == member){
//          return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        	member=memberService.addMember(tel);
        }
        
        //假手机号不发短信
        if(StringUtils.isNotEmpty(tel)){
        	if(tel.startsWith(Constant.tel_pre_num2)){
        		return new JsonEntity("" , "该号码不支持验证码发送") ; 
        	}
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
        	member=memberService.addMember(tel);
        }

        Sms sms = new Sms();

        sms.setUserId(member.getId());
        sms.setCode(code);
        sms.setType(Sms.SmsType.BINDING);
        sms.setStatus(Sms.Status.EFFECTIVE);
        
        Sms findSms = null;
        //假手机号不发短信
    	if(tel.startsWith(Constant.tel_pre_num2)){
    		findSms = new Sms();
    		String code2 =  CommonUtils.getInnerCheckCode(tel);
    		findSms.setCode(code2);
    		findSms.setMobile(tel);
        }else{
        	findSms = smsService.findSms(sms) ;
        }

        if(null == findSms || !code.equals(findSms.getCode())){
            return new JsonEntity("101003" , "验证码错误");
        }

        if(!tel.equals(findSms.getMobile())){
            return new JsonEntity("101005" , "手机号错误");
        }
        //判断用户是否绑定过主账号了
        ChildMember childMember = childMemberService.findByUnionId(unionId);
        if(null != childMember.getMember()){
            return new JsonEntity("101006" , "该微信号已经绑定过收货点主账号");
        }

        /* Need need = needService.findByTel(tel);
        if(null == need){
            return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        }*/
        if(tel.startsWith(Constant.tel_pre_num1)){
        	if(this.isExpired(findSms)){
                findSms.setStatus(Sms.Status.EXPIRED);
                smsService.update(findSms) ;
                return new JsonEntity("101004" , "验证码过期");
            }
        	findSms.setStatus(Sms.Status.USED);
            smsService.update(findSms) ;
        }
        

        childMember.setMember(member);
        childMemberService.update(childMember) ;

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

   public static String getResponse(String serverUrl){  
        StringBuffer result = new StringBuffer();  
        try {  
            URL url = new URL(serverUrl);  
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
  
            String line;  
            while((line = in.readLine()) != null){  
                result.append(line);  
            }  
            in.close();  
  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return result.toString();  
    }

	@SuppressWarnings("rawtypes")
	public static String getResponse(String serverUrl, Map map){
		PrintWriter out = null;
        StringBuffer result = new StringBuffer();  
        try {  
            URL url = new URL(serverUrl);  
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);  
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(map);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
            String line;
            while((line = in.readLine()) != null){  
                result.append(line);  
            }  
            in.close();  
            out.close();
            
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return result.toString();  
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
	 * 获取正式供应商列表
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/formal")
	@ResponseBody
	public JsonEntity formalSupplier(HttpServletRequest request, HttpServletResponse response , Pageable pageable, String unionId){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

		try {
			//获取当前收货点用户
			Member member = childMemberService.findByUnionId(unionId).getMember();
			List<SupplierDto> supplyList = supplierService.findSupplierListByMember(member,null);
			for (SupplierDto dto : supplyList) {
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("id", dto.getSupplier().getId());
    			map.put("name", dto.getSupplier().getName());
    			map.put("address", dto.getSupplier().getAddress());
    			map.put("tel", dto.getSupplier().getTel());
    			map.put("userName", dto.getSupplier().getUserName());
    			map.put("imagelogo", dto.getSupplier().getImagelogo());
            	resultList.add(map);
			}
//			List<Supplier> suppliers = supplierService.findFormal(need , pageable) ;
//			List<Map<String , Object>> retSuppliers = new ArrayList<>();
//			for(final Supplier supplier : suppliers){
//				retSuppliers.add(new HashMap<String , Object>(){{
//					this.put("id", supplier.getId());
//					this.put("name", supplier.getName());
//					this.put("address", supplier.getAddress());
//					this.put("tel", supplier.getTel());
//					this.put("userName", supplier.getUserName());
//					this.put("imagelogo", supplier.getImagelogo());
//				}});
//			}

			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), resultList);

		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code010401, Code.code010401.getDesc());
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/updateUserinfo", method = RequestMethod.GET)
	public JsonEntity updateUserinfo(String smOpenId, String nickName, String headPic) {
	   ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
	   childMember.setHeadImgUrl(headPic);
	   childMember.setNickName(nickName);
	   childMemberService.update(childMember);
       return JsonEntity.successMessage();
    }

	
	/**public void getPublicOpenId(){
        String url = "http://test.dinghuo.me/api/small/login/getPublicOpenId.jhtml";
        //对url进行URLEncode转换
        String strUrl = null;
		try {
			strUrl = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String jumpPath = weChatService.getCode(strUrl);
		logger.info("getPublicOpenId in=======================================");
        try {
        	WebUtils.get(jumpPath, new HashMap<String, Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPublicOpenId", method = RequestMethod.GET)
	public JsonEntity getPublicOpenId(String code, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		logger.info("getPublicOpenId  code is " + code); 
		//获取到code后的正常逻辑
        Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
        if (null == userMap) {
        	logger.info("getPublicOpenId userMap is null!");
            return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), null);
        }
        String openId = userMap.get("openid");
        String unionId = userMap.get("unionId");
        logger.info("getPublicOpenId openId:{}", openId);
        logger.info("getPublicOpenId unionId:{}", unionId);
        if(StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(unionId)){
        	ChildMember userInfo = childMemberService.findByUnionId(unionId);
        	userInfo.setOpenId(openId);
        	childMemberService.update(userInfo);
        }
        return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), null);
	}**/
}
