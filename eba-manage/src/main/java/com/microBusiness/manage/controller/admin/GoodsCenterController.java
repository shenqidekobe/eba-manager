package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.FileType;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.SpecificationCenter;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.CategoryCenterService;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.service.GoodsCenterImportImageInfoService;
import com.microBusiness.manage.service.GoodsCenterImportInfoService;
import com.microBusiness.manage.service.GoodsCenterImportLogService;
import com.microBusiness.manage.service.GoodsCenterService;
import com.microBusiness.manage.service.SnService;
import com.microBusiness.manage.service.SpecificationItemService;
import com.microBusiness.manage.util.CommonUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午2:54
 * Describe:
 * Update:
 */
@Controller
@RequestMapping("/admin/goodsCenter")
public class GoodsCenterController extends BaseController {

    @Resource
    private CategoryCenterService categoryCenterService;

    @Resource
    private GoodsCenterService goodsCenterService ;

    @Resource
    private AdminService adminService ;
    
    @Resource
    private SpecificationItemService specificationItemService ;
    
    @Resource
    private GoodsCenterImportLogService goodsCenterImportLogService ;

    @Resource
    private GoodsCenterImportInfoService goodsCenterImportInfoService;
    
    @Resource
    private FileService fileService;
    
    @Resource
    private GoodsCenterImportImageInfoService goodsCenterImportImageInfoService;
    
    @Resource
    private SnService snService;
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("types", GoodsCenter.Type.values());
        model.addAttribute("productCategoryTree", categoryCenterService
                .findTree());

        model.addAttribute("storageConditions",
                GoodsCenter.StorageConditions.values());
        model.addAttribute("weightUnits", GoodsCenter.WeightUnit.values());
        model.addAttribute("volumeUnits", GoodsCenter.VolumeUnit.values());
        model.addAttribute("natures", GoodsCenter.Nature.values());
        model.addAttribute("units", GoodsCenter.Unit.values());
        model.addAttribute("labels" , GoodsCenter.Label.values()) ;

        return "/admin/goods_center/add";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(GoodsCenter.Type type, Long productCategoryId , Boolean isMarketable, Boolean isList,
                       Boolean isTop, Pageable pageable, ModelMap model) {
        CategoryCenter productCategory = categoryCenterService
                .find(productCategoryId);
        model.addAttribute("types", GoodsCenter.Type.values());
        model.addAttribute("productCategoryTree", categoryCenterService
                .findTree());
        model.addAttribute("type", type);
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
//        model.addAttribute("orSearchValue", pageable.getOrSearchValue());
        model.addAttribute("page", goodsCenterService.findPage(type, productCategory, isMarketable, isList, isTop , null , pageable));
        return "/admin/goods_center/list";
    }


    @RequestMapping(value = "/check_sn", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkSn(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return false;
        }
        return !goodsCenterService.snExists(sn);
    }

