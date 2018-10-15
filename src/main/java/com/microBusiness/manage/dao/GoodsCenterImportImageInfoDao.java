package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;

public interface GoodsCenterImportImageInfoDao extends BaseDao<GoodsCenterImportImageInfo, Long> {

    Page<GoodsCenterImportImageInfo> findPage(Pageable pageable, String batch);

    List<GoodsCenterImportImageInfo> findList(String batch);
    
    GoodsCenterImportImageInfo query(String sn, String batch);
}
