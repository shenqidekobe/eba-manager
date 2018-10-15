package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodsVisitDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.dto.GoodsVisitDto;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsVisit;

@Repository
public class AssGoodsVisitDaoImpl extends BaseDaoImpl<AssGoodsVisit, Long> implements AssGoodsVisitDao{

	@Override
	public Long getCountByRelation(AssCustomerRelation assCustomerRelation) {
		String sql="select count(*) from AssGoodsVisit assGoodsVisit where assGoodsVisit.assCustomerRelation=:assCustomerRelation";
		Query query = entityManager.createQuery(sql,Long.class).setParameter("assCustomerRelation", assCustomerRelation);
		return (Long) query.getSingleResult();
	}

	@Override
	public Page<GoodsVisitDto> findPage(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember,Pageable pageable) {
		StringBuffer findSql=new StringBuffer(" select goods.*,count(goods.id) goodsVisit from ass_goods_visit goods ");
		findSql.append(" where goods.ass_customer_relation=:assCustomerRelation and goods.ass_child_member=:assChildMember group by goods.ass_goods order by goodsVisit desc");
		
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(" select goods.id from ass_goods_visit goods where goods.ass_customer_relation=:assCustomerRelation and goods.ass_child_member=:assChildMember group by goods.ass_goods ");
		countSql.append(" ) datas ");
		
		Query query = entityManager.createNativeQuery(findSql.toString());
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		query.setParameter("assCustomerRelation", assCustomerRelation.getId());
		query.setParameter("assChildMember", assChildMember.getId());
		countQuery.setParameter("assCustomerRelation", assCustomerRelation.getId());
		countQuery.setParameter("assChildMember", assChildMember.getId());
		
		query.unwrap(SQLQuery.class).addEntity(AssGoodsVisit.class).addScalar("goodsVisit",LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodsVisitDto.class));
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		List<GoodsVisitDto> list=query.getResultList();
		
		return new Page<GoodsVisitDto>(list, total, pageable);
	}
	
	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		String findSql=" select count(goods.id) from ass_goods_visit goods left join ass_customer_relation relation on relation.id=goods.ass_customer_relation where relation.share_type=0 and relation.ass_child_member=:assChildMember ";
		Query query = entityManager.createNativeQuery(findSql.toString());
		query.setParameter("assChildMember", assChildMember.getId());
		return (BigInteger) query.getSingleResult();
	}

	@Override
	public List<GoodsVisitDto> findList(AssCustomerRelation assCustomerRelation, AssChildMember assChildMember) {
		StringBuffer findSql=new StringBuffer(" select goods.*,count(goods.id) goodsVisit from ass_goods_visit goods ");
		findSql.append(" where goods.ass_customer_relation=:assCustomerRelation and goods.ass_child_member=:assChildMember group by goods.ass_goods order by goodsVisit desc");
		
		Query query = entityManager.createNativeQuery(findSql.toString());
		query.setParameter("assCustomerRelation", assCustomerRelation.getId());
		query.setParameter("assChildMember", assChildMember.getId());
		
		query.unwrap(SQLQuery.class).addEntity(AssGoodsVisit.class).addScalar("goodsVisit",LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodsVisitDto.class));
		
		List<GoodsVisitDto> list=query.getResultList();
		
		return list;
	}

}
