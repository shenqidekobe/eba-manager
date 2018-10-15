package com.microBusiness.manage.service.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.service.BaseService;

public interface AssGoodDirectoryService extends BaseService<AssGoodDirectory, Long>{

	Page<AssGoodDirectory> findPage(String theme,Supplier supplier,Pageable pageable);
	
	void save(String theme,String profiles,Supplier supplier,List<Goods> goods);
	void update(Long id,String profiles,List<Goods> goods);
	
	Page<AssGoodDirectory> findPage(AssChildMember assChildMember , Pageable pageable);
	
	void copyGoodsCatalogue(AssGoodDirectory assGoodDirectory , AssChildMember assChildMember);
	
	void copyGoods(AssChildMember assChildMember , AssGoodDirectory assGoodDirectory , Goods goods);
	
	List<AssGoodDirectory> findList(AssChildMember assChildMember);

	boolean themeExists(String theme,Supplier supplier);
	
	List<AssGoodDirectory> findList(Goods goods);
}
