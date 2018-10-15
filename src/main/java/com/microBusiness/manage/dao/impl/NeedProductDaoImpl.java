package com.microBusiness.manage.dao.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.SupplyNeed.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.NeedProductDao;
import com.microBusiness.manage.dao.ProductDao;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class NeedProductDaoImpl extends BaseDaoImpl<NeedProduct, Long> implements NeedProductDao {

	@Resource
	private ProductDao productDao;
	
	@Override
	public NeedProduct findNeedProduct(NeedProduct needProduct) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
        Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
         restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products") , needProduct.getProducts()));
         restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed") , needProduct.getSupplyNeed()));
        criteriaQuery.where(restrictions);
        List<NeedProduct> list = super.findList(criteriaQuery, 0, 1, null, null);
        if(list != null && !list.isEmpty()){
        	return list.get(0);
        }
        return null;
	}
	
	@Override
	public List<Goods> findByKerword(Long needId,Long supplierId,String keywords,Integer isMarketable) {
		
		StringBuffer buff = new StringBuffer();
		String str1 = "SELECT DISTINCT g.id , g.* FROM " +
				"(SELECT np.products p  FROM t_need_product np WHERE np.supply_need IN " +
				"(SELECT id FROM t_supply_need sn WHERE 1=1 ";
		String str2 = ")) pid , xx_product pro ,xx_goods g WHERE p = pro.id AND pro.goods = g.id";
		buff.append(str1);
		if(needId!=null){
			buff.append(" AND sn.need = :needId ");
		}
		if(supplierId!=null){
			buff.append(" AND sn.supplier = :supplierId");
		}
		buff.append(str2);
		if(keywords!=null){
			buff.append(" AND g.name LIKE :keywords");
		}
		if(isMarketable==null ||isMarketable==1 ){
			buff.append(" AND g.is_marketable = 1 ");
		}else if(isMarketable==0){
			buff.append(" AND g.is_marketable = 0 ");
		}

		String sql = buff.toString();
		Query query = entityManager.createNativeQuery(sql,Goods.class);
		if(needId!=null){
			query.setParameter("needId", needId);
		}
		if(supplierId!=null){
			query.setParameter("supplierId", supplierId);
		}
		if(keywords!=null){
			query.setParameter("keywords", "%"+keywords+"%");
		}
		List<Goods> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public List<Goods> findByKerword(Long needId, Long supplierId, String keywords, Integer isMarketable, Pageable pageable) {
		//修改供应关系必须是供应中的
		StringBuffer buff = new StringBuffer();
		String str1 = "SELECT DISTINCT g.id , g.* FROM " +
				"(SELECT np.products p  FROM t_need_product np WHERE np.supply_need IN " +
				"(SELECT id FROM t_supply_need sn WHERE 1=1 and sn.status=0 ";
		String str2 = ")) pid , xx_product pro ,xx_goods g WHERE p = pro.id AND pro.goods = g.id";
		buff.append(str1);
		if(needId!=null){
			buff.append(" AND sn.need = :needId ");
		}
		if(supplierId!=null){
			buff.append(" AND sn.supplier = :supplierId");
		}
		buff.append(str2);
		if(keywords!=null){
			buff.append(" AND g.name LIKE :keywords");
		}
		if(isMarketable==null ||isMarketable==1 ){
			buff.append(" AND g.is_marketable = 1 ");
		}else if(isMarketable==0){
			buff.append(" AND g.is_marketable = 0 ");
		}

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				buff.append(" and g.product_category=:searchValue");
			}
		}

		//处理排序
		String orderProperty = pageable.getOrderProperty() ;
		Order.Direction direction = pageable.getOrderDirection();
		if(StringUtils.isNotEmpty(orderProperty) && null != direction){
			String directionStr = "asc";
			switch (direction){
				case asc:
					directionStr = "asc";
					break ;
				case desc:
					directionStr = "desc";
					break ;
			}
			if(orderProperty.equals("sales")){
				buff.append(" order by g.sales ").append(directionStr);
			}
		}else{
			buff.append(" order by g.create_date desc");
		}


		String sql = buff.toString();
		Query query = entityManager.createNativeQuery(sql,Goods.class);
		if(needId!=null){
			query.setParameter("needId", needId);
		}
		if(supplierId!=null){
			query.setParameter("supplierId", supplierId);
		}
		if(keywords!=null){
			query.setParameter("keywords", "%"+keywords+"%");
		}

		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				query.setParameter("searchValue", Long.valueOf(searchValue));
			}

		}

		List<Goods> resultList = query.getResultList();
		return resultList;

	}

	/**
	 * @param needProduct
	 * @return
	 */
	@Override
	public List<NeedProduct> findByParams(NeedProduct needProduct) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != needProduct.getSupplyNeed()){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("supplyNeed") , needProduct.getSupplyNeed())) ;
		}
		criteriaQuery.where(restrictions);

		return super.findList(criteriaQuery , null , null , null ,null );
	}

	/**
	 *
	 * @param product
	 * @param supplierId 供应商id
	 * @param needId 收货点id
	 * @return
	 */
	@Override
	public NeedProduct getProduct(Product product, Long supplierId , Long needId , SupplyNeed.Status status) {
		StringBuffer findSql = new StringBuffer();
		findSql.append("select needProduct.* from t_need_product needProduct ");
		findSql.append(" inner join t_supply_need supplyNeed on needProduct.supply_need=supplyNeed.id");
		findSql.append(" where 1=1");
		findSql.append(" and supplyNeed.supplier=:supplierId");
		findSql.append(" and supplyNeed.need=:needId");
		findSql.append(" and needProduct.products=:product");
		findSql.append(" and supplyNeed.status=:status") ;

		Query query = entityManager.createNativeQuery(findSql.toString() , NeedProduct.class);

		query.setParameter("supplierId" , supplierId);
		query.setParameter("needId" , needId) ;
		query.setParameter("product" , product.getId());
		query.setParameter("status" , status.ordinal());

		NeedProduct needProduct = (NeedProduct) query.getSingleResult();
		return needProduct;
	}

	@Override
	public NeedProduct findByNeedSupplier(SupplyNeed supplyNeed, Product product) {
		 CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
	        Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
	        criteriaQuery.select(root);
	        Predicate restrictions = criteriaBuilder.conjunction();
	        if (supplyNeed !=null) {
	        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed));
			}
	        if (product !=null) {
	        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products") , product));
			}
	        criteriaQuery.where(restrictions);
	        List<NeedProduct> list=entityManager.createQuery(criteriaQuery).getResultList();
	        if (list!=null && list.size()>0) {
				return list.get(0);
			}
	        return null;
	}

	@Override
	public List<NeedProduct> getNeeedProducts(SupplyNeed supplyNeed,
			Set<Product> products) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null !=supplyNeed){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed)) ;
		}
		if (null != products) {
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.in(root.get("products")).value(products)) ;
		}
		criteriaQuery.where(restrictions);

		return super.findList(criteriaQuery , null , null , null ,null );
	}

	@Override
	public List<NeedProduct> findNeedProductByProduct(Product product,Supplier supplier, List<Status> status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null !=product){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("products") , product)) ;
		}
		
		Subquery<SupplyNeed> productSubquery = criteriaQuery.subquery(SupplyNeed.class);
		Root<SupplyNeed> productSubqueryRoot = productSubquery.from(SupplyNeed.class);
		productSubquery.select(productSubqueryRoot);
		productSubquery.where(criteriaBuilder.and(criteriaBuilder.equal(productSubqueryRoot.get("supplier"), supplier),criteriaBuilder.in(productSubqueryRoot.get("status")).value(status)));
		
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("supplyNeed")).value(productSubquery));
		
		criteriaQuery.where(restrictions);

		return super.findList(criteriaQuery , null , null , null ,null );
	}

	@Override
	public Page<NeedProduct> findPage(SupplyNeed supplyNeed, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != supplyNeed){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed)) ;
		}
		criteriaQuery.where(restrictions);

		return super.findPage(criteriaQuery, pageable);
	}


	@Override
	public List<NeedProduct> findList(SupplyNeed supplyNeed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != supplyNeed){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed)) ;
		}
		
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery , null , null , null ,null );
	}


	/**@Override
	public List<NeedProduct> findList(SupplyNeed supplyNeed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NeedProduct> criteriaQuery = criteriaBuilder.createQuery(NeedProduct.class);
		Root<NeedProduct> root = criteriaQuery.from(NeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(null != supplyNeed){
			restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(root.get("supplyNeed") , supplyNeed)) ;
		}
		criteriaQuery.where(restrictions);

		return super.findList(criteriaQuery , null , null , null ,null );
	}**/

}
