package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;

import java.util.List;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：
 * 修改记录：
 */
public interface NeedImportInfoService extends BaseService<NeedImportInfo, Long> {

    Page<NeedImportInfo> findPage(Pageable pageable , NeedImportLog needImportLog);

    List<NeedImportInfo> findList(NeedImportLog needImportLog , Boolean valid);

}
