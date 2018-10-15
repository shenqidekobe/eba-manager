package com.microBusiness.manage.controller.api.small;

import com.google.zxing.WriterException;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.QRCodeUtil;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microBusiness.manage.util.Code.code019998;

/**
 * Created by mingbai on 2017/3/22.
 * 功能描述：配送相关操作
 * 修改记录：
 */
@Controller("smallShippingController")
@RequestMapping(value = "/api/small/shipping")
public class ShippingController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShippingController.class) ;

    @Resource
    private ShippingService shippingService ;

    @Resource
	private ChildMemberService childMemberService ;
    @Resource
	private WeChatService weChatService;

    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity get(String unionId, String smOpenId, Long id , 
    			HttpServletRequest request , HttpServletResponse response){
        if(null == id){
            return new JsonEntity(code019998);
        }
        Shipping shipping = shippingService.find(id);
        if(null == shipping){
            return new JsonEntity(code019998);
        }
        //Member member=childMemberService.findByUnionId(unionId).getMember();
        Member member = childMemberService.findBySmOpenId(smOpenId).getMember();
        if(!shipping.getOrder().getMember().equals(member) && !member.isHostingShop(shipping.getOrder().getShop())) {
        	return JsonEntity.error(Code.code_scanning_014001, Code.code_scanning_014001.getDesc());
        }
        if(shipping.getStatus() != Shipping.Status.waitCustomerCheck 
        		&& shipping.getStatus() != Shipping.Status.senderDenied) {
        	return new JsonEntity(Code.code_logistics_14002);
        }

        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();

        for (ShippingItem shippingItem : shipping.getShippingItems()) {
            Map<String, Object> shippingItemMap = new HashMap<String, Object>();
            shippingItemMap.put("goodsId", shippingItem.getProduct().getGoods().getId());
            shippingItemMap.put("productId", shippingItem.getProduct().getId());
            shippingItemMap.put("productName", shippingItem.getName());
            shippingItemMap.put("img", shippingItem.getProduct().getImage());
            shippingItemMap.put("quantity", shippingItem.getQuantity());
            shippingItemMap.put("specs", shippingItem.getSpecifications());
            shippingItemMap.put("realQuantity", shippingItem.getRealQuantity());

            shippingItemMap.put("itemId", shippingItem.getId());
            itemsList.add(shippingItemMap);
        }

        Map<String , Object> returnMap = new HashMap<>();

        returnMap.put("shippingId" , shipping.getId()) ;
        returnMap.put("status" , shipping.getStatus()) ;
        returnMap.put("items" , itemsList);
        returnMap.put("orderId", shipping.getOrder().getId());

        return JsonEntity.successMessage(returnMap) ;

    }

    @RequestMapping(value = "/customerCheck" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity customerCheck(String unionId, String smOpenId, Shipping shipping, HttpServletRequest request , HttpServletResponse response){
        if(null == shipping.getId()){
            return new JsonEntity(code019998 , "参数错误") ;
        }

        //ChildMember childMember = childMemberService.findByUnionId(unionId);
        ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
        shippingService.customerCheck(shipping , childMember);

        return JsonEntity.successMessage();
    }

    @Deprecated
    @RequestMapping(value = "/senderGet")
    @ResponseBody
    public JsonEntity senderGet(Long id, HttpServletRequest request , HttpServletResponse response){

        if(null == id){
            return new JsonEntity(code019998);
        }
        Shipping shipping = shippingService.find(id);
        if(null == shipping){
            return new JsonEntity(code019998);
        }

        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();

        for (ShippingItem shippingItem : shipping.getShippingItems()) {
            Map<String, Object> shippingItemMap = new HashMap<String, Object>();
            shippingItemMap.put("goodsId", shippingItem.getProduct().getGoods().getId());
            shippingItemMap.put("productId", shippingItem.getProduct().getId());
            shippingItemMap.put("productName", shippingItem.getName());
            shippingItemMap.put("img", shippingItem.getProduct().getImage());
            shippingItemMap.put("quantity", shippingItem.getQuantity());
            shippingItemMap.put("specs", shippingItem.getSpecifications());
            shippingItemMap.put("realQuantity", shippingItem.getRealQuantity());

            shippingItemMap.put("itemId", shippingItem.getId());
            itemsList.add(shippingItemMap);
        }

        Map<String , Object> returnMap = new HashMap<>();

        returnMap.put("shippingId" , shipping.getId()) ;
        returnMap.put("status" , shipping.getStatus()) ;

        returnMap.put("items" , itemsList);

        return JsonEntity.successMessage(returnMap) ;

    }

    @RequestMapping(value = "/senderCheck" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity senderCheck(Long id , Boolean passed , String code ,  HttpServletRequest request , HttpServletResponse response){

        if(null == id){
            return new JsonEntity(code019998);
        }

        shippingService.senderCheck(id , passed , code);

        return JsonEntity.successMessage() ;

    }

    @RequestMapping(value = "/getQRCode", method={RequestMethod.GET})
    public void getQRCode(HttpServletRequest request, HttpServletResponse response,Long id) {
    	response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

        CloseableHttpResponse httpResponse = null;
		try {
			String accessToken = weChatService.getSmallGlobalToken();

			httpResponse = ApiSmallUtils.getInputStream("pages/driverconfirm/driverconfirm?id="+id, accessToken, request, response);

			InputStream inputStream = null;
			
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				inputStream = httpEntity.getContent();
			}
			
			OutputStream stream = response.getOutputStream();
			ImageIO.write(ImageIO.read(inputStream), "jpg", stream);
			stream.flush();
			stream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			try {
				httpResponse.close();
			} catch (IOException e) {
			}
		}
    }

}
