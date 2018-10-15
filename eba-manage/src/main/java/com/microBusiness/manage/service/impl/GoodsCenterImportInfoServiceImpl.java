package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsCenterImportInfoDao;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;
import com.microBusiness.manage.service.GoodsCenterImportInfoService;

import org.springframework.stereotype.Service;

@Service
public class GoodsCenterImportInfoServiceImpl extends BaseServiceImpl<GoodsCenterImportInfo, Long> implements GoodsCenterImportInfoService {

	@Resource
	private GoodsCenterImportInfoDao goodsCenterImportInfoDao;
	
	@Override
	public Page<GoodsCenterImportInfo> findPage(Pageable pageable, GoodsCenterImportLog goodsImportLog) {
		Page<GoodsCenterImportInfo> page = goodsCenterImportInfoDao.findPage(pageable ,goodsImportLog);
    	for (GoodsCenterImportInfo goodsImportInfo : page.getContent()) {
			if (goodsImportInfo.getCategoryCenterImport().getGrade() == 0) {
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getCategoryCenterImport());
			} else if(goodsImportInfo.getCategoryCenterImport().getGrade() == 1){
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getCategoryCenterImport().getParent());
			} else {
				goodsImportInfo.setOneProductCategoryImport(goodsImportInfo.getCategoryCenterImport().getParent().getParent());
			}
			
		}
    	
        return page;
	}

	@Override
	public List<GoodsCenterImportInfo> findList(GoodsCenterImportLog goodsImportLog, Boolean valid) {
		return goodsCenterImportInfoDao.findList(goodsImportLog, valid);
	}

}
