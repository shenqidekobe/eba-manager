package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.entity.AdPosition;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ImgType;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.service.AdPositionService;
import com.microBusiness.manage.service.AdService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.WeChatService;

/**
 * 
 */
@Controller("apiIndexController")
@RequestMapping("/api/small/index")
public class IndexController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
    private MemberService memberService;
    @Resource
    private ProductService productService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private SupplierService supplierService;
    @Resource
    private ChildMemberService childMemberService ;
    @Resource
	private WeChatService weChatService;
    
    @Resource
    private ProductCategoryService productCategoryService ;
    
	@Resource
	private AdPositionService adPositionService;
	
	@Resource
	private AdService adService;
	
	/**
	 * 首页数据加载
	 * 轮播图、广告、商品、分类
	 * */
	@SuppressWarnings("serial")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity list(String unionId, String smOpenId, Long supplierId ,Long relationId, 
    		HttpServletRequest request , HttpServletResponse response){
		long startTime = System.currentTimeMillis();
		if(supplierId == null){
			supplierId = 1l;
		}
		//ChildMember childMember = childMemberService.findByOpenId(openId);
		Map<String, Object> map = new HashMap<String, Object>();
		//轮播图广告位
		AdPosition topadPosition = adPositionService.find(1l);
		//轮播图
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("adPosition");
		filter.setValue(topadPosition.getId());
		filters.add(filter);
		
		List<Order> adorders = new ArrayList<Order>();
		Order adorder = new Order();
		adorder.setDirection(Direction.asc);
		adorder.setProperty("order");
		adorders.add(adorder);
		
		List<Map<String, Object>> adMapList = new ArrayList<Map<String, Object>>();
		List<Ad> topadList = adService.findList(8, filters, adorders);
		if(topadList != null){
			for (Ad ad : topadList) {
				Map<String, Object> admap = new HashMap<String, Object>();
				admap.put("content", ad.getContent());
				admap.put("path", ad.getPath());
				if(StringUtils.isNotEmpty(ad.getPath())){
					String storePath = ad.getPath();
					String destMediumPath = getMImagePath(storePath);
	            	//String destSmallPath=paths[0]+"-"+ImgType.small+"."+paths[1];
	            	admap.put("path", destMediumPath);
				}
				admap.put("url", ad.getUrl());
				admap.put("title", ad.getTitle());
				admap.put("id", ad.getId());
				adMapList.add(admap);
			}
		}
		map.put("topadList", adMapList);
        
		//特价商品
		filters = new ArrayList<Filter>();
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isMainSell");
		filter.setValue(true);
		filters.add(filter);
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("deleted");
		filter.setValue(false);
		filters.add(filter);
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isMarketable");
		filter.setValue(true);
		filters.add(filter);
		List<Goods> mainGoodsList = goodsService.findList(0, 3, filters, null);
		List<Map<String, Object>> maingoodsMapList = new ArrayList<Map<String, Object>>();
		if(mainGoodsList != null){
			for (Goods goods : mainGoodsList) {
				Map<String, Object> gmap = new HashMap<String, Object>();
				gmap.put("goodsId", goods.getId());
				gmap.put("name", goods.getName());
				gmap.put("image", goods.getImage());
				if(StringUtils.isNotBlank(goods.getImage())){
					String storePath = goods.getImage();
					String destMediumPath = getMImagePath(storePath);
					gmap.put("image", destMediumPath);
				}
				
				gmap.put("price", goods.getPrice());
				gmap.put("sales", goods.getSales());
				maingoodsMapList.add(gmap);
			}
		}
		map.put("maingoodsMapList", maingoodsMapList);
		
		//精选TOP商品
		Order orderDir = new Order();
		orderDir.setDirection(Direction.asc);
		orderDir.setProperty("order");
		List<Order> orders = new ArrayList<Order>();
		orderDir = new Order();
		orderDir.setDirection(Direction.desc);
		orderDir.setProperty("hits");
		orders.add(orderDir);
		filters = new ArrayList<Filter>();
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("deleted");
		filter.setValue(false);
		filters.add(filter);
		filter = new Filter();
		filter.setIgnoreCase(true);
		filter.setOperator(Operator.eq);
		filter.setProperty("isMarketable");
		filter.setValue(true);
		filters.add(filter);
		List<Goods> goodsList = goodsService.findList(0, 20, filters, orders);
		 List<Map<String, Object>> goodsMapList = new ArrayList<Map<String,Object>>();
		 for(final Goods goods : goodsList) {
			 goodsMapList.add(new HashMap<String, Object>(){{
				 this.put("goodsId", goods.getId());
				 this.put("name", goods.getName());
				 this.put("image", goods.getImage());
				 if(StringUtils.isNotBlank(goods.getImage())){
						String storePath = goods.getImage();
						String destMediumPath = getMImagePath(storePath);
						this.put("image", destMediumPath);
					}
				 this.put("specification", goods.getSpecificationItems());
				 this.put("price", goods.getDefaultProduct().getPrice());
				 this.put("sales", goods.getSales());
				 this.put("hasSpecifications", goods.hasSpecification());
				 List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
				 for (Product product : goods.getProducts()) {
					 Map<String, Object> pmap = new HashMap<String, Object>();
					 pmap.put("productId", product.getId());
					 pmap.put("price", product.getPrice());
					 pmap.put("sales", product.getSales());
					 pmap.put("minOrderQuantity", product.getMinOrderQuantity());
					 pmap.put("addValue", product.getAddValue());
					 pmap.put("specifications", product.getSpecifications());
					 productList.add(pmap);
					 this.put("products", productList);
				}
			 }});
		 }
		 map.put("goodsList", goodsMapList);
		
		
		logger.info("time : " + (System.currentTimeMillis()-startTime));
		
        return JsonEntity.successMessage(map);
    }
	
	@RequestMapping(value = "/goodsList", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity goodsList(String unionId, String smOpenId, Long supplierId ,Long relationId, 
    		HttpServletRequest request , HttpServletResponse response, Pageable pageable){
		if(supplierId == null){
			supplierId = 1l;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Order> orders = new ArrayList<Order>();
		Order order = new Order();
		order.setDirection(Direction.desc);
		order.setProperty("hits");
		orders.add(order);
		List<Goods> goodsList = goodsService.findList(pageable.getPageNumber(), 
				pageable.getPageSize(), null, orders);
		
		List<Map<String, Object>> goodsMapList = new ArrayList<Map<String, Object>>();
		if(goodsList != null){
			for (Goods goods : goodsList) {
				Map<String, Object> gmap = new HashMap<String, Object>();
				gmap.put("goodsId", goods.getId());
				gmap.put("name", goods.getName());
				gmap.put("image", goods.getImage());
				gmap.put("price", goods.getPrice());
				gmap.put("sales", goods.getSales());
				goodsMapList.add(gmap);
			}
		}
		map.put("goodsList", goodsMapList);
		
		
		
		return JsonEntity.successMessage(map);
	}

	
	public String getMImagePath(String storePath){
		logger.info(storePath);
		int index = storePath.lastIndexOf("\\.");
		logger.info("index : " + index);
		int index2 = storePath.lastIndexOf(".");
		if(index == -1){
			index = index2;
			logger.info("index = index2 : " + index2);
		}
		logger.info("index2 : " + index2);
		String path1 = storePath.substring(0, index) + "-" + ImgType.medium;
		String path2 = storePath.substring(index);
    	String destMediumPath = path1 + path2;
    	logger.info(destMediumPath);
    	return destMediumPath;
	}
	
}
