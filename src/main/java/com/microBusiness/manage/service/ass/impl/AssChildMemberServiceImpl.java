package com.microBusiness.manage.service.ass.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;

import org.springframework.stereotype.Service;

/**
 * 功能描述：
 * 修改记录：
 */
@Service
public class AssChildMemberServiceImpl extends BaseServiceImpl<AssChildMember , Long> implements AssChildMemberService {
    @Resource
    private AssChildMemberDao asschildMemberDao ;

    @Override
    public AssChildMember findByOpenId(String openId) {
        return asschildMemberDao.findByOpenId(openId);
    }

    /**
     * @param unionId
     * @return
     */
    @Override
    public AssChildMember findByUnionId(String unionId) {
        return asschildMemberDao.findByUnionId(unionId);
    }

	@Override
	public AssChildMember unBind(AssChildMember childMember) {
		childMember.setBindFlag(AssChildMember.BindFlag.unbind);
		childMember.setMember(null);
	    return childMember;
	}

	@Override
	public List<AssChildMember> findList(Supplier supplier) {
		return asschildMemberDao.findList(supplier);
	}

	@Override
	public List<AssChildMember> findList(Need need) {
		return asschildMemberDao.findList(need);
	}

	@Override
	public AssChildMember find(Admin admin) {
		return asschildMemberDao.find(admin);
	}

	@Override
	public List<AssChildMember> findList(CustomerRelation customerRelation) {
		return asschildMemberDao.findList(customerRelation);
	}

}
