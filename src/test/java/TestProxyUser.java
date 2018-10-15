import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Gender;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.PaymentMethod;
import com.microBusiness.manage.entity.ProxyCheck;
import com.microBusiness.manage.entity.ProxyCheck.Level;
import com.microBusiness.manage.entity.ProxyCheck.SourceType;
import com.microBusiness.manage.entity.ProxyCheckStatus;
import com.microBusiness.manage.entity.ProxyJoinType;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.ShippingMethod;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.PaymentMethodService;
import com.microBusiness.manage.service.ProxyCheckService;
import com.microBusiness.manage.service.ProxyUserService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.ShippingMethodService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.impl.WeChatServiceImpl;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

public class TestProxyUser extends BaseTest{

	@Resource
	private ProxyUserService proxyUserService;
	
	@Resource
	private ProxyCheckService proxyCheckService;
	@Resource
	private ChildMemberService childMemberService;
	
	@Resource
	private AreaService areaServcie;
	@Resource
	private OrderService orderService;
	@Resource
	private ShippingMethodService shippingMethodService;
	@Resource
	private PaymentMethodService paymentMethodService;
	@Resource
	private ReceiverService receiverService;
	@Resource
	private WeChatService weChatService;

	
	@Test
	@Transactional
	public void testProxyCheckSave(){
		ProxyCheck proxyCheck = new ProxyCheck();
		proxyCheck.setName("小王");
		proxyCheck.setTel("13250801861");
		ChildMember childMember = childMemberService.find(1l);
		proxyCheck.setChildMember(childMember);
		proxyCheck.setAddress("长宁区虹桥路");
		Area area = areaServcie.find(1l);
		proxyCheck.setArea(area);
		proxyCheck.setCreateDate(new Date());
		proxyCheck.setDeleted(false);
		proxyCheck.setGender(Gender.male);
		proxyCheck.setIdenNo("340803198402162879");
		proxyCheck.setLevel(Level.三级代理);
		proxyCheck.setMonthMoney("5万");
		proxyCheck.setNowManageCategory("欧诗漫");
		proxyCheck.setOrder(1);
		//ProxyUser proxyUser = proxyUserService.find(1l);
		proxyCheck.setParentProxyUser(null);
		proxyCheck.setProxyCheckStatus(ProxyCheckStatus.wait);
		proxyCheck.setProxyJoinType(ProxyJoinType.SELF_DIRECT);
		proxyCheck.setReason("觉得你们品牌很不错！");
		proxyCheck.setVersion(1l);
		proxyCheck.setWebchat("243542913");
		proxyCheck.setWorkTime("1年");
		proxyCheck.setSourceType(SourceType.朋友推荐);
		proxyCheckService.save(proxyCheck);
	}
	
	@Test
	@Transactional
	public void testProxyCheckCheck(){
		ProxyCheck proxyCheck = proxyCheckService.find(1l);
		proxyCheck.setProxyCheckStatus(ProxyCheckStatus.finish);
		proxyCheckService.update(proxyCheck);
		
		ProxyUser proxyUser = new ProxyUser();
		proxyUser.setParent(proxyCheck.getParentProxyUser());
		proxyUser.setName(proxyCheck.getName());
		proxyUser.setAddress(proxyCheck.getAddress());
		proxyUser.setArea(proxyCheck.getArea());
		proxyUser.setChildMember(proxyCheck.getChildMember());
		proxyUser.setCreateDate(new Date());
		proxyUser.setGender(proxyCheck.getGender());
		proxyUser.setDeleted(false);
		proxyUser.setGrade(proxyCheck.getParentProxyUser().getGrade()+1);
		proxyUser.setIdenNo(proxyCheck.getIdenNo());
		proxyUser.setProxyJoinType(proxyCheck.getProxyJoinType());
		proxyUser.setSupplier(proxyCheck.getParentProxyUser().getSupplier());
		proxyUser.setTel(proxyCheck.getTel());
		proxyUser.setWebchat(proxyCheck.getWebchat());
		proxyUserService.save(proxyUser);
	}
	
	
	
	
	@Test
	@Transactional
	public void testOrder(){
		Map<String, Object> data = new HashMap<String, Object>();
		ChildMember childMember = childMemberService.findBySmOpenId("oi5qo5XuAmNntw9XEUwVDLYZ7Bp8");
		Long receiverId = 1l;
		Receiver receiver = receiverService.find(receiverId);
		//ChildMember childMember = childMemberService.findByUnionId(unionId);
		ShippingMethod shippingMethod = shippingMethodService.find(1l);
		PaymentMethod paymentMethod = paymentMethodService.find(3l);
		Date date = DateUtils.formatStringToDate("2018-06-16", DateformatEnum.yyyyMMdd2);
		//ChildMember childMember = super.getCurrChildMem(request);
		Order order = orderService.create(44l,  4,
				  paymentMethod,  shippingMethod,  null,
				  null,  null,  "测试",  date , 
				  1l ,  SupplyType.temporary ,  childMember,  receiver);
		data.put("sn", order.getSn());
	}
	
	@Test
	@Transactional
	public void testSendTemplateMsg(){
		String token = new WeChatServiceImpl().getGlobalToken();
    	String templateId = "1ag_JKXjqi6ch6PrzJhn6_UUdopgpX5sYqdIsgEYOFw";
    	Order order = orderService.find(1l);
    	weChatService.sendTemplateMessage2ParentChildMember(order, templateId, token);
    	
	}
	
	
}
