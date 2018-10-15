package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssGoodDirectoryService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品目录
 * @author yuezhiwei
 *
 */
@Controller
@RequestMapping("/ass/assGoodsCatalogue")
public class AssGoodsCatalogueController extends BaseController {

	@Resource
	private AssChildMemberService assChildMemberService;
	@Resource
	private AssGoodDirectoryService assGoodDirectoryService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private AssUpdateTipsService assUpdateTipsService;
	
	/**
	 * 商品目录列表
	 * @param unionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public JsonEntity list(String unionId , Pageable pageable) {
		if(null == unionId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		final AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		if(null != childMember && null != childMember.getAdmin()) {
			Page<AssGoodDirectory> page = assGoodDirectoryService.findPage(childMember, pageable);
			List<AssGoodDirectory> goodDirectories = page.getContent();
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(final AssGoodDirectory directory : goodDirectories) {
				list.add(new HashMap<String, Object>(){{
					this.put("id", directory.getId());
					this.put("profiles", directory.getProfiles());
					this.put("theme", directory.getTheme());
					this.put("clientName", directory.getSupplier().getName());
					AssUpdateTips assUpdateTips = assUpdateTipsService.find(childMember, directory, AssUpdateTips.Type.companyGoods);
					if(null != assUpdateTips) {
						if(assUpdateTips.getWhetherUpdate() == AssUpdateTips.WhetherUpdate.yes) {
							this.put("updateTips", true);
						} else {
							this.put("updateTips", false);
						}
					}else {
						this.put("updateTips", false);
					}
				}});
			}
			resultmap.put("list", list);
			resultmap.put("totalPages", page.getTotalPages());
		}else {
			resultmap.put("list", null);
			resultmap.put("totalPages", null);
		}
		return JsonEntity.successMessage(resultmap);
	}
	
	/**
	 * 商品列表
	 * @param unionId
	 * @param id 商品目录id
	 * @param pageable
	 * @param goodsName 商品名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/goodsList" , method = RequestMethod.GET)
	public JsonEntity goodsList(String unionId , Long id , String goodsName , Pageable pageable) {
		if(null == unionId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
		if(null == id) {
			return new JsonEntity(Code.code_goods_catalog_100001, Code.code_goods_catalog_100001.getDesc());
		}
		AssGoodDirectory assGoodDirectory = assGoodDirectoryService.find(id);
		Page<Goods> page = goodsService.findPage(assGoodDirectory, goodsName, pageable);
		List<Goods> goodsList = page.getContent();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(Goods goods : goodsList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodId", goods.getId());
			map.put("name", goods.getName());
			List<String> images = new ArrayList<String>();
			if(!StringUtils.isEmpty(goods.getImage())) {
				images.add(goods.getImage());
			}
			List<String> imageList = goods.getImages();
			for(String str : imageList) {
				images.add(str);
			}
			//map.put("image", goods.getImage());
			map.put("image", images);
			map.put("labels", goods.getLabels());
			Set<Product> set = goods.getProducts();
			List<Map<String, Object>> products = new ArrayList<Map<String,Object>>();
			for(final Product product : set) {
				products.add(new HashMap<String, Object>(){{
					this.put("id", product.getId());
					this.put("spec", product.getSpecificationValues());
				}});
			}
			map.put("products", products);
			list.add(map);
		}
		AssUpdateTips updateTips = assUpdateTipsService.find(childMember, assGoodDirectory, AssUpdateTips.Type.companyGoods);
		if(null != updateTips) {
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.no);
			assUpdateTipsService.update(updateTips);
		}
		resultMap.put("list", list);
		resultMap.put("goodDirectoryId", assGoodDirectory.getId());
		resultMap.put("theme", assGoodDirectory.getTheme());
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 同步商品目录
	 * @param unionId
	 * @param id 商品目录id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copyGoodsCatalogue" , method = RequestMethod.POST)
	public JsonEntity copyGoodsCatalogue(String unionId , Long id) {
		if(null == unionId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
		if(null == id) {
			return new JsonEntity(Code.code_goods_catalog_100001, Code.code_goods_catalog_100001.getDesc());
		}
		AssGoodDirectory assGoodDirectory = assGoodDirectoryService.find(id);
		assGoodDirectoryService.copyGoodsCatalogue(assGoodDirectory , childMember);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 同步商品
	 * @param unionId
	 * @param goodsCatalogueId 商品目录id
	 * @param goodsId 商品id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copyGoods" , method = RequestMethod.POST)
	public JsonEntity copyGoods(String unionId , Long goodsCatalogueId , Long goodsId) {
		if(null == unionId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
		if(null == goodsCatalogueId) {
			return new JsonEntity(Code.code_goods_catalog_100001, Code.code_goods_catalog_100001.getDesc());
		}
		if(null == goodsId) {
			return new JsonEntity(Code.code_goods_catalog_100002, Code.code_goods_catalog_100002.getDesc());
		}
		AssGoodDirectory assGoodDirectory = assGoodDirectoryService.find(goodsCatalogueId);
		Goods goods = goodsService.find(goodsId);
		assGoodDirectoryService.copyGoods(childMember, assGoodDirectory, goods);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 商品详情
	 * @param unionId
	 * @param goodsId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryGoodsDetail" , method = RequestMethod.GET)
	public JsonEntity queryGoodsDetail(String unionId , Long goodsId) {
		if(null == unionId) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember childMember = assChildMemberService.findByUnionId(unionId);
		if(null == goodsId) {
			return new JsonEntity(Code.code_goods_catalog_100002, Code.code_goods_catalog_100002.getDesc());
		}
		Goods goods = goodsService.find(goodsId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", goods.getId());
		resultMap.put("name", goods.getName());
		List<String> image = new ArrayList<String>();
		if(!StringUtils.isEmpty(goods.getImage())) {
			image.add(goods.getImage());
		}
		List<String> imageList = goods.getImages();
		for(String str : imageList) {
			image.add(str);
		}
		resultMap.put("image", image);
		resultMap.put("labels", goods.getLabels());
		resultMap.put("details", goods.getIntroduction());
		Set<Product> products = goods.getProducts();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(final Product product : products) {
			list.add(new HashMap<String, Object>(){{
				this.put("id", product.getId());
				this.put("spec", product.getSpecificationValues());
			}});
		}
		resultMap.put("products", list);
		return JsonEntity.successMessage(resultMap);
	}
	
}
