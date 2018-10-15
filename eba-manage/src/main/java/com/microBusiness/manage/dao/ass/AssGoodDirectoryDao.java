package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;

public interface AssGoodDirectoryDao extends BaseDao<AssGoodDirectory, Long>{

	Page<AssGoodDirectory> findPage(String theme, Supplier supplier, Pageable pageable);
	
	Page<AssGoodDirectory> findPage(AssChildMember assChildMember , Pageable pageable);
	
	List<AssGoodDirectory> findList(AssChildMember assChildMember);
	
	List<AssGoodDirectory> findList(Goods goods);

	boolean themeExists(String theme, Supplier supplier);

}
