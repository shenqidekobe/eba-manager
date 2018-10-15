/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.OrderLogDao;
import com.microBusiness.manage.dao.OrderNewsPushDao;
import com.microBusiness.manage.dao.ShippingDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.LogType;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderLog;
import com.microBusiness.manage.entity.OrderNewsPush;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.exception.ValidExeption;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.util.JsonUtils;
import com.microBusiness.manage.util.SystemUtils;
import com.microBusiness.manage.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("shippingServiceImpl")
public class ShippingServiceImpl extends BaseServiceImpl<Shipping, Long> implements ShippingService {

	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;


	@Resource
	private OrderService orderService ;

	@Resource
	private OrderLogDao orderLogDao ;


	@Value("${qr.sender.path}")
	private String qrSenderPath ;
	
	@Resource
	private OrderNewsPushDao orderNewsPushDao;
	@Resource
	private NeedDao needDao;

	@Transactional(readOnly = true)
	public Shipping findBySn(String sn) {
		return shippingDao.findBySn(sn);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Cacheable("shipping")
	public List<Map<String, String>> getTransitSteps(Shipping shipping) {
		Assert.notNull(shipping);

		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			return Collections.emptyList();
		}
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", setting.getKuaidi100Key());
		parameterMap.put("com", shipping.getDeliveryCorpCode());
		parameterMap.put("nu", shipping.getTrackingNo());
		parameterMap.put("show", "0");
		parameterMap.put("muti", "1");
		parameterMap.put("order", "asc");
		String content = WebUtils.get("http://api.kuaidi100.com/api", parameterMap);
		Map<String, Object> data = JsonUtils.toObject(content, new TypeReference<Map<String, Object>>() {
		});
		if (!StringUtils.equals(String.valueOf(data.get("status")), "1")) {
			return Collections.emptyList();
		}
		return (List<Map<String, String>>) data.get("data");
	}

	@Override
	@Transactional
	public Shipping save(Shipping shipping) {
		Assert.notNull(shipping);

		shipping.setSn(snDao.generate(Sn.Type.shipping));

		return super.save(shipping);
	}


	@Override
	public String getQrPath(Shipping shipping) {
		Setting setting = SystemUtils.getSetting() ;
		String path = setting.getSiteUrl() + String.format(qrSenderPath, shipping.getId());
		/*if(shipping.getStatus().equals(Shipping.Status.waitSenderCheck)){
			path = setting.getSiteUrl() + String.format(qrSenderPath, shipping.getId());
		}*/
		return path;
	}

	@Override
	public void customerCheck(Shipping shipping) {

		Shipping beforeShipping = shippingDao.find(shipping.getId());

		if(null == beforeShipping){
			throw new ValidExeption("参数错误");
		}

		//判断状态是否为等待用户确认状态
		if(!(Shipping.Status.waitCustomerCheck.equals(beforeShipping.getStatus()) || Shipping.Status.senderDenied.equals(beforeShipping.getStatus()))){
			throw new ValidExeption("非法操作");
		}


		List<ShippingItem> shippingItems = beforeShipping.getShippingItems();

		List<ShippingItem> updItems = shipping.getShippingItems();

		Map<Long , ShippingItem> updMap = new HashMap<>();

		//将修改的转换成 map 方便检索
		for(ShippingItem shippingItem : updItems){
			updMap.put(shippingItem.getId() , shippingItem) ;
		}

		for(ShippingItem shippingItem : shippingItems){
			ShippingItem updItem = updMap.get(shippingItem.getId()) ;
			if(null != updItem){
				shippingItem.setRealQuantity(updItem.getRealQuantity());
			}
		}
		//修改配送状态，等待送货员确认
		beforeShipping.setStatus(Shipping.Status.waitSenderCheck);

	}

