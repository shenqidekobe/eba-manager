package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.ass.AssCartItemService;
import com.microBusiness.manage.service.ass.AssCartService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssProductService;
import com.microBusiness.manage.service.ass.AssShippingAddressService;
import com.microBusiness.manage.util.Code;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assCartController")
@RequestMapping("/ass/cart")
public class AssCartController extends BaseController {

	@Resource
	private AssProductService assProductService;
	@Resource
	private AssCartService assCartService;
	@Resource
	private AssChildMemberService assChildMemberService;
	@Resource
	private AssCustomerRelationService assCustomerRelationService;
	@Resource
	private AssCartItemService assCartItemService;
	@Resource
	private AssShippingAddressService assShippingAddressService;
	
	/**
	 * 获取购物车列表
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody JsonEntity list(String unionId,  Long assCustomerRelationId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.find(assCustomerRelationId);
		//List<AssShippingAddress> assShippingAddress = assShippingAddressService.findByChildMember(assChildMember);
		//获取地址
		AssShippingAddress assShippingAddress = assShippingAddressService.getListAddress(assChildMember);

		List<Map<String, Object>> acrList = new ArrayList<Map<String, Object>>();
		AssCart cart = null;
		try {
			cart = assChildMember.getAssCart();
			if(cart == null){
				return JsonEntity.successMessage(acrList);
			}

		} catch (Exception e) {
			return JsonEntity.error(Code.code11101, Code.code11101.getDesc());
		}
		Set<AssCartItem> cartItemList = null;
		try {
			cartItemList = cart.getCartItems(assCustomerRelation.getId());
		} catch (Exception e) {
			return JsonEntity.error(Code.code11103, Code.code11103.getDesc());
		}
		Map<String, Object> supplierMap = null;
		Map<String, Object> goodsMap = null;
		
		Map<String, Object> res = new HashMap<>();
		
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

		supplierMap = new HashMap<String, Object>();
		supplierMap.put("supplierId", assCustomerRelation.getId());
		supplierMap.put("supplierName", assCustomerRelation.getClientName());
		supplierMap.put("cartId", cart.getId());
		res.put("supplier", supplierMap);

		for (AssCartItem cartItem : cartItemList) {
			AssProduct product = cartItem.getAssproduct();
			AssGoods goods = product.getAssGoods();
			
			goodsMap = new HashMap<String, Object>();
			goodsMap.put("goodsId", goods.getId());
			goodsMap.put("goodsName", goods.getName());
			goodsMap.put("productId", product.getId());
			goodsMap.put("quantity", cartItem.getQuantity());
			goodsMap.put("cartItemId", cartItem.getId());
			goodsMap.put("img", goods.getImage());
			goodsMap.put("specs", product.getSpecification());
			goodsMap.put("unit", goods.getUnit());

			reList.add(goodsMap);
		}
		res.put("goods", reList);

		if (null == assShippingAddress) {
			res.put("address", null);
		}else {
			Map<String, Object> addressMap = new HashMap<String, Object>();
			addressMap.put("id", assShippingAddress.getId());
			addressMap.put("name", assShippingAddress.getName());
			addressMap.put("tel", assShippingAddress.getTel());
			addressMap.put("addressName", assShippingAddress.getAddressName());
			addressMap.put("address", assShippingAddress.getAddress());
			if (assShippingAddress.getArea() != null) {
				addressMap.put("area", assShippingAddress.getArea().getFullName());
			}
			res.put("address", addressMap);
		}
		
		return JsonEntity.successMessage(res);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity add(String unionId, Long productId, Integer quantity, Long aCRId, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null) {
			return JsonEntity.error(Code.code11102, Code.code11102.getDesc());
		}
		AssProduct product = assProductService.find(productId);
		if (product == null) {
			return JsonEntity.error(Code.code8324, Code.code8324.getDesc());
		}
		// 获取当前用户的购物车
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assCustomerRelationService.find(aCRId);

		AssCart cart = assChildMember.getAssCart();
		// 如果数量为0, 清除该购物车记录
		if (quantity == 0) {
			AssCartItem cartItem = new AssCartItem();
			cartItem.setAssproduct(product);
			cartItem.setCart(cart);
			assCartItemService.deleteByCartItem(cartItem);
			return JsonEntity.successMessage();
		}
		
		try {
			assCartService.add(assChildMember, product, assCustomerRelation.getId(), quantity);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}

	}

	/**
	 * 清空购物车
	 * 
	 * @param unionId
	 * @param aCRId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/empty", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity empty(String unionId, Long aCRId, HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户的购物车
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCart cart = assChildMember.getAssCart();
		if (cart != null) {
			AssCustomerRelation assCustomerRelation = assCustomerRelationService.find(aCRId);
			Set<AssCartItem> assCartItems = cart.getCartItems(assCustomerRelation.getId());
			if (assCartItems.size() > 0) {
				for (AssCartItem assCartItem : assCartItems) {
					assCartItemService.delete(assCartItem);
				}
			}
		}
		
		return JsonEntity.successMessage();
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
	JsonEntity deleteCartItem(Long cartItemId, String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCart cart = null;
		try {
			cart = assChildMember.getAssCart();
		} catch (Exception e) {
			return JsonEntity.error(Code.code11104, Code.code11104.getDesc());
		}
		if (cart == null || cart.isEmpty()) {
			return  JsonEntity.error(Code.code11105, Code.code11105.getDesc());
		}
		AssCartItem cartItem = assCartItemService.find(cartItemId);
		if (!cart.contains(cartItem)) {
			return  JsonEntity.error(Code.code11106, Code.code11106.getDesc());
		}
		try {
			assCartItemService.delete(cartItem);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return  JsonEntity.error(Code.code11107, Code.code11107.getDesc());
		}
	}

}