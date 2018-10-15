package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;

public interface GoodsCenterImportImageInfoService extends BaseService<GoodsCenterImportImageInfo, Long> {

	Page<GoodsCenterImportImageInfo> findPage(Pageable pageable , String batch);

    List<GoodsCenterImportImageInfo> findList(String batch);

}
