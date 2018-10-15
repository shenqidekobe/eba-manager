package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;
import com.microBusiness.manage.entity.ass.AssListMemberStatus;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ass.AssCardGoodsService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.ass.AssGoodsSyncLogService;
import com.microBusiness.manage.service.ass.AssGoodsVisitService;
import com.microBusiness.manage.service.ass.AssPageVisitService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.PosterImageUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 功能描述：
 * 修改记录：
 */ 
@Controller("assWxCustomerRelation")
@RequestMapping("/ass/customerRelation")
public class AssCustomerRelationController extends BaseController {

	@Resource
	private AssCustomerRelationService assRelationService;
	@Resource
	private AreaService areaService;
	@Resource
    private AssChildMemberService assChildMemberService;
	@Resource
	private AssGoodsService assGoodsService;
	@Resource
	private AssGoodsSyncLogService assGoodsSyncLogService;
	@Resource
	private AssGoodsVisitService assGoodsVisitService;
	@Resource
	private AssPageVisitService assPageVisitService;
	@Resource
	private AssCardGoodsService assCardGoodsService;

	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity list(String unionId, Pageable pageable , ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Page<AssCustomerRelation> resultList;
		try {
			AssChildMember assChildMember = this.getAssChildMember(unionId);
			pageable.setPageSize(1000);
			resultList = assRelationService.findPage(null, null ,null,pageable, null, null, assChildMember);
			
			List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
			
			for (AssCustomerRelation dto : resultList.getContent()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", dto.getId());
				if (dto.getArea() != null) {
					map.put("area", dto.getArea().getFullName());
				}
				map.put("userName", dto.getUserName());
				map.put("address", dto.getAddress());
				map.put("tel", dto.getTel());
				map.put("clientName", dto.getClientName());
				map.put("sourceType",dto.getSourceType());
				map.put("theme", dto.getTheme());
				map.put("profiles", dto.getProfiles());
				reList.add(map);
			}

			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code11710, Code.code11710.getDesc());
		}
		
	}
	
	/**
	 * 新建供应商
	 * @param customerRelation
	 * @param areaId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity save(String unionId, AssCustomerRelation customerRelation , Long areaId, String clientName , ModelMap model) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		if(clientName == null) {
			 return new JsonEntity("010502" , "参数错误");
		}
		if(assRelationService.inviteNameExists(clientName , customerRelation.getTheme(), assChildMember) != null) {
			return new JsonEntity().error(Code.code11711, Code.code11711.getDesc());
		}
		
		try {
			customerRelation.setSourceType(AssCustomerRelation.SourceType.MOBILE);
			if (areaId != null) {
				customerRelation.setArea(areaService.find(areaId));
			}
			customerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
			customerRelation.setAssChildMember(assChildMember);
			assRelationService.save(customerRelation);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code11712, Code.code11712.getDesc());
		}
		
	}
	
	@RequestMapping("/edit")
	public JsonEntity edit(Long id, String oldClientName, HttpServletRequest request, HttpServletResponse response) {
		if (id == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		try {
			
			AssCustomerRelation customerRelation = assRelationService.find(id);
			customerRelation.setOldClientName(oldClientName);
			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), customerRelation);
		} catch (Exception e) {
			return new JsonEntity().error(Code.code11713, Code.code11713.getDesc());
		}

	}

	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity update(String unionId, AssCustomerRelation customerRelation, ModelMap mode) {
		if(customerRelation == null) {
			 return new JsonEntity("010502" , "参数错误");
		}
		AssCustomerRelation assCustomerRelationN = assRelationService.find(customerRelation.getId());
		if (!assCustomerRelationN.getClientName().equals(customerRelation.getClientName())) {
			AssChildMember assChildMember = this.getAssChildMember(unionId);
			if(assRelationService.inviteNameExists(customerRelation.getClientName() , customerRelation.getTheme(), assChildMember) != null) {
				return new JsonEntity().error(Code.code11711, Code.code11711.getDesc());
			}
		}
		
		try {
			
			assRelationService.update(customerRelation, "sourceType", "shareType", "source","assGoodDirectory","adminName");
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code11713, Code.code11713.getDesc());
		}
	}

	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity delete(Long id, String unionId) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		if(id == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		Pageable pageable = new Pageable();
		
		Page<AssGoods> goods = assGoodsService.findByList(assRelationService.find(id), null, pageable);
		if (goods.getContent().size() > 0) {
			for (AssGoods gd : goods.getContent()) {
				gd.setDeleted(true);
				assGoodsService.update(gd);
			}
		}

		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		
		AssCard assCard = assChildMember.getAssCard();
		AssCardGoods assCardGoods = assCardGoodsService.get(assCard, assCustomerRelation);
		if (assCardGoods != null) {
			assCardGoods.setDeleted(true);
			assCardGoodsService.update(assCardGoods);
		}

		assCustomerRelation.setDeleted(true);
		
		assRelationService.update(assCustomerRelation);
		return JsonEntity.successMessage();
	}
	
	@RequestMapping(value = "/checkName", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkName(String unionId, String clientName, String oldClientName){
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		
        if (StringUtils.isEmpty(clientName)) {
            return false;
        }
//        AssCustomerRelation assCustomerRelation = assRelationService.inviteNameExists(clientName, assChildMember);
//        if(assCustomerRelation != null) {
//        	if(assCustomerRelation.getClientName().equals(oldClientName)) {
//        		return true;
//        	}
//        	if(!assCustomerRelation.getClientName().equals(oldClientName)) {
//        		return false;
//        	}
//		}
        return true;
    }

	@RequestMapping(value = "/getCustomerRelation" , method = RequestMethod.GET)
	public @ResponseBody JsonEntity getCustomerRelation(Long id, String unionId, HttpServletRequest request, HttpServletResponse response) {
		if(id == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		AssCustomerRelation dto = assRelationService.find(id);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", dto.getId());
		if (dto.getArea() != null) {
			map.put("areaId", dto.getArea().getId());
		}else {
			map.put("areaId", "");
		}
		map.put("userName", dto.getUserName());
		map.put("address", dto.getAddress());
		map.put("tel", dto.getTel());
		map.put("clientName", dto.getClientName());
		map.put("accountName", dto.getAccountName());
		map.put("bank", dto.getBank());
		map.put("bankAccountNum", dto.getBankAccountNum());
		map.put("theme", dto.getTheme());
		map.put("profiles", dto.getProfiles());
		
		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
	}
	
	/**
	 * 分享商品
	 * @param unionId
	 * @param id  供应商id
	 * @return
	 */
	@RequestMapping(value = "/share" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity share(String unionId,Long id,String sn,boolean synchronizationFla, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||id == null || StringUtils.isBlank(sn)) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		if (!assChildMember.equals(assCustomerRelation.getAssChildMember())) {
			return new JsonEntity(Code.code_customer_11110 , Code.code_customer_11110.getDesc());
		}
		
		AssCustomerRelation aRelation=assGoodsService.copyShareAssGoods(assCustomerRelation, assChildMember,sn,synchronizationFla);
		Map<String, Object> map=new HashMap<>();
		map.put("id", aRelation.getId());
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 获取需要分享的信息
	 * @param unionId
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getCustomerRelationGood" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getCustomerRelationGood(String unionId,Long id,Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||id == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		if (!assChildMember.equals(assCustomerRelation.getAssChildMember())) {
			return new JsonEntity(Code.code_customer_11110 , Code.code_customer_11110.getDesc());
		}
		
		Map<String, Object> map=dealAssCustomerRelation(assCustomerRelation,pageable);
		//名片信息
		AssCard assCard=assChildMember.getAssCard();
		if (assCard != null) {
			map.put("exist", true);
			map.put("cardId", assCard.getId());
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
		}else {
			map.put("exist", false);
		}
		return JsonEntity.successMessage(map);
	}
	
	/**
	 * 生成海报
	 * @param unionId
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */ 
	@RequestMapping(value = "/getPoster" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getPoster(String unionId,Long id, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||id == null) {
			return new JsonEntity("010502" , "参数错误"); 
		} 
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		if (!assChildMember.equals(assCustomerRelation.getAssChildMember())) {
			return new JsonEntity(Code.code_customer_11110 , Code.code_customer_11110.getDesc());
		}
		Map<String, Object> map=PosterImageUtil.genImage(id,unionId);
		return JsonEntity.successMessage(map); 
	}
	
	/**
	 * 查看分享
	 * @param unionId
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getShare" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getShare(String unionId,String sn,Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||StringUtils.isBlank(sn)) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assRelationService.findBySn(sn);
		if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
			return new JsonEntity(Code.code_customer_11111, Code.code_customer_11111.getDesc());
		} 
		
		Map<String, Object> map=dealAssCustomerRelation(assCustomerRelation,pageable);

		//名片信息
		AssCard assCard=assCustomerRelation.getAssCard();
		if (assCard != null) {
			map.put("exist", true);
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
		}else {
			map.put("exist", false);
		}
		//当前用户相对于分享的角色  分享人  同步过的人  未同步过的人 

		AssListMemberStatus memberStatus;
		if (assChildMember.equals(assCustomerRelation.getAssChildMember())) {
			 memberStatus=AssListMemberStatus.self;
		}else {
			List<AssCustomerRelation> list=assRelationService.findList(AssCustomerRelation.SourceType.MOBILE, AssCustomerRelation.ShareType.noshare, assCustomerRelation, assChildMember);
			if (list != null && list.size()>0) {
				memberStatus=AssListMemberStatus.participate;
			}else {
				memberStatus=AssListMemberStatus.noparticipate;
			}
		}
		map.put("memberStatus", memberStatus);
		return JsonEntity.successMessage(map);
	}

	/**
	 * 同步商品
	 * @param unionId
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/synchronize" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity synchronize(String unionId,Long id, HttpServletRequest request, HttpServletResponse response) {
		if(unionId==null ||id == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
			return new JsonEntity(Code.code_customer_11111, Code.code_customer_11111.getDesc());
		}
		AssCustomerRelation aRelation=assGoodsService.copyToNoShareAssGoods(assCustomerRelation, assChildMember);
		Map<String, Object> map=new HashMap<>();
		map.put("id", aRelation.getId());
		
		// 新增商品同步目录
		assGoodsSyncLogService.add(assCustomerRelation, assChildMember);
		return JsonEntity.successMessage(map);
	}
	
	
	private Map<String, Object> dealAssCustomerRelation(AssCustomerRelation assCustomerRelation,Pageable pageable){
		Map<String, Object> map=new HashMap<>();
		map.put("id", assCustomerRelation.getId());
		map.put("clientName", assCustomerRelation.getClientName());
		map.put("theme", assCustomerRelation.getTheme());
		map.put("profiles", assCustomerRelation.getProfiles());
		
		Page<AssGoods> page=assGoodsService.findByList(assCustomerRelation, null, pageable);
		List<AssGoods> assGoodsList = page.getContent();
		
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		for(AssGoods assGoods : assGoodsList) {
			Map<String, Object> gooodMap = new HashMap<String, Object>();
			//获取商品规格
			List<AssProduct> assProducts = assGoodsService.getSpecifications(assGoods.getId());
			if(assProducts.size() > 1) {
				gooodMap.put("exist", "many");//多规格
				List<Long> productIds = new ArrayList<Long>();
				for(AssProduct assProduct : assProducts) {
					productIds.add(assProduct.getId());
				}
				gooodMap.put("productIds", productIds);
			}else {
				if (StringUtils.isNotBlank(assProducts.get(0).getSpecification())) {
					gooodMap.put("exist", "many");
				}else {
					gooodMap.put("exist", "single");
				}
				gooodMap.put("productId", assProducts.get(0).getId());
			}
			gooodMap.put("image", assGoods.getImage());
			gooodMap.put("name", assGoods.getName());
			gooodMap.put("goodId", assGoods.getId());
			gooodMap.put("labels", assGoods.getLabels());
			gooodMap.put("sourceType", assGoods.getSourceType());
			if(null == assGoods.getGoods()) {
				gooodMap.put("sour", false);
			}else {
				gooodMap.put("sour", true);
			}
			resultMap.add(gooodMap);
		}
		map.put("goods", resultMap);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return map;
	}
	
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity copy(String unionId, AssCustomerRelation assCustomerRelation, HttpServletRequest request, HttpServletResponse response){
		AssChildMember assChildMember = this.getAssChildMember(unionId);

		if(assRelationService.inviteNameExists(assCustomerRelation.getClientName() , assCustomerRelation.getTheme(), assChildMember) != null) {
			return new JsonEntity().error(Code.code11711, Code.code11711.getDesc());
		}
		
		AssCustomerRelation assCustomerRelationN = assRelationService.find(assCustomerRelation.getId());

		assGoodsService.copyNoShareAssGoods(assCustomerRelationN, assChildMember, assCustomerRelation.getClientName(), assCustomerRelation.getTheme());
		return JsonEntity.successMessage();
    }

	@RequestMapping(value = "/getCopy", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getCopy(String unionId, Long id, HttpServletRequest request, HttpServletResponse response){
		AssChildMember assChildMember = this.getAssChildMember(unionId);

		AssCustomerRelation assCustomerRelation = assRelationService.find(id);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clientName", assCustomerRelation.getClientName());
		map.put("theme", assCustomerRelation.getTheme());
		
		//return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
		return JsonEntity.successMessage(map);
    }
	
	/**
	 * 分页查询分享出去的商品目录
	 * @param unionId
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/getSharePage", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getSharePage(String unionId,Pageable pageable){
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		Page<AssCustomerRelation> page=assRelationService.findPageByShareType(assChildMember, AssCustomerRelation.ShareType.share, pageable);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (AssCustomerRelation assCustomerRelation : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", assCustomerRelation.getId());
			map.put("clientName", assCustomerRelation.getClientName());
			map.put("theme", assCustomerRelation.getTheme());
			map.put("type", assCustomerRelation.getType());
			if (assCustomerRelation.getAssGoods().size()>1) {
				map.put("goodsTitle", assCustomerRelation.getAssGoods().get(0).getName()+"等"+assCustomerRelation.getAssGoods().size()+"件商品");
			}else if (assCustomerRelation.getAssGoods().size() == 1) {
				map.put("goodsTitle", assCustomerRelation.getAssGoods().get(0).getName());
			}
			map.put("createDate", assCustomerRelation.getCreateDate());
			//页面访问记录
			map.put("pageVisit", assPageVisitService.getCountByRelation(assCustomerRelation));
			//商品浏览记录
			map.put("goodsVisit", assGoodsVisitService.getCountByRelation(assCustomerRelation));
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	/**
	 * 获取用户点击页面、浏览商品总数记录
	 * @param unionId
	 * @param id
	 * @param orderBy
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/getShareUserPage", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getShareUserPage(String unionId,Long id,String orderBy,Pageable pageable){
		if(unionId==null ||id == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
		if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
			return new JsonEntity(Code.code_customer_11111, Code.code_customer_11111.getDesc());
		}
		if (!assCustomerRelation.getAssChildMember().equals(assChildMember)) {
			return new JsonEntity(Code.code_customer_11112 , Code.code_customer_11112.getDesc());
		}
		Page<ShareUserPageDto> page=assPageVisitService.findPage(assCustomerRelation, orderBy, pageable);
		List<Map<String, Object>> list=new ArrayList<>();
		for (ShareUserPageDto dto : page.getContent()) {
			Map<String, Object> map=new HashMap<>();
			map.put("memberId", dto.getId());
			map.put("name", dto.getName());
			map.put("headImgUrl", dto.getHeadImgUrl());
			map.put("date", dto.getDate());
			map.put("pageVisit", dto.getPageVisit());
			map.put("goodsVisit", dto.getGoodsVisit());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	/**
	 * 获取用户浏览商品记录
	 * @param unionId
	 * @param id
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/getShareUserGoods", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getShareUserGoods(String unionId,Long memberId,Long id,Pageable pageable){
		if(unionId==null ||memberId == null || id == null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssChildMember user = assChildMemberService.find(memberId);
		AssCustomerRelation assCustomerRelation=assRelationService.find(id);
		if (assCustomerRelation.getShareType().equals(AssCustomerRelation.ShareType.noshare)) {
			return new JsonEntity(Code.code_customer_11111, Code.code_customer_11111.getDesc());
		}
		if (!assCustomerRelation.getAssChildMember().equals(assChildMember)) {
			return new JsonEntity(Code.code_customer_11112 , Code.code_customer_11112.getDesc());
		}
		Page<GoodsVisitDto> page=assGoodsVisitService.findPage(assCustomerRelation, user, pageable);
		List<Map<String, Object>> list=new ArrayList<>();
		for (GoodsVisitDto dto : page.getContent()) {
			Map<String , Object> map=new HashMap<>();
			AssGoodsVisit assGoodsVisit=dto.getAssGoodsVisit();
			map.put("image", assGoodsVisit.getAssGoods().getImage());
			map.put("goodsName", assGoodsVisit.getGoodsName());
			map.put("goodsVisit", dto.getGoodsVisit());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	/**
	 * 获取分享总次数、页面浏览总次数、商品浏览总次数  
	 * @param unionId
	 * @return
	 */
	@RequestMapping(value = "/getShareAllVisit", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity getShareAllVisit(String unionId){
		if(unionId==null) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		Map<String, Object> map=new HashMap<>();
		map.put("shareCount", assRelationService.getShareAllVisit(assChildMember));
		map.put("pageAllVisit", assPageVisitService.getShareAllVisit(assChildMember));
		map.put("goodsAllVisit", assGoodsVisitService.getShareAllVisit(assChildMember));
		return JsonEntity.successMessage(map);
	}
}
