package com.microBusiness.manage.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.CategoryCenterDao;
import com.microBusiness.manage.dao.CategoryCenterImportDao;
import com.microBusiness.manage.dao.GoodsCenterDao;
import com.microBusiness.manage.dao.GoodsCenterImportImageInfoDao;
import com.microBusiness.manage.dao.GoodsCenterImportInfoDao;
import com.microBusiness.manage.dao.GoodsCenterImportLogDao;
import com.microBusiness.manage.dao.ProductCategoryDao;
import com.microBusiness.manage.dao.ProductCenterDao;
import com.microBusiness.manage.dao.ProductCenterImportDao;
import com.microBusiness.manage.dao.SnDao;
import com.microBusiness.manage.dao.SpecificationCenterDao;
import com.microBusiness.manage.dao.SpecificationCenterImportDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.CategoryCenterImport;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo.Second;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;
import com.microBusiness.manage.entity.ImportError;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.ProductCenterImport;
import com.microBusiness.manage.entity.ProductImport;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.SpecificationCenter;
import com.microBusiness.manage.entity.SpecificationCenterImport;
import com.microBusiness.manage.entity.SpecificationItem;
import com.microBusiness.manage.entity.SpecificationItem.Entry;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.service.GoodsCenterService;
import com.microBusiness.manage.service.ProductImageService;
import com.microBusiness.manage.service.SpecificationValueService;
import com.microBusiness.manage.util.CommonUtils;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午3:26
 * Describe:
 * Update:
 */
@Service
public class GoodsCenterServiceImpl extends BaseServiceImpl<GoodsCenter, Long> implements GoodsCenterService {
    @Resource
    private GoodsCenterDao goodsCenterDao;
    
    @Resource
    private GoodsCenterImportInfoDao goodsCenterImportInfoDao;

    @Resource
    private ProductCenterImportDao productCenterImportDao;
    
    @Resource
    private ProductCenterDao productCenterDao;

    @Resource
    private ProductImageService productImageService;

    @Resource
    private SpecificationValueService specificationValueService;

    @Resource
    private SnDao snDao;
    

    @Resource
    private CategoryCenterImportDao categoryCenterImportDao ;
    
    @Resource
    private SpecificationCenterImportDao specificationCenterImportDao ;
    
    @Resource
    private SpecificationCenterDao specificationCenterDao;
    
    @Resource
    private GoodsCenterImportLogDao goodsCenterImportLogDao;
    
    @Resource
    private ProductCategoryDao productCategoryDao;
    
    @Resource
    private CategoryCenterDao categoryCenterDao;
    
    @Resource
    private GoodsCenterImportImageInfoDao goodsCenterImportImageInfoDao;
    
    private static Logger LOGGER =  LoggerFactory.getLogger(GoodsCenterServiceImpl.class) ;



    @Override
    public Page<GoodsCenter> findPage(GoodsCenter.Type type, CategoryCenter productCategory, Boolean isMarketable, Boolean isList, Boolean isTop, GoodsCenter.OrderType orderType, Pageable pageable) {
        return goodsCenterDao.findPage(type, productCategory, isMarketable, isList, isTop, orderType, pageable);
    }

    @Override
    public GoodsCenter save(GoodsCenter goods, ProductCenter product, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
        }

        product.setIsDefault(true);
        product.setGoodsCenter(goods);
        product.setSpecificationValues(null);

        goods.setPrice(product.getPrice());
        goods.setMarketPrice(product.getMarketPrice());

        goods.setBarCode(product.getBarCode());

