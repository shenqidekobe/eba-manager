package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssPageVisitDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.dto.OrderStatisticsDto;
import com.microBusiness.manage.dto.ShareUserPageDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssPageVisit;

@Repository
public class AssPageVisitDaoImpl extends BaseDaoImpl<AssPageVisit, Long> implements AssPageVisitDao{

	@Override
	public Long getCountByRelation(AssCustomerRelation assCustomerRelation) {
		String sql="select count(*) from AssPageVisit assPageVisit where assPageVisit.assCustomerRelation=:assCustomerRelation";
		Query query = entityManager.createQuery(sql,Long.class).setParameter("assCustomerRelation", assCustomerRelation);
		return (Long) query.getSingleResult();
	}

	@Override
	public Page<ShareUserPageDto> findPage(AssCustomerRelation assCustomerRelation, String orderBy, Pageable pageable) {
		StringBuffer findSql=new StringBuffer(" SELECT member.id id,member.nick_name name,member.head_img_url headImgUrl,COUNT(page.id) pageVisit,(SELECT pagevisit.create_date from ass_page_visit pagevisit where pagevisit.ass_child_member = page.ass_child_member ORDER BY pagevisit.create_date DESC LIMIT 1) date,");
		findSql.append(" (SELECT COUNT(goods.id) FROM ass_goods_visit goods ");
		findSql.append(" WHERE page.ass_child_member = goods.ass_child_member and page.ass_customer_relation=goods.ass_customer_relation ");
		findSql.append(" ) goodsVisit ");
		findSql.append(" FROM ass_page_visit page left join ass_child_member member on page.ass_child_member=member.id ");
		findSql.append(" where page.ass_customer_relation=:relation ");
		findSql.append(" GROUP BY page.ass_child_member ");
		if (StringUtils.isBlank(orderBy)) {
			findSql.append(" ORDER BY date DESC ");
		}else {
			findSql.append(" ORDER BY pageVisit "+orderBy+",goodsVisit desc,date desc ");
		}
		
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(" SELECT id from ass_page_visit where ass_customer_relation=:relation GROUP BY ass_child_member ");
		countSql.append(" ) datas ");
		
		Query query = entityManager.createNativeQuery(findSql.toString());
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		query.setParameter("relation", assCustomerRelation.getId());
		countQuery.setParameter("relation", assCustomerRelation.getId());
		
		query.unwrap(SQLQuery.class).addScalar("id", LongType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("headImgUrl", StringType.INSTANCE).addScalar("goodsVisit", LongType.INSTANCE).addScalar("pageVisit", LongType.INSTANCE).addScalar("date", TimestampType.INSTANCE).setResultTransformer(Transformers.aliasToBean(ShareUserPageDto.class));
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<ShareUserPageDto> list=query.getResultList();
		
		return new Page<ShareUserPageDto>(list, total, pageable);
	}

	@Override
	public BigInteger getShareAllVisit(AssChildMember assChildMember) {
		String findSql=" select count(page.id) from ass_page_visit page left join ass_customer_relation relation on relation.id=page.ass_customer_relation where relation.share_type=0 and relation.ass_child_member=:assChildMember";
		Query query = entityManager.createNativeQuery(findSql.toString());
		query.setParameter("assChildMember", assChildMember.getId());
		return (BigInteger) query.getSingleResult();
	}

	@Override
	public boolean isSynchronize(long memberId, List<Long> goodsIds) {
		String findSql=" select goods.* from ass_goods goods left join ass_customer_relation relation on relation.id=goods.ass_customer_relation where relation.ass_child_member=:memberId and goods.source in (:goodsIds)";
		Query query = entityManager.createNativeQuery(findSql.toString(),AssGoods.class);
		query.setParameter("memberId", memberId);
		query.setParameter("goodsIds", goodsIds);
		List<AssGoods> assGoods=query.getResultList();
		if (assGoods.size() > 0) {
			return true;
		}
		return false;
	}

}
