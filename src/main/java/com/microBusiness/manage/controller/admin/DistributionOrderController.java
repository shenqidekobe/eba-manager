package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Payment;
import com.microBusiness.manage.entity.Refunds;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.DeliveryCorpService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("distributionOrderController")
@RequestMapping("/admin/distributionOrder")
public class DistributionOrderController extends BaseController {
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;
	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;
	@Resource
	private NeedService needService;
	@Value("${orderItem.updateTotal.admin}")
	//后台修改的次数
	private Integer itemAdminUpdateTotal ;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Order.Type type, Order.Status status,Order.Status[] statuses, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {
		type=Order.Type.distribution;
		model.addAttribute("types", Order.Type.values());
		model.addAttribute("statuses", Order.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("isPendingReceive", isPendingReceive);
		model.addAttribute("isPendingRefunds", isPendingRefunds);
		model.addAttribute("isAllocatedStock", isAllocatedStock);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        model.addAttribute("timeSearch", timeSearch);
        //只查询等待审核、申请取消两个状态的分销订单
    	statuses=new Order.Status[2];
    	statuses[0]=Order.Status.pendingReview;
    	statuses[1]=Order.Status.applyCancel;
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("useExpired",""+supplier.isExpired());
		model.addAttribute("whetherCertify", ""+supplier.isProbation());
		model.addAttribute("supplierStatic", supplier.getStatus());
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());

		model.addAttribute("page", orderService.findPage(type, status,statuses, 
				null, null, isPendingReceive, isPendingRefunds, null, null, 
				isAllocatedStock, hasExpired, pageable , supplier , startDate ,  
				endDate , searchName , timeSearch, null));
		//统计页面跳转带多个状态
		if (statuses!=null) {
			for (Order.Status status2 : statuses) {
				model.addAttribute(status2.toString(), status2.toString());
			}
		}
		return "/admin/order/distribution_list";
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model , HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("methods", Payment.Method.values());
		model.addAttribute("refundsMethods", Refunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		Order order = orderService.find(id) ;
		Need need = order.getNeed();
		order.setNeed(need);
		model.addAttribute("order", order);

		model.addAttribute("isOverUpdate" , order.getItemAdminUpdate().compareTo(itemAdminUpdateTotal) >=0 ? true : false) ;
		if (DeviceUtils.getCurrentDevice(request).isMobile()) {
			//订单物流动态数量
			model.addAttribute("shippingSize" , order.getShippings().size()) ;
			//订单附件数量
			model.addAttribute("orderFilesSize" , order.getOrderFiles().size()) ;
			//订单日志数量
			model.addAttribute("orderLogSize", order.getOrderLogs().size());
			//订单备注数量
			model.addAttribute("orderRemarksSize", order.getOrderRemarks().size());
			//订单商品数量
			model.addAttribute("quantitySize", order.getQuantity());
		}
		return "/admin/order/distribution_view";
	}
	
	@RequestMapping(value = "/review", method = RequestMethod.POST)
	public String review(Long id, Boolean passed, RedirectAttributes redirectAttributes , String deniedReason) {

		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		Supplier supplier = super.getCurrentSupplier();
		orderService.demolitionOrder(order, passed, admin , deniedReason,supplier);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);



		return "redirect:view.jhtml?id=" + id;
	}
	
	/**
	 * 批量审核
	 * @param orderIds 订单id
	 * @param passed
	 * @param redirectAttributes
	 * @param deniedReason
	 * @return
	 */
	@RequestMapping(value = "/batchReview" , method = RequestMethod.POST)
	public String batchReview(Long[] orderIds , Boolean passed, RedirectAttributes redirectAttributes , String deniedReason) {
		if(orderIds == null) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		Supplier supplier = super.getCurrentSupplier();

		for(int i=0 ; i<orderIds.length ; i++) {
			Order order = orderService.find(orderIds[i]);
			if(order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus())) {
				return ERROR_VIEW;
			}
			if(orderService.isLocked(order, admin, true)) {
				return ERROR_VIEW;
			}
			orderService.demolitionOrder(order, passed, admin, deniedReason,supplier);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?";
	}
	@RequestMapping(value = "/applyCancel", method = RequestMethod.POST)
	public String applyCancel(Long id, Boolean passed , RedirectAttributes redirectAttributes) {

		Order order = orderService.find(id);
		if (order == null || order.hasExpired()) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}