        goods.setScore(0F);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Date());
        goods.setMonthHitsDate(new Date());
        goods.setWeekSalesDate(new Date());
        goods.setMonthSalesDate(new Date());
        goods.setGenerateMethod(GoodsCenter.GenerateMethod.eager);
        goods.setSpecificationItems(null);
        goods.setProductCenters(null);
        setValue(goods);
        goodsCenterDao.persist(goods);

        setValue(product);
        productCenterDao.persist(product);
        return goods;
    }

    @Override
    public GoodsCenter save(GoodsCenter goods, List<ProductCenter> products, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(goods.hasSpecification());
        Assert.notEmpty(products);

        final List<SpecificationItem> specificationItems = goods
                .getSpecificationItems();
        if (CollectionUtils.exists(products, new Predicate() {
            private Set<List<Integer>> set = new HashSet<List<Integer>>();

            public boolean evaluate(Object object) {
                ProductCenter product = (ProductCenter) object;
                return product == null
                        || !product.isNew()
                        || !product.hasSpecification()
                        || !set.add(product.getSpecificationValueIds())
                        || !specificationValueService.isValid(
                        specificationItems,
                        product.getSpecificationValues());
            }
        })) {
            throw new IllegalArgumentException();
        }

        ProductCenter defaultProduct = (ProductCenter) CollectionUtils.find(products,
                new Predicate() {
                    public boolean evaluate(Object object) {
                        ProductCenter product = (ProductCenter) object;
                        return product != null && product.getIsDefault();
                    }
                });
        if (defaultProduct == null) {
            defaultProduct = products.get(0);
            defaultProduct.setIsDefault(true);
        }

        for (ProductCenter product : products) {

            if (product.getMarketPrice() == null) {
                product.setMarketPrice(calculateDefaultMarketPrice(product
                        .getPrice()));
            }
            if (product != defaultProduct) {
                product.setIsDefault(false);
            }
            product.setGoodsCenter(goods);
        }

        goods.setPrice(defaultProduct.getPrice());
        goods.setMarketPrice(defaultProduct.getMarketPrice());

        goods.setBarCode(defaultProduct.getBarCode());

        goods.setScore(0F);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Date());
        goods.setMonthHitsDate(new Date());
        goods.setWeekSalesDate(new Date());
        goods.setMonthSalesDate(new Date());
        goods.setGenerateMethod(GoodsCenter.GenerateMethod.eager);

        setValue(goods);
        goodsCenterDao.persist(goods);

        for (ProductCenter product : products) {
            setValue(product);
            productCenterDao.persist(product);
        }

        return goods;
    }

    @Override
    public GoodsCenter update(GoodsCenter goods, ProductCenter product, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(!goods.isNew());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

        GoodsCenter pGoods = goodsCenterDao.find(goods.getId());

        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product
                    .getPrice()));
        }
        product.setIsDefault(true);
        product.setGoodsCenter(pGoods);
        product.setSpecificationValues(null);

        if (pGoods.hasSpecification()) {
            for (ProductCenter pProduct : pGoods.getProductCenters()) {
                productCenterDao.remove(pProduct);
            }

            setValue(product);
            productCenterDao.persist(product);
        } else {
            ProductCenter defaultProduct = pGoods.getDefaultProduct();
            defaultProduct.setPrice(product.getPrice());
            defaultProduct.setBarCode(product.getBarCode());
            defaultProduct.setCost(product.getCost());
            defaultProduct.setMarketPrice(product.getMarketPrice());
        }

        goods.setPrice(product.getPrice());
        goods.setMarketPrice(product.getMarketPrice());

        goods.setBarCode(product.getBarCode());

        setValue(goods);
        copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
                "scoreCount", "hits", "weekHits", "monthHits", "sales",
                "weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
                "weekSalesDate", "monthSalesDate", "generateMethod", "productCenters");
        pGoods.setGenerateMethod(GoodsCenter.GenerateMethod.eager);

        return pGoods;
    }

    @Override
    public GoodsCenter update(GoodsCenter goods, List<ProductCenter> products, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(!goods.isNew());
        Assert.isTrue(goods.hasSpecification());
        Assert.notEmpty(products);

        final List<SpecificationItem> specificationItems = goods
                .getSpecificationItems();
        if (CollectionUtils.exists(products, new Predicate() {
            private Set<List<Integer>> set = new HashSet<List<Integer>>();

            public boolean evaluate(Object object) {
                ProductCenter product = (ProductCenter) object;
                return product == null
                        || !product.isNew()
                        || !product.hasSpecification()
                        || !set.add(product.getSpecificationValueIds())
                        || !specificationValueService.isValid(
                        specificationItems,
                        product.getSpecificationValues());
            }
        })) {
            throw new IllegalArgumentException();
        }

        ProductCenter defaultProduct = (ProductCenter) CollectionUtils.find(products,
                new Predicate() {
                    public boolean evaluate(Object object) {
                        ProductCenter product = (ProductCenter) object;
                        return product != null && product.getIsDefault();
                    }
                });
        if (defaultProduct == null) {
            defaultProduct = products.get(0);
            defaultProduct.setIsDefault(true);
        }

        GoodsCenter pGoods = goodsCenterDao.find(goods.getId());
        for (ProductCenter product : products) {

            if (product.getMarketPrice() == null) {
                product.setMarketPrice(calculateDefaultMarketPrice(product
                        .getPrice()));
            }
            if (product != defaultProduct) {
                product.setIsDefault(false);
            }
            product.setGoodsCenter(pGoods);
        }

        if (pGoods.hasSpecification()) {
            for (ProductCenter pProduct : pGoods.getProductCenters()) {
                if (!exists(products, pProduct.getSpecificationValueIds())) {
                    productCenterDao.remove(pProduct);
                }
            }
            for (ProductCenter product : products) {
                ProductCenter pProduct = find(pGoods.getProductCenters(),
                        product.getSpecificationValueIds());
                if (pProduct != null) {
                    pProduct.setPrice(product.getPrice());
                    pProduct.setCost(product.getCost());
                    pProduct.setMarketPrice(product.getMarketPrice());

                    pProduct.setBarCode(product.getBarCode());

                    pProduct.setIsDefault(product.getIsDefault());
                    pProduct.setSpecificationValues(product
                            .getSpecificationValues());
                } else {

                    setValue(product);
                    productCenterDao.persist(product);
                }
            }
        } else {
            productCenterDao.remove(pGoods.getDefaultProduct());
            for (ProductCenter product : products) {
                setValue(product);
                productCenterDao.persist(product);
            }
        }

        goods.setPrice(defaultProduct.getPrice());
        goods.setMarketPrice(defaultProduct.getMarketPrice());

        goods.setBarCode(defaultProduct.getBarCode());

        setValue(goods);
        copyProperties(goods, pGoods, "sn", "type", "score", "totalScore",
                "scoreCount", "hits", "weekHits", "monthHits", "sales",
                "weekSales", "monthSales", "weekHitsDate", "monthHitsDate",
                "weekSalesDate", "monthSalesDate", "generateMethod", "productCenters");
        pGoods.setGenerateMethod(GoodsCenter.GenerateMethod.eager);

        return pGoods;
    }


    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Assert.notNull(price);

        Setting setting = SystemUtils.getSetting();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return defaultMarketPriceScale != null ? setting.setScale(price
                .multiply(new BigDecimal(String
                        .valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
    }


    private void setValue(ProductCenter product) {
        if (product == null) {
            return;
        }

        if (product.isNew()) {
            GoodsCenter goods = product.getGoodsCenter();
            if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
                String sn;
                int i = product.hasSpecification() ? 1 : 0;
                do {
                    sn = goods.getSn() + (i == 0 ? "" : "_" + i);
                    i++;
                } while (productCenterDao.snExists(sn));
                product.setSn(sn);
            }
        }
    }

    private void setValue(CategoryCenter productCategory) {
		if (productCategory == null) {
			return;
		}
		CategoryCenter parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId()
					+ ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}
    
    
    private void setValue(GoodsCenter goods) {
        if (goods == null) {
            return;
        }

        productImageService.generate(goods.getProductImages());
        if (StringUtils.isEmpty(goods.getImage())
                && StringUtils.isNotEmpty(goods.getThumbnail())) {
            goods.setImage(goods.getThumbnail());
        }
        if (goods.isNew()) {
            if (StringUtils.isEmpty(goods.getSn())) {
                String sn;
                do {
                    sn = snDao.generate(Sn.Type.goods);
                } while (snExists(sn));
                goods.setSn(sn);
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean snExists(String sn) {
        return goodsCenterDao.snExists(sn);
    }


    private ProductCenter find(Collection<ProductCenter> products,
                         final List<Integer> specificationValueIds) {
        if (CollectionUtils.isEmpty(products)
                || CollectionUtils.isEmpty(specificationValueIds)) {
            return null;
        }

        return (ProductCenter) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                ProductCenter product = (ProductCenter) object;
                return product != null
                        && product.getSpecificationValueIds() != null
                        && product.getSpecificationValueIds().equals(
                        specificationValueIds);
            }
        });
    }

    private boolean exists(Collection<ProductCenter> products,
                           final List<Integer> specificationValueIds) {
        return find(products, specificationValueIds) != null;
    }


    /**
     * 处理商品上传
     *
     * @param multipartFile
     * @param admin
     * @return
     */
    @Override
    public GoodsCenterImportLog dealMoreGoods(MultipartFile multipartFile, Admin admin) {
        GoodsCenterImportLog goodsImportLog = new GoodsCenterImportLog();

        try {
            Workbook wb;
            // excel 版本区别
            if (multipartFile.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
                wb = new HSSFWorkbook(multipartFile.getInputStream());
            } else {
                wb = new XSSFWorkbook(multipartFile.getInputStream());
            }

            Sheet sheet = wb.getSheetAt(0);
            int rowLen = sheet.getPhysicalNumberOfRows();
            int successNum = 0;
            int errorNum = 0;

            /*if (!isthisExcel(sheet)) {
                return null;
            }*/

            goodsImportLog.setTotal(rowLen - 2);

            goodsImportLog.setAdmin(admin);

//            List<GoodsCenterImportInfo> GoodsCenterImportInfos = new ArrayList<>();
            
            //List<ProductImport> products = new ArrayList<>();

            Map<String, GoodsCenterImportInfo> exitsGoods = new HashMap<>();
            Map<String, GoodsCenterImportInfo> exitsGoodsSn = new HashMap<>();

            for (int i = 2; i < rowLen; i++) {
            	List<ProductCenterImport> products = new ArrayList<>();
            	
                GoodsCenterImportInfo importInfo = new GoodsCenterImportInfo();
                List<ImportError> errors = new ArrayList<>();

                Row row = sheet.getRow(i);

                // 需要处理类型
                Cell snCell = row.getCell(0);
                if (snCell != null) {
                    snCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String sn = CommonUtils.getCellValueStr(snCell);

                Cell nameCell = row.getCell(1);
                if (nameCell != null) {
                	nameCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String name = CommonUtils.getCellValue(nameCell);

                //
                Cell oneProductCategoryCell = row.getCell(2);
                if (oneProductCategoryCell != null) {
                    oneProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String oneProductCategory = CommonUtils.getCellValue(oneProductCategoryCell);

                Cell twoProductCategoryCell = row.getCell(3);
                if (twoProductCategoryCell != null) {
                    twoProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String twoProductCategory = CommonUtils.getCellValue(twoProductCategoryCell);

                Cell threeProductCategoryCell = row.getCell(4);
                if (threeProductCategoryCell != null) {
                    threeProductCategoryCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String threeProductCategory = CommonUtils.getCellValue(threeProductCategoryCell);


                // 内包装数量
                Cell packagesNumCell = row.getCell(5);
                String packagesNumStr = CommonUtils.getCellValueStr(packagesNumCell);

                // 保质期
                Cell shelfLifeCell = row.getCell(6);
                String shelfLifeStr = CommonUtils.getCellValueStr(shelfLifeCell);

                // 基本单位
                Cell unitCell = row.getCell(7);
                String unit = CommonUtils.getCellValue(unitCell);

                // 保存条件
				Cell storageConditionsCell = row.getCell(8);
				String storageConditions = CommonUtils.getCellValue(storageConditionsCell);
                
				// 商品性质
				Cell natureCell = row.getCell(9);
				String nature = CommonUtils.getCellValue(natureCell);

                // 体积
                Cell volumeCell = row.getCell(10);
                String volumeStr = CommonUtils.getCellValue(volumeCell);

                // 体积单位
                Cell volumeUnitCell = row.getCell(11);
                String volumeUnit = CommonUtils.getCellValue(volumeUnitCell);

                //条码
                Cell barCodeCell = row.getCell(12);
                String barCode = CommonUtils.getCellValueStr(barCodeCell) ;

                // 成本价
                Cell costCell = row.getCell(13);
                String costStr = CommonUtils.getCellValue(costCell);

                // 销售价
                Cell priceCell = row.getCell(14);
                String priceStr = CommonUtils.getCellValue(priceCell);

                Cell specificationNameCell = row.getCell(15);
                if (specificationNameCell != null) {
                    specificationNameCell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String specificationName = CommonUtils.getCellValue(specificationNameCell);

                Cell specificationOption1Cell = row.getCell(16);
                if (specificationOption1Cell != null) {
                    specificationOption1Cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String specification1Option = CommonUtils.getCellValue(specificationOption1Cell);

                //条码
                Cell barCode1Cell1 = row.getCell(17);
                String barCode1 = CommonUtils.getCellValueStr(barCode1Cell1) ;

                // 成本价
                Cell costCell1 = row.getCell(18);
                String costStr1 = CommonUtils.getCellValue(costCell1);

                // 销售价
                Cell priceCell1 = row.getCell(19);
                String priceStr1 = CommonUtils.getCellValue(priceCell1);

                Cell specificationOption2Cell = row.getCell(20);
                if (specificationOption2Cell != null) {
                    specificationOption2Cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String specification2Option = CommonUtils.getCellValue(specificationOption2Cell);

                //条码
                Cell barCode1Cell2 = row.getCell(21);
                String barCode2 = CommonUtils.getCellValueStr(barCode1Cell2) ;

                // 成本价
                Cell costCell2 = row.getCell(22);
                String costStr2 = CommonUtils.getCellValue(costCell2);

                // 销售价
                Cell priceCell2 = row.getCell(23);
                String priceStr2 = CommonUtils.getCellValue(priceCell2);

                Cell specificationOption3Cell = row.getCell(24);
                if (specificationOption3Cell != null) {
                    specificationOption3Cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String specification3Option = CommonUtils.getCellValue(specificationOption3Cell);

                //条码
                Cell barCodeCell3 = row.getCell(25);
                String barCode3 = CommonUtils.getCellValueStr(barCodeCell3) ;

                // 成本价
                Cell costCell3 = row.getCell(26);
                String costStr3 = CommonUtils.getCellValue(costCell3);

                // 销售价
                Cell priceCell3 = row.getCell(27);
                String priceStr3 = CommonUtils.getCellValue(priceCell3);

                boolean vaild = true;

                if (StringUtils.isNotEmpty(sn)) {
                    if (exitsGoodsSn.containsKey(sn)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("sn");
                                this.setErrorInfo("excel商品编号重复");
                            }
                        });
                        vaild = false;

                        GoodsCenterImportInfo exits = exitsGoodsSn.get(sn);

                        if (exits.getValid()) {
                            exits.setValid(false);
                            exits.getErrors().add(new ImportError() {
                                {
                                    this.setErrorField("sn");
                                    this.setErrorInfo("excel商品编号重复");
                                }
                            });
                            errorNum++;
                            successNum--;
                        }

                    } else {
                        exitsGoodsSn.put(name, importInfo);
                        // 判断商品是否重复
                        Boolean flag = goodsCenterDao.snExists(sn);
                        if (flag) {
                            errors.add(new ImportError() {
                                {
                                    this.setErrorField("sn");
                                    this.setErrorInfo("系统中已存商品编号");
                                }
                            });
                            vaild = false;
                        }
                    }
                }

                if (!StringUtils.isEmpty(sn)) {
                	if (CommonUtils.strTest(sn)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("sn");
                                this.setErrorInfo("商品编号带有特殊符号");
                            }
                        });
                        vaild = false;
                    }
				}
                
                if (StringUtils.isEmpty(oneProductCategory)) {
                    errors.add(new ImportError() {
                        {
                            this.setErrorField("oneProductCategory");
                            this.setErrorInfo("一级分类为空");
                        }
                    });
                    vaild = false;
                } else {
                    if (CommonUtils.strTest(oneProductCategory)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("oneProductCategory");
                                this.setErrorInfo("一级分类包含特殊符号");
                            }
                        });
                        vaild = false;
                    }
                }

                if (!StringUtils.isEmpty(twoProductCategory)) {
                    if (CommonUtils.strTest(twoProductCategory)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("twoProductCategory");
                                this.setErrorInfo("二级分类包含特殊符号");
                            }
                        });
                        vaild = false;
                    }
                }

                if (!StringUtils.isEmpty(threeProductCategory)) {
                    if (CommonUtils.strTest(threeProductCategory)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("threeProductCategory");
                                this.setErrorInfo("三级分类包含特殊符号");
                            }
                        });
                        vaild = false;
                    }
                }

                if (StringUtils.isEmpty(name)) {
                    errors.add(new ImportError() {
                        {
                            this.setErrorField("name");
                            this.setErrorInfo("商品名称为空");
                        }
                    });
                    vaild = false;
                } else {
                    // 如果发现重复商品名称，需要将重复的商品名称处理为失败
                    if (exitsGoods.containsKey(name)) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("name");
                                this.setErrorInfo("excel商品名称重复");
                            }
                        });
                        vaild = false;

                        GoodsCenterImportInfo exits = exitsGoods.get(name);

                        if (exits.getValid()) {
                            exits.setValid(false);
                            ImportError ImportError = new ImportError();
                            ImportError.setErrorField("name");
                            ImportError.setErrorInfo("excel商品名称重复");
                            exits.getErrors().add(ImportError);
                            errorNum++;
                            successNum--;
                        }
                    } else {
                        exitsGoods.put(name, importInfo);
                        // 判断商品是否重复
                        boolean isExist = goodsCenterDao.findByName(name);
                        if (isExist) {
                            errors.add(new ImportError() {
                                {
                                    this.setErrorField("name");
                                    this.setErrorInfo("系统中已存该商品");
                                }
                            });
                            vaild = false;
                        }
                    }
                }
                BigDecimal cost = new BigDecimal(0);
                if (!StringUtils.isEmpty(costStr)) {
                    try {
                        cost = BigDecimal.valueOf(Double.parseDouble(costStr));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("cost");
                                this.setErrorInfo("成本价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal price = new BigDecimal(0);
                if (!StringUtils.isEmpty(priceStr)) {
                    try {
                        price = BigDecimal.valueOf(Double.parseDouble(priceStr));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("price");
                                this.setErrorInfo("销售价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal cost1 = new BigDecimal(0);
                if (!StringUtils.isEmpty(costStr1)) {
                    try {
                    	cost1 = BigDecimal.valueOf(Double.parseDouble(costStr1));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("cost");
                                this.setErrorInfo("成本价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal price1 = new BigDecimal(0);
                if (!StringUtils.isEmpty(priceStr1)) {
                    try {
                    	price1 = BigDecimal.valueOf(Double.parseDouble(priceStr1));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("price");
                                this.setErrorInfo("销售价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                
                BigDecimal cost2 = new BigDecimal(0);
                if (!StringUtils.isEmpty(costStr2)) {
                    try {
                    	cost2 = BigDecimal.valueOf(Double.parseDouble(costStr2));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("cost");
                                this.setErrorInfo("成本价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal price2 = new BigDecimal(0);
                if (!StringUtils.isEmpty(priceStr2)) {
                    try {
                    	price2 = BigDecimal.valueOf(Double.parseDouble(priceStr2));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("price");
                                this.setErrorInfo("销售价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                
                BigDecimal cost3 = new BigDecimal(0);
                if (!StringUtils.isEmpty(costStr3)) {
                    try {
                    	cost3 = BigDecimal.valueOf(Double.parseDouble(costStr3));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("cost");
                                this.setErrorInfo("成本价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal price3 = new BigDecimal(0);
                if (!StringUtils.isEmpty(priceStr3)) {
                    try {
                    	price3 = BigDecimal.valueOf(Double.parseDouble(priceStr3));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("price");
                                this.setErrorInfo("销售价无效");
                            }
                        });
                        vaild = false;
                    }
                }
                
                Long packagesNum = 0L;
                if (!StringUtils.isEmpty(packagesNumStr)) {
                    try {
                        packagesNum = Long.parseLong(packagesNumStr);
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("packagesNumStr");
                                this.setErrorInfo("内包装数量无效");
                            }
                        });
                        vaild = false;
                    }
                }
                Long shelfLife = 0L;
                if (!StringUtils.isEmpty(shelfLifeStr)) {
                    try {
                        shelfLife = Long.parseLong(shelfLifeStr);
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("shelfLifeStr");
                                this.setErrorInfo("保质期无效");
                            }
                        });
                        vaild = false;
                    }
                }
                BigDecimal volume = new BigDecimal(0);
                if (!StringUtils.isEmpty(volumeStr)) {
                    try {
                        volume = BigDecimal.valueOf(Double
                                .parseDouble(volumeStr));
                    } catch (NumberFormatException e) {
                        errors.add(new ImportError() {
                            {
                                this.setErrorField("volume");
                                this.setErrorInfo("体积值无效");
                            }
                        });
                        vaild = false;
                    }
                }

                Boolean specificationNameFlag = false;
                if (StringUtils.isEmpty(specificationName)) {
                	specificationNameFlag = true;
				}
                if (!StringUtils.isEmpty(specification1Option) 
                		|| !StringUtils.isEmpty(barCode1) 
                		|| !StringUtils.isEmpty(costStr1)
                		|| !StringUtils.isEmpty(priceStr1)
                		|| !StringUtils.isEmpty(specification2Option) 
                		|| !StringUtils.isEmpty(barCode2) 
                		|| !StringUtils.isEmpty(costStr2)
                		|| !StringUtils.isEmpty(priceStr2)
                		|| !StringUtils.isEmpty(specification3Option) 
                		|| !StringUtils.isEmpty(barCode3) 
                		|| !StringUtils.isEmpty(costStr3)
                		|| !StringUtils.isEmpty(priceStr3)) {
					
                	if (specificationNameFlag) {
                		errors.add(new ImportError() {
                            {
                                this.setErrorField("name");
                                this.setErrorInfo("规格名称为空");
                            }
                        });
                        vaild = false;
					}
				}
                
                if (!StringUtils.isEmpty(barCode1) 
                		|| !StringUtils.isEmpty(costStr1)
                		|| !StringUtils.isEmpty(priceStr1)) {
					
                	if (StringUtils.isEmpty(specification1Option)) {
                		errors.add(new ImportError() {
                            {
                                this.setErrorField("name");
                                this.setErrorInfo("规格1为空");
                            }
                        });
                        vaild = false;
					}
				}
                
                if (!StringUtils.isEmpty(barCode2) 
                		|| !StringUtils.isEmpty(costStr2)
                		|| !StringUtils.isEmpty(priceStr2)) {
					
                	if (StringUtils.isEmpty(specification2Option)) {
						errors.add(new ImportError() {
                            {
                                this.setErrorField("name");
                                this.setErrorInfo("规格2为空");
                            }
                        });
                        vaild = false;
					}
				}
                
                if (!StringUtils.isEmpty(barCode3) 
                		|| !StringUtils.isEmpty(costStr3)
                		|| !StringUtils.isEmpty(priceStr3)) {
					
                	if (StringUtils.isEmpty(specification3Option)) {
						errors.add(new ImportError() {
                            {
                                this.setErrorField("name");
                                this.setErrorInfo("规格3为空");
                            }
                        });
                        vaild = false;
					}
				}
                
                //处理规格
                CategoryCenterImport oneCategory = new CategoryCenterImport();
                CategoryCenterImport twoCategory = new CategoryCenterImport();
                CategoryCenterImport threeCategory = new CategoryCenterImport();
                // 判断一级菜单是否已存在
                if (!oneProductCategory.equals("")) {
                    List<CategoryCenterImport> list = categoryCenterImportDao.findByParent(null, oneProductCategory);
                    if (list.size() > 0) {
                        oneCategory = list.get(0);
                    } else {
                        CategoryCenterImport productCategory = new CategoryCenterImport();
                        productCategory.setName(oneProductCategory);
                        productCategory.setGrade(0);
                        oneCategory = saveProductCategory(productCategory);
                    }
                } else {
                    List<CategoryCenterImport> list = categoryCenterImportDao.findByParent( null, " ");
                    if (list.size() > 0) {
                        oneCategory = list.get(0);
                    } else {
                        CategoryCenterImport productCategory = new CategoryCenterImport();
                        productCategory.setName(oneProductCategory);
                        productCategory.setGrade(0);
                        oneCategory = saveProductCategory(productCategory);
                    }
                }
                if (!twoProductCategory.equals("")) {
                    List<CategoryCenterImport> list = categoryCenterImportDao.findByParent(oneCategory,
                                    twoProductCategory);
                    if (list.size() > 0) {
                        twoCategory = list.get(0);
                    } else {
                        CategoryCenterImport productCategory = new CategoryCenterImport();
                        productCategory.setName(twoProductCategory);
                        productCategory.setGrade(1);
                        productCategory.setParent(oneCategory);
                        twoCategory = saveProductCategory(productCategory);
                    }
                }

                // 如果二级分类为空，三级变二级
                if (!threeProductCategory.equals("")) {
                    if (!twoProductCategory.equals("")) {
                        List<CategoryCenterImport> list = categoryCenterImportDao.findByParent(twoCategory, threeProductCategory);
                        if (list.size() > 0) {
                            threeCategory = list.get(0);
                        } else {
                            CategoryCenterImport productCategory = new CategoryCenterImport();
                            productCategory.setName(threeProductCategory);
                            productCategory.setGrade(2);
                            productCategory.setParent(twoCategory);
                            threeCategory = saveProductCategory(productCategory);
                        }
                    } else {
                        List<CategoryCenterImport> list = categoryCenterImportDao
                                .findByParent( oneCategory,
                                        threeProductCategory);
                        if (list.size() > 0) {
                            twoCategory = list.get(0);
                        } else {
                            CategoryCenterImport productCategory = new CategoryCenterImport();
                            productCategory.setName(threeProductCategory);
                            productCategory.setGrade(1);
                            productCategory.setParent(oneCategory);
                            twoCategory = saveProductCategory(productCategory);
                        }
                    }
                }
                List<String> options = new ArrayList<>();
                if (!specification1Option.isEmpty()) {
                	options.add(specification1Option);
				}
                if (!specification2Option.isEmpty()) {
                	options.add(specification2Option);
				}
                if (!specification3Option.isEmpty()) {
                	options.add(specification3Option);
				}
                SpecificationCenterImport specification = new SpecificationCenterImport();
                specification.setOptions(options);
                specification.setName(specificationName);

                // 安置规格到最后一个分类
                if (threeProductCategory.equals("")) {
                    if (twoProductCategory.equals("")) {
                    	importInfo.setCategoryCenterImport(oneCategory);
                        specification = saveSpecification(specification, oneCategory, options);
                    } else {
                        importInfo.setCategoryCenterImport(twoCategory);
                        specification = saveSpecification(specification, twoCategory, options);
                    }
                } else {
                    if (twoProductCategory.equals("")) {
                        importInfo.setCategoryCenterImport(twoCategory);
                        specification = saveSpecification(specification, twoCategory, options);
                    } else {
                        importInfo.setCategoryCenterImport(threeCategory);
                        specification = saveSpecification(specification, threeCategory, options);
                    }

                }

                importInfo.setName(name);

                Map<String, String> map = new HashMap<>();
                
                // 组建规格格式
//                ProductCenterImport product1 = new ProductCenterImport();
//                ProductCenterImport product2 = new ProductCenterImport();
//                ProductCenterImport product3 = new ProductCenterImport();
//                if (!specification1Option.isEmpty()) {
//                	product1 = new ProductCenterImport();
//                    product1.setBarCode(barCode1);
//                    product1.setIsDefault(true);
//                    product1.setSpecificationValues(null);
//                    product1.setCost(cost1);
//                    product1.setPrice(price1);
//                    
//                    map.put(specification1Option, specification1Option);
//				}
//                
//                if (!specification2Option.isEmpty()) {
//                	 if (!specification1Option.isEmpty()) {
//                		 product2 = new ProductCenterImport();
//                         product2.setBarCode(barCode2);
//                         product2.setIsDefault(true);
//                         product2.setSpecificationValues(null);
//                         product2.setCost(cost2);
//                         product2.setPrice(price2);
//                         
//					 }else {
//						 product1 = new ProductCenterImport();
//                         product1.setBarCode(barCode2);
//                         product1.setIsDefault(true);
//                         product1.setSpecificationValues(null);
//                         product1.setCost(cost2);
//                         product1.setPrice(price2);
//					}
//                	 map.put(specification2Option, specification2Option);
//				}
//                
//                if (!specification3Option.isEmpty()) {
//					if (!specification2Option.isEmpty()) {
//						 	product3 = new ProductCenterImport();
//			                product3.setBarCode(barCode3);
//			                product3.setIsDefault(true);
//			                product3.setSpecificationValues(null);
//			                product3.setCost(cost3);
//			                product3.setPrice(price3);
//					}else {
//						if (!specification1Option.isEmpty()) {
//							 product2 = new ProductCenterImport();
//	                         product2.setBarCode(barCode3);
//	                         product2.setIsDefault(true);
//	                         product2.setSpecificationValues(null);
//	                         product2.setCost(cost3);
//	                         product2.setPrice(price3);
//						}else {
//							 product1 = new ProductCenterImport();
//	                         product1.setBarCode(barCode3);
//	                         product1.setIsDefault(true);
//	                         product1.setSpecificationValues(null);
//	                         product1.setCost(cost3);
//	                         product1.setPrice(price3);
//						}
//					}
//					
//					 map.put(specification3Option, specification3Option);
//				}
               
//                ProductCenterImport product1 = new ProductCenterImport();
//                product1.setBarCode(barCode1);
//                product1.setIsDefault(true);
//                product1.setSpecificationValues(null);
//                product1.setCost(cost1);
//                product1.setPrice(price1);
//                
//                ProductCenterImport product2 = new ProductCenterImport();
//                product2.setBarCode(barCode2);
//                product2.setIsDefault(true);
//                product2.setSpecificationValues(null);
//                product2.setCost(cost2);
//                product2.setPrice(price2);
//                
//                ProductCenterImport product3 = new ProductCenterImport();
//                product3.setBarCode(barCode3);
//                product3.setIsDefault(true);
//                product3.setSpecificationValues(null);
//                product3.setCost(cost3);
//                product3.setPrice(price3);
                
                ProductCenterImport  product1 = null;
                ProductCenterImport  product2 = null;
                ProductCenterImport  product3 = null;
                
				if (StringUtils.isEmpty(specification1Option)) {
					if (StringUtils.isEmpty(specification2Option)) {
						if (!StringUtils.isEmpty(specification3Option)) {
							product1 = new ProductCenterImport();
			                product1.setBarCode(barCode3);
			                product1.setIsDefault(true);
			                product1.setSpecificationValues(null);
			                product1.setCost(cost3);
			                product1.setPrice(price3);
						}
					}else {
						product1 = new ProductCenterImport();
		                product1.setBarCode(barCode2);
		                product1.setIsDefault(true);
		                product1.setSpecificationValues(null);
		                product1.setCost(cost2);
		                product1.setPrice(price2);
						
						if (!StringUtils.isEmpty(specification3Option)) {
							product2 = new ProductCenterImport();
			                product2.setBarCode(barCode3);
			                product2.setIsDefault(true);
			                product2.setSpecificationValues(null);
			                product2.setCost(cost3);
			                product2.setPrice(price3);
						}
					}
				}else {
					product1 = new ProductCenterImport();
	                product1.setBarCode(barCode1);
	                product1.setIsDefault(true);
	                product1.setSpecificationValues(null);
	                product1.setCost(cost1);
	                product1.setPrice(price1);
					
					if (!StringUtils.isEmpty(specification2Option)) {
						product2 = new ProductCenterImport();
		                product2.setBarCode(barCode2);
		                product2.setIsDefault(true);
		                product2.setSpecificationValues(null);
		                product2.setCost(cost2);
		                product2.setPrice(price2);
						
						if (!StringUtils.isEmpty(specification3Option)) {
							product3 = new ProductCenterImport();
			                product3.setBarCode(barCode3);
			                product3.setIsDefault(true);
			                product3.setSpecificationValues(null);
			                product3.setCost(cost3);
			                product3.setPrice(price3);
						}
					}else {
						if (!StringUtils.isEmpty(specification3Option)) {
							product2 = new ProductCenterImport();
			                product2.setBarCode(barCode3);
			                product2.setIsDefault(true);
			                product2.setSpecificationValues(null);
			                product2.setCost(cost3);
			                product2.setPrice(price3);
						}
					}
					
				}
                
                List<SpecificationItem> specificationItems = new ArrayList<>();
                List<Entry> entrys = new ArrayList<>();
                SpecificationItem specificationItem = new SpecificationItem();
                if (!StringUtils.isEmpty(specificationName)) {
                    if (!specification1Option.equals("") || !specification2Option.equals("") || !specification3Option.equals("")) {
                        specificationItem.setName(specificationName);
                        if (!specification1Option.equals("")) {
                            Entry entry = new Entry();
                            entry.setId(0);
                            entry.setValue(specification1Option);
                            entry.setIsSelected(true);
                            entrys.add(entry);
                        }
                        if (!specification2Option.equals("")) {
                            Entry entry = new Entry();
                            if (!specification1Option.equals("")) {
                                entry.setId(1);
                                entry.setIsSelected(true);
                            } else {
                                entry.setId(0);
                                entry.setIsSelected(true);
                            }

                            entry.setValue(specification2Option);

                            entrys.add(entry);
                        }
                        if (!specification3Option.equals("")) {
                            Entry entry = new Entry();

                            if (!specification2Option.equals("") && !specification1Option.equals("")) {
                                entry.setId(2);
                                entry.setIsSelected(true);
                            } else {
                                if (specification1Option.equals("")) {
                                    if (specification2Option.equals("")) {
                                        entry.setId(0);
                                        entry.setIsSelected(true);
                                    } else {
                                    	entry.setId(1);
                                        entry.setIsSelected(true);
									}
                                } else {
                                    if (!specification2Option.equals("")) {
                                        entry.setId(2);
                                        entry.setIsSelected(true);
                                    } else {
                                        entry.setId(1);
                                        entry.setIsSelected(true);
                                    }
                                }
                            }

                            entry.setValue(specification3Option);
                            entrys.add(entry);
                        }
                        specificationItem.setEntries(entrys);
                        specificationItems.add(specificationItem);
                    }
                }

                importInfo.setSpecificationItems(specificationItems);

                for (Entry entry : entrys) {
                	if (entry.getId() == 0) {
                		 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
                		 SpecificationValue specificationValue = new SpecificationValue();
                         specificationValue.setId(entry.getId());
                         specificationValue.setValue(entry.getValue());
                         specificationValues.add(specificationValue);
                         product1.setSpecificationValues(specificationValues);
                         products.add(product1);
					}
                	if (entry.getId() == 1) {
               		 	List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
               		 	SpecificationValue specificationValue = new SpecificationValue();
                        specificationValue.setId(entry.getId());
                        specificationValue.setValue(entry.getValue());
                        specificationValues.add(specificationValue);
                        product2.setSpecificationValues(specificationValues);
                        products.add(product2);
					}
                	if (entry.getId() == 2) {
               		 	List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
               		 	SpecificationValue specificationValue = new SpecificationValue();
                        specificationValue.setId(entry.getId());
                        specificationValue.setValue(entry.getValue());
                        specificationValues.add(specificationValue);
                        product3.setSpecificationValues(specificationValues);
                        products.add(product3);
					}
                }

                importInfo.setSn(sn);
                importInfo.setPrice(price);
                importInfo.setMarketPrice(cost);
                importInfo.setShelfLife(shelfLife);
                importInfo.setUnit(unit(unit));
                importInfo.setStorageConditions(storageConditions(storageConditions));
                importInfo.setNature(nature(nature));
                importInfo.setVolume(volume);
                importInfo.setVolumeUnit(volumeUnit(volumeUnit));
                importInfo.setPackagesNum(packagesNum);
                importInfo.setValid(vaild);
                importInfo.setErrors(errors);

//                GoodsCenterImportInfos.add(importInfo);

                if (vaild) {
                    successNum++;
                } else {
                    errorNum++;
                }
                
                goodsImportLog.setErrorNum(errorNum);
                goodsImportLog.setSuccessNum(successNum);
                goodsCenterImportLogDao.persist(goodsImportLog);

                importInfo.setLog(goodsImportLog);

                setValue(importInfo);
                goodsCenterImportInfoDao.persist(importInfo);

                for (ProductCenterImport productCenterImport : products) {
                   productCenterImport.setGoodsCenter(importInfo);
                   setValue(productCenterImport);
                   productCenterImportDao.persist(productCenterImport);
    			}
            }

//            goodsImportLog.setErrorNum(errorNum);
//            goodsImportLog.setSuccessNum(successNum);
//            goodsCenterImportLogDao.persist(goodsImportLog);
//
//            for (int i = 0; i < GoodsCenterImportInfos.size(); i++) {
//                GoodsCenterImportInfo goodsCenterImportInfo = GoodsCenterImportInfos.get(i);
//
//                goodsCenterImportInfo.setLog(goodsImportLog);
//
//                setValue(goodsCenterImportInfo);
//                goodsCenterImportInfoDao.persist(goodsCenterImportInfo);
//
//                for (ProductCenterImport productCenterImport : products) {
//                	  productCenterImport.setGoodsCenter(goodsCenterImportInfo);
//                      setValue(productCenterImport);
//                      productCenterImportDao.persist(productCenterImport);
//				}
//            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("method:dealImportMore , excel file read error : ", e);
            return null;

        }
        return goodsImportLog;
    }
    
    private CategoryCenterImport saveProductCategory(CategoryCenterImport categoryCenterImport) {
		setValue(categoryCenterImport);
		categoryCenterImportDao.persist(categoryCenterImport);
		return categoryCenterImport;
	}
    
    private void setValue(CategoryCenterImport categoryCenter) {
		if (categoryCenter == null) {
			return;
		}
		CategoryCenterImport parent = categoryCenter.getParent();
		if (parent != null) {
			categoryCenter.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			categoryCenter.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		categoryCenter.setGrade(categoryCenter.getParentIds().length);
	}
    
    private SpecificationCenterImport saveSpecification(SpecificationCenterImport specification, CategoryCenterImport productCategory, List<String> options) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(
				new UniquePredicate(), new Predicate() {
					public boolean evaluate(Object object) {
						String option = (String) object;
						return StringUtils.isNotEmpty(option);
					}
				}));

		// 同分类下同规格，excel规格追加规格
		List<SpecificationCenterImport> list = specificationCenterImportDao.findByName(specification.getName(), productCategory);
		if (list != null) {
			if (list.size() > 0) {
				specification = list.get(0);
				List<String> optionsTshi = new ArrayList<>();
				Map<String, String> map = new HashMap<String, String>();
				for (String str : specification.getOptions()) {
					map.put(str, str);
				}
				for (String str : options) {
					map.put(str, str);
				}
				for (String str : map.keySet()) {
					optionsTshi.add(str);
				}

				specification.setOptions(optionsTshi);
				specificationCenterImportDao.merge(specification);
			} else {
				specification.setProductCategoryImport(productCategory);
				specificationCenterImportDao.persist(specification);
			}
		}

		return specification;
	}

    private GoodsCenter.Unit unit(String unit) {
    	GoodsCenter.Unit un = null;
		switch (unit) {
		case "箱":
			un = GoodsCenter.Unit.box;
			break;

		case "瓶":
			un = GoodsCenter.Unit.bottle;
			break;

		case "袋":
			un = GoodsCenter.Unit.bag;
			break;

		case "盒":
			un = GoodsCenter.Unit.frame;
			break;

		case "包":
			un = GoodsCenter.Unit.pack;
			break;
			
		case "个":
			un = GoodsCenter.Unit.one;
			break;
			
		case "件":
			un = GoodsCenter.Unit.pieces;
			break;
			
		case "捅":
			un = GoodsCenter.Unit.barrel;
			break;
			
		case "份":
			un = GoodsCenter.Unit.copies;
			break;
			
		case "克":
			un = GoodsCenter.Unit.g;
			break;
			
		case "罐":
			un = GoodsCenter.Unit.tank;
			break;
			
		case "升":
			un = GoodsCenter.Unit.rise;
			break;

		case "条":
			un = GoodsCenter.Unit.bar;
			break;
			
		case "只":
			un = GoodsCenter.Unit.only;
			break;
			
		case "卷":
			un = GoodsCenter.Unit.volume;
			break;
			
		case "听":
			un = GoodsCenter.Unit.listen;
			break;
		}

		return un;
	}

    private GoodsCenter.Nature nature(String nature) {
    	if (StringUtils.isEmpty(nature)) {
			return null;
		}
		if (nature.equals("气体")) {
			return GoodsCenter.Nature.gas;
		} else if (nature.equals("固体")) {
			return GoodsCenter.Nature.solid;
		} else {
			return GoodsCenter.Nature.liquid;
		}
	}
    
    private GoodsCenter.VolumeUnit volumeUnit(String volumeUnit) {
    	if (StringUtils.isEmpty(volumeUnit)) {
			return null;
		}
		if (volumeUnit.equals("m³")) {
			return GoodsCenter.VolumeUnit.cubicMeter;
		} else {
			return GoodsCenter.VolumeUnit.cubicCentimeters;
		}
	}

	private Goods.WeightUnit weightUnit(String weightUnit) {
		if (StringUtils.isEmpty(weightUnit)) {
			return null;
		}
		if (weightUnit.equals("g")) {
			return Goods.WeightUnit.g;
		} else {
			return Goods.WeightUnit.kg;
		}
	}
	
	private GoodsCenter.StorageConditions storageConditions(String StorageConditions) {
		if (StringUtils.isEmpty(StorageConditions)) {
			return null;
		}
		if (StorageConditions.equals("常温")) {
			return GoodsCenter.StorageConditions.roomTemperature;
		} else if (StorageConditions.equals("冷藏")) {
			return GoodsCenter.StorageConditions.refrigeration;
		} else {
			return GoodsCenter.StorageConditions.frozen;
		}
	}
	
	private void setValue(GoodsCenterImportInfo goods) {
		if (goods == null) {
			return;
		}

		if (goods.isNew()) {
			if (StringUtils.isEmpty(goods.getSn())) {
				String sn;
				do {
					sn = snDao.generate(Sn.Type.goods);
				} while (snExists(sn));
				goods.setSn(sn);
			}
		}
	}
	
	private void setValue(ProductCenterImport product) {
		if (product == null) {
			return;
		}

		if (product.isNew()) {
			GoodsCenterImportInfo goods = product.getGoodsCenter();
			if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
				String sn;
				int i = product.hasSpecification() ? 1 : 0;
				do {
					sn = goods.getSn() + (i == 0 ? "" : "_" + i);
					i++;
				} while (productCenterImportDao.snExists(sn));
				product.setSn(sn);
			}
		}
	}

	@Override
	public boolean saveMore(GoodsCenterImportLog goodsCenterImportLog, Admin operator) {
		List<GoodsCenterImportInfo> infos = goodsCenterImportInfoDao.findList(goodsCenterImportLog, Boolean.TRUE);

		for (GoodsCenterImportInfo goodsImportInfo : infos) {
			GoodsCenter goods = new GoodsCenter();
			goods.setSn(goodsImportInfo.getSn());
			goods.setName(goodsImportInfo.getName());

			CategoryCenter oneCategory = null;
			CategoryCenter twoCategory = null;
			CategoryCenter threeCategory = null;

			if (goodsImportInfo.getCategoryCenterImport() != null) {
				if (goodsImportInfo.getCategoryCenterImport().getGrade() == 0) {
					// 添加一级分类
					List<CategoryCenter> category = categoryCenterDao.findByParent(null,goodsImportInfo.getCategoryCenterImport().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new CategoryCenter();
						oneCategory.setName(goodsImportInfo.getCategoryCenterImport().getName());
						setValue(oneCategory);
						categoryCenterDao.persist(oneCategory);
					}
					goods.setCategoryCenter(oneCategory);
				}
				if (goodsImportInfo.getCategoryCenterImport().getGrade() == 1) {
					// 添加一级分类
					List<CategoryCenter> category = categoryCenterDao.findByParent(null,goodsImportInfo.getCategoryCenterImport().getParent().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new CategoryCenter();
						oneCategory.setName(goodsImportInfo.getCategoryCenterImport().getParent().getName());
						setValue(oneCategory);
						categoryCenterDao.persist(oneCategory);

					}

					// 添加二级分类
					List<CategoryCenter> categorys = categoryCenterDao.findByParent(oneCategory,goodsImportInfo.getCategoryCenterImport().getName());
					if (categorys.size() > 0) {
						twoCategory = categorys.get(0);
					} else {
						twoCategory = new CategoryCenter();
						twoCategory.setName(goodsImportInfo.getCategoryCenterImport().getName());
						twoCategory.setGrade(goodsImportInfo.getCategoryCenterImport().getGrade());
						twoCategory.setParent(oneCategory);
						setValue(twoCategory);
						categoryCenterDao.persist(twoCategory);
					}

					goods.setCategoryCenter(twoCategory);
				}
				if (goodsImportInfo.getCategoryCenterImport().getGrade() == 2) {
					// 添加一级分类
					List<CategoryCenter> category = categoryCenterDao.findByParent(null,goodsImportInfo.getCategoryCenterImport().getParent().getParent().getName());
					if (category.size() > 0) {
						oneCategory = category.get(0);
					} else {
						oneCategory = new CategoryCenter();
						oneCategory.setName(goodsImportInfo.getCategoryCenterImport().getParent().getParent().getName());
						setValue(oneCategory);
						categoryCenterDao.persist(oneCategory);
					}

					// 添加二级分类
					List<CategoryCenter> categorys = categoryCenterDao.findByParent(oneCategory,goodsImportInfo.getCategoryCenterImport().getParent().getName());
					if (categorys.size() > 0) {
						twoCategory = categorys.get(0);
					} else {
						twoCategory = new CategoryCenter();
						twoCategory.setName(goodsImportInfo.getCategoryCenterImport().getParent().getName());
						twoCategory.setGrade(goodsImportInfo.getCategoryCenterImport().getParent().getGrade());
						twoCategory.setParent(oneCategory);
						setValue(twoCategory);
						categoryCenterDao.persist(twoCategory);
					}

					if (goodsImportInfo.getCategoryCenterImport().getParent().getParent() != null) {
						// 添加三级分类
						List<CategoryCenter> categorysNew = categoryCenterDao.findByParent(twoCategory, goodsImportInfo.getCategoryCenterImport().getName());
						if (categorysNew.size() > 0) {
							threeCategory = categorysNew.get(0);
						} else {
							threeCategory = new CategoryCenter();
							threeCategory.setName(goodsImportInfo.getCategoryCenterImport().getName());
							threeCategory.setGrade(goodsImportInfo.getCategoryCenterImport().getGrade());
							threeCategory.setParent(twoCategory);
							setValue(threeCategory);
							categoryCenterDao.persist(threeCategory);
						}
					}

					goods.setCategoryCenter(threeCategory);
				}
			}

			Set<ProductCenter> products = new HashSet<ProductCenter>();
			SpecificationCenter specification = new SpecificationCenter();
			if (goodsImportInfo.getSpecificationItems().size() > 0) {
				specification.setName(goodsImportInfo.getSpecificationItems().get(0).getName());
				specification.setOptions(specificationCenterImportDao.findByName(specification.getName(), goodsImportInfo.getCategoryCenterImport()).get(0).getOptions());
				
					if (threeCategory == null) {
						if (twoCategory == null) {
							saveSpecification(specification, oneCategory, specification.getOptions());
						} else {
							saveSpecification(specification, twoCategory, specification.getOptions());
						}
					} else {
						if (twoCategory == null) {
							saveSpecification(specification, oneCategory, specification.getOptions());
						} else {
							saveSpecification(specification, threeCategory, specification.getOptions());
						}
					}
			} else {
				 ProductCenter product = new ProductCenter();
				 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
                 product.setPrice(new BigDecimal(0));
                 product.setCost(new BigDecimal(0));
                 product.setIsDefault(true);
                 product.setSpecificationValues(specificationValues);
                 products.add(product);
			}
			
			goods.setPrice(goodsImportInfo.getPrice());
			goods.setMarketPrice(goodsImportInfo.getMarketPrice());
			goods.setWeekHitsDate(new Date());
			goods.setMonthHitsDate(new Date());
			goods.setWeekSalesDate(new Date());
			goods.setMonthSalesDate(new Date());

			Map<String, String> map = new HashMap<>();
			Map<String, ProductCenter> mapProduct = new HashMap<>();
			for (ProductCenterImport productImport : goodsImportInfo.getProducts()) {
				List<SpecificationValue> list = productImport.getSpecificationValues();
				if (list.size() > 0) {
					for (SpecificationValue specificationValue : productImport.getSpecificationValues()) {
						if (StringUtils.isEmpty(map.get(specificationValue.getValue()))) {
							ProductCenter product = new ProductCenter();
							if (specificationValue.getId() == 0) {
								product.setIsDefault(true);
							}else {
								product.setIsDefault(false);
							}
							List<SpecificationValue> specificationValueL = new ArrayList<>();
							specificationValueL.add(specificationValue);
							product.setSpecificationValues(specificationValueL);
							product.setCost(productImport.getCost());
							product.setPrice(productImport.getPrice());
							product.setBarCode(productImport.getBarCode());
							products.add(product);

							map.put(specificationValue.getValue(), specificationValue.getValue());
							mapProduct.put(specificationValue.getValue(), product);
						}
					}
				} else {
					ProductCenter product = new ProductCenter();
					product.setIsDefault(true);
					List<SpecificationValue> specificationValueL = new ArrayList<>();
					product.setSpecificationValues(specificationValueL);
					product.setCost(productImport.getCost());
					product.setBarCode(productImport.getBarCode());
					product.setPrice(productImport.getPrice());
					products.add(product);
				}

			}
			
			List<SpecificationCenter> specificationList = specificationCenterDao.findByName(specification.getName(), goods.getCategoryCenter());
			List<SpecificationItem> specificationItems = new ArrayList<>();
			for (SpecificationCenter specificationCenter : specificationList) {
				List<Entry> entrys = new ArrayList<>();
				SpecificationItem specificationItem = new SpecificationItem();
				int i = 0;
				for (String option : specificationCenter.getOptions()) {
					Entry entry = new Entry();
					entry.setId(i);
					entry.setValue(option);
					
					if (map.get(option) != null) {
						entry.setIsSelected(true);
					}else {
						entry.setIsSelected(false);
					}
					
					entrys.add(entry);
					i++;
				}
				specificationItem.setName(specification.getName());
				specificationItem.setEntries(entrys);
				specificationItems.add(specificationItem);
				
				for (Entry entry : entrys) {
					 if (mapProduct.get(entry.getValue()) != null) {
						 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
						 SpecificationValue specificationValue = new SpecificationValue();
	                     specificationValue.setId(entry.getId());
	                     specificationValue.setValue(entry.getValue());
	                     specificationValues.add(specificationValue);
	                     mapProduct.get(entry.getValue()).setSpecificationValues(specificationValues);
	                     products.add( mapProduct.get(entry.getValue()));
					 } else {
//						 ProductCenter product = new ProductCenter();
//						 List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();
//						 SpecificationValue specificationValue = new SpecificationValue();
//	                     specificationValue.setId(entry.getId());
//                       specificationValue.setValue(entry.getValue());
//	                     specificationValues.add(specificationValue);
//	                     product.setPrice(new BigDecimal(0));
//	                     product.setCost(new BigDecimal(0));
//	                     product.setIsDefault(false);
//	                     product.setSpecificationValues(specificationValues);
//	                     products.add(product);
					 }
					 
				}
			}
			
			goods.setIsList(false);
			goods.setIsMarketable(true);
			goods.setIsTop(false);
			goods.setScore(0F);
			goods.setTotalScore(0L);
			goods.setScoreCount(0L);
			goods.setHits(0L);
			goods.setWeekHits(0L);
			goods.setMonthHits(0L);
			goods.setSales(0L);
			goods.setWeekSales(0L);
			goods.setMonthSales(0L);
			goods.setShelfLife(goodsImportInfo.getShelfLife());
			goods.setUnit(goodsImportInfo.getUnit());
			goods.setWeight(goodsImportInfo.getWeight());
			goods.setWeightUnit(goodsImportInfo.getWeightUnit());
			goods.setStorageConditions(goodsImportInfo.getStorageConditions());
			goods.setNature(goodsImportInfo.getNature());
			goods.setVolume(goodsImportInfo.getVolume());
			goods.setVolumeUnit(goodsImportInfo.getVolumeUnit());
			goods.setPackagesNum(goodsImportInfo.getPackagesNum());
			goods.setIsList(false);
			goods.setIsMarketable(true);
			goods.setUnit(goodsImportInfo.getUnit());
			goods.setIsDelivery(true);
			goods.setMonthHits(0L);
			goods.setIsTop(false);
			goods.setMarketPrice(new BigDecimal(0));
			goods.setType(GoodsCenter.Type.general);
			goods.setPrice(goodsImportInfo.getPrice());
			
			filter(specificationItems);
			goods.setSpecificationItems(specificationItems);
			this.save(goods);

			for (ProductCenter product : products) {
				product.setGoodsCenter(goods);
				setValue(product);
				productCenterDao.persist(product);
			}

		}

		for (GoodsCenterImportInfo goodsCenterImportInfo : infos) {
			if (goodsCenterImportInfo.getSpecificationItems() != null && goodsCenterImportInfo.getSpecificationItems().size()>0) {
				specificationCenterImportDao.delete(goodsCenterImportInfo.getSpecificationItems().get(0).getName(), goodsCenterImportInfo.getCategoryCenterImport());
			}
		}
		
		return true;
	}

	private SpecificationCenter saveSpecification(SpecificationCenter specification, CategoryCenter productCategory, List<String> options) {
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(
				new UniquePredicate(), new Predicate() {
					public boolean evaluate(Object object) {
						String option = (String) object;
						return StringUtils.isNotEmpty(option);
					}
				}));

		List<SpecificationCenter> list = specificationCenterDao.findByName(specification.getName(), productCategory);
		if (list != null) {
			if (list.size() > 0) {
				specification = list.get(0);
				List<String> optionsTshi = new ArrayList<>();
				Map<String, String> map = new HashMap<String, String>();
				for (String str : specification.getOptions()) {
					map.put(str, str);
				}
				for (String str : options) {
					map.put(str, str);
				}
				for (String str : map.keySet()) {
					optionsTshi.add(str);
				}
				specification.setOptions(optionsTshi);
				specificationCenterDao.merge(specification);
			} else {
				specification.setCategoryCenter(productCategory);
				specificationCenterDao.persist(specification);
			}
		}

		return specification;
	}

	public void filter(List<SpecificationItem> specificationItems) {
		CollectionUtils.filter(specificationItems, new Predicate() {
			public boolean evaluate(Object object) {
				SpecificationItem specificationItem = (SpecificationItem) object;
				if (specificationItem == null
						|| StringUtils.isEmpty(specificationItem.getName())) {
					return false;
				}
				CollectionUtils.filter(specificationItem.getEntries(),
						new Predicate() {
							private Set<Integer> idSet = new HashSet<Integer>();
							private Set<String> valueSet = new HashSet<String>();

							public boolean evaluate(Object object) {
								SpecificationItem.Entry entry = (SpecificationItem.Entry) object;
								return entry != null
										&& entry.getId() != null
										&& StringUtils.isNotEmpty(entry
												.getValue())
										&& entry.getIsSelected() != null
										&& idSet.add(entry.getId())
										&& valueSet.add(entry.getValue());
							}
						});
				return CollectionUtils.isNotEmpty(specificationItem
						.getEntries()) && specificationItem.isSelected();
			}
		});
	}

	@Override
	public GoodsCenter findBySn(String sn) {
		return goodsCenterDao.findBySn(sn);
	}

	@Override
	public Map<String, Object> uploadImage(String sn, String url, String batch) {
		System.out.println("进入导入商品方法时间sn(" + sn + ")"+ System.currentTimeMillis());

		Map<String, Object> data = new HashMap<String, Object>();

		GoodsCenterImportImageInfo goodsImportImageInfo = new GoodsCenterImportImageInfo();
		goodsImportImageInfo.setBatch(batch);
		if (CommonUtils.fileNameTest(sn) || CommonUtils.fileNameTestSpace(sn)) {
			// 次图
			String snStr = sn.substring(0, sn.length() - 3).trim();
			GoodsCenter goods = goodsCenterDao.findBySn(snStr);

			GoodsCenterImportImageInfo info = goodsCenterImportImageInfoDao.query(snStr, batch);
			if (info == null) {
				List<Second> seconds = new ArrayList<>();
				Second second = new Second();
				second.setId(0);
				second.setValue(url);
				seconds.add(second);
				goodsImportImageInfo.setSn(snStr);
				goodsImportImageInfo.setImages(seconds);
				goodsImportImageInfo.setName(goods.getName());
				goodsCenterImportImageInfoDao.persist(goodsImportImageInfo);

				goodsCenterImportImageInfoDao.flush();

				LOGGER.info("次图新增sn" + sn);
			} else {
				List<Second> secondM = new ArrayList<>();
				List<Second> seconds = info.getImages();
				if (seconds.size() < 2) {
					Second second = new Second();
					second.setId(1);
					second.setValue(url);
					secondM.addAll(seconds);
					secondM.add(second);
					info.setImages(secondM);
					goodsCenterImportImageInfoDao.merge(info);

					goodsCenterImportImageInfoDao.flush();

					LOGGER.info("次图修改sn" + sn);
				} else {
					data.put("message", "图片上传已超过上限");
					data.put("state", "admin.upload.error");
					return data;
				}
			}
		} else {
			// 主图
			sn = sn.trim();
			GoodsCenter goods = goodsCenterDao.findBySn(sn);

			GoodsCenterImportImageInfo goodsImage = goodsCenterImportImageInfoDao.query(sn, batch);
			goodsImportImageInfo.setImage(url);
			goodsImportImageInfo.setName(goods.getName());
			goodsImportImageInfo.setSn(sn);
			if (goodsImage == null) {
				List<Second> seconds = new ArrayList<>();
				goodsImportImageInfo.setImages(seconds);
				goodsCenterImportImageInfoDao.persist(goodsImportImageInfo);

				goodsCenterImportImageInfoDao.flush();

				LOGGER.info("主图新增sn" + sn);
			} else {
				goodsImage.setImage(url);
				goodsCenterImportImageInfoDao.merge(goodsImage);

				goodsCenterImportImageInfoDao.flush();

				LOGGER.info("主图修改sn" + sn);
			}
		}

		System.out.println("结束导入商品方法时间sn(" + sn + ")"+ System.currentTimeMillis());

		data.put("message", "admin.message.success");
		data.put("state", "SUCCESS");
		data.put("url", url);
		
		return data;
	}

	@Override
	public boolean saveMore(String batch, Admin operator) {
		List<GoodsCenterImportImageInfo> goodsImportImageInfoList = goodsCenterImportImageInfoDao.findList(batch);
		for (GoodsCenterImportImageInfo goodsImportImageInfo : goodsImportImageInfoList) {
			GoodsCenter goods = goodsCenterDao.findBySn(goodsImportImageInfo.getSn());
			if (!StringUtils.isEmpty(goodsImportImageInfo.getImage())) {
				goods.setImage(goodsImportImageInfo.getImage());
			}

			List<Second> seconds = goodsImportImageInfo.getImages();
			List<String> str = new ArrayList<>();
			if (seconds.size() > 0) {
				if (seconds.size() == 1) {
					if (goods.getImages().size() == 0) {
						str.add(seconds.get(0).getValue());
					}
					if (goods.getImages().size() == 1) {
						str.add(goods.getImages().get(0));
						str.add(seconds.get(0).getValue());
					}
					if (goods.getImages().size() == 2) {
						str.add(seconds.get(0).getValue());
						str.add(goods.getImages().get(1));
					}
				}
				if (seconds.size() == 2) {
					for (Second second : seconds) {
						str.add(second.getValue());
					}
				}
				goods.setImages(str);
			}

			goodsCenterDao.merge(goods);
		}

		return true;
	}

	@Override
	public boolean findByName(String name) {
		return goodsCenterDao.findByName(name);
	}

}
