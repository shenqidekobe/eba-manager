package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.service.BaseService;

/**
 * 功能描述：
 * 修改记录：
 */
public interface AssChildMemberService extends BaseService<AssChildMember , Long> {
    AssChildMember findByOpenId(String openId);

    AssChildMember unBind(AssChildMember childMember);
    
    /**
     *
     * @param unionId
     * @return
     */
    AssChildMember findByUnionId(String unionId);
    
    List<AssChildMember> findList(Supplier supplier);
    
    List<AssChildMember> findList(Need need);
    
    AssChildMember find(Admin admin);
    
    List<AssChildMember> findList(CustomerRelation customerRelation);
}
