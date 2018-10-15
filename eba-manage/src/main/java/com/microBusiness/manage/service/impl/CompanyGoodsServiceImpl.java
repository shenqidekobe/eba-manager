package com.microBusiness.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.CategoryDao;
import com.microBusiness.manage.dao.CompanyGoodsDao;
import com.microBusiness.manage.dao.FavorCompanyDao;
import com.microBusiness.manage.dao.FavorCompanyGoodsDao;
import com.microBusiness.manage.dao.GoodsDao;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.CompanyGoods.GenerateMethod;
import com.microBusiness.manage.entity.CompanyGoods.PubType;
import com.microBusiness.manage.entity.CompanyGoods.PubfromSource;
import com.microBusiness.manage.entity.CompanyGoods.Status;
import com.microBusiness.manage.entity.CompanyGoods.Type;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.FavorCompanyGoods;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.JsonUtils;

@Service("companyGoodsServiceImpl")
public class CompanyGoodsServiceImpl extends BaseServiceImpl<CompanyGoods, Long> implements CompanyGoodsService {

	@Resource(name = "companyGoodsDaoImpl")
	private CompanyGoodsDao companyGoodsDao;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private FavorCompanyDao favorCompanyDao ;
	@Resource
	private FavorCompanyGoodsDao favorCompanyGoodsDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private ProductDao productDao;

