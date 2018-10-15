package com.microBusiness.manage.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.CommodityReportDao;
import com.microBusiness.manage.dto.CommodityReportDto;
import com.microBusiness.manage.dto.CommodityStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.Supplier;

@Repository("commodityReportDaoImpl")
public class CommodityReportDaoImpl implements CommodityReportDao {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public List<CommodityReportDto> findBycommodityReport(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember) {
		/*StringBuffer findSql = new StringBuffer("select orderItem.name name,orderItem.specifications specification,COUNT(DISTINCT orders.id) orderNumber,");
		findSql.append("SUM(orderItem.quantity) orderQuantity,COUNT(DISTINCT supplier.id) customersNum,");
		findSql.append("SUM(orderItem.quantity*orderItem.price) orderAmount from xx_order orders");
		findSql.append(" INNER JOIN xx_member member on orders.member = member.id");
		findSql.append(" INNER JOIN t_need need on member.need = need.id");
		findSql.append(" INNER JOIN t_supplier supplier on need.supplier = supplier.id");
		findSql.append(" INNER JOIN t_supplier suppliers on orders.supplier = suppliers.id");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders where 1=1");*/
		List<Integer> typeOne=new ArrayList<>();
		typeOne.add(Order.Type.general.ordinal());
		typeOne.add(Order.Type.billGeneral.ordinal());
		
		
		List<Integer> typeTwo=new ArrayList<>();
		typeTwo.add(Order.Type.billDistribution.ordinal());
		
		StringBuffer findSql = new StringBuffer("select product.id productId,product.sn productSn, orderItem.name name,goods.source source,orderItem.specifications specification,COUNT(DISTINCT orders.id) orderNumber,");
		findSql.append("SUM(orderItem.quantity) orderQuantity,COUNT(DISTINCT orders.need) customersNum,");
		findSql.append("SUM(orderItem.price) orderAmount from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		findSql.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1 ");
		if(null != childMember) {
			findSql.append(" and orders.child_member=:childMember");
		}
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		if(null != supplier.getId()) {
			findSql.append(" AND (( orders.supplier =:supplier and orders.type in (:typeOne) ) or ( orders.to_supplier =:supplier and orders.type in (:typeTwo) )) ");
		}
		findSql.append(" GROUP BY productId ORDER BY orderQuantity DESC");

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != supplier.getId()){
			query.setParameter("supplier" , supplier.getId());
		}
		if(null != childMember){
			query.setParameter("childMember" , childMember.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		query.setParameter("typeOne", typeOne);
		query.setParameter("typeTwo", typeTwo);
		query.unwrap(SQLQuery.class).addScalar("productSn",StringType.INSTANCE).addScalar("productId",StringType.INSTANCE).addScalar("name",StringType.INSTANCE).addScalar("specification",StringType.INSTANCE)
		.addScalar("orderNumber",IntegerType.INSTANCE).addScalar("orderQuantity",IntegerType.INSTANCE)
		.addScalar("customersNum",IntegerType.INSTANCE).addScalar("orderAmount",BigDecimalType.INSTANCE)
		.addScalar("source", IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CommodityReportDto.class));

		List<CommodityReportDto> result = query.getResultList();
		return result;
	}
	
