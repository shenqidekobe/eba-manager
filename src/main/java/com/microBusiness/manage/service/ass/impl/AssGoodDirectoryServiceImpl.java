package com.microBusiness.manage.service.ass.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.dao.ass.AssGoodDirectoryDao;
import com.microBusiness.manage.dao.ass.AssGoodsDao;
import com.microBusiness.manage.dao.ass.AssProductDao;
import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Goods.Label;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.ass.AssGoodDirectoryService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

@Service
public class AssGoodDirectoryServiceImpl extends BaseServiceImpl<AssGoodDirectory, Long> implements AssGoodDirectoryService{

	@Resource(name = "assGoodDirectoryDaoImpl")
	private AssGoodDirectoryDao assGoodDirectoryDao;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource(name = "assCustomerRelationDaoImpl")
	private AssCustomerRelationDao assCustomerRelationDao;
	@Resource
	private AssGoodsDao assGoodsDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "assProductDaoImpl")
	private AssProductDao assProductDao;
	@Resource
	private AssChildMemberDao assChildMemberDao;
	@Resource
	private AssUpdateTipsDao assUpdateTipsDao;
	
	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return assGoodsDao.snExists(sn);
	}

	@Override
	public Page<AssGoodDirectory> findPage(String theme, Supplier supplier, Pageable pageable) {
		// TODO Auto-generated method stub
		return assGoodDirectoryDao.findPage(theme,supplier,pageable);
	}

	@Override
	public void save(String theme, String profiles, Supplier supplier,List<Goods> goods) {
		try {
			AssGoodDirectory assGoodDirectory=new AssGoodDirectory();
			assGoodDirectory.setTheme(theme);
			assGoodDirectory.setProfiles(profiles);
			assGoodDirectory.setSupplier(supplier);
			
			List<Goods> goodsList=new ArrayList<>();
			for (Goods goods2 : goods) {
				goods2=goodsDao.find(goods2.getId());
				goodsList.add(goods2);
			}
			assGoodDirectory.setGoods(goodsList);
			assGoodDirectoryDao.persist(assGoodDirectory);
			
			//添加更新提示
			List<AssChildMember> assChildMembers = assChildMemberDao.findList(supplier);
			for(AssChildMember assChildMember : assChildMembers) {
				AssUpdateTips updateTips = new AssUpdateTips();
				updateTips.setAssChildMember(assChildMember);
				updateTips.setAssGoodDirectory(assGoodDirectory);
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				updateTips.setType(AssUpdateTips.Type.companyGoods);
				assUpdateTipsDao.persist(updateTips);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Long id, String profiles, List<Goods> goods) {
		AssGoodDirectory assGoodDirectory=assGoodDirectoryDao.find(id);
		assGoodDirectory.setProfiles(profiles);
		
		List<Goods> goodsList=new ArrayList<>();
		for (Goods goods2 : goods) {
			goods2=goodsDao.find(goods2.getId());
			goodsList.add(goods2);
		}
		assGoodDirectory.setGoods(goodsList);
		//添加更新提示
		List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(null, assGoodDirectory, null, AssUpdateTips.Type.companyGoods);
		for(AssUpdateTips updateTips : assUpdateTips) {
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
			assUpdateTipsDao.merge(updateTips);
		}
	}

	@Override
	public Page<AssGoodDirectory> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		return assGoodDirectoryDao.findPage(assChildMember, pageable);
	}

	/**
	 * 同步商品目录
	 */
	/**
	 * @param assGoodDirectory
	 * @param assChildMember
	 * @return
	 */
	@Override
	public void copyGoodsCatalogue(AssGoodDirectory assGoodDirectory , AssChildMember assChildMember) {
		Assert.notNull(assGoodDirectory);
		Assert.isTrue(!assGoodDirectory.isNew());
		
		Supplier supplier = assGoodDirectory.getSupplier();
		//查询重复的供应商
		AssCustomerRelation assCustomerRelation = assCustomerRelationDao.
				inviteNameExists(supplier.getName(), assGoodDirectory.getTheme(), AssCustomerRelation.SourceType.syncBackstage,assChildMember);
		//查询目录下面的商品
		List<Goods> goodsList = assGoodDirectory.getGoods();
		
		if(null == assCustomerRelation) {
			//复制供应商
			AssCustomerRelation customerRelation = new AssCustomerRelation();
			customerRelation.setClientName(supplier.getName());
			customerRelation.setTheme(assGoodDirectory.getTheme());
			customerRelation.setProfiles(assGoodDirectory.getProfiles());
			customerRelation.setAssChildMember(assChildMember);
			customerRelation.setAssGoodDirectory(assGoodDirectory);
			customerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
			customerRelation.setSourceType(AssCustomerRelation.SourceType.syncBackstage);
			customerRelation.setAdminName(assChildMember.getAdmin().getName());
			assCustomerRelationDao.persist(customerRelation);
			for(Goods goods : goodsList) {
				//同步商品
				addGoods(goods, customerRelation);
			}
		} else {
			//修改供应商
			assCustomerRelation.setTheme(assGoodDirectory.getTheme());
			assCustomerRelation.setProfiles(assGoodDirectory.getProfiles());
			assCustomerRelationDao.merge(assCustomerRelation);
			
			List<Goods> pGoods = assGoodsDao.find(assCustomerRelation);
			//需要新增的商品
			List<Goods> addGoods = (List<Goods>) CollectionUtils.subtract(goodsList , pGoods);
			for(Goods goods : addGoods) {
				addGoods(goods, assCustomerRelation);
			}
			//需要删除的商品
			List<Goods> delGoods = (List<Goods>)CollectionUtils.subtract(pGoods , goodsList);
			for(Goods goods : delGoods) {
				AssGoods assGoods = assGoodsDao.find(goods, assCustomerRelation);
				Set<AssProduct> assProducts = assGoods.getAssProducts();
				for(AssProduct assProduct : assProducts) {
					assProduct.setDeleted(true);
					assProductDao.merge(assProduct);
				}
				assGoods.setDeleted(true);
				assGoodsDao.merge(assGoods);
				
			}
			//需要修改的商品
	        List<Goods> updateGoods = (List<Goods>)CollectionUtils.retainAll(pGoods , goodsList);
			for(Goods goods : updateGoods) {
				AssGoods assGood = assGoodsDao.find(goods, assCustomerRelation);
				List<String> images = new ArrayList<String>();
				if(!StringUtils.isEmpty(goods.getImage())) {
					images.add(goods.getImage());
				}
				List<String> imageList = goods.getImages();
				for(String str : imageList) {
					images.add(str);
				}
				assGood.setImage(images);
				if(null != goods.getUnit()) {
					if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.box.toString())) {
						assGood.setUnit(AssGoods.Unit.box);
					} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bottle.toString())) {
						assGood.setUnit(AssGoods.Unit.bottle);
					} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bag.toString())) {
						assGood.setUnit(AssGoods.Unit.bag);
					} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.frame.toString())) {
						assGood.setUnit(AssGoods.Unit.frame);
					} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.pack.toString())) {
						assGood.setUnit(AssGoods.Unit.pack);
					}
				}
				assGood.setCreateDate(goods.getCreateDate());
				assGood.setAssCustomerRelation(assCustomerRelation);
				
				List<Label> list = goods.getLabels();
				List<com.microBusiness.manage.entity.ass.AssGoods.Label> labels = new ArrayList<AssGoods.Label>();
				for(Label label : list) {
					if(label.toString().equalsIgnoreCase(AssGoods.Label.newProducts.toString())) {
						labels.add(AssGoods.Label.newProducts);
					}else if(label.toString().equalsIgnoreCase(AssGoods.Label.selling.toString())) {
						labels.add(AssGoods.Label.selling);
					}else if(label.toString().equalsIgnoreCase(AssGoods.Label.promotions.toString())) {
						labels.add(AssGoods.Label.promotions);
					}else if(label.toString().equalsIgnoreCase(AssGoods.Label.specialOffer.toString())) {
						labels.add(AssGoods.Label.specialOffer);
					}else if(label.toString().equalsIgnoreCase(AssGoods.Label.popularity.toString())) {
						labels.add(AssGoods.Label.popularity);
					}else if(label.toString().equalsIgnoreCase(AssGoods.Label.explosions.toString())) {
						labels.add(AssGoods.Label.explosions);
					}
				}
				assGood.setLabels(labels);
				assGood.setGoods(goods);
				assGood.setSourceType(AssGoods.SourceType.syncBackstage);
				assGood.setDetails(goods.getIntroduction());
				assGood.setName(goods.getName());
				assGoodsDao.merge(assGood);
				
				Set<AssProduct> set = assGood.getAssProducts();
				Set<Product> set2 = goods.getProducts();
				for(Product product : set2) {
					boolean b=false;
					for(AssProduct assProduct : set) {
						if(product.getId() == assProduct.getProduct().getId()) {
							b=true;
							assProduct.setAssGoods(assGood);
							assProduct.setCreateDate(product.getCreateDate());
							List<SpecificationValue> specificationValues = product.getSpecificationValues();
							String str = "";
							for(SpecificationValue spe : specificationValues) {
								if(str.equals("")) {
									str = spe.getValue();
								}else {
									str +=","+spe.getValue();
								}
							}
							assProduct.setSpecification(str);
							assProduct.setProduct(product);
							assProduct.setModifyDate(product.getModifyDate());
							assProduct.setSourceType(AssProduct.SourceType.syncBackstage);
							assProductDao.merge(assProduct);
						}
					}
					if(!b) {
						AssProduct assProduct = new AssProduct();
						assProduct.setAssGoods(assGood);
						assProduct.setCreateDate(product.getCreateDate());
						List<SpecificationValue> specificationValues = product.getSpecificationValues();
						String str = "";
						for(SpecificationValue spe : specificationValues) {
							if(str.equals("")) {
								str = spe.getValue();
							}else {
								str +=","+spe.getValue();
							}
							
						}
						assProduct.setSpecification(str);
						assProduct.setProduct(product);
						assProduct.setSourceType(AssProduct.SourceType.syncBackstage);
						setValue(assProduct);
						assProductDao.persist(assProduct);
					}
					
				}
				for(AssProduct assProduct : set) {
					boolean b = false ;
					for(Product product : set2) {
						if(assProduct.getProduct().getId() == product.getId()) {
							b = true;
						}
					}
					if(!b) {
						assProduct.setDeleted(true);
						assProductDao.merge(assProduct);
					}
				}
			//结尾	
			}
		}
	}
	
	/**
	 * 同步商品
	 */
	@Override
	public void copyGoods(AssChildMember assChildMember,AssGoodDirectory assGoodDirectory, Goods goods) {
		Assert.notNull(assGoodDirectory);
		Assert.isTrue(!assGoodDirectory.isNew());
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		
		Supplier supplier = assGoodDirectory.getSupplier();
		//查询重复的供应商
		AssCustomerRelation assCustomerRelation = assCustomerRelationDao.
				inviteNameExists(supplier.getName(), assGoodDirectory.getTheme(), AssCustomerRelation.SourceType.syncBackstage, assChildMember);
		
		if(null == assCustomerRelation || null == assCustomerRelation.getId()) {
			//复制供应商
			AssCustomerRelation customerRelation = new AssCustomerRelation();
			customerRelation.setClientName(supplier.getName());
			customerRelation.setTheme(assGoodDirectory.getTheme());
			customerRelation.setProfiles(assGoodDirectory.getProfiles());
			customerRelation.setAssChildMember(assChildMember);
			customerRelation.setAssGoodDirectory(assGoodDirectory);
			customerRelation.setShareType(AssCustomerRelation.ShareType.noshare);
			customerRelation.setSourceType(AssCustomerRelation.SourceType.syncBackstage);
			customerRelation.setAdminName(assChildMember.getAdmin().getName());
			assCustomerRelationDao.persist(customerRelation);
			//同步单个商品
			addGoods(goods, customerRelation);
		}else {
			//修改供应商
			assCustomerRelation.setTheme(assGoodDirectory.getTheme());
			assCustomerRelation.setProfiles(assGoodDirectory.getProfiles());
			assCustomerRelationDao.merge(assCustomerRelation);
			
			List<AssGoods> assGoods = assCustomerRelation.getAssGoods();
			boolean exist = false;
			Long assGoodsId = null;
			for(AssGoods assGoods2 : assGoods) {
				Goods goods2 = assGoods2.getGoods();
				if(goods2.getId() == goods.getId()) {
					exist = true;
					assGoodsId = assGoods2.getId();
					
					AssGoods pAssGoods = assGoodsDao.find(assGoods2.getId());
					List<String> images = new ArrayList<String>();
					if(!StringUtils.isEmpty(goods.getImage())) {
						images.add(goods.getImage());
					}
					List<String> imageList = goods.getImages();
					for(String str : imageList) {
						images.add(str);
					}
					pAssGoods.setImage(images);
					if(null != goods.getUnit()) {
						if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.box.toString())) {
							pAssGoods.setUnit(AssGoods.Unit.box);
						} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bottle.toString())) {
							pAssGoods.setUnit(AssGoods.Unit.bottle);
						} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bag.toString())) {
							pAssGoods.setUnit(AssGoods.Unit.bag);
						} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.frame.toString())) {
							pAssGoods.setUnit(AssGoods.Unit.frame);
						} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.pack.toString())) {
							pAssGoods.setUnit(AssGoods.Unit.pack);
						}
					}
					pAssGoods.setCreateDate(goods.getCreateDate());
					pAssGoods.setAssCustomerRelation(assCustomerRelation);
					
					List<Label> list = goods.getLabels();
					List<com.microBusiness.manage.entity.ass.AssGoods.Label> labels = new ArrayList<AssGoods.Label>();
					for(Label label : list) {
						if(label.toString().equalsIgnoreCase(AssGoods.Label.newProducts.toString())) {
							labels.add(AssGoods.Label.newProducts);
						}else if(label.toString().equalsIgnoreCase(AssGoods.Label.selling.toString())) {
							labels.add(AssGoods.Label.selling);
						}else if(label.toString().equalsIgnoreCase(AssGoods.Label.promotions.toString())) {
							labels.add(AssGoods.Label.promotions);
						}else if(label.toString().equalsIgnoreCase(AssGoods.Label.specialOffer.toString())) {
							labels.add(AssGoods.Label.specialOffer);
						}else if(label.toString().equalsIgnoreCase(AssGoods.Label.popularity.toString())) {
							labels.add(AssGoods.Label.popularity);
						}else if(label.toString().equalsIgnoreCase(AssGoods.Label.explosions.toString())) {
							labels.add(AssGoods.Label.explosions);
						}
					}
					pAssGoods.setLabels(labels);
					pAssGoods.setGoods(goods);
					pAssGoods.setSourceType(AssGoods.SourceType.syncBackstage);
					pAssGoods.setDetails(goods.getIntroduction());
					pAssGoods.setName(goods.getName());
					assGoodsDao.merge(pAssGoods);
					
					Set<AssProduct> set = pAssGoods.getAssProducts();
					Set<Product> set2 = goods.getProducts();
					for(Product product : set2) {
						boolean b=false;
						for(AssProduct assProduct : set) {
							if(product.getId() == assProduct.getProduct().getId()) {
								b=true;
								assProduct.setAssGoods(pAssGoods);
								assProduct.setCreateDate(product.getCreateDate());
								List<SpecificationValue> specificationValues = product.getSpecificationValues();
								String str = "";
								for(SpecificationValue spe : specificationValues) {
									if(str.equals("")) {
										str = spe.getValue();
									}else {
										str +=","+spe.getValue();
									}
								}
								assProduct.setSpecification(str);
								assProduct.setProduct(product);
								assProduct.setModifyDate(product.getModifyDate());
								assProduct.setSourceType(AssProduct.SourceType.syncBackstage);
								assProductDao.merge(assProduct);
							}
						}
						if(!b) {
							AssProduct assProduct = new AssProduct();
							assProduct.setAssGoods(pAssGoods);
							assProduct.setCreateDate(product.getCreateDate());
							List<SpecificationValue> specificationValues = product.getSpecificationValues();
							String str = "";
							for(SpecificationValue spe : specificationValues) {
								if(str.equals("")) {
									str = spe.getValue();
								}else {
									str +=","+spe.getValue();
								}
								
							}
							assProduct.setSpecification(str);
							assProduct.setProduct(product);
							assProduct.setSourceType(AssProduct.SourceType.syncBackstage);
							setValue(assProduct);
							assProductDao.persist(assProduct);
						}
						
					}
					for(AssProduct assProduct : set) {
						boolean b = false ;
						for(Product product : set2) {
							if(assProduct.getProduct().getId() == product.getId()) {
								b = true;
							}
						}
						if(!b) {
							assProduct.setDeleted(true);
							assProductDao.merge(assProduct);
						}
					}

				}
			}
			//商品不存在，新增商品
			if(!exist) {
				addGoods(goods, assCustomerRelation);
			}
		}

	}
	
	//新增单个商品
	private void addGoods(Goods goods , AssCustomerRelation assCustomerRelation) {
		//复制商品
		AssGoods assGoods = new AssGoods();
		assGoods.setName(goods.getName());
		
		List<String> images = new ArrayList<String>();
		if(!StringUtils.isEmpty(goods.getImage())) {
			images.add(goods.getImage());
		}
		List<String> imageList = goods.getImages();
		for(String str : imageList) {
			images.add(str);
		}
		assGoods.setImage(images);
		if(null != goods.getUnit()) {
			if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.box.toString())) {
				assGoods.setUnit(AssGoods.Unit.box);
			} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bottle.toString())) {
				assGoods.setUnit(AssGoods.Unit.bottle);
			} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.bag.toString())) {
				assGoods.setUnit(AssGoods.Unit.bag);
			} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.frame.toString())) {
				assGoods.setUnit(AssGoods.Unit.frame);
			} else if(goods.getUnit().toString().equalsIgnoreCase(AssGoods.Unit.pack.toString())) {
				assGoods.setUnit(AssGoods.Unit.pack);
			}
		}
		assGoods.setCreateDate(goods.getCreateDate());
		assGoods.setAssCustomerRelation(assCustomerRelation);
		
		List<Label> list = goods.getLabels();
		List<com.microBusiness.manage.entity.ass.AssGoods.Label> labels = new ArrayList<AssGoods.Label>();
		for(Label label : list) {
			if(label.toString().equalsIgnoreCase(AssGoods.Label.newProducts.toString())) {
				labels.add(AssGoods.Label.newProducts);
			}else if(label.toString().equalsIgnoreCase(AssGoods.Label.selling.toString())) {
				labels.add(AssGoods.Label.selling);
			}else if(label.toString().equalsIgnoreCase(AssGoods.Label.promotions.toString())) {
				labels.add(AssGoods.Label.promotions);
			}else if(label.toString().equalsIgnoreCase(AssGoods.Label.specialOffer.toString())) {
				labels.add(AssGoods.Label.specialOffer);
			}else if(label.toString().equalsIgnoreCase(AssGoods.Label.popularity.toString())) {
				labels.add(AssGoods.Label.popularity);
			}else if(label.toString().equalsIgnoreCase(AssGoods.Label.explosions.toString())) {
				labels.add(AssGoods.Label.explosions);
			}
		}
		assGoods.setLabels(labels);
		assGoods.setGoods(goods);
		assGoods.setSourceType(AssGoods.SourceType.syncBackstage);
		assGoods.setDetails(goods.getIntroduction());
		setValue(assGoods);
		assGoodsDao.persist(assGoods);
		Set<Product> products = goods.getProducts();
		for(Product product : products) {
			//复制product
			AssProduct assProduct = new AssProduct();
			assProduct.setAssGoods(assGoods);
			assProduct.setCreateDate(product.getCreateDate());
			assProduct.setModifyDate(product.getModifyDate());
			List<SpecificationValue> specificationValues = product.getSpecificationValues();
			String str = "";
			for(SpecificationValue spe : specificationValues) {
				if(str.equals("")) {
					str = spe.getValue();
				}else {
					str +=","+spe.getValue();
				}
				
			}
			assProduct.setSpecification(str);
			assProduct.setProduct(product);
			assProduct.setSourceType(AssProduct.SourceType.syncBackstage);
			setValue(assProduct);
			assProductDao.persist(assProduct);
		}
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
	public List<AssGoodDirectory> findList(AssChildMember assChildMember) {
		return assGoodDirectoryDao.findList(assChildMember);
	}

	@Override
	public boolean themeExists(String theme, Supplier supplier) {
		// TODO Auto-generated method stub
		return assGoodDirectoryDao.themeExists(theme,supplier);
	}

	@Override
	public List<AssGoodDirectory> findList(Goods goods) {
		return assGoodDirectoryDao.findList(goods);
	}

	
	
	
}
