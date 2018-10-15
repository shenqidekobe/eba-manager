package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.NoticeUserDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.OrderNeedsForm.OrderNeedsItem;
import com.microBusiness.manage.entity.OrderRemarks.MsgType;
import com.microBusiness.manage.form.OrderItemUpdateForm;
import com.microBusiness.manage.service.*;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mingbai on 2017/4/12.
 * 功能描述：我方订单管理
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/ownOrder")
public class OwnOrderController extends BaseController {
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;
    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;
    @Resource(name = "deliveryCorpServiceImpl")
    private DeliveryCorpService deliveryCorpService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;

    @Resource
    private SupplierService supplierService ;

    @Resource
    private NeedService needService ;
    
    @Resource
    private OrderRemarksService orderRemarksService;
    
    @Resource
    private OrderSettingService orderSettingService;

    @Resource
    private WeChatService weChatService ;

    @Value("${ordderitem.updateTotal.customer}")
    //前台修改次数
    private Integer itemCustomerUpdateTotal ;

    @Value("${order.template.common.templateId}")
    private String commonTemplateId;

    @Resource
    private NoticeUserDao noticeUserDao ;

    /**
     *
     * @param type
     * @param status
     * @param memberUsername
     * @param isPendingReceive
     * @param isPendingRefunds
     * @param isAllocatedStock
     * @param hasExpired
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Order.Type type, Order.Status status,Order.Status[] statuses, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {
    	model.addAttribute("types", Order.Type.values());
        model.addAttribute("statuses", Order.Status.values());
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("memberUsername", memberUsername);
        model.addAttribute("isPendingReceive", isPendingReceive);
        model.addAttribute("isPendingRefunds", isPendingRefunds);
        model.addAttribute("isAllocatedStock", isAllocatedStock);
        model.addAttribute("hasExpired", hasExpired);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("searchName", searchName);
        model.addAttribute("timeSearch", timeSearch);
        
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

        Member member = memberService.findByUsername(memberUsername);
        if (StringUtils.isNotEmpty(memberUsername) && member == null) {
            model.addAttribute("page", Page.emptyPage(pageable));
        } else {
            model.addAttribute("page", orderService.findPageByOwn(null,status,statuses, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch));
        }
      //统计页面跳转带多个状态
      	if (statuses!=null) {
      		for (Order.Status status2 : statuses) {
      			model.addAttribute(status2.toString(), status2.toString());
      		}
      	}
        return "/admin/order/own_list";
    }


    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model, HttpServletRequest request) {
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
        model.addAttribute("isOverUpdate" , order.getItemCustomerUpdate().compareTo(itemCustomerUpdateTotal) >=0 ? true : false) ;
        if (DeviceUtils.getCurrentDevice(request).isMobile()) {
			//订单物流动态数量
			model.addAttribute("shippingSize" , order.getShippings().size()) ;
			//订单附件数量
			model.addAttribute("orderFilesSize" , order.getOrderFiles().size()) ;
			//订单日志数量
			model.addAttribute("orderLogSize", order.getOrderLogs().size());
			//订单备注数量
			Integer remarksSize=0;
			if (order.getSupplyType().equals(SupplyType.temporary)) {
				for (OrderRemarks orderRemarks : order.getOrderRemarks()) {
					if (orderRemarks.getMsgType().equals(MsgType.btob)) {
						remarksSize++;
					}
				}
			}else {
				remarksSize=order.getOrderRemarks().size();
			}
			model.addAttribute("orderRemarksSize", remarksSize);
			//订单商品数量
			model.addAttribute("quantitySize", order.getQuantity());
		}
        return "/admin/order/own_view";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model , RedirectAttributes redirectAttributes) {
        Supplier currSupplier = super.getCurrentSupplier() ;

        if(null == currSupplier){
            addFlashMessage(redirectAttributes, Message.error("您不是供应商"));
            return "redirect:list.jhtml";
        }

        model.put("needs" , currSupplier.getGeneralNeeds());

        return "/admin/order/own_add" ;
    }


    @RequestMapping(value = "/getSuppliers", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long , Map<String , Object>> getSuppliers(Long needId , ModelMap model , RedirectAttributes redirectAttributes) {
        Need need = needService.find(needId);
        List<Supplier> suppliers = supplierService.findFormal(need , null);
        Map<Long , Map<String , Object>> ret = new HashMap();

        for(final Supplier supplier : suppliers){
            ret.put(supplier.getId() , new HashMap<String , Object>(){{
                this.put("name" , supplier.getName());
            }});
        }
        return ret ;
    }


//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public String create(Long supplier , Long need ,  String memo ,  ModelMap model , Date reDate , OrderProductForm orderProductForm , RedirectAttributes redirectAttributes) {
//    	Admin admin = adminService.getCurrent();
//        if(null == supplier || null == need || null == reDate || null == orderProductForm){
//            addFlashMessage(redirectAttributes, Message.error("参数错误"));
//            return "redirect:list.jhtml";
//        }
//        orderService.createByOwn(supplier , need  , memo , reDate , orderProductForm , adminService.getCurrentUsername() , false , admin) ;
//
//        return "redirect:list.jhtml";
//    }
//
//
//    @RequestMapping(value = "/addMore", method = RequestMethod.GET)
//    public String addMore(ModelMap model , RedirectAttributes redirectAttributes) {
//
//        Supplier currSupplier = super.getCurrentSupplier() ;
//        if(null == currSupplier){
//            addFlashMessage(redirectAttributes, Message.error("您不是供应商"));
//            return "redirect:list.jhtml";
//        }
//
//        model.put("suppliers" , supplierService.getSupplierFromBy(currSupplier.getId()));
//
//        return "/admin/order/own_add_more" ;
//    }



//    @RequestMapping(value = "/createMore", method = RequestMethod.POST)
//    public String createMore(Long supplier , OrderProductForm orderProductForm , RedirectAttributes redirectAttributes , OrderNeedsForm orderNeedsForm) {
//
//        if(null == supplier || null == orderProductForm || CollectionUtils.isEmpty(orderProductForm.getOwnOrderItems()) || null == orderNeedsForm || CollectionUtils.isEmpty(orderNeedsForm.getOrderNeedsItems()) ){
//            addFlashMessage(redirectAttributes, Message.error("参数错误"));
//            return "redirect:list.jhtml";
//        }
//        Admin admin = adminService.getCurrent() ;
//        orderService.createOwnMore(supplier , orderProductForm  , admin.getUsername() , orderNeedsForm , admin);
//        return "redirect:list.jhtml";
//    }


    @RequestMapping(value = "/getOutDownload", method = RequestMethod.GET)
    public ModelAndView download(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {

        Supplier supplier = super.getCurrentSupplier();

        return new ModelAndView(orderService.downOrderOwn(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch), model);
    }
    
    /*
     * 未发货采购单后台PC申请取消
     */
    @RequestMapping(value = "/applicationCancel",method = RequestMethod.POST)
    public String applicationCancel(Long id,RedirectAttributes redirectAttributes) {
    	Order order = orderService.find(id);
    	if(order == null) {
    		return ERROR_VIEW;
    	}
    	if(!(order.getStatus() == Order.Status.pendingReview || order.getStatus() == Order.Status.pendingShipment)) {
    		return ERROR_VIEW;
    	}
    	Admin admin = adminService.getCurrent();
    	if(orderService.isLocked(order, admin, true)) {
    		return ERROR_VIEW;
    	}
    	orderService.applicationCancel(order, admin);
    	
    	//向供应商的接收员发送模版消息
        //weChatService.sendTemplateMessageToNoticeUser(order.getSupplier() , order , Order.OrderStatus.applyCancel , commonTemplateId , weChatService.getGlobalToken() ) ;
    	
    	addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    	return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 修改商品数量和收货时间
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/updateItems", method = RequestMethod.POST)
    public String updateItems(Long id, RedirectAttributes redirectAttributes , OrderItemUpdateForm orderItemUpdateForm , ModelMap modelMap) {
        Order order = orderService.find(id);
        if (order == null || order.hasExpired()/* || !Order.Status.received.equals(order.getStatus())*/) {
            return ERROR_VIEW;
        }
        if(!Order.Status.pendingReview.equals(order.getStatus())){
            modelMap.addAttribute("errorMessage" , "状态错误");
            return ERROR_VIEW;
        }
        if(itemCustomerUpdateTotal.compareTo(order.getItemCustomerUpdate()) <= 0){
            modelMap.addAttribute("errorMessage" , "超过次数");
            return ERROR_VIEW;
        }
        Admin admin = adminService.getCurrent();
        if (orderService.isLocked(order, admin, true)) {
        	modelMap.addAttribute("errorMessage" , "订单被锁定，请稍后再操作");
            return ERROR_VIEW;
        }

        orderService.updateItems(order , orderItemUpdateForm , admin.getUsername() , OrderItemLog.OperatorType.update , OrderItemLog.Type.admin , super.getCurrentSupplier().getName());

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + id;
    }
    
    /**
	 * 添加备注
	 * @param orderId
	 * @param orderRemarks
	 * @return
	 */
	@RequestMapping(value = "/addRemarks",method = RequestMethod.POST)
	public @ResponseBody Message addRemarks(Long orderId,OrderRemarks orderRemarks,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Order order = orderService.find(orderId);
		if(order == null) {
			return ERROR_MESSAGE;
		}
		Admin admin = adminService.getCurrent();
		//备注人所在供应商
		
		Supplier supplier = admin.getSupplier();
		String supplierName = admin.getSupplier().getName();
		if (orderService.isLocked(order, admin, true)) {
			return Message.warn("订单被锁定，请稍后再操作");
		}
		//orderRemarksService.saveOrderRemarks(order, admin,orderRemarks,supplierName);
		orderRemarksService.savePurchaseOrderRemarks(order, admin, orderRemarks, supplierName);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

		//发送模版消息
		if(StringUtils.isNotEmpty(orderRemarks.getDescription())){
			weChatService.sendTemplateMessageByNotice(order , weChatService.getGlobalToken() , null , this.commonTemplateId , null,orderRemarks.getDescription(),orderRemarks.getMsgType(),orderRemarks.getLogType(),supplier);
		}

		return SUCCESS_MESSAGE;
	}
	
	
	/**
	 * 
	 * 收货点代下单 验证 下单时间和下单次数
	 * @param supplier
	 * @param need
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/conditionVerif", method = RequestMethod.GET)
	public Map<String, Object> conditionVerif(Long supplier , Long need){
		Map<String, Object> model=new HashMap<>();
		//根据supplier查询供应商的规则设置
        OrderSetting orderSetting = orderSettingService.findBySupplier(supplier);

        if(null == orderSetting){
            orderSetting = orderSettingService.setDefaultValue() ;
        }

        DateTime canOrderTime = new DateTime().plusDays(orderSetting.getTimeReceipt()) ;
        model.put("minDate", DateUtils.formatDateToString(canOrderTime.toDate() , DateformatEnum.yyyyMMddHHmmss2));
        if(orderSetting == null || orderSetting.getEndTime() == null || orderSetting.getStartTime() == null) {
        	orderSetting = orderSettingService.setDefaultValue();
        }
        //判断下单时间
        if(!orderSettingService.compareTime(orderSetting)) {
        	model.put("exists", false);
        	model.put("msg", "当前供应商每天"+orderSetting.getStartTime()+"至"+orderSetting.getEndTime()+"之间允许下单！");
        	return model;
        }
        Need need2 = needService.find(need);
        Date startDate = DateUtils.currentStartTime();
        Date endDate = DateUtils.currentEndTime();
        
        int count = orderService.countNumByOrder(need2 , startDate, endDate , supplierService.find(supplier));//得到当天下单次数
        if(count >= orderSetting.getNumberTimes()) {
        	model.put("exists", false);
        	model.put("msg", "您今天的订单已经超过"+orderSetting.getNumberTimes()+",不能再下单");
        	return model;
        }
        model.put("exists", true);
		return model;
	}
	
	/**
	 * 多地址代下单验证下单时间，收货时间
	 * @param supplier
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/manyaddressVerify", method = RequestMethod.GET)
	public Map<String, Object> manyaddressVerify(Long supplier){
		Map<String, Object> model=new HashMap<>();
		//根据supplier查询供应商的规则设置
        OrderSetting orderSetting = orderSettingService.findBySupplier(supplier);
        if(null == orderSetting){
            orderSetting = orderSettingService.setDefaultValue() ;
        }
        //收货时间
        DateTime canOrderTime = new DateTime().plusDays(orderSetting.getTimeReceipt()) ;
        model.put("minDate", DateUtils.formatDateToString(canOrderTime.toDate() , DateformatEnum.yyyyMMddHHmmss2));
        if(orderSetting == null || orderSetting.getEndTime() == null || orderSetting.getStartTime() == null) {
        	orderSetting = orderSettingService.setDefaultValue();
        }
        //判断下单时间
        if(!orderSettingService.compareTime(orderSetting)) {
        	model.put("exists", false);
        	model.put("msg", "当前供应商每天"+orderSetting.getStartTime()+"至"+orderSetting.getEndTime()+"之间允许下单！");
        	return model;
        }
        model.put("exists", true);
		return model;
	}
	
	/**
	 * 导出选中的订单
	 * @return
	 */
	@RequestMapping(value = "/getOutExportSelectedOrder", method = RequestMethod.GET)
    public ModelAndView exportSelectedOrder(Long[] ids , ModelMap model ) {
		return new ModelAndView(orderService.exportSelectedOrder(ids), model);
    }
	
	/**
	 * 导出食药监报告
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
	 * @return
	 */
	@RequestMapping(value = "/getOutExportFoodMedicine", method = RequestMethod.GET)
    public void exportFoodMedicine(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response) {
        Supplier supplier = super.getCurrentSupplier();
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
        orderService.exportFoodMedicine(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, supplier, startDate, endDate, searchName, timeSearch, request, response);
    }
	
	/**
	 * 导出选中食药监报告
	 * @param ids
	 * @param pageable
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOutSelectedReports", method = RequestMethod.GET)
	public void selectedReports(Long[] ids ,Pageable pageable, ModelMap model , HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		orderService.selectedReports(ids, supplier, request, response);
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
	@RequestMapping(value = "/getOutBatchSplitOut", method = RequestMethod.GET)
    public void batchSplitOut (Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch , HttpServletRequest request , HttpServletResponse response) {

        Supplier supplier = super.getCurrentSupplier();
        orderService.batchSplitOut(type, status, memberUsername, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable, supplier, startDate, endDate, searchName, timeSearch, request, response);
    }
	
	/**
	 * 选中拆分导出
	 * @param ids
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOutSelectedSplitOut", method = RequestMethod.GET)
	public void selectedSplitOut(Long[] ids , HttpServletRequest request , HttpServletResponse response) {
		Supplier supplier = super.getCurrentSupplier();
		orderService.selectedSplitOut(ids, supplier, request, response);
	}
	@RequestMapping(value = "/editViewByFormal", method = RequestMethod.GET)
	public String editViewByFormal(Long id, ModelMap model) {
		Order order = orderService.find(id) ;
		List<Product> list=new ArrayList<>();
		Need need=order.getNeed();
		
		SupplierSupplier supplierSupplier=order.getSupplierSupplier();

		if (supplierSupplier == null){
			model.addAttribute("hasSupplierSupplier", false);
		}else {
			if (supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply) || supplierSupplier.getStatus().equals(SupplierSupplier.Status.suspendSupply)) {
				if (order.getType().equals(Order.Type.formal) && order.getBuyType().equals(Order.BuyType.waterSubstitute)) {
					list=productService.getGoodListByFormal(supplierSupplier,null);
				}else {
					list=productService.getGoodListByFormal(supplierSupplier,need);
				}
				model.addAttribute("hasSupplierSupplier", true);
			}else {
				model.addAttribute("hasSupplierSupplier", false);
			}
		}
		List<OrderItem> orderItems=order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			for (Product product : list) {
				if (orderItem.getProduct().equals(product)) {
					orderItem.setMinOrderQuantity(product.getMinOrderQuantity());
					break;
				}
			}
		}
		model.addAttribute("order", order);
		model.addAttribute("products", list);
		return "/admin/order/own_edit";
	}
	
	@RequestMapping(value = "/getGoodListByFormal", method = RequestMethod.GET)
	public String getGoodListByFormal(long orderId,Long productCategoryId,String goodName,Pageable pageable,ModelMap model){
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Order order=orderService.find(orderId);
		Need need=order.getNeed();
		if (order.getType().equals(Order.Type.formal) && order.getBuyType().equals(Order.BuyType.waterSubstitute)) {
			need=null;
		}
		SupplierSupplier supplierSupplier=order.getSupplierSupplier();
		
		if (supplierSupplier == null){
			return ERROR_VIEW;
		}else {
			if (!supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply) && !supplierSupplier.getStatus().equals(SupplierSupplier.Status.suspendSupply)) {
				return ERROR_VIEW;
			}
		}
		
		model.addAttribute("orderId", orderId);
		model.addAttribute("goodName", goodName);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategoryTree", productCategoryService.findByGradeAndNeedByFormal(supplierSupplier,need));
		Page<Product> page=productService.getGoodListPageByFormal(supplierSupplier,need, productCategory, goodName, pageable,null);

		model.addAttribute("page",page);
		return "/admin/order/own_goodList";
	}
	
//	@RequestMapping(value = "/editView", method = RequestMethod.GET)
//	public String editView(Long id, ModelMap model) {
//		Order order = orderService.find(id) ;
//		//Supplier bySupplier=order.getMember().getNeed().getSupplier();
//		Need need = needService.findNeedByMemberSupplier(order.getToSupplier()==null ? order.getSupplier(): order.getToSupplier(), order.getMember());
//		Supplier bySupplier = order.getToSupplier()!= null ? order.getToSupplier() : order.getSupplier();
//		
//		Supplier supplier=order.getSupplier();
//		List<SupplierSupplier.Status> status=new ArrayList<>();
//		status.add(SupplierSupplier.Status.inTheSupply);
//		status.add(SupplierSupplier.Status.suspendSupply);
//		SupplierSupplier supplierSupplier=supplierSupplierService.getSupplierSupplier(bySupplier,supplier,order.getCreateDate(),status);
//		Page<Product> page;
//		if (supplierSupplier == null){
//			page=new Page<>();
//			model.addAttribute("hasSupplierSupplier", false);
//		}else {
//			page=productService.getGoodList(supplierSupplier,need, null, null, null);
//			model.addAttribute("hasSupplierSupplier", true);
//		}
//		
//		model.addAttribute("order", order);
//		model.addAttribute("products", page.getContent());
//		return "/admin/order/own_edit";
//	}
//	
//	@RequestMapping(value = "/getGoodList", method = RequestMethod.GET)
//	public String getGoodList(long orderId,Long productCategoryId,String goodName,Pageable pageable,ModelMap model){
//		ProductCategory productCategory = productCategoryService.find(productCategoryId);
//		Order order=orderService.find(orderId);
//		//Supplier bySupplier=order.getMember().getNeed().getSupplier();
//		Need need = needService.findNeedByMemberSupplier(order.getToSupplier()==null ? order.getSupplier(): order.getToSupplier(), order.getMember());
//		Supplier bySupplier = order.getToSupplier()!= null ? order.getToSupplier() : order.getSupplier();
//		Supplier supplier=order.getSupplier();
//		List<SupplierSupplier.Status> status=new ArrayList<>();
//		status.add(SupplierSupplier.Status.inTheSupply);
//		status.add(SupplierSupplier.Status.suspendSupply);
//		SupplierSupplier supplierSupplier=supplierSupplierService.getSupplierSupplier(bySupplier,supplier,order.getCreateDate(),status);
//		if (supplierSupplier == null){
//			return ERROR_VIEW;
//		}
//		model.addAttribute("orderId", orderId);
//		model.addAttribute("goodName", goodName);
//		model.addAttribute("productCategoryId", productCategoryId);
//		model.addAttribute("productCategoryTree", productCategoryService.findByGradeAndNeed(supplierSupplier,need));
//		Page<Product> page=productService.getGoodList(supplierSupplier,need, productCategory, goodName, pageable);
//		model.addAttribute("page",page);
//		return "/admin/order/own_goodList";
//	}
	
	/**
	 * 手机版后台采购单订单附件列表
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/orderAttachmentList" , method = RequestMethod.GET)
	public String orderAttachmentList(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("orderFiles", order.getOrderFiles());
		return "/admin/order/own_annex_mobile";
	}
	
	/**
	 * 手机版后台采购单物流信息
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/logisticsInfo" , method = RequestMethod.GET)
	public String logisticsInfo(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("shippings", order.getShippings());
		return "/admin/order/own_logistics_mobile";
	}
	
	/**
	 * 手机版后台采购单商品信息
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
		return "/admin/order/own_product_mobile";
	}
	
	/**
	 * 手机版后台采购单备注
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/remarksList" , method = RequestMethod.GET)
	public String remarksList(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("type", order.getType());
		model.addAttribute("orderRemarks", order.getOrderRemarks());
		return "/admin/order/own_remarks_mobile";
	}
	
	/**
	 * 手机版后台采购单订单记录
	 * @param model
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value = "/ownLog" , method = RequestMethod.GET)
	public String ownLog(ModelMap model , Long id) {
		Order order = orderService.find(id);
		model.addAttribute("orderLogs", order.getOrderLogs());
		return "/admin/order/own_log_mobile";
	}
	
	/**
	 * 手机版后台采购单列表
	 * @param type
	 * @param status
	 * @param statuses
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
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
    public JsonEntity asyncList(Order.Type type, Order.Status status,Order.Status[] statuses, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model , Date startDate , Date endDate , String searchName , String timeSearch) {
        if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
        
        Supplier supplier = super.getCurrentSupplier();

        Member member = memberService.findByUsername(memberUsername);
        Page<Order> page = null;
        if (StringUtils.isNotEmpty(memberUsername) && member == null) {
        	page = Page.emptyPage(pageable);
        } else {
        	page = orderService.findPageByOwn(null,status,statuses, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable , supplier , startDate , endDate , searchName , timeSearch);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> orderList=new ArrayList<>();
        List<Order> orders = page.getContent();
        if(null != orders) {
        	for (Order order : page.getContent()) {
    			Map<String, Object> map=new HashMap<>();
    			map.put("orderId", order.getId());
    			map.put("sn", order.getSn());
    			map.put("status", order.getStatus().ordinal());
    			map.put("statusString", order.getStatus());
    			map.put("needName", order.getNeed().getName());
    			map.put("quantity", order.getQuantity());
    			map.put("type", order.getType());
    			orderList.add(map);
    		}
        }
        resultMap.put("orderList", orderList);
        resultMap.put("totalPages", page.getTotalPages());
      	return JsonEntity.successMessage(resultMap);
    }
	
	/**
	 * 手机版后台采购单搜索页面
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search() {
		return "/admin/order/own_listSearch_mobile";
	}
	
	/**
	 * 
	 * @Title: addOwnRemarksMobile
	 * @author: yuezhiwei
	 * @date: 2018年1月31日下午2:25:22
	 * @Description: 手机版后台采购单添加备注
	 * @return: String
	 */
	@RequestMapping(value = "/addOwnRemarksMobile", method = RequestMethod.GET)
	public String addOwnRemarksMobile(ModelMap model) {
		return "/admin/order/own_addRemarks_mobile";
	}
	


}