    @RequestMapping(value = "/specifications", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String, Object>> specifications(Long productCategoryId) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        CategoryCenter productCategory = categoryCenterService.find(productCategoryId);
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecificationCenters())) {
            return data;
        }
        for (SpecificationCenter specification : productCategory.getSpecificationCenters()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", specification.getName());
            item.put("options", specification.getOptions());
            data.add(item);
        }
        return data;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(GoodsCenter goods, ProductForm productForm, ProductListForm productListForm, Long productCategoryId, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        specificationItemService.filter(goods.getSpecificationItems());

        goods.setCategoryCenter(categoryCenterService.find(productCategoryId));


        if (!isValid(goods, BaseEntity.Save.class)) {
            return ERROR_VIEW;
        }
        if (StringUtils.isNotEmpty(goods.getSn())
                && goodsCenterService.snExists(goods.getSn())) {
            return ERROR_VIEW;
        }

        Admin admin = adminService.getCurrent();

        if (goods.hasSpecification()) {
            List<ProductCenter> products = productListForm.getProductList();
            if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(goods.getType()),
                    BaseEntity.Save.class)) {
                return ERROR_VIEW;
            }
            goodsCenterService.save(goods, products, admin);
        } else {
            ProductCenter product = productForm.getProduct();
            if (product == null || !isValid(product, getValidationGroup(goods.getType()),
                    BaseEntity.Save.class)) {
                return ERROR_VIEW;
            }
            goodsCenterService.save(goods, product, admin);
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("types", GoodsCenter.Type.values());
        
        model.addAttribute("productCategoryTree", categoryCenterService.findTree());


        GoodsCenter goods = goodsCenterService.find(id);
        model.addAttribute("goods", goods);

        model.addAttribute("storageConditions",
                GoodsCenter.StorageConditions.values());
        model.addAttribute("weightUnits", GoodsCenter.WeightUnit.values());
        model.addAttribute("volumeUnits", GoodsCenter.VolumeUnit.values());
        model.addAttribute("natures", GoodsCenter.Nature.values());
        model.addAttribute("units", GoodsCenter.Unit.values());
        model.addAttribute("labels" , goods.getLabels()) ;

        return "/admin/goods_center/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(GoodsCenter goods, ProductForm productForm, ProductListForm productListForm, Long id, Long productCategoryId, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        specificationItemService.filter(goods.getSpecificationItems());

        goods.setCategoryCenter(categoryCenterService.find(productCategoryId));

        if (!isValid(goods, BaseEntity.Update.class)) {
            return ERROR_VIEW;
        }

        Admin admin = adminService.getCurrent();
        if (goods.hasSpecification()) {
            List<ProductCenter> products = productListForm.getProductList();
            if (CollectionUtils.isEmpty(products)
                    || !isValid(products, getValidationGroup(goods.getType()),
                    BaseEntity.Update.class)) {
                return ERROR_VIEW;
            }
            goodsCenterService.update(goods, products, admin);
        } else {
            ProductCenter product = productForm.getProduct();
            if (product == null
                    || !isValid(product, getValidationGroup(goods.getType()),
                    BaseEntity.Update.class)) {
                return ERROR_VIEW;
            }
            goodsCenterService.update(goods, product, admin);
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        goodsCenterService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    private Class<?> getValidationGroup(GoodsCenter.Type type) {
        Assert.notNull(type);

        switch (type) {
            case general:
                return Product.General.class;
            case exchange:
                return Product.Exchange.class;
            case gift:
                return Product.Gift.class;
        }
        return null;
    }

    public static class ProductForm {

        private ProductCenter product;

        public ProductCenter getProduct() {
            return product;
        }

        public void setProduct(ProductCenter product) {
            this.product = product;
        }
    }

    public static class ProductListForm {

        private List<ProductCenter> productList;

        public List<ProductCenter> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductCenter> productList) {
            this.productList = productList;
        }
    }

    public enum GoodListStatus {
        add, edit
    }


    @RequestMapping(value = "/importMore", method = RequestMethod.GET)
    public String importMore(ModelMap model) {
        return "/admin/goods_center/import";
    }

   @RequestMapping(value = "/viewMore", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity viewMore(ModelMap model, MultipartFile multipartFile) {

        GoodsCenterImportLog goodsImportLog = goodsCenterService.dealMoreGoods(multipartFile, adminService.getCurrent());
        if (null == goodsImportLog || null == goodsImportLog.getId()) {
            return new JsonEntity("15001", "导入失败,尝试检查是否使用下载模板");
        }
        return JsonEntity.successMessage(goodsImportLog.getId());
    }

    @RequestMapping(value = "/importList", method = RequestMethod.GET)
    public String importList(Pageable pageable, ModelMap model, Long logId) {

        if (null == logId) {
            return ERROR_VIEW;
        }

        GoodsCenterImportLog goodsImportLog = goodsCenterImportLogService.find(logId);

        Page<GoodsCenterImportInfo> page = goodsCenterImportInfoService.findPage(pageable, goodsImportLog);

        model.put("page", page);
        model.put("goodsImportLog", goodsImportLog);

        return "/admin/goods_center/importList";
    }

    @RequestMapping(value = "/saveMore", method = RequestMethod.POST)
    public String saveMore(ModelMap model, Long logId,
                           RedirectAttributes redirectAttributes) {

        if (null == logId) {
            return ERROR_VIEW;
        }

        GoodsCenterImportLog goodsImportLog = goodsCenterImportLogService.find(logId);

        if (null == goodsImportLog) {
            return ERROR_VIEW;
        }

        boolean isOk = goodsCenterService.saveMore(goodsImportLog, adminService.getCurrent());
        if (isOk) {
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } else {
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
        }
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/getGoods", method = RequestMethod.GET)
    @ResponseBody
    public boolean getGoods(String name, String oldName) {
        if (name.equals(oldName)) {
            return true;
        }
        return !goodsCenterService.findByName(name);
    }

    /*
    @RequestMapping(value = "/importImg", method = RequestMethod.GET)
    public String importImg(ModelMap model) {
        String batch = snService.generate(Sn.Type.goodsImage);
        model.addAttribute("batch", batch);
        return "/admin/goods/importImg";
    }*/

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> upload(FileType fileType, MultipartFile file, String batch) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (fileType == null || file == null || file.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			data.put("state", message("admin.message.error"));
			return data;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
			data.put("state", message("admin.upload.invalid"));
			return data;
		}

		String sn = FilenameUtils.getBaseName(file.getOriginalFilename());
		if (CommonUtils.fileNameTest(sn) || CommonUtils.fileNameTestSpace(sn)) {
			String snStr = sn.substring(0, sn.length() - 3);
			GoodsCenter goods = goodsCenterService.findBySn(snStr);
			if (goods == null) {
				data.put("message", "未找到对应商品");
				data.put("state", "admin.upload.error");
				return data;
			}
		} else {
			// 主图
			if (!CommonUtils.snTest(sn) && !CommonUtils.snTestSpace(sn)) {
				data.put("message", "商品编号有误");
				data.put("state", "admin.upload.error");
				return data;
			}

			GoodsCenter goods = goodsCenterService.findBySn(sn);
			if (goods == null) {
				data.put("message", "未找到对应商品");
				data.put("state", "admin.upload.error");
				return data;
			}
		}

		String url = fileService.upload(fileType, file, false, true);
		if (StringUtils.isEmpty(url)) {
			data.put("message", Message.warn("admin.upload.error"));
			data.put("state", message("admin.upload.error"));
			return data;
		}
		Map<String, Object> map = new HashMap();
		synchronized(this){
			map = goodsCenterService.uploadImage(sn, url, batch);
		}

		return map;
	}

    @RequestMapping(value = "/importImageList", method = RequestMethod.POST)
    public @ResponseBody
    List<Map<String, Object>> importImageList(Pageable pageable, ModelMap model, String batch) {

        if (StringUtils.isEmpty(batch)) {
            return null;
        }

        Page<GoodsCenterImportImageInfo> page = goodsCenterImportImageInfoService.findPage(pageable, batch);

        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

        for (GoodsCenterImportImageInfo goodsImportImageInfo : page.getContent()) {
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("sn", goodsImportImageInfo.getSn());
            retMap.put("image", goodsImportImageInfo.getImage());
            retMap.put("images", goodsImportImageInfo.getImages());
            retMap.put("name", goodsImportImageInfo.getName());
            reList.add(retMap);
        }

        return reList;
    }

    @RequestMapping(value = "/saveMoreImage", method = RequestMethod.GET)
    public String saveMoreImage(ModelMap model, String batch,RedirectAttributes redirectAttributes) {

        if (StringUtils.isEmpty(batch)) {
            return ERROR_VIEW;
        }

        boolean isOk = goodsCenterService.saveMore(batch, adminService.getCurrent());
        if (isOk) {
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } else {
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
        }
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/importImg", method = RequestMethod.GET)
	public String importImg(ModelMap model) {
		String batch = snService.generate(Sn.Type.goodsImage);
		model.addAttribute("batch", batch);
		return "/admin/goods_center/importImg";
	}

}
