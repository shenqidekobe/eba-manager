/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.CategoryCenterDao;
import com.microBusiness.manage.dao.ProductCategoryDao;
import com.microBusiness.manage.dao.SpecificationDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.SpecificationCenter;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.ProductCategoryService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("productCategoryServiceImpl")
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory, Long> implements ProductCategoryService {

	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;
	@Resource
	private SpecificationDao specificationDao;
	@Resource
	private CategoryCenterDao categoryCenterdao;
	
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots() {
		return productCategoryDao.findRoots(null);
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count, Supplier supplier) {
		return productCategoryDao.findRoots(count, supplier);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count) {
		return productCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findRoots(Integer count, boolean useCache) {
		return productCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findTree() {
		return productCategoryDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public ProductCategory save(ProductCategory productCategory) {
		Assert.notNull(productCategory);

		setValue(productCategory);
		return super.save(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory) {
		Assert.notNull(productCategory);

		setValue(productCategory);
		for (ProductCategory children : productCategoryDao.findChildren(productCategory, true, null)) {
			setValue(children);
		}
		return super.update(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory, String... ignoreProperties) {
		return super.update(productCategory, ignoreProperties);
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
	public void delete(ProductCategory productCategory) {
		super.delete(productCategory);
	}

	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findTree(Supplier supplier , String searchName) {
		return productCategoryDao.findChildren(null, true, null , supplier , searchName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findByTemporary(Need need, Long relationId) {
		return productCategoryDao.findByTemporary(need , relationId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findByFormal(Need need, Long relationId) {
		return productCategoryDao.findByFormal(need ,relationId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findByTemporary(Long needId, Long supplierId) {
		return productCategoryDao.findByTemporary(needId , supplierId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findByFormal(Long needId, Long supplierId) {
		return productCategoryDao.findByFormal(needId , supplierId);
	}

	@Override
	public List<ProductCategory> findByAllSupplier(Long supplierId) {
		return productCategoryDao.findByAllSupplier(supplierId);
	}

	/**
	 * @param supplierRelationId
	 * @return
	 */
	@Override
	public List<ProductCategory> findByAssSupplier(Long supplierRelationId) {
		return productCategoryDao.findByAssSupplier(supplierRelationId);
	}

	@Override
	public List<ProductCategory> findLike(Long productCategoryId) {
		return productCategoryDao.findLike(productCategoryId);
	}

	@Override
	public List<ProductCategory> findByGradeAndSupplierSuppler(SupplierSupplier supplierSupplier) {
		// TODO Auto-generated method stub
		return productCategoryDao.findByGradeAndSupplierSuppler(supplierSupplier);
	}
	
	@Override
	public List<ProductCategory> findBySupplyNeed(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution) {
		// TODO Auto-generated method stub
		return productCategoryDao.findBySupplyNeed(supplyNeed,supplier,cruuSupplier,hasDistribution);
	}

	@Override
	public List<ProductCategory> findTreeList(Supplier supplier,
			String searchName) {
		return productCategoryDao.findChildrenList(null, true, null , supplier , searchName);
	}
	
	@Override
	public List<ProductCategory> findByGradeAndNeedByFormal(SupplierSupplier supplierSupplier,Need need) {
		// TODO Auto-generated method stub
		return productCategoryDao.findByGradeAndNeedByFormal(supplierSupplier,need);
	}

	@Override
	public List<ProductCategory> findByAssSupplier(Long supplierId, Long bySupplierId) {
		return productCategoryDao.findByAssSupplier(supplierId , bySupplierId);
	}

	@Override
	public List<ProductCategory> findByParent(Supplier supplier, ProductCategory parent, String name) {
		return productCategoryDao.findByParent(supplier, parent, name);
	}

	@Override
	public List<ProductCategory> findByParentMember(Member member, ProductCategory parent, String name) {
		return productCategoryDao.findByParent(member, parent, name);
	}
	
	@Override
	public List<ProductCategory> findRootByMember(Member member) {

		return productCategoryDao.findRootByMember(member);
	}

	@Override
	public List<ProductCategory> findTree(Member member, String searchName) {
		return productCategoryDao.findTree(member, searchName, null);
	}

	@Override
	public List<ProductCategory> findLocalChildren(
			ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findLocalChildren(productCategory, recursive, count);
	}

	@Override
	public List<ProductCategory> findBySupplierSuppler(Long supplierSupplierId) {
		return productCategoryDao.findBySupplierSuppler(supplierSupplierId);
	}

	@Override
	public List<ProductCategory> findBySupplyNeedId(Long supplyNeedId) {
		return productCategoryDao.findBySupplyNeedId(supplyNeedId);
	}

	@Override
	public List<ProductCategory> findBySupplierSupplerNeed(Long supplierSupplierId, Need need) {
		return productCategoryDao. findBySupplierSupplerNeed(supplierSupplierId, need);
	}

	@Override
	public List<ProductCategory> findBySupplyNeedShop(Long supplyNeedId, Long shopId,Long supplierId) {
		return productCategoryDao.findBySupplyNeedShop(supplyNeedId, shopId,supplierId);
	}

	@Override
	public ProductCategory save(CategoryCenter categoryCenter, Member member) {
		try {
			String categoryName1 = "";
			String categoryName2 = "";
			String categoryName3 = "";
			if (categoryCenter.getGrade() == 0) {
	    		categoryName1 = categoryCenter.getName();
			}
	    	if (categoryCenter.getGrade() == 1) {
	    		categoryName1 = categoryCenter.getParent().getName();
	    		categoryName2 = categoryCenter.getName();
			}
	    	if (categoryCenter.getGrade() == 2) {
	        	categoryName1 = categoryCenter.getParent().getParent().getName();
	    		categoryName2 = categoryCenter.getParent().getName();
	    		categoryName3 = categoryCenter.getName();
			}
			
			// 添加分类
			ProductCategory oneCategory = new ProductCategory();
			ProductCategory twoCategory = new ProductCategory();
			ProductCategory threeCategory = new ProductCategory();
			
			if (!categoryName1.equals("")) {
				List<ProductCategory> list = productCategoryDao.findByParent(member, null, categoryName1);
				if (list.size() > 0) {
					oneCategory = list.get(0);
				} else {
					ProductCategory productCategory = new ProductCategory();
					productCategory.setMember(member);
					productCategory.setName(categoryName1);
					productCategory.setGrade(0);
					oneCategory = saveProductCategory(productCategory);
				}
			} else {
				List<ProductCategory> list = productCategoryDao.findByParent(member, null, " ");
				if (list.size() > 0) {
					oneCategory = list.get(0);
				} else {
					ProductCategory productCategory = new ProductCategory();
					productCategory.setMember(member);
					productCategory.setName(categoryName1);
					productCategory.setGrade(0);
					oneCategory = saveProductCategory(productCategory);
				}
			}
			if (!categoryName2.equals("")) {
				List<ProductCategory> list = productCategoryDao.findByParent(member, oneCategory, categoryName2);
				if (list.size() > 0) {
					twoCategory = list.get(0);
				} else {
					ProductCategory productCategory = new ProductCategory();
					productCategory.setMember(member);
					productCategory.setName(categoryName2);
					productCategory.setGrade(1);
					productCategory.setParent(oneCategory);
					twoCategory = saveProductCategory(productCategory);
				}
			}
			if (!categoryName3.equals("")) {
				List<ProductCategory> list = productCategoryDao.findByParent(member, twoCategory, categoryName3);
				if (list.size() > 0) {
					threeCategory = list.get(0);
				} else {
					ProductCategory productCategory = new ProductCategory();
					productCategory.setMember(member);
					productCategory.setName(categoryName3);
					productCategory.setGrade(2);
					productCategory.setParent(twoCategory);
					threeCategory = saveProductCategory(productCategory);
				}
			}
			
			for (SpecificationCenter specificationCenter : categoryCenter.getSpecificationCenters()) {
				Specification specification = new Specification();
				specification.setName(specificationCenter.getName());
				List<String> options = specificationCenter.getOptions();
				specification.setOptions(options);
				
				// 安置规格
				if (categoryName3.equals("")) {
					if (categoryName2.equals("")) {
						specification = saveSpecification(specification, oneCategory, options, member);
					} else {
						specification = saveSpecification(specification, twoCategory, options, member);
					}
				} else {
					if (categoryName2.equals("")) {
						specification = saveSpecification(specification, twoCategory, options, member);
					} else {
						specification = saveSpecification(specification, threeCategory, options, member);
					}
				}
				
			}
			
			if (!categoryName1.equals("")) {
				if (categoryName2.equals("")) {
					return oneCategory;
				} else {
					if (categoryName3.equals("")) {
						return twoCategory;
					}else {
						return threeCategory;
					}
				}
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private ProductCategory saveProductCategory(ProductCategory productCategory) {
		setValue(productCategory);
		productCategory.setTypes(Types.local);
		productCategoryDao.persist(productCategory);
		return productCategory;
	}
	
	private Specification saveSpecification(Specification specification, ProductCategory productCategory, List<String> options, Member member) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(
				new UniquePredicate(), new Predicate() {
					public boolean evaluate(Object object) {
						String option = (String) object;
						return StringUtils.isNotEmpty(option);
					}
				}));

		List<Specification> list = specificationDao.findByName(specification.getName(), member,productCategory);
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
				specification.setTypes(Types.local);
				specification.setMember(member);
				specification.setProductCategory(productCategory);
				specificationDao.persist(specification);
			}
		}

		return specification;
	}

	@Override
	public List<ProductCategory> findByParent(Member member,
			ProductCategory parent, String name, Types types) {
		return productCategoryDao.findByParent(member, parent, name, types);
	}

}