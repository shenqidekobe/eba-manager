package com.microBusiness.manage.controller.ass;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssListMemberStatus;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.ass.AssCartItemService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssGoodsSyncLogService;
import com.microBusiness.manage.service.ass.AssProductService;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assGoodsController")
@RequestMapping("/ass/goods")
public class AssGoodsController extends BaseController {
	
	@Resource
	private AssCustomerRelationService assCustomerRelationService;
	@Resource
	private AssGoodsService assGoodsService;
	@Resource
	private AssProductService assProductService;
	@Resource
	private AssCartItemService assCartItemService;
	@Resource
	private AssGoodsSyncLogService assGoodsSyncLogService;
	
	/**
	 * 添加助手商品
	 * @param assGoods
	 * @param options
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity save(AssGoods assGoods, Long assCustomerRelationId, AssSpecificationFrom assSpecificationFrom, String unionId) {
		if(null == assCustomerRelationId) {
			return JsonEntity.error(Code.code13001, Code.code13001.getDesc()); 
		}
		if(null == assGoods.getName()) {
			return JsonEntity.error(Code.code13002, Code.code13002.getDesc()); 
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		//获取供应商信息
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.findByCustomerRelation(assCustomerRelationId);
		if(assCustomerRelation == null || assCustomerRelation.getId() == null) {
			return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		}
		//判断新添加的商品名称是否已经存在
		boolean bool = assGoodsService.findNameExist(assCustomerRelation, assGoods);
		if(bool) {
			return JsonEntity.error(Code.code13007, Code.code13007.getDesc());
		}
		
		//规格
		List<String> specification = assSpecificationFrom.getSpecification();
		try {
			if(specification != null) {//添加有规格的商品
				assGoodsService.save(assGoods, assCustomerRelation, specification);
			}else {//添加没有规格的商品
				AssProduct assProduct = new AssProduct();
				assGoodsService.save(assGoods, assProduct, assCustomerRelation);
			}
			return JsonEntity.successMessage();
			
		} catch (Exception e) {
			return JsonEntity.error(Code.code13006, Code.code13006.getDesc());
		}
		
	}
	
	/**
	 * 查看商品
	 * @param goodId 商品id
	 * @return
	 */
	@RequestMapping(value = "/findAssGoods" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity editAssGoods(Long goodId , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == goodId) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		//获取商品信息
		AssGoods assGoods = assGoodsService.find(goodId);
		if(null != assGoods) {
			map.put("goodId", assGoods.getId());
			map.put("name", assGoods.getName());
			map.put("image", assGoods.getImage());
			map.put("sn", assGoods.getSn());
			map.put("unit", assGoods.getUnit());
			map.put("assCustomerRelationId", assGoods.getAssCustomerRelation().getId());
			map.put("labels", assGoods.getLabels());
			map.put("detailsDescription", assGoods.getDetailsDescription());
			map.put("detailsImage", assGoods.getDetailsImage());
			map.put("sourceType", assGoods.getSourceType());
			map.put("details", assGoods.getDetails());
			AssGoods ass = assGoods.getSource();
			if(ass != null && ass.getAssCustomerRelation().getShareType() == AssCustomerRelation.ShareType.share) {
				map.put("exist", false);
			}else {
				map.put("exist", true);
			}
		}
		//根据商品id获取assProduct(规格)
		List<AssProduct> assProducts = assProductService.findByList(assGoods);
		List<Map<String, Object>> assProductList = new ArrayList<Map<String,Object>>();
		if(assProducts == null) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		for(final AssProduct assProduct : assProducts) {
			assProductList.add(new HashMap<String, Object>(){{
				this.put("productId", assProduct.getId());
				//判断是否为null，如果为null显示空
				if(StringUtils.isEmpty(assProduct.getSpecification())) {
					this.put("specification", "");
				}else {
					this.put("specification", assProduct.getSpecification());
				}
				//是否删除规格(默认不删除)
				this.put("exist", assProduct.getExist());
			}});
		}
		map.put("assProductList", assProductList);
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 修改商品
	 * @param assGoods 助手商品
	 * @param assCustomerRelationId 供应商id
	 * @param assProductListFrom assProduct集合
	 * @param unionId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateAssGoods" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity updateAssGoods(AssGoods assGoods , Long assCustomerRelationId, 
			AssProductListFrom assProductListFrom , String unionId , HttpServletRequest request) {
		if(assGoods.getId() == null || assCustomerRelationId == null) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		if(null == assGoods.getName()) {
			return JsonEntity.error(Code.code13002, Code.code13002.getDesc());
		}
		//获取账号信息
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		List<AssProduct> assProducts = assProductListFrom.getpAssProducts();
		if(assProducts == null) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		//获取供应商信息
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.findByCustomerRelation(assCustomerRelationId);
		if(assCustomerRelation == null || assCustomerRelation.getId() == null) {
			return JsonEntity.error(Code.code13004, Code.code13004.getDesc());
		}
		//判断新添加的商品名称是否已经存在
		boolean bool = assGoodsService.findNameExist(assCustomerRelation, assGoods);
		if(bool) {
			return JsonEntity.error(Code.code13007, Code.code13007.getDesc());
		}
		
		//修改商品
		assGoodsService.updateAssGoods(assGoods, assProducts, assCustomerRelation);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 删除商品
	 * @param id 商品id
	 * @return
	 */
	@RequestMapping(value = "/deleteAssGoods" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity deleteAssGoods(Long id , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == id) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		AssGoods assGoods = assGoodsService.find(id);
		//获取assProduct
		Set<AssProduct> assProducts = assGoods.getAssProducts();
		List<Long> ids = new ArrayList<Long>();
		for(AssProduct assProduct : assProducts) {
			ids.add(assProduct.getId());
		}
		List<AssCartItem> assCartItems = assCartItemService.findByList(ids);
		try {
			if(null != assCartItems) {
				for(AssCartItem assCartItem : assCartItems) {
					assCartItemService.delete(assCartItem);
				}
			}
			for(AssProduct assProduct : assProducts) {
				assProductService.deleted(assProduct);
			}
			assGoodsService.deleted(id);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return  JsonEntity.error(Code.code13005, Code.code13005.getDesc());
		}
	}
	
	/**
	 * 获取商品规格
	 * @param id 商品id
	 * @return
	 */
	@RequestMapping(value = "/getSpecifications" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity getSpecifications(Long id , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		if(null == id) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		//此处根据商品id查询assProduct，也就是规格，页面上显示的规格也是assProduct
		List<AssProduct> assProducts = assGoodsService.getSpecifications(id);
		for(AssProduct assProduct : assProducts) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productId", assProduct.getId());
			map.put("specification", assProduct.getSpecification());
			resultMap.add(map);
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 根据供应商获取商品列表
	 * @param assCustomerRelationId 供应商id
	 * @param name 商品名称
	 * @param pageable
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/supplierGoodsList" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity supplierGoodsList(Long assCustomerRelationId , String name , Pageable pageable , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == assCustomerRelationId) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		//获取供应商信息
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.findByCustomerRelation(assCustomerRelationId);
		Page<AssGoods> page = assGoodsService.findByList(assCustomerRelation, name, pageable);
		List<AssGoods> assGoodsList = page.getContent();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		for(AssGoods assGoods : assGoodsList) {
			Map<String, Object> map = new HashMap<String, Object>();
			//获取商品规格
			List<AssProduct> assProducts = assGoodsService.getSpecifications(assGoods.getId());
			if(assProducts.size() > 1) {
				map.put("exist", "many");//多规格
				map.put("goodId", assGoods.getId());
				map.put("name", assGoods.getName());
				map.put("image", assGoods.getImage());
				map.put("labels", assGoods.getLabels());
				List<Long> productIds = new ArrayList<Long>();
				for(AssProduct assProduct : assProducts) {
					productIds.add(assProduct.getId());
				}
				map.put("productIds", productIds);
				
			}else {
				if(StringUtils.isEmpty(assProducts.get(0).getSpecification())){
					map.put("exist", "single");//没有规格
					map.put("productId", assProducts.get(0).getId());
					map.put("name", assProducts.get(0).getAssGoods().getName());
					map.put("image", assProducts.get(0).getAssGoods().getImage());
					map.put("labels", assProducts.get(0).getAssGoods().getLabels());
				}else{
					map.put("exist", "many");//单规格
					map.put("goodId", assGoods.getId());
					map.put("name", assGoods.getName());
					map.put("image", assGoods.getImage());
					map.put("labels", assGoods.getLabels());
					List<Long> productIds = new ArrayList<Long>();
					for(AssProduct assProduct : assProducts) {
						productIds.add(assProduct.getId());
					}
					map.put("productIds", productIds);
				}
				
			}
			resultMap.add(map);
		}
		result.put("resultMap", resultMap);
		result.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(result);
	}
	
	/**
	 * 根据供应商获取商品列表(此处返回的是商品信息和assProduct信息)
	 * @param assCustomerRelationId 供应商id
	 * @param name 商品名称
	 * @param pageable
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/goodsByList" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity goodsByList(Long assCustomerRelationId , String name , Pageable pageable , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == assCustomerRelationId) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		//获取供应商信息
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.findByCustomerRelation(assCustomerRelationId);
		Page<AssGoods> page = assGoodsService.findByList(assCustomerRelation, name, pageable);
		List<AssGoods> assGoodsList = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(AssGoods assGoods : assGoodsList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodId", assGoods.getId());
			map.put("name", assGoods.getName());
			map.put("image", assGoods.getImage());
			map.put("labels", assGoods.getLabels());
			map.put("sourceType", assGoods.getSourceType());
			if(null == assGoods.getGoods()) {
				map.put("sour", false);
			}else {
				map.put("sour", true);
			}
			//获取商品规格
			List<AssProduct> assProducts = assGoodsService.getSpecifications(assGoods.getId());
			List<Map<String, Object>> assPrList = new ArrayList<Map<String,Object>>();
			for(AssProduct assProduct : assProducts) {
				Map<String, Object> productMap = new HashMap<String, Object>();
				productMap.put("id", assProduct.getId());
				productMap.put("specification", assProduct.getSpecification());
				assPrList.add(productMap);
			}
			map.put("assPrList", assPrList);
			resultList.add(map);
		}
		resultMap.put("resultList", resultList);
		resultMap.put("clientName", assCustomerRelation.getClientName());
		resultMap.put("theme", assCustomerRelation.getTheme());
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 获取要复制的供应商
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/getCopySupplier" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity getCopySupplier(String unionId,Long assCustomerRelationId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		List<AssCustomerRelation> assCustomerRelations = assCustomerRelationService.findCustomerRelationList(assChildMember , assCustomerRelationId);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(AssCustomerRelation relation : assCustomerRelations) {
			Map<String, Object> relationMap = new HashMap<String, Object>();
			relationMap.put("id", relation.getId());
			relationMap.put("clientName", relation.getClientName());
			relationMap.put("theme", relation.getTheme());
			resultList.add(relationMap);
		}
		return JsonEntity.successMessage(resultList);
	}
	
	
	/**
	 * 复制商品
	 * @param assCustomerRelationId 供应商id
	 * @param goodsId 商品id
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/copyGoods" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity copyGoods(Long assCustomerRelationId , Long goodsId , String unionId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == assCustomerRelationId) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		if(null == goodsId) {
			return JsonEntity.error(Code.code13008, Code.code13008.getDesc());
		}
		//获取供应商信息
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.findByCustomerRelation(assCustomerRelationId);
		//获取供应商下面的所有商品
		List<AssGoods> list = assGoodsService.findByList(assCustomerRelation);
		//获取商品信息
		AssGoods assGoods = assGoodsService.find(goodsId);
		for(AssGoods goods : list) {
			if(goods.getName().equals(assGoods.getName())) {
				return JsonEntity.error(Code.code13010, Code.code13010.getDesc());
			}
		}
		try {
			//复制商品
			assGoodsService.copyGoods(assCustomerRelation, assGoods);
		} catch (Exception e) {
			return JsonEntity.error(Code.code13009, Code.code13009.getDesc());
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 查看商品详情
	 * @param unionId
	 * @param goodsId 商品id
	 * @return
	 */
	@RequestMapping(value = "/viewAssGoodDetails" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity viewAssGoodDetails(String unionId , Long goodsId) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == goodsId) {
			return JsonEntity.error(Code.code13011, Code.code13011.getDesc());
		}
		//获取商品信息
		AssGoods assGoods = assGoodsService.find(goodsId);
		Map<String, Object> resuletMap = new HashMap<String, Object>();
		resuletMap.put("id", assGoods.getId());
		resuletMap.put("detailsDescription", assGoods.getDetailsDescription());
		resuletMap.put("detailsImage", assGoods.getDetailsImage());
		return JsonEntity.successMessage(resuletMap);
	}
	
	/**
	 * 修改商品详情
	 * @param unionId
	 * @param goodsId 商品id
	 * @param detailsDescription 商品详情描述
	 * @param detailsImage 商品详情图片
	 * @return
	 */
	@RequestMapping(value = "/updateAssGoodDetails" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity updateAssGoodDetails(String unionId , Long goodsId , String detailsDescription , DetailsImageFrom detailsImageFrom) {
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		if(null == goodsId) {
			return JsonEntity.error(Code.code13011, Code.code13011.getDesc());
		}
		//获取商品信息
		AssGoods assGoods = assGoodsService.find(goodsId);
		assGoods.setDetailsDescription(detailsDescription);
		assGoods.setDetailsImage(detailsImageFrom.detailsImage);
		assGoodsService.update(assGoods);
		return JsonEntity.successMessage();
	}
	
	
	public static class AssProductListFrom {
		
		private List<AssProduct> pAssProducts;

		public List<AssProduct> getpAssProducts() {
			return pAssProducts;
		}

		public void setpAssProducts(List<AssProduct> pAssProducts) {
			this.pAssProducts = pAssProducts;
		}
	}
	
	public static class AssSpecificationFrom {
		private List<String> specification;

		public List<String> getSpecification() {
			return specification;
		}

		public void setSpecification(List<String> specification) {
			this.specification = specification;
		}
	}
	
	public static class DetailsImageFrom {
		private List<String> detailsImage;

		public List<String> getDetailsImage() {
			return detailsImage;
		}

		public void setDetailsImage(List<String> detailsImage) {
			this.detailsImage = detailsImage;
		}
	}
	
	/**
	 * 分享商品
	 * @param unionId
	 * @param id  供应商id
	 * @return
	 */
	@RequestMapping(value = "/share" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity share(String unionId,Long goodId,String sn, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||goodId == null || StringUtils.isBlank(sn)) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssGoods assGoods=assGoodsService.find(goodId);
		AssCustomerRelation assCustomerRelation = assGoods.getAssCustomerRelation();
		if (!assChildMember.equals(assCustomerRelation.getAssChildMember())) {
			return new JsonEntity(Code.code_customer_11110 , Code.code_customer_11110.getDesc());
		}
		if (assGoodsService.findBySn(sn) != null) {
			return new JsonEntity(Code.code_assgoods_11110, Code.code_assgoods_11110.getDesc());
		}
		
		AssGoods goods=assGoodsService.copySingleShareAssAssGoods(assCustomerRelation, assGoods,sn);
		Map<String, Object> map=new HashMap<>();
		map.put("id", goods.getId());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 查看分享
	 * @param unionId
	 * @param shareSn
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getShare" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getShare(String unionId,String sn, HttpServletRequest request, HttpServletResponse response){
		if(unionId==null || null == sn) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		AssChildMember assChildMember = super.getAssChildMember(unionId);
		Map<String, Object> map = new HashMap<String, Object>();
		//获取商品信息
		AssGoods assGoods = assGoodsService.findBySn(sn);
		if(null != assGoods) {
			map.put("goodId", assGoods.getId());
			map.put("name", assGoods.getName());
			map.put("image", assGoods.getImage());
			map.put("sn", assGoods.getSn());
			map.put("unit", assGoods.getUnit());
			map.put("assCustomerRelationId", assGoods.getAssCustomerRelation().getId());
			map.put("labels", assGoods.getLabels());
			map.put("detailsDescription", assGoods.getDetailsDescription());
			map.put("detailsImage", assGoods.getDetailsImage());
			map.put("details", assGoods.getDetails());
		}
		//根据商品id获取assProduct(规格)
		List<AssProduct> assProducts = assProductService.findByList(assGoods);
		List<Map<String, Object>> assProductList = new ArrayList<Map<String,Object>>();
		if(assProducts == null) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		for(final AssProduct assProduct : assProducts) {
			assProductList.add(new HashMap<String, Object>(){{
				this.put("productId", assProduct.getId());
				//判断是否为null，如果为null显示空
				if(StringUtils.isEmpty(assProduct.getSpecification())) {
					this.put("specification", "");
				}else {
					this.put("specification", assProduct.getSpecification());
				}
			}});
		}
		map.put("assProductList", assProductList);
		
		//当前用户相对于分享的角色  分享人  同步过的人  未同步过的人 
		AssListMemberStatus memberStatus;
		if (assChildMember.equals(assGoods.getAssCustomerRelation().getAssChildMember())) {
			 memberStatus=AssListMemberStatus.self;
		}else {
			if (assGoodsService.findBySource(assGoods,assChildMember) != null ) {
				memberStatus=AssListMemberStatus.participate;
			}else {
				memberStatus=AssListMemberStatus.noparticipate;
			}
		}
		map.put("memberStatus", memberStatus);
		
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 同步商品时验证商品名是否重复
	 * @param unionId
	 * @param goodId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/verifyGoodName" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity verifyGoodName(String unionId,Long goodId, HttpServletRequest request, HttpServletResponse response){
		if(unionId==null ||goodId == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssGoods assGoods=assGoodsService.find(goodId);
		AssCustomerRelation assCustomerRelation = assGoods.getAssCustomerRelation();
		AssCustomerRelation relation = assCustomerRelationService.inviteNameExists(assCustomerRelation.getClientName(), assCustomerRelation.getTheme(), assChildMember);
		Map<String, Object> map=new HashMap<>();
		if (relation == null) {
			map.put("exist", false);
		}else {
			if (assGoodsService.findNameExist(relation, assGoods)) {
				map.put("exist", true);
			}else {
				map.put("exist", false);
			}
		}
		return JsonEntity.successMessage(map);
	}
	
	/**&
	 * 同步商品
	 * @param unionId
	 * @param goodId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/synchronize" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity synchronize(String unionId,Long goodId, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||goodId == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssGoods assGoods=assGoodsService.find(goodId);
		AssCustomerRelation assCustomerRelation = assGoods.getAssCustomerRelation();
		if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
			return new JsonEntity(Code.code_customer_11111, Code.code_customer_11111.getDesc());
		}
		AssGoods goods=assGoodsService.copySingleNoShareAssAssGoods(assCustomerRelation, assGoods, assChildMember);
		Map<String, Object> map=new HashMap<>();
		map.put("id", goods.getAssCustomerRelation().getId());
		
		// 新增商品同步目录
		assGoodsSyncLogService.add(assCustomerRelation, assChildMember);
		return JsonEntity.successMessage(map);
	}
	
	
	/**
	 * 商品分享海报
	 * @param goodId
	 * @param unionId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getPoster" , method = RequestMethod.GET)
	public void getPoster(Long goodId, String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssGoods assGoods=assGoodsService.find(goodId);
		try {
			BufferedImage image=assGoodsService.getPoster(assGoods, request, response);
			OutputStream stream = response.getOutputStream();
			ImageIO.write(image, "jpg", stream);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
