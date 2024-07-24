package com.ces;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Log4j2
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开发缓存注解功能
@EnableScheduling//定时器
public class VillageEStationApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillageEStationApplication.class, args);
        log.info("项目启动成功......");
    }
}
