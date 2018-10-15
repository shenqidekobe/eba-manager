package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.*;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.controller.admin.GoodsController.GoodListStatus;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.CustomerRelationService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.TagService;
import com.microBusiness.manage.util.JsonUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by mingbai on 2017/2/5.
 * 功能描述：临时供应
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/temporary")
public class TemporarySupplyController extends BaseController {
    @Resource
    private SupplyNeedService supplyNeedService ;
    @Resource
    private SupplierService supplierService ;
    @Resource
    private NeedProductService needProductService ;
    @Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource
	private CustomerRelationService customerRelationService;
    /**
     * 临时供应列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable , SupplyNeed supplyNeed , ModelMap model , String searchName) {
        final Supplier currentSupp = super.getCurrentSupplier();
        supplyNeed.setSupplier(currentSupp);
        Page<SupplyNeed> result = supplyNeedService.findPage(pageable , supplyNeed , new Date() , new Date() , searchName);
        model.addAttribute("searchName", searchName);
        model.addAttribute("status", supplyNeed.getStatus());
        model.addAttribute("page" , result);
        return "/admin/temporary/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        //获取所有企业
        //List<Supplier> suppliers = supplierService.findByStatus(Supplier.Status.authenticationFailed,Supplier.Status.certification,Supplier.Status.notCertified);
    	//获取有客户关系的临时供应商
    	Supplier supplier = super.getCurrentSupplier();
    	List<Supplier> suppliers = customerRelationService.findByTemporarySupply(supplier, Supplier.Status.verified);
        model.addAttribute("suppliers" , suppliers);
        model.addAttribute("currSupplier" , super.getCurrentSupplier());

        return "/admin/temporary/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(SupplyNeed supplyNeed , NeedProductForm needProductForm) {
        // TODO: 2017/2/7  判断当前的 供应关系是否存在
        try{
            supplyNeed.setSupplier(super.getCurrentSupplier());
            supplyNeedService.save(supplyNeed , needProductForm.getNeedProductList()) ;
            return SUCCESS_MESSAGE;

        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }

    public static class NeedProductForm {

        private List<NeedProduct> needProductList;

        public List<NeedProduct> getNeedProductList() {
            return needProductList;
        }

        public void setNeedProductList(List<NeedProduct> needProductList) {
            this.needProductList = needProductList;
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        supplyNeedService.delete(ids);
        return SUCCESS_MESSAGE;
    }


    @RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
    public @ResponseBody
    Message delete(SupplyNeed supplyNeed) {
        supplyNeedService.updateStatus(supplyNeed);
        return SUCCESS_MESSAGE;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap model , final Long id) {
        //获取当前供应关系
        final SupplyNeed supplyNeed = supplyNeedService.find(id);

        List<NeedProduct> needProducts = needProductService.findByParams(new NeedProduct(){{
            this.setSupplyNeed(supplyNeed);
        }});
        Map<String , Map<String , Map<String , String>>> results = new HashMap<>();

        for(final NeedProduct needProduct : needProducts){
            String goodsId = needProduct.getProducts().getGoods().getId().toString() ;
            System.out.println("goodsId :" + goodsId);
            if(results.containsKey(goodsId)){
                Map<String , Map<String , String>> tempRes = results.get(goodsId) ;
                tempRes.put(needProduct.getProducts().getId().toString() ,new HashMap<String , String>(){{
                        this.put("products" , needProduct.getProducts().getId().toString());
                        this.put("supplyPrice" , needProduct.getSupplyPrice().toString());
                }});
                results.put(goodsId, tempRes);
            }else{
                Map<String , Map<String , String>> tempRes = new HashMap<>();
                tempRes.put(needProduct.getProducts().getId().toString() ,new HashMap<String , String>(){{
                    this.put("products" , needProduct.getProducts().getId().toString());
                    this.put("supplyPrice" , needProduct.getSupplyPrice().toString());
                }});
                results.put(goodsId, tempRes);
            }
        }

        model.addAttribute("needProducts" , needProducts) ;
        model.addAttribute("supplyNeed" , supplyNeed) ;
        model.addAttribute("needProductsStr" , JsonUtils.toJson(results)) ;
        model.addAttribute("proSize" , results.size()) ;

        return "/admin/temporary/edit";
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(SupplyNeed supplyNeed , NeedProductForm needProductForm) {
        // TODO: 2017/2/7  判断当前的 供应关系是否存在
        try{
            supplyNeed.setSupplier(super.getCurrentSupplier());
            supplyNeedService.update(supplyNeed , needProductForm.getNeedProductList()) ;
            return SUCCESS_MESSAGE;

        }catch (Exception e){
            e.printStackTrace();
            return ERROR_MESSAGE;
        }

    }
    
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
	public String selectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model , GoodListStatus goodListStatus) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("goodListStatus", goodListStatus);
		model.addAttribute("page", goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable , super.getCurrentSupplier(), true, null));
		return "/admin/goods/goods_list";
	}
    

}
