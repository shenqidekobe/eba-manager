package com.microBusiness.manage.job;

/**
 * Created by mingbai on 2017/3/31.
 * 功能描述：
 * 修改记录：
 */

import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.service.SupplierSupplierService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/*@Lazy(false)
@Component*/
public class FormalSupplyJob {
    @Resource
    private SupplierSupplierService supplierSupplierService ;
    /**
     * 定时处理过期的正式供应
     */
    //@Scheduled(cron = "${job.formalSupply.cron}")
    //@PostConstruct
    public void expiredFormalSupply() {
        Date now = new Date() ;
        supplierSupplierService.dealExpiredSupply(now , SupplierSupplier.Status.expired);
        supplierSupplierService.dealWillSupplyToSupply(now);
    }
}
