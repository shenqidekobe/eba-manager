/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.CouponCode;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Invoice;
import com.microBusiness.manage.entity.LocalOrderSharingStatus;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.OrderItemLog;
import com.microBusiness.manage.entity.OrderNeedsForm;
import com.microBusiness.manage.entity.OrderProductForm;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Returns;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.form.OrderItemUpdateForm;

public interface OrderService extends BaseService<Order, Long> {

	Order findBySn(String sn);

	List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                         List<com.microBusiness.manage.Order> orders);

	Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable);

	Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

	BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

	BigDecimal calculateTax(Order order);

	BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

	BigDecimal calculateAmount(Order order);

	boolean isLocked(Order order, Admin admin, boolean autoLock);

	boolean isLocked(Order order, Member member, boolean autoLock);

	void lock(Order order, Admin admin);

	void lock(Order order, Member member);

	void undoExpiredUseCouponCode();

	void undoExpiredExchangePoint();

	void releaseExpiredAllocatedStock();

	Order generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo);

	/**Order create(Order.Type type, Cart cart, Receiver receiver, 
			PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode,
			Invoice invoice, BigDecimal balance, String memo, Date reDate);**/

	void update(Order order, Admin operator);

	void cancel(Order order , Admin admin);

	void review(Order order, boolean passed, Admin operator);

	void payment(Order order, Payment payment, Admin operator);

	void refunds(Order order, Refunds refunds, Admin operator);

	void shipping(Order order, Shipping shipping, Admin operator);

	void returns(Order order, Returns returns, Admin operator);

	void receive(Order order, Admin operator);

	void complete(Order order, Admin operator);

	void fail(Order order, Admin operator);

	Page<Order> findPage(Order.Type type, Order.Status status,Order.Status[] reportStatus,
			Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, 
			Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, 
			Boolean hasExpired, Pageable pageable , Supplier supplier , Date startDate , Date endDate , 
			String searchName , String timeSearch, ChildMember childMember);

	/**
	 * 前端查询订单  平台订单
	 * @param status
	 * @param shops
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageByMember(Order.Status status,List<Shop> shops,Pageable pageable);

	/**
	 * 前端查询订单  本地订单
	 * @param status
	 * @param shops
	 * @param childMember
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageOrderLocal(String sharingStatus,List<Shop> shops,Shop shop,ChildMember childMember,Pageable pageable);

	void shipping(Order order, Shipping shipping, Admin operator , List<OrderFile> orderFiles);


	void review(Order order, boolean passed, Admin operator , List<OrderItem> orderItems , String deniedReason , Date reDate);

	Order create( Cart cart, Shop shop,SupplierType supplierType,Types types,Need need,Set<CartItem> cartItems,String itemIds,
				 PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode,
				 Invoice invoice, BigDecimal balance, String memo, Date reDate , 
				 Long supplierId , SupplyType supplyType , ChildMember childMember,Long relationId, Receiver receiver);

	Order create(Long productId, Integer quantity,
			 PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode,
			 Invoice invoice, BigDecimal balance, String memo, Date reDate , 
			 Long supplierId , SupplyType supplyType , ChildMember childMember, Receiver receiver);
	
	
	
	void applyCancel(Order order);

	/**
	 * 处理用户提交的取消申请操作
	 * @param order
	 * @param passed
	 */
	void dealAppyCancel(Order order , boolean passed , Admin operator);

	/**
	 * 查询我方订单
	 * @param type
	 * @param status
	 * @param member
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @return
	 */
	Page<Order> findPageByOwn(Order.Type type, Order.Status status,Order.Status[] statuses, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier , Date startDate , Date endDate , String searchName , String timeSearch);

	/**
	 *
	 * @param supplierId
	 * @param needId
	 * @param memo
	 * @param reDate
	 * @param orderProductForm
	 * @param operator
	 * @return
	 */

