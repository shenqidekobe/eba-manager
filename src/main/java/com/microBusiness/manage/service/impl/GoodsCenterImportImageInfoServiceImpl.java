package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsCenterImportImageInfoDao;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;
import com.microBusiness.manage.service.GoodsCenterImportImageInfoService;

import org.springframework.stereotype.Service;

@Service("goodsCenterImportImageInfoServiceImpl")
public class GoodsCenterImportImageInfoServiceImpl extends BaseServiceImpl<GoodsCenterImportImageInfo, Long> implements GoodsCenterImportImageInfoService {

    @Resource
    private GoodsCenterImportImageInfoDao goodsImportImageInfoDao ;

    @Override
    public Page<GoodsCenterImportImageInfo> findPage(Pageable pageable, String batch) {
    	Page<GoodsCenterImportImageInfo> page = goodsImportImageInfoDao.findPage(pageable ,batch);
        return page;
    }

    @Override
    public List<GoodsCenterImportImageInfo> findList(String batch) {
        return goodsImportImageInfoDao.findList(batch);
    }
}
