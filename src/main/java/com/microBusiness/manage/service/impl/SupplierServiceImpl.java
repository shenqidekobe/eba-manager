package com.microBusiness.manage.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.*;
import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Supplier.Status;
import com.microBusiness.manage.service.SupplierService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mingbai on 2017/2/6.
 * 功能描述：
 * 修改记录：
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl<Supplier , Long> implements SupplierService {

	@Resource(name = "supplierDaoImpl")
	private SupplierDao supplierDao;
	@Value("${supplier.role.verified}")
	private Long verifiedRoleId ;
	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao ;
	@Resource
	private RoleDao roleDao ;
	@Resource
	private SupplyNeedDao supplyNeedDao ;
	@Resource
	private NeedProductDao needProductDao;
	@Resource
	private NeedShopProductDao needShopProductDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private SupplierNeedProductDao supplierNeedProductDao;
	@Resource
	private SupplierAssignRelationDao supplierAssignRelationDao;
	@Resource
	private SupplierSupplierDao supplierSupplierDao;
	@Resource
	private StockGoodsDao stockGoodsDao;
	@Resource
	private GoodsDao goodsDao;
	
    @Override
    @Transactional
    public void delete(Long... ids) {
        try {
            super.delete(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Transactional(readOnly = true)
	public boolean exists(String name) {
		return supplierDao.exists(name);
	}
    
    @Transactional(readOnly = true)
	public Page<Supplier> findPage(Pageable pageable , Supplier.Status status) {
		return supplierDao.findPage(pageable,status);
	}

	@Override
	public List<Supplier> findByStatus(Status... status) {
		// TODO Auto-generated method stub
		return supplierDao.findByStatus(status);
	}

	@Override
	public boolean update(Supplier supplier, Long adminId , String ...supplierIgnore) {

		super.update(supplier, supplierIgnore);
		if(supplier.getStatus().equals(Supplier.Status.verified)){

			Long[] roleIds = {verifiedRoleId};

			Admin admin = adminDao.find(adminId);

			List<Filter> filters = new ArrayList<>();

			filters.add(Filter.in("id" , Arrays.asList(roleIds)));

			List<Role> roles = roleDao.findList(null , null , filters , null);

			admin.setRoles(new HashSet<Role>(roles));

			/*Admin admin=new Admin();
			admin.setId(adminId);
			admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
			adminService.update(admin,"username","password","email","name","department","isEnabled","isLocked","loginFailureCount","lockedDate","loginDate","loginIp","lockKey","supplier");*/
		}

		return false;
	}

	/**
	 * 只获取相对与收货点并且生效的供应商
	 * @param need
	 * @param pageable
	 * @return
	 */
	@Override
	public List<Supplier> findFormal(Need need, Pageable pageable) {
		return supplierDao.findFormal(need , pageable) ;
	}

	/**
	 * 通过被供应企业查询供应的企业
	 *
	 * @param bySupplierId
	 * @return
	 */
	@Override
	public List<Supplier> getSupplierFromBy(Long bySupplierId) {
		return supplierDao.getSupplierFromBy(bySupplierId);
	}

	@Override
	public boolean existaName(String name, Long id) {
		return supplierDao.existaName(name, id);
	}


	/**
	 * 通过邀请码查询企业
	 *
	 * @param inviteCode
	 * @return
	 */
	@Override
	public Supplier getSupplierByInviteCode(String inviteCode) {
		return supplierDao.getSupplierByInviteCode(inviteCode);
	}

	@Override
	public boolean inviteCodeExists(String inviteCode) {
		return supplierDao.inviteCodeExists(inviteCode);
	}

	@Override
	public List<Supplier> recommendSupplierList(Long count) {
		return supplierDao.getSupplierList(count);
	}

	@Override
	public Page<Supplier> findPage(Admin admin, Pageable pageable, String name, Status status) {
		// 获取所有企业
		Boolean flag = null;
		if (status == Status.notCertified || status == Status.authenticationFailed || status == Status.certification) {
			flag = false;
		}
		if (status == Status.verified) {
			flag = true;
		}
		Page<Supplier> pageSupplier = supplierDao.findPage(pageable, name, flag);
		
//		if (admin != null) {
//			for (Supplier supplier : pageSupplier.getContent()) {
//				// 获取收藏企业
//				FavorCompany favorCompany = favorCompanyDao.getFavorCompanyIsNull(admin.getId(), supplier.getId());
//				if (favorCompany != null) {
//					supplier.setFavorCompany(favorCompany);
//				}
//			}
//		}

		return pageSupplier;
	}

	@Override
	public boolean updateSupplier(Long[] ids, Long[] idsNo) {
		try {
			// 推荐企业
			if (ids[0] != -1) {
				for (int i = 0; i < ids.length; i++) {
					Supplier supplier = this.find(ids[i]);
					supplier.setRecommendFlag(true);
					supplierDao.merge(supplier);
				}
			}
			
			// 取消推荐企业
			if (idsNo[0] != -1) {
				for (int i = 0; i < idsNo.length; i++) {
					Supplier supplier = this.find(idsNo[i]);
					supplier.setRecommendFlag(false);
					supplierDao.merge(supplier);
				}
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public List<Supplier> findBySupplierNeed(Need need) {
		return supplierDao.findBySupplierNeed(need);
	}

	@Override
	public List<SupplierDto> findSupplierListByMember(Member member , Long supplierId) {
		return supplierDao.findSupplierListByMember(member , supplierId);
	}

	@Override
	public List<SupplierDto> findFormal(Member member, Long supplierId) {
		return supplierDao.findFormal(member , supplierId);
	}

	@Override
	public Supplier findByAppId(String appId) {
		return supplierDao.findByAppId(appId);
	}


	@Override
	public boolean doesItExistName(String name, Types types, Member member) {
		return supplierDao.doesItExistName(name, types,member);
	}

	@Override
	public List<Supplier> getSupplierListByMember(Member member) {
		return supplierDao.getSupplierListByMember(member);
	}


	@Transactional
	@Override
	public void assignProducts(Shop shop, SupplierType supplierType, Supplier supplier, Long relationId, SupplyNeed.AssignedModel assignedModel, List<Product> productList) {

		if (SupplierType.ONE.equals(supplierType)){
			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			if (supplyNeed == null){
				supplyNeed = new SupplyNeed();
				supplyNeed.setNeed(need);
				supplyNeed.setAssignedModel(assignedModel);
				supplyNeed.setSupplier(supplier);
				supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
				supplyNeed.setOpenNotice(false);
				supplyNeed.setNoticeDay(5);
				supplyNeedDao.persist(supplyNeed);
			}else {
				supplyNeed.setAssignedModel(assignedModel);
				//删除后重新分配   NeedProduct
				Set<NeedProduct> needProducts=supplyNeed.getNeedProducts();
				for(NeedProduct needProduct:needProducts){
					needProductDao.remove(needProduct);
				}

				//删除后重新分配   NeedShopProduct
				List<NeedShopProduct> list=needShopProductDao.getList(supplyNeed,shop,null);
				for (NeedShopProduct needShopProduct :list) {
					needShopProductDao.remove(needShopProduct);
				}
			}
			//分配
			for (Product product : productList) {
				Product productEntity=productDao.find(product.getId());
				NeedProduct needProduct=new NeedProduct();
				needProduct.setSupplyNeed(supplyNeed);
				needProduct.setProducts(productEntity);
				needProduct.setMinOrderQuantity(product.getMinOrderQuantity());
				needProduct.setSupplyPrice(product.getSupplyPrice());
				needProductDao.persist(needProduct);

				NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setProducts(productEntity);
				needShopProduct.setSupplyNeed(supplyNeed);
				needShopProduct.setMinOrderQuantity(product.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(product.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
			}
		}else if (SupplierType.TWO.equals(supplierType)){
			SupplierSupplier supplierSupplier=supplierSupplierDao.find(relationId);
			Need need=shop.getNeeds().iterator().next();  //由于直营店必定对应一个need
			List<SupplierAssignRelation> supplierAssignRelations=supplierAssignRelationDao.findListBySupplier(supplierSupplier,need);
			//分配关系
			SupplierAssignRelation supplierAssignRelation;
			//是否已分配
			if (supplierAssignRelations.size() > 0){
				supplierAssignRelation=supplierAssignRelations.get(0);//如果已分配，有且只有一条分配关系
			}else {
				supplierAssignRelation=new SupplierAssignRelation();
				supplierAssignRelation.setNeed(need);
				supplierAssignRelation.setSupplyRelation(supplierSupplier);
			}
			//删除之前的分配
			if (supplierAssignRelation.getId() != null) {
				for (SupplierNeedProduct sNeedProduct : supplierAssignRelation.getSupplierNeedProducts()) {
					supplierNeedProductDao.remove(sNeedProduct);
				}
			}
			//分配
			List<SupplierNeedProduct> newSupplierNeedProductList=new ArrayList<>();
			for (Product product : productList) {
				SupplierNeedProduct supplierNeedProduct=new SupplierNeedProduct();
				supplierNeedProduct.setAssignRelation(supplierAssignRelation);
				supplierNeedProduct.setSupplyRelation(supplierSupplier);
				supplierNeedProduct.setNeed(need);
				supplierNeedProduct.setProducts(productDao.find(product.getId()));
				supplierNeedProduct.setSupplyPrice(product.getSupplyPrice());
				newSupplierNeedProductList.add(supplierNeedProduct);
			}
			supplierAssignRelation.setSupplierNeedProducts(newSupplierNeedProductList);
			supplierAssignRelationDao.persist(supplierAssignRelation);
		}else if (SupplierType.THREE.equals(supplierType)){
			//企业-->店铺  只有加盟店能分配这种供应  先判断店铺--need关系  没有就需要关联
			SupplyNeed supplyNeed=supplyNeedDao.find(relationId);
			Need need=supplyNeed.getNeed();
			if (!shop.exitsNeed(need)){
				shop.getNeeds().add(need);
			}
			//删除后重新分配   NeedShopProduct
			List<NeedShopProduct> list=needShopProductDao.getList(supplyNeed,shop,null);
			for (NeedShopProduct needShopProduct :list) {
				needShopProductDao.remove(needShopProduct);
			}
			//分配
			for (Product product : productList) {
				NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setProducts(productDao.find(product.getId()));
				needShopProduct.setSupplyNeed(supplyNeed);
				needShopProduct.setMinOrderQuantity(product.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(product.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
			}
		}else if (SupplierType.FOUR.equals(supplierType)){
			//删除后重新分配   NeedShopProduct
			List<NeedShopProduct> list=needShopProductDao.getList(null,shop,supplier);
			for (NeedShopProduct needShopProduct :list) {
				needShopProductDao.remove(needShopProduct);
			}
			//分配
			for (Product product : productList) {
				NeedShopProduct needShopProduct=new NeedShopProduct();
				needShopProduct.setShop(shop);
				needShopProduct.setSupplier(supplier);
				needShopProduct.setProducts(productDao.find(product.getId()));
				needShopProduct.setMinOrderQuantity(product.getMinOrderQuantity());
				needShopProduct.setSupplyPrice(product.getSupplyPrice());
				needShopProductDao.persist(needShopProduct);
			}
		}
		
		// 分配的商品加入库存
		for (Product product : productList) {
			product = productDao.find(product.getId());
			StockGoods stockGoods = stockGoodsDao.findByProduct(product, shop);
			if (stockGoods == null) {
				StockGoods stockGoodsNew = new StockGoods();
				stockGoodsNew.setProduct(product);
				stockGoodsNew.setActualStock(0);
				stockGoodsNew.setStatus(StockGoods.Status.hide);
				stockGoodsNew.setShop(shop);
				
				stockGoodsDao.persist(stockGoodsNew);
			}
		}

	}

	@Override
	public boolean deletedLocalSupplier(Supplier supplier, List<Goods> goods) {
		if(goods.size() > 0) {
			for(Goods gd : goods) {
				gd.setDeleted(true);
				goodsDao.merge(gd);
			}
		}
		supplier.setDeleted(true);
		supplierDao.merge(supplier);
		return true;
	}

	@Override
	public List<Supplier> getSupplierListByMember(Member member, ProductCenter productCenter, String name) {
		//获取 member 下面所有供应商
		List<Supplier> allSupplier = supplierDao.getSupplierListByMember(member) ;
		//处理已经存在的供应商
		List<Goods> exitsGoods = goodsDao.findByMember(member , name);
		Set<Supplier> allSupplierSet = new HashSet(allSupplier);
		Set<Supplier> exitsSupplier = new HashSet();
		if(CollectionUtils.isEmpty(exitsGoods)) {
			return new ArrayList<Supplier>(allSupplierSet);
		}
		List<SpecificationValue> specificationValues = productCenter.getSpecificationValues() ;
		if(CollectionUtils.isEmpty(specificationValues)) {
			for(Goods goods : exitsGoods) {
				if(!goods.hasSpecification()) {
					exitsSupplier.add(goods.getSupplier()) ;
				}
			}
			allSupplierSet.removeAll(exitsSupplier) ;
			return new ArrayList<Supplier>(allSupplierSet);
		}
		for(Goods goods : exitsGoods){
			Set<Product> products = goods.getProducts();
			for(Product product : products) {
				List<SpecificationValue> specificationValues2 = product.getSpecificationValues();
				for(SpecificationValue value : specificationValues2) {
					for(SpecificationValue value2 : specificationValues) {
						if(value.getValue().equals(value2.getValue())) {
							exitsSupplier.add(goods.getSupplier()) ;
							break;
						}
					}
				}
			}
		}
		
		allSupplierSet.removeAll(exitsSupplier) ;
		
		return new ArrayList<Supplier>(allSupplierSet);
	}
}
