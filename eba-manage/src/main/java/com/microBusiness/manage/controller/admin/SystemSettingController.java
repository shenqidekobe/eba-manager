package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SystemSetting;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.SystemSettingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingbai on 2017/10/11.
 * 功能描述：
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/systemSetting")
public class SystemSettingController extends BaseController {
    @Resource
    private SystemSettingService systemSettingService ;

    @Resource
    private SupplyNeedService supplyNeedService ;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model) {
        Supplier supplier = super.getCurrentSupplier() ;
        SystemSetting systemSetting = supplier.getSystemSetting() ;
        model.addAttribute("systemSetting" , systemSetting) ;
        return  "/admin/system_setting/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody Message save(HttpServletRequest request, SystemSetting systemSetting) {
    	Supplier currSupplier = super.getCurrentSupplier() ;

        if(null == systemSetting || null == systemSetting.getId()){

            return Message.error("systemSetting is null");
        }
        //分销模式关闭，判断是否存在供应中的分销模式供应
        if(!systemSetting.getIsDistributionModel()){
            List<SupplyNeed> supplierNeeds = supplyNeedService.findBySupplier(currSupplier , new ArrayList<SupplyNeed.Status>(){{
                this.add(SupplyNeed.Status.SUPPLY);
                this.add(SupplyNeed.Status.WILLSUPPLY);
            }} , SupplyNeed.AssignedModel.BRANCH) ;

            if(CollectionUtils.isNotEmpty(supplierNeeds)){
                return Message.error("存在供应中或未开始的分销供应，不可关闭分销模式!");
            }
        }
        
        systemSettingService.update(systemSetting , "supplier");
        //设置成功后退出登录
        Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
        return Message.success("分销模式设置成功,请重新登录功能才能生效");
    }
}
