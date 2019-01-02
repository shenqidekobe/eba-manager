package com.microBusiness.manage.dao.impl;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.DictDao;
import com.microBusiness.manage.entity.Dict;

@Repository("dictDaoImpl")
public class DictDaoImpl extends BaseDaoImpl<Dict, Long> implements DictDao {


}