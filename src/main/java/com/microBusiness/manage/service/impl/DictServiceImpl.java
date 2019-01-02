package com.microBusiness.manage.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.dao.DictDao;
import com.microBusiness.manage.entity.Dict;
import com.microBusiness.manage.service.DictService;

@Service("dictServiceImpl")
public class DictServiceImpl extends BaseServiceImpl<Dict, Long> implements DictService {

	@Resource(name = "dictDaoImpl")
	private DictDao dictDao;
	
	@Override
	@Transactional
	public Dict save(Dict Dict) {
		return super.save(Dict);
	}

	@Override
	@Transactional
	public Dict update(Dict Dict) {
		return super.update(Dict);
	}

	@Override
	@Transactional
	public Dict update(Dict Dict, String... ignoreProperties) {
		return super.update(Dict, ignoreProperties);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(Dict Dict) {
		super.delete(Dict);
	}
	
	@CacheEvict(value = "dict", allEntries = true)
	@Override
	public Dict find(Long id) {
		return super.find(id);
	}

}