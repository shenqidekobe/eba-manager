/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.ass.impl;


import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.dao.ass.AssCardDao;
import com.microBusiness.manage.dao.ass.AssCardGoodsDao;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.dao.ass.AssGoodsDao;
import com.microBusiness.manage.dao.ass.AssProductDao;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.SnService;
import com.microBusiness.manage.service.ass.AssCardService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("assCardServiceImpl")
public class AssCardServiceImpl extends BaseServiceImpl<AssCard, Long> implements AssCardService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private AssCardDao assCardDao;
	@Resource
	private AssCardGoodsDao assCardGoodsDao;
	@Resource
	private AssCustomerRelationDao assCustomerRelationDao;
	@Resource
	private AssGoodsDao assGoodsDao;
	@Resource
	private AssProductDao assProductDao;
	@Resource
	private SnDao snDao;
	@Resource
	private SnService snService;

	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return assGoodsDao.snExists(sn);
	}
	
	@Override
	public AssCard copy(AssCard assCard, AssChildMember assChildMember, String sn) {
		Assert.notNull(assCard);
		Assert.isTrue(!assCard.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		
		AssCard assCardIs = this.findBySn(sn);
		if (assCardIs == null) {
			
			AssCard assCardNew = new AssCard();
			assCardNew.setAssChildMember(assChildMember);
			assCardNew.setName(assCard.getName());
			assCardNew.setPhone(assCard.getPhone());
			assCardNew.setCompanyName(assCard.getCompanyName());
			assCardNew.setPosition(assCard.getPosition());
			assCardNew.setWxNum(assCard.getWxNum());
			assCardNew.setEmail(assCard.getEmail());
			assCardNew.setProfiles(assCard.getProfiles());
			assCardNew.setShareType(AssCard.ShareType.share);
			assCardNew.setSn(sn);
			
			assCardDao.persist(assCardNew);
			
			List<AssCardGoods> assCardGoodsList = assCardGoodsDao.getAssCardGoodsByCard(this.find(assCard.getId()));
			for (AssCardGoods assCardGoods : assCardGoodsList) {
				// 复制供应商，商品和规格
				AssCustomerRelation assCustomerRelation = copyToNoShareAssGoods(assCardGoods.getAssCustomerRelation(), assChildMember, assCardNew);
				
				// 复制名片商品条目关联表
				AssCardGoods assCardGoodsNew = new AssCardGoods();
				assCardGoodsNew.setAssCard(assCardNew);
				assCardGoodsNew.setAssCustomerRelation(assCustomerRelation);
				assCardGoodsDao.persist(assCardGoodsNew);
			}
			
			logger.info("复制名片时无重复sn，新增名片assCard="+assCardNew.toString());
			
			return assCardNew;
		} else {
			
			logger.info("复制名片时已有重复sn，无需新增assCard="+assCardIs.toString());
			
			return assCardIs;
		}
		
	}

	@Override
	public AssCard findBySn(String sn) {
		return assCardDao.findBySn(sn);
	}

	@Override
	public AssCard save(AssCard assCard, String idStr) {
		AssCard assCardNew = this.save(assCard);
		
		String[] ids = idStr.split(",");
		for (String id : ids) {
			if (!id.equals("")) {
				AssCardGoods assCardGoods = new AssCardGoods();
				assCardGoods.setAssCard(assCardNew);
				assCardGoods.setAssCustomerRelation(assCustomerRelationDao.find(Long.parseLong(id)));
				assCardGoodsDao.persist(assCardGoods);
			}
		}
		
		return assCardNew;
	}

	@Override
	public AssCard update(AssCard assCard, String idStr) {
		// 删除名片商品关联数据
		List<AssCardGoods> assCardGoodsList = assCardGoodsDao.getAssCardGoodsByCard(this.find(assCard.getId()));
		if (assCardGoodsList != null) {
			for (AssCardGoods assCardGoods : assCardGoodsList) {
				assCardGoodsDao.delete(assCardGoods);
			}
		}
		
		AssCard assCardNew = this.update(assCard, "shareType", "assChildMember");
		
		// 重新添加商品目录
		String[] ids = idStr.split(",");
		for (String id : ids) {
			if (!id.equals("")) {
				AssCardGoods assCardGoods = new AssCardGoods();
				assCardGoods.setAssCard(assCardNew);
				assCardGoods.setAssCustomerRelation(assCustomerRelationDao.find(Long.parseLong(id)));
				assCardGoodsDao.persist(assCardGoods);
			}
		}

		return assCard;
	}
	
	@Override
	public void delete(Long id) {
		// 删除名片商品关联数据
		List<AssCardGoods> assCardGoodsList = assCardGoodsDao.getAssCardGoodsByCard(this.find(id));
		if (assCardGoodsList != null) {
			for (AssCardGoods assCardGoods : assCardGoodsList) {
				assCardGoodsDao.delete(assCardGoods);
			}
		}
		
		AssCard assCard = assCardDao.find(id);
		assCard.setDeleted(true);
		
		this.update(assCard);
	}
	
	private AssCustomerRelation copyToNoShareAssGoods(
			AssCustomerRelation assCustomerRelation,
			AssChildMember assChildMember, AssCard assCard) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		
		//复制供应商
		AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
		pAssCustomerRelation.setClientName(assCustomerRelation.getClientName());
		pAssCustomerRelation.setTheme(assCustomerRelation.getTheme());
		pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
		pAssCustomerRelation.setAssChildMember(assChildMember);
		pAssCustomerRelation.setSource(assCustomerRelation);
		pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.share);
		pAssCustomerRelation.setSourceType(assCustomerRelation.getSourceType());
		
		String sn = snService.generate(Sn.Type.relation);
		pAssCustomerRelation.setSn(sn);
