package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by yuezhiwei on 2017/9/13.
 * 功能描述：后台订单消息推送
 * 修改记录：
 */
@Entity
@Table(name = "t_news_push")
@SequenceGenerator(name = "sequenceGenerator" , sequenceName = "seq_news")
public class OrderNewsPush extends BaseEntity<Long> {

	private static final long serialVersionUID = 2507270862742893776L;
	//企业
	private Supplier supplier;
	//收货点
	private Need need;
	//订单
	private Order order;
	
	public enum OrderStatus {
		// 下单
		placeAnOrder,
		// 申请取消
		applicationCancel,
		// 订单留言
		leaveAMessage,
		// 订单修改
		modify,
		// 完成
		complete,
		// 审核通过
		reviewBy,
		// 拒绝
		refuse,
		// 发货
		deliverGoods,
		//同意申请取消
		agreeApplicationCancel,
		//拒绝申请取消
		refuseApplicationCancel,
		//取消
		cancel
		
	}
	
	//订单查看状态
	private OrderStatus orderStatus;
	
	public enum Status {
		// 未读
		unread,
		// 已读
		haveRead
	}
	
	//订货单查看状态
	private Status status;
	
	//发送内容
	private String send;
	
	//接收内容
	private String receive;
	
	public enum NoticeObject {
		//订货单
		order,
		//采购单
		purchase
	}
	
	//通知对象
	private NoticeObject noticeObject;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders" , nullable = true, updatable = false ,foreignKey = @ForeignKey(name = "null"))
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public NoticeObject getNoticeObject() {
		return noticeObject;
	}

	public void setNoticeObject(NoticeObject noticeObject) {
		this.noticeObject = noticeObject;
	}
}
