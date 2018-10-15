package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.*;
import com.microBusiness.manage.util.Code;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mingbai on 2017/4/13.
 * 功能描述：这个控制层用来处理，无需权限的功能
 * 修改记录：
 */
@Controller
@RequestMapping(value = "/admin/utils")
public class UtilsController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(UtilsController.class);

    @Resource
    private ProductService productService ;

    @Resource
    private SupplierService supplierService ;

    @Resource
    private NeedService needService ;

    @Resource
    private AreaService areaService ;

    @Resource
    private ProductCategoryService productCategoryService ;
    
    @Resource
    private AdminService adminService;

    @Resource
    private OrderSettingService orderSettingService ;

    private static final String NEED_NAME = "流水收货点" ;


    @Resource
    private CustomerRelationService relationService;


    /**
     * 个体客户代下单商品列表 --自营
     * @param pageable
     * @param modelMap
     * @param supplierId
     * @param needId
     * @param productCategoryId
     * @return
     */
    @RequestMapping(value = "/proList", method = RequestMethod.GET)
    public String getProducts(Pageable pageable , ModelMap modelMap , Long supplierId , Long needId , Long productCategoryId,String searchName){
        modelMap.put("supplierId" , supplierId);
        modelMap.put("needId" , needId);
        modelMap.put("searchName" , searchName);
        modelMap.put("productCategoryId" , productCategoryId);
        modelMap.put("productCategoryTree" , productCategoryService.findByTemporary(needId , supplierId)) ;
        modelMap.put("page" , productService.getTemporaryProductPage(supplierId , needId , pageable , productCategoryId,searchName)) ;

        return "/admin/utils/product_list_temporary" ;
    }
    
    /**
     * 个体客户代下单商品列表  ---非自营
     * @param pageable
     * @param modelMap
     * @param supplierId
     * @param needId
     * @param productCategoryId
     * @return
     */
    @RequestMapping(value = "/proListFormal", method = RequestMethod.GET)
    public String getProductsFormal(Pageable pageable , ModelMap modelMap , Long supplierId , Long needId , Long productCategoryId,String searchName){
        modelMap.put("supplierId" , supplierId);
        modelMap.put("needId" , needId);
        modelMap.put("searchName" , searchName);
        modelMap.put("productCategoryId" , productCategoryId);
        modelMap.put("productCategoryTree" , productCategoryService.findByFormal(needId , supplierId)) ;
        modelMap.put("page" , productService.getFormalProductPage(supplierId , needId , pageable , productCategoryId,searchName)) ;

        return "/admin/utils/product_list_formal" ;
    }


    /**
     * 获取关系供应中的供应商
     * @param supplierId
     * @return
     */
    @RequestMapping(value = "/relSuppliers", method = RequestMethod.GET)
    public Map<Long , Map<String , Object>> getRelSuppliers(Long supplierId , ModelMap modelMap){

        List<Supplier> suppliers = supplierService.getSupplierFromBy(supplierId) ;

        Map<Long , Map<String , Object>> ret = new HashMap();

        for(final Supplier supplier : suppliers){
            ret.put(supplier.getId() , new HashMap<String , Object>(){{
                this.put("name" , supplier.getName());
            }});
        }
        return ret ;

    }

    /**
     * 流水客户代下单商品列表
     * @param pageable
     * @param modelMap
     * @param supplierId
     * @return
     */
    @RequestMapping(value = "/getAssProducts", method = RequestMethod.GET)
    public String getAssProducts(Pageable pageable , ModelMap modelMap , Long productCategoryId){
        Supplier currSupplier = super.getCurrentSupplier() ;
        modelMap.put("productCategoryId" , productCategoryId);

        if(null == currSupplier){
            modelMap.put("page" , Page.emptyPage(pageable)) ;
        }else{
            modelMap.put("productCategoryTree" , productCategoryService.findByAllSupplier(currSupplier.getId())) ;
            modelMap.put("page" , productService.getSupplierAssignProducts(currSupplier.getId(), pageable , productCategoryId)) ;
        }
        return "/admin/utils/ass_product_list_temporary" ;
    }
    
    
    
    /**
     * 供应商之间分配的商品
     * @param pageable
     * @param modelMap
     * @param supplierId
     * @return
     */
    @RequestMapping(value = "/getAssProductsFormal", method = RequestMethod.GET)
    public String getAssProductsFormal(Pageable pageable , ModelMap modelMap , Long supplierId , Long productCategoryId){
        Supplier currSupplier = super.getCurrentSupplier() ;
        modelMap.put("supplierId" , supplierId);
        modelMap.put("productCategoryId" , productCategoryId);

        if(null == currSupplier){
            modelMap.put("page" , Page.emptyPage(pageable)) ;
        }else{
            modelMap.put("productCategoryTree" , productCategoryService.findByAssSupplier(supplierId , currSupplier.getId())) ;
            modelMap.put("page" , productService.getSupplierAssignProductsFormal(supplierId , currSupplier.getId() , pageable , productCategoryId)) ;
        }
        return "/admin/utils/ass_product_list_formal" ;
    }

    @RequestMapping(value = "/saveNeed" , method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity saveNeed(Need need , HttpServletRequest request , Long areaId){
        Map<String , Object> ret = new HashMap();
        try{
            need.setName(this.NEED_NAME);
            need.setType(Need.Type.turnover);
            need.setArea(areaService.find(areaId));
            need.setSupplier(super.getCurrentSupplier());
            needService.save(need);
            ret.put("id" , need.getId()) ;

            String address = StringUtils.isEmpty(need.getAddress()) ? "" :need.getAddress() ;

            ret.put("address" , address);
            ret.put("userName" , need.getUserName()) ;
            ret.put("tel" , need.getTel()) ;
            ret.put("areaId" , areaId) ;

            return JsonEntity.successMessage(ret) ;
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("save need error:" , e);
            return new JsonEntity(Code.code019996) ;
        }
    }

    @RequestMapping(value = "/checkNeedTel", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkNeedTel(String tel){

        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        Need need = needService.findByTel(tel) ;
        return null == need? true : false ;

    }

    /**
     * 弹窗获取流水收货点
     * @param pageable
     * @param model
     * @param need
     * @return
     */
    @RequestMapping(value = "/turnOverNeedList", method = RequestMethod.GET)
    public String turnOverNeedList(Pageable pageable, ModelMap model , Need need) {
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(super.getCurrentSupplier());
        need.setType(Need.Type.turnover);
        Supplier supplier = super.getCurrentSupplier();
        model.addAttribute("page", needService.findPage(pageable , need , null, null, null));
        model.addAttribute("tel", need.getTel());

        OrderSetting orderSetting = supplier.getOrderSetting() ;
        if(null == orderSetting){
            orderSetting = orderSettingService.setDefaultValue() ;
        }
        model.addAttribute("numberTimes", orderSetting.getNumberTimes());
        return "/admin/utils/turnover_need_list";
    }

    /**
     * 消息通知里 的选择客户企业
     * @param searchName
     * @param startDate
     * @param endDate
     * @param clientType
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "/customerRelList" , method = RequestMethod.GET)
    public String customerRelList(String searchName , Date startDate , Date endDate , CustomerRelation.ClientType clientType , Pageable pageable , ModelMap model) {
        model.addAttribute("searchName", searchName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("clientType", clientType);

        Supplier supplier = super.getCurrentSupplier();
        model.addAttribute("page", relationService.findPage(searchName, startDate, endDate, clientType, pageable, supplier));
        return "/admin/utils/customer_rel_list";
    }

    /**
     * 消息通知里的选择需方
     * @param pageable
     * @param model
     * @param need
     * @param searchName
     * @return
     */
    @RequestMapping(value = "/needList" , method = RequestMethod.GET)
    public String needList(Pageable pageable, ModelMap model , Need need , String searchName , Need.Type searchType) {
        Admin admin = adminService.getCurrent();
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(admin.getSupplier());
        need.setType(searchType);
        model.addAttribute("needTypes" , Need.Type.values());
        model.addAttribute("searchName", searchName);
        model.addAttribute("searchType", searchType);
        model.addAttribute("page", needService.findPage(pageable , need , searchName, null, null));
        return "/admin/utils/need_list";
    }

    /**
     * 订货单里的消息通知需方
     * @param pageable
     * @param model
     * @param need
     * @param searchName
     * @param searchType
     * @return
     */
    @RequestMapping(value = "/orderNeedList" , method = RequestMethod.GET)
    public String orderNeedList(Pageable pageable, ModelMap model , Need need , String searchName , Need.Type searchType) {
        Admin admin = adminService.getCurrent();
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(admin.getSupplier());
        need.setType(searchType);
        model.addAttribute("needTypes" , Need.Type.values());
        model.addAttribute("searchName", searchName);
        model.addAttribute("searchType", searchType);
        model.addAttribute("page", needService.findPage(pageable , need , searchName, null, null));
        return "/admin/utils/order_need_list";
    }

}