//		pAssCustomerRelation.setAssCard(assCard); 
		pAssCustomerRelation.setType(AssCustomerRelation.Type.card);
		pAssCustomerRelation.setAssGoodDirectory(assCustomerRelation.getAssGoodDirectory());
		pAssCustomerRelation.setAdminName(assCustomerRelation.getAdminName());
		assCustomerRelationDao.persist(pAssCustomerRelation);
		
		//根据供应商获取商品信息
		List<AssGoods> assGoodsList = assGoodsDao.findByList(assCustomerRelation);
		for(AssGoods assGoods : assGoodsList) {
			//复制商品
			AssGoods pAssGoods = new AssGoods();
			pAssGoods.setName(assGoods.getName());
			pAssGoods.setImage(assGoods.getImage());
			pAssGoods.setUnit(assGoods.getUnit());
			pAssGoods.setCreateDate(assGoods.getCreateDate());
			pAssGoods.setModifyDate(assGoods.getModifyDate());
			pAssGoods.setAssCustomerRelation(pAssCustomerRelation);
			pAssGoods.setLabels(assGoods.getLabels());
			pAssGoods.setDetailsDescription(assGoods.getDetailsDescription());
			pAssGoods.setDetailsImage(assGoods.getDetailsImage());
			pAssGoods.setSource(assGoods);
			pAssGoods.setSourceType(assGoods.getSourceType());
			pAssGoods.setGoods(assGoods.getGoods());
			pAssGoods.setDetails(assGoods.getDetails());
			setValue(pAssGoods);
			assGoodsDao.persist(pAssGoods);
			Set<AssProduct> assProducts = assGoods.getAssProducts();
			for(AssProduct assProduct : assProducts) {
				//复制AssProduct
				AssProduct pAssProduct = new AssProduct();
				pAssProduct.setAssGoods(pAssGoods);
				pAssProduct.setCreateDate(assProduct.getCreateDate());
				pAssProduct.setModifyDate(assProduct.getModifyDate());
				pAssProduct.setSpecification(assProduct.getSpecification());
				pAssProduct.setSourceType(assProduct.getSourceType());
				pAssProduct.setProduct(assProduct.getProduct());
				setValue(pAssProduct);
				assProductDao.persist(pAssProduct);
			}
		}
		return pAssCustomerRelation;
	}
	
	private void setValue(AssGoods assGoods) {
		if (assGoods == null) {
			return;
		}

		if (assGoods.isNew()) {
			if (StringUtils.isEmpty(assGoods.getSn())) {
				String sn;
				do {
					sn = snDao.generate(Sn.Type.ass_goods);
				} while (snExists(sn));
				assGoods.setSn(sn);
			}
		}
	}
	
	private void setValue(AssProduct assProduct) {
		if (assProduct == null) {
			return;
		}

		if (assProduct.isNew()) {
			AssGoods assGoods = assProduct.getAssGoods();
			if (assGoods != null && StringUtils.isNotEmpty(assGoods.getSn())) {
				String sn;
				int i = assProduct.hasSpecification() ? 1 : 0;
				do {
					sn = assGoods.getSn() + (i == 0 ? "" : "_" + i);
					i++;
				} while (assProductDao.snExists(sn));
				assProduct.setSn(sn);
			}
		}
	}
}