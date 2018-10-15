package com.microBusiness.manage.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.OrderItemDao;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.dao.StorageFormDao;
import com.microBusiness.manage.dao.StorageFormLogDao;
import com.microBusiness.manage.dao.StorageGoodsDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.OrderItem;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.entity.StorageForm;
import com.microBusiness.manage.entity.StorageFormLog;
import com.microBusiness.manage.entity.StorageGoods;
import com.microBusiness.manage.service.StorageFormService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("storageFormServiceImpl")
public class StorageFormServiceImpl extends BaseServiceImpl<StorageForm, Long> implements StorageFormService {

	@Resource
	private StorageFormDao storageFormDao;
	
	@Resource
	private StorageFormLogDao storageFormLogDao;
	
	@Resource
	private StorageGoodsDao storageGoodsDao;
	
	@Resource
	private ProductDao productDao;
	
	@Resource
	private StockGoodsDao stockGoodsDao;
	
	@Resource
	private OrderItemDao orderItemDao;
	
	@Override
	public boolean exists(Shop shop, SfIfStatus sfIfStatus) {
		return storageFormDao.exists(shop, sfIfStatus);
	}

	@Override
	public Page<StorageForm> findPage(Pageable pageable, String Keyword, Shop shop) {
		return storageFormDao.findPage(pageable, Keyword, shop);
	}

	@Transactional
	@Override
	public boolean save(StorageForm storageForm, List<StorageGoods> storageGoods) {
		try {
			storageForm.setStorageCode(this.generateCode(storageForm.getMember()));
			storageFormDao.persist(storageForm);
			
			// 操作记录
			StorageFormLog storageFormLog = new StorageFormLog();
			storageFormLog.setOrder(storageForm.getOrder());
			storageFormLog.setMember(storageForm.getMember());
			storageFormLog.setRecord(StorageFormLog.Record.create);
			storageFormLog.setShop(storageForm.getShop());
			storageFormLog.setStorageForm(storageForm);
			storageFormLogDao.persist(storageFormLog);
			
			// 入库单商品关系
			List<StorageFormLog.Entry> entrys = new ArrayList<>();
			if (storageGoods.size() > 0) {
				for (StorageGoods storage : storageGoods) {
					
					Product product = productDao.find(storage.getProduct().getId());
					
					// 只记录与当前订单有关的商品
					OrderItem orderItem = orderItemDao.getOrderItemByProduct(storageForm.getOrder(), product);
					if (orderItem != null) {
						StorageFormLog.Entry entry = new StorageFormLog.Entry();
						entry.setName(product.getGoods().getName());
						
						String proStr = "";
						for (SpecificationValue specificationValue : product.getSpecificationValues()) {
							proStr += specificationValue.getValue();
						}
						
						entry.setProduct(proStr);
						entry.setUrl(product.getGoods().getImage());
						entry.setNumber(storage.getActualStock());
						
						entrys.add(entry);
					}
					
					storage.setProduct(product);
					storage.setStorageForm(storageForm);
					storageGoodsDao.persist(storage);

					if (storageForm.getStatus() == SfIfStatus.completed) {
						// 修改库存状况盘点库存
						StockGoods stockGoods = stockGoodsDao.findByProduct(storage.getProduct(), storageForm.getShop());
						stockGoods.setActualStock(storage.getActualStock()+stockGoods.getActualStock());
						stockGoods.setStatus(StockGoods.Status.display);
						stockGoodsDao.merge(stockGoods);
					}
				}
				
				storageFormLog.setEntries(entrys);
				storageFormLogDao.persist(storageFormLog);
				
				if (storageForm.getStatus() == SfIfStatus.completed) {
					StorageFormLog storageFormLogCom = new StorageFormLog();
					storageFormLogCom.setMember(storageForm.getMember());
					storageFormLogCom.setRecord(StorageFormLog.Record.completed);
					storageFormLogCom.setShop(storageForm.getShop());
					storageFormLogCom.setStorageForm(storageForm);
					storageFormLogCom.setOrder(storageForm.getOrder());
					storageFormLogCom.setEntries(entrys);
					storageFormLogDao.persist(storageFormLogCom);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private String generateCode(Member member) {
		
		String storageCode = "";
		
		Long count = storageFormDao.getCount(member);
			
		// 获取年月日
		String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String month = new SimpleDateFormat("MM",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String day = new SimpleDateFormat("dd",Locale.CHINESE).format(Calendar.getInstance().getTime());
			
		// 补充0
		String str = String.format("%06d", count);
			
		storageCode = "RK"+yearLast+month+day+str;
			
		StorageForm storageForm = storageFormDao.findByStorageCode(storageCode, member);

		if (storageForm != null) {
			str = String.format("%06d", count++);
			storageCode = "RK"+yearLast+month+day+str;
		}
		
		return storageCode;
	}

	@Override
	public boolean update(StorageForm storageForm, List<StorageGoods> storageGoods) {
		try {
			// 删除入库单商品关联
			storageGoodsDao.delete(storageForm.getId());

			// 修改盘点单信息
			StorageForm entity = this.find(storageForm.getId());
			entity.setRemarks(storageForm.getRemarks());
			entity.setStatus(storageForm.getStatus());
			this.update(entity);
			
			StorageFormLog storageFormLog = new StorageFormLog();
			
			// 重新建立入库单商品关联
			for (StorageGoods storage : storageGoods) {
				storage.setProduct(productDao.find(storage.getProduct().getId()));
				storage.setStorageForm(entity);
				storageGoodsDao.persist(storage);
				
				if (storageForm.getStatus() == SfIfStatus.completed) {
					// 修改库存状况盘点库存
					StockGoods stockGoods = stockGoodsDao.findByProduct(storage.getProduct(), storageForm.getShop());
					stockGoods.setActualStock(storage.getActualStock()+stockGoods.getActualStock());
					stockGoods.setStatus(StockGoods.Status.display);
					stockGoodsDao.merge(stockGoods);
					
					storageFormLog.setRecord(StorageFormLog.Record.completed);
				}else {
					storageFormLog.setRecord(StorageFormLog.Record.edit);
				}
			}
			
			// 操作记录
			storageFormLog.setMember(storageForm.getMember());
			storageFormLog.setShop(storageForm.getShop());
			storageFormLog.setStorageForm(entity);
			storageFormLogDao.persist(storageFormLog);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean cancel(StorageForm storageForm, Member member) {
		storageForm.setStatus(SfIfStatus.canceled);
		this.update(storageForm);
		
		// 操作记录
		StorageFormLog storageFormLog = new StorageFormLog();
		storageFormLog.setMember(member);
		storageFormLog.setRecord(StorageFormLog.Record.canceled);
		storageFormLog.setShop(storageForm.getShop());
		storageFormLog.setStorageForm(storageForm);
		storageFormLogDao.persist(storageFormLog);
		return true;
	}

}