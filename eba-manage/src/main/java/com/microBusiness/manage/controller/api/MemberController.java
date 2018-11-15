package com.microBusiness.manage.controller.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Error;
import com.microBusiness.manage.entity.Need.NeedStatus;
import com.microBusiness.manage.service.*;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.CommonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.CookieUtil;
import com.microBusiness.manage.util.DateformatEnum;

/**
 * Created by afei.
 * User: afei
 * Date: 2016/5/26 10:06
 * Describe:
 * Update:
 */
@Controller
@RequestMapping("/api/member")
public class MemberController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private WeChatService weChatService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource
    private MemberService memberService;

    @Resource
    private NeedService needService ;
    @Resource
    private SmsService smsService ;

    @Resource
    private CartService cartService ;
    
    @Resource
    private OrderService orderService;

    @Resource
    private ChildMemberService childMemberService ;

    /**
     * 前端用户登录
     * 通过微信用户code登录获取openId和sessionKey
     * 通过openId再获取微信用户信息、保存数据库
     * parentOpenId记录当前登录用户的推荐人
     * */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
            String code = null;
            String shareUrl = request.getParameter("url");
            if(shareUrl != null){
            	shareUrl = shareUrl.trim();
            }
            String parentOpenId = request.getParameter("parentOpenId");
            logger.info("【shareUrl】：" + shareUrl);
            logger.info("【parentOpenId】：" + parentOpenId);
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            code = request.getParameter(Constant.MAP_KEY);

            if (StringUtils.isEmpty(code)) {
                //获取code值
                String jumpPath = weChatService.getCode(strUrl);
                response.sendRedirect(jumpPath);
                return;
            }

            //获取到code后的正常逻辑
            Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
            if (null == userMap) {
                //通过接口获取到的用户信息异常
                //response.sendRedirect("/b2b/error.html");
            	logger.error("通过接口获取到的用户信息异常!");
                return;
            }

            /**
             * 业务修改，原先使用openId 来确认用户，现在使用 unionId 来区分用户
             * unionId 只要在接入了微信开发平台才会返回
             */
             String openId = userMap.get("openid");
             
             ChildMember userInfo = childMemberService.findByOpenId(openId);
             if(null == userInfo){
                 String globalToken = weChatService.getGlobalToken();
                 logger.info("【globalToken】:{}", globalToken);
                 if(globalToken != null){
                	 //通过微信接口获取用户信息，可能获取用户信息失败
                     userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                 }
                
                 //插入数据库
                 if (null == userInfo) {
                	 userInfo = new ChildMember();
                 }
                 logger.info("【公众号获取 昵称】:{}", userInfo.getNickName());
            	 userInfo.setOpenId(openId);
                 userInfo.setSourceType(ChildMember.SourceType.wx_public);
                 userInfo.setIsChecked(false);
                 userInfo = childMemberService.save(userInfo);
             }else{
            	 if(StringUtils.isEmpty(userInfo.getNickName())){
            		 String globalToken = weChatService.getGlobalToken();
                     logger.info("【globalToken】:{}", globalToken);
                     if(globalToken != null){
                    	 //通过微信接口获取用户信息，可能获取用户信息失败
                         ChildMember tmpinfo = weChatService.getChildFromUnionId(userMap, globalToken);
                         if(tmpinfo != null){
                        	 userInfo.setNickName(tmpinfo.getNickName());
                             userInfo.setHeadImgUrl(tmpinfo.getHeadImgUrl());
                         }
                     }
                     //更新数据库
                     logger.info("【公众号获取 昵称】:{}", userInfo.getNickName());
                     userInfo = childMemberService.update(userInfo);
            	 }
            	 
             }

            //获取用户信息异常
            if (null == userInfo) {
            	logger.error("通过接口获取到的用户信息异常!");
                return;
            }

