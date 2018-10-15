package com.microBusiness.manage.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.service.CartItemService;
import com.microBusiness.manage.service.CartService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.Code;

@Controller("shopCartController")
@RequestMapping("/api/cart")
public class CartController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;
	@Resource
	private NeedService needService;
	@Resource
	private SupplierService supplierService;
	
	@Resource
	private SupplyNeedService supplyNeedService;
	
	/**
	 * 获取购物车列表
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody JsonEntity list(HttpServletRequest request, HttpServletResponse response , Long supplierId , SupplyType supplyType,Long relationId) {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		Member member = this.getUserInfo(request);
		Supplier sup = supplierService.find(supplierId);
		
		Need need = needService.findNeedByMemberSupplier(sup, member);
		
		List<Map<String, Object>> supplierList = new ArrayList<Map<String, Object>>();
		Cart cart = null;
		try {
			cart = member.getCart();
			if(cart == null){
				return JsonEntity.successMessage(supplierList);
			}
		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		Set<CartItem> cartItemList = null;
		try {
			cartItemList = cart.getCartItems(supplierId , supplyType,relationId);
		} catch (Exception e) {
			return JsonEntity.error(Code.code11103, Code.code11103.getDesc());
		}
		List<Map<String, Object>> goodsList = null;
		Map<String, Object> supplierKeyMap = new HashMap<String, Object>();
		Map<String, Object> goodsKeyMap = new HashMap<String, Object>();
		Map<String, Object> supplierMap = null;
		Map<String, Object> goodsMap = null;
		
		

		for (CartItem cartItem : cartItemList) {
			Product product = cartItem.getProduct();
			Goods goods = product.getGoods();
			Supplier supplier = goods.getSupplier();
			if(supplierKeyMap.get(String.valueOf(supplier.getId())) == null){
				supplierMap = new HashMap<String, Object>();
				supplierMap.put("supplierId", supplier.getId());
				supplierMap.put("supplierName", supplier.getName());
				supplierMap.put("cartId", cart.getId());
				supplierList.add(supplierMap);
			}else{
				supplierMap = (Map)supplierKeyMap.get(String.valueOf(supplier.getId()));
			}

			if(goodsKeyMap.get(String.valueOf(supplier.getId())) == null){
				goodsList = new ArrayList<Map<String, Object>>();
			}else{
				goodsList = (List)goodsKeyMap.get(String.valueOf(supplier.getId()));
			}

			goodsMap = new HashMap<String, Object>();
			goodsMap.put("goodsId", goods.getId());
			goodsMap.put("goodsName", goods.getName());
			goodsMap.put("productId", product.getId());
			goodsMap.put("quantity", cartItem.getQuantity());
			goodsMap.put("cartItemId", cartItem.getId());
			goodsMap.put("img", goods.getImage());
			goodsMap.put("specs", product.getSpecifications());
			
			
			Integer minOrderQuantity = productService.getMinOrderQuantity(product , 
					supplierId , supplyType , need) ;
			goodsMap.put("minOrderQuantity" , minOrderQuantity);

			goodsList.add(goodsMap);

			supplierMap.put("goods", goodsList);
			supplierKeyMap.put(String.valueOf(supplier.getId()), supplierMap);
			goodsKeyMap.put(String.valueOf(supplier.getId()), goodsList);
		}
		return JsonEntity.successMessage(supplierList);
	}

	/*@RequestMapping(value = "/quantity", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity quantity() {
		JsonEntity jsonEntity = new JsonEntity();
		Map<String, Integer> data = new HashMap<String, Integer>();
		Cart cart = cartService.getCurrent();
		data.put("quantity", cart != null ? cart.getProductQuantity() : 0);
		jsonEntity.setData(data);
		return jsonEntity;
		
	}*/

	@RequestMapping(value = "/add")
	public @ResponseBody
	JsonEntity add(Long productId, Integer quantity, Long supplierId  , SupplyType supplyType , HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (quantity == null) {
			return JsonEntity.error(Code.code11102, Code.code11102.getDesc());
		}
		Product product = productService.find(productId);
		if (product == null) {
			return JsonEntity.error(Code.code8324, Code.code8324.getDesc());
		}
		// 获取当前用户的购物车
		Member member = this.getUserInfo(request);
		Supplier supplier = supplierService.find(supplierId);
		Need need = needService.findNeedByMemberSupplier(supplier, member);
		
		SupplyNeed supplyNeedParam = new SupplyNeed();
		supplyNeedParam.setSupplier(supplier);
		supplyNeedParam.setNeed(need);
		SupplyNeed supplyNeed = supplyNeedService.findSupplyNeedOnSupply(supplyNeedParam);
		
		Cart cart = member.getCart();
		//如果数量为0, 清除该购物车记录
		if(quantity == 0){
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setSupplyType(supplyType);
			cartItemService.deleteByCartItem(cartItem);
			return JsonEntity.successMessage();
		}
		if (!product.getIsMarketable()) {
			//return jsonEntity;
		}
		try {
			cartService.add(member, product, quantity , supplierId , supplyType, supplyNeed , null);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}
	}

	
	
	/**
	 * 删除购物车条目逻辑
	 * @param cartItemId 购物车条目ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteCartItem")
	public @ResponseBody
	JsonEntity deleteCartItem(Long cartItemId, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Member member = this.getUserInfo(request);
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
	
	/*
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		if (!cart.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
			return data;
		}
		Product product = cartItem.getProduct();
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			return data;
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		return data;
	}
	

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		if (!cart.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		cartItemService.delete(cartItem);
		cart.getCartItems().remove(cartItem);
		data.put("quantity", cart.getProductQuantity());
		return data;
	}
	
	

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return SUCCESS_MESSAGE;
	}
	
	
	public Member convertMember(Member member){
		Member wxMember = new Member();
		wxMember.setId(member.getId());
		wxMember.setUsername(member.getUsername());
		return wxMember;
	}*/

}