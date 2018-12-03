package com.microBusiness.manage.controller.api.small;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.CartItem.CartType;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedShopProduct;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SupplierNeedProduct;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplierType;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.CartItemService;
import com.microBusiness.manage.service.CartService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.NeedShopProductService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.ShopService;
import com.microBusiness.manage.service.SupplierNeedProductService;
import com.microBusiness.manage.util.Code;

@Controller("smallCartController")
@RequestMapping("/api/small/cart")
public class CartController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;
	@Resource
	private ChildMemberService childMemberService ;
	@Resource
	private OrderService orderService;
	@Resource
	private ShopService shopService;
	@Resource
	private NeedShopProductService needShopProductService;
	@Resource
	private SupplierNeedProductService supplierNeedProductService;

	/**
	 * 获取购物车列表
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody JsonEntity list(String unionId , String smOpenId, 
				Long supplierId,Long relationId,Long shopId,Types types,SupplierType supplierType) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		List<Map<String, Object>> resultList = new ArrayList<>();
		//Shop shop=shopService.find(shopId);
		if(member == null){
			return JsonEntity.successMessage(resultList);
		}

		
		Cart cart;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.successMessage(resultList);
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		Set<CartItem> cartItemList;
		try {
			cartItemList = cartItemService.getCartItems(cart, null, supplierId, relationId, 
					supplierType, types);
		} catch (Exception e) {
			return JsonEntity.error(Code.code11103, Code.code11103.getDesc());
		}

		for (CartItem cartItem : cartItemList) {
			//不读取代理的购物车
//			if(cartItem.getCartType().equals(CartType.proxy)){
//				continue;
//			}
			Map<String,Object> map=new HashMap<>();
			Product product=cartItem.getProduct();
			//Integer minOrderQuantity=-1;
			if (cartItem.getValid()) {
				//minOrderQuantity=productService.getMinOrderQuantity(null, supplierId,supplierType,relationId,product);
				//if (minOrderQuantity == -1){
				if(!product.getIsMarketable()){
					//商品下架
					cartItem.setValid(false);
					cartItemService.refreshCartItem(cartItem);
				}else{
					cartItem.setValid(true);
					cartItemService.refreshCartItem(cartItem);
				}
					
				//}
			}
			map.put("valid",cartItem.getValid());
			map.put("goodsId",product.getGoods().getId());
			map.put("cartItemId",cartItem.getId());
			map.put("cartId",cart.getId());
			map.put("productId",product.getId());
			map.put("name",product.getGoods().getName());
			map.put("image",product.getGoods().getImage());
			map.put("specifications",product.getSpecifications());
			map.put("quantity",cartItem.getQuantity());
			map.put("minOrderQuantity",product.getMinOrderQuantity());
			map.put("addValue",product.getAddValue());
			map.put("price",product.getPrice());
			map.put("cartType",cartItem.getCartType());
			Map<String,Object> carttool=new HashMap<>();
			carttool.put("pri", product.getPrice());
			carttool.put("sum", product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
			carttool.put("num", cartItem.getQuantity());
			carttool.put("status", "disabled");
			map.put("carttool",carttool);
			resultList.add(map);
		}

		return JsonEntity.successMessage(resultList);
	}

	@RequestMapping(value = "/add")
	public @ResponseBody
	JsonEntity add(String unionId, String smOpenId, Long shopId, 
			Types types, Long productId, Integer quantity, Long supplierId ,SupplyType supplyType , 
			Long relationId, SupplierType supplierType, CartType cartType) {
		//Shop shop=shopService.find(shopId);
		if (quantity == null) {
			return JsonEntity.error(Code.code11102, Code.code11102.getDesc());
		}
		Product product = productService.find(productId);
		if (product == null) {
			return JsonEntity.error(Code.code8324, Code.code8324.getDesc());
		}
		// 获取当前用户的购物车
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		try {
			Cart cart = member.getCart();
			if(cart != null){
				Set<CartItem> cartItems = cart.getCartItems();
				for (CartItem cartItem : cartItems) {
					if(!cartItem.getCartType().equals(cartType)){
						if(cartType == null || cartType.equals(CartType.personal)){
							return JsonEntity.error(Code.code111120, Code.code111120.getDesc());
						}else{
							return JsonEntity.error(Code.code111119, Code.code111119.getDesc());
						}
						
					}
				}
			}
			cartService.add(member, null, types, product, quantity, supplierId, supplyType,
					relationId, supplierType, cartType);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}
	}

	/**
	 * 删除购物车条目逻辑
	 * @param cartItemId 购物车条目ID
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteCartItem")
	public @ResponseBody
	JsonEntity deleteCartItem(String unionId, String smOpenId, Long cartItemId, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		Cart cart = null;
		try {
			cart = member.getCart();
		} catch (Exception e) {
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}
		if (cart == null || cart.isEmpty()) {
			return  JsonEntity.error(Code.code11105, Code.code11105.getDesc());
		}
		CartItem cartItem = cartItemService.find(cartItemId);
		if (!cart.contains(cartItem)) {
			return  JsonEntity.error(Code.code11106, Code.code11106.getDesc());
		}
		try {
			cartItemService.delete(cartItem);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return  JsonEntity.error(Code.code11107, Code.code11107.getDesc());
		}
	}
	
	/**
	 * 清空购物车
	 */
	@RequestMapping(value = "/clear")
	public @ResponseBody
	JsonEntity clear(String unionId, String smOpenId, Long cartItemId, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
		Cart cart = null;
		try {
			cart = member.getCart();
		} catch (Exception e) {
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}
		if (cart == null || cart.isEmpty()) {
			return  JsonEntity.error(Code.code11105, Code.code11105.getDesc());
		}
		try {
			cartService.clearCart(cart);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return  JsonEntity.error(Code.code11107, Code.code11107.getDesc());
		}
	}
	
	
	/**
     * 再来一单
     * @param unionId
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comeOnAgain" , method = RequestMethod.POST)
    public JsonEntity comeOnAgain(String unionId, String smOpenId, Long orderId) {
    	if(null == unionId || null ==orderId){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	//Member member = childMemberService.findByUnionId(unionId).getMember();
    	Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
    	Order order = orderService.find(orderId);
    	Need need=order.getNeed();
    	Long relationId=null;
		if (order.getShop() == null || order.getSupplierType() == null){
			return new JsonEntity(Code.code11113, Code.code11113.getDesc());
		}
    	try {
        	List<OrderItem> orderItems = order.getOrderItems();
        	Types types;
        	if (order.getType() != Order.Type.local){
				types=Types.platform;
			}else {
				types=Types.local;
			}
        	if (SupplierType.ONE == order.getSupplierType() || SupplierType.THREE== order.getSupplierType()) {
				SupplyNeed supplyNeed = order.getSupplyNeed();
				relationId=supplyNeed.getId();
				List<NeedShopProduct> needShopProducts=needShopProductService.getList(supplyNeed,order.getShop(),null);
				if (needShopProducts == null || needShopProducts.size() == 0){
					return new JsonEntity(Code.code11111, Code.code11111.getDesc());
				}
				if(supplyNeed.getStatus() == SupplyNeed.Status.SUPPLY) {
					cartService.addMore(member, orderItems, order.getSupplier().getId(), order.getSupplyType(), supplyNeed.getId(),order.getSupplierType(),types,order.getShop());
				}else {
					return new JsonEntity(Code.code11112, Code.code11112.getDesc());
				}
			}else if(SupplierType.TWO == order.getSupplierType()){
				if (Need.NeedStatus.suspend.equals(need.getNeedStatus())) {
					return new JsonEntity(Code.code17803, Code.code17803.getDesc());
				}
				SupplierSupplier supplierSupplier = order.getSupplierSupplier();
				relationId=supplierSupplier.getId();
				if(supplierSupplier.getStatus() != SupplierSupplier.Status.inTheSupply) {
					return new JsonEntity(Code.code11112, Code.code11112.getDesc());
				}
				Date now=new Date();
				if (supplierSupplier.getStartDate().getTime() >now.getTime() || now.getTime() > supplierSupplier.getEndDate().getTime() ) {
					return new JsonEntity(Code.code11114, Code.code11114.getDesc());
				}
				List<SupplierNeedProduct> supplierNeedProducts=supplierNeedProductService.findBySupplier(supplierSupplier,need);
				if (supplierNeedProducts == null || supplierNeedProducts.size() == 0){
					return new JsonEntity(Code.code11111, Code.code11111.getDesc());
				}
				cartService.addMore(member, orderItems, order.getSupplier().getId(), order.getSupplyType(), supplierSupplier.getId(),order.getSupplierType(),types,order.getShop());
			}else if (SupplierType.FOUR == order.getSupplierType()){
				List<NeedShopProduct> needShopProducts=needShopProductService.getList(null,order.getShop(),order.getSupplier());
				if (needShopProducts == null || needShopProducts.size() == 0){
					return new JsonEntity(Code.code11111, Code.code11111.getDesc());
				}
				cartService.addMore(member, orderItems, order.getSupplier().getId(), order.getSupplyType(), null,order.getSupplierType(),types,order.getShop());
			}
		} catch (Exception e) {
			return new JsonEntity(Code.code11110, Code.code11110.getDesc());
		}

		Map<String,Object> map=new HashMap<>();
    	map.put("supplierId",order.getSupplier().getId());
		map.put("shopId",order.getShop().getId());
		map.put("supplierType",order.getSupplierType());
		map.put("relationId",relationId);
		if (Order.Type.local == order.getType()){
			map.put("types",Types.local);
		}else {
			map.put("types",Types.platform);
		}
		map.put("supplyType",order.getSupplyType());

    	return JsonEntity.successMessage(map);
    }

}