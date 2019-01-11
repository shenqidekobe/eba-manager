package com.microBusiness.manage.controller.api.small;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Filter.Operator;
import com.microBusiness.manage.Order.Direction;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberIncome;
import com.microBusiness.manage.entity.MemberMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.MemberIncomeService;
import com.microBusiness.manage.service.MemberMemberService;
import com.microBusiness.manage.service.OrderService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.WithdrawService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.DateUtils;

/**
 * 会员逻辑接口
 */
@Controller("smallMemberController")
@RequestMapping("/api/small/member")
public class MemberController extends BaseController {
	
    @Resource
    private OrderService orderService;
    @Resource
    private ChildMemberService childMemberService ;
    @Resource
    private MemberMemberService memberMemberService;
    @Resource
    private HostingShopService hostingShopService;
    @Resource
	private AdminService adminService;
    @Resource
	private WeChatService weChatService;
	@Resource(name = "memberIncomeServiceImpl")
	private MemberIncomeService memberIncomeService;
	@Resource(name = "withdrawServiceImpl")
	private WithdrawService withdrawService;
	
	/**
	 * 会员首页
	 * */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity index(String smOpenId, String searchName) {
    	ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
    	Member member = childMember.getMember();
    	if(childMember.getIsShoper()==null||!childMember.getIsShoper()) {
    		return JsonEntity.error(Code.code132,"您还不是店主！");
    	}
    	//计算昨日收益
    	Pageable pageable=new Pageable();
    	Calendar cal=Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, -1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	Date startDate=cal.getTime();
    	Date endDate=cal.getTime();
    	Page<MemberIncome> page=memberIncomeService.findPage(MemberIncome.TYPE_INCOME, childMember, startDate, endDate, pageable);
    	BigDecimal yesterdayIncome=BigDecimal.ZERO;
    	if(!page.getContent().isEmpty()){
    		for(MemberIncome inc:page.getContent()){
    			yesterdayIncome=yesterdayIncome.add(inc.getAmount());
    		}
    	}
    	member.setYesterdayIncome(yesterdayIncome);
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("isChecked", childMember.getIsChecked());
		rootMap.put("isShoper", childMember.getIsShoper());
		rootMap.put("point", member.getPoint());
		rootMap.put("id", member.getId());
		rootMap.put("balance", member.getBalance());
		rootMap.put("income", member.getIncome());
		rootMap.put("yesterdayIncome", member.getYesterdayIncome());
		rootMap.put("nickName", childMember.getNickName());
		rootMap.put("headPic", childMember.getHeadImgUrl());
		return JsonEntity.successMessage(rootMap);
	}

	
	//申请提现
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity withdraw(String smOpenId,String account,
			String accountName,String phone,String way,
			BigDecimal amount,String types) {
    	ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
    	Member member = childMember.getMember();
    	if(amount==null||StringUtils.isEmpty(account)) {
    		return JsonEntity.error(Code.code132,"请填写完整的表单！");
    	}
    	if(member.getIsShoper()==null||!member.getIsShoper()) {
    		return JsonEntity.error(Code.code132,"您还不是店主！");
    	}
    	if(amount.compareTo(BigDecimal.ONE)==-1) {
    		return JsonEntity.error(Code.code132,"提现金额最低不能低于1元！");
    	}
    	if(member.getBalance().compareTo(amount)==-1) {
    		return JsonEntity.error(Code.code123,"提现金额超过了您的余额！");
    	}
    	if(types!=null&&"all".equals(types)){
    		amount=member.getBalance();//全部提现
    	}
    	Withdraw withdraw=new Withdraw();
    	withdraw.setMember(childMember);
    	withdraw.setAccount(account);
    	withdraw.setWay(way);
    	withdraw.setAccountName(accountName);
    	withdraw.setAmount(amount);
    	withdrawService.createWithdraw(withdraw);
    	
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("id", withdraw.getId());
		return JsonEntity.successMessage(rootMap);
	}
    
