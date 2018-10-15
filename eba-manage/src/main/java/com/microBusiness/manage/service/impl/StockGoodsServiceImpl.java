package com.microBusiness.manage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.entity.StockGoods.Status;
import com.microBusiness.manage.service.StockGoodsService;

import org.springframework.stereotype.Service;

@Service("stockGoodsServiceImpl")
public class StockGoodsServiceImpl extends BaseServiceImpl<StockGoods, Long> implements StockGoodsService {

	@Resource
	private StockGoodsDao stockGoodsDao;
	
	@Override
	public Page<StockGoods> page(Shop shop, String Keyword, Status status, Pageable pageable) {
		return stockGoodsDao.page(shop, Keyword, status, pageable);
	}

	@Override
	public void save(Shop shop, List<Product> products) {
		for (Product product : products) {
			StockGoods stockGoods = stockGoodsDao.findByProduct(product, shop);
			if (stockGoods == null) {
				StockGoods stockGoodsNew = new StockGoods();
				stockGoodsNew.setProduct(product);
				stockGoodsNew.setActualStock(0);
				stockGoodsNew.setStatus(StockGoods.Status.hide);
				stockGoodsNew.setShop(shop);
				
				super.save(stockGoodsNew);
			}
		}

	}

	@Override
	public List<StockGoods> findByBarCode(String barCode, Shop shop, Status status) {
		return stockGoodsDao.findByBarCode(barCode, shop, status);
	}

	@Override
	public StockGoods findByProduct(Product product, Shop shop) {
		return stockGoodsDao.findByProduct(product, shop);
	}

	@Override
	public List<StockGoods> getStockGoodsByMember(Product product, Member member, Shop shop) {
		return stockGoodsDao.getStockGoodsByMember(product, member, shop);
	}

}