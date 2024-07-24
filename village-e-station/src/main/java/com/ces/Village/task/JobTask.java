package com.ces.Village.task;

import com.ces.Village.service.EnterpriseHiresJobService;
import com.ces.Village.service.GigHiresJobService;
import com.ces.Village.service.GigHuntingJobService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 就业信息表定时器
 */
@Log4j2
@Component
public class JobTask {
    /**
     * 企业招聘信息表
     */
    @Autowired
    private EnterpriseHiresJobService enterpriseHiresService;
    /**
     * 零工招聘信息表
     */
    @Autowired
    private GigHiresJobService gigHiresService;
    /**
     * 零工求职信息表
     */
    @Autowired
    private GigHuntingJobService gigJobHuntingService;


    /**
     * 每天凌晨1点更新3个就业信息表
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateRecruitmentStatus() {
        enterpriseHiresService.updateDeadlineStatus();
        log.info(LocalDateTime.now() + "更新企业招聘信息表的，到达截止时间的信息，的状态为已下架");
        gigHiresService.updateDeadlineStatus();
        log.info(LocalDateTime.now() + "更新零工招聘信息表的，到达截止时间的信息，的状态为已下架");
        gigJobHuntingService.updateDeadlineStatus();
        log.info(LocalDateTime.now() + "更新零工求职信息表的，到达截止时间的信息，的状态为已下架");
    }
}
