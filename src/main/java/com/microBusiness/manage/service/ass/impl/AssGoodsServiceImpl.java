package com.microBusiness.manage.service.ass.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.dao.ass.AssCardDao;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.dao.ass.AssGoodsDao;
import com.microBusiness.manage.dao.ass.AssListDao;
import com.microBusiness.manage.dao.ass.AssProductDao;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.SpecificationValueService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.ass.AssGoodsService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.DateUtils;

@Service("assistantGoodsServiceImpl")
public class AssGoodsServiceImpl extends BaseServiceImpl<AssGoods, Long> implements AssGoodsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private AssGoodsDao assGoodsDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "assProductDaoImpl")
	private AssProductDao assProductDao;
	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;
	@Resource(name = "assListDaoImpl")
	private AssListDao assListDao;
	@Resource
	private AssCustomerRelationDao assCustomerRelationDao;
	@Resource
	private WeChatService weChatService;
	@Resource
	private AssCardDao assCardDao;
	
	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return assGoodsDao.snExists(sn);
	}
	
	/**
	 * 添加有规格商品
	 */
	@Override
	public AssGoods save(AssGoods assGoods, AssCustomerRelation assCustomerRelation , List<String> specification) {
		Assert.notNull(assGoods);
		Assert.isTrue(assGoods.isNew());
		
		assGoods.setAssCustomerRelation(assCustomerRelation);
		setValue(assGoods);//生成助手商品id
		assGoodsDao.persist(assGoods);
		//每一个规格生成一个AssProduct
		for(String str : specification) {
			AssProduct assProduct = new AssProduct();
			assProduct.setAssGoods(assGoods);
			assProduct.setSpecification(str);
			setValue(assProduct);//生成assProductId
			assProductDao.persist(assProduct);
			
		}
		
		return assGoods;
	}

	/**
	 * 添加没有规格的商品
	 */
	@Override
	public AssGoods save(AssGoods assGoods, AssProduct assProduct , AssCustomerRelation assCustomerRelation) {
		Assert.notNull(assGoods);
		Assert.isTrue(assGoods.isNew());
		
		assProduct.setAssGoods(assGoods);
		assGoods.setAssCustomerRelation(assCustomerRelation);
		setValue(assGoods);//生成助手商品ID
		assGoodsDao.persist(assGoods);
		
		setValue(assProduct);
		assProductDao.persist(assProduct);
		return assGoods;
	}
	
	@Override
	public Page<AssGoods> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		return assGoodsDao.findPage(assChildMember, pageable);
	}

	/**
	 * 删除助手商品
	 */
	@Override
	public boolean deleteAssGoods(Long id) {
		AssGoods assGoods = assGoodsDao.find(id);
		//获取assProduct
		Set<AssProduct> assProducts = assGoods.getAssProducts();
		List<Long> ids = new ArrayList<Long>();
		for(AssProduct assProduct : assProducts) {
			ids.add(assProduct.getId());
		}
		//根据assProduct查询采购清单中是否包含该商品
		boolean bool = assListDao.assListItem(ids);
		//如果为false，表示不包含可以删除
		if(!bool) {
			assGoods.setDeleted(true);
			return true;
		}
		return false;
	}

	/**
	 * 修改商品
	 */
	@Override
	public AssGoods updateAssGoods(AssGoods assGoods,
			List<AssProduct> assProducts,  AssCustomerRelation assCustomerRelation) {
		Assert.notNull(assGoods);
		Assert.isTrue(!assGoods.isNew());
		Assert.notEmpty(assProducts);
		//获取商品信息
		AssGoods pAssGoods = assGoodsDao.find(assGoods.getId());
		pAssGoods.setImage(assGoods.getImage());
		pAssGoods.setName(assGoods.getName());
		pAssGoods.setUnit(assGoods.getUnit());
		pAssGoods.setLabels(assGoods.getLabels());
		//pAssGoods.setAssCustomerRelation(assCustomerRelation);
		/**
		 * 此处判断原来没有规格，修改时添加了规格，把原有的parduct也加上规格
		 */
		List<AssProduct> aProducts = assProductDao.findByList(pAssGoods);
		if(aProducts.size() == 1 && assProducts.size() >= 1 && (aProducts.get(0).getSpecification() == null || aProducts.get(0).getSpecification() == "")) {
			Iterator<AssProduct> iterator = assProducts.iterator();
			while (iterator.hasNext()) {
				AssProduct assProduct = iterator.next();
				if(assProduct.getId() != null) {
					iterator.remove();
				}else {
					if(aProducts.get(0).getSpecification() == null || aProducts.get(0).getSpecification() == "") {
						if(assProduct.getId() == null && assProduct.getSpecification() != null && assProduct.getSpecification() != "") {
							assProduct.setId(aProducts.get(0).getId());
							aProducts.get(0).setSpecification(assProduct.getSpecification());
							break;
						}
					}
				}
				
			}
			
		}
		for(AssProduct assProduct : assProducts) {
			if(assProduct.getExist() == AssProduct.Exist.delete && assProduct.getId() != null) {//如果exist为delete，则删除商品
				AssProduct pAssProduct = assProductDao.find(assProduct.getId());
				assProductDao.delete(pAssProduct);
			}else if(assProduct.getId() == null) {//如果id为null，表示没有assProduct，添加商品
				assProduct.setAssGoods(pAssGoods);
				setValue(assProduct);
				assProductDao.persist(assProduct);
			}else {//修改assProduct
				AssProduct pAssProduct = assProductDao.find(assProduct.getId());
				pAssProduct.setSpecification(assProduct.getSpecification());
				assProductDao.merge(pAssProduct);
			}
		}
		super.update(pAssGoods);
		return pAssGoods;
	}
	
	@Override
	public List<AssProduct> getSpecifications(Long id) {
		return assProductDao.findBySpecifications(id);
	}
	
	private AssProduct find(Collection<AssProduct> assProducts, final List<Integer> specificationValueIds) {
		if (CollectionUtils.isEmpty(assProducts) || CollectionUtils.isEmpty(specificationValueIds)) {
			return null;
		}

		return (AssProduct) CollectionUtils.find(assProducts, new Predicate() {
			public boolean evaluate(Object object) {
				AssProduct assProduct = (AssProduct) object;
				return assProduct != null && assProduct.getSpecificationValueIds() != null && assProduct.getSpecificationValueIds().equals(specificationValueIds);
			}
		});
	}
	
	private boolean exists(Collection<AssProduct> assProducts, final List<Integer> specificationValueIds) {
		return find(assProducts, specificationValueIds) != null;
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

	@Override
	public Page<AssGoods> findByList(AssCustomerRelation assCustomerRelation , String name , Pageable pageable) {
		return assGoodsDao.findByList(assCustomerRelation, name , pageable);
	}

	@Override
	public boolean findNameExist(AssCustomerRelation assCustomerRelation,
			AssGoods assGoods) {
		return assGoodsDao.findNameExist(assCustomerRelation, assGoods);
	}

	@Override
	public AssCustomerRelation copyShareAssGoods(AssCustomerRelation assCustomerRelation,AssChildMember assChildMember,String sn, Boolean synchronizationFla) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		
		//复制名片
		AssCard pAssCard=null;
		if (synchronizationFla) {
			pAssCard=new AssCard();
			AssCard assCard=assChildMember.getAssCard();
			pAssCard.setAssChildMember(assChildMember);
			pAssCard.setShareType(AssCard.ShareType.share);
			pAssCard.setName(assCard.getName());
			pAssCard.setPhone(assCard.getPhone());
			pAssCard.setCompanyName(assCard.getCompanyName());
			pAssCard.setPosition(assCard.getPosition());
			pAssCard.setEmail(assCard.getEmail());
			pAssCard.setWxNum(assCard.getWxNum());
			pAssCard.setProfiles(assCard.getProfiles());
			
			assCardDao.persist(pAssCard);
		}
		//复制供应商
		AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
		pAssCustomerRelation.setClientName(assCustomerRelation.getClientName());
		pAssCustomerRelation.setTheme(assCustomerRelation.getTheme());
		pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
		pAssCustomerRelation.setAssChildMember(assChildMember);
		pAssCustomerRelation.setSource(assCustomerRelation);
		pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.share);
		pAssCustomerRelation.setSourceType(assCustomerRelation.getSourceType());
		pAssCustomerRelation.setSn(sn);
		pAssCustomerRelation.setAssCard(pAssCard);
		pAssCustomerRelation.setType(AssCustomerRelation.Type.many);
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

	@Override
	public AssCustomerRelation copyNoShareAssGoods(AssCustomerRelation assCustomerRelation,AssChildMember assChildMember,String mClientName , String mTheme) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		//复制供应商
		AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
		pAssCustomerRelation.setClientName(mClientName);
		pAssCustomerRelation.setTheme(mTheme);
		pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
		pAssCustomerRelation.setAssChildMember(assChildMember);
		pAssCustomerRelation.setSource(assCustomerRelation);
		pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
		pAssCustomerRelation.setSourceType(assCustomerRelation.getSourceType());
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

	@Override
	public AssCustomerRelation copyToNoShareAssGoods(
			AssCustomerRelation assCustomerRelation,
			AssChildMember assChildMember) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		//复制供应商
		AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
		pAssCustomerRelation.setClientName(assCustomerRelation.getClientName());
		AssCustomerRelation relation = assCustomerRelationDao.inviteNameExists(assCustomerRelation.getClientName(), assCustomerRelation.getTheme(), assChildMember);
		if(null != relation) {
			int num = relation.getNumberOfCopy();
			int pNum = num+1;
			if(pNum == 1) {
				pAssCustomerRelation.setTheme(assCustomerRelation.getTheme()+"("+"副本"+")");
			}
			if(pNum > 1) {
				pAssCustomerRelation.setTheme(assCustomerRelation.getTheme()+"("+"副本"+num+")");
			}
			relation.setNumberOfCopy(pNum);
			assCustomerRelationDao.merge(relation);
		}else {
			pAssCustomerRelation.setTheme(assCustomerRelation.getTheme());
		}
		pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
		pAssCustomerRelation.setAssChildMember(assChildMember);
		pAssCustomerRelation.setSource(assCustomerRelation);
		pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
		pAssCustomerRelation.setSourceType(AssCustomerRelation.SourceType.MOBILE);
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
			pAssGoods.setSourceType(AssGoods.SourceType.MOBILE);
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
				pAssProduct.setSourceType(AssProduct.SourceType.MOBILE);
				pAssProduct.setProduct(assProduct.getProduct());
				setValue(pAssProduct);
				assProductDao.persist(pAssProduct);
			}
		}
		return pAssCustomerRelation;
	}
	
	@Override
	public AssGoods copyGoods(AssCustomerRelation assCustomerRelation,AssGoods assGoods) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assGoods);
		Assert.isTrue(!assGoods.isNew());
		//复制商品
		AssGoods pAssGoods = new AssGoods();
		pAssGoods.setName(assGoods.getName());
		pAssGoods.setImage(assGoods.getImage());
		pAssGoods.setUnit(assGoods.getUnit());
		pAssGoods.setCreateDate(assGoods.getCreateDate());
		pAssGoods.setAssCustomerRelation(assCustomerRelation);
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
		return pAssGoods;
	}

	@Override
	public List<AssGoods> findByList(AssCustomerRelation assCustomerRelation) {
		return assGoodsDao.findByList(assCustomerRelation);
	}

	@Override
	public AssGoods copySingleShareAssAssGoods(AssCustomerRelation assCustomerRelation,
			AssGoods assGoods, String sn) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assGoods);
		Assert.isTrue(!assGoods.isNew());
		Assert.notNull(sn);
		//复制供应商
		AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
		pAssCustomerRelation.setClientName(assCustomerRelation.getClientName());
		pAssCustomerRelation.setTheme(assCustomerRelation.getTheme());
		pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
		pAssCustomerRelation.setAssChildMember(assCustomerRelation.getAssChildMember());
		pAssCustomerRelation.setSource(assCustomerRelation);
		pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.share);
		pAssCustomerRelation.setSourceType(assCustomerRelation.getSourceType());
		pAssCustomerRelation.setType(AssCustomerRelation.Type.single);
		pAssCustomerRelation.setAssGoodDirectory(assCustomerRelation.getAssGoodDirectory());
		pAssCustomerRelation.setAdminName(assCustomerRelation.getAdminName());
		assCustomerRelationDao.persist(pAssCustomerRelation);
		
		//复制商品
		AssGoods pAssGoods = new AssGoods();
		pAssGoods.setName(assGoods.getName());
		pAssGoods.setImage(assGoods.getImage());
		pAssGoods.setUnit(assGoods.getUnit());
		pAssGoods.setCreateDate(assGoods.getCreateDate());
		pAssGoods.setModifyDate(assGoods.getModifyDate());
		pAssGoods.setAssCustomerRelation(pAssCustomerRelation);
		pAssGoods.setLabels(assGoods.getLabels());
		pAssGoods.setSn(sn);
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
		return pAssGoods;
	}
	
	@Override
	public AssGoods copySingleNoShareAssAssGoods(AssCustomerRelation assCustomerRelation, AssGoods assGoods,AssChildMember assChildMember) {
		Assert.notNull(assCustomerRelation);
		Assert.isTrue(!assCustomerRelation.isNew());
		Assert.notNull(assGoods);
		Assert.isTrue(!assGoods.isNew());
		Assert.notNull(assChildMember);
		Assert.isTrue(!assChildMember.isNew());
		
		//判断要复制的供应商是否存在，如果不存在就复制
		AssCustomerRelation relation = assCustomerRelationDao.inviteNameExists(assCustomerRelation.getClientName(), assCustomerRelation.getTheme(), assChildMember);
		AssGoods pAssGoods = new AssGoods();
		if(null == relation) {
			//复制供应商
			AssCustomerRelation pAssCustomerRelation = new AssCustomerRelation();
			pAssCustomerRelation.setClientName(assCustomerRelation.getClientName());
			pAssCustomerRelation.setTheme(assCustomerRelation.getTheme());
			pAssCustomerRelation.setProfiles(assCustomerRelation.getProfiles());
			pAssCustomerRelation.setAssChildMember(assChildMember);
			pAssCustomerRelation.setSource(assCustomerRelation);
			pAssCustomerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
			pAssCustomerRelation.setSourceType(AssCustomerRelation.SourceType.MOBILE);
			pAssCustomerRelation.setAssGoodDirectory(assCustomerRelation.getAssGoodDirectory());
			pAssCustomerRelation.setAdminName(assCustomerRelation.getAdminName());
			assCustomerRelationDao.persist(pAssCustomerRelation);
			
			//复制商品
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
			pAssGoods.setSourceType(AssGoods.SourceType.MOBILE);
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
				pAssProduct.setSourceType(AssProduct.SourceType.MOBILE);
				pAssProduct.setProduct(assProduct.getProduct());
				setValue(pAssProduct);
				assProductDao.persist(pAssProduct);
			}
		}else {
			//复制商品
			AssGoods findAssGoods = assGoodsDao.findNameExist(relation, assGoods.getName());
			if(null != findAssGoods) {
				findAssGoods.setDeleted(true);
			}
			pAssGoods.setName(assGoods.getName());
			pAssGoods.setImage(assGoods.getImage());
			pAssGoods.setUnit(assGoods.getUnit());
			pAssGoods.setCreateDate(assGoods.getCreateDate());
			pAssGoods.setModifyDate(assGoods.getModifyDate());
			pAssGoods.setAssCustomerRelation(relation);
			pAssGoods.setLabels(assGoods.getLabels());
			pAssGoods.setDetailsDescription(assGoods.getDetailsDescription());
			pAssGoods.setDetailsImage(assGoods.getDetailsImage());
			pAssGoods.setSource(assGoods);
			pAssGoods.setSourceType(AssGoods.SourceType.MOBILE);
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
				pAssProduct.setSourceType(AssProduct.SourceType.MOBILE);
				pAssProduct.setProduct(assProduct.getProduct());
				setValue(pAssProduct);
				assProductDao.persist(pAssProduct);
			}
			
		}
		return pAssGoods;
	}
	
	@Override
	public AssGoods findBySn(String sn) {
		// TODO Auto-generated method stub
		return assGoodsDao.findBySn(sn);
	}
	
	@Override
	public AssGoods findBySource(AssGoods assGoods, AssChildMember assChildMember) {
		// TODO Auto-generated method stub
		return assGoodsDao.findBySource(assGoods, assChildMember);
	}
	
	@Override
	public BufferedImage getPoster(AssGoods assGoods, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String path=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String path=request.getSession().getServletContext().getRealPath("/");
		
		List<String> imageList=assGoods.getImage();
		BufferedImage goodImage=null;
		String imgUrl="";
		if (imageList.size()>0) {
			imgUrl=imageList.get(0);
			goodImage=this.getGoodImage(imgUrl);
		}else {
			goodImage=ImageIO.read(new File(path+"/resources/poster/images/wu.png"));
		}
		BufferedImage qrCode=this.qrCode(assGoods, request, response);
		int width=750;
		int height=1206;
		BufferedImage image=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics(); 
		//抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,image.getWidth(),image.getHeight());//填充整个屏幕
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
		//商品图片
		g.drawImage(goodImage, 0, 0,image.getWidth(),image.getWidth(),null);
        //商品名称        
        Font Dfont = new Font("黑体", Font.BOLD, (int)(width*0.048));
        g.setFont(Dfont);
        // 设置大字体  
		FontRenderContext context = g.getFontRenderContext();  
		// 获取字体的像素范围对象  
		Rectangle2D stringBounds = Dfont.getStringBounds(assGoods.getName(), context);  
		double fontWidth = stringBounds.getWidth();  
		float x=(image.getWidth()-(int)fontWidth)/2;
		float y=(image.getHeight()*0.67f);
		g.setColor(Color.BLACK);
		g.drawString(assGoods.getName(), x, y);
		
		//标签
		List<BufferedImage> labelImages=new ArrayList<>();
		List<AssGoods.Label> labels=assGoods.getLabels();
		for (AssGoods.Label label : labels) {
			String imageName="";
			switch (label) {
			case newProducts:
				imageName="xinping.png";
				break;
			case selling:
				imageName="remai.png";
				break;
			case promotions:
				imageName="chuxiao.png";
				break;
			case specialOffer:
				imageName="tehui.png";
				break;
			case popularity:
				imageName="renqi.png";
				break;
			case explosions:
				imageName="baokuan.png";
				break;
			}
			if (StringUtils.isBlank(imageName)) {
				break;
			}
			String labelPath=path+"/resources/poster/images/"+imageName;
			BufferedImage labelImage=ImageIO.read(new File(labelPath));
			labelImages.add(labelImage);
		}
		int labelWidth=(int) (image.getWidth()*0.08);
		int labelHeight=(int) (image.getHeight()*0.025);
		int labelX=(int) ((image.getWidth()-(labelWidth+image.getWidth()*0.013)*labelImages.size())/2);
		int labelY=(int) (image.getHeight()*0.705);
		for (BufferedImage labelImage : labelImages) {
			g.drawImage(labelImage, labelX, labelY,labelWidth,labelHeight,null);
			labelX+=labelWidth+image.getWidth()*0.013;
		}
		
		//
		Dfont = new Font("黑体", Font.PLAIN, (int)(width*0.037));
		g.setFont(Dfont);
		x=(float)(image.getWidth()*0.16);
		y=(float)(image.getHeight()*0.85);
		g.drawString("更多商品信息", x, y);
		g.drawString("请长按识别小程序", x, y+(int)(Dfont.getSize()*2.1));
		
		Date now=new Date();
		String date=DateUtils.convertToString(now, "yyyy年MM月dd日 HH:mm");
		g.setColor(new Color(220, 220, 220));
		g.drawString(date, x, y+(int)(Dfont.getSize()*4));
		
		g.drawImage(qrCode, (int)(image.getWidth()*0.62), (int)(image.getHeight()*0.80),(int)(image.getWidth()*0.22),(int)(image.getWidth()*0.22),null);
		
		g.setStroke(new BasicStroke(1.0f));  
	     
	    g.setColor(new Color(238, 238, 238));
	    
	    //计算直线的两个点坐标
	    int x1=(int) (image.getWidth()*0.08);
	    int y1=(int) (image.getHeight()*0.76);
	    int x2=(int) (image.getWidth()*0.92);
	    int y2=(int) (image.getHeight()*0.76);
	    
	    g.drawLine(x1, y1, x2, y2);  
		g.dispose();
		
		return image;
	}
	
	/**
	 * 下载图片
	 * @param imageUrl
	 * @return
	 */
	public BufferedImage getGoodImage(String imageUrl) {
		BufferedImage image=null;
		 URL url = null;  
		 try {
			url = new URL(imageUrl);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			image=ImageIO.read(dataInputStream);
			dataInputStream.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	/**
	 * 获取二维码
	 * @param assGoods
	 * @param request
	 * @param response
	 */
	public BufferedImage qrCode(AssGoods assGoods,HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		BufferedImage image=null;
		 CloseableHttpResponse httpResponse = null;
			try {
				String accessToken = weChatService.getAssSmallGlobalToken();

				httpResponse = ApiSmallUtils.getInputStream("pages/goodsShare/goodsShare?sn="+assGoods.getSn(), accessToken, request, response);
				logger.info("生成助手二维码结果："+httpResponse);
				HttpEntity httpEntity = httpResponse.getEntity();
				
				Header head = httpEntity.getContentType();
				
				String str = head.getValue();
				if (str.indexOf("application/json") != -1) {
					return null;
				}

				InputStream inputStream = null;

				httpEntity.getContentType();
				if (httpEntity != null) {
					inputStream = httpEntity.getContent();
				}
				image=ImageIO.read(inputStream);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally{
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
			return image;
	}

}
