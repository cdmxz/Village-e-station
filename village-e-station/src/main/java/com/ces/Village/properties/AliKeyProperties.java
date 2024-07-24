package com.ces.Village.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云key 配置
 */
@Component
@ConfigurationProperties(prefix = "ces.alikey")
@Data
public class AliKeyProperties {
    private String accessKeyId;
    private String accessKeySecret;
}
