package com.microBusiness.manage.dao;


import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;

public interface GoodsCenterImportInfoDao extends BaseDao<GoodsCenterImportInfo , Long> {

	 Page<GoodsCenterImportInfo> findPage(Pageable pageable, GoodsCenterImportLog GoodsImportLog);

	 List<GoodsCenterImportInfo> findList(GoodsCenterImportLog goodsImportLog , Boolean valid);
}
