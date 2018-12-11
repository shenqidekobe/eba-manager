package com.microBusiness.manage.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.CaptchaService;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.MessageService;
import com.microBusiness.manage.service.OrderNewsPushService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.QRCodeUtil;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.google.zxing.WriterException;

@Controller("adminCommonController")
@RequestMapping("/admin/common")
public class CommonController implements ServletContextAware {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${system.name}")
	private String systemName;
	@Value("${system.version}")
	private String systemVersion;
	@Value("${system.description}")
	private String systemDescription;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource
	private WeChatService weChatService;
	@Resource
	private AdminService adminService ;

	private ServletContext servletContext;
	@Resource
	private OrderNewsPushService orderNewsPushService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpServletRequest request, ModelMap model) {
//		Admin admin = adminService.getCurrent();
//		if(admin != null){
//			request.getSession().setAttribute("adminSession", admin);
//		}else{
//			request.getSession().removeAttribute("adminSession");
//		}

		/*//如果是手机端，则跳转到 ／homePage/index.jhtml
		if(DeviceUtils.getCurrentDevice(request).isMobile()){
			return "redirect:/admin/homePage/index.jhtml";
		}*/

		String redirectUrl = (String)request.getSession().getAttribute("redirectUrl");
		if(StringUtils.isNotBlank(redirectUrl) ){
			return "redirect:" + redirectUrl;
		}
		Supplier supplier = adminService.getCurrent().getSupplier();
		model.addAttribute("supplier" , supplier);
		Admin admin = adminService.getCurrent();
		if(supplier != null) {
			//认证状态
			model.addAttribute("status", supplier.isProbation());
			//试用结束日期
			model.addAttribute("endDate", supplier.getProbationEndDay());
			//是否过期
			model.addAttribute("whetherExpired", supplier.isExpired());
		}
		model.addAttribute("bootPage", admin.getBootPage());
		model.addAttribute("prompts", admin.getPrompts());
		//Pageable pageable = new Pageable();
		//model.addAttribute("pageByOrder", orderNewsPushService.findPageOrder(supplier,OrderNewsPush.Mark.purchaseOrder,OrderNewsPush.Status.unread, pageable));
		//model.addAttribute("pageByPurchase", orderNewsPushService.findPageByPurchaseOrder(supplier,OrderNewsPush.Mark.order,OrderNewsPush.PurchaseViewStatus.unread, pageable));
		return "/admin/common/main";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model) {
		model.addAttribute("systemName", systemName);
		model.addAttribute("systemVersion", systemVersion);
		model.addAttribute("systemDescription", systemDescription);
		model.addAttribute("javaVersion", System.getProperty("java.version"));
		model.addAttribute("javaHome", System.getProperty("java.home"));
		model.addAttribute("osName", System.getProperty("os.name"));
		model.addAttribute("osArch", System.getProperty("os.arch"));
		model.addAttribute("serverInfo", servletContext.getServerInfo());
		model.addAttribute("servletVersion", servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
		model.addAttribute("pendingReviewOrderCount", orderService.count(null, Order.Status.pendingReview, null, null, null, null, null, null, null, null));
		model.addAttribute("pendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, null, null, null, null, null, null, null, null));
		model.addAttribute("pendingReceiveOrderCount", orderService.count(null, null, null, null, true, null, null, null, null, null));
		model.addAttribute("pendingRefundsOrderCount", orderService.count(null, null, null, null, null, true, null, null, null, null));
		model.addAttribute("marketableProductCount", goodsService.count(null, null, true, null, null, null, null));
		model.addAttribute("notMarketableProductCount", goodsService.count(null, null, false, null, null, null, null));
		model.addAttribute("stockAlertProductCount", goodsService.count(null, null, null, null, null, null, true));
		model.addAttribute("outOfStockProductCount", goodsService.count(null, null, null, null, null, true, null));
		model.addAttribute("memberCount", memberService.count());
		model.addAttribute("unreadMessageCount", messageService.count(null, false));

		model.addAttribute("supplier" , adminService.getCurrent().getSupplier());
		return "/admin/common/index";
	}

	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> area(Long parentId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Area parent = areaService.find(parentId);
		Collection<Area> areas = parent != null ? parent.getChildren() : areaService.findRoots();
		for (Area area : areas) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", area.getName());
			item.put("value", area.getId());
			data.add(item);
		}
		return data;
	}
	
	@RequestMapping(value = "/getTreePath", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTreePath(Long id) {
		Area area= areaService.find(id);
		Map<String, Object> map=new HashMap<>();
		map.put("treePath", area.getTreePath());
		map.put("fullName", area.getFullName());
		return map;
	}

	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void captcha(String captchaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(captchaId)) {
			captchaId = request.getSession().getId();
		}
		String pragma = new StringBuilder().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String value = new StringBuilder().append("utlz.cn").toString();
		response.addHeader(pragma, value);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		OutputStream outputStream = response.getOutputStream();
		BufferedImage bufferedImage = captchaService.buildImage(captchaId);
		ImageIO.write(bufferedImage, "jpg", outputStream);
		outputStream.flush();
	}

	@RequestMapping("/error")
	public String error() {
		return "/admin/common/error";
	}

	@RequestMapping("/unauthorized")
	public String unauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			response.addHeader("loginStatus", "unauthorized");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return "/admin/common/unauthorized";
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request, HttpServletResponse response) throws IOException {

		return "/admin/login/test";
	}

	@RequestMapping(value = "/getQRCode", method={RequestMethod.GET, RequestMethod.POST})
	public void getQRCode(HttpServletRequest request, HttpServletResponse response,Long supplierId , SupplyType supplyType) {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		try {
			Setting setting = SystemUtils.getSetting();
			//生成图像
			int width = 300; // 图像宽度
			int height = 300; // 图像高度
			String format = "jpg";// 图像类型
			String url = getUrl(supplyType , supplierId);
			logger.info("qrcoe url is :{}", url);

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

	@RequestMapping(value = "/downQRCode", method = {RequestMethod.GET, RequestMethod.POST})
	public void downQRCode(HttpServletRequest request, HttpServletResponse response, Long supplierId , SupplyType supplyType) {
		try {
			Supplier curr = adminService.getCurrent().getSupplier() ;
			//生成图像
			int width = 300; // 图像宽度
			int height = 300; // 图像高度
			String format = "jpg";// 图像类型
			String fileName = null == curr ? "商城二维码" + this.getQrCodePicName(supplyType) + "." + format : curr.getName() + this.getQrCodePicName(supplyType) + "." +format;
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
			String url = getUrl(supplyType , supplierId);
			logger.info("qrcoe url is :{}", url);
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

	private String getUrl(SupplyType supplyType , Long supplierId){
		Setting setting = SystemUtils.getSetting();
		String url = setting.getSiteUrl() + Constant.LOGIN_URL_PRE + String.format(Constant.BACK_URL_QRCODE, supplierId) + "%26supplyType=" + supplyType.name();
		return url ;
	}

	private String getQrCodePicName(SupplyType supplyType){
		String picName = "临时供应二维码";

		switch (supplyType) {
			case formal:
				picName = "正式供应二维码" ;
				;
				break;
			case temporary:
				picName = "临时供应二维码"  ;
				;
				break;
			default:
				;
				break;
		}
		return picName ;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/operationTips", method = RequestMethod.POST)
	public Map<String, Object> operationTips(Admin.Prompts prompts) {
		Map<String, Object> map = new HashMap<String, Object>();
		Admin admin = adminService.getCurrent();
		adminService.operationTips(prompts, admin);
		map.put("exist", true);
		return map;
	}

	@RequestMapping(value = "/generateTwoCode", method={RequestMethod.GET, RequestMethod.POST})
    public void generateTwoCode(Long supplierId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

        CloseableHttpResponse httpResponse = null;
		try {
			String accessToken = weChatService.getSmallGlobalToken();

			httpResponse = ApiSmallUtils.getInputStream("pages/welcome/welcome?supplierId="+supplierId, accessToken, request, response);

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

	@RequestMapping(value = "/downTwoCode", method = {RequestMethod.GET, RequestMethod.POST})
	public void downTwoCode(HttpServletRequest request, HttpServletResponse response, Long supplierId) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		
		CloseableHttpResponse httpResponse = null;
		try {
		   String fileName = "xiaochengxu.jpg";
		   fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
		   
		   response.setContentType("application/msexcel;charset=utf-8");
		   Enumeration enumeration = request.getHeaders("User-Agent");
		   String browserName = (String) enumeration.nextElement();
		   boolean isMSIE = browserName.contains("MSIE");
		   if (isMSIE) {
			  response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF8"));
		   } else {
			  response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
		   }
		   String accessToken = weChatService.getSmallGlobalToken();

		   httpResponse = ApiSmallUtils.getInputStream("pages/welcome/welcome?supplierId="+supplierId, accessToken, request, response);

		   HttpEntity httpEntity = httpResponse.getEntity();
		   if (httpEntity != null) {
			 inputStream = httpEntity.getContent();
		   }
		   
		   //图片下载
		   outputStream = response.getOutputStream();
		   IOUtils.copy(inputStream, outputStream);
		   httpResponse.close();
		} catch (IOException e) {
		   System.err.println(e);
		}catch (Exception e) {
		   System.err.println(e);
		}finally {
		   IOUtils.closeQuietly(inputStream); 
		   IOUtils.closeQuietly(outputStream); 
		   try {
				httpResponse.close();
			} catch (IOException e) {
			}
		} 
	}


	/**
	 * 成功登陆，进行来源判断
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deviceOauth", method = RequestMethod.GET)
	public String deviceAuth(HttpServletRequest request, ModelMap model) {
		Device device = DeviceUtils.getCurrentDevice(request) ;

		if(device.isMobile()){
			return "/admin/common/index" ;
		}else{
			return "redirect:main.jhtml";
		}

	}
	
	/**
	 * 
	 * @Title: noAuthority
	 * @author: yuezhiwei
	 * @date: 2018年2月1日下午5:24:17
	 * @Description: 手机版后台没有权限跳转页面
	 * @return: String
	 */
	@RequestMapping(value = "/noAuthority", method = RequestMethod.GET)
	public String noAuthority(ModelMap model) {
		Supplier supplier = adminService.getCurrentSupplier();
		model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
		return "/admin/common/noAuthority";
	}



}