	@Override
	public List<CommodityReportDto> findBycommodityReport2(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember) {
		
		List<Integer> type=new ArrayList<>();
		type.add(Order.Type.billDistribution.ordinal());
		type.add(Order.Type.formal.ordinal());
		
		StringBuffer findSql = new StringBuffer("select product.id productId,product.sn productSn, orderItem.name name,goods.source source,orderItem.specifications specification,COUNT(DISTINCT orders.id) orderNumber,");
		findSql.append("SUM(orderItem.quantity) orderQuantity,COUNT(DISTINCT orders.member) customersNum,");
		findSql.append("SUM(orderItem.priceB) orderAmount from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		findSql.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1");
		if(null != childMember) {
			findSql.append(" and orders.child_member=:childMember");
		}
		
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		if(null != supplier.getId()) {
			findSql.append(" AND ( orders.supplier =:supplier and orders.type in (:type) ) ");
		}
		findSql.append(" GROUP BY productId ORDER BY orderQuantity DESC");

		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		if(null != childMember) {
			query.setParameter("childMember" , childMember);
		}
		if(null != supplier.getId()){
			query.setParameter("supplier" , supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		query.setParameter("type", type);
		query.unwrap(SQLQuery.class).addScalar("productSn",StringType.INSTANCE).addScalar("productId",StringType.INSTANCE).addScalar("name",StringType.INSTANCE).addScalar("specification",StringType.INSTANCE)
		.addScalar("orderNumber",IntegerType.INSTANCE).addScalar("orderQuantity",IntegerType.INSTANCE)
		.addScalar("customersNum",IntegerType.INSTANCE).addScalar("orderAmount",BigDecimalType.INSTANCE)
		.addScalar("source", IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CommodityReportDto.class));

		List<CommodityReportDto> result = query.getResultList();
		return result;
	}
	@Override
	public CommodityStatisticsDto findByCommodityStatistics(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember) {
		StringBuffer findSql = new StringBuffer("select  COUNT(DISTINCT product.id) numberOfGoods,COUNT(DISTINCT orders.need) numberOfCustomers,");
		findSql.append("COUNT(DISTINCT orders.id) orderTotal from xx_order orders");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product ON product.id = orderItem.product");
		findSql.append(" INNER JOIN t_supplier supplier on orders.supplier = supplier.id where 1=1");
		//findSql.append(" INNER JOIN xx_member member ON orders.member = member.id");
		//findSql.append(" INNER JOIN t_need need ON member.need = need.id");
		//findSql.append(" INNER JOIN t_supplier suppliers ON need.supplier = suppliers.id where 1=1");
		if(null != supplier.getId()) {
			findSql.append(" and (supplier.id=:supplierId or (orders.to_supplier=:supplierId and orders.type!=4))");
		}
		if(null != childMember) {
			findSql.append(" and orders.child_member=:childMember");
		}
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		findSql.append(" and orders.type !=1");
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		if(null != childMember){
			query.setParameter("childMember" , childMember.getId());
		}
		if(null != supplier.getId()){
			query.setParameter("supplierId" , supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		query.unwrap(SQLQuery.class).addScalar("orderTotal",LongType.INSTANCE).addScalar("numberOfGoods",LongType.INSTANCE).addScalar("numberOfCustomers",LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(CommodityStatisticsDto.class));
		
		CommodityStatisticsDto commodityStatisticsDto = (CommodityStatisticsDto) query.getSingleResult();
		return commodityStatisticsDto;
	}

	@Override
	public List<CommodityReportDto> purchaseOrderData(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status) {
		StringBuffer findSql = new StringBuffer("select goods.name name,goods.source source,product.sn productSn,orderItem.specifications specification,COUNT(DISTINCT orders.id) orderNumber,");
		findSql.append("SUM(orderItem.quantity) orderQuantity,COUNT(DISTINCT suppliers.id) suppliersNum,");
		findSql.append("SUM(orderItem.priceB) orderAmount from xx_order orders");
		//findSql.append(" INNER JOIN xx_member member on orders.member = member.id");
		//findSql.append(" INNER JOIN t_need need on member.need = need.id");
		//findSql.append(" INNER JOIN t_supplier supplier on need.supplier = supplier.id");
		findSql.append(" INNER JOIN t_supplier suppliers on orders.supplier = suppliers.id");
		findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
		findSql.append(" INNER JOIN xx_product product on orderItem.product = product.id");
		findSql.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1 ");
		if(null != supplier.getId()) {
			findSql.append(" and orders.to_supplier=:supplierId");
		}
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		findSql.append(" and orders.type !=1");
		findSql.append(" GROUP BY product.id ORDER BY orderQuantity DESC");

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != supplier.getId()){
			query.setParameter("supplierId" , supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		query.unwrap(SQLQuery.class).addScalar("productSn",StringType.INSTANCE).addScalar("name",StringType.INSTANCE).addScalar("specification",StringType.INSTANCE)
		.addScalar("orderNumber",IntegerType.INSTANCE).addScalar("orderQuantity",IntegerType.INSTANCE)
		.addScalar("suppliersNum",IntegerType.INSTANCE).addScalar("orderAmount",BigDecimalType.INSTANCE)
		.addScalar("source",IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CommodityReportDto.class));

		List<CommodityReportDto> result = query.getResultList();
		return result;
	}

	@Override
	public List<CommodityReportDto> categoryQuery(Date startDate, Date endDate,
			Supplier supplier, List<Integer> status, ChildMember childMember) {
		List<Integer> typeOne=new ArrayList<>();
		typeOne.add(Order.Type.general.ordinal());
		typeOne.add(Order.Type.billGeneral.ordinal());
		
		List<Integer> typeTwo=new ArrayList<>();
		typeTwo.add(Order.Type.billDistribution.ordinal());
		
		StringBuffer findSql = new StringBuffer("SELECT a.orderQuantity orderQuantity,category.name name,category.id categoryId,category.source source,a.orderNumber orderNumber,a.goodAmount goodAmount,a.customersNum customersNum");
		findSql.append(" from (SELECT COUNT(DISTINCT orders.id) orderNumber,SUM(orderItem.price) goodAmount,");
		findSql.append("COUNT(DISTINCT orders.need) customersNum,SUM(orderItem.quantity) orderQuantity,category.id,");
		findSql.append("CASE SUBSTRING_INDEX(category.tree_path, ',', 2) WHEN ',' THEN category.id ELSE");
		findSql.append(" substring(SUBSTRING_INDEX(category.tree_path, ',', 2),2) END sss FROM xx_order orders");
		findSql.append(" LEFT JOIN xx_order_item orderItem ON orderItem.orders = orders.id");
		findSql.append(" LEFT JOIN xx_product products ON products.id = orderItem.product");
		findSql.append(" LEFT JOIN xx_goods goods ON goods.id = products.goods");
		findSql.append(" LEFT JOIN xx_product_category category ON goods.product_category = category.id where 1=1");
		if(null != childMember) {
			findSql.append(" and orders.child_member=:childMember");
		}
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		if(null != supplier) {
			findSql.append(" AND (( orders.supplier =:supplier and orders.type in (:typeOne) ) or ( orders.to_supplier =:supplier and orders.type in (:typeTwo) )) ");
		}
		findSql.append(" GROUP BY sss) a,xx_product_category category WHERE category.id = a.sss");

		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		if(null != childMember) {
			query.setParameter("childMember" , childMember);
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		if(null != supplier.getId()){
			query.setParameter("supplier" , supplier.getId());
		}
		query.setParameter("typeOne", typeOne);
		query.setParameter("typeTwo", typeTwo);
		query.unwrap(SQLQuery.class).addScalar("categoryId",StringType.INSTANCE).addScalar("name",StringType.INSTANCE).addScalar("orderQuantity",IntegerType.INSTANCE)
		.addScalar("orderNumber",IntegerType.INSTANCE).addScalar("goodAmount",BigDecimalType.INSTANCE)
		.addScalar("customersNum",IntegerType.INSTANCE).addScalar("source",IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CommodityReportDto.class));
		List<CommodityReportDto> result = query.getResultList();
		return result;
	}
	
	@Override
	public List<CommodityReportDto> categoryQuery2(Date startDate, Date endDate,
			Supplier supplier, List<Integer> status, ChildMember childMember) {
		List<Integer> type=new ArrayList<>();
		type.add(Order.Type.billDistribution.ordinal());
		type.add(Order.Type.formal.ordinal());
		
		StringBuffer findSql = new StringBuffer("SELECT a.orderQuantity orderQuantity,category.name name,category.id categoryId,category.source source,a.orderNumber orderNumber,a.goodAmount goodAmount,a.customersNum customersNum");
		findSql.append(" from (SELECT COUNT(DISTINCT orders.id) orderNumber,SUM(orderItem.priceB) goodAmount,");
		findSql.append("COUNT(DISTINCT orders.member) customersNum,SUM(orderItem.quantity) orderQuantity,category.id,");
		findSql.append("CASE SUBSTRING_INDEX(category.tree_path, ',', 2) WHEN ',' THEN category.id ELSE");
		findSql.append(" substring(SUBSTRING_INDEX(category.tree_path, ',', 2),2) END sss FROM xx_order orders");
		findSql.append(" LEFT JOIN xx_order_item orderItem ON orderItem.orders = orders.id");
		findSql.append(" LEFT JOIN xx_product products ON products.id = orderItem.product");
		findSql.append(" LEFT JOIN xx_goods goods ON goods.id = products.goods");
		findSql.append(" LEFT JOIN xx_product_category category ON goods.product_category = category.id where 1=1");
		
		if(null != childMember) {
			findSql.append(" and orders.child_member=:childMember");
		}
		if(null != startDate) {
			findSql.append(" and orders.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and orders.create_date<=:endDate");
		}
		if(null != status) {
			findSql.append(" and orders.status in(:status)");
		}
		if(null != supplier) {
			findSql.append(" AND ( orders.supplier =:supplier and orders.type in (:type) ) ");
		}
		findSql.append(" GROUP BY sss) a,xx_product_category category WHERE category.id = a.sss");

		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		if(null != childMember) {
			query.setParameter("childMember" , childMember);
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(null != status) {
			query.setParameter("status" , status);
		}
		if(null != supplier.getId()){
			query.setParameter("supplier" , supplier.getId());
		}
		query.setParameter("type", type);
		query.unwrap(SQLQuery.class).addScalar("categoryId",StringType.INSTANCE).addScalar("name",StringType.INSTANCE).addScalar("orderQuantity",IntegerType.INSTANCE)
		.addScalar("orderNumber",IntegerType.INSTANCE).addScalar("goodAmount",BigDecimalType.INSTANCE)
		.addScalar("customersNum",IntegerType.INSTANCE).addScalar("source",IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CommodityReportDto.class));
		List<CommodityReportDto> result = query.getResultList();
		return result;
	}

    @Override
    public CommodityStatisticsDto purchasingCommodityStatistics(Date startDate, Date endDate,
            Supplier supplier, List<Integer> status) {
        StringBuffer findSql = new StringBuffer("select COUNT(DISTINCT orderItem.sn) numberOfGoods,COUNT(DISTINCT orders.id) orderTotal,SUM(orderItem.quantity) total,");
        findSql.append("SUM(orderItem.priceB) totalAmount,COUNT(DISTINCT suppliers.id) numberOfSuppliers from xx_order orders");
        findSql.append(" INNER JOIN xx_order_item orderItem on orders.id = orderItem.orders");
        //findSql.append(" INNER JOIN xx_member member on orders.member = member.id");
        //findSql.append(" INNER JOIN t_need need on member.need = need.id");
        //findSql.append(" INNER JOIN t_supplier supplier on need.supplier = supplier.id");
        findSql.append(" INNER JOIN t_supplier suppliers ON orders.supplier = suppliers.id where 1=1");
        if(null != supplier.getId()) {
                findSql.append(" and orders.to_supplier=:supplierId");
        }
        if(null != startDate) {
                findSql.append(" and orders.create_date>=:startDate");
        }
        if(null != endDate) {
                findSql.append(" and orders.create_date<=:endDate");
        }
        if(null != status) {
                findSql.append(" and orders.status in(:status)");
        }
        findSql.append(" and orders.type !=1");
        Query query = entityManager.createNativeQuery(findSql.toString()) ;

        if(null != supplier.getId()){
                query.setParameter("supplierId" , supplier.getId());
        }
        if(null != startDate) {
                query.setParameter("startDate" , startDate);
        }
        if(null != endDate) {
                query.setParameter("endDate" , endDate);
        }
        if(null != status) {
                query.setParameter("status" , status);
        }
        query.unwrap(SQLQuery.class).addScalar("orderTotal",LongType.INSTANCE).addScalar("numberOfGoods",LongType.INSTANCE).addScalar("total",StringType.INSTANCE)
        .addScalar("totalAmount",BigDecimalType.INSTANCE).addScalar("numberOfSuppliers",LongType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(CommodityStatisticsDto.class));
        
        CommodityStatisticsDto commodityStatisticsDto = (CommodityStatisticsDto) query.getSingleResult();
        return commodityStatisticsDto;
    }

	

}