		orderService.dealAppyCancel(order , passed , admin);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

		return "redirect:view.jhtml?id=" + id;
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired()/* || !Order.Status.received.equals(order.getStatus())*/) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.cancel(order , admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}
	
	@RequestMapping(value = "/getOutDownload", method = RequestMethod.GET)
	public ModelAndView download(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {

		Supplier supplier = super.getCurrentSupplier();

		return new ModelAndView(orderService.downOrderCustomer(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch), model);
	}
	
	
	/**
	 * 批量拆分导出
	 * @param type
	 * @param status
	 * @param memberUsername
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param model
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOutSplitOut", method = RequestMethod.GET)
	public void splitOut(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response) {

		Supplier supplier = super.getCurrentSupplier();
		orderService.splitOut(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, supplier, startDate, endDate, searchName, timeSearch, request, response);
	}
	
	/**
	 * 手机版后台分销单审核列表
	 * @param type
	 * @param status
	 * @param statuses
	 * @param isPendingReceive
	 * @param isPendingRefunds
	 * @param isAllocatedStock
	 * @param hasExpired
	 * @param pageable
	 * @param model
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @param timeSearch
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
	public JsonEntity asyncList(Order.Type type, Order.Status status,Order.Status[] statuses, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {
		type=Order.Type.distribution;
		
        //只查询等待审核、申请取消两个状态的分销订单
    	statuses=new Order.Status[2];
    	statuses[0]=Order.Status.pendingReview;
    	statuses[1]=Order.Status.applyCancel;
        
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier = super.getCurrentSupplier();

		model.addAttribute("page", orderService.findPage(type, status,statuses, null, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate ,  endDate , searchName , timeSearch, null));
		
		Page<Order> page = orderService.findPage(type, status,statuses, null, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate ,  endDate , searchName , timeSearch, null);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList=new ArrayList<>();
		for (Order order : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("orderId", order.getId());
			map.put("sn", order.getSn());
			map.put("status", order.getStatus().ordinal());
			map.put("statusString", order.getStatus());
			map.put("needName", order.getNeed().getName());
			map.put("quantity", order.getQuantity());
			orderList.add(map);
		}
		resultMap.put("orderList", orderList);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 手机版后台分销单订单附件列表
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/orderAttachmentList" , method = RequestMethod.GET)
	public String orderAttachmentList(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("orderFiles", order.getOrderFiles());
		return "/admin/order/distribution_annex_mobile";
	}
	
	/**
	 * 手机版后台分销单物流信息
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/logisticsInfo" , method = RequestMethod.GET)
	public String logisticsInfo(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("shippings", order.getShippings());
		return "/admin/order/distribution_logistics_mobile";
	}
	
	/**
	 * 手机版后台分销单商品信息
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/productInfo" , method = RequestMethod.GET)
	public String productInfo(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("orderItems", order.getOrderItems());
		model.addAttribute("orderItemLogs", order.getOrderItemLogs());
		model.addAttribute("quantity", order.getQuantity());
		return "/admin/order/distribution_product_mobile";
	}
	
	/**
	 * 手机版后台分销单备注
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/remarksList" , method = RequestMethod.GET)
	public String remarksList(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("type", order.getType());
		model.addAttribute("orderRemarks", order.getOrderRemarks());
		return "/admin/order/distribution_remarks_mobile";
	}
	
	/**
	 * 手机版后台分销单订单记录
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/ownLog" , method = RequestMethod.GET)
	public String ownLog(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("orderLogs", order.getOrderLogs());
		return "/admin/order/distribution_log_mobile";
	} 
	
	/**
	 * 手机版后台分销订单搜索页面
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search() {
		return "/admin/order/distribution_search_mobile";
	}
}
