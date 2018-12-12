package com.microBusiness.manage.controller.api;

import static com.microBusiness.manage.util.Code.code019998;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.ShippingItem;
import com.microBusiness.manage.service.ShippingService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.QRCodeUtil;

/**
 * Created by mingbai on 2017/3/22.
 * 功能描述：配送相关操作
 * 修改记录：
 */
@Controller
@RequestMapping(value = "/api/shipping")
public class ShippingController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShippingController.class) ;

    @Resource
    private ShippingService shippingService ;


    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity get(Long id , HttpServletRequest request , HttpServletResponse response){
        if(null == id){
            return new JsonEntity(code019998);
        }
        Shipping shipping = shippingService.find(id);
        if(null == shipping){
            return new JsonEntity(code019998);
        }
        if(!shipping.getOrder().getMember().equals(super.getUserInfo(request))) {
        	return JsonEntity.error(Code.code_scanning_014001, Code.code_scanning_014001.getDesc());
        }
        if(shipping.getStatus() != Shipping.Status.waitCustomerCheck && shipping.getStatus() != Shipping.Status.senderDenied) {
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

    @RequestMapping(value = "/customerCheck" , method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity customerCheck(Shipping shipping, HttpServletRequest request , HttpServletResponse response){
        if(null == shipping.getId()){
            return new JsonEntity(code019998 , "参数错误") ;
        }

        ChildMember childMember = super.getCurrChildMem(request);

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


    @RequestMapping(value = "/senderCheck" , method = RequestMethod.POST)
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
        try {
            //Setting setting = SystemUtils.getSetting();
            //生成图像
            int width = 300; // 图像宽度
            int height = 300; // 图像高度
            String format = "jpg";// 图像类型

            String url = shippingService.getQrPath(shippingService.find(id));

            LOGGER.info("qrcoe url is :{}", url);
            BufferedImage bufferedImage = QRCodeUtil.encode(width, height, format, url);
            OutputStream stream = response.getOutputStream();
            ImageIO.write(bufferedImage, format, stream);
            stream.flush();
            stream.close();
        } catch (WriterException e) {
            e.printStackTrace();
            LOGGER.error("creat qrcoe error:" , e);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("creat qrcoe error:" , e);
        }
    }

}
