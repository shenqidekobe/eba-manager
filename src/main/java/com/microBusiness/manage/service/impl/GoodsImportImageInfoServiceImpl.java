package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsImportImageInfoDao;
import com.microBusiness.manage.entity.GoodsImportImageInfo;
import com.microBusiness.manage.service.GoodsImportImageInfoService;

import org.springframework.stereotype.Service;

@Service("goodsImportImageInfoServiceImpl")
public class GoodsImportImageInfoServiceImpl extends BaseServiceImpl<GoodsImportImageInfo, Long> implements GoodsImportImageInfoService {

    @Resource
    private GoodsImportImageInfoDao goodsImportImageInfoDao ;

    @Override
    public Page<GoodsImportImageInfo> findPage(Pageable pageable, String batch) {
    	Page<GoodsImportImageInfo> page = goodsImportImageInfoDao.findPage(pageable ,batch);
        return page;
    }

    @Override
    public List<GoodsImportImageInfo> findList(String batch) {
        return goodsImportImageInfoDao.findList(batch);
    }
}
