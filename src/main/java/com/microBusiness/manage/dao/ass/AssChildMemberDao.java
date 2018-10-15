package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssChildMemberDao extends BaseDao<AssChildMember , Long> {
    AssChildMember findByOpenId(String openId);

    AssChildMember findByUnionId(String unionId);
    
    List<AssChildMember> findList(Supplier supplier);
    
    List<AssChildMember> findList(Need need);
    
    AssChildMember find(Admin admin);
    
    List<AssChildMember> findList(CustomerRelation customerRelation);
    
    
}