    //提现详情
    @RequestMapping(value = "/withdraw/detail", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity withdrawDetail(String smOpenId,Long id) {
    	Withdraw obj = withdrawService.find(id);
    	if(obj==null||!obj.getMember().getSmOpenId().equals(smOpenId)) {
    		return JsonEntity.error(Code.code132,"数据不存在！");
    	}
    	
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("obj",obj);
		return JsonEntity.successMessage(rootMap);
	}
    
    /**
   	 * 提现记录列表
   	 */
   	@RequestMapping(value = "/withdraw/list", method = RequestMethod.GET)
   	public @ResponseBody
   	JsonEntity withdrawList(String unionId, String smOpenId, Withdraw_Status status, 
   			Pageable pageable,  Date startDate, Date endDate, String timeSearch, String ts) {
   		Map<String, Object> resultMap = new HashMap<String, Object>();
   		List<Withdraw> list = new ArrayList<Withdraw>();
   		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
   		if(null != startDate) {
   			startDate = DateUtils.specifyDateZero(startDate);
   		}
   		if(null != endDate) {
   			endDate = DateUtils.specifyDatetWentyour(endDate);
   		}
		List<com.microBusiness.manage.Order> orders = new ArrayList<com.microBusiness.manage.Order>();
		com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
		order.setDirection(Direction.desc);
		order.setProperty("createDate");
		orders.add(order);
		pageable.setOrders(orders);
		
   		Page<Withdraw> page = withdrawService.findPage(status, childMember, startDate, endDate, timeSearch, pageable);
   		list=page.getContent();
   		resultMap.put("withdraws", list);
   		resultMap.put("pageNumber", page.getPageNumber());
   		resultMap.put("totalPages", page.getTotalPages());
   		return JsonEntity.successMessage(resultMap);

   	}

    
    /**
	 * 收益记录列表
	 */
	@RequestMapping(value = "/income/list", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity incomeList(String unionId, String smOpenId, 
			Pageable pageable,  Date startDate, Date endDate, String searchName, String ts) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MemberIncome> incomeList = new ArrayList<MemberIncome>();
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		if(null != startDate) {
			startDate = DateUtils.specifyDateZero(startDate);
		}
		if(null != endDate) {
			endDate = DateUtils.specifyDatetWentyour(endDate);
		}
		List<com.microBusiness.manage.Order> orders = new ArrayList<com.microBusiness.manage.Order>();
		com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
		order.setDirection(Direction.desc);
		order.setProperty("createDate");
		orders.add(order);
		pageable.setOrders(orders);
		
		Page<MemberIncome> page = memberIncomeService.findPage(null, childMember, startDate, endDate, pageable);
		incomeList=page.getContent();
		resultMap.put("incomes", incomeList);
		resultMap.put("pageNumber", page.getPageNumber());
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);

	}
	
	
    /**
     * 解除用户的绑定
     * @param request
     * @return
     */
    @RequestMapping(value = "/unBind", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity unBind(String unionId, HttpServletRequest request) {
        ChildMember childMember = childMemberService.findByUnionId(unionId);
        if(null == childMember.getMember()){
            return new JsonEntity("101011" , "您已经解除了绑定");
        }
        childMemberService.unBind(childMember) ;

        return JsonEntity.successMessage() ;
    }

    /**
     * 修改用户名
     * @param request
     * @param childMember
     * @return
     */
    @RequestMapping(value = "/nickName", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity updateNickName(String unionId, String nickName, HttpServletRequest request) {

        if(null == unionId || null == nickName){
            return new JsonEntity(Code.code019998);
        }

        ChildMember currChildMem = childMemberService.findByUnionId(unionId);
        currChildMem.setNickName(nickName);
        childMemberService.updateChildMember(currChildMem , currChildMem) ;

        Map<String , Object> retMap = new HashMap<>() ;

        retMap.put("nickName" , currChildMem.getNickName()) ;
        retMap.put("tel" , CommonUtils.getHiddenTel(currChildMem.getMember().getMobile())) ;

        return JsonEntity.successMessage(retMap) ;
    }
    
    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity get(String unionId, String smOpenId, HttpServletRequest request) {

        //ChildMember currChildMem = childMemberService.findByUnionId(unionId);
    	ChildMember currChildMem = childMemberService.findBySmOpenId(smOpenId);

        Map<String , Object> retMap = new HashMap<>() ;
        
        retMap.put("nickName" , currChildMem.getNickName()) ;
        
        Member member = currChildMem.getMember();
        if(member != null){
        	if(StringUtils.isNotEmpty(currChildMem.getMember().getMobile())){
            	retMap.put("tel" , CommonUtils.getHiddenTel(currChildMem.getMember().getMobile())) ;
            }
            // 判断是否注册企业
            Admin admin=member.getAdmin();
            retMap.put("phone", member.getMobile());
            if (admin!=null) {
            	retMap.put("flag" , true) ;
    		}else {
    			retMap.put("flag" , false) ;
    		}
        }
        
        
        return JsonEntity.successMessage(retMap) ;
    }
    
    /**
     * 订单看板
     * @param unionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/purchaseListKanban" , method = RequestMethod.GET)
    public JsonEntity purchaseListKanban(String unionId) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	List<Integer> status = new ArrayList<Integer>();
    	status.add(Order.Status.pendingReview.ordinal());
    	status.add(Order.Status.pendingShipment.ordinal());
    	status.add(Order.Status.inShipment.ordinal());
    	status.add(Order.Status.shipped.ordinal());
    	status.add(Order.Status.completed.ordinal());
    	status.add(Order.Status.applyCancel.ordinal());
    	status.add(Order.Status.denied.ordinal());
    	status.add(Order.Status.canceled.ordinal());
    	Map<Integer , List<AssListStatisticsDto>> resultMap = orderService.purchaseListKanban(member, status);
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 看板详情
     * @param unionId
     * @param year
     * @param month
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/kanbanDetails" , method = RequestMethod.GET)
    public JsonEntity kanbanDetails(String unionId , int year, int month) {
    	if(null == unionId ){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	Date startDate = DateUtils.specifyMonthStartTime(year, month);
		Date endDate = DateUtils.specifyMonthEndTime(year, month);
		Map<String, Object> resultMap = orderService.kanbandetail(member, startDate, endDate);
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 
     * @Title: clerkList
     * @author: yuezhiwei
     * @date: 2018年3月5日下午2:43:38
     * @Description: 店员管理列表
     * id 当前主账号member的id
     * @return: JsonEntity
     */
    @SuppressWarnings("serial")
	@ResponseBody
    @RequestMapping(value = "/clerkList" , method = RequestMethod.GET)
    public JsonEntity clerkList(String unionId , Pageable pageable) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	Page<MemberMember> page = memberMemberService.findPage(pageable, member, null);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	List<Map<String, Object>> mmbLis = new ArrayList<Map<String,Object>>();
    	for(final MemberMember mb : page.getContent()) {
    		mmbLis.add(new HashMap<String, Object>(){{
    			this.put("memberMemberId", mb.getId());
    			this.put("name", mb.getName());
    			this.put("tel", mb.getByMember().getMobile());
    		}});
    	}
    	resultMap.put("memberMemberList", mmbLis);
    	resultMap.put("totalPages", page.getTotalPages());
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 
     * @Title: saveClerk
     * @author: yuezhiwei
     * @date: 2018年3月5日下午3:03:53
     * @Description: 新增店员
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/saveClerk" , method = RequestMethod.POST)
    public JsonEntity saveClerk(String tel , String name, String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	if(null == tel || "" == tel) {
    		return JsonEntity.error(Code.code_clerk_100001, Code.code_clerk_100001.getDesc());
    	}
    	if(!tel.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
    		return JsonEntity.error(Code.code_clerk_100004, Code.code_clerk_100004.getDesc());
    	}
    	if(tel.equals(member.getUsername())) {
    		return JsonEntity.error(Code.code_clerk_100006, Code.code_clerk_100006.getDesc());
    	}
    	if(null == name || "" == name) {
    		return JsonEntity.error(Code.code_clerk_100002, Code.code_clerk_100002.getDesc());
    	}
    	if(memberMemberService.verifyTelExists(tel, member, null) != null) {
    		return JsonEntity.error(Code.code_clerk_100005, Code.code_clerk_100005.getDesc());
    	}
    	memberMemberService.save(member, tel, name);
    	return JsonEntity.successMessage();
    }
    
    /**
     * 
     * @Title: editClerk
     * @author: yuezhiwei
     * @date: 2018年3月7日上午10:19:09
     * @Description: TODO
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/editClerk" , method = RequestMethod.GET)
    public JsonEntity editClerk(Long id , String unionId) {
    	//Member member = childMemberService.findByUnionId(unionId).getMember();
    	MemberMember memberMember = memberMemberService.find(id);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("id", memberMember.getId());
    	resultMap.put("name", memberMember.getName());
    	resultMap.put("tel", memberMember.getByMember() == null ? null : memberMember.getByMember().getMobile());
    	return JsonEntity.successMessage(resultMap);
    }
    
    /**
     * 
     * @Title: updateClerk
     * @author: yuezhiwei
     * @date: 2018年3月7日上午10:43:12
     * @Description: 修改店员
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/updateClerk" , method = RequestMethod.POST)
    public JsonEntity updateClerk(Long id , String tel , String name, String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	if(null == id) {
    		return JsonEntity.error(Code.code_clerk_100003, Code.code_clerk_100003.getDesc());
    	}
    	if(null == tel || "" == tel) {
    		return JsonEntity.error(Code.code_clerk_100001, Code.code_clerk_100001.getDesc());
    	}
    	if(!tel.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
    		return JsonEntity.error(Code.code_clerk_100004, Code.code_clerk_100004.getDesc());
    	}
    	if(tel.equals(member.getUsername())) {
    		return JsonEntity.error(Code.code_clerk_100006, Code.code_clerk_100006.getDesc());
    	}
    	if(null == name || "" == name) {
    		return JsonEntity.error(Code.code_clerk_100002, Code.code_clerk_100002.getDesc());
    	}
    	MemberMember memberMember = memberMemberService.find(id);
    	if( memberMemberService.verifyTelExists(tel, member, memberMember) !=null) {
    		return JsonEntity.error(Code.code_clerk_100005, Code.code_clerk_100005.getDesc());
    	}
    	
    	memberMember.setName(name);
    	memberMemberService.update(memberMember, tel);
    	return JsonEntity.successMessage();
    }
    
    /**
     * 
     * @Title: deleteClerk
     * @author: yuezhiwei
     * @date: 2018年3月13日下午3:00:42
     * @Description: 删除店员
     * @return: JsonEntity
     */
    @ResponseBody
    @RequestMapping(value = "/deleteClerk" , method = RequestMethod.POST)
    public JsonEntity deleteClerk(Long id , String unionId) {
    	Member member = childMemberService.findByUnionId(unionId).getMember();
    	if(null == id) {
    		return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
    	}
    	MemberMember memberMember = memberMemberService.find(id);
    	boolean bool = hostingShopService.exist(member, memberMember.getByMember());
    	if(bool) {
    		return JsonEntity.error(Code.code_clerk_100007, Code.code_clerk_100007.getDesc());
    	}
    	memberMemberService.delete(memberMember);
    	return JsonEntity.successMessage();
    }
    
    @RequestMapping(value = "/listChildren", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity listChildren(String smOpenId, int level, String searchName) {
    	ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Map<String, Object> rootMap = new HashMap<String, Object>();
		List<Filter> searchfilters = new ArrayList<Filter>();
		Filter filter2 = new Filter();
		filter2.setIgnoreCase(true);
		filter2.setOperator(Operator.eq);
		filter2.setProperty("parent");
		filter2.setValue(childMember.getId());
		searchfilters.add(filter2);
//		List<com.microBusiness.manage.Order> searchOrders = new ArrayList<com.microBusiness.manage.Order>();
//		if(StringUtils.isNotEmpty(searchName)){
//			filter2 = new Filter();
//			filter2.setIgnoreCase(true);
//			filter2.setOperator(Operator.like);
//			filter2.setProperty("nickName");
//			filter2.setValue("%" + searchName + "%");
//			searchfilters.add(filter2);
//		}
//		com.microBusiness.manage.Order order = new com.microBusiness.manage.Order();
//		order.setDirection(Direction.asc);
//		order.setProperty("order");
//		searchOrders.add(order);
		List<ChildMember> plist = childMemberService.findList(0, 10000, searchfilters, null);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(plist != null){
			for (ChildMember cm1 : plist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", cm1.getId());
				map.put("name", cm1.getNickName());
				map.put("image", cm1.getHeadImgUrl());
				
				
				
				searchfilters = new ArrayList<Filter>();
				filter2 = new Filter();
				filter2.setIgnoreCase(true);
				filter2.setOperator(Operator.eq);
				filter2.setProperty("parent");
				filter2.setValue(cm1.getId());
				searchfilters.add(filter2);
				List<ChildMember> p2list = childMemberService.findList(0, 10000, searchfilters, null);
				if(p2list != null){
					if(level == 0){
						map.put("count", p2list.size());
					}
					if(level == 1){
						for (ChildMember cm2 : p2list) {
							Map<String, Object> map2 = new HashMap<String, Object>();
							map2.put("id", cm2.getId());
							map2.put("name", cm2.getNickName());
							map2.put("image", cm2.getHeadImgUrl());
							
							searchfilters = new ArrayList<Filter>();
							filter2 = new Filter();
							filter2.setIgnoreCase(true);
							filter2.setOperator(Operator.eq);
							filter2.setProperty("parent");
							filter2.setValue(cm2.getId());
							searchfilters.add(filter2);
							List<ChildMember> p3list = childMemberService.findList(0, 10000, searchfilters, null);
							if(p3list != null){
								map2.put("count", p3list.size());
							}
							list.add(map2);
						}
						
					}
				}
				if(level == 0){
					list.add(map);
				}
				
				
			}
		}
		rootMap.put("childrenSize", plist.size());
		rootMap.put("list", list);
		return JsonEntity.successMessage(rootMap);
	}
    
    @RequestMapping(value = "/myFarm", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity myFarm(String smOpenId, String searchName, Pageable pageable) {
    	ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);
		Map<String, Object> rootMap = new HashMap<String, Object>();
		List<Filter> searchfilters = new ArrayList<Filter>();
		Filter filter2 = new Filter();
		filter2.setIgnoreCase(true);
		filter2.setOperator(Operator.eq);
		filter2.setProperty("parent");
		filter2.setValue(childMember.getId());
		searchfilters.add(filter2);
		List<ChildMember> plist = childMemberService.findList(0, 10000, searchfilters, null);
		List<ChildMember> p2list = null;
		int p2Size = 0;
		if(plist != null){
			for (ChildMember cm1 : plist) {
				searchfilters = new ArrayList<Filter>();
				filter2 = new Filter();
				filter2.setIgnoreCase(true);
				filter2.setOperator(Operator.eq);
				filter2.setProperty("parent");
				filter2.setValue(cm1.getId());
				searchfilters.add(filter2);
				p2list = childMemberService.findList(0, 10000, searchfilters, null);
				if(p2list != null){
					p2Size += p2list.size();
				}
			}
		}else{
			plist = new ArrayList<ChildMember>();
		}
		if(p2list == null){
			p2list = new ArrayList<ChildMember>();
		}
		rootMap.put("l2", p2Size);
		rootMap.put("l1", plist.size());
		return JsonEntity.successMessage(rootMap);
	}
    
    //我的店铺二维码
    @RequestMapping(value = "/getQRCode", method={RequestMethod.GET})
    public void getQRCode(HttpServletRequest request, HttpServletResponse response,String smOpenId) {
    	response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

        CloseableHttpResponse httpResponse = null;
		try {
			String accessToken = weChatService.getSmallGlobalToken();

			httpResponse = ApiSmallUtils.getInputStream("/pages/index/index?parentOpenId="+smOpenId, accessToken, request, response);

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
