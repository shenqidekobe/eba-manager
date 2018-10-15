package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;

public interface GoodsCenterImportInfoService extends BaseService<GoodsCenterImportInfo , Long> {

    Page<GoodsCenterImportInfo> findPage(Pageable pageable , GoodsCenterImportLog goodsImportLog);

    List<GoodsCenterImportInfo> findList(GoodsCenterImportLog goodsImportLog , Boolean valid);
	
}
