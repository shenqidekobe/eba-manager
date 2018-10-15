package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.dao.ChildMemberDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;

/**
 * Created by mingbai on 2017/2/11.
 * 功能描述：
 * 修改记录：
 */
@Service
public class ChildMemberServiceImpl extends BaseServiceImpl<ChildMember , Long> implements ChildMemberService {
    @Resource
    private ChildMemberDao childMemberDao ;
    @Resource
    private MemberService MemberService;

    @Override
    public ChildMember findByOpenId(String openId) {
        return childMemberDao.findByOpenId(openId);
    }
    
    @Override
    public ChildMember findBySmOpenId(String smOpenId) {
        return childMemberDao.findBySmOpenId(smOpenId);
    }

    @Override
    public ChildMember unBind(ChildMember childMember) {
        childMember.setMember(null);
        return childMember;
    }


    /**
     * 修改微信用户基本信息
     *
     * @param childMember
     * @return
     */
    @Override
    public ChildMember updateChildMember(ChildMember childMember , ChildMember currChildMember) {

        currChildMember.setNickName(childMember.getNickName());

        return currChildMember;
    }

    /**
     * @param unionId
     * @return
     */
    @Override
    public ChildMember findByUnionId(String unionId) {
        return childMemberDao.findByUnionId(unionId);
    }

    @Transactional
	@Override
	public ChildMember saveChildMember(ChildMember childMember) {
		if(childMember.getMember() == null){
			Member member = MemberService.addMember("");
			childMember.setMember(member);
		}
		if(childMember.getId() == null){
			childMember = super.save(childMember);
		}else{
			childMember = super.update(childMember);
		}
		return childMember;
	}
}
