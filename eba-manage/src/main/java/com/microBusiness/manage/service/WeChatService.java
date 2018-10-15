package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;

import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.SmallTemplateInfo;
import org.aspectj.weaver.ast.Not;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by afei.
 * User: afei
 * Date: 2016/5/26 9:53
 * Describe:
 * Update:
 */
public interface WeChatService {

    Member getUserInfo(String openId);

    String getCode(String url);

    Map<String, String> getWebAuthAccessToken(String code);
    
    Map<String, String> getSmallAccessToken(String code);

    Member getWeChatUserFromUnionId(Map<String, String> map, String access_token);

    ChildMember getChildFromUnionId(Map<String, String> map, String access_token);

    String getGlobalToken();
    
    String getSmallGlobalToken();

    /**
     * @param templateInfo
     * @return
     */
    boolean sendTemplateMessage(TemplateInfo templateInfo, String access_token);

    /**
     * @param openid      微信用户openid
     * @param sn          订单sn
     * @param messageType 消息类型：1.下单支付成功推送（配送）,2.下单支付成功推送（自提）,3.备货完成（配送）,4.备货完成（自提）
     * @param payTime     支付时间
     * @return
     */
    public boolean sendMessageToUser(String openid, String sn, int messageType, String payTime);


    boolean sendTemplateMessage(Order order , String templateId , String accessToken , Order.OrderStatus orderStatus);

    boolean sendTemplateMessage(Order order , String templateId , String accessToken , Order.OrderStatus orderStatus , String remark);

    /**
     * 获取用户基本信息（包括UnionID机制）
     * @param openId
     * @param accessToken
     * @return
     */
    WeChatUserInfo getWeChatUserByUnion(String openId , String accessToken);

    /**
     * 配置的接收员发送模版消息
     * @param supplier
     * @param order
     * @param orderStatus
     * @param templateId
     * @return
     */
    boolean sendTemplateMessageToNoticeUser(Supplier supplier , Order order , Order.OrderStatus orderStatus , String templateId , String accessToken);

    /**
     * 订单有备注的时候增加消息提醒 ， 采购方提醒接受自己的备注提醒
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @return
     */
    boolean sendOrderRemarkNotice(Order order , Member member , String templateId , String accessToken , String memo);

    /**
     * 订单有备注的时候增加消息提醒 ， 供应方消息接受员接受用户备注信息
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @param noticeUsers
     * @return
     */
    boolean sendOrderRemarkNotice(Order order , Member member , String templateId , String accessToken , List<NoticeUser> noticeUsers , String memo);

    /**
     *
     * @param order
     * @param member
     * @param supplier
     * @param templateId
     * @param accessToken
     * @param memo
     * @return
     */
    boolean sendOrderRemarkNotice(Order order , Member member , Supplier supplier ,String templateId , String accessToken , String memo);


    /**
     * 采购单消息接受员接受模版消息
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
    boolean sendTemplateMessageToNoticeUserPurchase(Supplier supplier , Supplier purchaseSupplier , Need need , Order order , Order.OrderStatus orderStatus , String templateId , String accessToken , NoticeTypePurchase.Type typePurchase , String remark);

    /**
     * 采购单消息接受员接受模版消息
     * @param order
     * @param orderStatus
     * @param templateId
     * @param accessToken
     * @param typePurchase
     * @param remark
     * @return
     */
    boolean sendTemplateMessageToNoticeUserPurchase(Order order , Order.OrderStatus orderStatus , String templateId , String accessToken , NoticeTypePurchase.Type typePurchase , String remark);


    /**
     * 订单有备注的时候增加消息提醒 ， 采购方消息接受员接受消息提醒
     * @param order
     * @param member
     * @param templateId
     * @param accessToken
     * @param isPurchase 是否采购方备注
     * @return
     */
    boolean sendOrderRemarkNoticeToNoticeUserPurchase(Order order , Member member , String templateId , String accessToken , String memo , boolean isPurchase);


    String getCode2(String url);
    
    Map<String, String> getWebAuthAccessToken2(String code);
    
    String getGlobalToken2();

    /**
     * 消息接受员接受消息通知 , 根据订单操作（包含接受员和C端用户）
     * @param order
     * @param orderStatus
     * @param accessToken 公众号accessToken
     * @param smallAccessToken 小程序accessToken
     * @param templateId 公众号模版Id
     * @param smallTemplateId 小程序模版Id
     * @param remark 模版消息中的部分remark（比如拒绝原因）
     * @return
     */
    boolean sendTemplateMessageByOrderStatus(Order order , Order.OrderStatus orderStatus , String accessToken , String smallAccessToken , String templateId , String smallTemplateId , String remark);

