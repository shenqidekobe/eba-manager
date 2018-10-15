package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午2:55
 * Describe:
 * Update:
 */
public interface GoodsCenterService extends BaseService<GoodsCenter , Long> {

    Page<GoodsCenter> findPage(GoodsCenter.Type type, CategoryCenter productCategory , Boolean isMarketable, Boolean isList, Boolean isTop , GoodsCenter.OrderType orderType, Pageable pageable);

    GoodsCenter save(GoodsCenter goods, ProductCenter product, Admin operator);

    GoodsCenter save(GoodsCenter goods, List<ProductCenter> products, Admin operator);

    GoodsCenter save(GoodsCenter goods);

    GoodsCenter update(GoodsCenter goods, ProductCenter product, Admin operator);

    GoodsCenter update(GoodsCenter goods);

    GoodsCenter update(GoodsCenter goods, List<ProductCenter> products, Admin operator);

    boolean snExists(String sn);

    boolean saveMore(String batch, Admin operator);
    
    GoodsCenter findBySn(String sn);
    
    /**
     * 处理商品上传
     * @param multipartFile
     * @param admin
     * @return
     */
    GoodsCenterImportLog dealMoreGoods(MultipartFile multipartFile , Admin admin);

    boolean saveMore(GoodsCenterImportLog goodsCenterImportLog, Admin operator);

    Map<String, Object> uploadImage(String fileName, String url, String batch);
    
    boolean findByName(String name);
}
