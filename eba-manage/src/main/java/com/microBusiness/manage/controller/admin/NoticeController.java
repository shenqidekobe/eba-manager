package com.microBusiness.manage.controller.admin;

import com.google.zxing.WriterException;
import net.sf.ehcache.search.expression.Not;
import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.NoticeUserService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.CookieUtil;
import com.microBusiness.manage.util.QRCodeUtil;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mingbai on 2017/3/28.
 * 功能描述：消息通知
 * 修改记录：
 */
@Controller(value = "adminNoticeController")
@RequestMapping(value = "/admin/notice")
public class NoticeController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(NoticeController.class);

    @Resource
    private NoticeUserService noticeUserService ;

    @Resource
    private WeChatService weChatService;

    @Value("${qr.noticeUser.check}")
    private String checkPath ;

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request , HttpServletResponse response , Pageable pageable , ModelMap modelMap , String searchName){

        Supplier supplier = super.getCurrentSupplier() ;
        Page<NoticeUser> noticeUser = noticeUserService.findPage(pageable , supplier , searchName);
        modelMap.put("page" , noticeUser) ;
        modelMap.put("types" , NoticeType.Type.values());
        modelMap.put("searchName", searchName);
        return "/admin/notice/list" ;
    }

    public String delete(HttpServletRequest request , HttpServletResponse response){
        return null ;
    }

    /**
     * 弹出二维码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/add")
    public void add(HttpServletRequest request , HttpServletResponse response){
        /*response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");*/
        try {
            Setting setting = SystemUtils.getSetting();
            //生成图像
            int width = 300; // 图像宽度
            int height = 300; // 图像高度
            String format = "jpg";// 图像类型
            String url = setting.getSiteUrl() + String.format(this.checkPath , super.getCurrentSupplier().getId());
            LOG.info("qrcoe url is :{}", url);
            BufferedImage bufferedImage = QRCodeUtil.encode(width, height, format, url);
            OutputStream stream = response.getOutputStream();
            ImageIO.write(bufferedImage, format, stream);
            stream.flush();
            stream.close();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id , NoticeType.Type[] types   ,   HttpServletRequest request , HttpServletResponse response){
        NoticeUser noticeUser = noticeUserService.update(id , types);
        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request , HttpServletResponse response , ModelMap modelMap){
        try {
            String code = null;
            String queryString = request.getQueryString();
            String url = request.getRequestURL().toString();
            url = StringUtils.isEmpty(queryString) ? url : url + "?" + queryString;
            //对url进行URLEncode转换
            String strUrl = URLEncoder.encode(url, "utf-8");
            //得到请求的参数Map，注意map的value是String数组类型
            code = request.getParameter(Constant.MAP_KEY);

            if (null == code || code.equals("")) {
                //获取code值
                String jumpPath = weChatService.getCode(strUrl);
                //CookieUtil.addCookie(response, "wxShareUrl", reqUrl, "/", 60 * 60 );
                //response.sendRedirect(jumpPath);
                return "redirect:" + jumpPath ;
            }

            boolean isOk = true ;

            //获取到code后的正常逻辑
            Map<String, String> userMap = weChatService.getWebAuthAccessToken(code);
           if (null == userMap) {

               isOk = false ;

               modelMap.addAttribute("isOk" , isOk);
               modelMap.addAttribute("message" , "获取微信用户信息失败");
               return "/admin/notice/result";
            }
            //获取到 openId
            String openId = userMap.get("openid");
            String supplierId = request.getParameter("supplierId") ;
            if(StringUtils.isEmpty(openId) || StringUtils.isEmpty(supplierId)){
                LOG.error("openId or supplierId is null");

                isOk = false ;
                modelMap.addAttribute("isOk" , isOk);
                modelMap.addAttribute("message" , "获取微信用户信息失败");
                return "/admin/notice/result";
            }

            //查询用户是否存在
            NoticeUser noticeUser = noticeUserService.findByOpenId(openId);
            //不存在则新增
            if(null != noticeUser){
                LOG.error("have been notice user");

                isOk = false ;
                modelMap.addAttribute("isOk" , isOk);
                modelMap.addAttribute("message" , "您已绑定过该公众号的消息接收员");
                return "/admin/notice/result";

            }

            String globalToken = weChatService.getGlobalToken();
            LOG.debug("globalToken:{}", globalToken);
            WeChatUserInfo userInfo = weChatService.getWeChatUserByUnion(openId, globalToken);
            noticeUser = noticeUserService.save(userInfo , Long.valueOf(supplierId));

            isOk = true ;
            modelMap.addAttribute("isOk" , isOk);
            modelMap.addAttribute("message" , "您已成功绑定该公众号的消息接收员");
            return "/admin/notice/result";

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null ;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        noticeUserService.delete(ids);
        return SUCCESS_MESSAGE;
    }


    @RequestMapping(value = "/getTypes", method = RequestMethod.GET)
    public @ResponseBody
    JsonEntity getTypes(Long id) {
        NoticeUser noticeUser = noticeUserService.find(id);
        List<NoticeType> types = noticeUser.getNoticeTypes();
        List<NoticeType.Type> retTypes = new ArrayList<>();
        for (NoticeType type : types){
            retTypes.add(type.getType()) ;
        }
        return JsonEntity.successMessage(retTypes);
    }

    /**
     * 订货单通知
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/orderNotice", method = RequestMethod.GET)
    public String orderNotice(Long id , ModelMap modelMap) {

        NoticeUser noticeUser = noticeUserService.find(id);

        List<NoticeType> noticeTypes = noticeUser.getNoticeTypes();

        modelMap.put("isSet", CollectionUtils.isEmpty(noticeTypes) ? false : true);

        //已经设置的类型
        modelMap.put("setTypes" , noticeUserService.getOrderNoticeTypes(noticeUser.getNoticeTypes())) ;
        modelMap.put("types" , NoticeType.Type.values());
        //设置的企业
        modelMap.put("suppliers" , noticeUserService.getOrderNoticeSupplier(noticeUser.getNoticeUserSuppliers())) ;

        modelMap.put("noticeUserId" , id) ;

        modelMap.put("needs" , noticeUserService.getOrderNoticeNeeds(noticeUser.getNoticeUserOrderNeeds()));


        return "/admin/notice/news_set" ;
    }

    /**
     * 采购单通知
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/purchaseNotice", method = RequestMethod.GET)
    public String purchaseNotice(Long id , ModelMap modelMap) {

        NoticeUser noticeUser = noticeUserService.find(id);

        List<NoticeTypePurchase> purchaseTypes = noticeUser.getNoticeTypePurchases();

        modelMap.put("isSet", CollectionUtils.isEmpty(purchaseTypes) ? false : true);

        //已经设置的类型
        modelMap.put("setTypes" , noticeUserService.getPurchaseNoticeTypes(noticeUser.getNoticeTypePurchases())) ;

        modelMap.put("types" , NoticeTypePurchase.Type.values());
        //设置的企业
        modelMap.put("needs" , noticeUserService.getPurchaseNoticeNeeds(noticeUser.getNoticeUserNeeds())) ;

        modelMap.put("noticeUserId" , id) ;

        return "/admin/notice/own_news_set" ;
    }

    @RequestMapping(value = "/saveOrderNotice", method = RequestMethod.POST)
    public String saveOrderNotice(Long noticeUserId , Long[] supplierIds , NoticeType.Type[] types , RedirectAttributes redirectAttributes , Long[] needIds) {

        noticeUserService.saveOrderNotice(noticeUserService.find(noticeUserId) , supplierIds , types , needIds) ;
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml" ;
    }


    @RequestMapping(value = "/savePurchaseNotice", method = RequestMethod.POST)
    public String savePurchaseNotice(Long noticeUserId , Long[] needIds , NoticeTypePurchase.Type[] types , RedirectAttributes redirectAttributes) {
        noticeUserService.savePurchaseNotice(noticeUserService.find(noticeUserId) , needIds , types) ;
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml" ;
    }

}
