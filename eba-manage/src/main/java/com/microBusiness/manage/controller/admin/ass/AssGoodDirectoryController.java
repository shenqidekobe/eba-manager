package com.microBusiness.manage.controller.admin.ass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ass.AssGoodDirectoryService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;

@Controller("assGoodDirectory")
@RequestMapping("/admin/ass/goodDirectory")
public class AssGoodDirectoryController extends BaseController {
	@Resource
	private AssGoodDirectoryService assGoodDirectoryService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list(String theme , Pageable pageable , ModelMap model) {
		if (theme == null) {
			theme="";
		}
		model.addAttribute("theme", theme);
		
		Supplier supplier = super.getCurrentSupplier();
		model.addAttribute("page", assGoodDirectoryService.findPage(theme,supplier,pageable));
		
		return "/admin/assGoodDirectory/list";
	}
	
	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/assGoodDirectory/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(String theme,String profiles,GoodsForm goodsForm) {
        try{
            Supplier supplier=super.getCurrentSupplier();
            assGoodDirectoryService.save(theme, profiles, supplier, goodsForm.getGoodsList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }
    }
	
	@RequestMapping(value = "/edit" , method = RequestMethod.GET)
	public String edit(Long id,ModelMap model) {
		AssGoodDirectory assGoodDirectory=assGoodDirectoryService.find(id);
		List<Goods> goods=assGoodDirectory.getGoods();
		List<Long> goodsIds=new ArrayList<>();
		for (Goods good : goods) {
			goodsIds.add(good.getId());
		}
		model.addAttribute("assGoodDirectory", assGoodDirectory);
		model.addAttribute("goodsIds", goodsIds);
		return "/admin/assGoodDirectory/edit";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,String profiles,GoodsForm goodsForm) {
        try{
        	assGoodDirectoryService.update(id,  profiles, goodsForm.getGoodsList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }
    }
	
	@RequestMapping(value = "/view" , method = RequestMethod.GET)
	public String view(Long id,ModelMap model) {
		AssGoodDirectory assGoodDirectory=assGoodDirectoryService.find(id);
		model.addAttribute("assGoodDirectory", assGoodDirectory);
		return "/admin/assGoodDirectory/view";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		try{
			for (Long id : ids) {
				assGoodDirectoryService.deleted(id);
				//删除更新标志
				AssGoodDirectory assGoodDirectory = assGoodDirectoryService.find(id);
				List<AssUpdateTips> list = assUpdateTipsService.findList(null, assGoodDirectory, null, AssUpdateTips.Type.companyGoods);
				for(AssUpdateTips updateTips : list) {
					assUpdateTipsService.delete(updateTips);
				}
			}
			return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }
	}
	
	@RequestMapping(value = "/getGoodList", method = RequestMethod.GET)
	public String getGoodList(Long productCategoryId,Pageable pageable, ModelMap model, String goodName) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		model.addAttribute("productCategoryTree", productCategoryService.findTree(super.getCurrentSupplier() , null));
		model.addAttribute("productCategoryId", productCategoryId);
		Page<Goods> page=goodsService.findPage(getCurrentSupplier(),goodName,productCategory,pageable);
		model.addAttribute("page",page);
		return "/admin/assGoodDirectory/goodList";
	}
	
	@RequestMapping(value = "/getViewGoodList", method = RequestMethod.GET)
	public String getViewGoodList(Long assGoodDirectoryId,Pageable pageable, ModelMap model, String goodName) {
		AssGoodDirectory assGoodDirectory=assGoodDirectoryService.find(assGoodDirectoryId);
		Page<Goods> page=goodsService.findPage(assGoodDirectory,goodName,pageable);
		model.addAttribute("productCategoryId", assGoodDirectoryId);
		model.addAttribute("page",page);
		return "/admin/assGoodDirectory/good_list_view";
	}
	
	@RequestMapping(value = "/checkTheme", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkTheme(String theme) {
		if (StringUtils.isEmpty(theme)) {
			return false;
		}
		Supplier supplier=super.getCurrentSupplier();
		return !assGoodDirectoryService.themeExists(theme,supplier);
	}
	
	public static class GoodsForm {

        private List<Goods> goodsList;

		public List<Goods> getGoodsList() {
			return goodsList;
		}

		public void setGoodsList(List<Goods> goodsList) {
			this.goodsList = goodsList;
		}

      
    }
	
}
