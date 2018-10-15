package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsImportInfo;
import com.microBusiness.manage.entity.GoodsImportLog;

public interface GoodsImportInfoDao extends BaseDao<GoodsImportInfo, Long> {

    Page<GoodsImportInfo> findPage(Pageable pageable, GoodsImportLog GoodsImportLog);

    List<GoodsImportInfo> findList(GoodsImportLog goodsImportLog , Boolean valid);
}
