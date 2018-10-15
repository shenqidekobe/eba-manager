package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.ChildMember;

/**
 * Created by mingbai on 2017/2/11.
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
}
