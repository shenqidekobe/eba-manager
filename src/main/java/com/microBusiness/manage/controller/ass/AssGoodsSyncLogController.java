package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.GoodsSyncDto;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssGoodsSyncLog;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssGoodsSyncLogService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.DateUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assGoodsSyncLogController")
@RequestMapping("/ass/goodsSyncLog")
public class AssGoodsSyncLogController extends BaseController{

	@Resource
	private AssGoodsSyncLogService assGoodsSyncLogService;
	@Resource
	private AssGoodsService assGoodsService;
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity list(String unionId, String searchName, String startDate, String endDate, Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
		Page<AssGoodsSyncLog> resultList;
		try {
			AssChildMember assChildMember = this.getAssChildMember(unionId);
			
			resultList = assGoodsSyncLogService.query(startDate, endDate, searchName, pageable, assChildMember);
			
			List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
			
			for (AssGoodsSyncLog dto : resultList.getContent()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", dto.getId());
				map.put("nickName", dto.getBeAssChildMember().getNickName());
				map.put("headImgUrl", dto.getBeAssChildMember().getHeadImgUrl());
				map.put("createDate", dto.getCreateDate());
				map.put("clientName", dto.getAssCustomerRelation().getClientName());
				map.put("theme", dto.getAssCustomerRelation().getTheme());
				
				// 获取同步人同步的商品
				List<Map<String,Object>> goods = getGoods(dto.getAssCustomerRelation());
				map.put("goodsList", goods);
				map.put("goodsTotal", goods.size());
				reList.add(map);
			}

			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code17000, Code.code17000.getDesc());
		}
		
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getTotal" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getTotal(String unionId, Date startDate, Date endDate, String ts, Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
		try {
			AssChildMember assChildMember = this.getAssChildMember(unionId);
			if(null != ts) {
				if(ts.equalsIgnoreCase("thisWeek")) {
					startDate = DateUtils.startThisWeek();
					endDate = DateUtils.endOfTheWeek();
				};
				if(ts.equalsIgnoreCase("lastWeek")) {
					startDate = DateUtils.lastWeekStartTime();
					endDate = DateUtils.lastWeekEndTime();
				};
				if(ts.equalsIgnoreCase("lastMonth")) {
					startDate = DateUtils.lastMonthStartTime();
					endDate = DateUtils.lastMonthEndTime();
				};
				if(ts.equalsIgnoreCase("thisMonth")) {
					startDate = DateUtils.startThisMonth();
					endDate = DateUtils.theEndOfTheMonth();
				};
			}
			
			List<GoodsSyncDto> syncLog = assGoodsSyncLogService.queryAssGoodsSyncLogByDate(ts, startDate, endDate, assChildMember);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("list",syncLog);
			map.put("total", assGoodsSyncLogService.queryTotalByDate(startDate, endDate, assChildMember));

			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code17001, Code.code17001.getDesc());
		}
		
	}
	
	private List<Map<String,Object>> getGoods(AssCustomerRelation assCustomerRelation){
		List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
		
		List<AssGoods> goodsL = assGoodsService.findByList(assCustomerRelation);
		
		for (AssGoods assGoods : goodsL) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("goods", assGoods.getName());
			reList.add(map);
		}
		
		return reList;
	}
}
