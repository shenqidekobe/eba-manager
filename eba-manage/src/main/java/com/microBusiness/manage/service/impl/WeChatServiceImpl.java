package com.microBusiness.manage.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.dao.HostingShopDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Order.OrderStatus;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.SmallTemplateInfo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.sf.json.JSONObject;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NoticeUserDao;
import com.microBusiness.manage.dao.OrderFormDao;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;
import com.microBusiness.manage.util.JsonUtils;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * Created by afei.
 * User: afei
 * Date: 2016/5/26 9:54
 * Describe:
 * Update:
 */
@Service("weChatServiceImpl")
public class WeChatServiceImpl implements WeChatService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String newLines = "\n" ;

    @Resource
    private NoticeUserDao noticeUserDao ;
    @Resource
    private NeedDao needDao;

    @Value("${order.detail.url}")
    private String orderDetailUrl ;

    //消息接受员查看订单详情
    @Value("${order.detail.noticeUser.view}")
    private String noticeUserViewOrderUrl ;



    @Value("${ass.small.key}")
    private String assSmallKey ;

    @Value("${ass.small.secret}")
    private String assSmallSecret ;

    
    @Value("${childMember.template.common.templateId}")
	private String commonTemplateId;

    @Resource
    private HostingShopDao hostingShopDao;
    @Resource
    private OrderFormDao orderFormDao;

    @Override
    public Member getUserInfo(String openId) {

        return null;
    }

    /**
     * 获取微信用户信息UnionId机制
     *
     * @param map
     * @return
     */
    public Member getWeChatUserFromUnionId(Map<String, String> map, String access_token) {
        logger.info("获取微信用户信息UnionId机制" + map);

        try {
            String openId = map.get("openid");
            if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(access_token)) {
                return null;
            }
            // 拉取用户信息
            StringBuffer result = new StringBuffer();
            StringBuffer buffer = new StringBuffer();
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            //拼接微信URL连接地址
            buffer.append(Constant.GET_USERINFO_UNIONID + "access_token=" + access_token + "&openid=" + openId + "&lang=zh_CN");
            httpUrl = new URL(buffer.toString());
            logger.info("拉取微信用户信息请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            logger.info("获取用户信息的json字符串：" + obj.toString());
            System.out.println(obj.toString());
            if (obj.containsKey("errcode")) {
                return null;
            }
            String subscribe = obj.getString("subscribe");

            boolean isSubscribe = subscribe.equals("0") ? false : true;

            String openids = obj.getString("openid");
            String nickname = isSubscribe ? obj.getString("nickname") : "xmgs" + System.currentTimeMillis();
            String sex = isSubscribe ? obj.getString("sex") : "1";
            String province = isSubscribe ? obj.getString("province") : "";
            String city = isSubscribe ? obj.getString("city") : "";
            String country = isSubscribe ? obj.getString("country") : "";
            String headimgurl = isSubscribe ? obj.getString("headimgurl") : "";

            Member member = new Member();
            member.setOpenId(openids);
            member.setUsername(nickname);
            member.setNickname(nickname);

            if(sex.equals("1")){
                member.setGender(Member.Gender.male);
            }else if("0".equals(sex)){
                member.setGender(Member.Gender.female);
            }else{
                member.setGender(Member.Gender.unknow);
            }

            logger.info("获取用户信息：" + member);
            return member;
        } catch (Exception e) {
            logger.error("requestUserInfoFromWeChat error!", e);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取全局access_token
     */
    @Cacheable(value = "globalAccessToken")
    public String getGlobalToken() {
        String access_token_all = "";
        try {
            //access_token_all = redisClient.get("shanlin_wechat_global_access_token");
            if (access_token_all != null && !access_token_all.equals("")) {
                return access_token_all;
            }
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_GLOBAL_ACCESS_TOKEN + "grant_type=" + Constant.GLOBAL_GRANT_TYPE + "&appid=" + Constant.APP_ID + "&secret=" + Constant.SECRET);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("获取全局access_token的请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            access_token_all = obj.get("access_token") + "";
            logger.info("获取到的access_token值：{}", access_token_all);
            //redisClient.set("shanlin_wechat_global_access_token", access_token_all, 3600);
            return access_token_all;
        } catch (Exception e) {
            logger.error("register error!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 定货么小程序token
     * 获取全局小程序access_token
     */
    @Cacheable(value = "getSmallAccessToken")
    public String getSmallGlobalToken() {
        String access_token_all = "";
        try {
            //access_token_all = redisClient.get("shanlin_wechat_global_access_token");
            if (access_token_all != null && !access_token_all.equals("")) {
                return access_token_all;
            }
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_GLOBAL_ACCESS_TOKEN + "grant_type=" + Constant.GLOBAL_GRANT_TYPE + "&appid=" + Constant.SMALL_APPID + "&secret=" + Constant.SMALL_SECRET);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("获取全局access_token的请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            access_token_all = obj.get("access_token") + "";
            logger.info("获取到的access_token值：{}", access_token_all);
            //redisClient.set("shanlin_wechat_global_access_token", access_token_all, 3600);
            return access_token_all;
        } catch (Exception e) {
            logger.error("register error!", e);
            e.printStackTrace();
            return null;
        }
    }
    
    @Cacheable(value = "globalAccessToken2")
    public String getGlobalToken2() {
        String access_token_all = "";
        try {
            //access_token_all = redisClient.get("shanlin_wechat_global_access_token");
            if (access_token_all != null && !access_token_all.equals("")) {
                return access_token_all;
            }
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_GLOBAL_ACCESS_TOKEN + "grant_type=" + Constant.GLOBAL_GRANT_TYPE + "&appid=" + Constant.APP_ID_OLD + "&secret=" + Constant.SECRET_OLD);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("获取全局access_token的请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            access_token_all = obj.get("access_token") + "";
            logger.info("获取到的access_token值：{}", access_token_all);
            //redisClient.set("shanlin_wechat_global_access_token", access_token_all, 3600);
            return access_token_all;
        } catch (Exception e) {
            logger.error("register error!", e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getCode(String url) {
        logger.info("对传入的url进行微信请求地址拼接");
        try {
            logger.info("传入的url地址：" + url);
            System.out.println(url);
            String strUrl = Constant.GET_CODE + "appid=" + Constant.APP_ID + "&redirect_uri=" + url + "&response_type=" + Constant.RESPONSE_TYPE + "&scope=" + Constant.SCOPE_TYPE_BASE + "&state=" + Constant.STATE + "#wechat_redirect";//http://wx.qq.com
            logger.info("拼接后的url地址：" + strUrl);
            return strUrl;
        } catch (Exception e) {
            logger.error("getCode error!", e);
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getCode2(String url) {
        logger.info("对传入的url进行微信请求地址拼接");
        try {
            logger.info("传入的url地址：" + url);
            System.out.println(url);
            String strUrl = Constant.GET_CODE + "appid=" + Constant.APP_ID_OLD + "&redirect_uri=" + url + "&response_type=" + Constant.RESPONSE_TYPE + "&scope=" + Constant.SCOPE_TYPE_BASE + "&state=" + Constant.STATE + "#wechat_redirect";//http://wx.qq.com
            logger.info("拼接后的url地址：" + strUrl);
            return strUrl;
        } catch (Exception e) {
            logger.error("getCode error!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过code获取access_token和openid
     * 网页授权access_token
     *
     * @throws Exception
     */
    @Override
    public Map<String, String> getWebAuthAccessToken(String code) {
        logger.info("通过code获取access_token和openid");
        try {
            logger.info("传入的code值：" + code);
            if (StringUtils.isEmpty(code)) {
                return null;
            }
            // 通过code换取网页授权access_token
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_BY_CODE + "appid=" + Constant.APP_ID + "&secret=" + Constant.SECRET + "&grant_type=" + Constant.GRANT_TYPE + "&code=" + code);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("通过code换取网页授权access_token的微信请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            logger.info("微信获取到json字符串：" + obj.toString());
            //System.out.println(obj.toString());
            Map<String, String> map = new HashMap<String, String>();
            if (obj.containsKey("errcode")) {
                return null;
            } else {
                String access_token = obj.get("access_token") + "";
                String openId = obj.get("openid") + "";
                String unionId = obj.get("unionid") == null ? "" : (String) obj.get("unionid");
                map.put("access_token", access_token);
                map.put("openid", openId);
                map.put("unionId", unionId);

                logger.info("微信获取到的access_token和openid：" + map);
                return map;
            }
        } catch (Exception e) {
            logger.error("getAccessToken error!", e);
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Map<String, String> getWebAuthAccessToken2(String code) {
        logger.info("通过code获取access_token和openid");
        try {
            logger.info("传入的code值：" + code);
            if (StringUtils.isEmpty(code)) {
                return null;
            }
            // 通过code换取网页授权access_token
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_BY_CODE + "appid=" + Constant.APP_ID_OLD + "&secret=" + Constant.SECRET_OLD + "&grant_type=" + Constant.GRANT_TYPE + "&code=" + code);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("通过code换取网页授权access_token的微信请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            logger.info("微信获取到json字符串：" + obj.toString());
            //System.out.println(obj.toString());
            Map<String, String> map = new HashMap<String, String>();
            if (obj.containsKey("errcode")) {
                return null;
            } else {
                String access_token = obj.get("access_token") + "";
                String openId = obj.get("openid") + "";
                map.put("access_token", access_token);
                map.put("openid", openId);
                logger.info("微信获取到的access_token和openid：" + map);
                return map;
            }
        } catch (Exception e) {
            logger.error("getAccessToken error!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过code获取access_token和openid
     * 网页授权access_token
     *
     * @throws Exception
     */
    @Override
    public Map<String, String> getSmallAccessToken(String code) {
        logger.info("通过code获取access_token和openid");
        try {
            logger.info("传入的code值：" + code);
            if (StringUtils.isEmpty(code)) {
                return null;
            }
            // 通过code换取网页授权access_token
            StringBuffer buffer = new StringBuffer(); // 用来拼接参数
            StringBuffer result = new StringBuffer(); // 用来接受返回值
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            buffer.append(Constant.GET_BY_CODE + "appid=" + Constant.SMALL_APPID + "&secret=" + Constant.SMALL_SECRET + "&grant_type=" + Constant.GRANT_TYPE + "&code=" + code);

            // 创建URL
            httpUrl = new URL(buffer.toString());
            logger.info("通过code换取网页授权access_token的微信请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            JSONObject obj = JSONObject.fromObject(result.toString());
            logger.info("微信获取到json字符串：" + obj.toString());
            //System.out.println(obj.toString());
            Map<String, String> map = new HashMap<String, String>();
            if (obj.containsKey("errcode")) {
                return null;
            } else {
                String access_token = obj.get("access_token") + "";
                String openId = obj.get("openid") + "";
                map.put("access_token", access_token);
                map.put("openid", openId);
                logger.info("微信获取到的access_token和openid：" + map);
                return map;
            }
        } catch (Exception e) {
            logger.error("getAccessToken error!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param templateInfo
     * @return
     */
    @Override
    public boolean sendTemplateMessage(TemplateInfo templateInfo, String access_token) {
        //公众号消息推送
    	//String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        //小程序消息推送，必须带有form_id
    	String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token;
        String jsonStr = JsonUtils.toJson(templateInfo);
        WebUtils.post(url, jsonStr);
       try {
        	//更新formId的使用次数
        	OrderForm orderForm=orderFormDao.getByFormId(templateInfo.getFormId());
            if(orderForm!=null) {
            	orderForm.setUseNum(orderForm.getUseNum()+1);
            	orderFormDao.persist(orderForm);
            }
            //清除超过7天的数据
            orderFormDao.clearExpired();
		} catch (Exception e) {
		}
        
        return true;
    }

    public static void main(String args[]) {

    	String token = new WeChatServiceImpl().getGlobalToken();
    	String templateId = "nsd1yDfbBTNqjCTiZag0McGhYgEODTsbLwrS9OHZnxo";
    
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setToUser("o64Y-5U4k3zemQCVJXsJmGA4qdsg");
        templateInfo.setTemplateId(templateId);
        templateInfo.setFormId("56a58811b073adf541d1f1dcb8f842e4");
        templateInfo.setData(new HashMap<String, Map<String, String>>(){{
            this.put("first" , new HashMap<String, String>(){{
                this.put("value" , "成员加入提醒");
            }});
            this.put("keyword1" , new HashMap<String, String>(){{
                this.put("value" , "nickName");
            }});
            this.put("keyword2" , new HashMap<String, String>(){{
                this.put("value" , DateUtils.formatDateToString(new Date() , DateformatEnum.yyyyMMddHHmm));
           }});

            this.put("remark" , new HashMap<String, String>(){{
                this.put("value" , "您有新下级加入。");
            }});

        }});        

        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + token;
        //String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +  token;

        String jsonStr = JsonUtils.toJson(templateInfo);

        WebUtils.post(url, jsonStr);
    	
    	BigDecimal amount = new BigDecimal(10);
		System.out.println(amount.setScale(2, RoundingMode.HALF_UP));
		BigDecimal newPoint1 = new BigDecimal(10);
		System.out.println(newPoint1.setScale(2, RoundingMode.HALF_UP));
		BigDecimal total = new BigDecimal(3.60);
		System.out.println(total.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * @param openid      微信用户openid
     * @param sn          订单sn
     * @param messageType 消息类型：1.下单支付成功推送（配送）,2.下单支付成功推送（自提）,3.备货完成（配送）,4.备货完成（自提）
     * @param payTime     支付时间
     * @return
     */
    @Override
    public boolean sendMessageToUser(String openid, String sn, int messageType, String payTime) {
        return false;
    }


    @Override
    public ChildMember getChildFromUnionId(Map<String, String> map, String access_token) {
        try {
            String openId = map.get("openid");
            if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(access_token)) {
                return null;
            }
            /*// 拉取用户信息
            StringBuffer result = new StringBuffer();
            StringBuffer buffer = new StringBuffer();
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            //拼接微信URL连接地址
            buffer.append(Constant.GET_USERINFO_UNIONID + "access_token=" + access_token + "&openid=" + openId + "&lang=zh_CN");
            httpUrl = new URL(buffer.toString());
            logger.info("拉取微信用户信息请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();*/
            StringBuffer url  = new StringBuffer();
            url.append(Constant.GET_USERINFO_UNIONID).append("access_token=").append(access_token).append( "&openid=").append(openId).append("&lang=zh_CN");
            String urlStr = url.toString() ;
            logger.debug("get user info from wechat , request url is : {} " , urlStr);

            String result = WebUtils.post(urlStr , new HashMap<String, Object>() ) ;

            String userStr = result.toString() ;

            logger.debug("获取用户信息的json字符串：" + userStr);

            Map userMap = JsonUtils.toObject(userStr , HashMap.class);

            if (userMap.containsKey("errcode")) {
                return null;
            }

            WeChatUserInfo weChatUserInfo = JsonUtils.toObject(userStr , WeChatUserInfo.class);

            ChildMember member = new ChildMember();
            member.setOpenId(weChatUserInfo.getOpenId());
            member.setNickName(weChatUserInfo.getNickName());
            member.setHeadImgUrl(weChatUserInfo.getHeadImgUrl());
            member.setUnionId(weChatUserInfo.getUnionId());

            logger.debug("获取用户信息：" + member.toString());

            return member;
        } catch (Exception e) {
            logger.error("requestUserInfoFromWeChat error!", e);
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean sendTemplateMessage(final Order order, String templateId, 
    		String accessToken , Order.OrderStatus orderStatus) {
        Member member = order.getMember() ;
  //      Need need = order.getNeed();
        
        Supplier supplier = order.getSupplier() ;
        Set<ChildMember> childMembers = member.getChildMembers() ;
        //下单人
        ChildMember orderChildMem = order.getChildMember() ;
        String childName = "" ;
        if(null != orderChildMem){
            childName = orderChildMem.getNickName() ;
        }

        TemplateInfo templateInfo = new TemplateInfo() ;
        
        // 增加参数，跳转小程序
        Map<String, String> miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);
        
        templateInfo.setTemplateId(templateId);

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(orderDetailUrl, order.getId());
        templateInfo.setUrl(url);

        this.getCommonTemplateInfo(order , orderStatus , templateInfo , "" , supplier , null);

        for(ChildMember childMember : childMembers){
            templateInfo.setToUser(childMember.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        return true;
    }


    public boolean sendTemplateMessage(final Order order, String templateId, String accessToken , Order.OrderStatus orderStatus , final String remark) {

        Member member = order.getMember() ;
        //Need need = member.getNeed() ;
        Need need =  order.getNeed();
        
        Supplier supplier = order.getSupplier() ;
        Set<ChildMember> childMembers = member.getChildMembers() ;
        /*//下单人
        ChildMember orderChildMem = order.getChildMember() ;
        String childName = "" ;
        if(null != orderChildMem){
            childName = orderChildMem.getNickName() ;
        }*/

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        // 增加参数，跳转小程序
        Map<String, String> miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);
        
        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(orderDetailUrl, order.getId());

        this.getCommonTemplateInfo(order , orderStatus , templateInfo , remark , supplier , need );

        for(ChildMember childMember : childMembers){

            templateInfo.setToUser(childMember.getSmOpenId());
            templateInfo.setUrl(url);

            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        return true;
    }

    /**
     * 获取用户基本信息（包括UnionID机制）
     *
     * @param openId
     * @param accessToken
     * @return
     */
    @Override
    public WeChatUserInfo getWeChatUserByUnion(String openId, String accessToken) {
        try {
            if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(accessToken)) {
                return null;
            }
            // 拉取用户信息
            StringBuffer result = new StringBuffer();
            StringBuffer buffer = new StringBuffer();
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            URLConnection connection = null; // 创建的http连接
            BufferedReader bufferedReader = null; // 接受连接受的参数
            //拼接微信URL连接地址
            buffer.append(Constant.GET_USERINFO_UNIONID + "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN");
            httpUrl = new URL(buffer.toString());
            logger.info("拉取微信用户信息请求地址：" + httpUrl);
            // 建立连接
            connection = httpUrl.openConnection();
            connection.setRequestProperty("accept", Constant.ACCEPT);
            connection.setRequestProperty("connection", Constant.CONNECTION);
            connection.setRequestProperty("user-agent", Constant.USER_AGENT);
            connection.connect();
            // 接收连接返回参数
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            String userStr = result.toString() ;
            logger.info("获取用户信息的json字符串：" + userStr);

            Map userMap = JsonUtils.toObject(userStr , HashMap.class);

            if (userMap.containsKey("errcode")) {
                return null;
            }
            WeChatUserInfo userInfo = JsonUtils.toObject(userStr , WeChatUserInfo.class);
            return userInfo ;
        } catch (Exception e) {
            logger.error("requestUserInfoFromWeChat error!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 配置的接收员发送模版消息
     */
    @Override
    public boolean sendTemplateMessageToNoticeUser(Supplier supplier, final Order order, Order.OrderStatus orderStatus, String templateId , String accessToken) {
        //获取该供应商下需要发送模版消息的账号
        //noticeUserDao.findList(supplier , )
        NoticeType.Type noticeType = null;
        if(orderStatus.equals(Order.OrderStatus.create)){
            noticeType = NoticeType.Type.user_order ;
        }

        if(orderStatus.equals(Order.OrderStatus.applyCancel)){
            noticeType = NoticeType.Type.user_apply_cancel ;
        }

        if(orderStatus.equals(Order.OrderStatus.updateItems)){
            noticeType = NoticeType.Type.user_order_update ;
        }

        if(orderStatus.equals(Order.OrderStatus.completed)){
            noticeType = NoticeType.Type.user_order_complete ;
        }

        if(null == noticeType){
            return false ;
        }

       // final Need need = order.getMember().getNeed() ;
       final Need need =  order.getNeed();
        
        final Supplier bySupplier = need.getSupplier() ;

        List<NoticeUser> noticeUsers = noticeUserDao.findList(supplier , bySupplier , noticeType);

        if(CollectionUtils.isEmpty(noticeUsers)){
            return true ;
        }

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);

        String first = "" ;
        Date keyword2 = new Date() ;


        if(noticeType.equals(NoticeType.Type.user_apply_cancel)){
            first = "有一笔订货单申请取消，需要进行处理";
            keyword2 = order.getModifyDate() ;
        }

        if(noticeType.equals(NoticeType.Type.user_order)){
            first = "您有一笔新订单需要审核处理";
            keyword2 = order.getCreateDate() ;
        }

        if(noticeType.equals(NoticeType.Type.user_order_update)){
            first = "有一笔订单进行了修改";
            keyword2 = order.getModifyDate() ;
        }

        if(noticeType.equals(NoticeType.Type.user_order_complete)){
            first = "有一笔订单已完成";
            keyword2 = order.getModifyDate() ;
        }


        Map<String, Map<String, String>>  dataMap = new HashMap(){{
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("remark", new HashMap<String, String>() {{
                this.put("value", "订货方：" + bySupplier.getName() + "-" + need.getName());
            }});

        }} ;

        Map firstMap = new HashMap<String, String>() ;
        firstMap.put("value" , first) ;
        dataMap.put("first", firstMap);

        Map keword2Map = new HashMap<String, String>() ;
        keword2Map.put("value" , DateUtils.formatDateToString(keyword2, DateformatEnum.yyyyMMddHHmm)) ;
        dataMap.put("keyword2", keword2Map);

        templateInfo.setData(dataMap);

        /*if(noticeType.equals(NoticeType.Type.user_apply_cancel)){
            for(NoticeUser user:noticeUsers){
                templateInfo.setToUser(user.getOpenId());
                this.sendTemplateMessage(templateInfo , accessToken) ;
            }
        }


        if(noticeType.equals(NoticeType.Type.user_order)){
            for(NoticeUser user:noticeUsers){
                templateInfo.setToUser(user.getOpenId());
                this.sendTemplateMessage(templateInfo , accessToken) ;
            }
        }*/

        for(NoticeUser user:noticeUsers){
            templateInfo.setToUser(user.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        return true;
    }


    /**
     * 订单有备注的时候增加消息提醒
     *
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @return
     */
    @Override
    public boolean sendOrderRemarkNotice(final Order order, Member member, String templateId, String accessToken , String memo) {

        Set<ChildMember> childMembers = member.getChildMembers();
        if(CollectionUtils.isEmpty(childMembers)){
            return true ;
        }

        TemplateInfo templateInfo = this.getOrderRemarkNoticeTemplate(member , order , templateId , memo) ;

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(orderDetailUrl, order.getId());
        templateInfo.setUrl(url);

        for(ChildMember childMember : childMembers){

            templateInfo.setToUser(childMember.getSmOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;

        }

        return true;
    }

    /**
     * 订单有备注的时候增加消息提醒 ， 供应方消息接受员
     *
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @param noticeUsers
     * @return
     */
    @Override
    public boolean sendOrderRemarkNotice( Order order, Member member, String templateId, String accessToken, List<NoticeUser> noticeUsers , String memo) {
        TemplateInfo templateInfo = this.getOrderRemarkNoticeTemplates(member , order , templateId , memo) ;

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);

        if(noticeUsers != null){
        	for(NoticeUser noticeUser : noticeUsers){
                templateInfo.setToUser(noticeUser.getOpenId());
                this.sendTemplateMessage(templateInfo , accessToken) ;

            }
        }
        
        return true;
    }
    
    /**
     * 订单有备注的时候增加消息提醒 ， 供应方消息接受员
     * @param member
     * @param order
     * @param templateId
     * @param memo
     * @return
     */
    private TemplateInfo getOrderRemarkNoticeTemplates(Member member , final Order order , String templateId , String memo){

        //Need need = member.getNeed() ;
        Need need =  order.getNeed();
        
        Supplier bySupplier = need.getSupplier() ;

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);

        fistSb.append("备注方：").append(bySupplier.getName()).append("-").append(need.getName()).append(newLines);
        fistSb.append("备注内容：").append(memo);
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});

            this.put("remark", new HashMap<String, String>() {{
                this.put("value", "请进入订单了解更多信息");
            }});

        }});
        return templateInfo ;
    }
    

    /**
     * 给采购方自己的备注消息提醒
     * @param member
     * @param order
     * @param templateId
     * @param memo
     * @return
     */
    private TemplateInfo getOrderRemarkNoticeTemplate(Member member , final Order order , String templateId , String memo){

        //Need need = member.getNeed() ;
        Need need =  order.getNeed();
        Supplier bySupplier = need.getSupplier() ;

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);

        fistSb.append("备注方：").append(bySupplier.getName()).append("-").append(need.getName()).append(newLines);
        fistSb.append("备注内容：").append(memo);
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});

            this.put("remark", new HashMap<String, String>() {{
                this.put("value", "客服电话："+order.getSupplier().getCustomerServiceTel()+newLines+"请进入订单了解更多信息");
            }});

        }});
        return templateInfo ;
    }

    /**
     * 企业添加备注，采购方接受消息
     * @param order
     * @param member
     * @param supplier
     * @param templateId
     * @param accessToken
     * @param memo
     * @return
     */
    @Override
    public boolean sendOrderRemarkNotice(Order order, Member member, Supplier supplier, String templateId , String accessToken, String memo) {

        Set<ChildMember> childMembers = member.getChildMembers();
        if(CollectionUtils.isEmpty(childMembers)){
            return true ;
        }

        TemplateInfo templateInfo = this.getOrderRemarkNoticeTemplate(supplier , order  , templateId , memo) ;

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(orderDetailUrl, order.getId());
        templateInfo.setUrl(url);

        for(ChildMember childMember : childMembers){

            templateInfo.setToUser(childMember.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;

        }

        return true;
    }


    private TemplateInfo getOrderRemarkNoticeTemplate(Supplier supplier , final Order order , String templateId , String memo){


        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);

        fistSb.append("备注方：").append(supplier.getName()).append(newLines);
        fistSb.append("备注内容：").append(memo);
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});

            this.put("remark", new HashMap<String, String>() {{
                this.put("value", "客服电话："+order.getSupplier().getCustomerServiceTel()+newLines+"请进入订单了解更多信息");
            }});

        }});
        return templateInfo ;
    }

    /**
     * 采购单消息接受员接受模版消息
     *
     * @param supplier
     * @param purchaseSupplier 采购企业
     * @param need
     * @param order
     * @param orderStatus
     * @param templateId
     * @param accessToken
     * @param typePurchase
     * @param remark 附加备注
     * @return
     */
    @Override
    public boolean sendTemplateMessageToNoticeUserPurchase(Supplier supplier, Supplier purchaseSupplier, Need need, final Order order, Order.OrderStatus orderStatus, String templateId, String accessToken, NoticeTypePurchase.Type typePurchase , String remark) {

        List<NoticeUser> noticeUsers = noticeUserDao.findList(purchaseSupplier , need , typePurchase) ;
        if(CollectionUtils.isEmpty(noticeUsers)){
            return false ;
        }

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());

        this.getCommonTemplateInfo(order , orderStatus , templateInfo , remark , supplier  , need ) ;

        for(NoticeUser noticeUser : noticeUsers){

            templateInfo.setToUser(noticeUser.getOpenId());
            templateInfo.setUrl(url);
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        return true;

    }

    /**
     * 采购单消息接受员接受模版消息
     *
     * @param order
     * @param orderStatus
     * @param templateId
     * @param accessToken
     * @param typePurchase
     * @param remark
     * @return
     */
    @Override
    public boolean sendTemplateMessageToNoticeUserPurchase(Order order, Order.OrderStatus orderStatus, String templateId, String accessToken, NoticeTypePurchase.Type typePurchase, String remark) {
        Supplier supplier = order.getSupplier() ;
        Member member = order.getMember() ;
        //Need need = member.getNeed();
        Need need =  order.getNeed();
        Supplier purchaseSupplier = need.getSupplier();
        return this.sendTemplateMessageToNoticeUserPurchase(supplier , purchaseSupplier , need , order , orderStatus , templateId , accessToken , typePurchase , remark);
    }


    /**
     * 订单有备注的时候增加消息提醒 ， 采购方消息接受员接受消息提醒
     *
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @param memo
     * @param isPurchase  是否采购方备注
     * @return
     */
    @Override
    public boolean sendOrderRemarkNoticeToNoticeUserPurchase(Order order, Member member , String templateId, String accessToken, String memo, boolean isPurchase) {
        //Need need = member.getNeed();
        Need need = order.getNeed();
        //采购方企业
        Supplier purchaseSupplier = need.getSupplier() ;
        List<NoticeUser> noticeUsers = noticeUserDao.findList(purchaseSupplier , need , NoticeTypePurchase.Type.order_remark);
        TemplateInfo templateInfo = null ;
        if(isPurchase){
            templateInfo = this.getOrderRemarkNoticeTemplate(member , order  , templateId , memo) ;
        }else{
            //供应商企业
            Supplier supplier = order.getSupplier() ;
            templateInfo = this.getOrderRemarkNoticeTemplate(supplier , order  , templateId , memo) ;
        }

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);

        for(NoticeUser noticeUser : noticeUsers){
            templateInfo.setToUser(noticeUser.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        return false;
    }


    private void getCommonTemplateInfo(final Order order , Order.OrderStatus orderStatus  , TemplateInfo templateInfo , String remark , Supplier supplier , Need need){

        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("客服电话：").append(supplier.getCustomerServiceTel()).append(newLines);
    //  remarks.append("供应商：").append(supplier.getName()).append(newLines);
        remarks.append("收货地址：").append(order.getAreaName()+order.getAddress());

        if(orderStatus.equals(Order.OrderStatus.shipped)){
            //订单配送
            templateInfo.setData(new HashMap<String, Map<String, String>>(){{
                this.put("first" , new HashMap<String, String>(){{
                    this.put("value" , "订单发货");
                }});
                this.put("keyword1" , new HashMap<String, String>(){{
                    this.put("value" , order.getSn());
                }});

                this.put("keyword2" , new HashMap<String, String>(){{
                    this.put("value" , DateUtils.formatDateToString(new Date() , DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark" , new HashMap<String, String>(){{
                    this.put("value" , remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.create)){
            //订单创建
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单提交成功");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.canceled)){
            //取消
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.completed)){
            //完成
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单完成");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.denied)){
            remarks.append(newLines).append("原因：").append(order.getDeniedReason());
            //拒绝
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单已拒绝");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.passed)){
            remarks.append(newLines).append(remark);
            //审核通过
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单已通过");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.updateItems)){
            remarks.append(newLines).append(remark);
            //审核通过
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单信息变更");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }else if(orderStatus.equals(Order.OrderStatus.cancelShipped)){
            remarks.append(newLines).append(remark);
            //审核通过
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单发货取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});

            }});

        }
    }


    /**
     * 消息接受员接受消息通知 , 根据订单操作（包含接受员和C端用户）
     *
     * @param order
     * @param orderStatus
     * @param accessToken      公众号accessToken
     * @param smallAccessToken 小程序accessToken
     * @param templateId       公众号模版Id
     * @param smallTemplateId  小程序模版Id
     * @param remark 模版消息中的部分remark（比如拒绝原因）
     * @return
     */
    @Override
    public boolean sendTemplateMessageByOrderStatus(Order order, Order.OrderStatus orderStatus, String accessToken, String smallAccessToken, String templateId, String smallTemplateId , String remark) {

        Member member = order.getMember() ;

        ChildMember childMember = order.getChildMember() ;
        //卖方
        Supplier sell = order.getSupplier() ;
        //买方
        Supplier buy = order.getToSupplier() ;
        //客户
        Need need = order.getNeed() ;

        /*if(Order.Type.general.equals(order.getType()) ||  Order.Type.distribution.equals(order.getType()) || Order.Type.billGeneral.equals(order.getType())) {//直销单
            need = needDao.findNeedByMemberSupplier(sell , member) ;
        }

        if(Order.Type.billDistribution.equals(order.getType())){
            need = needDao.findNeedByMemberSupplier(buy , member) ;
        }*/

        this.sendTemplateMessageByOrderStatus(order, orderStatus, accessToken, smallAccessToken, templateId, smallTemplateId, sell, buy, member, need, remark);

        return true;
    }

    /**
     * @param order
     * @param orderStatus
     * @param accessToken      公众号accessToken
     * @param smallAccessToken 小程序accessToken
     * @param templateId       公众号模版Id
     * @param smallTemplateId  小程序模版Id
     * @param sell             卖方企业
     * @param buy              买方企业
     * @param member           下单主账号
     * @param need             个体客户关系
     * @param remark 模版消息中的部分remark（比如拒绝原因）
     * @return
     */
    @Override
    public boolean sendTemplateMessageByOrderStatus(Order order, Order.OrderStatus orderStatus,
    		String accessToken, String smallAccessToken, String templateId, String smallTemplateId, 
    		final Supplier sell, final Supplier buy, Member member, final Need need , String remark) {

        //主订单类型：直销订单 ， 分销订单
        Order.Type type = order.getType() ;
        //订单创建类型：正常下单，代下单
        Order.BuyType buyType = order.getBuyType() ;

        if(Order.Type.general.equals(order.getType()) ){//直销单
            logger.debug("直销单发送消息通知开始：");
            //订单创建
            if(Order.OrderStatus.create.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order , orderStatus , accessToken , childMembers , sell , buy , need , smallAccessToken , templateId , smallTemplateId , remark) ;
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            //订单审核通过
            if(Order.OrderStatus.passed.equals(orderStatus)){

                //C 端用户接受消息
                if (!buyType.equals(Order.BuyType.waterSubstitute)) {
                    Set<ChildMember> childMembers = member.getChildMembers();
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, 
                    		null , smallAccessToken , templateId , smallTemplateId , remark);
                }

            }

            //订单审核不通过
            if(Order.OrderStatus.denied.equals(orderStatus)){

                //C 端用户接受消息
                if (!buyType.equals(Order.BuyType.waterSubstitute)) {
                    Set<ChildMember> childMembers = member.getChildMembers();
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, 
                    		childMembers, sell, buy, null , smallAccessToken , 
                    		templateId , smallTemplateId , remark);
                }


            }

            //用户申请取消
            if(Order.OrderStatus.applyCancel.equals(orderStatus)){

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户申请取消通过
            if(Order.OrderStatus.applyCancel_passed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, 
                    		childMembers, sell, buy, null , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户申请取消不通过
            if(Order.OrderStatus.applyCancel_denied.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
               // List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
               // this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户修改商品
            if(Order.OrderStatus.updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_update) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //后台用户修改商品
            if(Order.OrderStatus.admin_updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, 
                    		childMembers, sell, buy, need , 
                    		smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_update) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //发货
            if(Order.OrderStatus.shipped.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.order_shipping) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单取消
            if(Order.OrderStatus.canceled.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.order_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            //订单完成
            if(Order.OrderStatus.completed.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_complete) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }
            logger.debug("直销单发送消息通知结束：");

        }else if(Order.Type.distribution.equals(type)){ //分销单
            logger.debug("分销单发送消息通知开始：");
            //订单创建
            if(Order.OrderStatus.create.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单审核通过
            if(Order.OrderStatus.passed.equals(orderStatus)){

            }

            //订单审核不通过
            if(Order.OrderStatus.denied.equals(orderStatus)){

            }

            //用户申请取消
            if(Order.OrderStatus.applyCancel.equals(orderStatus)){
                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            //用户申请取消通过
            if(Order.OrderStatus.applyCancel_passed.equals(orderStatus)){

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户申请取消不通过
            if(Order.OrderStatus.applyCancel_denied.equals(orderStatus)){

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户修改商品
            if(Order.OrderStatus.updateItems.equals(orderStatus)){

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //后台用户修改商品
            if(Order.OrderStatus.admin_updateItems.equals(orderStatus)){

            }

            //发货
            if(Order.OrderStatus.shipped.equals(orderStatus)){

            }

            //订单取消
            if(Order.OrderStatus.canceled.equals(orderStatus)){
            }

            //订单完成
            if(Order.OrderStatus.completed.equals(orderStatus)){

            }

            logger.debug("分销单发送消息通知结束：");

        }else if(Order.Type.billGeneral.equals(type)){ //拆单后的直销单
            logger.debug("拆单后的直销单发送消息通知开始：");

            //订单创建
            if(Order.OrderStatus.create.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order , orderStatus , accessToken , childMembers , sell , buy , need , smallAccessToken , templateId , smallTemplateId , remark) ;
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            //订单审核通过
            if(Order.OrderStatus.passed.equals(orderStatus)){

                //C 端用户接受消息
                if (!buyType.equals(Order.BuyType.waterSubstitute)) {
                    Set<ChildMember> childMembers = member.getChildMembers();
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

            }

            //订单审核不通过
            if(Order.OrderStatus.denied.equals(orderStatus)){

                //C 端用户接受消息
                if (!buyType.equals(Order.BuyType.waterSubstitute)) {
                    Set<ChildMember> childMembers = member.getChildMembers();
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_review) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消
            if(Order.OrderStatus.applyCancel.equals(orderStatus)){

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户申请取消通过
            if(Order.OrderStatus.applyCancel_passed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户申请取消不通过
            if(Order.OrderStatus.applyCancel_denied.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_apply_cancel) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //用户修改商品
            if(Order.OrderStatus.updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_update) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //后台用户修改商品
            if(Order.OrderStatus.admin_updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_update) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //发货
            if(Order.OrderStatus.shipped.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){
                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.order_shipping) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单取消
            if(Order.OrderStatus.canceled.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.order_cancel) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            //订单完成
            if(Order.OrderStatus.completed.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy, need , smallAccessToken , templateId , smallTemplateId , remark);
                }

                //订货单消息接受员
                //List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_complete) ;
                //this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , buy , sell , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;
            }

            logger.debug("拆单后的直销单发送消息通知结束：");

        }else if(Order.Type.billDistribution.equals(type)){//拆单后的分销单

            logger.debug("拆单后的分销单发送消息通知开始：");
            //订单创建
            if(Order.OrderStatus.create.equals(orderStatus)){

                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_create) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_order) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单审核通过
            if(Order.OrderStatus.passed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //分销商 采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_review) ;

                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单审核不通过
            if(Order.OrderStatus.denied.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //分销商 采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_review) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消
            if(Order.OrderStatus.applyCancel.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消通过
            if(Order.OrderStatus.applyCancel_passed.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消不通过
            if(Order.OrderStatus.applyCancel_denied.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId, remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId, remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId, remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId, remark) ;


            }

            //用户修改商品
            if(Order.OrderStatus.updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId, remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_update) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //后台用户修改商品
            if(Order.OrderStatus.admin_updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId, remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_update) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;



            }

            //发货
            if(Order.OrderStatus.shipped.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_shipped) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.order_shipping) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.order_shipping) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //订单取消
            if(Order.OrderStatus.canceled.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.order_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.order_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;



            }

            //订单完成
            if(Order.OrderStatus.completed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, buy, sell, need , smallAccessToken , templateId , smallTemplateId , remark);
                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_complete) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(分销商) 订货单消息接受员
                List<NoticeUser> noticeUsers = noticeUserDao.findList(buy , need , NoticeType.Type.user_order_complete) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , noticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_complete) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;



            }
            logger.debug("拆单后的分销单发送消息通知结束：");

        }else if(Order.Type.formal.equals(type)){//正式订单（正式供应产生的订单）

            logger.debug("正式供应订单发送消息通知开始：");

            //订单创建
            if(Order.OrderStatus.create.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }

                // 采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_create) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单审核通过
            if(Order.OrderStatus.passed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_review) ;

                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //订单审核不通过
            if(Order.OrderStatus.denied.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_review) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消
            if(Order.OrderStatus.applyCancel.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消通过
            if(Order.OrderStatus.applyCancel_passed.equals(orderStatus)){
                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //用户申请取消不通过
            if(Order.OrderStatus.applyCancel_denied.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.user_apply_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId, remark) ;

                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_apply_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId, remark) ;


            }

            //用户修改商品
            if(Order.OrderStatus.updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_update) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //后台用户修改商品
            if(Order.OrderStatus.admin_updateItems.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_update) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_update) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }

            //发货
            if(Order.OrderStatus.shipped.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_shipped) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.order_shipping) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


            }

            //订单取消
            if(Order.OrderStatus.canceled.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_cancel) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.order_cancel) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;



            }

            //订单完成
            if(Order.OrderStatus.completed.equals(orderStatus)){

                //C 端用户接受消息
                if(!buyType.equals(Order.BuyType.waterSubstitute)){

                    Set<ChildMember> childMembers = member.getChildMembers() ;
                    this.sendTemplateMessageToCustomer(order, orderStatus, accessToken, childMembers, sell, buy , need , smallAccessToken , templateId , smallTemplateId , remark);

                }


                //（分销商）采购单消息接受员
                List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(buy , need , NoticeTypePurchase.Type.order_complete) ;
                this.sendTemplateMessageToPurchaseNoticeUser(order , accessToken , orderStatus , sell , buy , need , purchaseNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;


                //(供应商) 订货单消息接受员
                List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(sell , buy , NoticeType.Type.user_order_complete) ;
                this.sendTemplateMessageToOrderNoticeUser(order , accessToken , orderStatus , sell , buy , need , supplierNoticeUsers , smallAccessToken , templateId , smallTemplateId , remark) ;

            }
            logger.debug("正式供应订单发送消息通知结束：");
        }else{
            //do nothing
        }

        return true;
    }

    /**
     * 拆单消息通知
     *
     * @param orders
     * @param need
     * @param member
     * @param childMember
     * @param sell
     * @param accessToken
     * @param smallAccessToken
     * @param templateId
     * @param smallTemplateId
     * @param mainOrder
     * @param partDate
     * @return
     */
    @Override
    public boolean sendTemplateMessageByOrderPart(List<Order> orders, Need need, Member member, ChildMember childMember, Supplier sell, String accessToken, String smallAccessToken, String templateId, String smallTemplateId , Order mainOrder , final Date partDate) {

        if(CollectionUtils.isEmpty(orders)){
            return false ;
        }

        int orderLen = orders.size() ;
        final StringBuffer snSb = new StringBuffer() ;

        for(int i= 0 ; i< orderLen ; i++){
            if(i > 0){
                snSb.append("、") ;
            }
            snSb.append(orders.get(i).getSn()) ;
        }

        final StringBuffer fistSb = new StringBuffer("您").append(mainOrder.getSn())
                .append("订单拆分成")
                .append(snSb.toString())
                .append(",").append(orderLen).append("笔订单，")
                .append("请分别关注各个订单的变化。");

        final StringBuffer remarks = new StringBuffer() ;

        remarks.append("客服电话：").append(sell.getCustomerServiceTel()).append(newLines);
        remarks.append("供应商：").append(sell.getName()).append(newLines);
        remarks.append("收货点：").append(need.getName());


        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(templateId);

        templateInfo.setData(new HashMap<String, Map<String, String>>(){{
            this.put("first" , new HashMap<String, String>(){{
                this.put("value" , fistSb.toString());
            }});
            this.put("keyword1" , new HashMap<String, String>(){{
                this.put("value" , snSb.toString());
            }});

            this.put("keyword2" , new HashMap<String, String>(){{
                this.put("value" , DateUtils.formatDateToString(partDate, DateformatEnum.yyyyMMddHHmm));
            }});

            this.put("remark", new HashMap<String, String>() {{
                this.put("value", remarks.toString());
            }});

        }});

        Set<ChildMember> childMembers = member.getChildMembers() ;
        if(CollectionUtils.isNotEmpty(childMembers)){
            for(ChildMember childMember1 : childMembers){
                templateInfo.setToUser(childMember1.getOpenId());
                this.sendTemplateMessage(templateInfo , accessToken) ;
            }
        }


        //发送给（分销商）订货单消息接受员
        List<NoticeUser> noticeUsers = noticeUserDao.findList(sell , need , NoticeType.Type.user_order_review) ;

        final StringBuffer noticeRemarks = new StringBuffer() ;

        noticeRemarks.append("订货方：").append(sell.getName()).append("-").append(need.getName());

        templateInfo.getData().get("remark").put("value" , noticeRemarks.toString());

        this.sendTemplateMessToNoticeUser(templateInfo , accessToken , noticeUsers);

        return true;
    }

    /**
     * 备注消息提醒
     *
     * @param order
     * @param accessToken
     * @param smallAccessToken
     * @param templateId
     * @param smallTemplateId
     * @param memo
     * @param logType          操作类型
     * @return
     */
    @Override
    public boolean sendTemplateMessageByNotice(Order order, String accessToken, String smallAccessToken, String templateId, String smallTemplateId, String memo, MsgType msgType,LogType logType,Supplier sell) {
    	//主订单类型：直销订单 ， 分销订单
        Order.Type type = order.getType() ;
        //订单创建类型：正常下单，代下单
        Order.BuyType buyType = order.getBuyType() ;
        
        Member member=order.getMember();
        
        TemplateInfo templateInfo = this.getCommonRemarkTemplateInfo(order , msgType , logType,memo,templateId,sell);
        TemplateInfo templateInfoMember=null;

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);
        //个体客户自己 备注时收到的模版消息
        if (logType.equals(LogType.member)) {
        	templateInfoMember = this.getMemberRemarkTemplateInfo(order , msgType , logType,memo,templateId);
        	templateInfoMember.setUrl(url);
        }
    	
    	if (type.equals(Order.Type.general)) {//直销单
    		if (!buyType.equals(Order.BuyType.waterSubstitute)) {
    			//C 端用户接受消息
    			Set<ChildMember> childMembers = member.getChildMembers() ;
    			if (logType.equals(LogType.member)) {
    				this.sendTemplateMessToCustomer(templateInfoMember,accessToken,childMembers);
				}else {
					this.sendTemplateMessToCustomer(templateInfo,accessToken,childMembers);
				}
			}
            //(分销商) 订货单消息接受员
            List<NoticeUser> noticeUsers = noticeUserDao.findList(order.getSupplier() , order.getNeed() , NoticeType.Type.user_apply_cancel) ;
            this.sendTemplateMessToNoticeUser(templateInfo,accessToken,noticeUsers);
            
		}else if (type.equals(Order.Type.billGeneral)) {//拆单后的直销单
			if (!buyType.equals(Order.BuyType.waterSubstitute)) {
    			//C 端用户接受消息
    			Set<ChildMember> childMembers = member.getChildMembers() ;
    			if (logType.equals(LogType.member)) {
    				this.sendTemplateMessToCustomer(templateInfoMember,accessToken,childMembers);
				}else {
					this.sendTemplateMessToCustomer(templateInfo,accessToken,childMembers);
				}
			}
            //(分销商) 订货单消息接受员
			 List<NoticeUser> noticeUsers = noticeUserDao.findList(order.getSupplier() , order.getNeed() , NoticeType.Type.user_apply_cancel) ;
	         this.sendTemplateMessToNoticeUser(templateInfo,accessToken,noticeUsers);
		}else if (type.equals(Order.Type.billDistribution)) {
			//企业和个体客户
	    	if (msgType.equals(MsgType.btoc)) {
	    		if (!buyType.equals(Order.BuyType.waterSubstitute)) {
	    			//C 端用户接受消息
	    			Set<ChildMember> childMembers = member.getChildMembers() ;
	    			if (logType.equals(LogType.member)) {
	    				this.sendTemplateMessToCustomer(templateInfoMember,accessToken,childMembers);
					}else {
						this.sendTemplateMessToCustomer(templateInfo,accessToken,childMembers);
					}
				}
	    		 List<NoticeUser> noticeUsers = noticeUserDao.findList(order.getToSupplier() , order.getNeed() , NoticeType.Type.user_apply_cancel) ;
	             this.sendTemplateMessToNoticeUser(templateInfo,accessToken,noticeUsers);
	    		
	    	}else if (msgType.equals(MsgType.btob)) {
	    		//（分销商）采购单消息接受员
	            List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(order.getToSupplier() , order.getNeed() , NoticeTypePurchase.Type.user_apply_cancel) ;
	            this.sendTemplateMessToNoticeUser(templateInfo,accessToken,purchaseNoticeUsers);
	            
	            //(供应商) 订货单消息接受员
	            List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(order.getSupplier() , order.getToSupplier() , NoticeType.Type.user_apply_cancel) ;
	            this.sendTemplateMessToNoticeUser(templateInfo,accessToken,supplierNoticeUsers);
	    	}
		}else if (type.equals(Order.Type.formal)) {
			if (!buyType.equals(Order.BuyType.waterSubstitute)) {
    			//C 端用户接受消息
    			Set<ChildMember> childMembers = member.getChildMembers() ;
    			if (logType.equals(LogType.member)) {
    				this.sendTemplateMessToCustomer(templateInfoMember,accessToken,childMembers);
				}else {
					this.sendTemplateMessToCustomer(templateInfo,accessToken,childMembers);
				}
			}
			//（分销商）采购单消息接受员
            List<NoticeUser> purchaseNoticeUsers = noticeUserDao.findList(order.getToSupplier() , order.getNeed() , NoticeTypePurchase.Type.user_apply_cancel) ;
            this.sendTemplateMessToNoticeUser(templateInfo,accessToken,purchaseNoticeUsers);
            
            //(供应商) 订货单消息接受员
            List<NoticeUser> supplierNoticeUsers = noticeUserDao.findList(order.getSupplier() , order.getToSupplier() , NoticeType.Type.user_apply_cancel) ;
            this.sendTemplateMessToNoticeUser(templateInfo,accessToken,supplierNoticeUsers);
		}
    	return true;
    }
    
    /**
     * 订单备注消息模版
     * @param order
     * @param msgType
     * @param logType
     * @param memo
     * @return
     */
    private TemplateInfo getCommonRemarkTemplateInfo(final Order order ,MsgType msgType,LogType logType,String memo, String templateId,Supplier sell){
    	
    	TemplateInfo templateInfo=new TemplateInfo();
    	templateInfo.setTemplateId(templateId);
    	
    	// 增加参数，跳转小程序
        Map<String, String> miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);
    	
    	final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);
    	//分销商
    	Supplier supplier;
    	if (order.getSupplyType().equals(SupplyType.temporary)) {
    		if (order.getType().equals(Order.Type.billDistribution)) {
    			supplier=order.getToSupplier();
    		}else {
    			supplier=order.getSupplier();
    		}
    		if (sell == null) {
    			sell=supplier;
    		}
    		//企业和个体客户
    		if (msgType.equals(MsgType.btoc)) {
    			if (logType.equals(LogType.member)) {
    				fistSb.append("备注方：").append(supplier.getName()).append("-").append(order.getNeed().getName()).append(newLines);
    			}else if (logType.equals(LogType.distributor)) {
    				fistSb.append("备注方：").append(supplier.getName()).append(newLines);
    			}
    		}else if (msgType.equals(MsgType.btob)) {
    			//供应商
    			Supplier bySupplier=order.getSupplier();
    			//企业和企业
    			if (logType.equals(LogType.distributor)) {
    				fistSb.append("备注方：").append(supplier.getName()).append(newLines);
    			}else if (logType.equals(LogType.supplier)) {
    				fistSb.append("备注方：").append(bySupplier.getName()).append(newLines);
    			}
    		}
		}else {
			supplier=order.getToSupplier();
			//供应商
			Supplier bySupplier=order.getSupplier();
			if (sell == null) {
    			sell=supplier;
    		}
			if (logType.equals(LogType.member)) {
				fistSb.append("备注方：").append(supplier.getName()).append("-").append(order.getNeed().getName()).append(newLines);
			}else if (logType.equals(LogType.distributor)) {
				fistSb.append("备注方：").append(supplier.getName()).append(newLines);
			}else if (logType.equals(LogType.supplier)) {
				fistSb.append("备注方：").append(bySupplier.getName()).append(newLines);
			}
		}
        fistSb.append("备注内容：").append(memo);
        
        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("客服电话：").append(sell.getCustomerServiceTel()).append(newLines);
        remarks.append("请进入订单了解更多信息");
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});
            
            this.put("remark", new HashMap<String, String>() {{
                this.put("value", remarks.toString());
            }});

        }});
        return templateInfo;
    }
    
    /**
     * 订单备注个体客户自己操作接收消息模版
     * @param order
     * @param msgType
     * @param logType
     * @param memo
     * @return
     */
    private TemplateInfo getMemberRemarkTemplateInfo(final Order order ,MsgType msgType,LogType logType,String memo, String templateId){
    	
    	TemplateInfo templateInfo=new TemplateInfo();
    	templateInfo.setTemplateId(templateId);
    	
    	// 增加参数，跳转小程序
        Map<String, String> miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);
    	
    	final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);
    	//分销商
    	Supplier supplier;
    	if (order.getSupplyType().equals(SupplyType.temporary)) {
	    	if (order.getType().equals(Order.Type.billDistribution)) {
	    		supplier=order.getToSupplier();
			}else {
				supplier=order.getSupplier();
			}
    	}else {
    		supplier=order.getToSupplier();
		}
    	fistSb.append("备注方：").append(supplier.getName()).append("-").append(order.getNeed().getName()).append(newLines);
        fistSb.append("备注内容：").append(memo);
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});
            
            this.put("remark", new HashMap<String, String>() {{
                this.put("value", "请进入订单了解更多信息");
            }});

        }});
        return templateInfo;
    }
    
    /**
     * 正式供应备注消息模版
     * @param order
     * @param msgType
     * @param logType
     * @param memo
     * @return
     */
    private TemplateInfo getCommonRemarkTemplateInfoFormal(final Order order ,MsgType msgType,LogType logType,String memo, String templateId,Supplier sell){
    	
    	TemplateInfo templateInfo=new TemplateInfo();
    	templateInfo.setTemplateId(templateId);
    	final StringBuffer fistSb = new StringBuffer("您的订单有备注信息").append(newLines);
    	//分销商
    	Supplier supplier;
    	
    	if (order.getType().equals(Order.Type.billDistribution)) {
    		supplier=order.getToSupplier();
    		
		}else {
			supplier=order.getSupplier();
		}
    	if (sell == null) {
    		sell=supplier;
		}
    	//企业和个体客户
    	if (msgType.equals(MsgType.btoc)) {
    		if (logType.equals(LogType.member)) {
    			fistSb.append("备注方：").append(supplier.getName()).append("-").append(order.getNeed().getName()).append(newLines);
			}else if (logType.equals(LogType.distributor)) {
				fistSb.append("备注方：").append(supplier.getName()).append(newLines);
			}
		}else if (msgType.equals(MsgType.btob)) {
			//供应商
	    	Supplier bySupplier=order.getSupplier();
			//企业和企业
			if (logType.equals(LogType.distributor)) {
				fistSb.append("备注方：").append(supplier.getName()).append(newLines);
			}else if (logType.equals(LogType.supplier)) {
				fistSb.append("备注方：").append(bySupplier.getName()).append(newLines);
			}
		}
        fistSb.append("备注内容：").append(memo);
        
        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("客服电话：").append(sell.getCustomerServiceTel()).append(newLines);
        remarks.append("请进入订单了解更多信息");
        templateInfo.setData(new HashMap<String, Map<String, String>>() {{
            this.put("first", new HashMap<String, String>() {{
                this.put("value", fistSb.toString());
            }});
            this.put("keyword1", new HashMap<String, String>() {{
                this.put("value", order.getSn());
            }});

            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
            }});
            
            this.put("remark", new HashMap<String, String>() {{
                this.put("value", remarks.toString());
            }});

        }});
        return templateInfo;
    }
    

    private void getCommonTemplateInfo(final Order order , Order.OrderStatus orderStatus  , TemplateInfo templateInfo){

        if(orderStatus.equals(Order.OrderStatus.shipped)){
            //订单配送
            templateInfo.setData(new HashMap<String, Map<String, String>>(){{
                this.put("first" , new HashMap<String, String>(){{
                    this.put("value" , "订单发货");
                }});
                this.put("keyword1" , new HashMap<String, String>(){{
                    this.put("value" , order.getSn());
                }});

                this.put("keyword2" , new HashMap<String, String>(){{
                    this.put("value" , DateUtils.formatDateToString(new Date() , DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark" , new HashMap<String, String>(){{
                    this.put("value" , remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.create)){
            //订单创建
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单提交成功");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel)){
            //申请取消
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单申请取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.canceled)){
            //取消
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.completed)){
            //完成
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单完成");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.denied)){
            //审核拒绝
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单已拒绝");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

               /* this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});
*/
            }});

        }else if(orderStatus.equals(Order.OrderStatus.passed)){
            //审核通过
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单已通过");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.updateItems)){
            //前台修改商品数量
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单信息变更");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.admin_updateItems)){
            //后台修改订单商品信息
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单信息变更");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.cancelShipped)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "订单发货取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel_passed)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "申请取消通过");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel_denied)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "申请取消拒绝");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }
    }

    /**
     * 向消息接受员发送模版消息
     * @param templateInfo
     * @param accessToken
     * @param noticeUsers
     */
    private void sendTemplateMessToNoticeUser(TemplateInfo templateInfo , String accessToken , List<NoticeUser> noticeUsers){
        for(NoticeUser noticeUser : noticeUsers){
            templateInfo.setToUser(noticeUser.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }
    }

    /**
     * 向客户发送模版消息
     * @param templateInfo
     * @param accessToken
     * @param childMembers
     */
    private void sendTemplateMessToCustomer(TemplateInfo templateInfo , String accessToken , Set<ChildMember> childMembers){
        for(ChildMember childMember : childMembers){
            templateInfo.setToUser(childMember.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }
    }


    /**
     * 向下单客户发送模版消息
     */
    @Override
    public void sendTemplateMessageToCustomer(Order order, Order.OrderStatus orderStatus,
    		String accessToken, Set<ChildMember> childMembers, final Supplier sell, 
    		final Supplier buy, final Need need1, String smallAccessToken , String templateId , 
    		String smallTemplateId , final String remark) {

        logger.debug("发送给客户开始：");


        /*if(CollectionUtils.isEmpty(childMembers)){
            logger.debug("客户为空");
            return ;
        }*/



        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(templateId);

        // 增加参数，跳转小程序
        Map miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);
        
        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(orderDetailUrl, order.getId());
        templateInfo.setUrl(url);

        this.getCommonTemplateInfo(order , orderStatus , templateInfo);

        Map<String, Map<String, String>> data = templateInfo.getData() ;

        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("客服电话：").append(sell.getCustomerServiceTel()).append(newLines);
        remarks.append("供应商：").append(sell.getName()).append(newLines);
        remarks.append("收货地址：").append(order.getAddress());
        if(StringUtils.isNotEmpty(remark)){
            remarks.append(newLines).append(remark) ;
        }

        data.put("remark" , new HashMap<String, String>(){{
            this.put("value" , remarks.toString());
        }});

        //主账号用户
        for(ChildMember childMember : childMembers){
            templateInfo.setToUser(childMember.getSmOpenId());
            OrderForm orderForm = childMember.getOrderFormOne();
    		Date now=new Date();
    		if(orderForm != null) {
    			//formId是否过期
    			if(DateUtils.daysBetween(orderForm.getCreateDate(), now) < 7) {
    				 templateInfo.setFormId(orderForm.getFormId());
    			     //templateInfo.setPage(orderForm.getPage());
    			}
    			orderFormDao.clearExpired();//删除formId
    		}
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        logger.debug("发送给客户结束：");
    }

    /**
     * 向订货单消息接受员发送消息
     *
     * @param order
     * @param accessToken
     * @param orderStatus
     * @param sell
     * @param buy
     * @param need
     * @param noticeUsers
     * @param remark
     */
    @Override
    public void sendTemplateMessageToOrderNoticeUser(Order order, String accessToken, Order.OrderStatus orderStatus, final Supplier sell, final Supplier buy, final Need need, List<NoticeUser> noticeUsers, String smallAccessToken , String templateId , String smallTemplateId , String remark) {
        logger.debug("发送给订货单消息接受员开始：");
        if(CollectionUtils.isEmpty(noticeUsers)){
            logger.debug("订货单消息接受员为空");
            return ;
        }


        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(templateId);

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);

        // 增加参数，跳转小程序
        /*Map miniprogram = new HashMap<String, String>();
        miniprogram.put("appid", Constant.SMALL_APPID);
        miniprogram.put("pagepath", String.format(Constant.PAGE_PATH, order.getId()));
        
        templateInfo.setMiniprogram(miniprogram);*/
        
        this.getCommonTemplateInfoBySupplier(order , orderStatus , templateInfo);

        final Map<String, Map<String, String>> data = templateInfo.getData() ;

        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("订货方：").append(buy.getName()).append("-").append(need.getName());

        data.put("remark" , new HashMap<String, String>(){{
            this.put("value" , remarks.toString());
        }});

        for(NoticeUser noticeUser : noticeUsers){
            templateInfo.setToUser(noticeUser.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        logger.debug("发送给订货单消息接受员结束：");
    }

    /**
     * 向采购单消息接受员发送消息
     *
     * @param order
     * @param accessToken
     * @param orderStatus
     * @param sell
     * @param buy
     * @param need
     * @param noticeUsers
     * @param remark
     */
    @Override
    public void sendTemplateMessageToPurchaseNoticeUser(Order order, String accessToken, Order.OrderStatus orderStatus, final Supplier sell, final Supplier buy, final Need need, List<NoticeUser> noticeUsers, String smallAccessToken , String templateId , String smallTemplateId , String remark) {

        logger.debug("发送给采购单消息接受员开始：");

        if(CollectionUtils.isEmpty(noticeUsers)){
            logger.debug("采购但消息接受员为空");
            return ;
        }

        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateId(templateId);

        Setting setting = SystemUtils.getSetting() ;
        String url = setting.getSiteUrl() + String.format(noticeUserViewOrderUrl, order.getId());
        templateInfo.setUrl(url);

        this.getCommonTemplateInfo(order , orderStatus , templateInfo);

        Map<String, Map<String, String>> data = templateInfo.getData() ;

        final StringBuffer remarks = new StringBuffer() ;
        remarks.append("客服电话：").append(sell.getCustomerServiceTel()).append(newLines);
        remarks.append("供应商：").append(sell.getName()).append(newLines);
        remarks.append("收货点：").append(need.getName());

        data.put("remark" , new HashMap<String, String>(){{
            this.put("value" , remarks.toString());
        }});

        for(NoticeUser noticeUser : noticeUsers){
            templateInfo.setToUser(noticeUser.getOpenId());
            this.sendTemplateMessage(templateInfo , accessToken) ;
        }

        logger.debug("发送给采购单消息接受员结束：");
    }


    /**
     * 供应商获取通用模版
     * @param order
     * @param orderStatus
     * @param templateInfo
     */
    private void getCommonTemplateInfoBySupplier(final Order order , Order.OrderStatus orderStatus  , TemplateInfo templateInfo){

        if(orderStatus.equals(Order.OrderStatus.shipped)){
            //订单配送
            templateInfo.setData(new HashMap<String, Map<String, String>>(){{
                this.put("first" , new HashMap<String, String>(){{
                    this.put("value" , "您有一笔订单发货");
                }});
                this.put("keyword1" , new HashMap<String, String>(){{
                    this.put("value" , order.getSn());
                }});

                this.put("keyword2" , new HashMap<String, String>(){{
                    this.put("value" , DateUtils.formatDateToString(new Date() , DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark" , new HashMap<String, String>(){{
                    this.put("value" , remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.create)){
            //订单创建
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔新订单需要审核");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(order.getCreateDate(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel)){
            //申请取消
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单申请取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.canceled)){
            //取消
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.completed)){
            //完成
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单完成");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.denied)){
            //审核拒绝
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单已拒绝");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

               /* this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});
*/
            }});

        }else if(orderStatus.equals(Order.OrderStatus.passed)){
            //审核通过
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单已通过");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.updateItems)){
            //前台修改商品数量
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单信息变更");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.admin_updateItems)){
            //后台修改订单商品信息
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单信息变更");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.cancelShipped)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔订单发货取消");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel_passed)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔申请取消通过");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }else if(orderStatus.equals(Order.OrderStatus.applyCancel_denied)){
            //发货作废
            templateInfo.setData(new HashMap<String, Map<String, String>>() {{
                this.put("first", new HashMap<String, String>() {{
                    this.put("value", "您有一笔申请取消拒绝");
                }});
                this.put("keyword1", new HashMap<String, String>() {{
                    this.put("value", order.getSn());
                }});

                this.put("keyword2", new HashMap<String, String>() {{
                    this.put("value", DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm));
                }});

                /*this.put("remark", new HashMap<String, String>() {{
                    this.put("value", remarks.toString());
                }});*/

            }});

        }
    }


    /**
     * 向发起者
     *
     * @param assList          回复的清单
     * @param initiator        发起人
     * @param smallAccessToken
     * @param smallTemplateId
     * @param reply            回复的内容
     * @return
     */
    @Override
    public boolean sendSmallTemplateMessageToInitiator(final AssList assList, AssChildMember initiator, String smallAccessToken, String smallTemplateId, final String reply , String formId) {

        final String replyDateStr = DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm) ;

        SmallTemplateInfo smallTemplateInfo = new SmallTemplateInfo() ;

        smallTemplateInfo.setTemplateId(smallTemplateId);
        smallTemplateInfo.setFormId(formId);
        smallTemplateInfo.setToUser(initiator.getSmOpenId());
        smallTemplateInfo.setPage("pages/jointalk/jointalk?id="+assList.getId());
        smallTemplateInfo.setData(new HashMap<String, Map<String, String>>(){{
        	
        	//订单编号
            this.put("keyword1", new HashMap<String, String>() {{
            	this.put("value", assList.getSn());
            }});
            //订单状态
            this.put("keyword2", new HashMap<String, String>() {{
                this.put("value", "已回复");
            }});
            //回复内容 
            this.put("keyword3", new HashMap<String, String>() {{
            	this.put("value", reply);
            }});
            //回复时间
            this.put("keyword4", new HashMap<String, String>() {{ 
                this.put("value", replyDateStr);
            }});
        }});

        return this.sendSmallTemplateMessage(smallTemplateInfo , smallAccessToken);

    }

    /**
     * 发送小程序模版消息
     *
     * @param smallTemplateInfo
     * @param smallAccessToken
     * @return
     */
    @Override
    public boolean sendSmallTemplateMessage(SmallTemplateInfo smallTemplateInfo, String smallAccessToken) {

        String url = String.format(Constant.SMALL_TEMPLATE_SEND_URL , smallAccessToken) ;
        String jsonStr = JsonUtils.toJson(smallTemplateInfo);
        WebUtils.post(url, jsonStr);
        return true;
    }


    /**
     * 获取订货助手 globalToken
     *
     * @return
     */
    @Cacheable(value="assSmallGlobalAccessToken")
    @Override
    public String getAssSmallGlobalToken() {
        try {
            String access_token_all = "" ;

            StringBuffer urlSb = new StringBuffer(); // 用来拼接参数

            urlSb.append(Constant.GET_GLOBAL_ACCESS_TOKEN + "grant_type=" + Constant.GLOBAL_GRANT_TYPE + "&appid=" + this.assSmallKey + "&secret=" + this.assSmallSecret);

            logger.info("获取ass small access token start:");

            String result = WebUtils.post(urlSb.toString() , new HashMap<String, Object>()) ;

            JSONObject obj = JSONObject.fromObject(result);

            access_token_all = obj.get("access_token") + "";

            logger.info("获取到的ass small access_token值：{}", access_token_all);

            return access_token_all;

        } catch (Exception e) {
            logger.error("get ass small token error!", e);
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 向发起者
     *
     * @param order          回复的订单
     * @param initiator        发起人
     * @param smallAccessToken
     * @param smallTemplateId
     * @param reply            回复的内容
     * @return
     */
	@Override
	public boolean sendSmallTemplateMessageToInitiator(final Order order,
			ChildMember initiator, String smallAccessToken,
			String smallTemplateId, final String reply, String formId) {
		 final String replyDateStr = DateUtils.formatDateToString(new Date(), DateformatEnum.yyyyMMddHHmm) ;

	        SmallTemplateInfo smallTemplateInfo = new SmallTemplateInfo() ;

	        smallTemplateInfo.setTemplateId(smallTemplateId);
	        smallTemplateInfo.setFormId(formId);
	        smallTemplateInfo.setToUser(initiator.getSmOpenId());
	        smallTemplateInfo.setPage("pages/localOrderShare/localOrderShare?orderId="+order.getId());
	        smallTemplateInfo.setData(new HashMap<String, Map<String, String>>(){{
	        	
	        	//订单编号
	            this.put("keyword1", new HashMap<String, String>() {{
	            	this.put("value", order.getSn());
	            }});
	            //订单状态
	            this.put("keyword2", new HashMap<String, String>() {{
	                this.put("value", "已回复");
	            }});
	            //回复内容 
	            this.put("keyword3", new HashMap<String, String>() {{
	            	this.put("value", reply);
	            }});
	            //回复时间
	            this.put("keyword4", new HashMap<String, String>() {{ 
	                this.put("value", replyDateStr);
	            }});
	        }});

	        return this.sendSmallTemplateMessage(smallTemplateInfo , smallAccessToken);
	}

	/**					
	 * 	二维码授权链接
	 *  component_appid	是	第三方平台方appid
		pre_auth_code	是	预授权码
		redirect_uri	是	回调URI
		auth_type		否	要授权的帐号类型，
		 					1 则商户扫码后，手机端仅展示公众号、
		 					2 表示仅展示小程序，
		 					3 表示公众号和小程序都展示。如果为未制定，则默认小程序和公众号都展示。
		 						第三方平台开发者可以使用本字段来控制授权的帐号类型。
	 */
	@Override
	public String authQRCodeBy3rd(String component_appid, String pre_auth_code, String redirect_uri, String auth_type) {
		String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+ component_appid
				+ "&pre_auth_code=" + pre_auth_code + "&redirect_uri=" + redirect_uri 
				+ "&auth_type=" + auth_type;
		return url;
	}
	
	
	
	public String authBy3rd(String component_appid, String pre_auth_code, String redirect_uri, String auth_type, String biz_appid) {
		String url = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=3&no_scan=1&component_appid=" 
					+ component_appid +"&pre_auth_code=" + pre_auth_code + "&redirect_uri="+ redirect_uri 
					+ "&auth_type=" + auth_type + "&biz_appid=" + biz_appid + "#wechat_redirect";
		return url;
	}

	/**
	 * 接受公众号推送的component_verify_ticket
	 */
	@Override
	public String receiveComponent_verify_ticket(String xmlStr) {
		String encodingAesKey = "1234567890qwertyuiopasdfghjklzxcvbnmzaqxswx";
		String token = "1Qaz";
		String timestamp = new Date().getTime() + "";
		String nonce = "xxxxxx";
		String appId = "wxb11529c136998cb6";
		
		WXBizMsgCrypt pc;
		try {
			pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

			String encrypt = nodelist1.item(0).getTextContent();
			String msgSignature = nodelist2.item(0).getTextContent();

			String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
			String fromXML = String.format(format, encrypt);

			//
			// 公众平台发送消息给第三方，第三方处理
			//

			// 第三方收到公众号平台发送的消息
			String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
			logger.info("解密后明文: " + result2);
			if(StringUtils.isNotBlank(result2)){
				sr = new StringReader(result2);
				is = new InputSource(sr);
				document = db.parse(is);
				Element root2 = document.getDocumentElement();
				NodeList nlist1 = root2.getElementsByTagName("AppId");
				NodeList nlist2 = root2.getElementsByTagName("CreateTime");
				NodeList nlist3 = root2.getElementsByTagName("InfoType");
				NodeList nlist4 = root2.getElementsByTagName("ComponentVerifyTicket");

				String AppId = nlist1.item(0).getTextContent();
				String CreateTime = nlist2.item(0).getTextContent();
				String InfoType = nlist3.item(0).getTextContent();
				String ComponentVerifyTicket = nlist4.item(0).getTextContent();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "success";
	}

	@Override
	public boolean sendSmallTemplateMessageToClientMemberForOrder(Order order, ChildMember childMember,
			String smallAccessToken, String smallTemplateId, String pageUrl, String formId) {
		// TODO Auto-generated method stub
		return false;
	}

	//奖励提醒
	@Override
	public boolean sendTemplateMessage2ParentChildMember(final Order order, String templateId, String accessToken) {
		logger.info("【开始 发送模板消息】");
		Member member = order.getMember() ;
		Set<ChildMember> childMembers = member.getChildMembers() ;
        //下单人
        final ChildMember orderChildMem = order.getChildMember() ;
        String childName = "" ;
        if(null != orderChildMem){
            childName = orderChildMem.getNickName() ;
        }
        
        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);
        final String sn = order.getSn();
        BigDecimal newPoint1 = new BigDecimal(0l);
        BigDecimal newPoint2 = new BigDecimal(0l);
        BigDecimal newPoint3 = new BigDecimal(0l);
        for (OrderItem orderItem : order.getOrderItems()) {
        	if(orderItem.getUone_score() != null){
        		newPoint1 = newPoint1.add(orderItem.getDone_score());
        	}
        	if(orderItem.getUtwo_score() != null){
        		newPoint2 = newPoint2.add(orderItem.getDtwo_score());
        	}
        	if(orderItem.getUthree_score() != null){
        		newPoint3 = newPoint3.add(orderItem.getDthree_score());
        	}
		}
        
        Map templateMap = new HashMap<String, Map<String, String>>(){{
            this.put("keyword1" , new HashMap<String, String>(){{
            	 String nickName = order.getChildMember().getNickName();
                 if(StringUtils.isEmpty(nickName)){
                	 nickName = "未更新昵称";
                 }
                this.put("value" , nickName);//"您的下级用户" + nickName + "已成功购买商品，新增奖励已转入您的店主账户");
            }});
            this.put("keyword2" , new HashMap<String, String>(){{
                this.put("value" , "");
            }});
            this.put("keyword3" , new HashMap<String, String>(){{
                this.put("value" , "");
            }});

        }};

        templateInfo.setData(templateMap);
        
        ChildMember childMember = order.getChildMember();
        
        if(childMember.getParent() != null){
    		logger.info("【本人】：" + childMember.getId());
    		logger.info("【上级1】：" + childMember.getParent().getId());
    		ChildMember c1 = childMember.getParent();
    		
    		final String remark = memberSetRemark(order.getAmount().setScale(2, RoundingMode.HALF_UP), 
            		newPoint1.setScale(2, RoundingMode.HALF_UP), 
            		c1.getMember().getBalance().setScale(2, RoundingMode.HALF_UP));
    		final String keyword2= newPoint1.setScale(2, RoundingMode.HALF_UP).toString();
    		templateMap.put("keyword2" , new HashMap<String, String>(){{
                this.put("value" ,keyword2);
            }});
        	templateMap.put("keyword3" , new HashMap<String, String>(){{
                this.put("value" , remark);
            }});
			templateInfo.setData(templateMap);
    		templateInfo.setToUser(c1.getOpenId());
			OrderForm orderForm = orderFormDao.getDoOrderForm(c1);
			if (orderForm != null) {
				templateInfo.setFormId(orderForm.getFormId());
				this.sendTemplateMessage(templateInfo , accessToken) ;
	            logger.info("【发送模板消息成功】：" + c1.getOpenId());
			}
    		ChildMember c2 = c1.getParent();
    	    ChildMember c3 = null;
    		if(c2 != null){
    			final String remark2 = memberSetRemark(order.getAmount().setScale(2, RoundingMode.HALF_UP), 
    	        		newPoint2, c2.getMember().getBalance().setScale(2, RoundingMode.HALF_UP));
    			final String keyword22= newPoint2.setScale(2, RoundingMode.HALF_UP).toString();
        		templateMap.put("keyword2" , new HashMap<String, String>(){{
                    this.put("value" ,keyword22);
                }});
    			templateMap.put("keyword3" , new HashMap<String, String>(){{
                    this.put("value" , remark2);
                }});
    			templateInfo.setData(templateMap);
    	        
    			logger.info("【上级2】：" + c2.getId());
    			templateInfo.setToUser(c2.getOpenId());
    			orderForm = orderFormDao.getDoOrderForm(c2);
    			if (orderForm != null) {
    				templateInfo.setFormId(orderForm.getFormId());
    				this.sendTemplateMessage(templateInfo , accessToken) ;
    			    logger.info("【发送模板消息成功2】：" + c2.getOpenId());
    			}
            
                c3 = c2.getParent();
                if(c3 != null){
                	final String remark3 = memberSetRemark(order.getAmount().setScale(2, RoundingMode.HALF_UP), 
        	        		newPoint3, c3.getMember().getBalance().setScale(2, RoundingMode.HALF_UP));
                	final String keyword222= newPoint3.setScale(2, RoundingMode.HALF_UP).toString();
            		templateMap.put("keyword2" , new HashMap<String, String>(){{
                        this.put("value" ,keyword222);
                    }});
                	templateMap.put("keyword3" , new HashMap<String, String>(){{
                        this.put("value" , remark3);
                    }});
        			templateInfo.setData(templateMap);
        			
                	logger.info("【上级3】：" + c3.getId());
        			templateInfo.setToUser(c3.getOpenId());
        			orderForm = orderFormDao.getDoOrderForm(c3);
        			if (orderForm != null) {
        				templateInfo.setFormId(orderForm.getFormId());
        				this.sendTemplateMessage(templateInfo , accessToken) ;
        			    logger.info("【发送模板消息成功3】：" + c3.getOpenId());
        			}
                }
    		}
    		
    	}
		return false;
	}
	
	
	public String memberSetRemark(BigDecimal amount, BigDecimal newPoint, BigDecimal totalPoint){
		final StringBuffer remarkBuf = new StringBuffer();
        remarkBuf.append("交易金额：");
        remarkBuf.append(amount);
        remarkBuf.append("元");
        remarkBuf.append("\n");
        remarkBuf.append("新增奖励：");
        remarkBuf.append(newPoint);
        remarkBuf.append("\n");
        remarkBuf.append("账户余额：");
        remarkBuf.append(totalPoint);
        remarkBuf.append("\n");
        remarkBuf.append("更新时间：" + DateUtils.formatDateToString(new Date() , DateformatEnum.yyyyMMddHHmm));
        return remarkBuf.toString();
	}
	
	@Override
	public boolean sendTemplateMessage2ChildMemberJoin(final ChildMember childMember, String templateId, String accessToken) {
		logger.info("【开始 发送模板消息 加入 通知上级】");

        TemplateInfo templateInfo = new TemplateInfo() ;
        templateInfo.setTemplateId(templateId);
     
	     //templateInfo.setPage(orderForm.getPage());
        if(StringUtils.isEmpty(childMember.getNickName())){
        	childMember.setNickName("未更新昵称");
        }
        
        Map templateMap = new HashMap<String, Map<String, String>>(){{
            this.put("keyword1" , new HashMap<String, String>(){{
                this.put("value" , childMember.getNickName());
            }});
            this.put("keyword2" , new HashMap<String, String>(){{
                this.put("value" , "0.0");
            }});
            this.put("keyword3" , new HashMap<String, String>(){{
                this.put("value" , childMember.getNickName() + "已通过您的分享，成为您的关联用户，产生消费您将获得奖励积分");
            }});

        }};

        templateInfo.setData(templateMap);
        
        if(childMember.getParent() != null){
    		logger.info("【本人】：" + childMember.getId());
    		logger.info("【上级1】：" + childMember.getParent().getId());
    		ChildMember c1 = childMember.getParent();
			templateInfo.setToUser(c1.getSmOpenId());
			OrderForm orderForm = orderFormDao.getDoOrderForm(c1);
			if (orderForm != null) {
				templateInfo.setFormId(orderForm.getFormId());
				this.sendTemplateMessage(templateInfo, accessToken);
				logger.info("【发送模板消息成功1】：" + c1.getSmOpenId());
			}
			ChildMember c2 = c1.getParent();
    	    ChildMember c3 = null;
    		if(c2 != null){
    			templateMap.put("keyword2" , new HashMap<String, String>(){{
                    this.put("value" , "已成为您的关联用户，产生消费您将获得奖励积分");
                }});
    			templateInfo.setData(templateMap);
    			logger.info("【上级2】：" + c2.getId());
    			templateInfo.setToUser(c2.getSmOpenId());
    			orderForm = orderFormDao.getDoOrderForm(c2);
    			if (orderForm != null) {
    				templateInfo.setFormId(orderForm.getFormId());
    				this.sendTemplateMessage(templateInfo, accessToken);
    				logger.info("【发送模板消息成功2】：" + c2.getSmOpenId());
    			}
                c3 = c2.getParent();
                if(c3 != null){
                	logger.info("【上级3】：" + c3.getId());
        			templateInfo.setToUser(c3.getSmOpenId());
        			orderForm = orderFormDao.getDoOrderForm(c3);
        			if (orderForm != null) {
        				templateInfo.setFormId(orderForm.getFormId());
        				this.sendTemplateMessage(templateInfo, accessToken);
        				logger.info("【发送模板消息成功3】：" + c3.getSmOpenId());
        			}
                }
    		}
    		
    	}

		return false;
	}
	
	
	
}
