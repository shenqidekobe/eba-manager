package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.FavorCompanyDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.DepositLog;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.FavorCompanyService;

@Service("favorCompanyServiceImpl")
public class FavorCompanyServiceImpl extends BaseServiceImpl<FavorCompany, Long> implements FavorCompanyService{

	@Resource(name = "favorCompanyDaoImpl")
	private FavorCompanyDao favorCompanyDao;
	@Resource
	private SupplierDao supplierDao;

	@Override
	public FavorCompany favorCompany(Admin admin, Long supplierId) {
		if (admin != null) {
			// 判断是否已经收藏
			FavorCompany favorCompany = favorCompanyDao.getFavorCompanyIsNull(admin.getId(), supplierId);

			// 收藏
			if (favorCompany == null) {
				favorCompany = new FavorCompany();
				favorCompany.setAdminId(admin.getId());
				favorCompany.setSupplierId(supplierId);
				favorCompany.setDelflag(FavorCompany.Delflag.delflag_no);
				favorCompanyDao.persist(favorCompany);
				return favorCompany;
			}else {
				// 取消收藏
				favorCompany.setDelflag(FavorCompany.Delflag.delflag_had);
				favorCompanyDao.merge(favorCompany);
				return favorCompany;
			}

		}

		return null;
	}

	@Override
	public Page<Supplier> findPage(Long adminId, FavorCompany.Delflag delflag, Pageable pageable) {
		return supplierDao.findPage(adminId, delflag, pageable);
	}

	@Override
	public boolean updateDelflag(Long supplierId, Long adminId , FavorCompany.Delflag delflag) {
		return favorCompanyDao.updateDelflag(supplierId, adminId ,delflag);
	}

	@Override
	public List<FavorCompany> getFavorCompany(Supplier supplier) {
		return favorCompanyDao.getFavorCompany(supplier);
	}

}
