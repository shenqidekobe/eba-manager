package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsImportImageInfo;

public interface GoodsImportImageInfoService extends BaseService<GoodsImportImageInfo, Long> {

	Page<GoodsImportImageInfo> findPage(Pageable pageable , String batch);

    List<GoodsImportImageInfo> findList(String batch);

}
