package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.ChildMember;

/**
 * Created by mingbai on 2017/2/11.
 * 功能描述：
 * 修改记录：
 */
public interface ChildMemberDao extends BaseDao<ChildMember , Long> {
	
    ChildMember findByOpenId(String openId);


    ChildMember findByUnionId(String unionId);
    
    ChildMember findBySmOpenId(String smOpenId);
}
