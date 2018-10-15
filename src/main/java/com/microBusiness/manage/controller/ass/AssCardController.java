package com.microBusiness.manage.controller.ass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCollectionCard;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.ass.AssCardGoodsService;
import com.microBusiness.manage.service.ass.AssCardService;
import com.microBusiness.manage.service.ass.AssCollectionCardService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assCardController")
@RequestMapping("/ass/card")
public class AssCardController extends BaseController {

	@Resource
	private AssCardService assCardService;
	@Resource
	private AssCollectionCardService assCollectionCardService;
	@Resource
	private AssCardGoodsService assCardGoodsService;
	@Resource
	private AssCustomerRelationService assRelationService;
	@Resource
	private AssGoodsService assGoodsService;
	@Resource
	private WeChatService weChatService;
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody JsonEntity get(String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard = assChildMember.getAssCard();

		List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (assCard != null) {
			map.put("id", assCard.getId());
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
			map.put("profiles", assCard.getProfiles());
			map.put("headImgUrl", assCard.getAssChildMember().getHeadImgUrl());
			
			reList.add(map);
		}
		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
	}

	/**
	 * 新建名片
	 * @param customerRelation
	 * @param areaId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity save(String unionId, AssCard assCard, String ids, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCardOld = assChildMember.getAssCard();
		
		if (assCardOld != null) {
			return new JsonEntity().error(Code.code16000, Code.code16000.getDesc());
		}
		
		try {
			assCard.setShareType(AssCard.ShareType.noshare);
			assCard.setAssChildMember(assChildMember);
			assCardService.save(assCard, ids);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code15000, Code.code15000.getDesc());
		}
		
	}
	
	@RequestMapping(value = "/edit" , method = RequestMethod.POST)
	@ResponseBody
	public JsonEntity edit(String unionId, AssCard assCard, String ids,HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		
		if (assCard.getId() == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		try {
			
			assCardService.update(assCard, ids);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code16001, Code.code16001.getDesc());
		}

	}
	
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity delete(Long id, String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		if(id == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		assCardService.delete(id);
		return JsonEntity.successMessage();
	}

	@RequestMapping(value = "/share" , method = RequestMethod.POST)
	public @ResponseBody JsonEntity share(Long id, String unionId, String sn, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		if(id == null || StringUtils.isEmpty(sn)) {
			return new JsonEntity("010502" , "参数错误");
		}
		AssCard assCard = assCardService.copy(assCardService.find(id), assChildMember, sn);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", assCard.getId());
		map.put("name", assCard.getName());
		map.put("phone", assCard.getPhone());
		map.put("companyName", assCard.getCompanyName());
		map.put("position", assCard.getPosition());
		map.put("email", assCard.getEmail());
		map.put("wxNum", assCard.getWxNum());
		map.put("profiles", assCard.getProfiles());
		map.put("sn", assCard.getSn());
		map.put("headImgUrl", assCard.getAssChildMember().getHeadImgUrl());
		
		return JsonEntity.successMessage(map);
	}

	@RequestMapping(value = "/getAssCard", method = RequestMethod.GET)
	public @ResponseBody JsonEntity getAssCard(String unionId, Long id, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard = assCardService.find(id);

		List<AssCardGoods> assCardGoodsList = assCardGoodsService.getAssCardGoodsByCard(assCard);
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (assCard != null) {
			map.put("id", assCard.getId());
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
			map.put("profiles", assCard.getProfiles());
			map.put("headImgUrl", assCard.getAssChildMember().getHeadImgUrl());
			if (unionId.equals(assCard.getAssChildMember().getUnionId())) {
				map.put("flag", true);
			} else {
				map.put("flag", false);
			}
			if (assCardGoodsList.size() > 0) {
				map.put("goodsFlag", true);
			} else {
				map.put("goodsFlag", false);
			}
			
			String ids = "";
			for (AssCardGoods assCardGoods : assCardGoodsList) {
				ids = ids + assCardGoods.getAssCustomerRelation().getId()+",";
			}
			map.put("ids", ids);
		}

		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
	}
	
	@RequestMapping(value = "/getAssCardBySn", method = RequestMethod.GET)
	public @ResponseBody JsonEntity getAssCardBySn(String unionId, String sn, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard = assCardService.findBySn(sn);

		Map<String,Object> map = new HashMap<String,Object>();
		if (assCard != null) {
			map.put("id", assCard.getId());
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
			map.put("profiles", assCard.getProfiles());
			map.put("headImgUrl", assCard.getAssChildMember().getHeadImgUrl());
			if (assCard.getAssChildMember().getAdmin() != null) {
				map.put("adminFlag", true);
				map.put("adminId", assCard.getAssChildMember().getAdmin().getId());
			} else {
				map.put("adminFlag", false);
			}

			if (unionId.equals(assCard.getAssChildMember().getUnionId())) {
				map.put("flag", true);
			} else {
				map.put("flag", false);
			}
			if (assCollectionCardService.getAssCollectionCard(assChildMember, assCard) != null) {
				map.put("isCollection", true);
			}else {
				map.put("isCollection", false);
			}
		}

		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), map);
	}
	
	/**
	 * 名片出售商品目录
	 * 
	 * @param unionId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getGoods", method = RequestMethod.GET)
	public @ResponseBody JsonEntity getGoods(String unionId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard = assChildMember.getAssCard();
		List<AssCardGoods> assCardGoodsList = assCardGoodsService.getAssCardGoodsByCard(assCard);
		
		List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
		
		for (AssCardGoods assCardGoods : assCardGoodsList) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("clientName", assCardGoods.getAssCustomerRelation().getClientName());
			map.put("theme", assCardGoods.getAssCustomerRelation().getTheme());
			
			reList.add(map);
		}

		return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
	}

	/**
	 * 收藏名片
	 * @return
	 */
	@RequestMapping(value = "/collectionCard", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity collectionCard(String unionId,Long cardId){
		if(null == unionId && null == cardId){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard=assCardService.find(cardId);
		if (assCard == null) {
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		if (assCollectionCardService.getAssCollectionCard(assChildMember, assCard) != null) {
			return new JsonEntity(Code.code_card_11111, Code.code_card_11111.getDesc());
		}
		AssCollectionCard assCollectionCard=new AssCollectionCard();
		assCollectionCard.setAssCard(assCard);
		assCollectionCard.setAssChildMember(assChildMember);
		assCollectionCardService.save(assCollectionCard);
		
		if (assCollectionCard.getId() != null) {
			return JsonEntity.successMessage();
		}else {
			return JsonEntity.error(Code.code_card_11110, Code.code_card_11110.getDesc());
		}
	}
	
	
	/**
	 * 取消收藏名片
	 * @return
	 */
	@RequestMapping(value = "/cancelCollectionCard", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity cancelCollectionCard(String unionId,Long cardId){
		if(null == unionId && null == cardId){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard=assCardService.find(cardId);
		AssCollectionCard assCollectionCard=assCollectionCardService.getAssCollectionCard(assChildMember, assCard);
		if (assCollectionCard != null) {
			assCollectionCardService.delete(assCollectionCard);
		}
		return JsonEntity.successMessage();
	}
	
	/**
	 * 获取收藏名片列表
	 * @return
	 */
	@RequestMapping(value = "/getCollectionCardList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getCollectionCardList(String unionId,Pageable pageable){
		if(null == unionId){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		Page<AssCollectionCard> page=assCollectionCardService.findPage(assChildMember,pageable);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (AssCollectionCard assCollectionCard : page.getContent()) {
			Map<String,Object> map = new HashMap<String,Object>();
			AssCard assCard=assCollectionCard.getAssCard();
			map.put("id", assCollectionCard.getId());
			map.put("cardId", assCard.getId());
			map.put("name", assCard.getName());
			map.put("phone", assCard.getPhone());
			map.put("companyName", assCard.getCompanyName());
			map.put("position", assCard.getPosition());
			map.put("email", assCard.getEmail());
			map.put("wxNum", assCard.getWxNum());
			map.put("profiles", assCard.getProfiles());
			map.put("headImgUrl", assCard.getAssChildMember().getHeadImgUrl());
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity list(String unionId, Pageable pageable , ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Page<AssCustomerRelation> resultList;
		try {
			AssChildMember assChildMember = this.getAssChildMember(unionId);
			pageable.setPageSize(1000);
			resultList = assRelationService.findPage(null, null ,null,pageable, null, AssCustomerRelation.SourceType.MOBILE, assChildMember);
			
			List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
			
			AssCard assCard = assChildMember.getAssCard();
			List<AssCardGoods> assCardGoodsList = assCardGoodsService.getAssCardGoodsByCard(assCard);
			
			for (AssCustomerRelation dto : resultList.getContent()) {
				Map<String,Object> map = new HashMap<String,Object>();
				
				boolean flag = false;
				for (AssCardGoods assCardGoods : assCardGoodsList) {
					if (dto.getId() == assCardGoods.getAssCustomerRelation().getId()) {
						flag = true;
					}
				}

				map.put("id", dto.getId());
				if (dto.getArea() != null) {
					map.put("area", dto.getArea().getFullName());
				}
				map.put("userName", dto.getUserName());
				map.put("address", dto.getAddress());
				map.put("tel", dto.getTel());
				map.put("clientName", dto.getClientName());
				map.put("sourceType", dto.getSourceType());
				map.put("theme", dto.getTheme());
				map.put("profiles", dto.getProfiles());
				map.put("flag", flag);
				reList.add(map);
			}

			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code11710, Code.code11710.getDesc());
		}
		
	}
	
	/**
	 * 名片 出售商品目录 列表
	 * 
	 * @param unionId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getShareList", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getShareList(String unionId,Long cardId,Pageable pageable) {
		if(null == unionId && null == cardId){
			return new JsonEntity(Code.code019998, Code.code019998.getDesc());
		}
		
		AssChildMember assChildMember = this.getAssChildMember(unionId);
		AssCard assCard = assCardService.find(cardId);
		Page<AssCardGoods> page=assCardGoodsService.findPage(assCard, pageable);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for (AssCardGoods assCardGoods : page.getContent()) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("clientName", assCardGoods.getAssCustomerRelation().getClientName());
			map.put("theme", assCardGoods.getAssCustomerRelation().getTheme());
			map.put("id", assCardGoods.getAssCustomerRelation().getId());
			map.put("sn", assCardGoods.getAssCustomerRelation().getSn());
			map.put("sourceType", assCardGoods.getAssCustomerRelation().getSourceType());
			if(null == assCardGoods.getAssCustomerRelation().getAssGoodDirectory()) {
				map.put("sour", false);
			}else {
				map.put("sour", true);
			}
			list.add(map);
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
	}
	
	@RequestMapping(value = "/generateTwoCode", method={RequestMethod.GET, RequestMethod.POST})
    public void generateTwoCode(Long id, String sn, String unionId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

        CloseableHttpResponse httpResponse = null;
		try {
			String accessToken = weChatService.getAssSmallGlobalToken();

			AssChildMember assChildMember = this.getAssChildMember(unionId);
			
			AssCard assCard = assCardService.copy(assCardService.find(id), assChildMember, sn);
			
			httpResponse = ApiSmallUtils.getInputStream("/pages/shareCard/shareCard?sn="+assCard.getSn(), accessToken, request, response);

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