	@Override
	@Transactional
	public CompanyGoods saveFromDhm(CompanyGoods companyGoods, String productIds, String categoryIds) {
		try {
			if(productIds != null){
				String[] productArray = productIds.split(",");
				String[] categoryArray = categoryIds.split(",");
				for (int i = 0; i < productArray.length; i++) {
					Product product = productDao.find(Long.valueOf(productArray[i]));
					Goods goods = product.getGoods();
					CompanyGoods companyGoodsEntity = new CompanyGoods();
					companyGoodsEntity.setPubfromSource(PubfromSource.pub_from_dinghuome);
					companyGoodsEntity.setName(goods.getName());
					companyGoodsEntity.setImage(goods.getImage());
					Category category = categoryDao.find(Long.valueOf(categoryArray[i]));
					if(category != null){
						companyGoodsEntity.setCategory(category);
					}
					if(goods.getUnit() != null){
						companyGoodsEntity.setUnit(CompanyGoods.Unit.valueOf(goods.getUnit().name()));
					}
					companyGoodsEntity.setMarketPrice(goods.getMarketPrice());
					companyGoodsEntity.setIntroduction(goods.getIntroduction());
					companyGoodsEntity.setCreateDate(new Date());
					companyGoodsEntity.setDelflag(Delflag.delflag_no);
					companyGoodsEntity.setGenerateMethod(GenerateMethod.none);
					companyGoodsEntity.setHits(goods.getHits());
					companyGoodsEntity.setType(Type.general);
					companyGoodsEntity.setSupplier(companyGoods.getSupplier());
					companyGoodsEntity.setPubType(PubType.pub_supply);
					companyGoodsEntity.setCreateDate(new Date());
					companyGoodsEntity.setDelflag(Delflag.delflag_no);
					companyGoodsEntity.setStatus(Status.status_wait);
					List<SpecificationValue> specItemList = product.getSpecificationValues();
					if(specItemList != null && !specItemList.isEmpty()){
						StringBuffer buf = new StringBuffer();
						for (SpecificationValue specificationValue : specItemList) {
							buf.append(specificationValue.getValue());
							buf.append(" ");
						}
						companyGoodsEntity.setGoodsSpec(buf.toString());
					}
					companyGoodsDao.persist(companyGoodsEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.getClass().getName() + ":[saveFromDhm]", e);
			return null;
		}
		return companyGoods;
	}

	@Override
	public Page<CompanyGoods> getCompanyGoodsList(Admin admin, CompanyGoods companyGoods, Boolean priceBoolean, Boolean popularityBoolean, Boolean packagesNumBoolean, Pageable pageable) {
		Page<CompanyGoods> pageCompanyGoods = companyGoodsDao.query(companyGoods, priceBoolean, popularityBoolean, packagesNumBoolean, pageable);

//		if (admin != null) {
//			for (CompanyGoods companyGoodsNew : pageCompanyGoods.getContent()) {
//				FavorCompany favorCompany = favorCompanyDao.getFavorCompanyIsNull(admin.getId(), companyGoodsNew.getSupplier().getId());
//				if (favorCompany != null) {
//					companyGoodsNew.getSupplier().setFavorCompany(favorCompany);
//				}
//			}
//		}

		return pageCompanyGoods;
	}

	@Override
	public CompanyGoods updateCompanyGoods(Long id) {
		CompanyGoods companyGoods = companyGoodsDao.find(id);
		if (companyGoods != null) {
			companyGoods.setHits(companyGoods.getHits()+1);
			companyGoodsDao.merge(companyGoods);
			return companyGoods;
		}
		return null;
	}

	@Override
	public CompanyGoods findById(Long id, Admin admin) {
		CompanyGoods companyGoods = companyGoodsDao.find(id);
		if (admin != null) {
			FavorCompanyGoods favorCompanyGoods = favorCompanyGoodsDao.getFavorCompanyGoodsIsNull(admin.getId(), companyGoods.getId());
			if (favorCompanyGoods != null) {
				companyGoods.setFavorCompanyGoods(favorCompanyGoods);
			}
		}
		return companyGoods;
	}

	/**
	 * 后台审核列表
	 * @param pageable
	 * @param category 分类
	 * @param status 审核状态
	 * @param searchValue 混合查询的内容
	 * @param pubType
	 * @return
	 */
	@Override
	public Page<CompanyGoods> findPage(Pageable pageable , Category category , CompanyGoods.Status status , String searchValue , CompanyGoods.PubType pubType) {
		return companyGoodsDao.findPage(pageable , category , status , searchValue , pubType);
	}

	/**
	 * @param companyGoods
	 * @param operator
	 * @return
	 */
	@Override
	public CompanyGoods update(CompanyGoods companyGoods, Admin operator) {
		CompanyGoods pGoods = companyGoodsDao.find(companyGoods.getId());

		pGoods.setCategory(companyGoods.getCategory());
		pGoods.setName(companyGoods.getName());
		pGoods.setPubfromSource(companyGoods.getPubfromSource());
		pGoods.setGoodsSpec(companyGoods.getGoodsSpec());
		pGoods.setImage(companyGoods.getImage());
		pGoods.setIntroduction(companyGoods.getIntroduction());
		pGoods.setStorageConditions(companyGoods.getStorageConditions());
		pGoods.setUnit(companyGoods.getUnit());
		pGoods.setSourceType(companyGoods.getSourceType());
		pGoods.setStatus(companyGoods.getStatus());


		if(pGoods.getPubType().equals(PubType.pub_supply)){
			pGoods.setShelfLife(companyGoods.getShelfLife());
			pGoods.setMarketPrice(companyGoods.getMarketPrice());
		}

		if(pGoods.getPubType().equals(PubType.pub_need)){
			pGoods.setNeedNum(companyGoods.getNeedNum());

		}

		return pGoods;
	}


	@Override
	public boolean check(Long[] ids, CompanyGoods.Status status) {
		if(null != ids){
			for(Long id : ids){
				CompanyGoods companyGoods = companyGoodsDao.find(id);
				companyGoods.setStatus(status);
			}
		}
		return true;
	}

	@Override
	public void delete(Long... ids) {
		if(null != ids){
			for(Long id : ids){
				CompanyGoods companyGoods = companyGoodsDao.find(id);
				companyGoods.setDelflag(Delflag.delflag_had);
			}
		}
	}

	/**
	 * @param pageable
	 * @param category
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	@Override
	public List<CompanyGoods> findList(Pageable pageable, Category category, Status status, String searchValue, PubType pubType) {
		return null;
	}

	/**
	 * @param pageable
	 * @param categoryId
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	@Override
	public ExcelView exportSupplyGoods(Pageable pageable, Long categoryId, Status status, String searchValue, PubType pubType) {
		String fileName = "供应列表.xls";
		Category category = categoryDao.find(categoryId);

		List<CompanyGoods> supplyGoods = companyGoodsDao.findList(pageable , category , status , searchValue , CompanyGoods.PubType.pub_supply);
		String[] titles = {"产品分类" , "产品名称" , "货源类型" , "保存条件" , "保质期" , "基本单位" , "产品规格" , "参考价格"} ;
		return new ExcelView(fileName, null, null, titles , null, null, supplyGoods, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				String[] titles = this.getTitles() ;
				int rowNumber = 0;
				//处理表头
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}
				List<CompanyGoods> goods = (List<CompanyGoods>) this.getData();
				//处理数据
				for(CompanyGoods companyGoods : goods){
					HSSFRow dataRow = sheet.createRow(rowNumber);

					dataRow.createCell(0).setCellValue(companyGoods.getCategory().getName());

					dataRow.createCell(1).setCellValue(companyGoods.getName());

					dataRow.createCell(2).setCellValue(companyGoods.getSourceType().getName());

					dataRow.createCell(3).setCellValue(companyGoods.getStorageConditions().getName()) ;

					dataRow.createCell(4).setCellValue(companyGoods.getShelfLife()) ;

					dataRow.createCell(5).setCellValue(companyGoods.getUnit().getName());

					dataRow.createCell(6).setCellValue(companyGoods.getGoodsSpec());

					//参考价格
					dataRow.createCell(7).setCellValue(companyGoods.getTrueMarketPrice()) ;

					rowNumber++ ;
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
						response.setHeader("Content-disposition", "attachment; filename=" + CommonUtils.getExportName(this.getFileName() , request));
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	@Override
	public ExcelView exportPurchaseGoods(Pageable pageable, Long categoryId, Status status, String searchValue, PubType pubType) {

		String fileName = "采购列表.xls";
		Category category = categoryDao.find(categoryId);

		List<CompanyGoods> supplyGoods = companyGoodsDao.findList(pageable , category , status , searchValue , CompanyGoods.PubType.pub_need);
		String[] titles = {"产品分类" , "产品名称" , "货源类型" , "保存条件" , "保质期" , "基本单位" , "产品规格" , "参考价格"} ;
		return new ExcelView(fileName, null, null, titles , null, null, supplyGoods, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				String[] titles = this.getTitles() ;
				int rowNumber = 0;
				//处理表头
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}
				List<CompanyGoods> goods = (List<CompanyGoods>) this.getData();
				//处理数据
				for(CompanyGoods companyGoods : goods){
					HSSFRow dataRow = sheet.createRow(rowNumber);

					dataRow.createCell(0).setCellValue(companyGoods.getCategory().getName());

					dataRow.createCell(1).setCellValue(companyGoods.getName());

					dataRow.createCell(2).setCellValue(companyGoods.getSourceType().getName());

					dataRow.createCell(3).setCellValue(companyGoods.getStorageConditions().getName()) ;

					dataRow.createCell(4).setCellValue(companyGoods.getShelfLife()) ;

					dataRow.createCell(5).setCellValue(companyGoods.getUnit().getName());

					dataRow.createCell(6).setCellValue(companyGoods.getGoodsSpec());

					//参考价格
					dataRow.createCell(7).setCellValue(companyGoods.getTrueMarketPrice()) ;

					rowNumber++ ;
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					response.setHeader("Content-disposition", "attachment; filename=" + CommonUtils.getExportName(this.getFileName() , request));
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	/**
	 * 获取层级分类
	 *
	 * @param category
	 * @return
	 */
	@Override
	public String getCategoryIds(Category category) {
		List<Category> parents = category.getParents() ;
		List<Long> ids = new ArrayList<>(parents.size() + 1 ) ;
		for(Category category1 : parents){
			ids.add(category1.getId()) ;
		}
		ids.add(category.getId()) ;
		return JsonUtils.toJson(ids);
	}
}