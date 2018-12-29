package com.microBusiness.manage.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.CustomerReportDto;
import com.microBusiness.manage.dto.GoodNeedDto;
import com.microBusiness.manage.dto.GoodSupplierDto;
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Order.Status;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.Supplier;

public interface OrderDao extends BaseDao<Order, Long> {

	Order findBySn(String sn);

	List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                         List<com.microBusiness.manage.Order> orders);

	Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable);

	Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

	Long createOrderCount(Date beginDate, Date endDate);

	Long completeOrderCount(Date beginDate, Date endDate);

	BigDecimal createOrderAmount(Date beginDate, Date endDate);

	BigDecimal completeOrderAmount(Date beginDate, Date endDate);

	Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Supplier supplier);
	
	Page<Order> findPage(Order.Type type, Order.Status status,Order.Status[] statuses, 
			Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, 
			Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, 
			Boolean hasExpired, Pageable pageable , Supplier supplier , Date startDate , Date endDate , 
			String searchName , String timeSearch, ChildMember childMember);


	Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Set<Need> needs , Date startDate , Date endDate);
	
	Page<Order> findPage(Order.Type type, Order.Status status,Order.Status[] statuses, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable , Set<Need> needs , Date startDate , Date endDate , String searchName , String timeSearch,Supplier toSupplier);

	/**
	 *
	 * @param startRow
	 * @param offset
	 * @param compareDate
	 * @return
	 */
	List<Order> getOrderInSupply(int startRow, int offset , Date compareDate) ;
	
	int countNumByOrder(Need need , Date startDate , Date endDate , Supplier supplier);
	
	List<Order> findListByIds(Long[] ids);
	
	/**
	 * 查询当前企业下面的商品
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<Product> findByProduct(Order.Status status,Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 查询采购商信息
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<Supplier> findBySupplier(Order.Status status,Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 查询商品信息收货点信息
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<GoodNeedDto> findByGoodsAndNeed(Order.Status status,Supplier supplier, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 查询供货者信息
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<Supplier> findBySuppliers(Order.Status status,Set<Need> needs, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 查询进货信息
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<GoodSupplierDto> findByGoodsAndSupplier(Order.Status status,Set<Need> needs, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 查询采购商产品信息
	 * @param status
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	List<Product> findBuyersByProduct(Order.Status status,Set<Need> needs, Date startDate, Date endDate , String searchName , String timeSearch);
	
	/**
	 * 根据id查询商品信息
	 * @param ids
	 * @return
	 */
	List<Product> findByids(Long[] ids);
	
	/**
	 * 跟据id查询采购商信息
	 * @param ids
	 * @return
	 */
	List<Supplier> queryBuyersByids(Long[] ids);
	
	/**
	 * 根据订单id查询商品信息和收货点信息
	 * @param ids
	 * @return
	 */
	List<GoodNeedDto> queryGoodsAndNeedByids(Long[] ids);
	
	/**
	 * 根据id查询供应商信息
	 * @param ids
	 * @return
	 */
	List<Supplier> querySupplierByids(Long[] ids);
	
	/**
	 * 根据订单id查询进货信息
	 * @param ids
	 * @return
	 */
	List<GoodSupplierDto> queryGoodSupplierByids(Long[] ids);
	
	/**
	 * 根据状态和时间统计订货单报表
	 * 统计直销单和拆单后的直销单、拆单后的分销单中当前企业为分销商的订单
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @return
	 */
	List<OrderReportDto> queryOrderReportByDate(List<Integer> status,Date startDate, Date endDate,Supplier supplier, ChildMember childMember);
	
	/**
	 * 根据状态和时间统计订货单报表
	 * 统计拆单后的分销单中当前企业为供应商的订单
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @return
	 */
	List<OrderReportDto> queryOrderReportByDate2(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	
	/**
	 * 根据状态和时间统计采购单报表
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param supplier
	 * @return
	 */
	List<OrderReportDto> queryPurchaseFormByDate(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	

	/**
	 * 获取订货单的总客户数 
	 * 统计直销单和拆单后的直销单、拆单后的分销单中当前企业为分销商的订单
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @return
	 */
	Integer getCustomersNumber(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	
	/**
	 * 获取采购单的总采购供应商数
	 * @param status 状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @return
	 */
	Integer getPurchaseCustomersNumber(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	
	/**
	 * 根据状态和时间统计客户报表   订单数和订单金额
	 * 个体客户
	 * @param status状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @return
	 */
	List<CustomerReportDto> queryCustomersReportByDate(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	
	/**
	 * 根据状态和时间统计客户报表   订单数和订单金额
	 * 企业客户
	 * @param status状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @return
	 */
	List<CustomerReportDto> queryCustomersReportByDate2(List<Integer> status,Date startDate, Date endDate,Supplier supplier);
	/**
	 * 根据状态和时间统计客户报表   商品数和商品SKU
	 * @param status状态
	 * @param startDate 开始时间 
	 * @param endDate  结束时间
	 * @param supplier 当前用户
	 * @param id   客户id
	 * @return
	 */
	CustomerReportDto queryCustomersReportGoodByDate(List<Integer> status,Date startDate, Date endDate,Supplier supplier,String needId,String supplierId);
	
	/**
	 * 首页按状态做订单统计
	 * @param status
	 * @param supplier
	 * @return
	 */
	Integer searchByStatus(Order.Status status,Supplier supplier);
	/**
	 * 首页查询今天订货单数和订订货单单总金额
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	OrderStatisticsDto todayOrderForm1(Supplier supplier,Date startDate, Date endDate);
	
	/**
	 * 首页查询今天订货单数和订订货单单总金额
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	OrderStatisticsDto todayOrderForm2(Supplier supplier,Date startDate, Date endDate);
	
	/**
	 * 首页查询订货单相关
	 * @param supplier
	 * @return
	 */
	OrderStatisticsDto orderRelated1(Supplier supplier);
	
	/**
	 * 首页查询订货单相关
	 * @param supplier
	 * @return
	 */
	OrderStatisticsDto orderRelated2(Supplier supplier);
	
	/**
	 * 统计订货商品SKU
	 * @param supplier
	 * @return
	 */
	OrderStatisticsDto countOrderGoods(Supplier supplier);
	
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
	 * 订货单走势图
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
	 * 前端查询订单
	 * @param status
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageByMember(Status status, List<Shop> shops, Pageable pageable);
	
	/**
	 * 定货么小程序订单统计
	 * @param member
	 * @param status
	 * @return
	 */
	List<AssListStatisticsDto> findByList(Member member , List<Integer> status);
	
	/**
	 * 定货么小程序订单统计详情
	 * @param member
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<AssListStatisticsDto> kanbandetail(Member member,Date startDate, Date endDate);
	
	/**
	 * 微商小管理同乐订单接口查询
	 * 
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Order> find(Supplier supplier, Date startDate , Date endDate );

	Page<Order> findPageOrderLocal(String sharingStatus,List<Shop> shops, Shop shop, ChildMember childMember, Pageable pageable);

	List<Order> findNoRakeBackList();
}