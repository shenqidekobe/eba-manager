package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.GoodsImportImageInfo;

public interface GoodsImportImageInfoDao extends BaseDao<GoodsImportImageInfo, Long> {

    Page<GoodsImportImageInfo> findPage(Pageable pageable, String batch);

    List<GoodsImportImageInfo> findList(String batch);
    
    GoodsImportImageInfo query(String sn, String batch);
}