//	Order createByOwn(Long supplierId , Long needId , String memo , Date reDate , OrderProductForm orderProductForm , String operator , boolean isMore , Admin admin,SupplierType supplierType);

	Order createByOwn(Long supplierId , Long needId , String memo , Date reDate , OrderProductForm orderProductForm , String operator , boolean isMore , Admin admin,SupplierType supplierType,Long areaId,String address,String consignee,String tel);

	
	/**
	 *多地址下单      2018-2-2 修改  地址根据页面传参
	 * @param supplierId
	 * @param needId
	 * @param memo
	 * @param reDate
	 * @param orderProductForm
	 * @param operator
	 * @param areaId
	 * @param adderss
	 * @return
	 */
	Order createByOwnMore(SupplyNeed.AssignedModel assignedModel,Supplier supplier , Long needId , String memo , Date reDate , OrderProductForm orderProductForm , String operator , boolean isMore , Admin admin, Long areaId, String adderss);

	/**
	 * 多地址下单
	 * @param supplierId
	 * @param orderProductForm
	 * @param operator
	 * @param orderNeedsForm
	 * @return
	 */
	void createOwnMore(SupplyNeed.AssignedModel assignedModel,Supplier supplier,OrderProductForm orderProductForm , String operator , OrderNeedsForm orderNeedsForm , Admin admin);

	/**
	 *
	 * @param order
	 * @param operatorName 操作人名称
	 */
	void completeByApi(Order order, String operatorName , String supplierName);

	/**
	 * 取消申请
	 * @param order
	 * @param operatorName 申请人
	 * @param supplier 申请人所属企业
	 */
	void applyCancel(Order order , String operatorName , Supplier supplier);

	/**
	 *
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @return
	 */
	ExcelView downOrderOwn(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier , Date startDate , Date endDate , String searchName , String timeSearch);

	/**
	 *
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 */
	ExcelView downOrderCustomer(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch);

	ExcelView downOrder(String fileName , List<Order> orders , String[] titles);
	
	ExcelView downOrder(String fileName , List<Order> orders , String[] titles , Supplier supplier);
	
	ExcelView downPurchaseOrder(String fileName , List<Order> orders , String[] titles);

	void review(Order order, boolean passed, Admin operator , String deniedReason ) ;
	
	void applicationCancel(Order order,Admin operator);

	/**
	 * 后台管理员修改商品数量和收货时间
	 * @param order
	 * @param orderItemUpdateForm
	 * @param admin
	 */
	void updateItems(Order order , OrderItemUpdateForm orderItemUpdateForm ,Admin admin);

	/**
	 * 发货作废
	 * @param order
	 * @param shipping
	 * @param admin
	 */
	void cancelShipped(Order order , Shipping shipping , Admin admin);

	/**
	 *
	 * @param order
	 * @param orderItemUpdateForm
	 * @param operatorName 操作人名称
	 * @param operatorType 操作类型
	 * @param type 操作来源
	 * @param supplierName 企业名称
	 */
	void updateItems(Order order , OrderItemUpdateForm orderItemUpdateForm ,String operatorName , OrderItemLog.OperatorType operatorType , OrderItemLog.Type type , String supplierName);

	/**
	 * 在供应中 的 正式供应 时间段内的下单
	 * @param startRow 开始行
	 * @param offset 偏移量
	 * @param compareDate
	 * @return
	 */
	List<Order> getOrderInSupply(int startRow , int offset , Date compareDate);

	void sendNoOrderNotice(String templateId);
	
	/**
	 * 统计收货点每天下单次数
	 * @param need 收货点
	 * @param member 主账号
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	int countNumByOrder(Need need, Date startDate , Date endDate , Supplier supplier);
	
	/**
	 * 根据订单id查询订单
	 * @param ids
	 * @return
	 */
	List<Order> findListByIds(Long[] ids);
	
	/**
	 * 采购单选中导出订单
	 * @param ids
	 * @return
	 */
	ExcelView exportSelectedOrder(Long[] ids);
	
	/**
	 * 订货单选中导出订单
	 * @param ids
	 * @return
	 */
	ExcelView exportSelectedOrderForm(Long[] ids , Supplier supplier);
	
	/**
	 * 订货单导出食药监报告
	 */
	void reportDownload(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 采购单导出食药监报告
	 */
	void exportFoodMedicine(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 订货单导出选中食药监报告
	 * @param ids
	 * @param supplier
	 * @param request
	 * @param response
	 */
	void selectedReport(Long[] ids , Supplier supplier , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 采购单到处选中食药监报告
	 * @param ids
	 * @param supplier
	 * @param request
	 * @param response
	 */
	void selectedReports(Long[] ids , Supplier supplier , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 订货单批量拆分导出
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	void splitOut(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 订货单选中拆分导出
	 * @param ids
	 * @param supplier
	 * @param request
	 * @param response
	 */
	void selectSplitExport(Long[] ids , Supplier supplier , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 采购单批量拆分导出
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param goods
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isUseCouponCode
	 * @param isExchangePoint
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @param request
	 * @param response
	 */
	void batchSplitOut(Order.Type type, Order.Status status, String memberUsername, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 采购单选中拆分导出
	 * @param ids
	 * @param request
	 * @param response
	 */
	void selectedSplitOut(Long[] ids , Supplier supplier , HttpServletRequest request , HttpServletResponse response);
	
	/**
	 * 首页订货单按状态做订单统计
	 * @param status
	 * @param supplier
	 * @return
	 */
	Integer searchByStatus(Order.Status status,Supplier supplier);
	/**
	 * 首页查询今天订货单数和订货单总金额
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	OrderStatisticsDto todayOrderForm(Supplier supplier,Date startDate, Date endDate);
	
	/**
	 * 首页查询订货单相关
	 * @param supplier
	 * @return
	 */
	OrderStatisticsDto orderRelated(Supplier supplier);
	
	/**
	 * 首页采购单按状态做订单统计
	 * @param status
	 * @param supplier
	 * @return
	 */
	Integer purchaseOrderByStaticQuery(Order.Status status,Supplier supplier);
	
	/**
	 * 首页查询今天采购单数和采购单总金额
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 */
	OrderStatisticsDto todayPurchaseOrder(Supplier supplier,Date startDate, Date endDate);
	
	/**
	 * 首页查询采购单相关
	 * @param supplier
	 * @return
	 */
	OrderStatisticsDto purchaseOrderRelated(Supplier supplier);
	
	/**
	 * 首页订货单走势图
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderStatisticsDto> orderharts(Supplier supplier,Date startDate, Date endDate);
	
	/**
	 * 首页采购单走势图
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderStatisticsDto> purchaseOrderCharts(Supplier supplier,Date startDate, Date endDate);
	/**
	 * 审核拆单
	 * @param order
	 * @param passed
	 * @param operator
	 * @param deniedReason
	 */
	void demolitionOrder(Order order, boolean passed, Admin operator , String deniedReason ,Supplier supplier);

	/**
	 * 流水代下单 向供应商下单  201-2-2修改  客户收货地址可修改，从前端传过来
	 * @param supplierId
	 * @param needId
	 * @param memo
	 * @param reDate
	 * @param orderProductForm
	 * @param operator
	 * @param isMore
	 * @param admin
	 * @param type     由于两个地方用到此方法，用于里面的业务逻辑判断
	 * @param areaId   前端传来的地址id
	 * @param adderss  前端传来的详细地址
	 * @return
	 */
	Order createByOwnFormal(Long supplierId, Long needId, String memo,
			Date reDate, OrderProductForm orderProductForm, String operator,
			boolean isMore, Admin admin,Long areaId, String address,String consignee,String tel,SupplierType supplierType );

	void createOwnMoreFormal(Long supplierId,
			OrderProductForm orderProductForm, String operator,
			OrderNeedsForm orderNeedsForm, Admin admin);

	/**
	 * 定货么小程序订单统计
	 * @param member
	 * @param status
	 * @return
	 */
	Map<Integer , List<AssListStatisticsDto>> purchaseListKanban(Member member , List<Integer> status);
	
	/**
	 * 定货么小程序订单统计详情
	 * @param member
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<String, Object> kanbandetail(Member member,Date startDate, Date endDate);
	
	/**
	 * 微商小管理同乐订单接口查询
	 * 
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Order> find(Supplier supplier, Date startDate , Date endDate );
	
	/**
	 * 
	 * @Title: updateStatus
	 * @date: 2018年3月19日下午3:24:49
	 * @Description: 分享、参与、终结、退出
	 * @return: void
	 */
	void updateStatus(Order order , LocalOrderSharingStatus status , ChildMember childMember);
	
	int countSaleGood(Member member);
	
	int updatePaySn(Member member, Order order);
	
	void payment(Order order, BigDecimal amountPaid, BigDecimal fee, 
			String transactionId, String operator, Map<String, String> weixinReMap);
	
	/**
	 * 分销结算
	 * */
	void distributionSettlement(Order order);
}