//            if(!userInfo.getOpenId().equals(openId) 
//            		&& userInfo.getSourceType().equals(ChildMember.SourceType.wx_public)){
//                 //存在的用户和之前的openid 不一样的话，进行更新
//                 logger.info("update user openId , old : {} , new : {}" , userInfo.getOpenId() , openId);
//                 userInfo.setOpenId(openId);
//            }

            /*String globalToken = weChatService.getGlobalToken();
            logger.info("globalToken:{}", globalToken);
            //通过微信接口获取用户信息，可能获取用户信息失败
            ChildMember weChatUser = weChatService.getChildFromUnionId(userMap, globalToken);
            //通过接口获取用户信息失败
            if(null == weChatUser){
                response.sendRedirect("/b2b/error.html");
                return;
            }

            //判断unionId是否存在
            String unionId = weChatUser.getUnionId() ;
            if(StringUtils.isEmpty(unionId)){
                logger.error("unionId is null");
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //通过unionId 获取用户信息
            ChildMember userInfo = childMemberService.findByUnionId(unionId);

            //数据库中不存在
            if(null == userInfo){
                weChatUser.setSourceType(ChildMember.SourceType.wx_public);
                userInfo = childMemberService.save(weChatUser);
            }*/
            
          Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_OPEN_ID_NAME);
          //判断cookie是否为空且token值是否为空
          if (null != cookie) {
              //刷新token的值
              CookieUtil.editCookie(response, request, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
          } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
              CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
          }
          
          Cookie cookieurl = CookieUtil.getCookieByName(request, "shareUrl");
          //判断cookie是否为空且token值是否为空
          if (null != cookieurl) {
              //刷新token的值
              CookieUtil.editCookie(response, request, "shareUrl", shareUrl, "/", 60 * 60 * 24 * 30);
          } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
              CookieUtil.addCookie(response, "shareUrl", shareUrl, "/", 60 * 60 * 24 * 30);
          }
          
          logger.info("【parentOpenId】：" + parentOpenId);
          Cookie cookieparentOpenId = CookieUtil.getCookieByName(request, "parentOpenId");
          //判断cookie是否为空且token值是否为空
          if (null != cookieparentOpenId) {
              //刷新token的值
              CookieUtil.editCookie(response, request, "parentOpenId", parentOpenId, "/", 60 * 60 * 24 * 30);
          } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
              CookieUtil.addCookie(response, "parentOpenId", parentOpenId, "/", 60 * 60 * 24 * 30);
          }
            logger.info("https://shcxnc.com/b2b/activation2.html...");
            response.sendRedirect("https://shcxnc.com/b2b/activation2.html");

           /* //判断用户是否已经存在,如果有数据库和缓存里有有用户信息的话，表示用户已经注册过
            ChildMember userInfo = childMemberService.findByOpenId(openId);
            //新用户逻辑
            if (null == userInfo) {
                String globalToken = weChatService.getGlobalToken();
                logger.info("globalToken:{}", globalToken);
                //通过微信接口获取用户信息，可能获取用户信息失败
                userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                //接口数据异常判断
                if (null == userInfo) {
                    //通过接口获取到的用户信息异常
                    response.sendRedirect("/b2b/error.html");
                    return;
                }

                //插入数据库
                userInfo = childMemberService.save(userInfo);

                *//*
                Cart cart = new Cart();
                //cart.setKey();
                cart.setMember(userInfo);
                cart.setKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
                cart.setExpire(DateUtils.addSeconds(new Date(), Cart.TIMEOUT));

                cartService.save(cart);*//*

                //新增名为token的cookie值
                CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            }
            //获取用户信息异常
            if (null == userInfo) {
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //token 保存到cookie
            Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_OPEN_ID_NAME);
            //判断cookie是否为空且token值是否为空
            if (null != cookie) {
                //刷新token的值
                CookieUtil.editCookie(response, request, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
                CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            }

            if(null == userInfo.getMember()*//* && super.needCheckMobile(shareUrl)*//*){
                String backUrl = StringUtils.isEmpty(shareUrl) ? "/b2b/supplier.html":shareUrl ;
                response.sendRedirect("/b2b/binding.html?backurl=" + backUrl);
                return;
            }
            Need need = userInfo.getMember().getNeed();
            if(need.getNeedStatus() == NeedStatus.suspend) {
            	response.sendRedirect("/b2b/error.html?status="+Error.needSuspend);
            	return;
            }
            *//*
             * 判断当前的账号是否认证，如果未认证，则判断是否过试用期，过了试用期不能操作
             *//*
            Supplier supplier = need.getSupplier();
            if(supplier.isProbation()) {
            	if(supplier.isExpired()) {
            		response.sendRedirect("/b2b/error.html?status="+Error.pastTrialPeriod);
            		return;
            	}
            }

            if (StringUtils.isNotEmpty(shareUrl)) {
                response.sendRedirect(shareUrl);
                return;
            }
            response.sendRedirect("/b2b/supplier.html");*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 前端用户登录
     * 通过微信用户code登录获取openId和sessionKey
     * 通过openId再获取微信用户信息、保存数据库
     * parentOpenId记录当前登录用户的推荐人
     * */
    @ResponseBody
    @RequestMapping(value = "/login2", method = RequestMethod.GET)
    public Map<String,String> login2(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	Map<String,String> map=new HashMap<>();
    	try {
            String code = null;
            String shareUrl = request.getParameter("url");
            if(shareUrl != null){
            	shareUrl = shareUrl.trim();
            }
            String parentOpenId = request.getParameter("parentOpenId");
            logger.info("【shareUrl】：" + shareUrl);
            logger.info("【parentOpenId】：" + parentOpenId);
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            code = request.getParameter(Constant.MAP_KEY);
            if (StringUtils.isEmpty(code)) {
                //获取code值
                String jumpPath = weChatService.getCode(strUrl);
                response.sendRedirect(jumpPath);
                return map;
            }
            Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
            if (null == userMap) {
            	logger.error("通过接口获取到的用户信息异常!");
                return map;
            }
             String unionId = userMap.get("unionId");
             String openId = userMap.get("openid");
             ChildMember userInfo = childMemberService.findByOpenId(openId);
             if(null == userInfo){
                 String globalToken = weChatService.getGlobalToken();
                 logger.info("【globalToken】:{}", globalToken);
                 if(globalToken != null){
                	 //通过微信接口获取用户信息，可能获取用户信息失败
                     userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                 }
                 if (null == userInfo) {
                	 userInfo = new ChildMember();
                 }
                 logger.info("【公众号获取 昵称】:{}", userInfo.getNickName());
            	 userInfo.setOpenId(openId);
                 userInfo.setSourceType(ChildMember.SourceType.wx_public);
                 userInfo.setIsChecked(false);
                 userInfo.setUnionId(unionId);
                 userInfo = childMemberService.save(userInfo);
             }else{
            	 if(StringUtils.isEmpty(userInfo.getNickName())){
            		 String globalToken = weChatService.getGlobalToken();
                     logger.info("【globalToken】:{}", globalToken);
                     if(globalToken != null){
                    	 //通过微信接口获取用户信息，可能获取用户信息失败
                         ChildMember tmpinfo = weChatService.getChildFromUnionId(userMap, globalToken);
                         if(tmpinfo != null){
                        	 userInfo.setNickName(tmpinfo.getNickName());
                             userInfo.setHeadImgUrl(tmpinfo.getHeadImgUrl());
                         }
                     }
                     //更新数据库
                     logger.info("【公众号获取 昵称】:{}", userInfo.getNickName());
                     userInfo = childMemberService.update(userInfo);
            	 }
             }
            //获取用户信息异常
            if (null == userInfo) {
            	logger.error("通过接口获取到的用户信息异常!");
                return map;
            }
            map.put("openId", openId);
            map.put("parentOpenId", parentOpenId);
            map.put("shareUrl", shareUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return map;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {

        return "/card/excard/index";
    }


    @RequestMapping(value = "/sendSms", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity sendSms(HttpServletRequest request , String tel) {

        //查询需要发送的手机号是否存在收货点主账号
        Member member = memberService.findByMobile(tel);

        if(null == member){
            return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        }
        
        //判断收货点是否已经关闭
        /**if(member.getNeed().getNeedStatus() == NeedStatus.suspend) {
        	return new JsonEntity("101003" , "该收货点已关闭!");
        }*/

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
    public JsonEntity checkSms(HttpServletRequest request , String tel , String code) {
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
        ChildMember childMember = super.getCurrChildMem(request);

        if(null != childMember.getMember()){
            return new JsonEntity("101006" , "该微信号已经绑定过收货点主账号");
        }

       /* Need need = needService.findByTel(tel);
        if(null == need){
            return new JsonEntity("101001" , "请核对手机号是否绑定收货点！");
        }*/

        if(this.isExpired(findSms)){
            findSms.setStatus(Sms.Status.EXPIRED);
            smsService.update(findSms) ;
            return new JsonEntity("101004" , "验证码过期");
        }

        findSms.setStatus(Sms.Status.USED);
        smsService.update(findSms) ;

        childMember.setMember(member);
        childMemberService.update(childMember) ;

        return JsonEntity.successMessage() ;
    }

    private static final Long MAX_SEND = 3L;
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
    
    
    @RequestMapping(value = "/orderList", method = RequestMethod.GET)
    public @ResponseBody 
    		JsonEntity orderList(Pageable pageable, Integer status,
    				HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
    	Member member = this.getUserInfo(request);
		Order.Status orderStatus = Order.convertIntegerToOrderStatus(status);

		Page<Order> page = orderService.findPage(null, orderStatus,null, member, null, null, null, null, null, null, null, pageable, null , null , null , null , null, null);
		if(page != null){
			List<Map<String, Object>> orderItemList = null;
			for (Order order : page.getContent()) {
				orderItemList = new ArrayList<Map<String, Object>>();
				Map<String, Object> orderMap = new HashMap<String, Object>();
				orderMap.put("orderId", order.getId());
				orderMap.put("supplierName", order.getSupplier().getName());
				orderMap.put("sn", order.getSn());
				orderMap.put("status", order.getStatus().ordinal());
                orderMap.put("supplierId" , order.getSupplier().getId()) ;
				List<OrderItem> itemsList = order.getOrderItems();
				for (OrderItem orderItem : itemsList) {
					Map<String, Object> orderItemMap = new HashMap<String, Object>();
					orderItemMap.put("goodsName", orderItem.getName());
					orderItemMap.put("img", orderItem.getProduct().getImage());
					orderItemList.add(orderItemMap);
				}
                orderMap.put("quantity" , order.getQuantity());
				orderMap.put("orderItems", orderItemList);
                orderMap.put("supplyType" , order.getSupplyType());
				orderList.add(orderMap);
			}
		}
		resultMap.put("orders", orderList);
		return JsonEntity.successMessage(resultMap);
		
	}

    /**
     * 解除用户的绑定
     * @param request
     * @return
     */
    @RequestMapping(value = "/unBind", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity unBind(HttpServletRequest request) {
        ChildMember childMember = super.getCurrChildMem(request);
        if(null == childMember.getMember()){
            return new JsonEntity("101011" , "您已经解除了绑定");
        }
        childMemberService.unBind(childMember) ;

        return JsonEntity.successMessage() ;
    }

    /**
     * 修改用户名
     * @param request
     * @param childMember
     * @return
     */
    @RequestMapping(value = "/nickName", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity updateNickName(HttpServletRequest request , ChildMember childMember) {

        ChildMember currChildMem = super.getCurrChildMem(request);

        if(null == childMember || null == childMember.getNickName()){
            return new JsonEntity(Code.code019998);
        }

        childMemberService.updateChildMember(childMember , currChildMem) ;

        Map<String , Object> retMap = new HashMap<>() ;

        retMap.put("nickName" , currChildMem.getNickName()) ;
        retMap.put("tel" , CommonUtils.getHiddenTel(currChildMem.getMember().getMobile())) ;

        return JsonEntity.successMessage(retMap) ;
    }


    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity get(HttpServletRequest request) {

        ChildMember currChildMem = super.getCurrChildMem(request);

        Map<String , Object> retMap = new HashMap<>() ;

        retMap.put("nickName" , currChildMem.getNickName()) ;
        retMap.put("tel" , CommonUtils.getHiddenTel(currChildMem.getMember().getMobile())) ;

        return JsonEntity.successMessage(retMap) ;
    }

    @ResponseBody
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public void check(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
            Long start = System.currentTimeMillis();
            String code = null;
            String shareUrl = request.getParameter("url");
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            Map map = request.getParameterMap();
            code = request.getParameter(Constant.MAP_KEY);

            if (null == code || code.equals("")) {
                //获取code值
                String jumpPath = weChatService.getCode(strUrl);
                //CookieUtil.addCookie(response, "wxShareUrl", reqUrl, "/", 60 * 60 );
                response.sendRedirect(jumpPath);
                return;
            }

            //获取到code后的正常逻辑
            Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
            if (null == userMap) {
                //通过接口获取到的用户信息异常
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //获取到 openId
            String openId = userMap.get("openid");
            //判断用户是否已经存在,如果有数据库和缓存里有有用户信息的话，表示用户已经注册过
            ChildMember userInfo = childMemberService.findByOpenId(openId);
            if(userInfo != null){
            	response.sendRedirect("/api/member/login.jhtml?url=" + shareUrl);
            	return;
            }
            
            //新用户逻辑
            if (null == userInfo) {
                String globalToken = weChatService.getGlobalToken();
                logger.info("globalToken:{}", globalToken);
                //通过微信接口获取用户信息，可能获取用户信息失败
                userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                //接口数据异常判断
                if (null == userInfo) {
                    //通过接口获取到的用户信息异常
                    response.sendRedirect("/b2b/error.html");
                    return;
                }

                //插入数据库
                //userInfo = childMemberService.save(userInfo);

                //新增名为token的cookie值
                CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            }
            //获取用户信息异常
            if (null == userInfo) {
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //token 保存到cookie
            Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_OPEN_ID_NAME);
            //判断cookie是否为空且token值是否为空
            if (null != cookie) {
                //刷新token的值
                CookieUtil.editCookie(response, request, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
                CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            }

            response.sendRedirect("/api/member/checkupdate.jhtml?url=" + shareUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/checkupdate", method = RequestMethod.GET)
    public void checkupdate(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
            Long start = System.currentTimeMillis();
            String code = null;
            String shareUrl = request.getParameter("url");
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            Map map = request.getParameterMap();
            code = request.getParameter(Constant.MAP_KEY);

            if (null == code || code.equals("")) {
                //获取code值
                String jumpPath = weChatService.getCode2(strUrl);
                //CookieUtil.addCookie(response, "wxShareUrl", reqUrl, "/", 60 * 60 );
                response.sendRedirect(jumpPath);
                return;
            }

            //获取到code后的正常逻辑
            Map<String, String> userMap = weChatService.getWebAuthAccessToken2(code);
            if (null == userMap) {
                //通过接口获取到的用户信息异常
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //获取到 openId
            String openId = userMap.get("openid");
            //判断用户是否已经存在,如果有数据库和缓存里有有用户信息的话，表示用户已经注册过
            ChildMember userInfo = childMemberService.findByOpenId(openId);
            //新用户逻辑
            if (null == userInfo) {
                String globalToken = weChatService.getGlobalToken2();
                logger.info("globalToken:{}", globalToken);
                //通过微信接口获取用户信息，可能获取用户信息失败
                userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                //接口数据异常判断
                if (null == userInfo) {
                    //通过接口获取到的用户信息异常
                    response.sendRedirect("/b2b/error.html");
                    return;
                }

            }
            //获取用户信息异常
            if (null == userInfo) {
                response.sendRedirect("/b2b/error.html");
                return;
            }
            //token 保存到cookie
            Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_OPEN_ID_NAME);
            if(cookie != null && userInfo.getId() != null){
            	String newOpenId = cookie.getValue();
            	userInfo.setOpenId(newOpenId);
            	userInfo = childMemberService.update(userInfo);
            }else{
            	response.sendRedirect("/api/member/login.jhtml?url=" + shareUrl);
            	return;
            }
            
            
            //判断cookie是否为空且token值是否为空
            if (null != cookie) {
                //刷新token的值
                //CookieUtil.editCookie(response, request, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
                //CookieUtil.addCookie(response, Constant.COOKIE_OPEN_ID_NAME, openId, "/", 60 * 60 * 24 * 30);
            }
            //response.sendRedirect("/b2b/getCheck.html?openId=" + openId);
            //response.sendRedirect("/b2b/getCheck.html?openId=" + cookie.getValue()  + "&openId2=" + openId);
            
          //判断unionId是否存在
            String unionId = userInfo.getUnionId() ;
            if(StringUtils.isEmpty(unionId)){
                logger.error("unionId is null");
                response.sendRedirect("/b2b/error.html");
                return;
            }
            
            //token 保存到cookie
            cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_UNION_ID_NAME);
            //判断cookie是否为空且token值是否为空
            if (null != cookie) {
                //刷新token的值
                CookieUtil.editCookie(response, request, Constant.COOKIE_UNION_ID_NAME, unionId, "/", 60 * 60 * 24 * 30);
            } else {//cookie为空或者token值为空，则新增cookie，并将微信用户信息插入缓存
                CookieUtil.addCookie(response, Constant.COOKIE_UNION_ID_NAME, unionId, "/", 60 * 60 * 24 * 30);
            }
            
            if(null == userInfo.getMember()/* && super.needCheckMobile(shareUrl)*/){
                String backUrl = StringUtils.isEmpty(shareUrl) ? "/b2b/supplier.html":shareUrl ;
                response.sendRedirect("/b2b/binding.html?backurl=" + backUrl);
                return;
            }
            /**Need need = userInfo.getMember().getNeed();
            if(need.getNeedStatus() == NeedStatus.suspend) {
            	response.sendRedirect("/b2b/error.html?status="+Error.needSuspend);
            	return;
            }*/

            if (StringUtils.isNotEmpty(shareUrl)) {
                response.sendRedirect(shareUrl);
                return;
            }
            response.sendRedirect("/b2b/supplier.html");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 迁移通知老公众号
     * @param request
     * @param response
     * @param model
     */
//    @ResponseBody
//    @RequestMapping(value = "/sendMoveNotice", method = RequestMethod.GET)
//    public void sendMoveNotice(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//    	TemplateInfo templateInfo = new TemplateInfo();
//		templateInfo.setTemplateId("pWNenU2txu_n3KsQ882YOQU2QFLq0PniH0S4j0XSfUc");
//		templateInfo.setData(new HashMap<String, Map<String, String>>(){{
//
//			this.put("first" , new HashMap<String, String>(){{
//				this.put("value", "公众号迁移通知");
//			}});
//
//			this.put("keyword1", new HashMap<String, String>() {{
//				this.put("value", "微商小管理");
//			}});
//
//			this.put("keyword2", new HashMap<String, String>() {{
//				this.put("value", "公众号迁移");
//			}});
//
//			this.put("remark", new HashMap<String, String>() {{
//				this.put("value", "为了提供更优质的订货服务，微商小管理平台搬迁至新号，请关注“微商小管理”微信公众号，尊享全新订货服务。（即日起本号不再提供订货服务）");
//			}});
//
//		}});
//    	List<ChildMember> list = childMemberService.findAll();
//    	String token = weChatService.getGlobalToken2();
//    	if(list != null){
//    		for (ChildMember childMember : list) {
//    			templateInfo.setToUser(childMember.getOpenId());
//				weChatService.sendTemplateMessage(templateInfo , token);
//				logger.info(childMember.getNickName() + ":" + childMember.getOpenId());
//			}
//    	}
//    }
    
   /** 
    @ResponseBody
    @RequestMapping(value = "/initUnionId", method = RequestMethod.GET)
    public void initUnionId(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
        	String globalToken = weChatService.getGlobalToken();
        	logger.info("globalToken:{}", globalToken);
        	String globalToken_old = weChatService.getGlobalToken2();
        	logger.info("globalToken_old:{}", globalToken_old);
        	List<ChildMember> list = childMemberService.findAll();
        	if(list != null){
        		for (ChildMember childMember : list) {
        			//通过微信接口获取用户信息，可能获取用户信息失败
                    Map<String, String> userMap = new HashMap<String, String>();
                    userMap.put("openid", childMember.getOpenId());
                    ChildMember weChatUser = weChatService.getChildFromUnionId(userMap, globalToken);
                    if(weChatUser == null){
                    	weChatUser = weChatService.getChildFromUnionId(userMap, globalToken_old);
                    }
                    if(weChatUser == null){
                    	logger.info("openId not update unionId:{}", childMember.getOpenId());
                    	continue;
                    }
                    String unionId = weChatUser.getUnionId() ;
                    logger.info("unionId:{}", unionId);
                    childMember.setUnionId(unionId);
                    if(childMember.getId() != null){
                    	childMemberService.update(childMember);
                    }
    			}
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }**/
    
    
    @ResponseBody
    @RequestMapping(value = "/publicActvity", method = RequestMethod.GET)
    public void publicActvity(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
            String code = null;
            String shareUrl = request.getParameter("url");
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            code = request.getParameter(Constant.MAP_KEY);

            if (null == code || code.equals("")) {
                //获取code值
                String jumpPath = weChatService.getCode(strUrl);
                response.sendRedirect(jumpPath);
                return;
            }

            //获取到code后的正常逻辑
            Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
            if (null == userMap) {
                //通过接口获取到的用户信息异常
                response.sendRedirect("/b2b/error.html");
                return;
            }

            /**
             * 业务修改，原先使用openId 来确认用户，现在使用 unionId 来区分用户
             * unionId 只要在接入了微信开发平台才会返回
             */
             String unionId = userMap.get("unionId");
             String openId = userMap.get("openid");

             if(StringUtils.isEmpty(unionId)){
                 logger.error("unionId is null");
                 response.sendRedirect("/b2b/error.html");
                 return;
             }
             //通过unionId 获取用户信息
             ChildMember userInfo = childMemberService.findByUnionId(unionId);

             if(null == userInfo){
                 String globalToken = weChatService.getGlobalToken();
                 logger.info("globalToken:{}", globalToken);
                 //通过微信接口获取用户信息，可能获取用户信息失败
                 userInfo = weChatService.getChildFromUnionId(userMap, globalToken);
                 if (null == userInfo) {
                     //通过接口获取到的用户信息异常
                     response.sendRedirect("/b2b/error.html");
                     return;
                 }
                 //插入数据库
                 userInfo.setSourceType(ChildMember.SourceType.wx_public);
                 userInfo = childMemberService.save(userInfo);
             }

            //获取用户信息异常
            if (null == userInfo) {
                response.sendRedirect("/b2b/error.html");
                return;
            }

            if(StringUtils.isEmpty(userInfo.getOpenId()) || !userInfo.getOpenId().equals(openId) 
            		/**&& userInfo.getSourceType().equals(ChildMember.SourceType.wx_public)**/){
                 //存在的用户和之前的openid 不一样的话，进行更新
                 logger.info("update user openId , old : {} , new : {}" , userInfo.getOpenId() , openId);
                 userInfo.setOpenId(openId);
                 childMemberService.update(userInfo);
            }
            if (StringUtils.isNotEmpty(shareUrl)) {
                response.sendRedirect(shareUrl);
                return;
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
