package com.microBusiness.manage.service.impl;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ProductCenterDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.service.ProductCenterService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:49
 * Describe:
 * Update:
 */
@Service
public class ProductCenterServiceImpl extends BaseServiceImpl<ProductCenter , Long> implements ProductCenterService {
    @Resource
    private ProductCenterDao productCenterDao ;
    /**
     * 规格商品分页
     *
     * @param categoryCenter
     * @param goodsName
     * @param pageable
     * @return
     */
    @Override
    public Page<ProductCenter> findPage(CategoryCenter categoryCenter, String goodsName, Pageable pageable) {
        return productCenterDao.findPage(categoryCenter , goodsName , pageable);
    }
	@Override
	public List<ProductCenter> findList(CategoryCenter categoryCenter,
			String goodsName, String barCode) {
		return productCenterDao.findList(categoryCenter, goodsName, barCode);
	}
	@Override
	public List<ProductCenter> findList(CategoryCenter categoryCenter,
			String searchName) {
		return productCenterDao.findList(categoryCenter, searchName);
	}
}
