package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;

/**
 * .正式供应分配
 * @author Administrator
 *
 */
@Controller("supplyDistributionController")
@RequestMapping("/admin/supplyDistribution")
public class SupplyDistributionController extends BaseController {
	@Resource(name = "supplierSupplierServiceImpl")
	private SupplierSupplierService supplierSupplierService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "supplierAssignRelationServiceImpl")
	private SupplierAssignRelationService supplierAssignRelationService;
	@Resource(name = "supplierNeedProductServiceImpl")
	private SupplierNeedProductService supplierNeedProductService;
	@Resource
    private NeedService needService ;

	@Resource
	private ProductCategoryService productCategoryService ;
	
	@Resource
	private SupplyDistributionService supplyDistributionService;
	@Resource(name = "supplierProductServiceImpl")
	private SupplierProductService supplierProductService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model, Pageable pageable , SupplierSupplier.Status status , String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		model.put("status", status);
		model.put("searchName", searchName);
		//model.put("page", supplierSupplierService.findPage(pageable, null, supplier, null, null));
		model.put("page", supplierSupplierService.supplyDistributionList(pageable, null, supplier, status, searchName));
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/supplyDistribution/list";
	}
	
	@RequestMapping(value = "/asyncList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncList(ModelMap model, Pageable pageable , SupplierSupplier.Status status , String searchName) {
		Supplier supplier = super.getCurrentSupplier();
		Page<SupplierSupplier> page=supplierSupplierService.supplyDistributionList(pageable, null, supplier, status, searchName);
		List<Map<String, Object>> list=new ArrayList<>();
		for (SupplierSupplier supplierSupplier : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", supplierSupplier.getId());
			map.put("status", supplierSupplier.getStatus());
			map.put("bySupplierName", supplierSupplier.getBySupplier().getName());
			map.put("supplierName", supplierSupplier.getSupplier().getName());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
    public Message updateStatus(SupplierSupplier supplierSupplier) {
		supplierSupplierService.updateStatus(supplierSupplier);
        return SUCCESS_MESSAGE;
    }
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id,ModelMap model,String type) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		model.put("supplierSupplier", supplierSupplier);
		model.put("type", type);
		return "/admin/supplyDistribution/view";
	}
	@RequestMapping(value = "/viewGoods", method = RequestMethod.GET)
	public String viewGoods(Long supplierSupplierId,ModelMap model,Pageable pageable , Long productCategoryId) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
		/*Set<SupplierProduct> supplierProducts=supplierSupplier.getSupplierProducts();
		List<Long> goodIds=new ArrayList<>();
		for (SupplierProduct supplierProduct : supplierProducts) {
			goodIds.add(supplierProduct.getProducts().getGoods().getId());
		}
		//去掉重复的id
		List<Long> newgoodids = new ArrayList<>();
		for(Long cd : goodIds) {
			if(!newgoodids.contains(cd)) {
				newgoodids.add(cd);
			}
		}
		Page<Goods> page=goodsService.findBySupplierSupplierPage(newgoodids, pageable , productCategoryId);
		List<Goods> goods=page.getContent();
		for (Goods good : goods) {
			for (SupplierProduct supplierProduct : supplierProducts) {
				if (good.getId() == supplierProduct.getProducts().getGoods().getId()) {
					good.getSupplierProducts().add(supplierProduct);
				}
			}
		}*/
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		model.put("page", page);
		model.put("supplierSupplier", supplierSupplier);
		return "/admin/supplyDistribution/good_list_view";
	}
	
	@RequestMapping(value = "/asyncViewGoods", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity asyncViewGoods(Long supplierSupplierId,Pageable pageable , Long productCategoryId) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		List<Map<String, Object>> list=new ArrayList<>();
		for (SupplierProduct supplierProduct : page.getContent()) {
			Product product=supplierProduct.getProducts();
			Map<String, Object> map=new HashMap<>();
			map.put("id", product.getId());
			map.put("image", product.getGoods().getImage());
			map.put("name", product.getGoods().getName());
			map.put("specificationValues", product.getSpecificationValues());
			map.put("supplyPrice",supplierProduct.getSupplyPrice());
			map.put("minOrderQuantity", supplierProduct.getMinOrderQuantity());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/distributionList", method = RequestMethod.GET)
	public String distributionList(ModelMap model,Long id,Pageable pageable , Need.Status status) {
		Supplier supplier=super.getCurrentSupplier();
		/*Page<Need> needs=needService.findPage(pageable,supplier , Need.Type.general);
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		List<SupplierAssignRelation> supplierAssignRelations = supplierAssignRelationService.findListBySupplier(supplierSupplier,null);
		for (Need need : needs.getContent()) {
			for (SupplierAssignRelation supplierAssignRelation : supplierAssignRelations) {
				if (supplierAssignRelation.getNeed().getId() == need.getId()) {
					need.setStatus(Need.Status.on);
				}
			}
		}*/
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		Page<Need> needs = needService.findPage(pageable, supplier, supplierSupplier, Need.Type.general, Need.NeedStatus.available, status,ShopType.direct);
		List<SupplierAssignRelation> supplierAssignRelations = supplierAssignRelationService.findListBySupplier(supplierSupplier,null);
		for (Need need : needs.getContent()) {
			for (SupplierAssignRelation supplierAssignRelation : supplierAssignRelations) {
				if (supplierAssignRelation.getNeed().getId() == need.getId() && supplierAssignRelation.getSupplierNeedProducts().size()>0) {
					need.setStatus(Need.Status.on);
				}
			}
		}
		model.put("page", needs);
		model.put("id", id);
		model.put("status", status);
		return "/admin/supplyDistribution/distributionList";
	}
	
	@RequestMapping(value = "/distributionView", method = RequestMethod.GET)
	public String distribution(ModelMap model,Long id,Long needId) {
		Need need=needService.find(needId);
		SupplierSupplier supplierSupplier=supplierSupplierService.find(id);
		List<SupplierNeedProduct> list=supplierNeedProductService.findBySupplier(supplierSupplier,need);
		String needProductIds="{\"ids\":[";
		for (int i = 0; i < list.size(); i++) {
			if (i==list.size()-1) {
				needProductIds+=list.get(i).getProducts().getId();
			}else {
				needProductIds+=list.get(i).getProducts().getId()+",";
			}
		}
		needProductIds+="]}";
		model.put("needProductIds", needProductIds);
		model.put("id", id);
		model.put("need", need);
		return "/admin/supplyDistribution/distribution_view";
	}
	@RequestMapping(value = "/distributionGoods", method = RequestMethod.GET)
	public String distributionGoods(Long supplierSupplierId,Long needId,ModelMap model,Pageable pageable , Long productCategoryId) {
		SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
		/*Set<SupplierProduct> supplierProducts=supplierSupplier.getSupplierProducts();
		List<Long> goodIds=new ArrayList<>();
		for (SupplierProduct supplierProduct : supplierProducts) {
			// FIXME: 2017/3/14 这里不建议这样写，而且并没有去重 ， 分页总数应该重新计算
			if(!goodIds.contains(supplierProduct.getProducts().getGoods().getId())){
				goodIds.add(supplierProduct.getProducts().getGoods().getId());
			}
		}
		Page<Goods> page=goodsService.findBySupplierSupplierPage(goodIds, pageable , productCategoryId);
		List<Goods> goods=page.getContent();
		for (Goods good : goods) {
			for (SupplierProduct supplierProduct : supplierProducts) {
				// FIXME: 2017/3/14 这里不能这样比较，应该使用compareTo 或 使用 equals
				if (good.getId().compareTo(supplierProduct.getProducts().getGoods().getId()) == 0 ) {
					good.getSupplierProducts().add(supplierProduct);
				}
			}
		}*/
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		model.put("page", page);
		model.put("needId", needId);
		model.put("supplierSupplier", supplierSupplier);
		model.put("productCategoryTree" , productCategoryService.findByAssSupplier(supplierSupplierId));
		model.put("productCategoryId", productCategoryId);
		return "/admin/supplyDistribution/distribution_good";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(Long supplierSupplierId,Long needId,SupplierNeedProductForm supplierNeedProductForm) {
        try{
            SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
            Need need=needService.find(needId);
            List<SupplierAssignRelation> supplierAssignRelations=supplierAssignRelationService.findListBySupplier(supplierSupplier, need);
            SupplierAssignRelation supplierAssignRelation=null;
            if (supplierAssignRelations.size() > 0) {
            	supplierAssignRelation=supplierAssignRelations.get(0);
			}else {
				supplierAssignRelation=new SupplierAssignRelation();
				supplierAssignRelation.setSupplyRelation(supplierSupplier);
				supplierAssignRelation.setNeed(need);
			}
            //删除
            //supplierAssignRelationService.deleteNeed(supplierSupplier,need);
            //supplierAssignRelationService.save(supplierAssignRelation,supplierNeedProductForm.getSupplierNeedProductList());
            supplyDistributionService.supplyDistribution(supplierSupplier, need, supplierAssignRelation, supplierNeedProductForm.getSupplierNeedProductList());
            return SUCCESS_MESSAGE;
        }catch (Exception e){
            return ERROR_MESSAGE;
        }

    }
	public static class SupplierNeedProductForm {

        private List<SupplierNeedProduct> supplierNeedProductList;

		public List<SupplierNeedProduct> getSupplierNeedProductList() {
			return supplierNeedProductList;
		}

		public void setSupplierNeedProductList(List<SupplierNeedProduct> supplierNeedProductList) {
			this.supplierNeedProductList = supplierNeedProductList;
		}
        
    }
	
	/**
	 *批量分配商品显示
	 * @param model
	 * @param id
	 * @param needId
	 * @return
	 */
	@RequestMapping(value = "/batchdistributionGoods", method = RequestMethod.GET)
	public String batchdistributionGoods(ModelMap model , Long id , Pageable pageable , Long productCategoryId , String needNum) {
		SupplierSupplier supplierSupplier = supplierSupplierService.find(id);
		/*Set<SupplierProduct> supplierProducts = supplierSupplier.getSupplierProducts();
		List<Long> goodIds=new ArrayList<>();
		for (SupplierProduct supplierProduct : supplierProducts) {
			// FIXME: 2017/3/14 这里不建议这样写，而且并没有去重 ， 分页总数应该重新计算
			if(!goodIds.contains(supplierProduct.getProducts().getGoods().getId())){
				goodIds.add(supplierProduct.getProducts().getGoods().getId());
			}
		}
		Page<Goods> page = goodsService.findBySupplierSupplierPage(goodIds, pageable, productCategoryId);
		List<Goods> goods = page.getContent();
		for (Goods good : goods) {
			for (SupplierProduct supplierProduct : supplierProducts) {
				// FIXME: 2017/3/14 这里不能这样比较，应该使用compareTo 或 使用 equals
				if (good.getId().compareTo(supplierProduct.getProducts().getGoods().getId()) == 0 ) {
					good.getSupplierProducts().add(supplierProduct);
				}
			}
		}*/
		Page<SupplierProduct> page = supplierProductService.findPage(pageable, productCategoryId, supplierSupplier);
		model.put("page", page);
		model.put("supplierSupplier", supplierSupplier);
		model.put("productCategoryTree" , productCategoryService.findByAssSupplier(id));
		model.put("productCategoryId", productCategoryId);
		model.put("needNum", needNum);
		return "/admin/supplyDistribution/batchdistribution_good";
	}
	
	
	/**
	 * 批量给收货点分配商品
	 * @param supplierSupplierId
	 * @param needId
	 * @param supplierNeedProductForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/batchDistributionSave", method = RequestMethod.POST)
    public Message batchDistributionSave(Long supplierSupplierId,Long[] needId,SupplierNeedProductForm supplierNeedProductForm) {
		try{
			SupplierSupplier supplierSupplier=supplierSupplierService.find(supplierSupplierId);
			
            supplyDistributionService.supplyDistribution(supplierSupplier, needId, supplierNeedProductForm.getSupplierNeedProductList());

			return SUCCESS_MESSAGE;
		}catch (Exception e){
            return ERROR_MESSAGE;
        }

    }
	
	@RequestMapping(value = "/searchMobile" , method = RequestMethod.GET)
	public String searchMobile() {
		return "/admin/supplyDistribution/search";
	}


}
