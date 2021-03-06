package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssUpdateTips;

public interface AssUpdateTipsDao extends BaseDao<AssUpdateTips, Long> {

	List<AssUpdateTips> findList(AssChildMember assChildMember , AssGoodDirectory assGoodDirectory , AssUpdateTips.WhetherUpdate whetherUpdate, AssUpdateTips.Type type);
	
	AssUpdateTips find(AssChildMember assChildMember , AssGoodDirectory assGoodDirectory , AssUpdateTips.Type type);
	
	AssUpdateTips find(AssChildMember assChildMember , CustomerRelation customerRelation , Need need , AssUpdateTips.Type type);
	
	List<AssUpdateTips> findList(CustomerRelation customerRelation , Need need , AssUpdateTips.Type type);
	
	List<AssUpdateTips> findList(AssChildMember assChildMember , AssUpdateTips.WhetherUpdate whetherUpdate , List<Integer> types);
}
