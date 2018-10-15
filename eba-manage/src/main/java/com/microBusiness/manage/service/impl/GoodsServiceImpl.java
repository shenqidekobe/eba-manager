/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.*;
import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.GoodsCenter.Label;
import com.microBusiness.manage.entity.GoodsImportImageInfo.Second;
import com.microBusiness.manage.entity.SpecificationItem.Entry;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.SpecificationValueService;
import com.microBusiness.manage.service.StaticService;
import com.microBusiness.manage.service.ass.AssGoodDirectoryService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@Service("goodsServiceImpl")
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long> implements
		GoodsService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GoodsServiceImpl.class);

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;
	@Resource(name = "brandDaoImpl")
	private BrandDao brandDao;
	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;
	@Resource(name = "tagDaoImpl")
	private TagDao tagDao;
	@Resource(name = "attributeDaoImpl")
	private AttributeDao attributeDao;
	@Resource(name = "stockLogDaoImpl")
	private StockLogDao stockLogDao;
	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource
	private NeedProductDao needProductDao;

	@Resource
	private SupplierProductDao supplierProductDao;

	@Resource
	private SupplierDao supplierDao;

	@Resource
	private SpecificationDao specificationDao;

	@Resource
	private GoodsImportLogDao goodsImportLogDao;
	@Resource
	private GoodsImportInfoDao goodsImportInfoDao;
	@Resource(name = "productCategoryImportDaoImpl")
	private ProductCategoryImportDao productCategoryImportDao;
	@Resource(name = "productImportDaoImpl")
	private ProductImportDao productImportDao;
	@Resource(name = "specificationImportDaoImpl")
	private SpecificationImportDao specificationImportDao;
	@Resource
	private AssGoodDirectoryService assGoodDirectoryService;
	@Resource
	private AssUpdateTipsDao assUpdateTipsDao;
	@Resource
	private GoodsImportImageInfoDao goodsImportImageInfoDao;
	@Resource
	private NeedShopProductDao needShopProductDao;
	@Resource
	private SupplyNeedDao supplyNeedDao;
	@Resource
	private SupplierSupplierDao supplierSupplierDao;
	@Resource
	private SupplierNeedProductDao supplierNeedProductDao;

	@Transactional(readOnly = true)
	public boolean snExists(String sn, Supplier supplier) {
		return goodsDao.snExists(sn, supplier);
	}

	@Transactional(readOnly = true)
	public Goods findBySn(String sn, Supplier supplier) {
		return goodsDao.findBySn(sn, supplier);
	}

	@Transactional(readOnly = true)
	public List<Goods> findList(Goods.Type type,
			ProductCategory productCategory, Brand brand, Promotion promotion,
			Tag tag, Map<Attribute, String> attributeValueMap,
			BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion,
			Goods.OrderType orderType, Integer count, List<Filter> filters,
			List<Order> orders) {
		return goodsDao.findList(type, productCategory, brand, promotion, tag,
				attributeValueMap, startPrice, endPrice, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert, hasPromotion, orderType,
				count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "goods", condition = "#useCache")
	public List<Goods> findList(Goods.Type type, Long productCategoryId,
			Long brandId, Long promotionId, Long tagId,
			Map<Long, String> attributeValueMap, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Boolean hasPromotion, Goods.OrderType orderType, Integer count,
			List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao
				.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		Brand brand = brandDao.find(brandId);
		if (brandId != null && brand == null) {
			return Collections.emptyList();
		}
		Promotion promotion = promotionDao.find(promotionId);
		if (promotionId != null && promotion == null) {
			return Collections.emptyList();
		}
		Tag tag = tagDao.find(tagId);
		if (tagId != null && tag == null) {
			return Collections.emptyList();
		}
		Map<Attribute, String> map = new HashMap<Attribute, String>();
		if (attributeValueMap != null) {
			for (Map.Entry<Long, String> entry : attributeValueMap.entrySet()) {
				Attribute attribute = attributeDao.find(entry.getKey());
				if (attribute != null) {
					map.put(attribute, entry.getValue());
				}
			}
		}
		if (MapUtils.isNotEmpty(attributeValueMap) && MapUtils.isEmpty(map)) {
			return Collections.emptyList();
		}
		return goodsDao.findList(type, productCategory, brand, promotion, tag,
				map, startPrice, endPrice, isMarketable, isList, isTop,
				isOutOfStock, isStockAlert, hasPromotion, orderType, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Goods> findList(ProductCategory productCategory,
			Boolean isMarketable, Goods.GenerateMethod generateMethod,
			Date beginDate, Date endDate, Integer first, Integer count) {
		return goodsDao.findList(productCategory, isMarketable, generateMethod,
				beginDate, endDate, first, count);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Goods.Type type,
			ProductCategory productCategory, Brand brand, Promotion promotion,
			Tag tag, Map<Attribute, String> attributeValueMap,
			BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion,
			Goods.OrderType orderType, Pageable pageable) {
		return goodsDao.findPage(type, productCategory, brand, promotion, tag,
				attributeValueMap, startPrice, endPrice, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert, hasPromotion, orderType,
				pageable);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable) {
		return goodsDao.findPage(rankingType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Member member, Pageable pageable) {
		return goodsDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Goods.Type type, Member favoriteMember,
			Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isOutOfStock, Boolean isStockAlert) {
		return goodsDao.count(type, favoriteMember, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert);
	}

	public long viewHits(Long id) {
		Assert.notNull(id);

		Ehcache cache = cacheManager.getEhcache(Goods.HITS_CACHE_NAME);
		Element element = cache.get(id);
		Long hits;
		if (element != null) {
			hits = (Long) element.getObjectValue() + 1;
		} else {
			Goods goods = goodsDao.find(id);
			if (goods == null) {
				return 0L;
			}
			hits = goods.getHits() + 1;
		}
		cache.put(new Element(id, hits));
		return hits;
	}

	public void addHits(Goods goods, long amount) {
		Assert.notNull(goods);
		Assert.state(amount >= 0);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(goodsDao.getLockMode(goods))) {
			goodsDao.refresh(goods, LockModeType.PESSIMISTIC_WRITE);
		}

		Calendar nowCalendar = Calendar.getInstance();
		Calendar weekHitsCalendar = DateUtils.toCalendar(goods
				.getWeekHitsDate());
		Calendar monthHitsCalendar = DateUtils.toCalendar(goods
				.getMonthHitsDate());
		if (nowCalendar.get(Calendar.YEAR) > weekHitsCalendar
				.get(Calendar.YEAR)
				|| nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar
						.get(Calendar.WEEK_OF_YEAR)) {
			goods.setWeekHits(amount);
		} else {
			goods.setWeekHits(goods.getWeekHits() + amount);
		}
		if (nowCalendar.get(Calendar.YEAR) > monthHitsCalendar
				.get(Calendar.YEAR)
				|| nowCalendar.get(Calendar.MONTH) > monthHitsCalendar
						.get(Calendar.MONTH)) {
			goods.setMonthHits(amount);
		} else {
			goods.setMonthHits(goods.getMonthHits() + amount);
		}
		goods.setHits(goods.getHits() + amount);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goodsDao.flush();
	}

	public void addSales(Goods goods, long amount) {
		Assert.notNull(goods);
		Assert.state(amount >= 0);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(goodsDao.getLockMode(goods))) {
			goodsDao.refresh(goods, LockModeType.PESSIMISTIC_WRITE);
		}

		Calendar nowCalendar = Calendar.getInstance();
		Calendar weekSalesCalendar = DateUtils.toCalendar(goods
				.getWeekSalesDate());
		Calendar monthSalesCalendar = DateUtils.toCalendar(goods
				.getMonthSalesDate());
		if (nowCalendar.get(Calendar.YEAR) > weekSalesCalendar
				.get(Calendar.YEAR)
				|| nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar
						.get(Calendar.WEEK_OF_YEAR)) {
			goods.setWeekSales(amount);
		} else {
			goods.setWeekSales(goods.getWeekSales() + amount);
		}
		if (nowCalendar.get(Calendar.YEAR) > monthSalesCalendar
				.get(Calendar.YEAR)
				|| nowCalendar.get(Calendar.MONTH) > monthSalesCalendar
						.get(Calendar.MONTH)) {
			goods.setMonthSales(amount);
		} else {
			goods.setMonthSales(goods.getMonthSales() + amount);
		}
		goods.setSales(goods.getSales() + amount);
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.lazy);
		goodsDao.flush();
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods, Product product, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		switch (goods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product
					.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product
					.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(goods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());

		goods.setBarCode(product.getBarCode());

		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setSpecificationItems(null);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		// TODO: 2017/1/23 添加供应商
		goods.setSupplier(operator.getSupplier());
		setValue(goods, operator.getSupplier());
		goodsDao.persist(goods);

		setValue(product,goods.getSupplier());
		productDao.persist(product);
		stockIn(product, operator);

		return goods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods, List<Product> products, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods
				.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null
						|| !product.isNew()
						|| !product.hasSpecification()
						|| !set.add(product.getSpecificationValueIds())
						|| !specificationValueService.isValid(
								specificationItems,
								product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products,
				new Predicate() {
					public boolean evaluate(Object object) {
						Product product = (Product) object;
						return product != null && product.getIsDefault();
					}
				});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		for (Product product : products) {
			switch (goods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product
						.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product
						.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(goods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());

		goods.setBarCode(defaultProduct.getBarCode());

		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		// TODO: 2017/1/23 添加供应商
		goods.setSupplier(operator.getSupplier());

		setValue(goods, operator.getSupplier());
		goodsDao.persist(goods);

		for (Product product : products) {
			setValue(product,goods.getSupplier());
			productDao.persist(product);
			stockIn(product, operator);
		}

		return goods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, Product product, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		Goods pGoods = goodsDao.find(goods.getId());
		switch (pGoods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product
					.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product
					.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(pGoods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				productDao.remove(pProduct);
			}
			if (product.getStock() == null) {
				product.setStock(999999999);
			}
			setValue(product,pGoods.getSupplier());
			productDao.persist(product);
			stockIn(product, operator);
		} else {
			Product defaultProduct = pGoods.getDefaultProduct();
			defaultProduct.setPrice(product.getPrice());
			defaultProduct.setCost(product.getCost());
			defaultProduct.setBarCode(product.getBarCode());
			defaultProduct.setMarketPrice(product.getMarketPrice());
			defaultProduct.setRewardPoint(product.getRewardPoint());
			defaultProduct.setExchangePoint(product.getExchangePoint());
			defaultProduct.setStock(product.getStock());
			defaultProduct.setMinOrderQuantity(product.getMinOrderQuantity());
			defaultProduct.setAddValue(product.getAddValue());
			defaultProduct.setProxyPrice(product.getProxyPrice());
		}

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());

		goods.setBarCode(product.getBarCode());

		setValue(goods, operator.getSupplier());
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
				"scoreCount", "hits", "weekHits", "monthHits", "sales",
				"weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
				"weekSalesDate", "monthSalesDate", "generateMethod", "reviews",
				"consultations", "favoriteMembers", "products", "supplier");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);

		// 更新标记
		List<AssGoodDirectory> goodDirectories = assGoodDirectoryService
				.findList(pGoods);
		for (AssGoodDirectory goodDirectory : goodDirectories) {
			List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(null,
					goodDirectory, null,AssUpdateTips.Type.companyGoods);
			for (AssUpdateTips updateTips : assUpdateTips) {
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				assUpdateTipsDao.merge(updateTips);
			}
		}

		return pGoods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, List<Product> products, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods
				.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null
						|| !product.isNew()
						|| !product.hasSpecification()
						|| !set.add(product.getSpecificationValueIds())
						|| !specificationValueService.isValid(
								specificationItems,
								product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products,
				new Predicate() {
					public boolean evaluate(Object object) {
						Product product = (Product) object;
						return product != null && product.getIsDefault();
					}
				});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		Goods pGoods = goodsDao.find(goods.getId());
		for (Product product : products) {
			switch (pGoods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product
						.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product
						.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(pGoods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				if (!exists(products, pProduct.getSpecificationValueIds())) {
					productDao.remove(pProduct);
				}
			}
			for (Product product : products) {
				Product pProduct = find(pGoods.getProducts(),
						product.getSpecificationValueIds());
				if (pProduct != null) {
					pProduct.setPrice(product.getPrice());
					pProduct.setCost(product.getCost());
					pProduct.setMarketPrice(product.getMarketPrice());

					pProduct.setBarCode(product.getBarCode());
					
					pProduct.setRewardPoint(product.getRewardPoint());
					pProduct.setExchangePoint(product.getExchangePoint());
					pProduct.setIsDefault(product.getIsDefault());
					pProduct.setSpecificationValues(product
							.getSpecificationValues());
					pProduct.setStock(product.getStock());
					pProduct.setMinOrderQuantity(product.getMinOrderQuantity());
					pProduct.setAddValue(product.getAddValue());
				} else {
					if (product.getStock() == null) {
						throw new IllegalArgumentException();
					}
					setValue(product,pGoods.getSupplier());
					productDao.persist(product);
					stockIn(product, operator);
				}
			}
		} else {
			productDao.remove(pGoods.getDefaultProduct());
			for (Product product : products) {
				if (product.getStock() == null) {
					throw new IllegalArgumentException();
				}
				setValue(product,pGoods.getSupplier());
				productDao.persist(product);
				stockIn(product, operator);
			}
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());
		goods.setBarCode(defaultProduct.getBarCode());
		setValue(goods, operator.getSupplier());
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
				"scoreCount", "hits", "weekHits", "monthHits", "sales",
				"weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
				"weekSalesDate", "monthSalesDate", "generateMethod", "reviews",
				"consultations", "favoriteMembers", "products", "supplier");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);

		// 更新标记
		List<AssGoodDirectory> goodDirectories = assGoodDirectoryService
				.findList(pGoods);
		for (AssGoodDirectory goodDirectory : goodDirectories) {
			List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(null,
					goodDirectory, null,AssUpdateTips.Type.companyGoods);
			for (AssUpdateTips updateTips : assUpdateTips) {
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				assUpdateTipsDao.merge(updateTips);
			}
		}

		return pGoods;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods, Supplier suplier) {
		Assert.notNull(goods);

		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		setValue(goods, suplier);
		return super.save(goods);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, Supplier suplier) {
		Assert.notNull(goods);

		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		setValue(goods, suplier);
		return super.update(goods);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, String... ignoreProperties) {
		return super.update(goods, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Goods goods) {
		staticService.delete(goods);
		super.delete(goods);
	}

	private void setValue(Goods goods, Supplier supplier) {
		if (goods == null) {
			return;
		}

		productImageService.generate(goods.getProductImages());
		if (StringUtils.isEmpty(goods.getImage())
				&& StringUtils.isNotEmpty(goods.getThumbnail())) {
			goods.setImage(goods.getThumbnail());
		}
		if (goods.isNew()) {
			if (StringUtils.isEmpty(goods.getSn())) {
				String sn;
				do {
					sn = snDao.generate(Sn.Type.goods);
				} while (snExists(sn, supplier));
				goods.setSn(sn);
			}
		}
	}

	private void setValue(GoodsImportInfo goods, Supplier supplier) {
		if (goods == null) {
			return;
		}

		productImageService.generate(goods.getProductImages());
		if (StringUtils.isEmpty(goods.getImage())
				&& StringUtils.isNotEmpty(goods.getThumbnail())) {
			goods.setImage(goods.getThumbnail());
		}
		if (goods.isNew()) {
			if (StringUtils.isEmpty(goods.getSn())) {
				String sn;
				do {
					sn = snDao.generate(Sn.Type.goods);
				} while (snExists(sn, supplier));
				goods.setSn(sn);
			}
		}
	}

	private void setValue(Product product, Supplier supplier) {
		if (product == null) {
			return;
		}

		if (product.isNew()) {
			Goods goods = product.getGoods();
			if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
				String sn;
				int i = product.hasSpecification() ? 1 : 0;
				do {
					sn = goods.getSn() + (i == 0 ? "" : "_" + i);
					i++;
				} while (productDao.snExists(sn,supplier));
				product.setSn(sn);
			}
		}
	}

	private void setValue(ProductImport product) {
		if (product == null) {
			return;
		}

		if (product.isNew()) {
			GoodsImportInfo goods = product.getGoodsImportInfo();
			if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
				String sn;
				int i = product.hasSpecification() ? 1 : 0;
				do {
					sn = goods.getSn() + (i == 0 ? "" : "_" + i);
					i++;
				} while (productImportDao.snExists(sn));
				product.setSn(sn);
			}
		}
	}

	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Assert.notNull(price);

		Setting setting = SystemUtils.getSetting();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return defaultMarketPriceScale != null ? setting.setScale(price
				.multiply(new BigDecimal(String
						.valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
	}

	private long calculateDefaultRewardPoint(BigDecimal price) {
		Assert.notNull(price);

		Setting setting = SystemUtils.getSetting();
		Double defaultPointScale = setting.getDefaultPointScale();
		return defaultPointScale != null ? price.multiply(
				new BigDecimal(String.valueOf(defaultPointScale))).longValue()
				: 0L;
	}

	private Product find(Collection<Product> products,
			final List<Integer> specificationValueIds) {
		if (CollectionUtils.isEmpty(products)
				|| CollectionUtils.isEmpty(specificationValueIds)) {
			return null;
		}

		return (Product) CollectionUtils.find(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null
						&& product.getSpecificationValueIds() != null
						&& product.getSpecificationValueIds().equals(
								specificationValueIds);
			}
		});
	}

	private boolean exists(Collection<Product> products,
			final List<Integer> specificationValueIds) {
		return find(products, specificationValueIds) != null;
	}

	private void stockIn(Product product, Admin operator) {
		if (product == null || product.getStock() == null
				|| product.getStock() <= 0) {
			return;
		}

		StockLog stockLog = new StockLog();
		stockLog.setType(StockLog.Type.stockIn);
		stockLog.setInQuantity(product.getStock());
		stockLog.setOutQuantity(0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator);
		stockLog.setMemo(null);
		stockLog.setProduct(product);
		stockLogDao.persist(stockLog);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Goods.Type type,
			ProductCategory productCategory, Brand brand, Promotion promotion,
			Tag tag, Map<Attribute, String> attributeValueMap,
			BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion,
			Goods.OrderType orderType, Pageable pageable, Supplier supplier,
			Boolean sourceFlag, Boolean branchFlag) {
		return goodsDao.findPage(type, productCategory, brand, promotion, tag,
				attributeValueMap, startPrice, endPrice, isMarketable, isList,
				isTop, isOutOfStock, isStockAlert, hasPromotion, orderType,
				pageable, supplier, sourceFlag, branchFlag);
	}

	@Override
	public Page<Goods> findBySupplierSupplierPage(List<Long> goodIds,
			Pageable pageable, Long productCategoryId) {
		return goodsDao.findBySupplierSupplierPage(goodIds, pageable,
				productCategoryId);
	}

	/**
	 * 获取正式供应的商品
	 * 
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @return
	 */
	@Override
	public List<Goods> getGoodsByFormal(Long needId, Long supplierId,
			String keywords, Integer isMarketable, Pageable pageable) {
		List<Goods> result = goodsDao.getGoodsByFormal(needId, supplierId,
				keywords, isMarketable, pageable, null);
		return result;
	}

	/**
	 * 处理过的正式供应商品
	 * 
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @param pageable
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getFormalGoodsDealed(Long needId,
			Long supplierId, String keywords, Integer isMarketable,
			Pageable pageable, SupplyType supplyType, Long relationId) {
		Supplier bySupplier = supplierDao.getSupplierByNeed(needId);
		List<Goods> goods = null;
		if (supplyType == SupplyType.formal) {
			goods = goodsDao.getGoodsByFormal(needId, supplierId, keywords,
					isMarketable, pageable, relationId);
		} else {
			goods = goodsDao.getGoodsByTemporary(needId, supplierId, keywords,
					isMarketable, pageable, relationId);
		}
		return this.dealFormalGoods(goods, needId, supplierId,
				bySupplier.getId(), supplyType);
	}

	/**
	 * 处理过的临时供应商品
	 * 
	 * @param needId
	 * @param supplierId
	 * @param keywords
	 * @param isMarketable
	 * @param pageable
	 * @return
	 */
	@Override
	public Set<Map<String, Object>> getTemporaryGoodsDealed(Long needId,
			Long supplierId, String keywords, Integer isMarketable,
			Pageable pageable) {
		List<Goods> goods = needProductDao.findByKerword(needId, supplierId,
				keywords, isMarketable, pageable);

		return this.dealTemporaryGoods(goods, supplierId, needId);
	}

	private List<Map<String, Object>> dealFormalGoods(List<Goods> goods,
			Long needId, Long supplierId, Long bySupplierId,
			SupplyType supplyType) {
		List<Map<String, Object>> returnGoods = new ArrayList<Map<String, Object>>();

		for (Goods good : goods) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodsId", good.getId());
			map.put("name", good.getName());
			map.put("caption", good.getCaption());
			map.put("keyword", good.getKeyword());
			map.put("image", good.getImage());
			map.put("introduction", good.getIntroduction());
			map.put("specificationItems", good.getSpecificationItems());
			map.put("productImages", good.getProductImages());
			Set<Product> products2 = good.getProducts();
			map.put("productsSize", products2.size());
			map.put("hasSpecification", good.hasSpecification());
			SupplierSupplier supplierSupplier = good.getSupplierSupplier();
			if (supplierSupplier != null) {
				if (supplierSupplier.getStatus().equals(
						SupplierSupplier.Status.inTheSupply)) {
					map.put("supplierStatus", true);
				} else {
					map.put("supplierStatus", false);
				}
			} else {
				map.put("supplierStatus", true);
			}

			if (!good.hasSpecification()) {
				Product tempPro = products2.iterator().next();
				map.put("productId", tempPro.getId());
				if (supplyType.equals(SupplyType.temporary)) {
					NeedProduct needProduct = needProductDao.getProduct(
							tempPro, bySupplierId, needId,
							SupplyNeed.Status.SUPPLY);
					map.put("minOrderQuantity",
							needProduct.getMinOrderQuantity());
				} else {
					SupplierProduct supplierProduct = supplierProductDao
							.getProduct(tempPro.getId(), supplierId,
									bySupplierId,
									SupplierSupplier.Status.inTheSupply, needId);
					map.put("minOrderQuantity",
							supplierProduct.getMinOrderQuantity());
				}

			} else {
				List<Long> productIds = new ArrayList<>(products2.size());
				// 因为现在是所有商品规格供应，直接取
				for (Product product : products2) {
					productIds.add(product.getId());
				}
				map.put("productIds", productIds);
			}

			returnGoods.add(map);
		}
		return returnGoods;
	}

	private Set<Map<String, Object>> dealTemporaryGoods(List<Goods> goods,
			Long supplierId, Long needId) {
		Set<Map<String, Object>> returnGoods = new HashSet<Map<String, Object>>();

		for (Goods good : goods) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodsId", good.getId());
			map.put("name", good.getName());
			map.put("caption", good.getCaption());
			map.put("keyword", good.getKeyword());
			map.put("image", good.getImage());
			map.put("introduction", good.getIntroduction());
			map.put("specificationItems", good.getSpecificationItems());
			map.put("productImages", good.getProductImages());
			Set<Product> products2 = good.getProducts();
			map.put("productsSize", products2.size());
			map.put("hasSpecification", good.hasSpecification());

			if (!good.hasSpecification()) {
				Product tempPro = products2.iterator().next();
				map.put("productId", tempPro.getId());
				NeedProduct needProduct = needProductDao.getProduct(tempPro,
						supplierId, needId, SupplyNeed.Status.SUPPLY);
				map.put("minOrderQuantity", needProduct.getMinOrderQuantity());
			} else {
				List<Long> productIds = new ArrayList<>(products2.size());
				// 因为现在是所有商品规格供应，直接取
				for (Product product : products2) {
					productIds.add(product.getId());
				}
				map.put("productIds", productIds);
			}
			returnGoods.add(map);
		}
		return returnGoods;
	}

	@Override
	public List<Product> copySupplierGoods(SupplierSupplier supplierSupplier,
			List<Long> goodsList, Supplier supplier) {
		List<Product> newProductList = new ArrayList<Product>();
		for (Long id : goodsList) {
			ProductCategory productCategory = new ProductCategory();

			Goods good = goodsDao.find(id);
			ProductCategory category = good.getProductCategory();

			ProductCategory categoryExist = productCategoryDao
					.findBySourceAndSupplier(supplier, category,
							supplierSupplier);
			if (categoryExist == null) {
				productCategory.setName(category.getName());
				productCategory.setSeoTitle(category.getSeoTitle());
				productCategory.setSeoKeywords(category.getSeoKeywords());
				productCategory.setSeoDescription(category.getSeoDescription());
				productCategory.setTreePath(",");
				productCategory.setGrade(0);
				productCategory.setParent(null);
				productCategory.setSupplier(supplier);
				productCategory.setSource(category);
				productCategory.setCreateDate(category.getCreateDate());
				productCategory.setModifyDate(category.getModifyDate());
				productCategory.setVersion(category.getVersion());
				productCategory.setSupplierSupplier(supplierSupplier);
				productCategoryDao.persist(productCategory);

				Set<Specification> set = category.getSpecifications();
				for (Specification spec : set) {
					Specification specification = new Specification();
					specification.setCreateDate(spec.getCreateDate());
					specification.setModifyDate(spec.getModifyDate());
					specification.setName(spec.getName());
					specification.setOptions(spec.getOptions());
					if (categoryExist != null) {
						specification.setProductCategory(categoryExist);
					} else {
						specification.setProductCategory(productCategory);
					}
					specification.setSource(spec);
					specification.setSupplier(supplier);
					specificationDao.persist(specification);
				}
			}

			Goods goods = new Goods();
			super.copyProperties(good, goods, "sn", "id", "type",
					"productImages", "parameterValues", "promotions", "tags",
					"reviews", "consultations", "favoriteMembers", "products",
					"supplier", "supplierProducts", "supplierSupplier",
					"source");
			goods.setSupplier(supplier);
			goods.setSource(good);
			goods.setSupplierSupplier(supplierSupplier);
			goods.setType(Goods.Type.distribution);
			if (categoryExist != null) {
				goods.setProductCategory(categoryExist);
			} else {
				goods.setProductCategory(productCategory);
			}

			setValue(goods, supplier);
			goodsDao.persist(goods);

			Set<Product> products = good.getProducts();
			/**
			 * 2.3.0版本
			 * 企业供应商商品规格分开展示，此处需要修改(添加企业供应关系的时候选择某个商品的几个规格就复制几个规格)
			 */
			List<Long> productIds = new ArrayList<Long>();
			for(Product prs : products) {
				productIds.add(prs.getId());
			}
			List<Product> products2 = productDao.findList(supplierSupplier, productIds);
			for(Product pro : products2) {
				Product product = new Product();

				product.setPrice(pro.getPrice());
				product.setCost(pro.getCost());
				product.setMarketPrice(pro.getMarketPrice());
				product.setRewardPoint(pro.getRewardPoint());
				product.setExchangePoint(pro.getExchangePoint());
				product.setStock(pro.getStock());
				product.setAllocatedStock(pro.getAllocatedStock());
				product.setIsDefault(pro.getIsDefault());
				product.setGoods(goods);
				// product.setMinOrderQuantity(pro.getMinOrderQuantity());
				// product.setSupplyPrice(pro.getSupplyPrice());
				product.setSource(pro);
				setValue(product,goods.getSupplier());
				product.setSpecificationValues(pro.getSpecificationValues());
				productDao.persist(product);

				newProductList.add(product);
			}
			
		}
		return newProductList;
	}

	@Override
	public List<Goods> queryDistributionGoods(Supplier supplier,
			Long supplierSupplierId) {
		return goodsDao.queryDistributionGoods(supplier, supplierSupplierId);
	}

	@Override
	public void removeGood(Long[] ids, Supplier supplier) {
		for (Long id : ids) {
			Goods goods = goodsDao.find(id);
			if (goods.getSource() == null) {
				goodsDao.delete(goods);
			} else {
				ProductCategory category = goods.getProductCategory();
				List<Goods> list = goodsDao.findByProductCategoryId(supplier,
						category, goods.getSupplierSupplier());
				if (list.size() > 1) {
					goodsDao.delete(goods);
				} else {
					Set<Specification> specifications = category
							.getSpecifications();
					if (specifications != null) {
						for (Specification specification : specifications) {
							specificationDao.delete(specification);
						}
					}
					goodsDao.delete(goods);
					productCategoryDao.delete(category);

				}
			}
			// 添加更新标记
			List<AssGoodDirectory> goodDirectories = assGoodDirectoryService
					.findList(goods);
			for (AssGoodDirectory goodDirectory : goodDirectories) {
				List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(
						null, goodDirectory, null,AssUpdateTips.Type.companyGoods);
				for (AssUpdateTips updateTips : assUpdateTips) {
					updateTips
							.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
					assUpdateTipsDao.merge(updateTips);
				}
			}

			// 删除商品目录和good的关联关系
			goodsDao.removeDirectoryGoods(id);
		}

	}

	@Override
	public Page<Goods> findPage(Supplier supplier, String goodName,
			ProductCategory productCategory, Pageable pageable) {
		// TODO Auto-generated method stub
		return goodsDao.findPage(supplier, goodName, productCategory, pageable);
	}

	private boolean isthisExcel(Sheet sheet) {
		Row row1 = sheet.getRow(0);
		if (row1 == null) {
			return false;
		}

		Cell titleCell = row1.getCell(0);
		String title = this.getCellValue(titleCell);
		if (!title.equals("商品信息")) {
			return false;
		}

		Row row = sheet.getRow(1);
		if (row == null) {
			return false;
		}

		Cell snCell = row.getCell(0);
		String sn = this.getCellValue(snCell);
		if (!sn.equals("商品编号")) {
			return false;
		}

		Cell nameCell = row.getCell(1);
		String name = this.getCellValue(nameCell);
		if (!name.equals("* 商品名称")) {
			return false;
		}

		Cell captionCell = row.getCell(2);
		String caption = this.getCellValue(captionCell);
		if (!caption.equals("副标题")) {
			return false;
		}

		Cell oneProductCategoryCell = row.getCell(3);
		String oneProductCategory = this.getCellValue(oneProductCategoryCell);
		if (!oneProductCategory.equals("* 一级分类")) {
			return false;
		}

		Cell twoProductCategoryCell = row.getCell(4);
		String twoProductCategory = this.getCellValue(twoProductCategoryCell);
		if (!twoProductCategory.equals("二级分类")) {
			return false;
		}

		Cell threeProductCategoryCell = row.getCell(5);
		String threeProductCategory = this
				.getCellValue(threeProductCategoryCell);
		if (!threeProductCategory.equals("三级分类")) {
			return false;
		}

		Cell productNameCell = row.getCell(6);
		String productName = this.getCellValue(productNameCell);
		if (!productName.equals("规格名称")) {
			return false;
		}

		Cell oneProductCell = row.getCell(7);
		String oneProduct = this.getCellValue(oneProductCell);
		if (!oneProduct.equals("规格1")) {
			return false;
		}

		Cell twoProductCell = row.getCell(8);
		String twoProduct = this.getCellValue(twoProductCell);
		if (!twoProduct.equals("规格2")) {
			return false;
		}

		Cell threeProductCell = row.getCell(9);
		String threeProduct = this.getCellValue(threeProductCell);
		if (!threeProduct.equals("规格3")) {
			return false;
		}

		// 内包装数量
		Cell packagesNumCell = row.getCell(10);
		String packagesNumStr = this.getCellValue(packagesNumCell);
		if (!packagesNumStr.equals("内包装数量")) {
			return false;
		}

		// 保质期
		Cell shelfLifeCell = row.getCell(11);
		String shelfLifeStr = this.getCellValue(shelfLifeCell);
		if (!shelfLifeStr.equals("保质期")) {
			return false;
		}

		// 基本单位
		Cell unitCell = row.getCell(12);
		String unit = this.getCellValue(unitCell);
		if (!unit.equals("基本单位")) {
			return false;
		}

		// 重量
		Cell weightCell = row.getCell(13);
		String weightStr = this.getCellValue(weightCell);
		if (!weightStr.equals("重量值")) {
			return false;
		}

		// 重量单位
		Cell weightUnitCell = row.getCell(14);
		String weightUnit = this.getCellValue(weightUnitCell);
		if (!weightUnit.equals("重量单位")) {
			return false;
		}

		// 保存条件
		Cell storageConditionsCell = row.getCell(15);
		String storageConditions = this.getCellValue(storageConditionsCell);
		if (!storageConditions.equals("保存条件")) {
			return false;
		}

		// 商品性质
		Cell natureCell = row.getCell(16);
		String nature = this.getCellValue(natureCell);
		if (!nature.equals("商品性质")) {
			return false;
		}

		// 体积
		Cell volumeCell = row.getCell(17);
		String volumeStr = this.getCellValue(volumeCell);
		if (!volumeStr.equals("体积值")) {
			return false;
		}

		// 体积单位
		Cell volumeUnitCell = row.getCell(18);
		String volumeUnit = this.getCellValue(volumeUnitCell);
		if (!volumeUnit.equals("体积单位")) {
			return false;
		}

		// 成本价
		Cell costCell = row.getCell(19);
		String costStr = this.getCellValue(costCell);
		if (!costStr.equals("成本价")) {
			return false;
		}

		// 市场价
		Cell marketPriceCell = row.getCell(20);
		String marketPriceStr = this.getCellValue(marketPriceCell);
		if (!marketPriceStr.equals("市场价")) {
			return false;
		}

		// 销售价
		Cell priceCell = row.getCell(21);
		String priceStr = this.getCellValue(priceCell);
		if (!priceStr.equals("销售价")) {
			return false;
		}

		return true;
	}

	 @Override
	 public GoodsImportLog dealImportMore(MultipartFile multipartFile, Admin admin, Supplier supplier) {
	    GoodsImportLog goodsImportLog = new GoodsImportLog();

	        try {
	            Workbook wb;
	            // excel 版本区别
	            if (multipartFile.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
	                wb = new HSSFWorkbook(multipartFile.getInputStream());
	            } else {
	                wb = new XSSFWorkbook(multipartFile.getInputStream());
	            }

	            Sheet sheet = wb.getSheetAt(0);
	            int rowLen = sheet.getPhysicalNumberOfRows();
	            int successNum = 0;
	            int errorNum = 0;

	            /*if (!isthisExcel(sheet)) {
	                return null;
	            }*/

	            goodsImportLog.setTotal(rowLen - 2);

	            goodsImportLog.setAdmin(admin);

	            //List<ProductImport> products = new ArrayList<>();

	            Map<String, GoodsImportInfo> exitsGoods = new HashMap<>();
	            Map<String, GoodsImportInfo> exitsGoodsSn = new HashMap<>();

	            for (int i = 2; i < rowLen; i++) {
	            	List<ProductImport> products = new ArrayList<>();
	            	
	                GoodsImportInfo importInfo = new GoodsImportInfo();
	                List<GoodsImportError> errors = new ArrayList<>();

	                Row row = sheet.getRow(i);

	                // 需要处理类型
	                Cell snCell = row.getCell(0);
	                if (snCell != null) {
	                    snCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String sn = CommonUtils.getCellValueStr(snCell);

	                
	                Cell nameCell = row.getCell(1);
	                if (nameCell != null) {
	                	nameCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String name = CommonUtils.getCellValue(nameCell);

	                //
	                Cell oneProductCategoryCell = row.getCell(2);
	                if (oneProductCategoryCell != null) {
	                    oneProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String oneProductCategory = CommonUtils.getCellValue(oneProductCategoryCell);

	                Cell twoProductCategoryCell = row.getCell(3);
	                if (twoProductCategoryCell != null) {
	                    twoProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String twoProductCategory = CommonUtils.getCellValue(twoProductCategoryCell);

	                Cell threeProductCategoryCell = row.getCell(4);
	                if (threeProductCategoryCell != null) {
	                    threeProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String threeProductCategory = CommonUtils.getCellValue(threeProductCategoryCell);


	                // 内包装数量
	                Cell packagesNumCell = row.getCell(5);
	                String packagesNumStr = CommonUtils.getCellValueStr(packagesNumCell);

	                // 保质期
	                Cell shelfLifeCell = row.getCell(6);
	                String shelfLifeStr = CommonUtils.getCellValueStr(shelfLifeCell);

	                // 基本单位
	                Cell unitCell = row.getCell(7);
	                String unit = CommonUtils.getCellValue(unitCell);

	                // 保存条件
					Cell storageConditionsCell = row.getCell(8);
					String storageConditions = CommonUtils.getCellValue(storageConditionsCell);
	                
					// 商品性质
					Cell natureCell = row.getCell(9);
					String nature = CommonUtils.getCellValue(natureCell);

	                // 体积
	                Cell volumeCell = row.getCell(10);
	                String volumeStr = CommonUtils.getCellValue(volumeCell);

	                // 体积单位
	                Cell volumeUnitCell = row.getCell(11);
	                String volumeUnit = CommonUtils.getCellValue(volumeUnitCell);

	                //条码
	                Cell barCodeCell = row.getCell(12);
	                String barCode = CommonUtils.getCellValueStr(barCodeCell) ;

	                // 成本价
	                Cell costCell = row.getCell(13);
	                String costStr = CommonUtils.getCellValue(costCell);

	                // 销售价
	                Cell priceCell = row.getCell(14);
	                String priceStr = CommonUtils.getCellValue(priceCell);

	                Cell specificationNameCell = row.getCell(15);
	                if (specificationNameCell != null) {
	                    specificationNameCell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String specificationName = CommonUtils.getCellValue(specificationNameCell);

	                Cell specificationOption1Cell = row.getCell(16);
	                if (specificationOption1Cell != null) {
	                    specificationOption1Cell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String specification1Option = CommonUtils.getCellValue(specificationOption1Cell);

	                //条码
	                Cell barCode1Cell1 = row.getCell(17);
	                String barCode1 = CommonUtils.getCellValueStr(barCode1Cell1) ;

	                // 成本价
	                Cell costCell1 = row.getCell(18);
	                String costStr1 = CommonUtils.getCellValue(costCell1);

	                // 销售价
	                Cell priceCell1 = row.getCell(19);
	                String priceStr1 = CommonUtils.getCellValue(priceCell1);

	                Cell specificationOption2Cell = row.getCell(20);
	                if (specificationOption2Cell != null) {
	                    specificationOption2Cell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String specification2Option = CommonUtils.getCellValue(specificationOption2Cell);

	                //条码
	                Cell barCode1Cell2 = row.getCell(21);
	                String barCode2 = CommonUtils.getCellValueStr(barCode1Cell2) ;

	                // 成本价
	                Cell costCell2 = row.getCell(22);
	                String costStr2 = CommonUtils.getCellValue(costCell2);

	                // 销售价
	                Cell priceCell2 = row.getCell(23);
	                String priceStr2 = CommonUtils.getCellValue(priceCell2);

	                Cell specificationOption3Cell = row.getCell(24);
	                if (specificationOption3Cell != null) {
	                    specificationOption3Cell.setCellType(Cell.CELL_TYPE_STRING);
	                }
	                String specification3Option = CommonUtils.getCellValue(specificationOption3Cell);

	                //条码
	                Cell barCodeCell3 = row.getCell(25);
	                String barCode3 = CommonUtils.getCellValueStr(barCodeCell3) ;

	                // 成本价
	                Cell costCell3 = row.getCell(26);
	                String costStr3 = CommonUtils.getCellValue(costCell3);

	                // 销售价
	                Cell priceCell3 = row.getCell(27);
	                String priceStr3 = CommonUtils.getCellValue(priceCell3);

	                boolean vaild = true;

	                if (StringUtils.isNotEmpty(sn)) {
	                    if (exitsGoodsSn.containsKey(sn)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("sn");
	                                this.setErrorInfo("excel商品编号重复");
	                            }
	                        });
	                        vaild = false;

	                        GoodsImportInfo exits = exitsGoodsSn.get(sn);

	                        if (exits.getValid()) {
	                            exits.setValid(false);
	                            exits.getErrors().add(new GoodsImportError() {
	                                {
	                                    this.setErrorField("sn");
	                                    this.setErrorInfo("excel商品编号重复");
	                                }
	                            });
	                            errorNum++;
	                            successNum--;
	                        }

	                    } else {
	                        exitsGoodsSn.put(name, importInfo);
	                        // 判断商品是否重复
	                        boolean flag = goodsDao.snExists(sn, null);
	                        if (flag) {
	                            errors.add(new GoodsImportError() {
	                                {
	                                    this.setErrorField("sn");
	                                    this.setErrorInfo("系统中已存商品编号");
	                                }
	                            });
	                            vaild = false;
	                        }
	                    }
	                }

	                if (!StringUtils.isEmpty(sn)) {
	                	if (CommonUtils.strTest(sn)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("sn");
	                                this.setErrorInfo("商品编号带有特殊符号");
	                            }
	                        });
	                        vaild = false;
	                    }
					}
	                
	                if (StringUtils.isEmpty(oneProductCategory)) {
	                    errors.add(new GoodsImportError() {
	                        {
	                            this.setErrorField("oneProductCategory");
	                            this.setErrorInfo("一级分类为空");
	                        }
	                    });
	                    vaild = false;
	                } else {
	                    if (CommonUtils.strTest(oneProductCategory)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("oneProductCategory");
	                                this.setErrorInfo("一级分类包含特殊符号");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }

	                if (!StringUtils.isEmpty(twoProductCategory)) {
	                    if (CommonUtils.strTest(twoProductCategory)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("twoProductCategory");
	                                this.setErrorInfo("二级分类包含特殊符号");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }

	                if (!StringUtils.isEmpty(threeProductCategory)) {
	                    if (CommonUtils.strTest(threeProductCategory)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("threeProductCategory");
	                                this.setErrorInfo("三级分类包含特殊符号");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }

	                if (StringUtils.isEmpty(name)) {
	                    errors.add(new GoodsImportError() {
	                        {
	                            this.setErrorField("name");
	                            this.setErrorInfo("商品名称为空");
	                        }
	                    });
	                    vaild = false;
	                } else {
	                    // 如果发现重复商品名称，需要将重复的商品名称处理为失败
	                    if (exitsGoods.containsKey(name)) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("name");
	                                this.setErrorInfo("excel商品名称重复");
	                            }
	                        });
	                        vaild = false;

	                        GoodsImportInfo exits = exitsGoods.get(name);

	                        if (exits.getValid()) {
	                            exits.setValid(false);
	                            GoodsImportError ImportError = new GoodsImportError();
	                            ImportError.setErrorField("name");
	                            ImportError.setErrorInfo("excel商品名称重复");
	                            exits.getErrors().add(ImportError);
	                            errorNum++;
	                            successNum--;
	                        }
	                    } else {
	                        exitsGoods.put(name, importInfo);
	                        // 判断商品是否重复
	                        List<Goods> goodsL = goodsDao.findByName(name, supplier, Types.platform);
	                        if (goodsL.size() > 0) {
								errors.add(new GoodsImportError() {
									{
										this.setErrorField("name");
										this.setErrorInfo("系统中已存该商品");
									}
								});
								vaild = false;
							}
	                    }
	                }
	                BigDecimal cost = new BigDecimal(0);
	                if (!StringUtils.isEmpty(costStr)) {
	                    try {
	                        cost = BigDecimal.valueOf(Double.parseDouble(costStr));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("cost");
	                                this.setErrorInfo("成本价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal price = new BigDecimal(0);
	                if (!StringUtils.isEmpty(priceStr)) {
	                    try {
	                        price = BigDecimal.valueOf(Double.parseDouble(priceStr));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("price");
	                                this.setErrorInfo("销售价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal cost1 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(costStr1)) {
	                    try {
	                    	cost1 = BigDecimal.valueOf(Double.parseDouble(costStr1));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("cost");
	                                this.setErrorInfo("成本价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal price1 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(priceStr1)) {
	                    try {
	                    	price1 = BigDecimal.valueOf(Double.parseDouble(priceStr1));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("price");
	                                this.setErrorInfo("销售价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                
	                BigDecimal cost2 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(costStr2)) {
	                    try {
	                    	cost2 = BigDecimal.valueOf(Double.parseDouble(costStr2));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("cost");
	                                this.setErrorInfo("成本价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal price2 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(priceStr2)) {
	                    try {
	                    	price2 = BigDecimal.valueOf(Double.parseDouble(priceStr2));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("price");
	                                this.setErrorInfo("销售价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                
	                BigDecimal cost3 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(costStr3)) {
	                    try {
	                    	cost3 = BigDecimal.valueOf(Double.parseDouble(costStr3));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("cost");
	                                this.setErrorInfo("成本价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal price3 = new BigDecimal(0);
	                if (!StringUtils.isEmpty(priceStr3)) {
	                    try {
	                    	price3 = BigDecimal.valueOf(Double.parseDouble(priceStr3));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("price");
	                                this.setErrorInfo("销售价无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                
	                Long packagesNum = 0L;
	                if (!StringUtils.isEmpty(packagesNumStr)) {
	                    try {
	                        packagesNum = Long.parseLong(packagesNumStr);
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("packagesNumStr");
	                                this.setErrorInfo("内包装数量无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                Long shelfLife = 0L;
	                if (!StringUtils.isEmpty(shelfLifeStr)) {
	                    try {
	                        shelfLife = Long.parseLong(shelfLifeStr);
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("shelfLifeStr");
	                                this.setErrorInfo("保质期无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }
	                BigDecimal volume = new BigDecimal(0);
	                if (!StringUtils.isEmpty(volumeStr)) {
	                    try {
	                        volume = BigDecimal.valueOf(Double
	                                .parseDouble(volumeStr));
	                    } catch (NumberFormatException e) {
	                        errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("volume");
	                                this.setErrorInfo("体积值无效");
	                            }
	                        });
	                        vaild = false;
	                    }
	                }

	                Boolean specificationNameFlag = false;
	                if (StringUtils.isEmpty(specificationName)) {
	                	specificationNameFlag = true;
					}
	                if (!StringUtils.isEmpty(specification1Option) 
	                		|| !StringUtils.isEmpty(barCode1) 
	                		|| !StringUtils.isEmpty(costStr1)
	                		|| !StringUtils.isEmpty(priceStr1)
	                		|| !StringUtils.isEmpty(specification2Option) 
	                		|| !StringUtils.isEmpty(barCode2) 
	                		|| !StringUtils.isEmpty(costStr2)
	                		|| !StringUtils.isEmpty(priceStr2)
	                		|| !StringUtils.isEmpty(specification3Option) 
	                		|| !StringUtils.isEmpty(barCode3) 
	                		|| !StringUtils.isEmpty(costStr3)
	                		|| !StringUtils.isEmpty(priceStr3)) {
						
	                	if (specificationNameFlag) {
	                		errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("name");
	                                this.setErrorInfo("规格名称为空");
	                            }
	                        });
	                        vaild = false;
						}
					}
	                
	                if (!StringUtils.isEmpty(barCode1) 
	                		|| !StringUtils.isEmpty(costStr1)
	                		|| !StringUtils.isEmpty(priceStr1)) {
						
	                	if (StringUtils.isEmpty(specification1Option)) {
	                		errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("name");
	                                this.setErrorInfo("规格1为空");
	                            }
	                        });
	                        vaild = false;
						}
					}
	                
	                if (!StringUtils.isEmpty(barCode2) 
	                		|| !StringUtils.isEmpty(costStr2)
	                		|| !StringUtils.isEmpty(priceStr2)) {
						
	                	if (StringUtils.isEmpty(specification2Option)) {
							errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("name");
	                                this.setErrorInfo("规格2为空");
	                            }
	                        });
	                        vaild = false;
						}
					}
	                
	                if (!StringUtils.isEmpty(barCode3) 
	                		|| !StringUtils.isEmpty(costStr3)
	                		|| !StringUtils.isEmpty(priceStr3)) {
						
	                	if (StringUtils.isEmpty(specification3Option)) {
							errors.add(new GoodsImportError() {
	                            {
	                                this.setErrorField("name");
	                                this.setErrorInfo("规格3为空");
	                            }
	                        });
	                        vaild = false;
						}
					}
	                
	                //处理规格
	                ProductCategoryImport oneCategory = new ProductCategoryImport();
					ProductCategoryImport twoCategory = new ProductCategoryImport();
					ProductCategoryImport threeCategory = new ProductCategoryImport();
	                // 判断一级菜单是否已存在
	                if (!oneProductCategory.equals("")) {
	                    List<ProductCategoryImport> list = productCategoryImportDao.findByParent(supplier, null, oneProductCategory);
	                    if (list.size() > 0) {
	                        oneCategory = list.get(0);
	                    } else {
	                    	ProductCategoryImport productCategory = new ProductCategoryImport();
	                        productCategory.setName(oneProductCategory);
	                        productCategory.setSupplier(supplier);
	                        productCategory.setGrade(0);
	                        oneCategory = saveProductCategory(productCategory);
	                    }
	                } else {
	                    List<ProductCategoryImport> list = productCategoryImportDao.findByParent(supplier , null, " ");
	                    if (list.size() > 0) {
	                        oneCategory = list.get(0);
	                    } else {
	                    	ProductCategoryImport productCategory = new ProductCategoryImport();
	                        productCategory.setName(oneProductCategory);
	                        productCategory.setGrade(0);
	                        productCategory.setSupplier(supplier);
	                        oneCategory = saveProductCategory(productCategory);
	                    }
	                }
	                if (!twoProductCategory.equals("")) {
	                    List<ProductCategoryImport> list = productCategoryImportDao.findByParent(supplier, oneCategory, twoProductCategory);
	                    if (list.size() > 0) {
	                        twoCategory = list.get(0);
	                    } else {
	                    	ProductCategoryImport productCategory = new ProductCategoryImport();
	                        productCategory.setName(twoProductCategory);
	                        productCategory.setGrade(1);
	                        productCategory.setSupplier(supplier);
	                        productCategory.setParent(oneCategory);
	                        twoCategory = saveProductCategory(productCategory);
	                    }
	                }

	                // 如果二级分类为空，三级变二级
	                if (!threeProductCategory.equals("")) {
	                    if (!twoProductCategory.equals("")) {
	                        List<ProductCategoryImport> list = productCategoryImportDao.findByParent(supplier, twoCategory, threeProductCategory);
	                        if (list.size() > 0) {
	                            threeCategory = list.get(0);
	                        } else {
	                        	ProductCategoryImport productCategory = new ProductCategoryImport();
	                            productCategory.setName(threeProductCategory);
	                            productCategory.setGrade(2);
	                            productCategory.setParent(twoCategory);
	                            threeCategory = saveProductCategory(productCategory);
	                        }
	                    } else {
	                        List<ProductCategoryImport> list = productCategoryImportDao.findByParent(supplier , oneCategory, threeProductCategory);
	                        if (list.size() > 0) {
	                            twoCategory = list.get(0);
	                        } else {
	                        	ProductCategoryImport productCategory = new ProductCategoryImport();
	                            productCategory.setName(threeProductCategory);
	                            productCategory.setGrade(1);
	                            productCategory.setParent(oneCategory);
	                            twoCategory = saveProductCategory(productCategory);
	                        }
	                    }
	                }
	                List<String> options = new ArrayList<>();
	                if (!specification1Option.isEmpty()) {
	                	options.add(specification1Option);
					}
	                if (!specification2Option.isEmpty()) {
	                	options.add(specification2Option);
					}
	                if (!specification3Option.isEmpty()) {
	                	options.add(specification3Option);
					}
	                SpecificationImport specification = new SpecificationImport();
	                specification.setOptions(options);
	                specification.setName(specificationName);
	                specification.setSupplier(supplier);

	                // 安置规格到最后一个分类
	                if (threeProductCategory.equals("")) {
	                    if (twoProductCategory.equals("")) {
	                    	importInfo.setProductCategoryImport(oneCategory);
	                        specification = saveSpecification(specification, oneCategory, options);
	                    } else {
	                        importInfo.setProductCategoryImport(twoCategory);
	                        specification = saveSpecification(specification, twoCategory, options);
	                    }
	                } else {
	                    if (twoProductCategory.equals("")) {
	                        importInfo.setProductCategoryImport(twoCategory);
	                        specification = saveSpecification(specification, twoCategory, options);
	                    } else {
	                        importInfo.setProductCategoryImport(threeCategory);
	                        specification = saveSpecification(specification, threeCategory, options);
	                    }

	                }

	                importInfo.setName(name);

	                Map<String, String> map = new HashMap<>();
	                
	                ProductImport  product1 = null;
	                ProductImport  product2 = null;
	                ProductImport  product3 = null;
	                
					if (StringUtils.isEmpty(specification1Option)) {
						if (StringUtils.isEmpty(specification2Option)) {
							if (!StringUtils.isEmpty(specification3Option)) {
								product1 = new ProductImport();
				                product1.setBarCode(barCode3);
				                product1.setMarketPrice(new BigDecimal(0));
				                product1.setExchangePoint(0L);
				                product1.setAllocatedStock(0);
				                product1.setIsDefault(true);
				                product1.setSpecificationValues(null);
				                product1.setCost(cost3);
				                product1.setPrice(price3);
				                product1.setRewardPoint(0L);
								product1.setStock(999999999);
							}
						}else {
							product1 = new ProductImport();
			                product1.setBarCode(barCode2);
			                product1.setMarketPrice(new BigDecimal(0));
			                product1.setExchangePoint(0L);
			                product1.setAllocatedStock(0);
			                product1.setIsDefault(true);
			                product1.setSpecificationValues(null);
			                product1.setCost(cost2);
			                product1.setPrice(price2);
			                product1.setRewardPoint(0L);
							product1.setStock(999999999);
							
							if (!StringUtils.isEmpty(specification3Option)) {
								product2 = new ProductImport();
				                product2.setBarCode(barCode3);
				                product2.setMarketPrice(new BigDecimal(0));
				                product2.setExchangePoint(0L);
				                product2.setAllocatedStock(0);
				                product2.setIsDefault(true);
				                product2.setSpecificationValues(null);
				                product2.setCost(cost3);
				                product2.setPrice(price3);
				                product2.setRewardPoint(0L);
								product2.setStock(999999999);
							}
						}
					}else {
						product1 = new ProductImport();
		                product1.setBarCode(barCode1);
		                product1.setMarketPrice(new BigDecimal(0));
		                product1.setExchangePoint(0L);
		                product1.setAllocatedStock(0);
		                product1.setIsDefault(true);
		                product1.setSpecificationValues(null);
		                product1.setCost(cost1);
		                product1.setPrice(price1);
		                product1.setRewardPoint(0L);
						product1.setStock(999999999);
						
						if (!StringUtils.isEmpty(specification2Option)) {
							product2 = new ProductImport();
			                product2.setBarCode(barCode2);
			                product2.setMarketPrice(new BigDecimal(0));
			                product2.setExchangePoint(0L);
			                product2.setAllocatedStock(0);
			                product2.setIsDefault(true);
			                product2.setSpecificationValues(null);
			                product2.setCost(cost2);
			                product2.setPrice(price2);
			                product2.setRewardPoint(0L);
							product2.setStock(999999999);
							
							if (!StringUtils.isEmpty(specification3Option)) {
								product3 = new ProductImport();
				                product3.setBarCode(barCode3);
				                product3.setMarketPrice(new BigDecimal(0));
				                product3.setExchangePoint(0L);
				                product3.setAllocatedStock(0);
				                product3.setIsDefault(true);
				                product3.setSpecificationValues(null);
				                product3.setCost(cost3);
				                product3.setPrice(price3);
				                product3.setRewardPoint(0L);
								product3.setStock(999999999);
							}
						}else {
							if (!StringUtils.isEmpty(specification3Option)) {
								product2 = new ProductImport();
				                product2.setBarCode(barCode3);
				                product2.setMarketPrice(new BigDecimal(0));
				                product2.setExchangePoint(0L);
				                product2.setAllocatedStock(0);
				                product2.setIsDefault(true);
				                product2.setSpecificationValues(null);
				                product2.setCost(cost3);
				                product2.setPrice(price3);
				                product2.setRewardPoint(0L);
								product2.setStock(999999999);
							}
						}
						
					}
					
	                List<SpecificationItem> specificationItems = new ArrayList<>();
	                List<Entry> entrys = new ArrayList<>();
	                SpecificationItem specificationItem = new SpecificationItem();
	                if (!StringUtils.isEmpty(specificationName)) {
	                    if (!specification1Option.equals("") || !specification2Option.equals("") || !specification3Option.equals("")) {
	                        specificationItem.setName(specificationName);
	                        if (!specification1Option.equals("")) {
	                            Entry entry = new Entry();
	                            entry.setId(0);
	                            entry.setValue(specification1Option);
	                            entry.setIsSelected(true);
	                            entrys.add(entry);
	                        }
	                        if (!specification2Option.equals("")) {
	                            Entry entry = new Entry();
	                            if (!specification1Option.equals("")) {
	                                entry.setId(1);
	                                entry.setIsSelected(true);
	                                
	                            } else {
	                                entry.setId(0);
	                                entry.setIsSelected(true);
	                                
	                            }

	                            entry.setValue(specification2Option);

	                            entrys.add(entry);
	                        }
	                        if (!specification3Option.equals("")) {
	                            Entry entry = new Entry();

	                            if (!specification2Option.equals("") && !specification1Option.equals("")) {
	                                entry.setId(2);
	                                entry.setIsSelected(true);
	                                
	                            } else {
	                                if (specification1Option.equals("")) {
	                                    if (specification2Option.equals("")) {
	                                        entry.setId(0);
	                                        entry.setIsSelected(true);
	                                        
	                                    } else {
	                                    	 entry.setId(1);
		                                     entry.setIsSelected(true);
		                                     
										}
	                                } else {
	                                    if (!specification2Option.equals("")) {
	                                        entry.setId(2);
	                                        entry.setIsSelected(true);
	                                        
	                                    } else {
	                                        entry.setId(1);
	                                        entry.setIsSelected(true);
	                                        
	                                    }
	                                }
	                            }

	                            entry.setValue(specification3Option);
	                            entrys.add(entry);
	                        }
	                        specificationItem.setEntries(entrys);
	                        specificationItems.add(specificationItem);
	                    }
	                }

	                importInfo.setSpecificationItems(specificationItems);

	                for (Entry entry : entrys) {
	                	if (entry.getId() == 0) {
	                		 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
	                		 SpecificationValue specificationValue = new SpecificationValue();
	                         specificationValue.setId(entry.getId());
	                         specificationValue.setValue(entry.getValue());
	                         specificationValues.add(specificationValue);
	                         product1.setSpecificationValues(specificationValues);
	                         products.add(product1);
						}
	                	if (entry.getId() == 1) {
	               		 	List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
	               		 	SpecificationValue specificationValue = new SpecificationValue();
	                        specificationValue.setId(entry.getId());
	                        specificationValue.setValue(entry.getValue());
	                        specificationValues.add(specificationValue);
	                        product2.setSpecificationValues(specificationValues);
	                        products.add(product2);
						}
	                	if (entry.getId() == 2) {
	               		 	List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
	               		 	SpecificationValue specificationValue = new SpecificationValue();
	                        specificationValue.setId(entry.getId());
	                        specificationValue.setValue(entry.getValue());
	                        specificationValues.add(specificationValue);
	                        product3.setSpecificationValues(specificationValues);
	                        products.add(product3);
						}
	                }

	                importInfo.setMarketPrice(new BigDecimal(0));
	                importInfo.setScore(0F);
					importInfo.setTotalScore(0L);
					importInfo.setScoreCount(0L);
	                importInfo.setScoreCount(0L);
					importInfo.setHits(0L);
					importInfo.setIsList(false);
					importInfo.setIsMarketable(false);
					importInfo.setIsTop(false);
					importInfo.setWeekHits(0L);
					importInfo.setMonthHits(0L);
					importInfo.setSales(0L);
					importInfo.setWeekSales(0L);
					importInfo.setMonthSales(0L);
	                importInfo.setSn(sn);
	                importInfo.setPrice(price);
	                importInfo.setShelfLife(shelfLife);
	                importInfo.setUnit(unit(unit));
	                importInfo.setStorageConditions(storageConditions(storageConditions));
	                importInfo.setNature(nature(nature));
	                importInfo.setVolume(volume);
	                importInfo.setVolumeUnit(volumeUnit(volumeUnit));
	                importInfo.setGenerateMethod(Goods.GenerateMethod.eager);
	                importInfo.setIsDelivery(true);
	                importInfo.setSupplier(supplier);
	                importInfo.setPackagesNum(packagesNum);
	                importInfo.setWeekHitsDate(new Date());
					importInfo.setMonthHitsDate(new Date());
					importInfo.setWeekSalesDate(new Date());
					importInfo.setMonthSalesDate(new Date());
	                importInfo.setValid(vaild);
	                importInfo.setErrors(errors);
	                importInfo.setType(Goods.Type.general);

//	                goodsImportInfos.add(importInfo);

	                if (vaild) {
	                    successNum++;
	                } else {
	                    errorNum++;
	                }
	                
	                goodsImportLog.setErrorNum(errorNum);
		            goodsImportLog.setSuccessNum(successNum);
		            goodsImportLogDao.persist(goodsImportLog);


		            importInfo.setGoodsImportLog(goodsImportLog);

		            setValue(importInfo, supplier);
		            goodsImportInfoDao.persist(importInfo);

		            for (ProductImport productImport : products) {
		               productImport.setGoodsImportInfo(importInfo);
		               setValue(productImport);
		               productImportDao.persist(productImport);
					}
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            LOGGER.error("method:dealImportMore , excel file read error : ", e);
	            return null;

	        }
	        return goodsImportLog;
	    }

	private ProductCategoryImport saveProductCategory(
			ProductCategoryImport productCategory) {
		setValue(productCategory);
		productCategoryImportDao.persist(productCategory);
		return productCategory;
	}

	private SpecificationImport saveSpecification(
			SpecificationImport specification,
			ProductCategoryImport productCategory, List<String> options) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(
				new UniquePredicate(), new Predicate() {
					public boolean evaluate(Object object) {
						String option = (String) object;
						return StringUtils.isNotEmpty(option);
					}
				}));

		// 同分类下同规格，excel规格追加规格
		List<SpecificationImport> list = specificationImportDao.findByName(
				specification.getName(), specification.getSupplier(),
				productCategory);
		if (list != null) {
			if (list.size() > 0) {
				specification = list.get(0);
				List<String> optionsTshi = new ArrayList<>();
				Map<String, String> map = new HashMap<String, String>();
				for (String str : specification.getOptions()) {
					map.put(str, str);
				}
				for (String str : options) {
					map.put(str, str);
				}
				for (String str : map.keySet()) {
					optionsTshi.add(str);
				}

				specification.setOptions(optionsTshi);
				specificationImportDao.merge(specification);
			} else {
				specification.setProductCategoryImport(productCategory);
				specificationImportDao.persist(specification);
			}
		}

		return specification;
	}

	private Specification saveSpecification(Specification specification, ProductCategory productCategory, List<String> options) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(
				new UniquePredicate(), new Predicate() {
					public boolean evaluate(Object object) {
						String option = (String) object;
						return StringUtils.isNotEmpty(option);
					}
				}));

		List<Specification> list = specificationDao.findByName(
				specification.getName(), specification.getSupplier(),
				productCategory);
		if (list != null) {
			if (list.size() > 0) {
				specification = list.get(0);
				List<String> optionsTshi = new ArrayList<>();
				Map<String, String> map = new HashMap<String, String>();
				for (String str : specification.getOptions()) {
					map.put(str, str);
				}
				for (String str : options) {
					map.put(str, str);
				}
				for (String str : map.keySet()) {
					optionsTshi.add(str);
				}
				specification.setOptions(optionsTshi);
				specificationDao.merge(specification);
			} else {
				specification.setProductCategory(productCategory);
				specificationDao.persist(specification);
			}
		}

		return specification;
	}

	private Goods.VolumeUnit volumeUnit(String volumeUnit) {
		if (volumeUnit.equals("m³")) {
			return Goods.VolumeUnit.cubicMeter;
		} else {
			return Goods.VolumeUnit.cubicCentimeters;
		}
	}

	private Goods.WeightUnit weightUnit(String weightUnit) {
		if (StringUtils.isEmpty(weightUnit)) {
			return null;
		}
		if (weightUnit.equals("g")) {
			return Goods.WeightUnit.g;
		} else {
			return Goods.WeightUnit.kg;
		}
	}

	private Goods.Nature nature(String nature) {
		if (StringUtils.isEmpty(nature)) {
			return null;
		}
		if (nature.equals("气体")) {
			return Goods.Nature.gas;
		} else if (nature.equals("固体")) {
			return Goods.Nature.solid;
		} else {
			return Goods.Nature.liquid;
		}
	}

	private Goods.StorageConditions storageConditions(String StorageConditions) {
		if (StringUtils.isEmpty(StorageConditions)) {
			return null;
		}
		if (StorageConditions.equals("常温")) {
			return Goods.StorageConditions.roomTemperature;
		} else if (StorageConditions.equals("冷藏")) {
			return Goods.StorageConditions.refrigeration;
		} else {
			return Goods.StorageConditions.frozen;
		}
	}

	private Goods.Unit unit(String unit) {
		Goods.Unit un = null;
		switch (unit) {
		case "箱":
			un = Goods.Unit.box;
			break;

		case "瓶":
			un = Goods.Unit.bottle;
			break;

		case "袋":
			un = Goods.Unit.bag;
			break;

		case "盒":
			un = Goods.Unit.frame;
			break;

		case "包":
			un = Goods.Unit.pack;
			break;
			
		case "个":
			un = Goods.Unit.one;
			break;
			
		case "件":
			un = Goods.Unit.pieces;
			break;
			
		case "捅":
			un = Goods.Unit.barrel;
			break;
			
		case "份":
			un = Goods.Unit.copies;
			break;
			
		case "克":
			un = Goods.Unit.g;
			break;
			
		case "罐":
			un = Goods.Unit.tank;
			break;
			
		case "升":
			un = Goods.Unit.rise;
			break;

		case "条":
			un = Goods.Unit.bar;
			break;
			
		case "只":
			un = Goods.Unit.only;
			break;
			
		case "卷":
			un = Goods.Unit.volume;
			break;
			
		case "听":
			un = Goods.Unit.listen;
			break;
		}

		return un;
	}

	private String getCellValue(Cell cell) {
		String cellValue = "";
		if (null == cell) {
			return cellValue;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			DecimalFormat df = new DecimalFormat("0.00");
			cellValue = df.format(cell.getNumericCellValue());
			break;

		case Cell.CELL_TYPE_STRING: // 字符串
			cellValue = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = cell.getBooleanCellValue() + "";
			break;

		case Cell.CELL_TYPE_FORMULA: // 公式
			cellValue = cell.getCellFormula() + "";
			break;

		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "";
			break;

		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "";
			break;

		default:
			cellValue = "";
			break;
		}
		return cellValue;
	}

	private String getCellValueStr(Cell cell) {
		String cellValue = "";
		if (null == cell) {
			return cellValue;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			DecimalFormat df = new DecimalFormat("0");
			cellValue = df.format(cell.getNumericCellValue());
			break;

		case Cell.CELL_TYPE_STRING: // 字符串
			cellValue = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = cell.getBooleanCellValue() + "";
			break;

		case Cell.CELL_TYPE_FORMULA: // 公式
			cellValue = cell.getCellFormula() + "";
			break;

		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "";
			break;

		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "";
			break;

		default:
			cellValue = "";
			break;
		}
		return cellValue;
	}

	private void setValue(ProductCategoryImport productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategoryImport parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId()
					+ ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId()
					+ ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	@Override
	public boolean saveMore(GoodsImportLog goodsImportLog, Admin operator) {
		List<GoodsImportInfo> infos = goodsImportInfoDao.findList(goodsImportLog, Boolean.TRUE);

		for (GoodsImportInfo goodsImportInfo : infos) {
			Goods goods = new Goods();
			goods.setSn(goodsImportInfo.getSn());
			goods.setName(goodsImportInfo.getName());

			ProductCategory oneCategory = null;
			ProductCategory twoCategory = null;
			ProductCategory threeCategory = null;

			if (goodsImportInfo.getProductCategoryImport() != null) {
				if (goodsImportInfo.getProductCategoryImport().getGrade() == 0) {
					// 添加一级分类
					List<ProductCategory> category = productCategoryDao.findByParent(operator.getSupplier(), null, goodsImportInfo.getProductCategoryImport().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new ProductCategory();
						oneCategory.setTypes(Types.platform);
						oneCategory.setSupplier(operator.getSupplier());
						oneCategory.setName(goodsImportInfo.getProductCategoryImport().getName());
						setValue(oneCategory);
						productCategoryDao.persist(oneCategory);
					}
					goods.setProductCategory(oneCategory);
				}
				if (goodsImportInfo.getProductCategoryImport().getGrade() == 1) {
					// 添加一级分类
					List<ProductCategory> category = productCategoryDao.findByParent(operator.getSupplier(), null,goodsImportInfo.getProductCategoryImport().getParent().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new ProductCategory();
						oneCategory.setTypes(Types.platform);
						oneCategory.setSupplier(operator.getSupplier());
						oneCategory.setName(goodsImportInfo.getProductCategoryImport().getParent().getName());
						setValue(oneCategory);
						productCategoryDao.persist(oneCategory);

					}

					// 添加二级分类
					List<ProductCategory> categorys = productCategoryDao.findByParent(operator.getSupplier(), oneCategory,goodsImportInfo.getProductCategoryImport().getName());
					if (categorys.size() > 0) {
						twoCategory = categorys.get(0);
					} else {
						twoCategory = new ProductCategory();
						twoCategory.setTypes(Types.platform);
						twoCategory.setSupplier(operator.getSupplier());
						twoCategory.setName(goodsImportInfo.getProductCategoryImport().getName());
						twoCategory.setGrade(goodsImportInfo.getProductCategoryImport().getGrade());
						twoCategory.setParent(oneCategory);
						setValue(twoCategory);
						productCategoryDao.persist(twoCategory);
					}

					goods.setProductCategory(twoCategory);
				}
				if (goodsImportInfo.getProductCategoryImport().getGrade() == 2) {
					// 添加一级分类
					List<ProductCategory> category = productCategoryDao.findByParent(operator.getSupplier(), null,goodsImportInfo.getProductCategoryImport().getParent().getParent().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new ProductCategory();
						oneCategory.setTypes(Types.platform);
						oneCategory.setSupplier(operator.getSupplier());
						oneCategory.setName(goodsImportInfo.getProductCategoryImport().getParent().getParent().getName());
						setValue(oneCategory);
						productCategoryDao.persist(oneCategory);
					}

					// 添加二级分类
					List<ProductCategory> categorys = productCategoryDao.findByParent(operator.getSupplier(), oneCategory,goodsImportInfo.getProductCategoryImport().getParent().getName());
					if (categorys.size() > 0) {
						twoCategory = categorys.get(0);
					} else {
						twoCategory = new ProductCategory();
						twoCategory.setTypes(Types.platform);
						twoCategory.setSupplier(operator.getSupplier());
						twoCategory.setName(goodsImportInfo.getProductCategoryImport().getParent().getName());
						twoCategory.setGrade(goodsImportInfo.getProductCategoryImport().getParent().getGrade());
						twoCategory.setParent(oneCategory);
						setValue(twoCategory);
						productCategoryDao.persist(twoCategory);
					}

					if (goodsImportInfo.getProductCategoryImport().getParent().getParent() != null) {
						// 添加三级分类
						List<ProductCategory> categorysNew = productCategoryDao.findByParent(operator.getSupplier(), twoCategory, goodsImportInfo.getProductCategoryImport().getName());
						if (categorysNew.size() > 0) {
							threeCategory = categorysNew.get(0);
						} else {
							threeCategory = new ProductCategory();
							threeCategory.setTypes(Types.platform);
							threeCategory.setSupplier(operator.getSupplier());
							threeCategory.setName(goodsImportInfo.getProductCategoryImport().getName());
							threeCategory.setGrade(goodsImportInfo.getProductCategoryImport().getGrade());
							threeCategory.setParent(twoCategory);
							setValue(threeCategory);
							productCategoryDao.persist(threeCategory);
						}
					}

					goods.setProductCategory(threeCategory);
				}
			}

			Set<Product> products = new HashSet<Product>();
			Specification specification = new Specification();
			specification.setTypes(Types.platform);
			if (goodsImportInfo.getSpecificationItems().size() > 0) {
				specification.setName(goodsImportInfo.getSpecificationItems().get(0).getName());
				specification.setOptions(specificationImportDao.findByName(specification.getName(), operator.getSupplier() , goodsImportInfo.getProductCategoryImport()).get(0).getOptions());
				specification.setSupplier(operator.getSupplier());
				
					if (threeCategory == null) {
						if (twoCategory == null) {
							saveSpecification(specification, oneCategory, specification.getOptions());
						} else {
							saveSpecification(specification, twoCategory, specification.getOptions());
						}
					} else {
						if (twoCategory == null) {
							saveSpecification(specification, oneCategory, specification.getOptions());
						} else {
							saveSpecification(specification, threeCategory, specification.getOptions());
						}
					}
			} else {
				 Product product = new Product();
				 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
                 product.setPrice(new BigDecimal(0));
                 product.setAllocatedStock(0);
				 product.setExchangePoint(0L);
				 product.setStock(99999999);
				 product.setMarketPrice(new BigDecimal(0));
                 product.setCost(new BigDecimal(0));
                 product.setIsDefault(true);
                 product.setRewardPoint(0L);
                 product.setSpecificationValues(specificationValues);
                 products.add(product);
			}
			
			goods.setPrice(goodsImportInfo.getPrice());
			goods.setMarketPrice(goodsImportInfo.getMarketPrice());
			goods.setWeekHitsDate(new Date());
			goods.setMonthHitsDate(new Date());
			goods.setWeekSalesDate(new Date());
			goods.setMonthSalesDate(new Date());

			Map<String, String> map = new HashMap<>();
			Map<String, Product> mapProduct = new HashMap<>();
			for (ProductImport productImport : goodsImportInfo.getProducts()) {
				List<SpecificationValue> list = productImport.getSpecificationValues();
				if (list.size() > 0) {
					for (SpecificationValue specificationValue : productImport.getSpecificationValues()) {
						if (StringUtils.isEmpty(map.get(specificationValue.getValue()))) {
							Product product = new Product();
							if (specificationValue.getId() == 0) {
								product.setIsDefault(true);
							}else {
								product.setIsDefault(false);
							}
							List<SpecificationValue> specificationValueL = new ArrayList<>();
							specificationValueL.add(specificationValue);
							product.setSpecificationValues(specificationValueL);
							product.setMarketPrice(new BigDecimal(0));
							product.setCost(productImport.getCost());
							product.setPrice(productImport.getPrice());
							product.setAllocatedStock(0);
							product.setExchangePoint(0L);
							product.setStock(99999999);
							product.setBarCode(productImport.getBarCode());
							product.setRewardPoint(0L);
							products.add(product);

							map.put(specificationValue.getValue(), specificationValue.getValue());
							mapProduct.put(specificationValue.getValue(), product);
						}
					}
				} else {
					Product product = new Product();
					product.setMarketPrice(new BigDecimal(0));
					product.setAllocatedStock(0);
					product.setRewardPoint(0L);
					product.setStock(99999999);
					product.setExchangePoint(0L);
					product.setIsDefault(true);
					List<SpecificationValue> specificationValueL = new ArrayList<>();
					product.setSpecificationValues(specificationValueL);
					product.setCost(productImport.getCost());
					product.setBarCode(productImport.getBarCode());
					product.setPrice(productImport.getPrice());
					products.add(product);
				}

			}
			
			List<Specification> specificationList = specificationDao.findByName(specification.getName(), operator.getSupplier() , goods.getProductCategory());
			List<SpecificationItem> specificationItems = new ArrayList<>();
			
			for (Specification specificationN : specificationList) {
				List<Entry> entrys = new ArrayList<>();
				SpecificationItem specificationItem = new SpecificationItem();
				int i = 0;
				for (String option : specificationN.getOptions()) {
					Entry entry = new Entry();
					entry.setId(i);
					entry.setValue(option);
					
					if (map.get(option) != null) {
						entry.setIsSelected(true);
					}else {
						entry.setIsSelected(false);
					}
					
					entrys.add(entry);
					i++;
				}
				specificationItem.setName(specification.getName());
				specificationItem.setEntries(entrys);
				specificationItems.add(specificationItem);
				
				for (Entry entry : entrys) {
					 if (mapProduct.get(entry.getValue()) != null) {
						 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
						 SpecificationValue specificationValue = new SpecificationValue();
	                     specificationValue.setId(entry.getId());
	                     specificationValue.setValue(entry.getValue());
	                     specificationValues.add(specificationValue);
	                     mapProduct.get(entry.getValue()).setSpecificationValues(specificationValues);
	                     products.add(mapProduct.get(entry.getValue()));
					 } else {
//						 ProductCenter product = new ProductCenter();
//						 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
//						 SpecificationValue specificationValue = new SpecificationValue();
//	                     specificationValue.setId(entry.getId());
//                       specificationValue.setValue(entry.getValue());
//	                     specificationValues.add(specificationValue);
//	                     product.setPrice(new BigDecimal(0));
//	                     product.setCost(new BigDecimal(0));
//	                     product.setIsDefault(false);
//	                     product.setSpecificationValues(specificationValues);
//	                     products.add(product);
					 }
					 
				}
			}
			
			goods.setTypes(Types.platform);
			goods.setHits(0L);
			goods.setIsList(false);
			goods.setIsMarketable(true);
			goods.setIsTop(false);
			goods.setScore(0F);
			goods.setTotalScore(0L);
			goods.setScoreCount(0L);
			goods.setHits(0L);
			goods.setWeekHits(0L);
			goods.setMonthHits(0L);
			goods.setSales(0L);
			goods.setWeekSales(0L);
			goods.setGenerateMethod(Goods.GenerateMethod.eager);
			goods.setMarketPrice(new BigDecimal(0));
			goods.setWeekHitsDate(new Date());
			goods.setMonthHitsDate(new Date());
			goods.setWeekSalesDate(new Date());
			goods.setMonthSalesDate(new Date());
			goods.setMonthSales(0L);
			goods.setShelfLife(goodsImportInfo.getShelfLife());
			goods.setUnit(goodsImportInfo.getUnit());
			goods.setWeight(goodsImportInfo.getWeight());
			goods.setWeightUnit(goodsImportInfo.getWeightUnit());
			goods.setStorageConditions(goodsImportInfo.getStorageConditions());
			goods.setNature(goodsImportInfo.getNature());
			goods.setVolume(goodsImportInfo.getVolume());
			goods.setVolumeUnit(goodsImportInfo.getVolumeUnit());
			goods.setPackagesNum(goodsImportInfo.getPackagesNum());
			goods.setSn(goodsImportInfo.getSn());
			goods.setIsList(false);
			goods.setIsMarketable(true);
			goods.setUnit(goodsImportInfo.getUnit());
			goods.setIsDelivery(true);
			goods.setMonthHits(0L);
			goods.setIsTop(false);
			goods.setMarketPrice(new BigDecimal(0));
			goods.setType(Goods.Type.general);
			goods.setPrice(goodsImportInfo.getPrice());
			goods.setSupplier(operator.getSupplier());
			
			filter(specificationItems);
			goods.setSpecificationItems(specificationItems);
			this.save(goods);

			for (Product product : products) {
				product.setGoods(goods);
				setValue(product, operator.getSupplier());
				productDao.persist(product);
				
			}

		}

		for (GoodsImportInfo goodsImportInfo : infos) {
			if (goodsImportInfo.getSpecificationItems() != null && goodsImportInfo.getSpecificationItems().size()>0) {
				specificationImportDao.delete(goodsImportInfo.getSpecificationItems().get(0).getName(), goodsImportInfo.getProductCategoryImport());
			}
		}
		
		return true;
	}

	@Override
	public List<Goods> findByName(String name, Supplier supplier, Types types) {
		return goodsDao.findByName(name, supplier, types);
	}

	@Override
	public Page<Goods> findPage(AssGoodDirectory assGoodDirectory,
			String goodName, Pageable pageable) {
		return goodsDao.findPage(assGoodDirectory, goodName, pageable);
	}

	public void filter(List<SpecificationItem> specificationItems) {
		CollectionUtils.filter(specificationItems, new Predicate() {
			public boolean evaluate(Object object) {
				SpecificationItem specificationItem = (SpecificationItem) object;
				if (specificationItem == null
						|| StringUtils.isEmpty(specificationItem.getName())) {
					return false;
				}
				CollectionUtils.filter(specificationItem.getEntries(),
						new Predicate() {
							private Set<Integer> idSet = new HashSet<Integer>();
							private Set<String> valueSet = new HashSet<String>();

							public boolean evaluate(Object object) {
								SpecificationItem.Entry entry = (SpecificationItem.Entry) object;
								return entry != null
										&& entry.getId() != null
										&& StringUtils.isNotEmpty(entry
												.getValue())
										&& entry.getIsSelected() != null
										&& idSet.add(entry.getId())
										&& valueSet.add(entry.getValue());
							}
						});
				return CollectionUtils.isNotEmpty(specificationItem
						.getEntries()) && specificationItem.isSelected();
			}
		});
	}

	@Override
	public Map<String, Object> uploadImage(String sn, String url, Supplier supplier, String batch) {
		System.out.println("进入导入商品方法时间sn(" + sn + ")"
				+ System.currentTimeMillis());

		Map<String, Object> data = new HashMap<String, Object>();

		GoodsImportImageInfo goodsImportImageInfo = new GoodsImportImageInfo();
		goodsImportImageInfo.setSupplier(supplier);
		goodsImportImageInfo.setBatch(batch);
		if (CommonUtils.fileNameTest(sn) || CommonUtils.fileNameTestSpace(sn)) {
			// 次图
			String snStr = sn.substring(0, sn.length() - 3).trim();
			Goods goods = goodsDao.findBySn(snStr, supplier);

			GoodsImportImageInfo info = goodsImportImageInfoDao.query(snStr, batch);
			if (info == null) {
				List<Second> seconds = new ArrayList<>();
				Second second = new Second();
				second.setId(0);
				second.setValue(url);
				seconds.add(second);
				goodsImportImageInfo.setSn(snStr);
				goodsImportImageInfo.setImages(seconds);
				goodsImportImageInfo.setName(goods.getName());
				goodsImportImageInfoDao.persist(goodsImportImageInfo);

				goodsImportImageInfoDao.flush();

				LOGGER.info("次图新增sn" + sn);
			} else {
				List<Second> secondM = new ArrayList<>();
				List<Second> seconds = info.getImages();
				if (seconds.size() < 2) {
					Second second = new Second();
					second.setId(1);
					second.setValue(url);
					secondM.addAll(seconds);
					secondM.add(second);
					info.setImages(secondM);
					goodsImportImageInfoDao.merge(info);

					goodsImportImageInfoDao.flush();

					LOGGER.info("次图修改sn" + sn);
				} else {
					data.put("message", "图片上传已超过上限");
					data.put("state", "admin.upload.error");
					return data;
				}
			}
		} else {
			// 主图
			sn = sn.trim();
			Goods goods = goodsDao.findBySn(sn, supplier);

			GoodsImportImageInfo goodsImage = goodsImportImageInfoDao.query(sn, batch);
			goodsImportImageInfo.setImage(url);
			goodsImportImageInfo.setName(goods.getName());
			goodsImportImageInfo.setSn(sn);
			if (goodsImage == null) {
				List<Second> seconds = new ArrayList<>();
				goodsImportImageInfo.setImages(seconds);
				goodsImportImageInfoDao.persist(goodsImportImageInfo);

				goodsImportImageInfoDao.flush();

				LOGGER.info("主图新增sn" + sn);
			} else {
				goodsImage.setImage(url);
				goodsImportImageInfoDao.merge(goodsImage);

				goodsImportImageInfoDao.flush();

				LOGGER.info("主图修改sn" + sn);
			}
		}

		System.out.println("结束导入商品方法时间sn(" + sn + ")"+ System.currentTimeMillis());

		data.put("message", "admin.message.success");
		data.put("state", "SUCCESS");
		data.put("url", url);
		
		return data;
	}

	@Override
	public boolean saveMore(String batch, Admin operator) {
		List<GoodsImportImageInfo> goodsImportImageInfoList = goodsImportImageInfoDao
				.findList(batch);
		for (GoodsImportImageInfo goodsImportImageInfo : goodsImportImageInfoList) {
			Goods goods = goodsDao.findBySn(goodsImportImageInfo.getSn(),
					operator.getSupplier());
			if (!StringUtils.isEmpty(goodsImportImageInfo.getImage())) {
				goods.setImage(goodsImportImageInfo.getImage());
			}

			List<Second> seconds = goodsImportImageInfo.getImages();
			List<String> str = new ArrayList<>();
			if (seconds.size() > 0) {
				if (seconds.size() == 1) {
					if (goods.getImages().size() == 0) {
						str.add(seconds.get(0).getValue());
					}
					if (goods.getImages().size() == 1) {
						str.add(goods.getImages().get(0));
						str.add(seconds.get(0).getValue());
					}
					if (goods.getImages().size() == 2) {
						str.add(seconds.get(0).getValue());
						str.add(goods.getImages().get(1));
					}
				}
				if (seconds.size() == 2) {
					for (Second second : seconds) {
						str.add(second.getValue());
					}
				}
				goods.setImages(str);
			}

			goodsDao.merge(goods);
		}

		return true;
	}

	@Override
	public List<Goods> supplyOfGoods(Long id) {
		return goodsDao.supplyOfGoods(id);
	}

	@Override
	public Page<Goods> findPage(Supplier supplier,
			ProductCategory productCategory, Pageable pageable , String searchName) {
		return goodsDao.findPage(supplier, productCategory, pageable , searchName);
	}

	@Override
	public Goods save(Goods goods, List<Product> products, Supplier supplier) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				
				Product product = (Product) object;
				
//				System.out.println("isNew"+!product.isNew());
//				System.out.println("hasSpecification"+!product.hasSpecification());
//				System.out.println("add"+!set.add(product.getSpecificationValueIds()));
//				System.out.println("isValid"+!specificationValueService.isValid(specificationItems,product.getSpecificationValues()));
				
				return product == null
						|| !product.isNew()
						|| !product.hasSpecification()
						|| !set.add(product.getSpecificationValueIds())
						|| !specificationValueService.isValid(specificationItems,product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products,
				new Predicate() {
					public boolean evaluate(Object object) {
						Product product = (Product) object;
						return product != null && product.getIsDefault();
					}
				});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		for (Product product : products) {
			switch (goods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product
						.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product
						.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(goods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());
		
		goods.setBarCode(defaultProduct.getBarCode());
		
		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		// TODO: 2017/1/23 添加供应商
		goods.setSupplier(supplier);

		setValue(goods, supplier);
		goodsDao.persist(goods);

		for (Product product : products) {
			setValue(product,goods.getSupplier());
			productDao.persist(product);
			//stockIn(product, null);
		}

		return goods;
	}

	@Override
	public Goods save(Goods goods, Product product, Supplier supplier) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		switch (goods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product
					.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product
					.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(goods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());
		
		goods.setBarCode(product.getBarCode());
		
		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setSpecificationItems(null);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		// TODO: 2017/1/23 添加供应商
		goods.setSupplier(supplier);
		setValue(goods, supplier);
		goodsDao.persist(goods);

		setValue(product,goods.getSupplier());
		productDao.persist(product);
		//stockIn(product, null);

		return goods;
	}

	@Override
	public Goods update(Goods goods, Product product, Supplier supplier) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		Goods pGoods = goodsDao.find(goods.getId());
		switch (pGoods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product
					.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product
					.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(pGoods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				productDao.remove(pProduct);
			}
			if (product.getStock() == null) {
				product.setStock(999999999);
			}
			setValue(product,pGoods.getSupplier());
			productDao.persist(product);
			//stockIn(product, null);
		} else {
			Product defaultProduct = pGoods.getDefaultProduct();
			defaultProduct.setPrice(product.getPrice());
			defaultProduct.setCost(product.getCost());
			defaultProduct.setMarketPrice(product.getMarketPrice());
			defaultProduct.setRewardPoint(product.getRewardPoint());
			defaultProduct.setExchangePoint(product.getExchangePoint());

			defaultProduct.setBarCode(product.getBarCode());
		}

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());

		goods.setBarCode(product.getBarCode());

		setValue(goods, supplier);
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
				"scoreCount", "hits", "weekHits", "monthHits", "sales",
				"weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
				"weekSalesDate", "monthSalesDate", "generateMethod", "reviews",
				"consultations", "favoriteMembers", "products", "supplier", "types");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);
		return pGoods;
	}

	@Override
	public Goods update(Goods goods, List<Product> products, Supplier supplier) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods
				.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null
						|| !product.isNew()
						|| !product.hasSpecification()
						|| !set.add(product.getSpecificationValueIds())
						|| !specificationValueService.isValid(
								specificationItems,
								product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products,
				new Predicate() {
					public boolean evaluate(Object object) {
						Product product = (Product) object;
						return product != null && product.getIsDefault();
					}
				});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		Goods pGoods = goodsDao.find(goods.getId());
		for (Product product : products) {
			switch (pGoods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product
						.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product
						.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(pGoods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				if (!exists(products, pProduct.getSpecificationValueIds())) {
					productDao.remove(pProduct);
				}
			}
			for (Product product : products) {
				Product pProduct = find(pGoods.getProducts(),
						product.getSpecificationValueIds());
				if (pProduct != null) {
					pProduct.setPrice(product.getPrice());
					pProduct.setCost(product.getCost());
					pProduct.setMarketPrice(product.getMarketPrice());
					pProduct.setRewardPoint(product.getRewardPoint());
					pProduct.setExchangePoint(product.getExchangePoint());
					pProduct.setIsDefault(product.getIsDefault());
					pProduct.setSpecificationValues(product
							.getSpecificationValues());

					pProduct.setBarCode(product.getBarCode());

				} else {
					if (product.getStock() == null) {
						throw new IllegalArgumentException();
					}
					setValue(product,pGoods.getSupplier());
					productDao.persist(product);
					//stockIn(product, null);
				}
			}
		} else {
			productDao.remove(pGoods.getDefaultProduct());
			for (Product product : products) {
				if (product.getStock() == null) {
					throw new IllegalArgumentException();
				}
				setValue(product,pGoods.getSupplier());
				productDao.persist(product);
				//stockIn(product, null);
			}
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());

		goods.setBarCode(defaultProduct.getBarCode());

		setValue(goods, supplier);
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
				"scoreCount", "hits", "weekHits", "monthHits", "sales",
				"weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
				"weekSalesDate", "monthSalesDate", "generateMethod", "reviews",
				"consultations", "favoriteMembers", "products", "supplier", "types");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);
		return pGoods;
	}

	
	@Override
	public Goods updateSmall(Goods goods, List<Product> products, Supplier supplier) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods
				.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null
						|| !product.isNew()
						|| !product.hasSpecification()
						|| !set.add(product.getSpecificationValueIds())
						|| !specificationValueService.isValid(
								specificationItems,
								product.getSpecificationValues());
			}
		})) {
			return null;
		}

		Product defaultProduct = (Product) CollectionUtils.find(products,
				new Predicate() {
					public boolean evaluate(Object object) {
						Product product = (Product) object;
						return product != null && product.getIsDefault();
					}
				});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		Goods pGoods = goodsDao.find(goods.getId());
		for (Product product : products) {
			switch (pGoods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product
						.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product
						.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(pGoods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				if (!exists(products, pProduct.getSpecificationValueIds())) {
					productDao.remove(pProduct);
				}
			}
			for (Product product : products) {
				Product pProduct = find(pGoods.getProducts(),
						product.getSpecificationValueIds());
				if (pProduct != null) {
					pProduct.setPrice(product.getPrice());
					pProduct.setCost(product.getCost());
					pProduct.setMarketPrice(product.getMarketPrice());
					pProduct.setRewardPoint(product.getRewardPoint());
					pProduct.setExchangePoint(product.getExchangePoint());
					pProduct.setIsDefault(product.getIsDefault());
					pProduct.setSpecificationValues(product
							.getSpecificationValues());

					pProduct.setBarCode(product.getBarCode());

				} else {
					if (product.getStock() == null) {
						throw new IllegalArgumentException();
					}
					setValue(product,pGoods.getSupplier());
					productDao.persist(product);
					//stockIn(product, null);
				}
			}
		} else {
			productDao.remove(pGoods.getDefaultProduct());
			for (Product product : products) {
				if (product.getStock() == null) {
					throw new IllegalArgumentException();
				}
				setValue(product,pGoods.getSupplier());
				productDao.persist(product);
				//stockIn(product, null);
			}
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());

		goods.setBarCode(defaultProduct.getBarCode());

		setValue(goods, supplier);
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
				"scoreCount", "hits", "weekHits", "monthHits", "sales",
				"weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
				"weekSalesDate", "monthSalesDate", "generateMethod", "reviews",
				"consultations", "favoriteMembers", "products", "supplier", "types");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);
		return pGoods;
	}
	
	@Override
	public List<Goods> findLocalGoodsList(Supplier supplier, Types types,
			ProductCategory productCategory, String searchName) {
		return goodsDao.findLocalGoodsList(supplier, types, productCategory, searchName);
	}

	@Override
	public Map<String,Object> findPageByAssign(Long relationId, Shop shop, Supplier supplier,SupplierType supplierType, String goodsName, Long productCategoryId, Pageable pageable) {
		List<Map<String,Object>> resultList=new ArrayList<>();
		Page<Goods> page=null;
		if (SupplierType.ONE == supplierType || SupplierType.THREE == supplierType || SupplierType.FOUR == supplierType){
			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
			if (SupplierType.FOUR == supplierType){
				supplyNeed=null;
			}else {
				supplier=null;
			}
			page=goodsDao.findPageByNeedShopProduct(supplyNeed, shop, supplier, goodsName, productCategoryId, pageable);
			for (Goods goods : page.getContent() ) {
				Map<String,Object> goodsMap=new HashMap<>();
				goodsMap.put("goodsId",goods.getId());
				goodsMap.put("image",goods.getImage());
				goodsMap.put("name",goods.getName());
				goodsMap.put("labels", goods.getLabels());
				if(goods.getSpecificationItems().size()>0){
					goodsMap.put("hasSpecifications",true);
				}else {
					goodsMap.put("hasSpecifications",false);
				}
				List<Map<String,Object>> productList=new ArrayList<>();
				Set<Product> products=goods.getProducts();
				for (Product product:products) {
					NeedShopProduct needShopProduct=needShopProductDao.getNeedShopProduct(supplyNeed,shop,null,product);
					if (needShopProduct == null){
						continue;
					}
					Map<String,Object> productMap=new HashMap<>();
					productMap.put("productId",product.getId());
					productMap.put("specifications",product.getSpecifications());
					productMap.put("minOrderQuantity",needShopProduct.getMinOrderQuantity());
					productList.add(productMap);
				}
				goodsMap.put("products",productList);
				resultList.add(goodsMap);
			}
		}else if (SupplierType.TWO.equals(supplierType)){
			SupplierSupplier supplierSupplier=supplierSupplierDao.find(relationId);
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			page=goodsDao.findPageBySupplierSupplier(supplierSupplier, need, goodsName, productCategoryId, pageable);
			for (Goods goods : page.getContent() ) {
				Map<String,Object> goodsMap=new HashMap<>();
				goodsMap.put("goodsId",goods.getId());
				goodsMap.put("image",goods.getImage());
				goodsMap.put("name",goods.getName());
				goodsMap.put("labels", goods.getLabels());
				if(goods.getSpecificationItems().size()>0){
					goodsMap.put("hasSpecifications",true);
				}else {
					goodsMap.put("hasSpecifications",false);
				}
				List<Map<String,Object>> productList=new ArrayList<>();
				Set<Product> products=goods.getProducts();
				for (Product product:products) {
					SupplierNeedProduct supplierNeedProduct=supplierNeedProductDao.findSupplierNeedProduct(supplierSupplier,need,product);
					if (supplierNeedProduct == null){
						continue;
					}
					SupplierProduct supplierProduct=supplierProductDao.findSupplierProduct(supplierSupplier,product);
					Map<String,Object> productMap=new HashMap<>();
					productMap.put("productId",product.getId());
					productMap.put("specifications",product.getSpecifications());
					productMap.put("minOrderQuantity",supplierProduct.getMinOrderQuantity());
					productList.add(productMap);
				}
				goodsMap.put("products",productList);
				resultList.add(goodsMap);
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("data",resultList);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return map;
	}

	@Override
	public Map<String, Object> findPageBySupply(Long relationId, Supplier supplier, SupplierType supplierType,
			String goodsName,ProductCategory productCategory, Pageable pageable) {
		List<Map<String,Object>> resultList=new ArrayList<>();
		Page<Goods> page=null;
		if (SupplierType.ONE == supplierType || SupplierType.FOUR == supplierType){
			page=goodsDao.findPage(supplier, goodsName, productCategory, pageable);
			for (Goods goods : page.getContent() ) {
				Map<String,Object> goodsMap=new HashMap<>();
				goodsMap.put("goodsId",goods.getId());
				goodsMap.put("image",goods.getImage());
				goodsMap.put("name",goods.getName());
				goodsMap.put("labels", goods.getLabels());
				if(goods.getSpecificationItems().size()>0){
					goodsMap.put("hasSpecifications",true);
				}else {
					goodsMap.put("hasSpecifications",false);
				}
				List<Map<String,Object>> productList=new ArrayList<>();
				Set<Product> products=goods.getProducts();
				for (Product product:products) {
					Map<String,Object> productMap=new HashMap<>();
					productMap.put("productId",product.getId());
					productMap.put("specifications",product.getSpecifications());
					productList.add(productMap);
				}
				goodsMap.put("products",productList);
				resultList.add(goodsMap);
			}
		}else if (SupplierType.TWO.equals(supplierType)){
			SupplierSupplier supplierSupplier=supplierSupplierDao.find(relationId);
			Long productCategoryId=productCategory == null ? null:productCategory.getId();
			page=goodsDao.findPageBySupplierSupplierGoods(supplierSupplier, goodsName, productCategoryId, pageable);
			for (Goods goods : page.getContent() ) {
				Map<String,Object> goodsMap=new HashMap<>();
				goodsMap.put("goodsId",goods.getId());
				goodsMap.put("image",goods.getImage());
				goodsMap.put("name",goods.getName());
				goodsMap.put("labels", goods.getLabels());
				if(goods.getSpecificationItems().size()>0){
					goodsMap.put("hasSpecifications",true);
				}else {
					goodsMap.put("hasSpecifications",false);
				}
				List<Map<String,Object>> productList=new ArrayList<>();
				Set<Product> products=goods.getProducts();
				for (Product product:products) {
					SupplierProduct supplierProduct=supplierProductDao.findSupplierProduct(supplierSupplier,product);
					if (supplierProduct == null){
						continue;
					}
					Map<String,Object> productMap=new HashMap<>();
					productMap.put("productId",product.getId());
					productMap.put("specifications",product.getSpecifications());
					productList.add(productMap);
				}
				goodsMap.put("products",productList);
				resultList.add(goodsMap);
			}
		}else if (SupplierType.THREE.equals(supplierType)){
			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
			Long productCategoryId=productCategory == null ? null:productCategory.getId();
			page=goodsDao.findPageBySupplyNeed(supplyNeed, goodsName, productCategoryId, pageable);
			for (Goods goods : page.getContent() ) {
				Map<String,Object> goodsMap=new HashMap<>();
				goodsMap.put("goodsId",goods.getId());
				goodsMap.put("image",goods.getImage());
				goodsMap.put("name",goods.getName());
				goodsMap.put("labels", goods.getLabels());
				if(goods.getSpecificationItems().size()>0){
					goodsMap.put("hasSpecifications",true);
				}else {
					goodsMap.put("hasSpecifications",false);
				}
				List<Map<String,Object>> productList=new ArrayList<>();
				Set<Product> products=goods.getProducts();
				for (Product product:products) {
					NeedProduct needProduct=needProductDao.findByNeedSupplier(supplyNeed, product);
					if (needProduct == null){
						continue;
					}
					Map<String,Object> productMap=new HashMap<>();
					productMap.put("productId",product.getId());
					productMap.put("specifications",product.getSpecifications());
					productList.add(productMap);
				}
				goodsMap.put("products",productList);
				resultList.add(goodsMap);
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("data",resultList);
		map.put("pageNumber", page.getPageNumber());
		map.put("totalPages", page.getTotalPages());
		return map;
	}

	@Override
	public boolean existLocalDistributionGoods(Supplier supplier, Goods goods) {
		return goodsDao.existLocalDistributionGoods(supplier, goods);
	}

	@Override
	public Map<String, Object> barCodeSave(ProductCenter productCenter,
			ProductCategory productCategory, List<Long> supplierIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		GoodsCenter goodsCenter = productCenter.getGoodsCenter();
		
		Long productId = 0l;
		Long pSupplierId = 0l;
		int i = 0;
		for(Long supplierId : supplierIds) {
			Supplier supplier = supplierDao.find(supplierId);
			List<Goods> goodsList = goodsDao.findByName(goodsCenter.getName(), supplier, Types.local);
			if(CollectionUtils.isNotEmpty(goodsList)) {
				Goods goods = goodsList.get(0);
				Product product = new Product();
				product.setBarCode(productCenter.getBarCode());
				product.setIsDefault(productCenter.getIsDefault());
				product.setPrice(productCenter.getPrice());
				product.setCost(productCenter.getCost());
				product.setMarketPrice(productCenter.getMarketPrice());
				product.setSpecificationValues(productCenter.getSpecificationValues());
				product.setStock(99999999);
				
				switch (goods.getType()) {
				case general:
					product.setExchangePoint(0L);
					break;
				case exchange:
					product.setPrice(BigDecimal.ZERO);
					product.setRewardPoint(0L);
					goods.setPromotions(null);
					break;
				case gift:
					product.setPrice(BigDecimal.ZERO);
					product.setRewardPoint(0L);
					product.setExchangePoint(0L);
					goods.setPromotions(null);
					break;
				}
				if (product.getMarketPrice() == null) {
					product.setMarketPrice(calculateDefaultMarketPrice(product
							.getPrice()));
				}
				if (product.getRewardPoint() == null) {
					product.setRewardPoint(calculateDefaultRewardPoint(product
							.getPrice()));
				}
				
				product.setAllocatedStock(0);
				product.setGoods(goods);
				product.setCartItems(null);
				product.setOrderItems(null);
				product.setShippingItems(null);
				product.setProductNotifies(null);
				product.setStockLogs(null);
				product.setGiftPromotions(null);

				setValue(product,goods.getSupplier());
				productDao.persist(product);
				
				if(i == 0){
					productId = product.getId();
					pSupplierId = supplier.getId();
				}
				i++;
			}else {
				Goods goods = new Goods();
				goods.setName(goodsCenter.getName());
				GoodsCenter.Unit unit = goodsCenter.getUnit();
				if(unit != null) {
					if(unit.toString().equals(Goods.Unit.box.toString())) {
						goods.setUnit(Goods.Unit.box);
					}else if(unit.toString().equals(Goods.Unit.bottle.toString())) {
						goods.setUnit(Goods.Unit.bottle);
					}else if(unit.toString().equals(Goods.Unit.bag.toString())) {
						goods.setUnit(Goods.Unit.bag);
					}else if(unit.toString().equals(Goods.Unit.frame.toString())) {
						goods.setUnit(Goods.Unit.frame);
					}else if(unit.toString().equals(Goods.Unit.pack.toString())) {
						goods.setUnit(Goods.Unit.pack);
					}else if(unit.toString().equals(Goods.Unit.one.toString())) {
						goods.setUnit(Goods.Unit.one);
					}else if(unit.toString().equals(Goods.Unit.pieces.toString())) {
						goods.setUnit(Goods.Unit.pieces);
					}else if(unit.toString().equals(Goods.Unit.barrel.toString())) {
						goods.setUnit(Goods.Unit.barrel);
					}else if(unit.toString().equals(Goods.Unit.copies.toString())) {
						goods.setUnit(Goods.Unit.copies);
					}else if(unit.toString().equals(Goods.Unit.jin.toString())) {
						goods.setUnit(Goods.Unit.jin);
					}else if(unit.toString().equals(Goods.Unit.g.toString())) {
						goods.setUnit(Goods.Unit.g);
					}else if(unit.toString().equals(Goods.Unit.tank.toString())) {
						goods.setUnit(Goods.Unit.tank);
					}else if(unit.toString().equals(Goods.Unit.rise.toString())) {
						goods.setUnit(Goods.Unit.rise);
					}else if(unit.toString().equals(Goods.Unit.bar.toString())) {
						goods.setUnit(Goods.Unit.bar);
					}else if(unit.toString().equals(Goods.Unit.only.toString())) {
						goods.setUnit(Goods.Unit.only);
					}else if(unit.toString().equals(Goods.Unit.volume.toString())) {
						goods.setUnit(Goods.Unit.volume);
					}else if(unit.toString().equals(Goods.Unit.listen.toString())) {
						goods.setUnit(Goods.Unit.listen);
					}
				}
				List<Label> goodsCenters = goodsCenter.getLabels();
				List<Goods.Label> labels = new ArrayList<Goods.Label>();
				for(Label label : goodsCenters) {
					if(label.toString().equals(Goods.Label.newProducts.toString())) {
						labels.add(Goods.Label.newProducts);
					}else if(label.toString().equals(Goods.Label.selling.toString())) {
						labels.add(Goods.Label.selling);
					}else if(label.toString().equals(Goods.Label.promotions.toString())) {
						labels.add(Goods.Label.promotions);
					}else if(label.toString().equals(Goods.Label.specialOffer.toString())) {
						labels.add(Goods.Label.specialOffer);
					}else if(label.toString().equals(Goods.Label.popularity.toString())) {
						labels.add(Goods.Label.popularity);
					}else if(label.toString().equals(Goods.Label.explosions.toString())) {
						labels.add(Goods.Label.explosions);
					}
				}
				goods.setLabels(labels);
				goods.setIntroduction(goodsCenter.getIntroduction());
				goods.setIsMarketable(true);
				goods.setIsDelivery(true);
				goods.setIsList(true);
				goods.setIsTop(false);
				goods.setType(Goods.Type.general);
				goods.setPrice(goodsCenter.getPrice());
				goods.setBarCode(goodsCenter.getBarCode());
				goods.setSpecificationItems(goodsCenter.getSpecificationItems());
				goods.setTypes(Types.local);
				goods.setProductCategory(productCategory);
				goods.setImage(goodsCenter.getImage());
				goods.setImages(goods.getImages());
				
				if(goods.hasSpecification()) {
					List<Product> products = new ArrayList<Product>();
					Product product = new Product();
					product.setBarCode(productCenter.getBarCode());
					product.setCost(productCenter.getCost());
					product.setIsDefault(productCenter.getIsDefault());
					product.setPrice(productCenter.getPrice());
					product.setSpecificationValues(productCenter.getSpecificationValues());
					product.setStock(99999999);
					
					products.add(product);
					this.save(goods, products, supplier);
					if(i == 0){
						productId = products.get(0).getId();
						pSupplierId = supplier.getId();
					}
				}else {
					//ProductCenter productCenter = goodsCenter.getDefaultProduct();
					Product product = new Product();
					product.setBarCode(productCenter.getBarCode());
					product.setCost(productCenter.getCost());
					product.setIsDefault(productCenter.getIsDefault());
					product.setPrice(productCenter.getPrice());
					product.setSpecificationValues(productCenter.getSpecificationValues());
					product.setStock(99999999);
					this.save(goods, product, supplier);
					if(i == 0){
						productId = product.getId();
						pSupplierId = supplier.getId();
					}
				}
				i++;
			}
			
		}
		map.put("supplierId", pSupplierId);
		map.put("productId", productId);
		return map;
	}

	@Override
	public List<Goods> findbyProductCategoryList(ProductCategory productCategory) {
		return goodsDao.findbyProductCategoryList(productCategory);
	}

}