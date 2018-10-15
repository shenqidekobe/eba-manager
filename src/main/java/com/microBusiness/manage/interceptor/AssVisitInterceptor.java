package com.microBusiness.manage.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;
import com.microBusiness.manage.entity.ass.AssPageVisit;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssGoodsVisitService;
import com.microBusiness.manage.service.ass.AssPageVisitService;

public class AssVisitInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private AssPageVisitService assPageVisitService;
	@Resource
	private AssGoodsVisitService assGoodsVisitService;
	@Resource
    private AssChildMemberService assChildMemberService ;
	@Resource
	private AssCustomerRelationService assRelationService;
	@Resource
	private AssGoodsService assGoodsService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String ip=getIpAddress(request);
			String unionId=request.getParameter("unionId");
			AssChildMember assChildMember = assChildMemberService.findByUnionId(unionId);
	    	if (assChildMember != null) {
	    		//目录浏览记录
	    		if (request.getRequestURI().equals("/ass/customerRelation/getShare.jhtml")) {
	    			String pageNumber=request.getParameter("pageNumber");
	    			String sn=request.getParameter("sn");
	    			//如果不是第一页就跳过
	    			if (pageNumber!=null && !pageNumber.equals("1")) {
	    				return true;
					}
	    			AssCustomerRelation assCustomerRelation=assRelationService.findBySn(sn);
	    			//如果是本人就不记录
	    			if (assCustomerRelation.getAssChildMember().equals(assChildMember)) {
	    				return true;
	    			}
	    			//如果不是分享的记录就跳过
	    			if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
	    				return true;
					}
	    			AssPageVisit assPageVisit=new AssPageVisit();
	    			assPageVisit.setName(assChildMember.getNickName());
	    			assPageVisit.setAssChildMember(assChildMember);
	    			assPageVisit.setAssCustomerRelation(assCustomerRelation);
	    			assPageVisit.setIp(ip);
	    			assPageVisitService.save(assPageVisit);
	    		}
	    		//商品浏览记录和页面访问记录
	    		if (request.getRequestURI().equals("/ass/goods/getShare.jhtml")) {
	    			String sn=request.getParameter("sn");
	    			AssGoods assGoods=assGoodsService.findBySn(sn);
	    			//如果是本人就不记录
	    			if (assGoods.getAssCustomerRelation().getAssChildMember().equals(assChildMember)) {
	    				return true;
	    			}
	    			//如果不是分享的记录就跳过
	    			if (assGoods.getAssCustomerRelation().getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
	    				return true;
					}
	    			AssPageVisit assPageVisit=new AssPageVisit();
	    			assPageVisit.setName(assChildMember.getNickName());
	    			assPageVisit.setAssChildMember(assChildMember);
	    			assPageVisit.setAssCustomerRelation(assGoods.getAssCustomerRelation());
	    			assPageVisit.setIp(ip);
	    			assPageVisitService.save(assPageVisit);
	    			
	    			AssGoodsVisit assGoodsVisit=new AssGoodsVisit();
	    			assGoodsVisit.setName(assChildMember.getNickName());
	    			assGoodsVisit.setGoodsName(assGoods.getName());
	    			assGoodsVisit.setAssChildMember(assChildMember);
	    			assGoodsVisit.setAssCustomerRelation(assGoods.getAssCustomerRelation());
	    			assGoodsVisit.setAssGoods(assGoods);
	    			assGoodsVisit.setIp(ip);
	    			assGoodsVisitService.save(assGoodsVisit);
				}
	    		//商品浏览记录
	    		if (request.getRequestURI().equals("/ass/goods/findAssGoods.jhtml")) {
	    			String goodId=request.getParameter("goodId");
	    			AssGoods assGoods=assGoodsService.find(Long.parseLong(goodId));
	    			//如果是本人就不记录
	    			if (assGoods.getAssCustomerRelation().getAssChildMember().equals(assChildMember)) {
	    				return true;
	    			}
	    			//如果不是分享的记录就跳过
	    			if (assGoods.getAssCustomerRelation().getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
	    				return true;
					}
	    			AssGoodsVisit assGoodsVisit=new AssGoodsVisit();
	    			assGoodsVisit.setName(assChildMember.getNickName());
	    			assGoodsVisit.setGoodsName(assGoods.getName());
	    			assGoodsVisit.setAssChildMember(assChildMember);
	    			assGoodsVisit.setAssCustomerRelation(assGoods.getAssCustomerRelation());
	    			assGoodsVisit.setAssGoods(assGoods);
	    			assGoodsVisit.setIp(ip);
	    			assGoodsVisitService.save(assGoodsVisit);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
    public static String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
}
