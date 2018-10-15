package com.microBusiness.manage.controller.admin.ass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.dto.AssShareStatisticsDto;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssGoodsVisitService;
import com.microBusiness.manage.service.ass.AssPageVisitService;
import com.microBusiness.manage.util.DateUtils;

@Controller("assShareStatistics")
@RequestMapping("/admin/ass/shareStatistics")
public class AssShareStatisticsController extends BaseController{
	@Resource
	private AssCustomerRelationService assRelationService;
	@Resource
	private AssGoodsService assGoodsService;
	@Resource
	private AssPageVisitService assPageVisitService;
	@Resource
    private AssChildMemberService assChildMemberService;
	@Resource
	private AssGoodsVisitService assGoodsVisitService;
	
	/**
	 * 列表
	 * @param searchName
	 * @param startDate
	 * @param endDate
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String add(String orderBy,String searchName, Date startDate, Date endDate, Pageable pageable,ModelMap model) {
		if(startDate != null) {
        	startDate = DateUtils.specifyDateZero(startDate);
        }
        if(endDate != null) {
        	endDate = DateUtils.specifyDatetWentyour(endDate);
        }
		Supplier supplier=getCurrentSupplier();
		Page<AssShareStatisticsDto> page=assRelationService.findSharePage(orderBy,searchName, startDate, endDate, pageable, supplier, AssCustomerRelation.ShareType.share, AssCustomerRelation.SourceType.syncBackstage);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("searchName", searchName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("page", page);
		return "/admin/assShareStatistics/list";
	}
	
	/**
	 * 详情页面
	 * @param id
	 * @param model
	 * @return
	 */
//	@RequestMapping(value = "/details" , method = RequestMethod.GET)
//	public String details(Long id,ModelMap model) {
//		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
//		model.addAttribute("assCustomerRelation", assCustomerRelation);
//		return "/admin/assShareStatistics/details";
//	}
	
	/**
	 * 分页查询商品列表
	 * @param id
	 * @param model
	 * @param pageable
	 * @return 
	 */
	@RequestMapping(value = "/goodDetails" , method = RequestMethod.GET)
	public String goodDetails(Long id,ModelMap model,Pageable pageable) {
		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
		Page<AssGoods> page = assGoodsService.findByList(assCustomerRelation, null, pageable);
		model.addAttribute("assCustomerRelation", assCustomerRelation);
		model.addAttribute("id", id);
		model.addAttribute("page", page);
		return "/admin/assShareStatistics/goodDetails";
	}
	
	/**
	 * 用户访问记录
	 * @param id
	 * @param model
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/userRecord" , method = RequestMethod.GET)
	public String userRecord(Long id,ModelMap model,Pageable pageable) {
		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
		Page<ShareUserPageDto> page=assPageVisitService.findPage(assCustomerRelation, null, pageable);
		List<AssGoods> assGoods=assCustomerRelation.getAssGoods();
		List<Long> goodsIds=new ArrayList<>();
		for (AssGoods assGoods2 : assGoods) {
			goodsIds.add(assGoods2.getId());
		}
		for (ShareUserPageDto dto : page.getContent()) {
			boolean isSynchronize=assPageVisitService.isSynchronize(dto.getId(), goodsIds);
			dto.setSynchronize(isSynchronize);
		}
		model.addAttribute("assCustomerRelation", assCustomerRelation);
		model.addAttribute("id", id);
		model.addAttribute("page", page);
		return "/admin/assShareStatistics/userRecord";
	}
	
	/**
	 * 用户浏览商品记录
	 * @param memberId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getShareUserGoods", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getShareUserGoods(Long memberId,Long id){
		AssChildMember user = assChildMemberService.find(memberId);
		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
		
		List<GoodsVisitDto> data=assGoodsVisitService.findList(assCustomerRelation, user);
		List<Map<String, Object>> list=new ArrayList<>();
		for (GoodsVisitDto dto : data) {
			Map<String , Object> map=new HashMap<>();
			AssGoodsVisit assGoodsVisit=dto.getAssGoodsVisit();
			map.put("image", assGoodsVisit.getAssGoods().getImage());
			map.put("goodsName", assGoodsVisit.getGoodsName());
			map.put("goodsVisit", dto.getGoodsVisit());
			list.add(map);
		}
		return JsonEntity.successMessage(list);
	}
}
