package com.microBusiness.manage.service;

import java.util.Date;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.ChildMember.Member_Rank;

/**
 * 功能描述：
 * 修改记录：
 */
public interface ChildMemberService extends BaseService<ChildMember , Long> {
   
	ChildMember findByOpenId(String openId);

    ChildMember unBind(ChildMember childMember);

    /**
     * 修改微信用户基本信息
     * @param childMember
     * @return
     */
    ChildMember updateChildMember(ChildMember childMember , ChildMember currChildMember);

    /**
     *
     * @param unionId
     * @return
     */
    ChildMember findByUnionId(String unionId);
    
    ChildMember findBySmOpenId(String smOpenId);
    
    ChildMember saveChildMember(ChildMember childMember);
    
	
	Page<ChildMember> findPage(String nickName,String smOpenId,ChildMember.SourceType type,Boolean isShoper,
			ChildMember parent,Member_Rank rank,Date startDate,Date endDate,Pageable pageable);
}
