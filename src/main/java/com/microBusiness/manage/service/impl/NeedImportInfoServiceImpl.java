package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedImportInfoDao;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;
import com.microBusiness.manage.service.NeedImportInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：
 * 修改记录：
 */
@Service
public class NeedImportInfoServiceImpl extends BaseServiceImpl<NeedImportInfo, Long> implements NeedImportInfoService {

    @Resource
    private NeedImportInfoDao needImportInfoDao ;

    @Override
    public Page<NeedImportInfo> findPage(Pageable pageable, NeedImportLog needImportLog) {
        return needImportInfoDao.findPage(pageable ,needImportLog);
    }

    @Override
    public List<NeedImportInfo> findList(NeedImportLog needImportLog , Boolean valid) {
        return needImportInfoDao.findList(needImportLog , valid);
    }
}
