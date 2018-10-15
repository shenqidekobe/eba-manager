package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NeedProductDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SupplyNeedDao;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Resource
    private SupplierDao supplierDao ;

    @Resource
    private NeedDao needDao ;

    @Resource
    private SupplyNeedDao supplyNeedDao ;

    @Resource
    private NeedProductDao needProductDao ;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String setting(ModelMap model) {
        List<Supplier> supplierList = supplierDao.findList(0,100 , null , null);

        List<Need> needs = needDao.findList(0 , 100 , null , null );

        List<Filter> list = new ArrayList<>();
        list.add(new Filter("supplier" , Filter.Operator.eq , 1)) ;

        List<SupplyNeed> supplyNeeds = supplyNeedDao.findList(0 , 100  , list , null);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("need.id" , Filter.Operator.eq , 1));

        List<NeedProduct> needProducts = needProductDao.findList(0 , 100 , filters , null) ;

        return null ;


    }
}
