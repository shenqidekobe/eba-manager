package com.microBusiness.manage.dao;

import java.util.Date;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.ChildMember.Member_Rank;

/**
 * Created by mingbai on 2017/2/11.
 * 功能描述：
 * 修改记录：
 */
public interface ChildMemberDao extends BaseDao<ChildMember , Long> {
	
    ChildMember findByOpenId(String openId);


    ChildMember findByUnionId(String unionId);
    
    ChildMember findBySmOpenId(String smOpenId);
    
    Page<ChildMember> findPage(String nickName,String smOpenId,ChildMember.SourceType type,Boolean isShoper,
			ChildMember parent,Member_Rank rank,Date startDate,Date endDate,Pageable pageable);
}
