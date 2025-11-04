package com.ces.village.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里oss配置
 */
@Component
@ConfigurationProperties(prefix = "ces.alioss")
@Data
public class AliOssProperties{
    @Qualifier("aliKeyProperties")
    @Autowired
    private AliKeyProperties Keys;
    private String endPoint;
    private String stsEndPoint;
//    private String accessKeyId;
//    private String accessKeySecret;
    private String bucketName;

    private String roleSessionName;

    private String roleArn;
}