    /**
     *
     * @param order
     * @param orderStatus
     * @param accessToken 公众号accessToken
     * @param smallAccessToken 小程序accessToken
     * @param templateId 公众号模版Id
     * @param smallTemplateId 小程序模版Id
     * @param sell 卖方企业
     * @param buy 买方企业
     * @param member 下单主账号
     * @param need 个体客户关系
     * @param remark 模版消息中的部分remark（比如拒绝原因）
     * @return
     */
    boolean sendTemplateMessageByOrderStatus(Order order , Order.OrderStatus orderStatus , String accessToken , String smallAccessToken , String templateId , String smallTemplateId , Supplier sell , Supplier buy , Member member , Need need , String remark);

    /**
     * 拆单消息通知
     * @param orders
     * @param need
     * @param member
     * @param sell
     * @param accessToken
     * @param smallAccessToken
     * @param templateId
     * @param smallTemplateId
     * @param mainOrder
     * @param partDate
     * @return
     */
    boolean sendTemplateMessageByOrderPart(List<Order> orders , Need need , Member member , ChildMember childMember , Supplier sell , String accessToken , String smallAccessToken , String templateId , String smallTemplateId , Order mainOrder , Date partDate);

    /**
     * 备注消息提醒
     * @param order
     * @param accessToken
     * @param smallAccessToken
     * @param templateId
     * @param smallTemplateId
     * @param memo
     * @param msgType
     * @param logType 操作类型
     * @return
     */
    boolean sendTemplateMessageByNotice(Order order , String accessToken , String smallAccessToken , String templateId , String smallTemplateId , String memo ,  MsgType msgType,LogType logType,Supplier sell);


    /**
     * 向客户发送模版消息
     * @param order
     * @param accessToken
     * @param childMembers
     * @param sell 卖方企业
     * @param need 客户
     * @param remark
     */
    void sendTemplateMessageToCustomer(Order order , Order.OrderStatus orderStatus , String accessToken , Set<ChildMember> childMembers , Supplier sell , Supplier buy , Need need , String smallAccessToken , String templateId , String smallTemplateId , String remark);

    /**
     * 向订货单消息接受员发送消息
     * @param order
     * @param accessToken
     * @param orderStatus
     * @param sell
     * @param buy
     * @param need
     * @param noticeUsers
     */
    void sendTemplateMessageToOrderNoticeUser(Order order , String accessToken , Order.OrderStatus orderStatus , Supplier sell , Supplier buy , Need need , List<NoticeUser> noticeUsers , String smallAccessToken , String templateId , String smallTemplateId , String remark);

    /**
     * 向采购单消息接受员发送消息
     * @param order
     * @param accessToken
     * @param orderStatus
     * @param sell
     * @param buy
     * @param need
     * @param noticeUsers
     */
    void sendTemplateMessageToPurchaseNoticeUser(Order order , String accessToken , Order.OrderStatus orderStatus , Supplier sell , Supplier buy , Need need , List<NoticeUser> noticeUsers , String smallAccessToken , String templateId , String smallTemplateId , String remark);

    /**
     * 向发起者
     * @param assList 回复的清单
     * @param initiator 发起人
     * @param smallAccessToken
     * @param smallTemplateId
     * @param reply 回复的内容
     * @param formId 微信返回的form_id
     * @return
     */
    boolean sendSmallTemplateMessageToInitiator(AssList assList , AssChildMember initiator , String smallAccessToken , String smallTemplateId , String reply , String formId) ;

    /**
     * 发送小程序模版消息
     * @param smallTemplateInfo
     * @param smallAccessToken
     * @return
     */
    boolean sendSmallTemplateMessage(SmallTemplateInfo smallTemplateInfo, String smallAccessToken);

    /**
     * 获取订货助手 globalToken
     * @return
     */
    String getAssSmallGlobalToken();
    
    /**
     * 向发起者
     * @param order 回复的订单
     * @param initiator 发起人
     * @param smallAccessToken
     * @param smallTemplateId
     * @param reply 回复的内容
     * @param formId 微信返回的form_id
     * @return
     */
    boolean sendSmallTemplateMessageToInitiator(Order order , ChildMember initiator , 
    		String smallAccessToken , String smallTemplateId , String reply , String formId) ;
    
    
    boolean sendSmallTemplateMessageToClientMemberForOrder(Order order , ChildMember childMember , 
    		String smallAccessToken , String smallTemplateId , String pageUrl , String formId) ;
    
    
    String authQRCodeBy3rd(String component_appid, String pre_auth_code, String redirect_uri, String auth_type);
    
    
    String receiveComponent_verify_ticket(String xmlStr);
    
    
    boolean sendTemplateMessage2ParentChildMember(Order order , String templateId , String accessToken);
    
    boolean sendTemplateMessage2ChildMemberJoin(final ChildMember childMember, String templateId, String accessToken);
    

}