	@Override
	public void senderCheck(Long id , Boolean passed , String code) {

		Shipping beforeShipping = shippingDao.find(id);
		if(null == beforeShipping){
			throw new ValidExeption("参数错误");
		}

		if(null == passed){
			throw new ValidExeption("非法操作");
		}

		//判断状态是否为等待送货员确认状态
		if(!Shipping.Status.waitSenderCheck.equals(beforeShipping.getStatus())){
			throw new ValidExeption("非法操作");
		}

		if(!beforeShipping.getDeliveryCode().equals(code)){
			throw new ValidExeption("送货码不正确");
		}



		ChildMember childMember = beforeShipping.getChildMember() ;
		String nickName = null == childMember ? "" : childMember.getNickName();


		//String supplierName = childMember.getMember().getNeed().getSupplier().getName();
		Supplier supplier;
		if (beforeShipping.getOrder().getType().equals(Order.Type.billDistribution)) {
			supplier=beforeShipping.getOrder().getToSupplier();
		}else {
			supplier=beforeShipping.getOrder().getSupplier();
		}
		Need need = beforeShipping.getOrder().getNeed();
		String supplierName = supplier.getName();
		if(supplierName == null) {
			supplierName = "";
		}
		if(passed){
			beforeShipping.setStatus(Shipping.Status.senderChecked);
			Order order = beforeShipping.getOrder();

			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.receive);
			orderLog.setOperator(nickName);
			orderLog.setOrder(beforeShipping.getOrder());
			orderLog.setSupplierName(supplierName);
			orderLogDao.persist(orderLog);
			
			//后台消息推送
			OrderNewsPush orderNewsPush = new OrderNewsPush();


			//Need need = order.getMember().getNeed();

//			orderNewsPush.setBuyers(need.getSupplier());
//			orderNewsPush.setSupplier(order.getSupplier());
//			orderNewsPush.setOrder(order);
//			orderNewsPush.setNeed(need);
//			orderNewsPush.setOrderStatus(OrderNewsPush.OrderStatus.complete);
//			orderNewsPush.setStatus(OrderNewsPush.Status.unread);
//			orderNewsPush.setPurchaseViewStatus(OrderNewsPush.PurchaseViewStatus.unread);
//			orderNewsPush.setMark(OrderNewsPush.Mark.all);
//			orderNewsPush.setOperator(OrderNewsPush.Operator.microLetter);
//			orderNewsPushDao.persist(orderNewsPush);

			if(order.getType() == Order.Type.formal) {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.complete, need, order.getToSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.order);
				orderNewsPushDao.addOrderNewPush(order.getToSupplier(), order, OrderNewsPush.OrderStatus.complete, need, order.getToSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.purchase);
			}else {
				orderNewsPushDao.addOrderNewPush(order.getSupplier(), order, OrderNewsPush.OrderStatus.complete, need, order.getSupplier().getName(), need.getName(), OrderNewsPush.NoticeObject.order);
			}
		}else{
			beforeShipping.setStatus(Shipping.Status.senderDenied);
			return ;
		}

		shippingDao.flush();

		//判断是否完成
		Order order = beforeShipping.getOrder() ;

		Set<Shipping> afterShipping = order.getShippings() ;

		boolean allOk = true ;

		for(Shipping shipping : afterShipping){

			if(!shipping.getStatus().equals(Shipping.Status.senderChecked)){
				allOk = false ;
				break ;
			}
		}

		if(allOk && order.getStatus().equals(Order.Status.shipped)){
			orderService.completeByApi(order , nickName , supplierName);
		}

	}


	@Override
	public void customerCheck(Shipping shipping, ChildMember childMember) {

		Shipping beforeShipping = shippingDao.find(shipping.getId());

		if(null == beforeShipping){
			throw new ValidExeption("参数错误");
		}

		//判断状态是否为等待用户确认状态
		if(!(Shipping.Status.waitCustomerCheck.equals(beforeShipping.getStatus()) ||
				Shipping.Status.senderDenied.equals(beforeShipping.getStatus()))){
			throw new ValidExeption("非法操作");
		}


		List<ShippingItem> shippingItems = beforeShipping.getShippingItems();

		List<ShippingItem> updItems = shipping.getShippingItems();

		Map<Long , ShippingItem> updMap = new HashMap<>();

		//将修改的转换成 map 方便检索
		for(ShippingItem shippingItem : updItems){
			updMap.put(shippingItem.getId() , shippingItem) ;
		}

		for(ShippingItem shippingItem : shippingItems){
			ShippingItem updItem = updMap.get(shippingItem.getId()) ;
			if(null != updItem){
				shippingItem.setRealQuantity(updItem.getRealQuantity());
			}
		}
		//修改配送状态，等待送货员确认
		//beforeShipping.setStatus(Shipping.Status.waitSenderCheck);
		beforeShipping.setStatus(Shipping.Status.customerChecked);
		beforeShipping.setChildMember(childMember);
		

	}

    @Override
    public Page<Shipping> getShippingByOrder(Order order, Pageable pageable) {
        // TODO Auto-generated method stub
        return shippingDao.getShippingByOrder(order,pageable);
    }
}