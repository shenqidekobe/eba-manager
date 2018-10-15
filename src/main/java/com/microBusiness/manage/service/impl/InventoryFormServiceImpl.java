package com.microBusiness.manage.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.InventoryFormDao;
import com.microBusiness.manage.dao.InventoryFormLogDao;
import com.microBusiness.manage.dao.InventoryGoodsDao;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.InventoryGoods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.service.InventoryFormService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("inventoryFormServiceImpl")
public class InventoryFormServiceImpl extends BaseServiceImpl<InventoryForm, Long> implements InventoryFormService {

	@Resource
	private InventoryFormDao inventoryFormDao;
	
	@Resource
	private InventoryGoodsDao inventoryGoodsDao;
	
	@Resource
	private InventoryFormLogDao InventoryFormLogDao;
	
	@Resource
	private StockGoodsDao stockGoodsDao;
	
	@Resource
	private ProductDao productDao;
	
	@Override
	public Page<InventoryForm> findPage(Pageable pageable, String inventoryCode, Shop shop, SfIfStatus status) {
		return inventoryFormDao.findPage(pageable, inventoryCode, shop, status);
	}

	private String generateCode(Member member) {
		
		String inventoryCode = "";
		
		InventoryForm inventoryForm = null;
		
		// 获取当天生成的盘点单数
		Long count = inventoryFormDao.getCount(member);
			
		// 获取年月日
		String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String month = new SimpleDateFormat("MM",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String day = new SimpleDateFormat("dd",Locale.CHINESE).format(Calendar.getInstance().getTime());
			
		// 补充0
		String str = String.format("%06d", count);
			
		inventoryCode = "PD"+yearLast+month+day+str;
			
		inventoryForm = inventoryFormDao.findByInventoryCode(inventoryCode, member);
		if (inventoryForm != null) {
			str = String.format("%06d", count++);
			inventoryCode = "PD"+yearLast+month+day+str;
		}
		
		return inventoryCode;
	}

	@Override
	public boolean save(InventoryForm inventoryForm, List<InventoryGoods> inventoryGoods) {
		try {
			// 盘点单
			inventoryForm.setInventoryCode(this.generateCode(inventoryForm.getMember()));
			inventoryFormDao.persist(inventoryForm);
			
			// 操作记录
			InventoryFormLog inventoryFormLog = new InventoryFormLog();
			inventoryFormLog.setRecord(InventoryFormLog.Record.create);
			inventoryFormLog.setMember(inventoryForm.getMember());
			inventoryFormLog.setShop(inventoryForm.getShop());
			inventoryFormLog.setInventoryForm(inventoryForm);
			InventoryFormLogDao.persist(inventoryFormLog);
			
			// 盘点商品关系
			if (inventoryGoods.size() > 0) {
				for (InventoryGoods inventory : inventoryGoods) {
					inventory.setProduct(productDao.find(inventory.getProduct().getId()));
					inventory.setInventoryForm(inventoryForm);
					inventoryGoodsDao.persist(inventory);
					
					if (inventoryForm.getStatus() == SfIfStatus.completed) {
						// 修改库存状况盘点库存
						StockGoods stockGoods = stockGoodsDao.findByProduct(inventory.getProduct(), inventoryForm.getShop());
						stockGoods.setActualStock(inventory.getInventoryStock());
						stockGoods.setStatus(StockGoods.Status.display);
						inventoryFormLog.setRecord(InventoryFormLog.Record.completed);
						stockGoodsDao.merge(stockGoods);
						
						// 操作记录
						InventoryFormLog inventoryFormLogCom = new InventoryFormLog();
						inventoryFormLogCom.setRecord(InventoryFormLog.Record.completed);
						inventoryFormLogCom.setMember(inventoryForm.getMember());
						inventoryFormLogCom.setShop(inventoryForm.getShop());
						inventoryFormLogCom.setInventoryForm(inventoryForm);
						InventoryFormLogDao.persist(inventoryFormLogCom);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Transactional
	@Override
	public boolean update(InventoryForm inventoryForm, List<InventoryGoods> inventoryGoods) {
		try {
			// 删除盘点单商品关联
			inventoryGoodsDao.delete(inventoryForm.getId());
			
			// 修改盘点单信息
			InventoryForm entity = this.find(inventoryForm.getId());
			entity.setRemarks(inventoryForm.getRemarks());
			entity.setStatus(inventoryForm.getStatus());
			this.update(entity);
			
			InventoryFormLog inventoryFormLog = new InventoryFormLog();
			
			// 重新建立盘点单商品关联
			for (InventoryGoods inventory : inventoryGoods) {
				inventory.setProduct(productDao.find(inventory.getProduct().getId()));
				inventory.setInventoryForm(entity);
				inventoryGoodsDao.persist(inventory);
				
				if (inventoryForm.getStatus() == SfIfStatus.completed) {
					// 修改库存状况盘点库存
					StockGoods stockGoods = stockGoodsDao.findByProduct(inventory.getProduct(), inventoryForm.getShop());
					stockGoods.setActualStock(inventory.getInventoryStock());
					stockGoods.setStatus(StockGoods.Status.display);
					stockGoodsDao.merge(stockGoods);
					
					inventoryFormLog.setRecord(InventoryFormLog.Record.completed);
				}else {
					inventoryFormLog.setRecord(InventoryFormLog.Record.edit);
				}
			}
			
			// 操作记录
			inventoryFormLog.setMember(inventoryForm.getMember());
			inventoryFormLog.setShop(inventoryForm.getShop());
			inventoryFormLog.setInventoryForm(entity);
			InventoryFormLogDao.persist(inventoryFormLog);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean exists(Shop shop, SfIfStatus sfIfStatus) {
		return inventoryFormDao.exists(shop, sfIfStatus);
	}

	@Transactional
	@Override
	public boolean cancel(InventoryForm inventoryForm, Member member) {

		inventoryForm.setStatus(SfIfStatus.canceled);
		this.update(inventoryForm);
		
		// 操作记录
		InventoryFormLog inventoryFormLog = new InventoryFormLog();
		inventoryFormLog.setMember(member);
		inventoryFormLog.setRecord(InventoryFormLog.Record.canceled);
		inventoryFormLog.setShop(inventoryForm.getShop());
		inventoryFormLog.setInventoryForm(inventoryForm);
		InventoryFormLogDao.persist(inventoryFormLog);
		return false;
	}

}