package com.ces;

import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Log4j2
@ServletComponentScan
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开发缓存注解功能
@EnableScheduling//定时器
@MapperScan("com.ces.village.mapper") //扫描Mapper接口
public class VillageEStationApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillageEStationApplication.class, args);
        log.info("项目启动成功......");
    }
}
