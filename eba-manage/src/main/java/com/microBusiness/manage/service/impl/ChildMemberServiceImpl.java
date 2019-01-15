package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ChildMemberDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.ChildMember.Member_Rank;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;

/**
 *
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
			childMember.setIsShoper(false);
			childMember.setRank(Member_Rank.common);
			childMember.setBuyNum(0);
			childMember.setSubBuyNum(0);
			childMember.setSubSubBuyNum(0);
			childMember.setTotalBuyNum(0);
			childMember.setBuyAmount(BigDecimal.ZERO);
			childMember.setSubBuyAmount(BigDecimal.ZERO);
			childMember.setSubSubBuyAmount(BigDecimal.ZERO);
			childMember.setTotalBuyAmount(BigDecimal.ZERO);
			childMember = super.save(childMember);
		}else{
			childMember = super.update(childMember);
		}
		return childMember;
	}
    
    @Override
	public Page<ChildMember> findPage(String nickName,String smOpenId,ChildMember.SourceType type,Boolean isShoper,
			ChildMember parent,Member_Rank rank,Date startDate,Date endDate,Pageable pageable){
    	return childMemberDao.findPage(nickName, smOpenId, type,isShoper, parent,rank, startDate, endDate, pageable);
    }
}
