package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsImportInfo;
import com.microBusiness.manage.entity.GoodsImportLog;

public interface GoodsImportInfoService extends BaseService<GoodsImportInfo, Long> {

    Page<GoodsImportInfo> findPage(Pageable pageable , GoodsImportLog goodsImportLog);

    List<GoodsImportInfo> findList(GoodsImportLog goodsImportLog , Boolean valid);

}
