package com.microBusiness.manage.controller.admin.ass;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Attribute;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AttributeService;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.ParameterValueService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SpecificationItemService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.service.SupplierSupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.TagService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssProductService;

@Controller("goodsAssController")
@RequestMapping("/admin/assgoods")
public class AssGoodsController extends BaseController {
	
	@Resource
	private AssGoodsService assGoodsService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "assProductServiceImpl")
	private AssProductService assProductService;
	
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "parameterValueServiceImpl")
	private ParameterValueService parameterValueService;
	@Resource(name = "specificationItemServiceImpl")
	private SpecificationItemService specificationItemService;
	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource
    private SupplyNeedService supplyNeedService ;
	@Resource
    private NeedProductService needProductService;
	
	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(String searchName, Boolean isMarketable, Pageable pageable, ModelMap model) {
		model.addAttribute("searchName", searchName);
		model.addAttribute("isMarketable", isMarketable);
		//model.addAttribute("page", assGoodsService.findPage(searchName, isMarketable, pageable));
		model.addAttribute("currSupplier", super.getCurrentSupplier());
		return "/admin/assGoods/list";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTreeList(super.getCurrentSupplier() , null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());

		return "/admin/assGoods/add";
	}
	
	
	public static class ProductForm {
		
		private AssProduct assProduct;
		
		public AssProduct getAssProduct() {
			return assProduct;
		}

		public void setAssProduct(AssProduct assProduct) {
			this.assProduct = assProduct;
		}

	}

	public static class ProductListForm {

		private List<AssProduct> assProducts;

		public List<AssProduct> getAssProducts() {
			return assProducts;
		}

		public void setAssProducts(List<AssProduct> assProducts) {
			this.assProducts = assProducts;
		}
	}

	public enum GoodListStatus{
		add,
		edit
	}
	
}
