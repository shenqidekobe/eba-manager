package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsImportInfoDao;
import com.microBusiness.manage.entity.GoodsImportInfo;
import com.microBusiness.manage.entity.GoodsImportLog;
import com.microBusiness.manage.service.GoodsImportInfoService;

import org.springframework.stereotype.Service;

@Service("goodsImportInfoServiceImpl")
public class GoodsImportInfoServiceImpl extends BaseServiceImpl<GoodsImportInfo, Long> implements GoodsImportInfoService {

    @Resource
    private GoodsImportInfoDao goodsImportInfoDao ;

    @Override
    public Page<GoodsImportInfo> findPage(Pageable pageable, GoodsImportLog goodsImportLog) {
    	Page<GoodsImportInfo> page = goodsImportInfoDao.findPage(pageable ,goodsImportLog);
    	for (GoodsImportInfo goodsImportInfo : page.getContent()) {
			if (goodsImportInfo.getProductCategoryImport().getGrade() == 0) {
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getProductCategoryImport());
			} else if(goodsImportInfo.getProductCategoryImport().getGrade() == 1){
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getProductCategoryImport().getParent());
			} else {
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getProductCategoryImport().getParent().getParent());
			}
			
		}
    	
        return page;
    }

    @Override
    public List<GoodsImportInfo> findList(GoodsImportLog goodsImportLog , Boolean valid) {
        return goodsImportInfoDao.findList(goodsImportLog , valid);
    }
}
