package com.microBusiness.manage.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.GoodsController.GoodListStatus;
import com.microBusiness.manage.entity.Brand;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Promotion;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.Tag;
import com.microBusiness.manage.service.BrandService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.PromotionService;
import com.microBusiness.manage.service.SupplierProductService;
import com.microBusiness.manage.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 流水供应
 * 
 * @author Administrator
 *
 */
@Controller("turnoverSupplyController")
@RequestMapping("/admin/turnoverSupply")
public class TurnoverSupplyController extends BaseController {

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
	
	@Resource(name = "supplierProductServiceImpl")
	private SupplierProductService supplierProductService;

	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
	public String selectList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, ModelMap model, GoodListStatus goodListStatus , String searchName) {
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
		
		/*Page<Goods> page = goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), null, null);
		
		for (Goods goods : page.getContent()) {
			if (goods.getSupplierSupplier() != null) {
				for (Product product : goods.getProducts()) {
					SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(goods.getSupplierSupplier(), product.getSource());
					product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
				}
			}
		}*/
		
		Page<Product> page = productService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable,
				isList, isTop, isOutOfStock, isStockAlert, null, null, pageable, super.getCurrentSupplier(), null, null,searchName);
		
		for (Product product : page.getContent()) {
			SupplierSupplier supplierSupplier = product.getGoods().getSupplierSupplier();
			if( supplierSupplier != null) {
				SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierSupplier, product.getSource());
				product.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
			}
		}

		model.addAttribute("page", page);
		
		return "/admin/turnoverSupply/list";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(ProductForm productForm) {
        try{
        	productService.update(productForm.productList);
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }
	
	public static class ProductForm {

        private List<Product> productList;

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}
        
    }

}
