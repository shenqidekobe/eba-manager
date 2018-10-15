package com.microBusiness.manage.job;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.service.SupplyNeedService;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*@Lazy(false)
@Component*/
public class NeedSupplyJob {
    @Resource
    private SupplyNeedService supplyNeedService ;

    /**
     * 定时处理过期的个体供应
     */
    //@Scheduled(cron = "${job.needSupply.cron}")
    //@PostConstruct
    public void expiredFormalSupply() {
        Date now = new Date() ;
        supplyNeedService.dealExpiredSupply(now , SupplyNeed.Status.EXPIRED);
        supplyNeedService.dealWillSupplyToSupply(now);
    }

}
