package com.microBusiness.manage.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.OrderSettingService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("ProxyOrderController")
@RequestMapping("/admin/proxyOrder")
public class ProxyOrderController extends BaseController  {
	
	@Resource(name = "adminServiceImpl")
    private AdminService adminService;
	@Resource(name = "orderServiceImpl")
    private OrderService orderService;
	@Resource
    private SupplierService supplierService ;
	@Resource
    private NeedService needService ;
	@Resource
    private OrderSettingService orderSettingService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "/admin/proxy_order/index";
	}
	
	@RequestMapping(value = "/individualAdd", method = RequestMethod.GET)
    public String add(ModelMap model , RedirectAttributes redirectAttributes,SupplyType type) {
        Supplier currSupplier = super.getCurrentSupplier() ;

        if(null == currSupplier){
            addFlashMessage(redirectAttributes, Message.error("您不是企业用户！"));
            return "redirect:index.jhtml";
        }
        if (type.equals(SupplyType.temporary)) {
        	model.put("supplierId" , currSupplier.getId());
        	model.put("needs", needService.findList(currSupplier, ShopType.direct));
        	return "/admin/proxy_order/individual_add_temporary" ;
		}else {
			model.put("needs" , currSupplier.getNeedsByShopType(ShopType.direct));
        	return "/admin/proxy_order/individual_add_formal" ;
		}
        
    }
	
	@RequestMapping(value = "/multipleAdd", method = RequestMethod.GET)
	public String multipleAdd(ModelMap model , RedirectAttributes redirectAttributes,SupplyType type) {
		Supplier currSupplier = super.getCurrentSupplier() ;

        if(null == currSupplier){
            addFlashMessage(redirectAttributes, Message.error("您不是企业用户！"));
            return "redirect:index.jhtml";
        }
        if (type.equals(SupplyType.temporary)) {
        	model.put("supplier" , currSupplier);
        	return "/admin/proxy_order/multiple_add_temporary";
        }else {
        	model.put("suppliers" , supplierService.getSupplierFromBy(currSupplier.getId()));
        	return "/admin/proxy_order/multiple_add_formal";
		}
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
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Long supplier , Long need ,  String memo ,  ModelMap model , Date reDate , OrderProductForm orderProductForm , RedirectAttributes redirectAttributes,Long areaIdT,String addressT,String userName,String tel) {
    	Admin admin = adminService.getCurrent();
        if(null == supplier || null == need || null == reDate || null == orderProductForm){
            addFlashMessage(redirectAttributes, Message.error("参数错误"));
            return "redirect:index.jhtml";
        }
        orderService.createByOwn(supplier , need  , memo , reDate , orderProductForm , adminService.getCurrentUsername() , false , admin,SupplierType.ONE,areaIdT,addressT,userName,tel) ;
        addFlashMessage(redirectAttributes, Message.success("下单成功"));
        return "redirect:index.jhtml";
    }
	
	@RequestMapping(value = "/createMore", method = RequestMethod.POST)
    public String createMore( OrderProductForm orderProductForm , RedirectAttributes redirectAttributes , OrderNeedsForm orderNeedsForm,SupplyNeed.AssignedModel assignedModel) {
		Supplier supplier=getCurrentSupplier();
        if(null==assignedModel ||null == supplier || null == orderProductForm || CollectionUtils.isEmpty(orderProductForm.getOwnOrderItems()) || null == orderNeedsForm || CollectionUtils.isEmpty(orderNeedsForm.getOrderNeedsItems()) ){
            addFlashMessage(redirectAttributes, Message.error("参数错误"));
            return "redirect:list.jhtml";
        }
        Admin admin = adminService.getCurrent() ;
        orderService.createOwnMore(assignedModel,supplier , orderProductForm  , admin.getUsername() , orderNeedsForm , admin);
        addFlashMessage(redirectAttributes, Message.success("下单成功"));
        return "redirect:index.jhtml";
    }
	
	
	@RequestMapping(value = "/createFormal", method = RequestMethod.POST)
    public String createFormal(Long supplier , Long need ,  String memo ,  ModelMap model , Date reDate , OrderProductForm orderProductForm , RedirectAttributes redirectAttributes,Long areaIdT,String addressT,String userName,String tel) {
    	Admin admin = adminService.getCurrent();
        if(null == supplier || null == need || null == reDate || null == orderProductForm){
            addFlashMessage(redirectAttributes, Message.error("参数错误"));
            return "redirect:index.jhtml";
        }
        orderService.createByOwnFormal(supplier , need  , memo , reDate , orderProductForm , adminService.getCurrentUsername() , false , admin,areaIdT,addressT,userName,tel,SupplierType.TWO) ;
        addFlashMessage(redirectAttributes, Message.success("下单成功"));
        return "redirect:index.jhtml";
    }
	
	@RequestMapping(value = "/createMoreFormal", method = RequestMethod.POST)
    public String createMoreFormal(Long supplier, OrderProductForm orderProductForm , RedirectAttributes redirectAttributes , OrderNeedsForm orderNeedsForm) {

        if(null == supplier || null == orderProductForm || CollectionUtils.isEmpty(orderProductForm.getOwnOrderItems()) || null == orderNeedsForm || CollectionUtils.isEmpty(orderNeedsForm.getOrderNeedsItems()) ){
            addFlashMessage(redirectAttributes, Message.error("参数错误"));
            return "redirect:list.jhtml";
        }
        Admin admin = adminService.getCurrent() ;
        orderService.createOwnMoreFormal(supplier , orderProductForm  , admin.getUsername() , orderNeedsForm , admin);
        addFlashMessage(redirectAttributes, Message.success("下单成功"));
        return "redirect:index.jhtml";
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
}
