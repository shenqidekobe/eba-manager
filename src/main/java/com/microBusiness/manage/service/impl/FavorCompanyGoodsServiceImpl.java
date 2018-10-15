package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.FavorCompanyGoodsDao;
import com.microBusiness.manage.dto.CompanyGoodsSupplierDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.FavorCompanyGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.FavorCompanyGoodsService;

import org.springframework.stereotype.Service;

@Service("favorCompanyGoodsServiceImpl")
public class FavorCompanyGoodsServiceImpl extends BaseServiceImpl<FavorCompanyGoods, Long> implements FavorCompanyGoodsService{

	@Resource(name = "favorCompanyGoodsDaoImpl")
	private FavorCompanyGoodsDao favorCompanyGoodsDao;
	
	@Override
	public FavorCompanyGoods favorCompanyGoods(Admin admin, Long companyGoodsId) {
		if (admin != null) {
			// 判断是否已经收藏
			FavorCompanyGoods favorCompanyGoods = favorCompanyGoodsDao.getFavorCompanyGoodsIsNull(admin.getId(), companyGoodsId);
			
			// 收藏
			if (favorCompanyGoods == null) {
				favorCompanyGoods = new FavorCompanyGoods();
				favorCompanyGoods.setAdminId(admin.getId());
				favorCompanyGoods.setCompanyGoodsId(companyGoodsId);
				favorCompanyGoods.setDelflag(Delflag.delflag_no);
				favorCompanyGoodsDao.persist(favorCompanyGoods);
				return favorCompanyGoods;
			}else { 
				// 取消收藏
				favorCompanyGoods.setDelflag(Delflag.delflag_had);
				favorCompanyGoodsDao.merge(favorCompanyGoods);
				return favorCompanyGoods;
			}
			
		}

		return null;
	}

	@Override
	public Page<CompanyGoodsSupplierDto> findPage(Pageable pageable,
			Long adminId, Delflag delflag) {
		return favorCompanyGoodsDao.findPage(pageable, adminId, delflag);
	}

	@Override
	public void updateDelflag(Long adminId, Long companyGoodsId, Delflag delflag) {
		favorCompanyGoodsDao.updateDelflag(adminId, companyGoodsId, delflag);
	}

	@Override
	public List<FavorCompanyGoods> getFavorCompanyGoods(Supplier supplier) {
		return favorCompanyGoodsDao.getFavorCompanyGoods(supplier);
	}

}
