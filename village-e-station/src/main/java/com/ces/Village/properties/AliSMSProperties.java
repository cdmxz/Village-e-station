package com.ces.village.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信配置
 */
@Component
@ConfigurationProperties(prefix = "ces.alisms")
@Data
public class AliSMSProperties{
    // 阿里云短信配置信息
//    private String accessKeyId;
//    private String accessKeySecret;
    @Qualifier("aliKeyProperties")
    @Autowired
    private AliKeyProperties Keys;

    private final String REGION_ID = "cn-shenzhen";
    private final String PRODUCT = "Dysmsapi";
    private final String ENDPOINT = "dysmsapi.aliyuncs.com";
    // 签名
    private String signName;
    // 模板
    private String templateCode;
}
